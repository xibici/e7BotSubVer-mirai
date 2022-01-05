package com.xjh.service.login;

import com.xjh.config.Configure;
import com.xjh.service.listener.ListenerCreate;
import com.xjh.service.timmer.TimmerService;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;

import java.io.*;
import java.util.*;

public class LoginService {


    public static void login(){

        BotConfiguration botConfiguration=new BotConfiguration();
        botConfiguration.fileBasedDeviceInfo();
        botConfiguration.noNetworkLog();
        botConfiguration.noBotLog();
        botConfiguration.setProtocol(BotConfiguration.MiraiProtocol.ANDROID_PAD);

        Bot bot = BotFactory.INSTANCE.newBot(
                Configure.account,
                Configure.password,
                botConfiguration
        );

        bot.login();

        //定时任务创建
        TimmerService timmerService = new TimmerService();
        timmerService.CreateTimerTask();

        //创建监听器 结束
        ListenerCreate.creatListener();


    }

    public static void initConfigure() {

        String fileUrl;
        //F:\e7\record
        fileUrl = Configure.fileBasePath+"record\\config.txt";
        /*if(System.getProperties().getProperty("os.name").equals("Windows 10")){
            fileUrl = Configure.fileBasePath+"record\\config.txt";
        }else {
            fileUrl=Configure.fileBasePath+"record/config.txt";
        }*/
        // System.out.println(fileUrl);

        File file = new File(fileUrl);// 指定要写入的文件
        //传输文件过去
        //bufferedImage 接口Image获取

        Map<String,String> loadMap=new HashMap<>();

        try {

            // 获得该文件的缓冲输入流
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

            StringBuilder sb=new StringBuilder();//总文本
            String line = "";// 用来保存每次读取一行的内容
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
                System.out.println("line:"+line);
                if (line.length()>1){
                    if(!line.contains("#")){

                        String[] lineStr = line.split("=");
                        List<String> leftRightList = Arrays.asList(lineStr);
                        loadMap.put(leftRightList.get(0),leftRightList.get(1));
                    }
                }



            }
            bufferedReader.close();// 关闭输入流

            System.out.println(loadMap);

            Configure.account=Integer.parseInt(loadMap.get("account"));
            Configure.password=loadMap.get("password");
            Configure.auto11pmDate=loadMap.get("auto11pmDate");
            Configure.WordFrequenciesToReturn=Integer.parseInt(loadMap.get("WordFrequenciesToReturn"));
            Configure.MinWordLength=          Integer.parseInt(loadMap.get("MinWordLength")    );
            Configure.MaxWordLength=          Integer.parseInt(loadMap.get("MaxWordLength")    );
            Configure.circleDiam=             Integer.parseInt(loadMap.get("circleDiam")  );
            Configure.gradientStepsC1AndC2=   Integer.parseInt(loadMap.get("gradientStepsC1AndC2") );
            Configure.gradientStepsC2AndC3=   Integer.parseInt(loadMap.get("gradientStepsC2AndC3") );
            Configure.minFontSize=   Integer.parseInt(loadMap.get("minFontSize") );
            Configure.maxFontSize=   Integer.parseInt(loadMap.get("maxFontSize") );
            Configure.color1Num=   Integer.parseInt(loadMap.get("color1Num"),16 );
            Configure.color2Num=   Integer.parseInt(loadMap.get("color2Num"),16 );
            Configure.color3Num=   Integer.parseInt(loadMap.get("color3Num"),16 );




        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Bot bot = Mirai.getInstance().getBotFactory().newBot(/*....*/);
}
