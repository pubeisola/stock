package cc.mrbird.febs.api.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.annotation.Limit;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.service.ValidateCodeService;
import cc.mrbird.febs.common.utils.Md5Util;
import cc.mrbird.febs.monitor.entity.LoginLog;
import cc.mrbird.febs.monitor.service.ILoginLogService;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import cc.mrbird.febs.common.utils.TokenUtils;
import cc.mrbird.febs.system.entity.User;

/**
 * @author chenggaofeng
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
public class RpaUserController extends BaseController {

    private final IUserService userService;
    private final ValidateCodeService validateCodeService;
    private final ILoginLogService loginLogService;

    /**
    *
    **/
    @PostMapping("/api/v1/users/login/password")
    //@Limit(key = "login", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public String login(@RequestBody String param, HttpServletRequest request) throws FebsException {

        try {
            // 无效参数时返回数据
            if (param == null || (param == "")) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("isSuccess", false);
                map.put("code", "0x0001");
                map.put("error", "无效参数");
                map.put("data", null);
                // {"isSuccess":false,"code":"0x0001","error":"无效参数","data":null}
                JSONObject json = new JSONObject(map);

                return json.toJSONString();
            }

            HttpSession session = request.getSession();
            // validateCodeService.check(session.getId(), verifyCode);  取消验证码检查

            JSONObject jo = new JSONObject();
            Map<String, Object> m = (Map<String, Object>) jo.parse(param);

            String username = (String) m.getOrDefault("username", "");
            String password = (String) m.getOrDefault("password", "");
            String platform = (String) m.getOrDefault("platform", "");
            password = Md5Util.encrypt(username.toLowerCase(), password);

            boolean rememberMe = false;  // rpa 客户端不需要
            UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
            super.login(token);
            // 保存登录日志
            LoginLog loginLog = new LoginLog();
            loginLog.setUsername(username);
            loginLog.setSystemBrowserInfo();
            this.loginLogService.saveLoginLog(loginLog);

            // 登录成功返回数据
            //data field
            Map<String, Object> data = new HashMap<String, Object>();


            User ccUser = userService.findByName(username);
            // 返回的用户数据
            /**
             *
             * lastLoginTime: 1591104457000
             * name: null
             * notic_end_at: "23:59:59"
             * notic_start_at: "00:00:00"
             * role: []
             * userId: 140
             * username: "pubsola"
             *
             */
            int[] role = new int[] { };
            Map<String, Object>  curUser =  new HashMap<String, Object>();
            curUser.put("lastLoginTime", ccUser.getLastLoginTime().getTime());
            curUser.put("notic_end_at", "23:59:59");
            curUser.put("notic_start_at", "00:00:00");
            curUser.put("role", role);
            curUser.put("name", ccUser.getUsername());
            curUser.put("username", ccUser.getUsername());
            curUser.put("userId", ccUser.getId().toString());

            String curToken = TokenUtils.token(username, password);
            data.put("token" , curToken);  // 保存用户token
            data.put("user" , curUser);
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
            map.put("error", "登录名或密码错误");
            map.put("data", null);
            // {"isSuccess":false,"code":"0x0001","error":"登录名或密码错误","data":null}
            JSONObject json = new JSONObject(map);

            return json.toJSONString();
        }
    }
    
    
    @PostMapping("/api/v1/user/info")
    @Limit(key = "user_info", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public FebsResponse user_info(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifyCode);
        password = Md5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }
    
    @PostMapping("/api/v1/user/logout")
    @Limit(key = "user_logout", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public FebsResponse user_logout(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifyCode);
        password = Md5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }
    
    @PostMapping("/api/v1/users/sendCerificationCode")
    @Limit(key = "user_sendCerificationCode", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public FebsResponse sendCerificationCode(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifyCode);
        password = Md5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }
    
    @PostMapping("/api/v1/users/smsRegister")
    @Limit(key = "user_smsRegister", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public FebsResponse smsRegister(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifyCode);
        password = Md5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }
    
    @PostMapping("/api/v1/users/smsLogin")
    @Limit(key = "user_smsLogin", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public FebsResponse smsLogin(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifyCode);
        password = Md5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }
    
    
    @PostMapping("/api/v1/users/smsRecoverPassword")
    @Limit(key = "user_smsRecoverPassword", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public FebsResponse smsRecoverPassword(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifyCode);
        password = Md5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }
    
    @PostMapping("/api/v1/users/smsGetUsername")
    @Limit(key = "user_smsGetUsername", period = 60, count = 10, name = "登录接口", prefix = "limit")
    public FebsResponse smsGetUsername(
            @NotBlank(message = "{required}") String username,
            @NotBlank(message = "{required}") String password,
            @NotBlank(message = "{required}") String verifyCode,
            boolean rememberMe, HttpServletRequest request) throws FebsException {
        HttpSession session = request.getSession();
        validateCodeService.check(session.getId(), verifyCode);
        password = Md5Util.encrypt(username.toLowerCase(), password);
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        super.login(token);
        // 保存登录日志
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setSystemBrowserInfo();
        this.loginLogService.saveLoginLog(loginLog);

        return new FebsResponse().success();
    }
}
