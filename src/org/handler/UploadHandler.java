package org.handler;

import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import org.apache.log4j.Logger;
import org.dao.UserDao;

import org.websocket.HandlePy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class UploadHandler extends ChannelInboundHandlerAdapter {
    Logger logger = Logger.getLogger(UploadHandler.class);
    public HandlePy handlePy=new HandlePy();
    public static Map map1=new HashMap<>();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        System.out.println(ctx.channel()+"=========================================================================================================================================================");
        FullHttpRequest fhr = (FullHttpRequest) msg;
        List<InterfaceHttpData> parmList = null;
        String json = null;
        UserDao userDao=new UserDao();

        DefaultFullHttpResponse response = null;
        HttpPostRequestDecoder decoder = null;
        if (fhr.uri().equals("/upload")) {
            logger.info("handle request uri:"+fhr.uri());
            decoder = new HttpPostRequestDecoder(fhr);
            InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
            parmList = httpdecoder.getBodyHttpDatas();
            System.out.println(parmList+"----------parmlist-----------------------------------");
            String registmsg = null;
            String message = null;
            HashMap map = new HashMap();
            String[] user = null;
            for (InterfaceHttpData l : parmList) {
                registmsg = String.valueOf(l);
                System.out.println(registmsg+"registmsg"+"----------------------------------------");
                message = registmsg.substring(7, registmsg.length());
                System.out.println(message+"----------message-----------------------------");
                user = message.split("=");
                System.out.println(user[1]+"------------------------user[1]-----------------------");
                map.put(user[0], user[1]);
            }

            String s=String.valueOf(parmList);
            String s1[]=s.split("code0=");
           String s2[]= s1[1].split("]");
            map.put("code0",s2[0]);
//            JSONObject jsonObject=new JSONObject();
//            jsonObject=(JSONObject) parmList;
//            jsonObject.get("account0");
//            jsonObject.get("code0");
//            System.out.println(jsonObject.get("account0"));
//            System.out.println("------------------------------------------------------------");
//
//            System.out.println(jsonObject.get("code0"));
//            System.out.println("------------------------------------------------------------");


            String account0= (String) map.get("account0");
            String code0= (String) map.get("code0");
            String color0=(String) map.get("color0");
            System.out.println("==========================================upload================================================");
            System.out.println("==========================================upload================================================");
            System.out.println("==========================================upload================================================");
            System.out.println("==========================================upload================================================");
            System.out.println(code0);
            handlePy.createPy(code0, Integer.parseInt(account0));
      //    handlePy.execPy(accountmap, colormap, Integer.parseInt(account[1]), Integer.parseInt(color0));
            System.out.println("response==========================================================");

            String json1 = "{\"status\":\"1\"}";

            logger.info("login status json:"+json1);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json1.getBytes("UTF-8")));
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


