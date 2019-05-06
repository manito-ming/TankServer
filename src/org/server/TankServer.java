package org.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import org.apache.log4j.Logger;
import org.handler.ChannelInitializerImp;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class TankServer {
    Logger logger=Logger.getLogger(TankServer.class);
    private static final String SERVERPATH = "/home/mzh/IdeaProjects/TankServer/src/org/resources/tanklegendNettyServer.properties";
    private static Map<String, String> configs= new ConcurrentHashMap();
    private String server_host;
    private int server_port;
//    static {
//        configs = new ConcurrentHashMap();
//    }
    public void bind(String host,int port){
        this.server_port=port;
        this.server_host=host;
    }
    public void init() {
        BufferedReader serverIn = null;
        String conf = "";

        try {
            serverIn=new BufferedReader(new FileReader(SERVERPATH));
            while ((conf = serverIn.readLine()) != null) {
                String[] pair = conf.split("=");
                if (pair.length != 2) {
                    throw new Exception("ServerTest config error");
                } else {
                    configs.put(pair[0], pair[1]);
                }
            }
//            System.out.println(configs.get("server_host"));
            bind(configs.get("server_host"),Integer.parseInt(configs.get("server_port")));
            logger.info("server_host---->"+configs.get("server_host"));
            logger.info("server_port---->"+configs.get("server_port"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            logger.error("catch FileNotFoundException TankServer.class 49");
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("catch IOException TankServer.class 53");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("catch Exception TankServer.class 56");
        }

    }

    public void  run(){
        EventLoopGroup bossgroup = new NioEventLoopGroup();

        EventLoopGroup workgroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();

        server.group(bossgroup, workgroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializerImp());

            ChannelFuture future = null;
        try {
            future = server.bind(server_host,server_port).sync();
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);

            logger.info("wait for connection.....");
            future.channel().closeFuture().sync();

        } catch (InterruptedException e) {
            logger.error("catch InterruptedException TankServer.class 79");
            e.printStackTrace();
        }


    }

}
