package java.nio01.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

public interface Handler {
    void handle(final FullHttpRequest fullHttpRequest,
                final ChannelHandlerContext context);
}
