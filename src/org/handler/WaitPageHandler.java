package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import sun.nio.ch.DirectBuffer;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
public class WaitPageHandler extends ChannelInboundHandlerAdapter {
    Logger logger=Logger.getLogger(WaitPageHandler.class);



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest fhr = (FullHttpRequest) msg;
        String uri = fhr.uri();
        FullHttpResponse response = null;
        Charset charset = Charset.forName("utf-8");
        ChannelInitializerImp ch=new ChannelInitializerImp();

        if (uri.equals("/please_wait.html")) {
            logger.info(uri);
            String html = "";
            FileInputStream fin = new FileInputStream("/home/mzh/IdeaProjects/TankServer/src/org/webPage/new_tank/html/please_wait.html");
            FileChannel fc = fin.getChannel();
            ByteBuffer bf = ByteBuffer.allocateDirect(2048);
            while ((fc.read(bf)) != -1) {
                bf.flip();
//                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer cb = charset.decode(bf);
                bf.clear();
                html += String.valueOf(cb);
            }
            logger.info("fileinputstream please_wait.html success");
//            System.out.println("uri====>"+uri);
            response = new DefaultFullHttpResponse(HTTP_1_1,
                    OK, Unpooled.wrappedBuffer(html.getBytes()));
            response.headers().set("Access-Control-Allow_Origin","*");
            response.headers().set(CONTENT_TYPE, "text/html");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            ((DirectBuffer)bf).cleaner().clean();
            logger.info("response please_wait.html success");
        } else {
//            System.out.println("uri====>"+uri);
            if (uri.contains("wait")) {
                String text = "";
                byte[] imgbyte = null;
                if (uri.contains("gif")) {
                    logger.info("please_wait.html  uri   "+uri);
//                System.out.println("uri====>"+uri);
                    FileInputStream fileInputStream = new FileInputStream("/home/mzh/IdeaProjects/TankServer/src/org/webPage/new_tank" + uri);
                    int img_size = fileInputStream.available();
                    imgbyte = new byte[img_size];
                    fileInputStream.read(imgbyte);
                    HttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(imgbyte));
                    res.headers().set("Access-Control-Allow_Origin","*");
                    res.headers().set(CONTENT_TYPE, "image/*");
                    res.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) res).content().readableBytes());
                    ctx.writeAndFlush(res);

                    logger.info("response please_wait.html success uri"+uri);
//                    ((DefaultFullHttpResponse) res).release();
//                    ((DefaultFullHttpResponse) res).retain();
                } else {
                    System.out.println("=================================="+uri);
                    String path="/home/mzh/IdeaProjects/TankServer/src/org/webPage/new_tank" + uri;
                    FileInputStream inputStream = new FileInputStream(path);
                    File f = new File(path);
                    logger.info("please_wait.html  uri   "+uri);
                    FileChannel fileChannel = inputStream.getChannel();
                    ByteBuffer buffer = ByteBuffer.allocateDirect(102400);
                    while ((fileChannel.read(buffer)) != -1) {
                        buffer.flip();
                        CharsetDecoder decoder = charset.newDecoder();
                        CharBuffer cb = decoder.decode(buffer);
                        buffer.clear();
                        text += String.valueOf(cb);
                    }
                    HttpResponse httpresponse = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(text.getBytes("utf-8")));
                    httpresponse.headers().set("Access-Control-Allow_Origin","*");
                    httpresponse.headers().set(CONTENT_TYPE, "application/x-javascript; charset=utf-8");
                    httpresponse.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) httpresponse).content().readableBytes());
                    ctx.writeAndFlush(httpresponse);
                    ((DirectBuffer)buffer).cleaner().clean();
                    logger.info("response please_wait.html success uri"+uri);
                    }
                }
                else {
                ctx.fireChannelRead(msg);
            }
        }

    }
}