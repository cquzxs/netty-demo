package com.zxs.ssh.template.service.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 
* Title: HelloServerHandler
* Description:  服务端业务逻辑
* Version:1.0.0  
* @author pancm
* @date 2017年10月8日
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

    /** 空闲次数 */
    private int idle_count =1; 
    /** 发送次数 */
    private int count = 1;  

     /**
      * 超时处理
      * 如果5秒没有接受客户端的心跳，就触发;
      * 如果超过两次，则直接关闭;
      */
   @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object obj) throws Exception {  
        if (obj instanceof IdleStateEvent) {  
            IdleStateEvent event = (IdleStateEvent) obj;  
            if (IdleState.READER_IDLE.equals(event.state())) {  //如果读通道处于空闲状态，说明没有接收到心跳命令
                System.out.println("【服务器】已经5秒没有接收到客户端的信息了");
                if (idle_count > 1) {  
                    System.out.println("【服务器】关闭这个不活跃的channel");
                    ctx.channel().close();  
                }  
                idle_count++;
            }  
        } else {  
            super.userEventTriggered(ctx, obj);  
        }  
    }    

    /**
     * 业务逻辑处理
     */
    @Override  
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String message = (String) msg;  
        if ("hb_request".equals(message)) {  //如果是心跳命令，则发送给客户端;否则什么都不做
            ctx.write("【服务器】服务端成功收到心跳信息");
            ctx.flush();
            System.out.println("【服务器】服务端成功收到 "+ctx.channel().remoteAddress()+" 心跳信息:"+msg);
        }else{
            System.out.println("【服务器】第"+count+"次"+",服务端接受的消息:"+msg);
            count++;
        }
    }  

    /**
     * 异常处理
     */
    @Override  
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {  
        cause.printStackTrace();  
        ctx.close();  
    } 
}
