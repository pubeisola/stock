package cc.mrbird.febs.api.controller;



import cc.mrbird.febs.api.entity.RpaPluginView;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.MenuTree;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.entity.Menu;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IMenuService;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;
import cc.mrbird.febs.common.entity.QueryRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.*;
import cc.mrbird.febs.api.service.IRpaPluginUserService;
import cc.mrbird.febs.api.service.IRpaPluginConfigService;
import cc.mrbird.febs.api.service.impl.RpaPluginConfigServiceImpl;
import cc.mrbird.febs.api.service.impl.RpaPluginUserServiceImpl;
import javax.servlet.http.HttpServletRequest;

/**
 * @author chenggafeng
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RpaPluginController extends BaseController {

    private final IRpaPluginConfigService rpaPluginConfigService;
    private final IRpaPluginUserService rpaPluginUserService;

    /**
     * 获取插件列表
     *  RPA  UIauto UiAuto 1.1.2 版本
     *  1. 全部插件接口使用
     **/
    @PostMapping("/api/v1/plugins/uiautoPluginList")
    public  String  getPluginsAll(@RequestBody String param,  QueryRequest request) throws FebsException {

        try {
            // 无效参数时返回数据
            if (param == null || (param == "")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("isSuccess", false);
                map.put("code", "0x0001");
                map.put("error", "无效参数");
                int[] data = new int[] { };
                map.put("data", data);
                // {"isSuccess":false,"code":"0x0001","error":"无效参数","data":null}
                JSONObject json = new JSONObject(map);

                return json.toJSONString();
            }

            JSONObject jo = new JSONObject();
            Map<String, Object> m = (Map<String, Object>) jo.parse(param);

            StringBuffer keyWordBuffer = new StringBuffer("");
            JSONObject jsonObject = (JSONObject) JSONObject.parseObject(param).getJSONObject("where");
            if (jsonObject != null) {
                JSONArray bodyJsonArray = jsonObject.getJSONArray("$or");
                JSONObject targetJsonObject = bodyJsonArray.getJSONObject(0);
                JSONObject targetJsonObjecttt = targetJsonObject.getJSONObject("plugin_id");
                String platform = (String) m.getOrDefault("$like", "");
                keyWordBuffer.append(platform);
            }
            String keyword   = keyWordBuffer.toString();

            // 登录成功返回数据
            //data field
            Map<String, Object> data = new HashMap<String, Object>();

            // 获取总记录数
            String total = rpaPluginConfigService.getPluginTotal(keyword) + ""; // 获取插件的总记录
            // 获取当前的页码  默认第一页
            String pageIndex =  m.getOrDefault("pageIndex", "1") + "";

            int offset = 0;
            try {
                offset = Integer.valueOf(pageIndex).intValue();
                offset = (offset - 1) *  6;    // 计算当前页面的偏移量
                if (offset <= 0) {
                    offset = 0;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            List<RpaPluginView> pageList = rpaPluginConfigService.findByPage(offset +"", keyword);
            List<RpaPluginView> isMorePageList = rpaPluginConfigService.findByPage((offset + 6) + "", keyword); // 获取下一页数据
            boolean isMore = false;
            if (isMorePageList != null) {
                if (isMorePageList.size() > 0) {
                    isMore = true;  // 还有下一页数据
                }
            }

            data.put("isMore" , isMore);          // 是否还有更多的记录
            data.put("pageIndex" , pageIndex);  // 获取的数据的页码
            data.put("pageSize" , 6);           // 获取的当前页的记录数
            data.put("total" , total);          // 获取的数据的总记录
            data.put("list" , pageList);              // 查询的当前页数据列表

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", true);
            map.put("code", "0x0001");
            map.put("data", (Object)data);

            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        } catch (Exception e) {
            // 登录异常返回数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", false);
            map.put("code", "0x0001");
            map.put("error", e.getMessage());

            int[] data = new int[] { };
            map.put("data", data);
            // {"isSuccess":false,"code":"0x0001","error":"登录名或密码错误","data":null}
            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        }
    }


    /**
    * 获取插件列表
     * RPA  UIauto UiAuto 1.1.0 版本
     *  1. 全部插件接口使用
    **/
    @PostMapping("/api/v1/plugins/base/user/list")
    public  String  getPluginsAllOld(@RequestBody String param,  QueryRequest request) throws FebsException {

        try {
            // 无效参数时返回数据
            if (param == null || (param == "")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("isSuccess", false);
                map.put("code", "0x0001");
                map.put("error", "无效参数");
                int[] data = new int[] { };
                map.put("data", data);
                // {"isSuccess":false,"code":"0x0001","error":"无效参数","data":null}
                JSONObject json = new JSONObject(map);

                return json.toJSONString();
            }

            JSONObject jo = new JSONObject();
            Map<String, Object> m = (Map<String, Object>) jo.parse(param);

            StringBuffer keyWordBuffer = new StringBuffer("");
            JSONObject jsonObject = (JSONObject) JSONObject.parseObject(param).getJSONObject("where");
            if (jsonObject != null) {
                JSONArray bodyJsonArray = jsonObject.getJSONArray("$or");
                JSONObject targetJsonObject = bodyJsonArray.getJSONObject(0);
                JSONObject targetJsonObjecttt = targetJsonObject.getJSONObject("plugin_id");
                String platform = (String) m.getOrDefault("$like", "");
                keyWordBuffer.append(platform);
            }
            String keyword   = keyWordBuffer.toString();

            // 登录成功返回数据
            //data field
            Map<String, Object> data = new HashMap<String, Object>();

            // 获取总记录数
            String total = rpaPluginConfigService.getPluginTotal(keyword) + ""; // 获取插件的总记录
            // 获取当前的页码  默认第一页
            String pageIndex =  m.getOrDefault("pageIndex", "1") + "";

            int offset = 0;
            try {
                offset = Integer.valueOf(pageIndex).intValue();
                offset = (offset - 1) *  6;    // 计算当前页面的偏移量
                if (offset <= 0) {
                    offset = 0;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            List<RpaPluginView> pageList = rpaPluginConfigService.findByPage(offset +"", keyword);
            List<RpaPluginView> isMorePageList = rpaPluginConfigService.findByPage((offset + 6) + "", keyword); // 获取下一页数据
            boolean isMore = false;
            if (isMorePageList != null) {
                if (isMorePageList.size() > 0) {
                    isMore = true;  // 还有下一页数据
                }
            }

            data.put("isMore" , isMore);          // 是否还有更多的记录
            data.put("pageIndex" , pageIndex);  // 获取的数据的页码
            data.put("pageSize" , 6);           // 获取的当前页的记录数
            data.put("total" , total);          // 获取的数据的总记录
            data.put("list" , pageList);              // 查询的当前页数据列表

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", true);
            map.put("code", "0x0001");
            map.put("data", (Object)data);

            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        } catch (Exception e) {
            // 登录异常返回数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", false);
            map.put("code", "0x0001");
            map.put("error", e.getMessage());

            int[] data = new int[] { };
            map.put("data", data);
            // {"isSuccess":false,"code":"0x0001","error":"登录名或密码错误","data":null}
            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        }
    }
    
    /**
     * 获取用户插件列表   全部和 本地 都有使用
     *  1. 传插件id 只获取对应的插件id 的信息
     *  2. 没有传插件id 获取所有插件
     *  3. 插件全部接口使用
     *  4. 插件本地接口使用
     *  RPA  UIauto UiAuto 1.1.0 版本
     */
    @PostMapping("/api/v1/plugins/base/user/views")
    public  String  getPluginsAllUser(@RequestBody String param, HttpServletRequest request) throws FebsException {

        try {
            // 无效参数时返回数据
            if (param == null || (param == "")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("isSuccess", false);
                map.put("code", "0x0001");
                map.put("error", "无效参数");
                int[] data = new int[] { };
                map.put("data", data);
                // {"isSuccess":false,"code":"0x0001","error":"无效参数","data":null}
                JSONObject json = new JSONObject(map);

                return json.toJSONString();
            }

            JSONObject jo = new JSONObject();
            JSONObject m = jo.parseObject(param);

            StringBuffer plugin_idBuffer = new StringBuffer("");
            JSONArray jsonObject = m.getJSONArray("plugin_id");;
            if (jsonObject != null) {
                for(int i = 0; i < jsonObject.size(); i++) {
                    String platform = (String) m.getOrDefault("$like", "");

                    if (plugin_idBuffer.length() <= 0) {
                        plugin_idBuffer.append(platform);
                    } else {
                        plugin_idBuffer.append(",");
                        plugin_idBuffer.append(platform);
                    }
                }
            }

            String plugin_ids   = plugin_idBuffer.toString();  // 插件id
            String user_id = request.getHeader("Authorization");
            if (user_id == null) {
                user_id = "";
            }

            String[] auth = user_id.split(" ");
            StringBuffer user_idBuffer = new StringBuffer("");
            if (auth != null) {
                if (auth.length == 2) {
                    user_idBuffer.append(auth[1]);
                }
            }
            String authKey = user_idBuffer.toString();

            // 返回根据插件id 检索的我的所有插件
            List<RpaPluginView> pageList = rpaPluginUserService.findUserAllPlugin(user_id, plugin_ids);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", true);
            map.put("code", "0x0001");
            map.put("data", (Object)pageList);

            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        } catch (Exception e) {
            // 登录异常返回数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", false);
            map.put("code", "0x0001");
            map.put("error", e.getMessage());

            int[] data = new int[] { };
            map.put("data", data);
            // {"isSuccess":false,"code":"0x0001","error":"登录名或密码错误","data":null}
            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        }
    }

    /**
     * 获取用户插件列表   全部和 本地 都有使用
     *  1. 传插件id 只获取对应的插件id 的信息
     *  2. 没有传插件id 获取所有插件
     *  3. 插件全部接口使用
     *  4. 插件本地接口使用
     *
     *  RPA  UIauto UiAuto 1.1.2 版本
     */
    @PostMapping("/api/v1/plugins/base/admin/views")
    public  String  getPluginsAllAdmin(@RequestBody String param, HttpServletRequest request) throws FebsException {

        try {
            // 无效参数时返回数据
            if (param == null || (param == "")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("isSuccess", false);
                map.put("code", "0x0001");
                map.put("error", "无效参数");
                int[] data = new int[] { };
                map.put("data", data);
                // {"isSuccess":false,"code":"0x0001","error":"无效参数","data":null}
                JSONObject json = new JSONObject(map);

                return json.toJSONString();
            }

            JSONObject jo = new JSONObject();
            JSONObject m = jo.parseObject(param);

            StringBuffer plugin_idBuffer = new StringBuffer("");
            JSONArray jsonObject = m.getJSONArray("plugin_id");  //1.1.2 版本改成plugin_name
            if (jsonObject != null) {
                for(int i = 0; i < jsonObject.size(); i++) {
                    String platform = (String) m.getOrDefault("$like", "");

                    if (plugin_idBuffer.length() <= 0) {
                        plugin_idBuffer.append(platform);
                    } else {
                        plugin_idBuffer.append(",");
                        plugin_idBuffer.append(platform);
                    }
                }
            }

            String plugin_ids   = plugin_idBuffer.toString();  // 插件id
            String user_id = request.getHeader("Authorization");
            if (user_id == null) {
                user_id = "";
            }

            String[] auth = user_id.split(" ");
            StringBuffer user_idBuffer = new StringBuffer("");
            if (auth != null) {
                if (auth.length == 2) {
                    user_idBuffer.append(auth[1]);
                }
            }
            String authKey = user_idBuffer.toString();

            // 返回根据插件id 检索的我的所有插件   1.1.2 版本改成plugin_name
            List<RpaPluginView> pageList = rpaPluginUserService.findUserAllPlugin(user_id, plugin_ids);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", true);
            map.put("code", "0x0001");
            map.put("data", (Object)pageList);

            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        } catch (Exception e) {
            // 登录异常返回数据
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", false);
            map.put("code", "0x0001");
            map.put("error", e.getMessage());

            int[] data = new int[] { };
            map.put("data", data);
            // {"isSuccess":false,"code":"0x0001","error":"登录名或密码错误","data":null}
            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        }
    }


    /**
    * 获取用户云端工程列表
     *
     *  拉取云端上传的工程
     *  在RPA客户端 项目库页面
    **/
    @PostMapping("/api/v1/uiautoClients/getCloudProjects")
    public  String getCloudProjects(@RequestBody String param, HttpServletRequest request) throws FebsException {

        try {
            // 获取认证参数  进行认证检查  todo 后边统一做

            /**
             *  无效参数时返回数据
             *  本页面只传入设备id
             *  请求负载样例
             *  {deviceId: "ecddab23d334cecccb963abf47ba2668289eb6a1"}
             */
            if (param == null || (param == "")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("isSuccess", false);
                map.put("code", "0x0001");
                map.put("error", "无效参数");
                int[] data = new int[] { };
                map.put("data", data);
                JSONObject json = new JSONObject(map);

                return json.toJSONString();
            }

            JSONObject jo = new JSONObject();
            Map<String, Object> m = (Map<String, Object>) jo.parse(param);   // 解析json 提交的 json 参数

            StringBuffer keyWordBuffer = new StringBuffer("");
            JSONObject jsonObject = (JSONObject) JSONObject.parseObject(param);   // 提取的获取的设备id 参数
            if (jsonObject != null) {
                JSONObject targetJsonObject = jsonObject.getJSONObject("deviceId");  // 设备端的设备id
                String curDeviceId =  (String) jsonObject.toString();  // 任务运行状态

                keyWordBuffer.append(curDeviceId);
            }
            String deviceId   = keyWordBuffer.toString();   // 根据设备id deviceId  获取云端项目数据

            // 获取 云端的项目库的项目 不分页获取本次所有列表 todo
            List<RpaPluginView> pageList = rpaPluginConfigService.findByPage("0", deviceId);

            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", true);
            map.put("code", "0x0001");
            map.put("data", (Object)pageList);  // 返回的项目数据

            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        } catch (Exception e) {
            // 登录异常返回数据   异常返回
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("isSuccess", false);
            map.put("code", "0x0001");
            map.put("error", e.getMessage());

            int[] data = new int[] { };
            map.put("data", data);
            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        }
    }
}
