package netty2.pri.client;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty2.pri.MessageType;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage.Header;

@Sharable
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter{

	public static final LoginAuthReqHandler DEFAULT_INSTANCE = new LoginAuthReqHandler();
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		NettyMessage<Object> message = buildLoginReq();
		ctx.writeAndFlush(message);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage<Object> message = (NettyMessage<Object>) msg;
		
		if(message.getHeader() != null && 
				message.getHeader().getType() == MessageType.LOGIN_RESP) {
			
			byte loginResult = (byte) message.getBody();
			
//			System.out.println("login is ok " + message);
			if(isLoginSuccess(loginResult)) {
				//握手成功！
				System.out.println("login is ok : " + message);
				ctx.fireChannelRead(msg);
			}else {
				ctx.close();
			}
		} else {
			//不是握手信息。
			ctx.fireChannelRead(msg);
		}
	}

	private boolean isLoginSuccess(byte result) {
		return result == 0;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		//MYTASK 为什么传递给之后的Handler处理。
//	 	要传递给HeartBeatReqHandler，HeartBeat 需要关闭定时发送的任务。
		ctx.fireExceptionCaught(cause);
	}

	private NettyMessage<Object> buildLoginReq(){
		NettyMessage<Object> message = new NettyMessage<>();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ);
		message.setHeader(header);
		return message;
	}
	
}
