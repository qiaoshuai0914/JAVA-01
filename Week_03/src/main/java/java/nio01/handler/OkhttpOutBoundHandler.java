package java.nio01.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpUtil;
import tools.OkHttpService;

import java.nio01.filter.HeaderHttpRequestFilter;
import java.nio01.filter.HeaderHttpResponseFilter;
import java.nio01.router.HttpEndpointRouter;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class OkhttpOutBoundHandler implements Handler {

    private HttpEndpointRouter httpEndpointRouter;
    private List<String> endpoints;

    java.nio01.filter.HeaderHttpResponseFilter headerHttpResponseFilter = new HeaderHttpResponseFilter();
    HeaderHttpRequestFilter headerHttpRequestFilter = new HeaderHttpRequestFilter();

    public OkhttpOutBoundHandler(HttpEndpointRouter httpEndpointRouter, List<String> endpoints) {
        this.httpEndpointRouter = httpEndpointRouter;
        this.endpoints = endpoints;
    }

    public void handle(final FullHttpRequest fullHttpRequest,
                       final ChannelHandlerContext context) {
        FullHttpResponse response = null;
        try {
            String url = httpEndpointRouter.route(endpoints);
            headerHttpRequestFilter.filter(fullHttpRequest, context);
            response = handlerTest(fullHttpRequest, context, url);
            headerHttpResponseFilter.filter(response);
        } finally {
            if (fullHttpRequest != null) {
                if (!HttpUtil.isKeepAlive(fullHttpRequest)) {
                    context.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    context.write(response);
                }
            }
        }


    }

    private FullHttpResponse handlerTest(FullHttpRequest fullRequest, ChannelHandlerContext ctx, String url) {


        FullHttpResponse response = null;
        try {

            String value = OkHttpService.requestGet(url);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(value.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

        } catch (Exception e) {
            System.out.println("处理出错:" + e.getMessage());
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {

        }
        return response;
    }
}
