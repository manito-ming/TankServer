package org.websocket;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.log4j.Logger;
import org.dao.UserDao;
import org.temp.Tempfield;
import sun.nio.ch.DirectBuffer;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.*;


import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 */

public class ServerHandler extends ChannelInboundHandlerAdapter {
    Logger logger= Logger.getLogger(ServerHandler.class);
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest fhr = (FullHttpRequest) msg;
            String uri = fhr.uri();
            FullHttpResponse response = null;
            Charset charset = Charset.forName("utf-8");
            if (uri.equals("/")||uri.equals("/login.html")) {
                logger.info("login.html");
                String html = "";
                FileInputStream fin = new FileInputStream("/home/mzh/IdeaProjects/TankServer/src/org/webPage/new_tank/html/login.html");
                FileChannel fc = fin.getChannel();
                ByteBuffer bf = ByteBuffer.allocateDirect(2048);
                while ((fc.read(bf)) != -1) {
                    bf.flip();
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer cb = decoder.decode(bf);
                    bf.clear();
                    html += String.valueOf(cb);
                }
                logger.info("fileinputstream login.html success");
                response = new DefaultFullHttpResponse(HTTP_1_1,
                        OK, Unpooled.wrappedBuffer(html.getBytes()));
                response.headers().set("Access-Control-Allow_Origin","*");
                response.headers().set(CONTENT_TYPE, "text/html");
                response.headers().setInt(CONTENT_LENGTH,
                        response.content().readableBytes());
                fin.close();
                ctx.writeAndFlush(response);
                ((DirectBuffer)bf).cleaner().clean();
                logger.info("response login.html success");
            }
           else if(fhr.uri().contains("/websocket")) {
                    ctx.fireChannelRead(msg);
                }
        } else if (msg instanceof WebSocketFrame){
            System.out.println("WebSocket类型");
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }
}