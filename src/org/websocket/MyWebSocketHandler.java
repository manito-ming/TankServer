package org.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MemoryAttribute;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * 接收/处理/响应客户端websocket请求的核心业务处理类
 * 通过添加hanlder，我们可以监听Channel的各种动作以及状态的改变，包括连接，绑定，接收消息等。
 *
 * @author liuyazhuang
 */

public class MyWebSocketHandler extends ChannelInboundHandlerAdapter {

    private static int count=0;
    private static int count2=0;
    private static Set<String>  s = new HashSet();

    private static int color=0; //color为1代表红方，2代表蓝方

    private static ArrayBlockingQueue arrayBlockingQueue=new ArrayBlockingQueue(1000);
    private volatile static Map map=new HashMap();//传一个id
    private static Map mapa=new HashMap();//传a的
    private static Map map1=new HashMap();//传两个id
    private static Map map2=new HashMap();//传消息
    public static HashMap accountmap=new HashMap();//
    private static HashMap hashMap=new HashMap();
    private static HashMap colormap=new HashMap();
    private HandlePy handlePy=new HandlePy();
    private static HashMap h1=new HashMap();
    private static HashMap h2=new HashMap();
    private static Map<String,String> roommap=new ConcurrentHashMap();
    private static Map<String,Channel> usermap=new ConcurrentHashMap<>();
    private static int q;
    private static int w;
    private static int e;
    private static int t;
    private static int y;
    private static int u;

    private static int b=0;
    private static final Logger logger = Logger
            .getLogger(WebSocketServerHandshaker.class.getName());


