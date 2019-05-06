package org.temp;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


//实现对网络数据的读写
public class
ServerHandler1 extends ChannelInboundHandlerAdapter { //回调类

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {//读数据时会调用该方法
        FullHttpRequest fullHR = (FullHttpRequest) msg;
//        System.out.println(fullHR);
//        ByteBuf sb = (ByteBuf) fullHR.content();
        Charset charset = Charset.forName("utf-8");
        String url = fullHR.uri();
        System.out.println(url + ":=============================================");
        if (url.equals("/")) {
//            File f = new File("/home/simulous/IdeaProjects/MyGameMap/web/Find_Road/Find_Road/test.html");
//            long length = f.length();
//            System.out.println("filelength:+++++++++++++++" + length);
            FileInputStream fin = new FileInputStream("/home/wq/MyGameMap/web/Find_Road/Find_Road/test.html");
            FileChannel fc = fin.getChannel();
            ByteBuffer bf = ByteBuffer.allocate(2048);
            String mess = "";
            while ((fc.read(bf)) != -1) {
                bf.flip();
                CharsetDecoder decoder = charset.newDecoder();
                CharBuffer cb = decoder.decode(bf);
//                System.out.println(cb);
                bf.clear();
                mess += String.valueOf(cb);
            }

            HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(mess.getBytes("utf-8")));
            response.headers().set(CONTENT_TYPE, "text/html");
            response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
            ctx.writeAndFlush(response);
        }
//        }
        else {
            String mess = "";
            byte[] imgbyte;
            if(url.contains("png")||url.contains("jpg")){
                FileInputStream fin = new FileInputStream("/home/pp/IdeaProjects/TankServer/src/org/webPage/new_tank"+url);
                int img_size = fin.available();
//                System.out.println(img_size);
                imgbyte = new byte[img_size];
                fin.read(imgbyte);
                HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(imgbyte));
                response.headers().set(CONTENT_TYPE, "image/*");
                response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                ctx.writeAndFlush(response);

            }else{
//            String[] split = url.split("/");
//            System.out.println(split[1]);
//            File f = new File("/home/simulous/IdeaProjects/MyGameMap/web/Find_Road/" + split[1] + "/" + split[split.length - 1]);
//            System.out.println("filePath---------" + "/home/simulous/IdeaProjects/MyGameMap/web/Find_Road/" + split[1] + "/" + split[split.length - 1]);
//            long length = f.length();
//            System.out.println(length);
                File f = new File(url);
                System.out.println(f.exists());
                System.out.println("\n" + f.length() + "\n");
                FileInputStream fin = new FileInputStream(fullHR.uri());
                FileChannel fc = fin.getChannel();
                ByteBuffer bf = ByteBuffer.allocateDirect(102400);
                while ((fc.read(bf)) != -1) {
                    bf.flip();
                    CharsetDecoder decoder = charset.newDecoder();
                    CharBuffer cb = decoder.decode(bf);
                    bf.clear();
                    mess += String.valueOf(cb);
//                System.out.println(mess);
                }
                HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(mess.getBytes("utf-8")));
                response.headers().set(CONTENT_TYPE, "text/html,application/x-javascript,text/css,image/gif; charset=utf-8");
                response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
                ctx.writeAndFlush(response);
            }
//            HttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(mess.getBytes("utf-8")));
//            response.headers().set(CONTENT_TYPE, "text/html,application/x-javascript,text/css,image/gif; charset=utf-8");
//            response.headers().setInt(CONTENT_LENGTH, ((DefaultFullHttpResponse) response).content().readableBytes());
//            ctx.writeAndFlush(response);
        }
// }


////        String s=sb.toString(io.netty.util.CharsetUtil.UTF_8);
//      ByteBuf bf=fullHR.content();
//      byte[] bytes =new byte[bf.capacity()];
//      bf.readBytes(bytes);
//      String s=new String(bytes,"utf-8");
//        JSONObject jsonObject=JSONObject.parseObject(s);
//        System.out.println(jsonObject.toJSONString());
//        JSONArray a = new JSONArray();

//      Set<String> key=jsonObject.keySet();
//      Object v=jsonObject.get(key);
//        List<Key> keys=new ArrayList<>();
//        for (int i = 0; i <jsonObject.size(); i++) {
//            JSONObject jo=jsonObject.getJSONObject(String.valueOf(i));
//            key.add((String) v);
//        }


//        System.out.println("=============");
//        System.out.println(s);
//        System.out.println("=============");


        //将报文体里的内容拿出来

//       int x=0;
//       int y=0;
//        HashMap<Integer,Integer> route=new HashMap<Integer, Integer>();
//        int [][]mapinfo = new int[x][y];//存放地图信息
//        Node startnode=new Node(x,y);//存放起点
//        Node finshnode=new Node(x,y);//存放终点
//        Monster[] monsters=null;//存放怪物属性
//        Man man=null;//角色信息
//
//
//        Search search=new Search(mapinfo,startnode,finshnode,monsters);
//        route=search.aStar(man);//得到坐标集
//
//        String listjson=JSON.toJSONString(route);
//        ctx.writeAndFlush(listjson);//给浏览器发回响应
//
//        ReciveHandler RH=new ReciveHandler(mapinfo,startnode,finshnode,monsters,route);


//        String stringJson=JSON.toJSONString(fullHR);
//        List<String> stringList= JSON.parseArray(stringJson,String.class);


//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {//读错时调用该方法
//       cause.printStackTrace(); //出错时打印
//       ctx.close();
    }
}
