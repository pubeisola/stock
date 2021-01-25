package cc.mrbird.febs.api.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaTaskCronRule;
import cc.mrbird.febs.api.service.IRpaTaskCronRuleService;
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
 * @date 2020-06-03 18:41:43
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class RpaTaskCronRuleController extends BaseController {

    private final IRpaTaskCronRuleService rpaTaskCronRuleService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "rpaTaskCronRule")
    public String rpaTaskCronRuleIndex(){
        return FebsUtil.view("rpaTaskCronRule/rpaTaskCronRule");
    }

    @GetMapping("rpaTaskCronRule")
    @ResponseBody
    @RequiresPermissions("rpaTaskCronRule:list")
    public FebsResponse getAllRpaTaskCronRules(RpaTaskCronRule rpaTaskCronRule) {
        return new FebsResponse().success().data(rpaTaskCronRuleService.findRpaTaskCronRules(rpaTaskCronRule));
    }

    @GetMapping("rpaTaskCronRule/list")
    @ResponseBody
    @RequiresPermissions("rpaTaskCronRule:list")
    public FebsResponse rpaTaskCronRuleList(QueryRequest request, RpaTaskCronRule rpaTaskCronRule) {
        Map<String, Object> dataTable = getDataTable(this.rpaTaskCronRuleService.findRpaTaskCronRules(request, rpaTaskCronRule));
        return new FebsResponse().success().data(dataTable);
    }

    @ControllerEndpoint(operation = "新增RpaTaskCronRule", exceptionMessage = "新增RpaTaskCronRule失败")
    @PostMapping("rpaTaskCronRule")
    @ResponseBody
    @RequiresPermissions("rpaTaskCronRule:add")
    public FebsResponse addRpaTaskCronRule(@Valid RpaTaskCronRule rpaTaskCronRule) {
        this.rpaTaskCronRuleService.createRpaTaskCronRule(rpaTaskCronRule);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "删除RpaTaskCronRule", exceptionMessage = "删除RpaTaskCronRule失败")
    @GetMapping("rpaTaskCronRule/delete")
    @ResponseBody
    @RequiresPermissions("rpaTaskCronRule:delete")
    public FebsResponse deleteRpaTaskCronRule(RpaTaskCronRule rpaTaskCronRule) {
        this.rpaTaskCronRuleService.deleteRpaTaskCronRule(rpaTaskCronRule);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改RpaTaskCronRule", exceptionMessage = "修改RpaTaskCronRule失败")
    @PostMapping("rpaTaskCronRule/update")
    @ResponseBody
    @RequiresPermissions("rpaTaskCronRule:update")
    public FebsResponse updateRpaTaskCronRule(RpaTaskCronRule rpaTaskCronRule) {
        this.rpaTaskCronRuleService.updateRpaTaskCronRule(rpaTaskCronRule);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改RpaTaskCronRule", exceptionMessage = "导出Excel失败")
    @PostMapping("rpaTaskCronRule/excel")
    @ResponseBody
    @RequiresPermissions("rpaTaskCronRule:export")
    public void export(QueryRequest queryRequest, RpaTaskCronRule rpaTaskCronRule, HttpServletResponse response) {
        List<RpaTaskCronRule> rpaTaskCronRules = this.rpaTaskCronRuleService.findRpaTaskCronRules(queryRequest, rpaTaskCronRule).getRecords();
        ExcelKit.$Export(RpaTaskCronRule.class, response).downXlsx(rpaTaskCronRules, false);
    }
}
