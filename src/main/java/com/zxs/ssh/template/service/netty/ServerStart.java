package com.zxs.ssh.template.service.netty;

import com.zxs.ssh.template.service.netty.client.ChatClient;
import com.zxs.ssh.template.service.netty.server.ChatServer;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Project Name:netty-demo
 * File Name:ServerStart
 * Package Name:com.zxs.ssh.template.service.netty
 * Date:2019/4/22
 * Author:zengxueshan
 * Description:
 * Copyright (c) 2019, 重庆云凯科技有限公司 All Rights Reserved.
 */

@Component
public class ServerStart implements InitializingBean {
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("启动成功。。。。。。。。。。。。。");
        Thread thread = new Thread(new ChatServer());
        thread.start();
        Thread.sleep(1000);
        Thread thread2 = new Thread(new ChatClient());
        thread2.start();
    }
}
