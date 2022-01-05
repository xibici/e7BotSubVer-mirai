
package com.xjh.config;

public class Configure {


    public static int account;
    public static String password;
    public static String fileBasePath;

    public static int WordFrequenciesToReturn;
    public static int MinWordLength;
    public static int MaxWordLength;
    public static int circleDiam;
    public static int gradientStepsC1AndC2;
    public static int gradientStepsC2AndC3;
    public static int minFontSize;
    public static int maxFontSize;
    public static int color1Num;
    public static int color2Num;
    public static int color3Num;




    //public static String auto11pmDate="yyyy-MM-dd 23:00:00";
    public static String auto11pmDate;
    static {
        //读取文件
        //account=401460519;
        //password="Zyl1996223";
        if(System.getProperties().getProperty("os.name").equals("Windows 10")){
            fileBasePath="F:\\e7\\";
            }else {
            fileBasePath="C:\\e7\\";
            }

    }

}
