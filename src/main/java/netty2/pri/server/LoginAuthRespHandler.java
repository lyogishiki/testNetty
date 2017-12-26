package netty2.pri.server;

import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelHandler.Sharable;
import netty2.pri.MessageType;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessage.Header;
import netty2.pri.ServerContext;


@Sharable
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter{
	
	public static final LoginAuthRespHandler DEFAULT_INSTANCE = new LoginAuthRespHandler();

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage<Object> message = (NettyMessage<Object>) msg;
		
		if(message.getHeader() != null && 
				message.getHeader().getType() == MessageType.LOGIN_REQ) {
			doLogin(ctx, message);
		} else {
			ctx.fireChannelRead(msg);
		}
	
	}
	
	private void doLogin(ChannelHandlerContext ctx,NettyMessage<Object> msg) {
		Channel channel = ctx.channel();
		NettyMessage<Object> loginResp = null;
		if(ServerContext.isLogin(channel)) {
			//重复登录，拒绝
			loginResp = buildResponse((byte)-1);
		}else {
			InetSocketAddress address = (InetSocketAddress) (channel.remoteAddress());
			String ip = address.getAddress().getHostAddress();
			if(ServerContext.isWhite(ip)) {	//白名单
				loginResp = buildResponse((byte) 0);
				String localAddress = address.getAddress().getHostAddress() + ":" + address.getPort();
				ServerContext.userLogin(channel, localAddress);
			}else{	//黑名单
				loginResp = buildResponse((byte) -1);
			}
		}
		
		System.out.println("The login respose is : " + 
				loginResp);
		
		ctx.writeAndFlush(loginResp);
	}

	private NettyMessage<Object> buildResponse(byte result){
		NettyMessage<Object> message = new NettyMessage<>();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP);
		message.setBody(result);
		message.setHeader(header);
		return message;
	}
	
	
}
