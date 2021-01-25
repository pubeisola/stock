package cc.mrbird.febs.api.controller;

import cc.mrbird.febs.api.entity.RpaPluginConfig;
import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaPluginUser;
import cc.mrbird.febs.api.service.IRpaPluginUserService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *  Controller
 *
 * @author MrBird
 * @date 2020-05-27 19:56:26
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class RpaPluginUserController extends BaseController {

    private final IRpaPluginUserService rpaPluginUserService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "rpaPluginUser")
    public String rpaPluginUserIndex(){
        return FebsUtil.view("rpaPluginUser/rpaPluginUser");
    }

    @GetMapping("rpaPluginUser")
    @ResponseBody
    @RequiresPermissions("rpaPluginUser:list")
    public FebsResponse getAllRpaPluginUsers(String username, String status, RpaPluginUser rpaPluginUser) {
        return new FebsResponse().success().data(rpaPluginUserService.findRpaPluginUsers(username, status, rpaPluginUser));
    }

    /**
     * 我安装的插件列表
     * @param username
     * @param status
     * @param request
     * @param rpaPluginUser
     * @return
     */
    @GetMapping("rpaPluginUser/list")
    @ResponseBody
    @RequiresPermissions("rpaPluginUser:list")
    public FebsResponse rpaPluginUserList(String username, String status, QueryRequest request, RpaPluginUser rpaPluginUser) {
        Map<String, Object> dataTable = getDataTable(this.rpaPluginUserService.findRpaPluginUsers(username, status, request, rpaPluginUser));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增RpaPluginUser", exceptionMessage = "新增RpaPluginUser失败")
    @PostMapping("rpaPluginUser")
    @ResponseBody
    @RequiresPermissions("rpaPluginUser:add")
    public FebsResponse addRpaPluginUser(@Valid RpaPluginUser rpaPluginUser) {
        this.rpaPluginUserService.createRpaPluginUser(rpaPluginUser);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除RpaPluginUser", exceptionMessage = "删除RpaPluginUser失败")
    @GetMapping("rpaPluginUser/delete")
    @ResponseBody
    @RequiresPermissions("rpaPluginUser:delete")
    public FebsResponse deleteRpaPluginUser(RpaPluginUser rpaPluginUser) {
        this.rpaPluginUserService.deleteRpaPluginUser(rpaPluginUser);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改RpaPluginUser", exceptionMessage = "修改RpaPluginUser失败")
    @PostMapping("rpaPluginUser/update")
    @ResponseBody
    @RequiresPermissions("rpaPluginUser:update")
    public FebsResponse updateRpaPluginUser(RpaPluginUser rpaPluginUser) {
        this.rpaPluginUserService.updateRpaPluginUser(rpaPluginUser);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改RpaPluginUser", exceptionMessage = "导出Excel失败")
    @GetMapping("rpaPluginUser/excel")
    @ResponseBody
    @RequiresPermissions("rpaPluginUser:export")
    public void export(String username, String status, QueryRequest queryRequest, RpaPluginUser rpaPluginUser, HttpServletResponse response) {
        List<RpaPluginUser> rpaPluginUsers = this.rpaPluginUserService.findRpaPluginUsers(username, status, queryRequest, rpaPluginUser).getRecords();
        ExcelKit.$Export(RpaPluginConfig.class, response).downXlsx(rpaPluginUsers, false);
    }
}
