package com.xjh.service.timmer;

import com.xjh.config.Configure;
import com.xjh.service.record.RecordService;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChain;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimmerService {

    public void CreateTimerTask(){
        ContactList<Group> groups = initGroupList();
        for (Group group:groups){
            Contact contact=group;

            try {

                //System.out.println("Start..." + new Date());
                // delay 5 seconds
                Thread.sleep(1000);
                //System.out.println("End..." + new Date());

            } catch (InterruptedException e) {
                System.err.format("IOException: %s%n", e);
            }

            timerRun(contact);
        }
        //ContactTemp重写 都没有重写方法 记住了!!!!!
        //Contact contact = bot.getGroup(goupId);

    }

    public ContactList<Group> initGroupList(){



        Bot bot = Bot.getInstance(Configure.account);
        ContactList<Group> groups = bot.getGroups();
        System.out.println("groups:"+groups);
        return groups;
    }

    public void timerRun(Contact contact) {
        RecordService recordService=new RecordService();
        // 一天的毫秒数
        long daySpan = 24 * 60 * 60 * 1000;
        // 规定的每天时间15:33:30运行
        SimpleDateFormat sdf = new SimpleDateFormat(Configure.auto11pmDate);
        // 首次运行时间
        try {
            Date startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sdf.format(new Date()));
            // 如果今天的已经过了 首次运行时间就改为明天
            if (System.currentTimeMillis() > startTime.getTime()) {
                startTime = new Date(startTime.getTime() + daySpan);
            }
            Timer t = new Timer();

            //任务修改地方-------
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    String wcTaskStr = recordService.doWordCloudTask(contact);
                    MessageChain chain = MessageChain.deserializeFromJsonString(wcTaskStr);
                    //不出错就解析
                    contact.sendMessage(chain);
                    System.out.print("定时器执行");
                }
            };


            // 以每24小时执行一次
            t.schedule(task, startTime, daySpan);

            System.out.println("定时器设置成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
