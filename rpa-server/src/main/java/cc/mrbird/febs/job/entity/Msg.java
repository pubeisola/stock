package cc.mrbird.febs.job.entity;

import java.util.HashMap;
import java.util.Map;

public class Msg {
    private int code;
    private String message;
    //用户要返回给浏览器的数据
    private Map<String,Object> data=new HashMap<String,Object>();

    //    public final static String Head = "{\"resultCode\":\"200\",\n"+"\"message\":\"success\",\n"+"\"data\":{";
//    public final static String HeadError = "{\"resultCode\":\"500\",\n"+"\"message\":\"Error\",\n"+"\"data\":{";
//    public final static String Foot = "\n}}";
    public static Msg success(String msg) {
        Msg result=new Msg();
        result.setCode(200);
        if (msg != null){
            result.setMsg(msg);
        }else {
            result.setMsg("success");
        }
        return result;
    }
    public static Msg success() {
        Msg result=new Msg();
        result.setCode(200);
        result.setMsg("success");
        return result;
    }
    public static Msg fail(String msg) {
        Msg result=new Msg();
        result.setCode(500);
        if (msg != null){
            result.setMsg(msg);
        }else {
            result.setMsg("Error");
        }
        return result;
    }
    public Msg add(String key,Object value) {
        this.getdata().put(key, value);
        return this;
    }

    public int getCode() {
        return code;
    }
    public void setCode(int code) {
        this.code = code;
    }
    public String getMsg() {
        return message;
    }
    public void setMsg(String message) {
        this.message = message;
    }
    public Map<String, Object> getdata() {
        return data;
    }
    public void setdata(Map<String, Object> data) {
        this.data = data;
    }

}