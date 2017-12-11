package netty.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOption;


public class HexDumpProxyFrontendHandler extends ChannelInboundHandlerAdapter {

	private final String remoteHost;
	private final int remotePort;
	
	  // As we use inboundChannel.eventLoop() when buildling the Bootstrap this does not need to be volatile as
	// the outboundChannel will use the same EventLoop (and therefore Thread) as the inboundChannel.
	private Channel outboundChannel;

    public HexDumpProxyFrontendHandler(String remoteHost, int remotePort) {
        this.remoteHost = remoteHost;
        this.remotePort = remotePort;
    }
    

    @Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	final Channel inboundChannel = ctx.channel();
    	
    	Bootstrap b = new Bootstrap();
    	b.group(inboundChannel.eventLoop())
    		.channel(ctx.channel().getClass())
    		.handler(new HexDumpProxyBackendHandler(inboundChannel))
    		.option(ChannelOption.AUTO_READ,false);
    	ChannelFuture f = b.connect(remoteHost,remotePort);
    	outboundChannel = f.channel();
    	f.addListener(new ChannelFutureListener() {
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				if(future.isSuccess()){
					inboundChannel.read();
				}else{
					inboundChannel.close();
				}
			}
		});
	}

    @Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    	if(outboundChannel.isActive()){
    		outboundChannel.writeAndFlush(msg).addListener(new ChannelFutureListener(){

				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (future.isSuccess()) {
                        ctx.channel().read();
                    } else {
                        future.channel().close();
                    }
				}
    		});
    	}
    }
    
    @Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    	if(outboundChannel != null){
    		closeOnFlush(outboundChannel);
    	}
    }

    @Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    	cause.printStackTrace();
    	closeOnFlush(ctx.channel());
    }

	/**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}

