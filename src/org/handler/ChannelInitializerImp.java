package org.handler;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.websocket.MyWebSocketHandler;


import java.util.HashMap;

class UserMatchMap{
    public static HashMap userMap=new HashMap();
    public static volatile int size;
    public static int getSize(){
        size = userMap.size();
        return size;
    }
    public static boolean isMatchSuccess(){
        if (userMap.size()==2){
            return true;
        }
        return false;
    }
}

public class ChannelInitializerImp extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel sc)  {

        sc.pipeline().addLast(new HttpRequestDecoder());//inbound
        sc.pipeline().addLast(new HttpObjectAggregator(65536));//inbound
        sc.pipeline().addLast(new HttpResponseEncoder());//outbound

        /*websocket*/
//        sc.pipeline().addLast("http-codec",new HttpServerCodec());
//        sc.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));
//        sc.pipeline().addLast("http-chunked",new ChunkedWriteHandler());
//        sc.pipeline().addLast("handler", new MyWebSocketHandler());
        sc.pipeline().addLast(new UserMsg());
        sc.pipeline().addLast(new LoginHandler());             //处理登录请求
        sc.pipeline().addLast(new JumpHandler());
        sc.pipeline().addLast(new UploadHandler());


        sc.pipeline().addLast(new WelcomePageHandler());   //返回首页
        sc.pipeline().addLast(new RegisterPageHandler());        //返回注册页面
//        sc.pipeline().addLast(new TestHandler());        //
        sc.pipeline().addLast(new ShopHandler());  //购物类
        sc.pipeline().addLast(new HallPageHandler());           //返回大厅页面
        sc.pipeline().addLast(new WaitPageHandler());           //匹配双人
        sc.pipeline().addLast(new CodePageHandler());            //
        sc.pipeline().addLast(new SendCodeHandler());             //发送代码并提取方法
//        sc.pipeline().addLast(new StartBattleHandler(j));           //战斗匹配
        sc.pipeline().addLast(new BattleHandler());                //发送对战页面
        sc.pipeline().addLast(new RegisterHandler());//处理注册请求




    }
}
