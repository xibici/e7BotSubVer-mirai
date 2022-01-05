package com.xjh;

import com.xjh.service.login.LoginService;

public class e7BotMirai {

    public static void main(String[] args) {
        //登陆操作 好像还会重连

        LoginService.initConfigure();
        LoginService.login();


    }
}
