package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import org.dao.MybatisTest;
import org.dao.RedisDao;
import org.dao.UserDao;
import org.pojo.User;
import org.temp.Tempfield;

import java.util.HashMap;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpResponseStatus.parseLine;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
public class LoginHandler extends ChannelInboundHandlerAdapter {
    Logger logger=Logger.getLogger(LoginHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest fhr = (FullHttpRequest) msg;
//        MybatisTest player = new MybatisTest();
        List<InterfaceHttpData> parmList = null;
        String json = null;
        UserDao userDao=new UserDao();
        Tempfield temp=new Tempfield();
        DefaultFullHttpResponse response = null;
        HttpPostRequestDecoder decoder = null;
        if (fhr.uri().equals("/log")) {
            logger.info("handle request uri:"+fhr.uri());

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
//            System.out.println(parmList);
//            parmList.remove(0);
//            System.out.println(parmList.get(0));

//            String passwd = player.findCustomerTest((String) map.get("account"));
            RedisDao redisDao = new RedisDao();
//            String passwd=userDao.searchPasswd((String) map.get("account"));         //jdbc
            System.out.println("====================================================");
            String passwd = redisDao.RedisLogin((String)map.get("account"));
            logger.info("account:"+map.get("account")+",passwd:"+passwd);
            if (passwd.equals(map.get("password"))) {
                json = "{\"status\":\"1\"}";
//                temp.setUid((String) map.get("account"));
//                temp.setUname(userDao.search((String) map.get("account")));
            } else {
                json = "{\"status\":\"0\"}";
            }
            logger.info("login status json:"+json);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set("Access-Control-Allow_Origin","*");
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            logger.info("response success uri "+fhr.uri());
        } else {
            ctx.fireChannelRead(msg);
        }

    }
}
