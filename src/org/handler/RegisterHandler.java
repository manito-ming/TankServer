package org.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import org.dao.Connector;
import org.dao.MybatisTest;
import org.temp.Tempfield;

import java.util.HashMap;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
public class RegisterHandler extends ChannelInboundHandlerAdapter {
    Logger logger=Logger.getLogger(RegisterPageHandler.class);



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest  fhr= (FullHttpRequest) msg;
        Connector conn=new Connector();
        DefaultFullHttpResponse response = null;
        Tempfield temp=new Tempfield();
        if ((fhr.uri()).equals("/registmsg")){
            logger.info("handle request uri:"+fhr.uri());
            List<InterfaceHttpData> parmList=null;
            HttpPostRequestDecoder decoder=null;
            decoder = new HttpPostRequestDecoder(fhr);
            InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
            parmList = httpdecoder.getBodyHttpDatas();
            String registmsg=null;
            String message=null;
            HashMap map=new HashMap();
            String[] user=null;
            for (InterfaceHttpData l:parmList){
               registmsg=String.valueOf(l);
               message=registmsg.substring(7,registmsg.length());
                user=message.split("=");
                map.put(user[0],user[1]);
            }
            String uid= (String) map.get("account");
            String uname= (String) map.get("username");
            String upasswd= (String) map.get("password");
            String utel= (String) map.get("phone_number");



            String json=null;
            MybatisTest player=new MybatisTest();
            player.addCustomer(uid,uname,upasswd,utel);
            if (MybatisTest.status==1){
                json = "{\"status\":\"1\"}";
                temp.setUname(uname);
                temp.setUid(uid);
            }
            else {
                json = "{\"status\":\"0\"}";
            }
            logger.info("uid:"+uid);
            logger.info("uname:"+uname);
            logger.info("upasswd:"+upasswd);
            logger.info("utel:"+utel);
            logger.info("register status json:"+json);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set("Access-Control-Allow_Origin","*");
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            logger.info("response success uri "+fhr.uri());
        }else{
            ctx.fireChannelRead(msg);
        }

    }
}
