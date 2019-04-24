package com.zxs.ssh.template.service.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoop;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Project Name:netty-demo
 * File Name:ChatClient
 * Package Name:com.zxs.ssh.template.service.netty.client
 * Date:2019/4/22
 * Author:zengxueshan
 * Description:
 * Copyright (c) 2019, 重庆云凯科技有限公司 All Rights Reserved.
 */


public class ChatClient implements Runnable{

    public String host = "127.0.0.1"; // ip地址
    public int port = 9876; // 端口
    // 通过nio方式来接收连接和处理连接
    private EventLoopGroup group = new NioEventLoopGroup();
    public static ChatClient nettyClient = new ChatClient();
    private static ChannelFuture f = null;
    /**唯一标记 */
    private boolean initFalg=true;

    @Override
    public void run() {
        doConnect(new Bootstrap(), group);
    }

    /**
     * 重连
     */
    public void doConnect(Bootstrap bootstrap, EventLoopGroup eventLoopGroup) {

        try {
            if (bootstrap != null) {
                bootstrap.group(eventLoopGroup);
                bootstrap.channel(NioSocketChannel.class);
                bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
                bootstrap.handler(new NettyClientFilter());
                bootstrap.remoteAddress(host, port);
                f = bootstrap.connect().addListener((ChannelFuture futureListener) -> {
                    final EventLoop eventLoop = futureListener.channel().eventLoop();
                    if (!futureListener.isSuccess()) {
                        System.out.println("与服务端断开连接!在10s之后准备尝试重连!");
                        eventLoop.schedule(() -> doConnect(new Bootstrap(), eventLoop), 10, TimeUnit.SECONDS);
                    }
                });
                if(initFalg){
                    System.out.println("Netty客户端启动成功!");
                    initFalg=false;
                }
                // 阻塞
                f.channel().closeFuture().sync();
            }
        } catch (Exception e) {
            System.out.println("客户端连接失败!"+e.getMessage());
        }

    }
    public void sendMessage(String message) throws Exception {
        if(message != null && !message.isEmpty()){
            f.channel().writeAndFlush(message+"~_~");
            //System.out.println("【客户端】客户端发送数据:"+message);
        }else{
            throw new Exception("消息不能为空");
        }
    }
}
