package org.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpPostRequestDecoder;
import org.apache.log4j.Logger;


import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


public class JumpHandler extends ChannelInboundHandlerAdapter {
    public static String json;
    public String registmsg ;
    public String message ;
    public static    CyclicBarrier barrier = new CyclicBarrier(2);

    public static     ExecutorService executor = Executors.newFixedThreadPool(2);


    Logger logger=Logger.getLogger(JumpHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        FullHttpRequest fhr= (FullHttpRequest) msg;
        String uri =fhr.uri();
        HttpPostRequestDecoder decoder = null;
        List<InterfaceHttpData> parmList = null;
        String json=null;
        if (uri.equals("/jump")){
            System.out.println(ctx.channel()+"=========================================================================================================================================================");

            System.out.println("=======================================================================================================");
            logger.info("handle request uri:"+fhr.uri());
            decoder = new HttpPostRequestDecoder(fhr);
            InterfaceHttpPostRequestDecoder httpdecoder = decoder.offer(fhr);
            parmList = httpdecoder.getBodyHttpDatas();
            FullHttpResponse response=null;
//            String registmsg = null;
         //   String message = null;
            HashMap map = new HashMap();
            String[] user = null;
            for (InterfaceHttpData l : parmList) {
                registmsg = String.valueOf(l);
                System.out.println(registmsg.toString());
                message = registmsg.substring(7, registmsg.length());
                user = message.split("=");
//                map.put(user[0], user[1]);
            }




            Future<String> future =executor.submit(new Runner(barrier));
//            UserMatchMap.userMap.put(map.get("account"),null);
//            Thread thread=new Thread(handleMatch);
////            thread.start();
//            UserMatchMap.userMap.put(map.get("account"),null);
//            System.out.println("map1-------->"+UserMatchMap.userMap);
//            while(UserMatchMap.getSize()!=2){
//                json= "{\"status\":\"0\"}";
//            }
//            json= "{\"status\":\"1\"}";
//            System.out.println("map2----------->" +UserMatchMap.userMap);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(future.get().getBytes("UTF-8")));
            response.headers().set(CONTENT_TYPE, "text/json");
            response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
            response.headers().setInt(CONTENT_LENGTH,
                    response.content().readableBytes());
            ctx.write(response);
            ctx.flush();
            logger.info("response success uri "+fhr.uri());
            UserMatchMap.userMap.clear();
            logger.info("============UserMatchMap userMap is clear============");
        }
        else {
            ctx.fireChannelRead(msg);
        }
    }
}



class Runner implements Callable {    // 一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)
    private CyclicBarrier barrier;
    FullHttpResponse response = null;
    private String name;
    private static int roomid=1;
    public static String json;

    public Runner(CyclicBarrier barrier) {
        super();
        this.barrier = barrier;
    }

    Logger logger=Logger.getLogger(Runner.class);

    @Override
    public String call() {
        try {
            barrier.await(40, TimeUnit.SECONDS);//是让这些线程等待至一定的时间，如果还有线程没有到达barrier状态就直接让到达barrier的线程执行后续任务。

                json = "{\"status\":\"2\",\"roomid\":"+roomid+"}";
                roomid++;
                return json;

        } catch (Exception e) {

            logger.error(e);
            json = "{\"status\":\"1\"}";
            return json;
        } finally {
            return json;
        }
    }
}


