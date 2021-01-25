package cc.mrbird.febs.common.utils;

import cc.mrbird.febs.common.entity.FebsConstant;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.util.UUID;
import cc.mrbird.febs.api.entity.RpaProjectUpload;
import cc.mrbird.febs.api.entity.RpaTaskLisuser;
import cc.mrbird.febs.api.entity.RpaTaskCronRule;
import java.io.*;
import java.nio.charset.Charset;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.util.*;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * rpa 工程文件上传
 *
 * @author chenggaofeng
 */
@Slf4j
public class ProjectFileUploadUtil {

    /**
     * 压缩文件或目录
     *
     * @param fromPath 上传的文件
     * @param storePath   压缩文件，如 xx.zip    工作流工程文件  json 格式
     */
    public static RpaProjectUpload process(MultipartFile file, String storePath) throws Exception {

         try {
             // 获取文件信息
             if (file.isEmpty()) {
                 throw new Exception("上传的文件不能为空");
             }

             String fileName = file.getOriginalFilename();
             String suffixName = fileName.substring(fileName.lastIndexOf("."));
             String filePath = "d:\\";
             if (storePath.length() <= 0) {
                 filePath = "d:\\";     // 为空是是磁盘存储路径
             } else {
                 filePath = storePath;
             }

             String path = filePath + fileName;
             File dest = new File(path);
             // 如果父目录不存在就新建
             if (!dest.getParentFile().exists()) {
                 dest.getParentFile().mkdirs();
             }
             // 保存文件到上传目录
             file.transferTo(dest);

             StringBuffer sBuffer = new StringBuffer("");

             // 直接从zip 中读取文件   路径为 dest
             ZipFile zf         = new ZipFile(path);
             InputStream in     = new BufferedInputStream(new FileInputStream(path));
             Charset utf8        = Charset.forName("utf8");
             ZipInputStream zin = new ZipInputStream(in, utf8);
             ZipEntry ze;
             while ((ze = zin.getNextEntry()) != null) {
                 if (ze.isDirectory()) {
                     // 如果是目录不用管
                 } else {
                     if (ze.toString().endsWith("json")) {
                         BufferedReader br = new BufferedReader(
                                 new InputStreamReader(zf.getInputStream(ze)));

                         String line;
                         while ((line = br.readLine()) != null) {
                             sBuffer.append(line.toString());     // 读取配置文件的行数据
                         }
                         br.close();
                     }}
             }
             zin.closeEntry();

             // 解析上传的json 工程文件新到数据库的存储格式
             // 工程配置文件
             String jsonProjectConfig  = sBuffer.toString();
             // 用fastJson 解析json 数据
             JSONObject jo = new JSONObject();
             Map<String, Object> m = (Map<String, Object>) jo.parse(jsonProjectConfig);

             // 工程配置信息 获取
             String project_name   = (String) m.getOrDefault("project_name", "");
             String createAt       = (String) m.getOrDefault("createAt", "");
             String updateAt       = (String) m.getOrDefault("updateAt", "");
             String project_type   = "cloud";  //rpa  设计器里的工程是本地工程(local)   上传到服务器变成云端工程(cloud)
             String cron           = (String) m.getOrDefault("cron", "");
             String retry_count    = (String) m.getOrDefault("retry_count", "");
             String retry_interval = (String) m.getOrDefault("retry_interval", "");
             String time_out       = (String) m.getOrDefault("time_out", "");
             String description    = (String) m.getOrDefault("description", "");
             boolean  automatic_recording = (boolean)m.getOrDefault("automatic_recording", "");

             // 构建工程对象
             // 构建cron 对象
             /**
              * rpa 上传的工程对应的cron 规则信息
              */
             RpaTaskCronRule rpaTaskCronRule = new RpaTaskCronRule();  // 对应工程要执行的cron规则    RPA 设计器上传的工程
             rpaTaskCronRule.setCronRule("cron");
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             Date startat = sdf.parse(createAt);
             rpaTaskCronRule.setCreateat(startat);
             Date updateat = sdf.parse(updateAt);
             rpaTaskCronRule.setUpdatedat(updateat);

             /**
              * rpa 上传的工程信息
              */
             RpaTaskLisuser rpaTaskLisuser = new RpaTaskLisuser();   // 个人工程信息   RPA 设计器上传的工程
             rpaTaskLisuser.setUserId("1");  // 根据当前会话id 查询用户id
             rpaTaskLisuser.setCompanyId(0);
             rpaTaskLisuser.setUserId2(1);
             rpaTaskLisuser.setPlationId(1);
             rpaTaskLisuser.setCreateat(startat);
             rpaTaskLisuser.setUpdatedat(updateat);
             rpaTaskLisuser.setTemporary1("");
             rpaTaskLisuser.setStatus("");
             rpaTaskLisuser.setRuleid(0L);         // 规则表返回的id   插入表格时返回重新赋值
             rpaTaskLisuser.setProjectType(project_type);
             rpaTaskLisuser.setProjectName(project_name);
             rpaTaskLisuser.setProjectCode(project_name);
             rpaTaskLisuser.setLoginname("");     // 在存储入数据库时重新赋值
             rpaTaskLisuser.setDeviceidname("");
             Date deletedat = sdf.parse("1970-01-01 00:00:00");
             rpaTaskLisuser.setDeletedat(deletedat);
             rpaTaskLisuser.setIsRecordScreen(automatic_recording ? 1 : 0);

             int myRetryCount = 0;
             try {
                 myRetryCount = Integer.parseInt(retry_count);
             } catch (NumberFormatException e) {
                 e.printStackTrace();
             }

             rpaTaskLisuser.setRetryCount(myRetryCount);  // 设置工程调度的重试次数

             int myretry_interval = 0;
             try {
                 myretry_interval = Integer.parseInt(retry_interval);
             } catch (NumberFormatException e) {
                 e.printStackTrace();
             }
             rpaTaskLisuser.setRetryRange(myretry_interval);   // 工程cron 的重试间隔

             int myTime_out = 0;
             try {
                 myTime_out = Integer.parseInt(time_out);
             } catch (NumberFormatException e) {
                 e.printStackTrace();
             }
             rpaTaskLisuser.setTimeout(myTime_out);

             RpaProjectUpload rpaProjectUpload = new RpaProjectUpload();  // 构造返回数据的对象组合  包括工程数据和工程调度规则
             rpaProjectUpload.setRpaTaskLisuser(rpaTaskLisuser);
             rpaProjectUpload.setRpaTaskCronRule(rpaTaskCronRule);

             // 返回最终的解析格式
             return rpaProjectUpload;
         } catch (IllegalStateException e) {
             throw e;
             //e.printStackTrace();
         } catch (IOException e) {
             throw e;
             //e.printStackTrace();
         } catch (Exception e) {
             throw e;
         }
    }

}