    private WebSocketServerHandshaker handshaker;
    //客户端与服务端创建连接的时候调用
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        // 添加


    }

    //客户端与服务端断开连接的时候调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // 移除
        Global.group.remove(ctx.channel());
        s.remove(ctx.channel().id().toString());

        color=0;//没办法只能放这
    }

    //服务端接收客户端发送过来的数据结束之后调用
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    //工程出现异常的时候调用
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {


        if (msg instanceof FullHttpRequest) {

            handleHttpRequest(ctx, ((FullHttpRequest) msg));

        } else if (msg instanceof WebSocketFrame) {
            try {
                handlerWebSocketFrame(ctx, (WebSocketFrame) msg);

            }catch (Exception e){

            }

        }

    }
    private void handlerWebSocketFrame(ChannelHandlerContext ctx,
                                       WebSocketFrame frame) {

        // 判断是否关闭链路的指令
        if (frame instanceof CloseWebSocketFrame) {
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame
                    .retain());
        }

        // 判断是否ping消息
        if (frame instanceof PingWebSocketFrame) {
            ctx.channel().write(
                    new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 本例程仅支持文本消息，不支持二进制消息
        if (!(frame instanceof TextWebSocketFrame)) {

            System.out.println("本例程仅支持文本消息，不支持二进制消息");

//			throw new UnsupportedOperationException(String.format(
//					"%s frame types not supported", frame.getClass().getName()));
        }



// 返回应答消息

        String r=null;
        try {
            r = ((TextWebSocketFrame) frame).text();

            System.out.println(r);
            for (String xx:usermap.keySet())
            {
                System.out.println("------------------------hashmap---------------------------"+usermap.get(xx).id().toString());
            }

            String a[]=r.split(",");

            if(a[0].equals("first")){
                usermap.put(a[2],ctx.channel());

            }
            arrayBlockingQueue.put(r);
        } catch (InterruptedException e) {
//			e.printStackTrace();
        }
        String request= null;
        try {
            request = (String) arrayBlockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String q[]=request.split(",");

        System.out.println(q);
        if(q[0].equals("first")){

            if(roommap.containsKey(q[1])){
                String result1=roommap.get(q[1]);
                String result2=handlePy.execPy(q[2]);
                roommap.put(q[1],result1+","+request+","+result2);

                System.out.println(roommap.get(q[1]));

            }else{

                String result=handlePy.execPy(q[2]);

                roommap.put(q[1],request+","+result);
                System.out.println(roommap.get(q[1]));


            }

        }else      if(q[0].equals("finish")){
            roommap.remove(q[1]);

        }else {

            String value=roommap.get(q[0]);

            String value1[]=value.split(",");
            System.out.println(value1);


            if (value1[2].equals(q[1])){   //q[1]为userid    value1[2],value1[]也为userid
                if(q[2].equals("1")){
                    if(q[3].equals("tank_path")){ //3-8为事件

                        Channel channel=usermap.get(q[1]);
                        System.out.println(channel+"==============================================channelid=============================");
                        System.out.println("--------------------------------------------------------"+channel.id().toString());
                        String json1="{\"message\":\"tank_path\",\"tank_path\":\""+value1[6]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("tank_w_path")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_w_path\",\"tank_w_path\":\""+value1[5]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("tank_meet_enemy_path_choose")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_meet_enemy_path_choose\",\"tank_meet_enemy_path_choose\":\""+value1[7]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("enemytank_path")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"enemytank_path\",\"enemytank_path\":\""+value1[14]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));
                    }
                    else if(q[3].equals("enemytank_w_path")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"enemytank_w_path\",\"enemytank_w_path\":\""+value1[13]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("enemy_meet_tank_path_choose")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"enemy_meet_tank_path_choose\",\"enemy_meet_tank_path_choose\":\""+value1[15]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));
                    }
                }else if(q[2].equals("2")){    ////当q2为蓝时
                    System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
                    if(q[3].equals("tank_path")){ //3-8为事件
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_path\",\"tank_path\":\""+value1[14]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("tank_w_path")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_w_path\",\"tank_w_path\":\""+value1[13]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("tank_meet_enemy_path_choose")){
                        Channel channel=usermap.get(q[15]);
                        String json1="{\"message\":\"tank_meet_enemy_path_choose\",\"tank_meet_enemy_path_choose\":\""+value1[7]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("enemytank_path")){
                        Channel channel=usermap.get(q[6]);
                        String json1="{\"message\":\"enemytank_path\",\"enemytank_path\":\""+value1[14]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));
                    }
                    else if(q[3].equals("enemytank_w_path")){
                        Channel channel=usermap.get(q[5]);
                        String json1="{\"message\":\"enemytank_w_path\",\"enemytank_w_path\":\""+value1[13]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("enemy_meet_tank_path_choose")){
                        Channel channel=usermap.get(q[7]);
                        String json1="{\"message\":\"enemy_meet_tank_path_choose\",\"enemy_meet_tank_path_choose\":\""+value1[15]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));
                    }




                }
            }else if(value1[10].equals(q[1])){
                System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
                if(q[2].equals("1")){          //当q2为红时
                    if(q[3].equals("tank_path")){ //3-8为事件
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_path\",\"tank_path\":\""+value1[14]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("tank_w_path")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_w_path\",\"tank_w_path\":\""+value1[13]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("tank_meet_enemy_path_choose")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_meet_enemy_path_choose\",\"tank_meet_enemy_path_choose\":\""+value1[15]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.write(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("enemytank_path")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"enemytank_path\",\"enemytank_path\":\""+value1[6]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));
                    }
                    else if(q[3].equals("enemytank_w_path")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"enemytank_w_path\",\"enemytank_w_path\":\""+value1[5]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("enemy_meet_tank_path_choose")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"enemy_meet_tank_path_choose\",\"enemy_meet_tank_path_choose\":\""+value1[7]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));
                    }
                }else if(q[2].equals("2")){    ////当q2为蓝时
                    if(q[3].equals("tank_path")){ //3-8为事件
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_path\",\"tank_path\":\""+value1[6]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("tank_w_path")){
                        Channel channel=usermap.get(q[1]);
                        String json1="{\"message\":\"tank_w_path\",\"tank_w_path\":\""+value1[5]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("tank_meet_enemy_path_choose")){
                        Channel channel=usermap.get(q[15]);
                        String json1="{\"message\":\"tank_meet_enemy_path_choose\",\"tank_meet_enemy_path_choose\":\""+value1[7]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("enemytank_path")){
                        Channel channel=usermap.get(q[6]);
                        String json1="{\"message\":\"enemytank_path\",\"enemytank_path\":\""+value1[14]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));
                    }
                    else if(q[3].equals("enemytank_w_path")){
                        Channel channel=usermap.get(q[5]);
                        String json1="{\"message\":\"enemytank_w_path\",\"enemytank_w_path\":\""+value1[13]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));

                    }else if(q[3].equals("enemy_meet_tank_path_choose")){
                        Channel channel=usermap.get(q[7]);
                        String json1="{\"message\":\"enemy_meet_tank_path_choose\",\"enemy_meet_tank_path_choose\":\""+value1[15]+"\"}";
                        JSONObject jsonObj = JSON.parseObject(json1);
                        channel.writeAndFlush(new TextWebSocketFrame(String.valueOf(jsonObj)));
                    }


                }

            }
        }


        System.out.println("=================================================finish=============================================================");
        System.out.println("=================================================finish=============================================================");
        System.out.println("=================================================finish=============================================================");
