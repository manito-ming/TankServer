package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import org.Util.ConnectionPool;
import org.apache.log4j.Logger;
import org.dao.UserDao;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class ShopHandler extends ChannelInboundHandlerAdapter {


    private static Logger log = Logger.getLogger(ShopHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        List<InterfaceHttpData> parmList = null;
        DefaultFullHttpResponse response = null;
        HttpPostRequestDecoder decoder = null;
        FullHttpRequest fhr = (FullHttpRequest) msg;


        ConnectionPool pool=new ConnectionPool(6379);
        Jedis jedis6379=pool.getConnection();

        String json = null;
        if (fhr.uri().equals("/shop/goods")) {
            log.info("handle request uri:"+fhr.uri());
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
            int account = userDao.searchAccount(String.valueOf(map.get("uid")));
            if (account >= Integer.parseInt((String) map.get("account"))) {
                //库2是仓库
                jedis6379.select(2);
                String store = jedis6379.get(String.valueOf(map.get("uid")));
                int cash=account-Integer.parseInt((String) map.get("account"));
                userDao.updateAccount(cash,String.valueOf(map.get("uid")));
                log.info("用户余额已修改");
                jedis6379.set(String.valueOf(map.get("uid")),String.valueOf(store+"&"+map.get("gid")));
                userDao.updateStore(String.valueOf(store+"&"+map.get("gid")),String.valueOf(map.get("uid")));
                log.info("用户仓库已修改");
                json = "{\"status\":\"1\"}";
            }else {
                json = "{\"status\":\"0\"}";
            }
            jedis6379.close();
            log.info("shop status json:"+json);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            log.info("response success uri "+fhr.uri());
        }
        else if (fhr.uri().equals("/store/dis")){

            log.info("handle request uri:"+fhr.uri());
            decoder = new HttpPostRequestDecoder(fhr);
            InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
            parmList = httpdecoder.getBodyHttpDatas();
            String registmsg = null;
            String message = null;
            String[] user = null;
            for (InterfaceHttpData l : parmList) {
                registmsg = String.valueOf(l);
                message = registmsg.substring(7, registmsg.length());
                user = message.split("=");
            }

            jedis6379.select(2);
            String gid= jedis6379.get(user[1]);
            String []gids = gid.split("&");
            StringBuffer res=new StringBuffer();
            res.append("{");
            for (int i =0 ;i<gids.length;i++) {
                res.append("\"gid"+(i+1)+"\":\""+gids[i]+"\";");
            }
            res.append("}");
            json = res.toString();
            jedis6379.close();
            log.info("store res json:"+json);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(json.getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.writeAndFlush(response);
            log.info("response success uri "+fhr.uri());

        }
        else {
            ctx.fireChannelRead(msg);
        }
    }


    public static void main(String[] args) {
        String gid="111&222&333";
        String []gids = gid.split("&");
        StringBuffer res=new StringBuffer();
        res.append("{");
        for (int i =0 ;i<gids.length;i++) {
            res.append("\"gid"+(i+1)+"\":\""+gids[i]+"\";");
        }
        res.append("}");
        System.out.println(res.toString());
    }
}
