package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import org.dao.MybatisTest;
import org.temp.DealStrSub;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
public class SendCodeHandler extends ChannelInboundHandlerAdapter {
    Logger logger = Logger.getLogger(SendCodeHandler.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest fhr = (FullHttpRequest) msg;
        MybatisTest player = new MybatisTest();
        List<InterfaceHttpData> parmList = null;
        DealStrSub d=new DealStrSub();
        String json = null;
        DefaultFullHttpResponse response = null;
        HttpPostRequestDecoder decoder = null;
        if (fhr.uri().equals("/code")){
            logger.info("handle request assemble_tank1.html uri:"+fhr.uri());


            json = "{\"status\":\"1\"}";
            logger.info("code status json:"+json);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set("Access-Control-Allow_Origin","*");
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            logger.info("response success uri "+fhr.uri());
        }
        else {
            ctx.fireChannelRead(msg);
        }

    }
}
