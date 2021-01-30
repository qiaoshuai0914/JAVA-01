package java.nio01.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.nio01.handler.OkhttpOutBoundHandler;
import java.nio01.router.HttpEndpointRouter;
import java.util.List;

public class HttpInitializer extends ChannelInitializer<SocketChannel> {

    OkhttpOutBoundHandler okhttpOutBoundHandler;

    public HttpInitializer(HttpEndpointRouter router, List<String> endpoints) {
        okhttpOutBoundHandler = new OkhttpOutBoundHandler(router, endpoints);
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline p = ch.pipeline();
        p.addLast(new HttpServerCodec());
        //p.addLast(new HttpServerExpectContinueHandler());
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        p.addLast(new HttpHandler(okhttpOutBoundHandler));
    }
}
