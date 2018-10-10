package netty2.xml.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.http.pojo.OrderFactory;
import netty2.xml.HttpXmlRequest;
import netty2.xml.HttpXmlResponse;

public class httpXmlClientHandler extends SimpleChannelInboundHandler<HttpXmlResponse> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
		System.out.println(msg);
		System.out.println(msg.getResponse().headers());
		System.out.println(msg.getResult());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		HttpXmlRequest request = new HttpXmlRequest(null, 
				OrderFactory.create(123));
		ctx.writeAndFlush(request);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	

}
