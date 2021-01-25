package cc.mrbird.febs.api.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaTaskList;
import cc.mrbird.febs.api.entity.RpaTaskListExcel;
import cc.mrbird.febs.api.service.IRpaTaskListService;
import com.wuwenze.poi.ExcelKit;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import javax.servlet.http.HttpServletRequest;
/**
 *  Controller
 *
 * @author MrBird
 * @date 2020-06-03 18:41:31
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class RpaTaskListController extends BaseController {

    private final IRpaTaskListService rpaTaskListService;

    @GetMapping(FebsConstant.VIEW_PREFIX + "rpaTaskList")
    public String rpaTaskListIndex(){
        return FebsUtil.view("rpaTaskList/rpaTaskList");
    }

    @GetMapping("rpaTaskList")
    @ResponseBody
    @RequiresPermissions("rpatask:list")
    public FebsResponse getAllRpaTaskLists(String name, String status,  String startTime, String endTime, HttpServletRequest request, QueryRequest qq) {
        return new FebsResponse().success().data(rpaTaskListService.findRpaTaskLists(name, status, startTime, endTime, request, qq));
    }

    @GetMapping("rpaTask/list")
    @ResponseBody
    @RequiresPermissions("rpatask:list")
    public  FebsResponse  rpaTaskListList(String name, String status,  String startTime, String endTime, HttpServletRequest request, RpaTaskList rpaTaskList, QueryRequest qq) {
        try {

            Map<String, Object> dataTable = getDataTable(this.rpaTaskListService.findRpaTaskLists(name, status, startTime, endTime, request, qq));
            return new FebsResponse().success().data(dataTable);

        } catch (Exception e) {
            return new FebsResponse().fail().message(e.getMessage() + e.getLocalizedMessage());
        }
    }

    @ControllerEndpoint(operation = "新增RpaTaskList", exceptionMessage = "新增RpaTaskList失败")
    @PostMapping("rpaTask")
    @ResponseBody
    @RequiresPermissions("rpatask:add")
    public FebsResponse addRpaTaskList(@Valid RpaTaskList rpaTaskList) {
        this.rpaTaskListService.createRpaTaskList(rpaTaskList);
        return new FebsResponse().success();
    }

    /**
     *
     * 删除任务列表
     * @param rpaTaskList
     * @return
     */
    @ControllerEndpoint(operation = "删除RpaTaskList", exceptionMessage = "删除RpaTaskList失败")
    @GetMapping("rpaTask/delete/{id}")
    @ResponseBody
    @RequiresPermissions("rpatask:delete")
    public FebsResponse deleteRpaTaskList(@PathVariable String id) {
        this.rpaTaskListService.deleteRpaTaskList(id);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改RpaTaskList", exceptionMessage = "修改RpaTaskList失败")
    @PostMapping("rpaTask/update")
    @ResponseBody
    @RequiresPermissions("rpatask:update")
    public FebsResponse updateRpaTaskList(RpaTaskList rpaTaskList) {
        this.rpaTaskListService.updateRpaTaskList(rpaTaskList);
        return new FebsResponse().success();
    }

    @ControllerEndpoint(operation = "修改RpaTaskList", exceptionMessage = "导出Excel失败")
    @GetMapping("rpaTask/excel")
    @ResponseBody
    @RequiresPermissions("rpatask:export")
    public void export(String name, String status,  String startTime, String endTime, QueryRequest queryRequest, RpaTaskList rpaTaskList, HttpServletResponse response) {
        List<RpaTaskListExcel> rpaTaskListExcel = this.rpaTaskListService.findAllRpaTaskLists(name, status, startTime, endTime,queryRequest);
        ExcelKit.$Export(RpaTaskListExcel.class, response).downXlsx(rpaTaskListExcel, false);
    }
}
