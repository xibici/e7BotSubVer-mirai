package com.xjh.service.record;

import com.xjh.config.Configure;
import com.xjh.service.login.LoginService;
import net.mamoe.mirai.contact.Contact;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;


public class RecordService {


    /*
     * GroupMessageEvent(group=703413626, senderName=あみやみや,
     * sender=1925493468, permission=OWNER,
     * message=[mirai:source:[1489],[312340638]]hjh)
     * */

    public void writeRecordInFile(GroupMessageEvent gEvent) {


        System.out.println(gEvent);
        MessageChain message = gEvent.getMessage();
        String tStr=" ";


        String onlyStr="";
        System.out.println(message.size());
        try {

            if (message.size()>2){
                String tempStr;
                for (int i = 0; i < message.size(); i++) {

                    tempStr=message.get(i).contentToString();
                    System.out.println("tempStr:"+tempStr);
                    if (!tempStr.toLowerCase().contains("mirai")){
                        onlyStr=onlyStr+message.get(i).contentToString();
                    }

                }
                System.out.println("onlyStr:"+onlyStr);
                tStr=onlyStr;

            }else {
                tStr=gEvent.getMessage().serializeToMiraiCode();

                if (tStr.contains("mirai")){
                    tStr=" ";

                }
            }


        } catch (Exception e) {

            tStr=gEvent.getMessage().serializeToMiraiCode();

            if (tStr.contains("mirai")){
                tStr=" ";

            }

            e.printStackTrace();
        }




        Contact contact = gEvent.getGroup();
        Long groupId= contact.getId();
        String groupIdStr = Long.toString(groupId);

        Member sender = gEvent.getSender();
        Long groupQQId = sender.getId();






        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        String dateString=ft.format(dNow);
        try {
            String fileUrl;
            String dirUrl;
            fileUrl = Configure.fileBasePath+"record\\"+groupIdStr+"\\"+dateString+"#"+groupIdStr +".txt";
            dirUrl= Configure.fileBasePath+"record\\"+groupIdStr;
            /*if(System.getProperties().getProperty("os.name").equals("Windows 10")){
                fileUrl = Configure.fileBasePath+"record\\"+groupIdStr+"\\"+dateString+"#"+groupIdStr +".txt";
                dirUrl= Configure.fileBasePath+"record\\"+groupIdStr;
            }else {
                fileUrl=Configure.fileBasePath+"record/"+groupIdStr+"/"+dateString+"#"+groupIdStr +".txt";
                dirUrl= Configure.fileBasePath+"record/"+groupIdStr;
            }*/
            System.out.println(fileUrl);

            File file = new File(fileUrl);// 指定要写入的文件
            File dirFile = new File(dirUrl);// 指定要写入的文件

            if (!dirFile.isDirectory()) {// 如果文件不存在则创建
                dirFile.mkdir();
            }

            if (!file.exists()) {// 如果文件不存在则创建
                file.createNewFile();
            }
            // 获取该文件的缓冲输出流
            BufferedWriter bufferedWriter = new BufferedWriter
                    (new OutputStreamWriter(new FileOutputStream(file,true), StandardCharsets.UTF_8));
            //BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
            // 写入信息
            bufferedWriter.write(groupQQId+"="+tStr);
            bufferedWriter.newLine();
            bufferedWriter.flush();// 清空缓冲区
            bufferedWriter.close();// 关闭输出流
            ////System.out.println("写入成功---"+name);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    public String doWordCloudTask(Contact contact){
        return loadRecordInFile(contact);
    }

    public List<String> getExclusiveList(){

        List<String> exList=new ArrayList<>();

        String fileUrl;
        fileUrl = Configure.fileBasePath+"record\\exclusive.txt";

        File file = new File(fileUrl);// 指定要写入的文件
        //传输文件过去
        try {

            // 获得该文件的缓冲输入流
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(isr);



            StringBuilder sb=new StringBuilder();//总文本
            String line = "";// 用来保存每次读取一行的内容
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);

                exList.add(line);

            }
            bufferedReader.close();// 关闭输入流

        } catch (Exception e) {
            System.out.println("获取排除词过程失败..");
            e.printStackTrace();
        }

