package netty2.chapter11.http;


import com.google.protobuf.MessageLite;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class ProtoBufInitializer extends ChannelInitializer<Channel> {

	private final MessageLite lite;
	
	public ProtoBufInitializer(MessageLite lite) {
		super();
		this.lite = lite;
	}

	@Override
	protected void initChannel(Channel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		//添加ProtobufVarint32FrameDecoder
		pipeline.addLast(new ProtobufVarint32FrameDecoder())
		//编码之前添加上长度信息
		.addLast(new ProtobufVarint32LengthFieldPrepender())
		//需要在ProtobufEncoder之前添加一个相应的ProtobufVarint32LengthFieldPrepender以编码进制长度信息
		.addLast(new ProtobufEncoder())
		.addLast(new ProtobufDecoder(lite))
		.addLast(new ObjectHandler());
	}

	public  static class ObjectHandler extends SimpleChannelInboundHandler<Object>{

		@Override
		protected void channelRead0(ChannelHandlerContext arg0, Object arg1) throws Exception {
			
		}
		
	}
}
