package cc.mrbird.febs.system.controller;

import cc.mrbird.febs.common.authentication.ShiroHelper;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.utils.DateUtil;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.system.entity.User;
import cc.mrbird.febs.system.service.IUserDataPermissionService;
import cc.mrbird.febs.system.service.IUserService;
import cc.mrbird.febs.api.entity.RpaPluginConfig;
import cc.mrbird.febs.api.entity.RpaPluginUser;
import cc.mrbird.febs.api.service.IRpaPluginConfigService;
import cc.mrbird.febs.api.service.IRpaPluginUserService;
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

import javax.servlet.http.HttpServletRequest;

/**
 * @author MrBird
 */
@Controller("systemView")
@RequiredArgsConstructor
public class ViewController extends BaseController {

    private final IUserService userService;
    private final ShiroHelper shiroHelper;
    private final IUserDataPermissionService userDataPermissionService;

    private final cc.mrbird.febs.api.service.IRpaPluginConfigService  rpaPluginConfigService;
    private final cc.mrbird.febs.api.service.IRpaPluginUserService rpaPluginUserService;
    private final cc.mrbird.febs.api.service.IRpaTaskListService rpaTaskListService;

    @GetMapping("login")
    @ResponseBody
    public Object login(HttpServletRequest request) {
        if (FebsUtil.isAjaxRequest(request)) {
            throw new ExpiredSessionException();
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setViewName(FebsUtil.view("login"));
            return mav;
        }
    }

    @GetMapping("unauthorized")
    public String unauthorized() {
        return FebsUtil.view("error/403");
    }


