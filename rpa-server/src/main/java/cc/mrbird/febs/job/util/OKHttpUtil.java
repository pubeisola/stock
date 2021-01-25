package cc.mrbird.febs.job.utils;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.MultipartConfigElement;

import java.io.IOException;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.*;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import java.util.Iterator;

public class OKHttpUtil {

  private static OkHttpClient okHttpClient;

  @Autowired
  public OKHttpUtil(OkHttpClient okHttpClient) {
    OKHttpUtil.okHttpClient = okHttpClient;
  }


  /**
   * POST post 模式请求
   *
   * Post请求发送JSON数据....{"name":"zhangsan","pwd":"123456"} 参数一：请求Url 参数二：请求的JSON
   * 参数三：请求回调
   */
  public static String postJsonParams(String url, String jsonParams, String token) {

      RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonParams);
      Request request         = new Request.Builder()
                                           .url(url)
                                           .post(requestBody)
                                           .addHeader("token", token)    // 设置请求的token
                                           .build();
      Response response       = null;

      try {
        response = okHttpClient.newCall(request).execute();
        // int status = response.code();
        if (response.isSuccessful()) {
          return response.body().string();
        }
      } catch (Exception e) {

        System.out.println("rest request ====" + e.getMessage());

        //return "{\"code\":\"0\"}";  测试使用
        return null;  // 如果出现异常返回null
      } finally {
        if (response != null) {
          response.close();
        }
      }
      return "";
  }

  /**
   * GET 模式请求
   *
   * @param url 请求的url
   * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
   * @return
   */
  public static String get(String url, Map<String, String> queries) {

    StringBuffer sb = new StringBuffer(url);
    if (MapUtils.isNotEmpty(queries)) {
      boolean firstFlag = true;
      Iterator iterator = queries.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry entry = (Map.Entry<String, String>) iterator.next();
        if (firstFlag) {
          sb.append("?" + entry.getKey() + "=" + entry.getValue());
          firstFlag = false;
        } else {
          sb.append("&" + entry.getKey() + "=" + entry.getValue());
        }
      }
    }

    Request request   = new Request.Builder().url(sb.toString()).build();
    Response response = null;

    try {
      response = okHttpClient.newCall(request).execute();
      //int status = response.code();
      if (response.isSuccessful()) {
        return response.body().string();
      }
    } catch (Exception e) {
      return null;
    } finally {
      if (response != null) {
        response.close();
      }
    }

    return "";
  }
}
