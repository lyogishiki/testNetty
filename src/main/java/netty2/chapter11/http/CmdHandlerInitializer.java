package netty2.chapter11.http;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class CmdHandlerInitializer extends ChannelInitializer<Channel>{

	static final byte SPACE = ' ';
	
	@Override
	protected void initChannel(Channel ch) throws Exception {
		ch.pipeline().addLast(new CmdDecoder(64 * 1024))
			.addLast(new CmdHandler());
	}

	static final class Cmd {
		private final ByteBuf name;
		private final ByteBuf args;
		
		public Cmd(ByteBuf name, ByteBuf args) {
			super();
			this.name = name;
			this.args = args;
		}

		public ByteBuf getName() {
			return name;
		}

		public ByteBuf getArgs() {
			return args;
		}
		
	}
	
	public static final class CmdDecoder extends LineBasedFrameDecoder{

		public CmdDecoder(int maxLength) {
			super(maxLength);
		}

		@Override
		protected Object decode(ChannelHandlerContext ctx, ByteBuf buffer) throws Exception {
			ByteBuf frame = (ByteBuf) super.decode(ctx, buffer);
			
			if(frame == null) {
				return null;
			}
			
			int indedx = frame.indexOf(frame.readerIndex(), frame.writerIndex(), SPACE);
			
			return new Cmd(
					frame.slice(frame.readerIndex(), indedx),
					frame.slice(indedx+1, frame.writerIndex()));
		}
		
	}
	
	public static final class CmdHandler extends SimpleChannelInboundHandler<Cmd>{

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
			System.out.println(msg);
		}
	}
}