        System.out.println("exList大小:----------"+exList.size());
        return exList;
    }

    public String loadRecordInFile( Contact contact){
        //Contact contact = gEvent.getGroup();
        Long groupId= contact.getId();
        String groupIdStr = Long.toString(groupId);

        MessageChain chain ;

        int qqCount=0;
        int messageCount=0;
        List<WordFrequency> tempWordList=null;


        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
        String dateString=ft.format(dNow);

        String fileUrl;
        fileUrl = Configure.fileBasePath+"record\\"+groupIdStr+"\\"+dateString+"#"+groupIdStr +".txt";
        /*if(System.getProperties().getProperty("os.name").equals("Windows 10")){
            fileUrl = Configure.fileBasePath+"record\\"+groupIdStr+"\\"+dateString+"#"+groupIdStr +".txt";
        }else {
            fileUrl=Configure.fileBasePath+"record/"+groupIdStr+"/"+dateString+"#"+groupIdStr +".txt";
        }*/
        // System.out.println(fileUrl);

        File file = new File(fileUrl);// 指定要写入的文件
        //传输文件过去
        //bufferedImage 接口Image获取

        Map<String,String> loadMap=new HashMap<>();
        String imagePathStr = null;
        InputStream inputStream;

        Set<String> qqSet=new HashSet<>();
        List<String> messageList = new ArrayList<>();


        try {

            // 获得该文件的缓冲输入流
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            inputStream = new FileInputStream(file);

            StringBuilder sb=new StringBuilder();//总文本
            String line = "";// 用来保存每次读取一行的内容
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);

                String[] lineStr = line.split("=");
                List<String> leftRightList = Arrays.asList(lineStr);

                //System.out.println(leftRightList);
                if (leftRightList.size()>1){
                    //loadMap.put(leftRightList.get(0),leftRightList.get(1));
                    qqSet.add(leftRightList.get(0));
                    messageList.add(leftRightList.get(1));
                }

            }
            bufferedReader.close();// 关闭输入流

            System.out.println(qqSet);
            //获取qq列表  重复qq的列表 可以获取消息总数
            qqCount=qqSet.size();
            messageCount=messageList.size();

            //System.out.println(qqSet);
            Map<String,List<WordFrequency>> tempResultMap = generateKuMoWordCloud(inputStream,groupIdStr,qqSet);

           // System.out.println(tempResultMap.keySet());
            imagePathStr = tempResultMap.keySet().iterator().next();
            tempWordList=tempResultMap.values().iterator().next();


            if (imagePathStr==null){
                System.out.println("获取词云图片出错,图片为空");
                return "获取词云图片出错,图片为空";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        ExternalResource externalResource = ExternalResource.create(new File(imagePathStr));
        net.mamoe.mirai.message.data.Image image = contact.uploadImage(externalResource);

        //时分秒
        SimpleDateFormat ft2 = new SimpleDateFormat ("HH:mm");
        String dateStringHM=ft2.format(dNow);
        String tempStr="";
        //获得话题前5
        List<String> topTopics=new ArrayList<>();
        if (tempWordList.size()>=5){
            topTopics.add(tempWordList.get(0).getWord());
            topTopics.add(tempWordList.get(1).getWord());
            topTopics.add(tempWordList.get(2).getWord());
            topTopics.add(tempWordList.get(3).getWord());
            topTopics.add(tempWordList.get(4).getWord());

            tempStr="🎤 今日话题榜 🎤\n"+
                    "📅 "+dateString+"\n"+
                    "⏱ 截至今天"+dateStringHM+"\n"+
                    "🗣️ 本群"+qqCount+"位朋友共产生"+messageCount+"条发言\n"+
                    "🤹‍ 大家今天讨论最多的是：\n\n"+
                    "\t"+tempWordList.get(0).getWord()+":"+tempWordList.get(0).getFrequency()+"\n"+
                    "\t"+tempWordList.get(1).getWord()+":"+tempWordList.get(1).getFrequency()+"\n"+
                    "\t"+tempWordList.get(2).getWord()+":"+tempWordList.get(2).getFrequency()+"\n"+
                    "\t"+tempWordList.get(3).getWord()+":"+tempWordList.get(3).getFrequency()+"\n"+
                    "\t"+tempWordList.get(4).getWord()+":"+tempWordList.get(4).getFrequency()+"\n"+
                    "\n"+
                    "看下有没有你感兴趣的话题? 👏";

        }else {
            topTopics.add(tempWordList.get(0).getWord());
            topTopics.add(tempWordList.get(1).getWord());
            topTopics.add(tempWordList.get(2).getWord());

            tempStr="🎤 今日话题榜 🎤\n"+
                    "📅 "+dateString+"\n"+
                    "⏱ 截至今天"+dateStringHM+"\n"+
                    "🗣️ 本群"+qqCount+"位朋友共产生"+messageCount+"条发言\n"+
                    "🤹‍ 大家今天讨论最多的是：\n\n"+
                    "\t"+tempWordList.get(0).getWord()+":"+tempWordList.get(0).getFrequency()+"\n"+
                    "\t"+tempWordList.get(1).getWord()+":"+tempWordList.get(1).getFrequency()+"\n"+
                    "\t"+tempWordList.get(2).getWord()+":"+tempWordList.get(2).getFrequency()+"\n"+
                    "\n"+
                    "看下有没有你感兴趣的话题? 👏";

        }

        chain= new MessageChainBuilder()
                .append(tempStr)
                .append(Image.fromId(image.getImageId()))
                .build();

        String resultChainStr = MessageChain.serializeToJsonString(chain);

        System.out.println();
        return resultChainStr;
        //return null;//调试 TODO
    }



    public Map<String,List<WordFrequency>> generateKuMoWordCloud(InputStream inputStream,String groupIdStr,Set<String> qqSet){
        String imagePathStr=null;
        List<WordFrequency> wordFrequencies = null;
        List<WordFrequency> withoutQQList=new ArrayList<>();
        try {
            Random ran=new Random();
            //创建一个词语解析器,类似于分词
            FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();

            //每次读取一次读取配置文件
            LoginService.initConfigure();

            //多少个词 setWordFrequenciesToReturn外置
            frequencyAnalyzer.setWordFrequenciesToReturn(Configure.WordFrequenciesToReturn);
            frequencyAnalyzer.setMinWordLength(Configure.MinWordLength);
            frequencyAnalyzer.setMaxWordLength(Configure.MaxWordLength);
            int circleDiam=Configure.circleDiam;
            int gradientStepsC1AndC2=Configure.gradientStepsC1AndC2;//10
            int gradientStepsC2AndC3=Configure.gradientStepsC2AndC3;//20
            int minFontSize=Configure.minFontSize;//10
            int maxFontSize=Configure.maxFontSize;//120
            int color1Num=Configure.color1Num;
            int color2Num=Configure.color2Num;
            int color3Num=Configure.color3Num;
            System.out.println("color1Num:"+color1Num);
            System.out.println("color2Num:"+color2Num);
            System.out.println("color3Num:"+color3Num);

            Color color1=new Color(color1Num);
            Color color2=new Color(color2Num);
            Color color3=new Color(color3Num);

            //这边要注意,引用了中文的解析器
            frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());

            //拿到文档里面分出的词,和词频,建立一个集合存储起来
            //List WF  清除含有qq的
            wordFrequencies = frequencyAnalyzer.load(inputStream);

            for (WordFrequency tempWF:wordFrequencies){
                if (!qqSet.contains(tempWF.getWord())){
                    withoutQQList.add(tempWF);
                }
            }

            wordFrequencies=withoutQQList;

            List<WordFrequency> exWithoutQQList=new ArrayList<>();

            //排除  词语
            List<String> exList = getExclusiveList();
            for (WordFrequency tempWF2:wordFrequencies){
                if (!exList.contains(tempWF2.getWord())){
                    exWithoutQQList.add(tempWF2);
                }
            }
            System.out.println(exList);
            wordFrequencies=exWithoutQQList;


            //记得设置圆的半径
            //设置
            //int circleDiam=1000;

            Dimension dimension = new Dimension(circleDiam, circleDiam);

            //设置图片相关的属性,这边是大小和形状,更多的形状属性,可以从CollisionMode源码里面查找
            WordCloud wordCloud = new WordCloud(dimension, CollisionMode.PIXEL_PERFECT);
            wordCloud.setPadding(2);

            //这边要注意意思,是设置中文字体的,如果不设置,得到的将会是乱码,
            //这是官方给出的代码没有写的,我这边拓展写一下,字体,大小可以设置
            //具体可以参照Font源码
            //linux没有微软雅黑?

            Font font = new Font("Microsoft YaHei", Font.PLAIN, 12);
            wordCloud.setKumoFont(new KumoFont(font));
            wordCloud.setBackgroundColor(new Color(255, 255, 255));
            //因为我这边是生成一个圆形,这边设置圆的半径
            wordCloud.setBackground(new CircleBackground(circleDiam/2));
            //设置颜色
            Color colorPurple=new Color(128, 0, 128);




            //wordCloud.setColorPalette(new ColorPalette(new Color(0xD5CFFA), new Color(0xBBB1FA), new Color(0x9A8CF5), new Color(0x806EF5)));
            /*wordCloud.setColorPalette(
                    new LinearGradientColorPalette(colorPurple, Color.green,Color.yellow,
                            gradientStepsC1AndC2,gradientStepsC2AndC3 ));*/
            wordCloud.setColorPalette(
                    new LinearGradientColorPalette(color1, color2,color3,
                            gradientStepsC1AndC2,gradientStepsC2AndC3 ));



            wordCloud.setFontScalar(new SqrtFontScalar(minFontSize,maxFontSize));
            //将文字写入图片
            wordCloud.build(wordFrequencies);
            //生成图片        wordCloud.writeToFile("output/chinese_language_circle.png");
            Date dNow = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd");
            String dateString=ft.format(dNow);

            imagePathStr=Configure.fileBasePath+"record\\"+groupIdStr+"\\"+dateString+"#"+ UUID.randomUUID() +".png";
            /*if(System.getProperties().getProperty("os.name").equals("Windows 10")){
                imagePathStr=Configure.fileBasePath+"record\\"+groupIdStr+"\\"+dateString+"#"+ UUID.randomUUID() +".png";
            }else {
                imagePathStr=Configure.fileBasePath+"record/"+groupIdStr+"/"+dateString+"#"+ UUID.randomUUID() +".png";
               // fileUrl=Configure.fileBasePath+"record/"+groupIdStr+"/"+dateString +".txt";
            }*/
            //imagePathStr=Configure.fileBasePath+"record\\"+groupIdStr+"\\"+dateString+"#"+ UUID.randomUUID() +".png";
            //Linux TODO
            wordCloud.writeToFile(imagePathStr);


        } catch (IOException e) {
            System.out.println("生成词云过程中出错");
            e.printStackTrace();
        }
        //wordFrequencies
        Map<String,List<WordFrequency>> tempResultMap=new HashMap<>();
        tempResultMap.put(imagePathStr,wordFrequencies);

        return tempResultMap;
    }

}
