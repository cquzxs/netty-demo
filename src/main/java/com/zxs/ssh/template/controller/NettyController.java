package com.zxs.ssh.template.controller;

import com.zxs.ssh.template.model.response.ResponseResult;
import com.zxs.ssh.template.service.netty.client.ChatClient;
import com.zxs.ssh.template.service.netty.server.ChatServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Project Name:netty-demo
 * File Name:NettyController
 * Package Name:com.zxs.ssh.template.controller
 * Date:2019/4/22
 * Author:zengxueshan
 * Description:
 * Copyright (c) 2019, 重庆云凯科技有限公司 All Rights Reserved.
 */

@RestController("nettyController")
public class NettyController {

    private static final Logger logger = LoggerFactory.getLogger(NettyController.class);

    @PostMapping("message/send")
    public Object messageSend(@RequestBody String message){
        try{
            ChatClient.nettyClient.sendMessage(message);
            return new ResponseResult(0, ResponseResult.ResponseState.SUCCESS, new HashMap<>(), "消息发送成功");
        }catch (Exception e){
            return new ResponseResult(1, ResponseResult.ResponseState.FAILURE, new ArrayList(), e.getMessage());
        }
    }
}
