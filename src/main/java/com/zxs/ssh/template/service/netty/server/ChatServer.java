package com.zxs.ssh.template.service.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Project Name:netty-demo
 * File Name:ChatServer
 * Package Name:com.zxs.ssh.template.service.netty.server
 * Date:2019/4/22
 * Author:zengxueshan
 * Description:
 * Copyright (c) 2019, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class ChatServer implements Runnable{

    private static final int port = 9876; //设置服务端端口
    private static EventLoopGroup boss = new NioEventLoopGroup();   // 通过nio方式来接收连接和处理连接
    private static  EventLoopGroup work = new NioEventLoopGroup();   // 通过nio方式来接收连接和处理连接
    private static ServerBootstrap b = new ServerBootstrap();

    @Override
    public void run() {
        startServer();
    }

    public static void startServer(){
        try {
            b.group(boss,work);
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new NettyServerFilter()); //设置过滤器
            // 服务器绑定端口监听
            ChannelFuture f = b.bind(port).sync();
            System.out.println("服务端启动成功,端口是:"+port);
            // 监听服务器关闭监听
            f.channel().closeFuture().sync();
        }catch(InterruptedException e){
            e.printStackTrace();
        } finally {
            //关闭EventLoopGroup，释放掉所有资源包括创建的线程
            work.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }
}
