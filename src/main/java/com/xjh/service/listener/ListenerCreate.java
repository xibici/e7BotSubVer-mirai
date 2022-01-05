package com.xjh.service.listener;

import com.xjh.service.MessageService;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.GlobalEventChannel;
import net.mamoe.mirai.event.Listener;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;
import net.mamoe.mirai.message.data.MessageChain;

public  class ListenerCreate {

    public static void creatListener(){

        //开始监听--所有机器人GlobalEventChannel
        //GroupMessageEvent 群消息事件
        // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`



        Listener listenerGroup = GlobalEventChannel.INSTANCE.subscribeAlways(GroupMessageEvent.class, gEvent -> {
            send(gEvent,dealWithMessage(gEvent,"group"),"group");



        });

        Listener listenerFriends = GlobalEventChannel.INSTANCE.subscribeAlways(FriendMessageEvent.class, fEvent -> {
            send(fEvent,dealWithMessage(fEvent,"friend"),"friend");
        });
    }

    //重写群消息
    public static String dealWithMessage(MessageEvent event,String type){
        MessageChain chain=null;
        Contact contact =null;
        String resultStr=null;

        if (type.equals("group")){
            GroupMessageEvent gEvent=(GroupMessageEvent) event;
            chain = gEvent.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
            contact = gEvent.getGroup();
        }else {
            FriendMessageEvent fEvent=(FriendMessageEvent) event;
            chain = fEvent.getMessage(); // 可获取到消息内容等, 详细查阅 `GroupMessageEvent`
            contact = fEvent.getFriend();
        }

        String tStr=chain.serializeToMiraiCode();

        MessageService messageService=new MessageService();

        if (type.equals("group")){
            resultStr= messageService.handleMessage(tStr,event,"group");
        }else {
            resultStr= messageService.handleMessage(tStr,event,"friend");
        }


        return resultStr;
    }



    public static void send(MessageEvent event, String str,String type){

        Contact contact =null;
        if (type.equals("group")){
            GroupMessageEvent gEvent=(GroupMessageEvent) event;
            contact = gEvent.getGroup();
        }else {
            FriendMessageEvent fEvent=(FriendMessageEvent) event;
            contact = fEvent.getFriend();
        }

        //event.getSubject().sendMessage("Hello!"); // 回复消息
        if (str!= null){
            System.out.println("###############################");
            System.out.println(str);
            try {
                MessageChain chain = MessageChain.deserializeFromJsonString(str);
                //不出错就解析
                contact.sendMessage(chain);
            } catch (Exception e) {
                //e.printStackTrace();
                System.out.println("非chain类型str");
                event.getSubject().sendMessage(str); // 回复消息
            }

        }

    }

}
