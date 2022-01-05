package com.xjh.service;

import com.xjh.service.record.RecordService;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.MessageEvent;


public class MessageService {

    RecordService recordService=new RecordService();

    public String handleMessage(String tStr, MessageEvent event,String type) {

        System.out.println(tStr);
        Contact contact = null;
        if (type.equals("group")) {

            GroupMessageEvent gEvent = (GroupMessageEvent) event;
            contact = gEvent.getGroup();

            recordService.writeRecordInFile(gEvent);

            if (tStr.contains("话题榜#")) {
                try {
                    System.out.println("话题榜");
                    String wordRes = recordService.doWordCloudTask(contact);
                    return wordRes;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

        return null;
    }
}
