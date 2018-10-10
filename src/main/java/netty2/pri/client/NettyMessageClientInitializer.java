package netty2.pri.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessageDecoder;
import netty2.pri.NettyMessageEncoder;

public class NettyMessageClientInitializer extends ChannelInitializer<Channel> {

	@Override
	protected void initChannel(Channel ch) throws Exception {
		// MYTASK Auto-generated method stub
		
		ch.pipeline()
		//.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4))
//		.addLast(new IdleStateHandler(3, 3, 3))			//和userEventTriggered搭配使用.
		.addLast(new ReadTimeoutHandler(300))
		.addLast(new ProtobufVarint32FrameDecoder())
		.addLast(new ProtobufVarint32LengthFieldPrepender())
		.addLast(new NettyMessageEncoder())
		.addLast(new NettyMessageDecoder(NettyMessage.class))
		.addLast(LoginAuthReqHandler.DEFAULT_INSTANCE)
//		.addLast(new DefaultEventExecutorGroup(1), new HeartBeatReqHandler())
//		Client端的心跳任务应该不用使用新的DefaultEventExecutorGroup，使用IO线程就可以了吧
		.addLast(new HeartBeatReqHandler())
		.addLast(NettyMessageClientHandler.DEFAULT_INSTANCE);
	}

	

}
