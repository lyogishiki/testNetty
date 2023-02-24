package netty.pri;



import java.util.HashMap;
import java.util.Map;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		//发送body为空的链接消息
		NettyMessage message = buildLoginReq();
		ctx.writeAndFlush(message);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		//如果是握手消息应答,需要判断认证是否成功
		if(message.getHeader() != null && 
				message.getHeader().getType() == MessageType.LOGIN_RESP.value()){
			byte loginResult = (byte) message.getBody();
			if(loginResult != (byte)0){
				//握手失败
				ctx.close();
			}else{
				System.out.println("login is ok : " + message);
				
				ctx.fireChannelRead(msg);
			}
			
			
		}else{
			//不是握手信息,直接头传给后面的Channelhandler处理
			ctx.fireChannelRead(msg);
		}
	}
	
	//构造空的请求消息
	private NettyMessage buildLoginReq(){
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		Map<String, Object> attachment = new HashMap<String, Object>();
		attachment.put("key_1", "value_1");
		header.setAttachment(attachment);
		header.setType(MessageType.LOGIN_REQ.value());
		message.setHeader(header);
		return message;
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		//?
		ctx.fireExceptionCaught(cause);
	}
	
	

}
