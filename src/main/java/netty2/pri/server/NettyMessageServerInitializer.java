package netty2.pri.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import netty2.pri.NettyMessage;
import netty2.pri.NettyMessageDecoder;
import netty2.pri.NettyMessageEncoder;

public class NettyMessageServerInitializer extends ChannelInitializer<Channel>{

	@Override
	protected void initChannel(Channel ch) throws Exception {
		// MYTASK 修改添加Handler
//		new ProtobufDecoder(prototype)
//		new ProtoBufEnocod
//		new ProtobufVarint32LengthFieldPrepender()
//		new ProtobufVarint32FrameDecoder()
		ch.pipeline()
			//.addLast(new LengthFieldBasedFrameDecoder(65535, 0, 4))
			.addLast(new ReadTimeoutHandler(300))
			.addLast(new ProtobufVarint32FrameDecoder())
			.addLast(new ProtobufVarint32LengthFieldPrepender())
			.addLast(new NettyMessageEncoder())
			.addLast(new NettyMessageDecoder(NettyMessage.class))
			.addLast(LoginAuthRespHandler.DEFAULT_INSTANCE)
			.addLast(HeartBeatRespHandler.DEFAULT_INSTANCE)
			.addLast(NettyMessageHandler.DEFAULT_INSTANCE);
	}

}
