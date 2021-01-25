package cc.mrbird.febs.api.controller;

import cc.mrbird.febs.common.authentication.ShiroHelper;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserDataPermissionService;
import cc.mrbird.febs.system.service.IUserService;
import cc.mrbird.febs.api.service.IRpaTaskListService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.session.ExpiredSessionException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import cc.mrbird.febs.common.entity.QueryRequest;
import java.util.*;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cc.mrbird.febs.api.entity.RpaTaskList;

/**
 * @author chenggafeng
 */
//@Controller("systemView")
@RequiredArgsConstructor
public class RpaTaskController extends BaseController {

    private final IRpaTaskListService rpaTaskListService;

    /**
    * 任务列表 /api/v1/tasks/list
    * 1. 执行的任务列表
    *  20200616
    * chenggaofeng
    **/
    @PostMapping("/api/v1/tasks/list")
    @ResponseBody
    public String list(@RequestBody String param, HttpServletRequest request, QueryRequest qq) {
        try {
            // 无效参数时返回数据
            if (param == null || (param == "")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("isSuccess", false);
                map.put("code", "0x0001");
                map.put("error", "任务列表无效参数");
                int[] data = new int[] { };
                map.put("data", data);
                // {"isSuccess":false,"code":"0x0001","error":"无效参数","data":null}
                JSONObject json = new JSONObject(map);

                return json.toJSONString();
            }

            // 获取查询参数
            JSONObject jo = new JSONObject();
            JSONObject m = jo.parseObject(param);

            StringBuffer keyWordBuffer = new StringBuffer("");
            JSONObject jsonObject = (JSONObject) JSONObject.parseObject(param).getJSONObject("where");
            String status =  (String) jsonObject.getOrDefault("status", "");  // 任务运行状态

            // name  模糊搜索
            if (jsonObject != null) {
                JSONObject targetJsonObjecttt = jsonObject.getJSONObject("name");
                if (targetJsonObjecttt != null) {
                    String name = (String) targetJsonObjecttt.getOrDefault("$like", "");
                    keyWordBuffer.append(name);
                }

            }
            String keyword   = keyWordBuffer.toString();   // 搜索关键字
            // 搜索创建时间
            String startTime = "";
            String endTime   = "";
            if (jsonObject != null) {
                JSONObject targetJsonObjecttt2 = jsonObject.getJSONObject("createdAt");
                if (targetJsonObjecttt2 != null) {
                    JSONArray bodyJsonArrayw = jsonObject.getJSONArray("$between");
                    if (bodyJsonArrayw != null ) {
                        JSONObject   jsonObjecttt = (JSONObject)bodyJsonArrayw.get(0);
                        if (jsonObjecttt != null) {
                            startTime = jsonObjecttt.toString();  // 搜索开始时间
                        }
                        JSONObject   jsonObjecttt2 = (JSONObject)bodyJsonArrayw.get(0);
                        if (jsonObjecttt2 != null) {
                            startTime = jsonObjecttt2.toString();  // 搜索开始时间
                        }
                    }
                }
            }


            // 登录成功返回数据
            //data field
            Map<String, Object> data = new HashMap<String, Object>();

            // 获取总记录数
            String total = rpaTaskListService.getTaskTotal(keyword) + ""; // 获取插件的总记录
            // 获取当前的页码  默认第一页
            String pageIndex =  m.getOrDefault("pageIndex", "1") + "";   // 页码
            String pageSize  =  m.getOrDefault("pageSize", "10") + "";   // 页大小
            int pageSizeInt = 10;

            try {
                pageSizeInt = Integer.valueOf(pageSize).intValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            int offset = 0;
            try {
                offset = Integer.valueOf(pageIndex).intValue();
                offset = (offset - 1) *  pageSizeInt;    // 计算当前页面的偏移量   任务列表每页10个执行任务
                if (offset <= 0) {
                    offset = 0;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            List<RpaTaskList> pageList = rpaTaskListService.findByPage((offset + pageSizeInt) +"", pageSizeInt,  keyword, startTime, endTime , status, request,  qq);   // 获取当前页数据
            List<RpaTaskList> isMorePageList = rpaTaskListService.findByPage((offset + pageSizeInt) + "", pageSizeInt,  keyword, startTime, endTime , status, request, qq); // 获取下一页数据
            boolean isMore = false;
            if (isMorePageList != null) {
                if (isMorePageList.size() > 0) {
                    isMore = true;  // 还有下一页数据
                }
            }

            data.put("isMore" , isMore);          // 是否还有更多的记录
            data.put("pageIndex" , pageIndex);  // 获取的数据的页码
            data.put("pageSize" , pageSizeInt);           // 获取的当前页的记录数
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
    * 任务编辑
    **/
    @PostMapping("/api/v1/tasks/edit")
    @ResponseBody
    public Object edit(HttpServletRequest request) {
        if (FebsUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(FebsUtil.view("login"));
            return mav;
        }
    }
    
    /**
    * 任务上传
    **/
    @PostMapping("/api/v1/tasks/synchronize/upload")
    @ResponseBody
    public Object upload(HttpServletRequest request) {
        if (FebsUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(FebsUtil.view("login"));
            return mav;
        }
    }
}