//
//
//
//
//
//		System.out.println("==========================================request===============================================");
//		System.out.println(request);
//		System.out.println("==========================================request===============================================");
//
//		String assemble[]=request.split(";");
//
//		String account[]=assemble[0].split("=");
//
//		System.out.println("===========================================account[0]=============================================");
//
//		System.out.println("account[0]------------------------------------------------"+account[0]);
//
//		System.out.println("length---------------------------------------"+request.length());
////
//		if(account[0].equals("a")){
//			System.out.println("=========================================同时跳转===========================================");
//		//if(request.equals("a")){//前面页面的处理
//			int a=2;
//		String s=String.valueOf(a);
//	//	TextWebSocketFrame tws = new TextWebSocketFrame(s
//			//	);
//		if (count==2) {
//        //  count=0;
//			// 群发
//			mapa.put("a",2);
//			System.out.println("mapa----------------------------------------------------------------->"+mapa.get("a"));
//			String jsona= JSON.toJSONString(mapa);
//			System.out.println("jsona------------------------------------------------------------------->"+jsona);
//			mapa.clear();
//			Global.group.writeAndFlush(new TextWebSocketFrame(jsona));
//		}
//		}
//
//
//
//
//
//		if(account[0].equals("account0")){
//			System.out.println("=========================================同时跳转0===========================================");
//			//if(request.equals("a")){//前面页面的处理
//			int a=2;
//			String s=String.valueOf(a);
//			//	TextWebSocketFrame tws = new TextWebSocketFrame(s
//			//	);
//
//
//
//			String color0[] = assemble[1].split("=");
//		System.out.println("===========================================color0[0]=============================================");
//		System.out.println(color0[1]);
//		System.out.println("===========================================color0[0]=============================================");
//
//		String code0[] = assemble[2].split("&");
//		System.out.println("===========================================code0[0]=============================================");
//		System.out.println(code0[1]);
//		System.out.println("===========================================code0[0]=============================================");
//
//		// accountmap
//
//		 System.out.println("==============================================================================================");
//
//		 System.out.println(Integer.parseInt(account[1])+"=================================="+Integer.parseInt(color0[1]));
//
//		 handlePy.createPy(code0[1], Integer.parseInt(account[1]));
//
//		 handlePy.execPy(accountmap, colormap, Integer.parseInt(account[1]), Integer.parseInt(color0[1]));
//			System.out.println("response==========================================================");
//
//			hashMap = (HashMap) accountmap.get(Integer.parseInt("222"));
////			System.out.println(" accountmap.get(account[1]----------------------------------------------------------->" +  accountmap.get(Integer.parseInt("222")));
////			System.out.println("account[1]----------------------------------------------------------->" + account[1]);
////
////			System.out.println("hashmap----------------------------------------------------------->" + hashMap);
////
////			System.out.println("response==========================================================");
//
//
//			if (count==2)   {
//				//  count=0;
//				// 群发
//				System.out.println(" ");
//				mapa.put("a",2);
//				System.out.println("mapa----------------------------------------------------------------->"+mapa.get(a));
//				String jsona= JSON.toJSONString(mapa);
//				System.out.println("jsona------------------------------------------------------------------->"+jsona);
//				mapa.clear();
//				Global.group.writeAndFlush(new TextWebSocketFrame(jsona));
//
//
//				h1= (HashMap) colormap.get(1);
//				System.out.println("h1-----------------------------------------"+h1);
//				h2= (HashMap) colormap.get(2);
//				System.out.println("h2-----------------------------------------"+h2);
//				q= Integer.parseInt(String.valueOf(h1.get("tank_crash_border")));
//				w=  Integer.parseInt(String.valueOf(h1.get("tank_crash_border")));
//				e=  Integer.parseInt(String.valueOf(h1.get("tank_crash_enemy")));
//
//				u=  Integer.parseInt(String.valueOf(h2.get("tank_crash_border")));
//				t=  Integer.parseInt(String.valueOf(h2.get("tank_crash_border")));
//				y=  Integer.parseInt(String.valueOf(h2.get("tank_crash_enemy")));
//			}
//
//		}
//
//
//		 if(account[0].equals("channelid")){
//			// System.out.println("account[1]------------------------------------------"+Integer.parseInt(account[1]));
////            else if (request.equals("channelid"))             //战斗画面的首次处理
////			{
//               // color=color+1;
//			System.out.println("----------------------------------------------------channelid--------------------");
////			String a=(String) account[1];
////			 System.out.println(a);
////                hashMap= (HashMap) accountmap.get(Integer.parseInt(account[1]));
////             HashMap
//
//				 hashMap = (HashMap) accountmap.get(Integer.parseInt(account[1]));
////			 System.out.println(" accountmap.get(account[1]----------------------------------------------------------->" +  accountmap.get(account[1]));
////			 System.out.println("account[1]----------------------------------------------------------->" + account[1]);
//
//				 System.out.println("hashmap----------------------------------------------------------->" + hashMap);
//
//				 color = (int) hashMap.get("color");
//			     System.out.println("玩家" + color+ "id------------------------------------------------------------>" + account[1]);
//
//
//				 map.put("color", color);
//				 map.put("account",account[1]);
//				 map.put("channelid", ctx.channel().id().toString());
//			     System.out.println("单个个页面的map------------------------------------------------------"+map);
//				 System.out.println("玩家" + color + "channelid------------------------------------------------------------>" + ctx.channel().id().toString());
////                if(color==1) {                                        //这里我假设两个坦克脚本的方法
////                    map.put("tankpath_choose", color);
////                    map.put("tankpath_w_choose", color);
////                    map.put("tank_meet_enemy_path_choose", color);
////                }else {
////                    map.put("enemytankpath_choose", color);
////                    map.put("enemytankpath_w_choose", color);
////                    map.put("enemy_meet_tank_path_choose", color);
////
////                }
//				 String json = JSON.toJSONString(map);
//
//				 map.clear();//清空map，等下一个坦克来
//				 map1.put("color1", 3);//这个是一起传两个坦克id的map
//				 map1.put("_" + color, ctx.channel().id().toString());
//
//				 System.out.println("map1 " + color + "--------------------------------------->" + ctx.channel().id().toString());
//				 //String channelid=color+","+ctx.channel().id().toString();
//				 ctx.channel().write(new TextWebSocketFrame(json));
//				 //ctx.channel().write(new TextWebSocketFrame(channelid));//定下红蓝的id
//
////				ctx.channel().write(new TextWebSocketFrame(ctx.channel().id().toString()));
////				ctx.writeAndFlush("sad");
//
//
//				if (count == 2)
//
//				{
//
//					System.out.println("===============================================匹配成功==============================================");
//					//count=count-1;
//                    //color=0;
//                    String json1= JSON.toJSONString(map1);
//					System.out.println(map1.get("_"+1));
//					System.out.println(map1.get("_"+2));
//					System.out.println("---------------------------------当count=2时广播------------------------");
//                    Global.group.writeAndFlush((new TextWebSocketFrame(json1)));
////					color=0;
//					//	Global.group.writeAndFlush((new TextWebSocketFrame(sb.toString())));
//
//					//Global.group.writeAndFlush((new TextWebSocketFrame(s1)));//广播id
//					System.out.println("count=2--------------------------------------------------------------");
//				}
//
//			}  else {
//
//			String[] id = request.split("=");
//			String json2;
//			if (account[0].equals("tank_path")) {
//				System.out.println("id[0] ---------------------------------------->              "+id[0]);
//
//				System.out.println("account[1] ---------------------------------------->              "+account[1]);
//			//	a= (int) h1.get("tank_crash_stone");
//				//map2.put("master",id[1]);
//				map2.put("master",account[1]);
//				map2.put("message", "tank_path");
//				map2.put("tankpath_choose",q);
//				json2 = JSON.toJSONString(map2);
//				map2.clear();
//				Global.group.writeAndFlush((new TextWebSocketFrame(json2)));
//				System.out.println("success ------------------------------------------------------------------");
//			}
//			else if (account[0].equals("tank_w_path") ){
//			//	a= (int) h1.get("tank_crash_border");
//
//				map2.put("master",account[1]);
//				map2.put("message", "tank_w_path");
//				//map2.put("tankpath_w_choose",  h1.get("tank_crash_border"));
//				map2.put("tankpath_w_choose",  w);
//
//				json2 = JSON.toJSONString(map2);
//				map2.clear();
//				Global.group.writeAndFlush((new TextWebSocketFrame(json2)));
//
//			}
//			else if (account[0].equals("tank_meet_enemy_path_choose") ){
//				//a= (int) h1.get("tank_crash_enemy");
//
//				map2.put("master",account[1]);
//				map2.put("message", "tank_meet_enemy_path_choose");
//				map2.put("tank_meet_enemy_path_choose",e);
//				json2 = JSON.toJSONString(map2);
//				map2.clear();
//				Global.group.writeAndFlush((new TextWebSocketFrame(json2)));
//			}
//			else if (account[0].equals( "enemytank_w_path") ){
//			//	a= (int) h2.get("tank_crash_stone");
//
//				map2.put("master",account[1]);
//				map2.put("message", "enemytank_w_path");
//				map2.put("enemytankpath_w_choose",1);
//				json2 = JSON.toJSONString(map2);
//				map2.clear();
//				Global.group.writeAndFlush((new TextWebSocketFrame(json2)));
//			}
//			else if (account[0].equals( "enemytank_path") ){
//				//	a= (int) h2.get("tank_crash_stone");
//
//				map2.put("master",account[1]);
//				map2.put("message", "enemytank_path");
//				map2.put("enemytankpath_choose",t);
//				json2 = JSON.toJSONString(map2);
//				map2.clear();
//				Global.group.writeAndFlush((new TextWebSocketFrame(json2)));
//			}
//			else if (account[0] .equals( "enemy_meet_tank_path_choose")) {
//			//	a= (int) h2.get("tank_crash_stone");
//
//				map2.put("master",account[1]);
//				map2.put("message", "enemy_meet_tank_path");
//				map2.put("enemy_meet_tank_path_choose", y);
//				json2 = JSON.toJSONString(map2);
//				map2.clear();
//				Global.group.writeAndFlush((new TextWebSocketFrame(json2)));
//			}
//		}
//
//		int a=2;
//		String s=String.valueOf(a);
//		TextWebSocketFrame tws = new TextWebSocketFrame(s
//				);

    }
    private void handleHttpRequest(ChannelHandlerContext ctx,
                                    Object msg) {
        FullHttpRequest req = (FullHttpRequest)msg;
        if (!req.decoderResult().isSuccess()
                || (!"websocket".equals(req.headers().get("Upgrade")))) {
            ctx.fireChannelRead(msg);
            return;
        }
        System.out.println("=============================================================================");
        WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(
                "ws://0.0.0.0:9999/websocket", null, false);

        handshaker = wsFactory.newHandshaker(req);

        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
            System.out.println("-----------------------------------------------------------------------------");
        } else {
            handshaker.handshake(ctx.channel(), req);//http升级成websocket建立成功了

            Global.group.add(ctx.channel());
            s.add(ctx.channel().id().toString());   //将这个channelid存放在set数组中
            System.out.println("ctx.id:"+s);
            count=s.size();
            System.out.println("客户端的连接数为  -----------"  +count);
            System.out.println("--------------------------------------"+usermap.toString());

        }

    }


}
