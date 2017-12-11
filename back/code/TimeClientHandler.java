package netty.code;

import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeClientHandler extends ChannelInboundHandlerAdapter  {

	
	private final AtomicInteger ai = new AtomicInteger(0);
	
	public TimeClientHandler() {
		super();
		
	}

	//当TCP建立连接后，会调用channelActivite
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		UserInfo[] infos = userInfos();
		for(UserInfo info : infos){
			ctx.write(info);
		}
		ctx.flush();
	}
	
	private UserInfo[] userInfos(){
		UserInfo[] userInfos = new UserInfo[100];
		for(int i=0;i<userInfos.length;i++){
			UserInfo userInfo = new UserInfo();
			userInfo.setUserId(i);
			userInfo.setUserName("HELLO"+i);
			userInfos[i] = userInfo;
		} 
		
		return userInfos;
	} 

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		//ByteBuf readBuf = (ByteBuf) msg;
		//byte[] bytes = new byte[readBuf.readableBytes()];
		//readBuf.readBytes(bytes);
		//String req = new String(bytes,"UTF-8");
		
		//System.out.println(req + ",times:" + ai.incrementAndGet());
		System.out.println(msg);
		ctx.write(msg);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
		cause.printStackTrace();
	}

	
}
