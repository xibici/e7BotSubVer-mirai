import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class timeListener {

    public static void main(String[] args) throws InterruptedException {
        Timer t = new Timer();
        TimerTask autoWordCloudTask=new TimerTask() {
            @Override
            public void run() {
                System.out.println("hello world");
            }
        };

        try {

            SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");
            //String tempTimeStr=ft.format(new Date());
            String customDateStr="17:14:00";

            Date dateIn8pm = ft.parse(customDateStr);

            System.out.println(customDateStr);
            t.schedule(autoWordCloudTask, dateIn8pm);


        } catch (ParseException e) {
            e.printStackTrace();
        }



    }



}
