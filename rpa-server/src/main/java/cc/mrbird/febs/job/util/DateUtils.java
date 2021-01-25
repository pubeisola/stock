package cc.mrbird.febs.job.util;

import javax.xml.crypto.Data;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static SimpleDateFormat DFYMD = new SimpleDateFormat("yyyyMMdd");//设置日期格式
    public static SimpleDateFormat DFYMDSFM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    public static SimpleDateFormat DFYMDS = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM


    public static String getYMD(){
        Date data = new Date();
        return DFYMD.format(data);
    }

    public static String getDateTime(){
        Date date = new Date();
        return DFYMDSFM.format(date);
    }
    public static Date StrtoDate(String date) throws ParseException {
        return DFYMDS.parse(date);
    }

    public static void main(String args[]){
        System.out.println(getYMD());
        String filName = "aaa.txt";
        File file=new File("D:\\upload\\"+getYMD()+"\\"+filName);
        System.out.println("D:\\upload\\"+getYMD()+"\\"+filName);
        System.out.println(file.exists());
        System.out.println(getDateTime());
        Date date = new Date();
        System.out.println(date.getTime());
    }

}
