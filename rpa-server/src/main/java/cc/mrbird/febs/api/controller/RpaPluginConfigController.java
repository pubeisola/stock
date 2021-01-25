package cc.mrbird.febs.api.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaPluginConfig;
import cc.mrbird.febs.api.service.IRpaPluginConfigService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Date;
import java.text.SimpleDateFormat;

/**
 *  Controller
 *
 * @author MrBird
 * @date 2020-05-27 19:12:10
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class RpaPluginConfigController extends BaseController {

    private final IRpaPluginConfigService rpaPluginConfigService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "rpaPluginConfig")
    public String rpaPluginConfigIndex(){
        return FebsUtil.view("rpaPluginConfig/rpaPluginConfig");
    }

    @GetMapping("rpaPluginConfig")
    @ResponseBody
    @RequiresPermissions("rpaPluginConfig:list")
    public FebsResponse getAllRpaPluginConfigs(String username, String status, RpaPluginConfig rpaPluginConfig) {
        return new FebsResponse().success().data(rpaPluginConfigService.findRpaPluginConfigs(username, status, rpaPluginConfig));
    }

    /**
     * 插件配置；列表
     *
     * @param username
     * @param status
     * @param request
     * @param rpaPluginConfig
     * @return
     */
    @GetMapping("rpaPluginConfig/list")
    @ResponseBody
    @RequiresPermissions("rpaPluginConfig:list")
    public FebsResponse rpaPluginConfigList(String username, String status, QueryRequest request, RpaPluginConfig rpaPluginConfig) {
        Map<String, Object> dataTable = getDataTable(this.rpaPluginConfigService.findRpaPluginConfigs(username, status,request, rpaPluginConfig));
        return new FebsResponse().success().data(dataTable);
    }

    /**
     * 系统插件添加
     * @param rpaPluginConfig
     * @return
     */
    @ControllerEndpoint(operation = "新增RpaPluginConfig", exceptionMessage = "新增RpaPluginConfig失败")
    @PostMapping("rpaPluginConfig/add")
    @ResponseBody
    @RequiresPermissions("rpaPluginConfig:add")
    public FebsResponse addRpaPluginConfig(@Valid RpaPluginConfig rpaPluginConfig) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-dd-MM HH:mm:ss");
        rpaPluginConfig.setCreateat(formatter.format(new Date()));
        this.rpaPluginConfigService.createRpaPluginConfig(rpaPluginConfig);
        return new FebsResponse().success();
    }

    /**
     * 系统插件拆除
     * @param id
     * @return
     */
    @ControllerEndpoint(operation = "删除RpaPluginConfig", exceptionMessage = "删除RpaPluginConfig失败")
    @GetMapping("rpaPluginConfig/delete/{id}")
    @ResponseBody
    @RequiresPermissions("rpaPluginConfig:delete")
    public FebsResponse deleteRpaPluginConfig(@PathVariable String id) {
        this.rpaPluginConfigService.deleteRpaPluginConfig(id);
        return new FebsResponse().success();
    }

    /**
     * 系统插件删除
     * @param rpaPluginConfig
     * @return
     */
    @ControllerEndpoint(operation = "修改RpaPluginConfig", exceptionMessage = "修改RpaPluginConfig失败")
    @PostMapping("rpaPluginConfig/update")
    @ResponseBody
    @RequiresPermissions("rpaPluginConfig:update")
    public FebsResponse updateRpaPluginConfig(RpaPluginConfig rpaPluginConfig) {
        this.rpaPluginConfigService.updateRpaPluginConfig(rpaPluginConfig);
        return new FebsResponse().success();
    }

    /**
     * 插件配置；列表  excel 导出
     * @param username
     * @param status
     * @param queryRequest
     * @param rpaPluginConfig
     * @return
     */
    @ControllerEndpoint(operation = "修改RpaPluginConfig", exceptionMessage = "导出Excel失败")
    @GetMapping("rpaPluginConfig/excel")
    @ResponseBody
    @RequiresPermissions("rpaPluginConfig:export")
    public void export(String username, String status, QueryRequest queryRequest, RpaPluginConfig rpaPluginConfig, HttpServletResponse response) {
        List<RpaPluginConfig> rpaPluginConfigs = this.rpaPluginConfigService.findRpaPluginConfigs(username, status, queryRequest, rpaPluginConfig).getRecords();
        ExcelKit.$Export(RpaPluginConfig.class, response).downXlsx(rpaPluginConfigs, false);
    }
}
