/*

import com.xjh.model.ContactTemp.ContactTemp;
import com.xjh.service.record.RecordService;
import net.mamoe.mirai.contact.Contact;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class timeListener3 {

    public void timerRun(Contact contact) {
        RecordService recordService=new RecordService();
        // 一天的毫秒数
        long daySpan = 24 * 60 * 60 * 1000;
        // 规定的每天时间15:33:30运行
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 17:20:00");
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
                    recordService.doWordCloudTask(contact);
                    System.out.print("定时器执行");
                }
            };


            // 以每24小时执行一次
            t.schedule(task, startTime, daySpan);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        ContactTemp makeContact=new ContactTemp();
        Long goupId= 703413626L;
        makeContact.setId(goupId);
        new timeListener3().timerRun(makeContact);
    }
    */
/*public static void main(String[] args) {
        new timeListener3().timerRun();
    }*//*


}*/