    /**
     * 映射域名根目录
     * @return
     */
    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/index";
    }


    /**
     * 程序的主入口
     * @param model
     * @return
     */
    @GetMapping("index")
    public String index(Model model) {
        AuthorizationInfo authorizationInfo = shiroHelper.getCurrentUserAuthorizationInfo();
        User user = super.getCurrentUser();
        User currentUserDetail = userService.findByName(user.getUsername());
        currentUserDetail.setPassword("It's a secret");
        model.addAttribute("user", currentUserDetail);
        model.addAttribute("permissions", authorizationInfo.getStringPermissions());
        model.addAttribute("roles", authorizationInfo.getRoles());
        return "index";
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "layout")
    public String layout() {
        return FebsUtil.view("layout");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "password/update")
    public String passwordUpdate() {
        return FebsUtil.view("system/user/passwordUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/profile")
    public String userProfile() {
        return FebsUtil.view("system/user/userProfile");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/avatar")
    public String userAvatar() {
        return FebsUtil.view("system/user/avatar");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "user/profile/update")
    public String profileUpdate() {
        return FebsUtil.view("system/user/profileUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user")
    @RequiresPermissions("user:view")
    public String systemUser() {
        return FebsUtil.view("system/user/user");
    }

    /**
     * 系统插件列表
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpapluginconfig/list")
    @RequiresPermissions("rpapluginconfig:list")
    public String rpapluginconfig_list() {
        return FebsUtil.view("api/rpapluginconfig/list");
    }

    /**
     * 我的插件列表
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpapluginuser/list")
    @RequiresPermissions("rpapluginuser:list")
    public String rpapluginuser_list() {
        return FebsUtil.view("api/rpapluginuser/list");
    }

    /**
     * 我的正在执行任务列表  =============================rpa  task======={{=================================
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpatask/list")
    @RequiresPermissions("rpatask:list")
    public String rpatask_list() {
        return FebsUtil.view("api/rpatask/list");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpatask/add")
    @RequiresPermissions("rpapluginconfig:add")
    public String rpaTaskAdd() {
        return FebsUtil.view("api/rpatask/add");
    }

    // plugin config detail
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpatask/detail/{id}")
    @RequiresPermissions("rpatask:view")
    public String rpaTaskDetail(@PathVariable String id, Model model) {
        resolveRpaTaskModel(id, model, true);
        return FebsUtil.view("api/rpatask/detail");
    }

    // plugin config update
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpatask/update/{id}")
    @RequiresPermissions("rpatask:update")
    public String rpaTaskUpdate(@PathVariable String id, Model model) {
        resolveRpaTaskModel(id, model, true);
        return FebsUtil.view("api/rpatask/update");
    }
    /**
     * 我的正在执行任务列表  =============================rpa  task=======}}=================================
     * @return
     */
    /**
     * 我的正在执行任务列表  =============================rpa  design download======={{=================================
     * @return
     */
    // plugin config update
    @GetMapping(FebsConstant.VIEW_PREFIX + "rpadesign/down")
    @RequiresPermissions("rpadesign:download")
    public String rpaDesignDownload(Model model) {
        return FebsUtil.view("api/rpadesign/detail");
    }

    /**
     * 我的正在执行任务列表  =============================rpa  design download=======}}=================================
     * @return
     */


    /**
     * 我的工程列表
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpaprogect/list")
    @RequiresPermissions("rpaprogect:list")
    public String rpaprogect_list() {
        return FebsUtil.view("api/rpaprogect/list");
    }

    /**
     * 我的云端任务列表
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/queue/list")
    @RequiresPermissions("queue:list")
    public String rpaqueue_list() {
        return FebsUtil.view("api/queue/list");
    }

    /**
     * 我的云端机器人列表
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpahost/list")
    @RequiresPermissions("rpahost:list")
    public String rpahost_list() {
        return FebsUtil.view("api/rpahost/list");
    }

    /**
     * 我的崩溃机器人主机列表
     * @return
     */
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/crash/list")
    @RequiresPermissions("crash:list")
    public String rpacrash_list() {
        return FebsUtil.view("api/crash/list");
    }


    // rpa pluginpluginconfig   ===================================================================
    // plugin config add
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpapluginconfig/add")
    @RequiresPermissions("rpapluginconfig:add")
    public String rpaPluginConfigAdd() {
        return FebsUtil.view("api/rpapluginconfig/add");
    }

    // plugin config detail
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpapluginconfig/detail/{id}")
    @RequiresPermissions("rpapluginconfig:view")
    public String rpaPluginConfigDetail(@PathVariable String id, Model model) {
        resolveRpaPluginConfigModel(id, model, true);
        return FebsUtil.view("api/rpapluginconfig/detail");
    }

    // plugin config update
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpapluginconfig/update/{id}")
    @RequiresPermissions("rpapluginconfig:update")
    public String rpaPluginConfigUpdate(@PathVariable String id, Model model) {
        resolveRpaPluginConfigModel(id, model, true);
        return FebsUtil.view("api/rpapluginconfig/update");
    }

    // user plugin list detail
    @GetMapping(FebsConstant.VIEW_PREFIX + "api/rpapluginuser/detail/{id}")
    @RequiresPermissions("rpapluginuser:view")
    public String rpaPluginUserDetail(@PathVariable String id, Model model) {
        resolveRpaPluginUserModel(id, model, true);
        return FebsUtil.view("api/rpapluginconfig/detail");
    }
    // rpa plugin   config  user ==========================================================================================


    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/add")
    @RequiresPermissions("user:add")
    public String systemUserAdd() {
        return FebsUtil.view("system/user/userAdd");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/detail/{username}")
    @RequiresPermissions("user:view")
    public String systemUserDetail(@PathVariable String username, Model model) {
        resolveUserModel(username, model, true);
        return FebsUtil.view("system/user/userDetail");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/user/update/{username}")
    @RequiresPermissions("user:update")
    public String systemUserUpdate(@PathVariable String username, Model model) {
        resolveUserModel(username, model, false);
        return FebsUtil.view("system/user/userUpdate");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/role")
    @RequiresPermissions("role:view")
    public String systemRole() {
        return FebsUtil.view("system/role/role");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/menu")
    @RequiresPermissions("menu:view")
    public String systemMenu() {
        return FebsUtil.view("system/menu/menu");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "system/dept")
    @RequiresPermissions("dept:view")
    public String systemDept() {
        return FebsUtil.view("system/dept/dept");
    }

    @RequestMapping(FebsConstant.VIEW_PREFIX + "index")
    public String pageIndex() {
        return FebsUtil.view("index");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "404")
    public String error404() {
        return FebsUtil.view("error/404");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "403")
    public String error403() {
        return FebsUtil.view("error/403");
    }

    @GetMapping(FebsConstant.VIEW_PREFIX + "500")
    public String error500() {
        return FebsUtil.view("error/500");
    }

    private void resolveUserModel(String username, Model model, Boolean transform) {
        User user = userService.findByName(username);
        String deptIds = userDataPermissionService.findByUserId(String.valueOf(user.getUserId()));
        user.setDeptIds(deptIds);
        model.addAttribute("user", user);
        if (transform) {
            String sex = user.getSex();
            if (User.SEX_MALE.equals(sex)) {
                user.setSex("男");
            } else if (User.SEX_FEMALE.equals(sex)) {
                user.setSex("女");
            } else {
                user.setSex("保密");
            }
        }
        if (user.getLastLoginTime() != null) {
            model.addAttribute("lastLoginTime", DateUtil.getDateFormat(user.getLastLoginTime(), DateUtil.FULL_TIME_SPLIT_PATTERN));
        }
    }

    /**
     * rpa plugin config
     * @param id
     * @param model
     * @param transform
     */
    private void resolveRpaPluginConfigModel(String id, Model model, Boolean transform) {
        RpaPluginConfig plugin = rpaPluginConfigService.findById(id);

        model.addAttribute("plugin", plugin);
    }

    /**
     * rpa plugin config
     * @param id
     * @param model
     * @param transform
     */
    private void resolveRpaTaskModel(String id, Model model, Boolean transform) {
        cc.mrbird.febs.api.entity.RpaTaskListExcel task = rpaTaskListService.findById(id);

        model.addAttribute("task", task);
    }

    /**
     * rpa plugin user
     * @param id
     * @param model
     * @param transform
     */
    private void resolveRpaPluginUserModel(String id, Model model, Boolean transform) {
        RpaPluginUser plugin = rpaPluginUserService.findById(id);

        model.addAttribute("plugin", plugin);
    }
}
