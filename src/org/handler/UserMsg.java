package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.util.concurrent.EventExecutorGroup;
import org.apache.log4j.Logger;
import org.dao.UserDao;

import java.util.HashMap;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class UserMsg extends ChannelInboundHandlerAdapter {
    Logger logger=Logger.getLogger(LoginHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        FullHttpRequest fhr= (FullHttpRequest) msg;
        String uri=fhr.uri();
        FullHttpResponse response = null;
        if (uri.contains("msg")){
            logger.info("uri ---->"+uri);
            HttpPostRequestDecoder decoder = null;
            List<InterfaceHttpData> parmList = null;
            decoder = new HttpPostRequestDecoder(fhr);
            InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
            parmList = httpdecoder.getBodyHttpDatas();
            String registmsg = null;
            String message = null;
            HashMap map = new HashMap();
            String[] user = null;
            for (InterfaceHttpData l : parmList) {
                registmsg = String.valueOf(l);
                message = registmsg.substring(7, registmsg.length());
                user = message.split("=");
                map.put(user[0], user[1]);
            }
            UserDao userDao=new UserDao();
            String uname=userDao.searchName((String) map.get("account"));
            String username="{\"uname\":\""+uname+"\"}";
            response = new DefaultFullHttpResponse(HTTP_1_1,
                    OK, Unpooled.wrappedBuffer(username.getBytes("UTF-8")));
            response.headers().set("Access-Control-Allow_Origin","*");
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            logger.info("response success uri---->"+uri);
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
}
