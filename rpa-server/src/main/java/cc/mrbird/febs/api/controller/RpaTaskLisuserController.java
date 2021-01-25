package cc.mrbird.febs.api.controller;

import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.utils.FebsUtil;
import cc.mrbird.febs.common.entity.FebsConstant;
import cc.mrbird.febs.common.controller.BaseController;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.api.entity.RpaTaskLisuser;
import cc.mrbird.febs.api.service.IRpaTaskLisuserService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.File;
import java.util.UUID;
import cc.mrbird.febs.common.utils.ProjectFileUploadUtil;
import javax.servlet.http.HttpServletRequest;
import cc.mrbird.febs.api.service.IRpaTaskCronRuleService;
import cc.mrbird.febs.api.entity.RpaProjectUpload;
import cc.mrbird.febs.api.entity.RpaTaskLisuser;
import cc.mrbird.febs.api.entity.RpaTaskCronRule;
import org.springframework.web.bind.annotation.PathVariable;
import cc.mrbird.febs.common.entity.FebsConstant;

/**
 *  Controller
 *
 * @author cheggaofeng
 * @date 2020-06-18 15:05:56
 */
@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class RpaTaskLisuserController extends BaseController {

    private final IRpaTaskLisuserService rpaTaskLisuserService;       // 工程信息服务
    private final IRpaTaskCronRuleService rpaTaskCronRuleService;     // cron 规则

    /**
     * RPA 客户端 单个工程上传服务器
     *
     * 1. 项目库上传设计项目到服务器
     * 2. 本api 归 RPA 设计 客户端使用  上传还情RPA设计器的工程文件到RPA SERVER
     * 3. 项目解析格式和服务器端手动上传的工程 zip压缩包一样
     *
     *     实现原理：
     *        1. 工作流文件都是js 配置文件
     *        2. 从json 文件中解析出工程信息和cron 配置 信息
     *        3. 把解析的cron 配置信息上传数据库 作为cloud 类型工程在 RPA 服务器
     *        4、运行机器人通过websocket 连接服务器定时检查服务器的要调度的云项目
     *        5. 通过云调度API 拉取工作流项目文件到机器人本地
     */
    @ControllerEndpoint(operation = "RPA客户端上传单个工程工作流文件到服务器", exceptionMessage = "RPA客户端上传单个工程工作流文件到服务器")
    @PostMapping("api/v1/rpataskuser/upload")
    @ResponseBody
    public FebsResponse uploadRpaTaskLisuserApi(RpaTaskLisuser rpaTaskLisuser, @RequestParam("file") MultipartFile file, HttpServletRequest request, QueryRequest qq) {

        try {
            // 获取上传的文件
            String storePath = "/opt/data/data/rpa/uploads";   // 服务器上传文件存储的路径
            RpaProjectUpload rpaProjectUpload = ProjectFileUploadUtil.process(file, storePath);
            RpaTaskLisuser rpaTaskLisuser_tmp     = rpaProjectUpload.getRpaTaskLisuser();
            RpaTaskCronRule rpaTaskCronRule_tmp   = rpaProjectUpload.getRpaTaskCronRule();

            // 对应的上传的工程的cron 执行调度规则 获取插入的记录id
            this.rpaTaskCronRuleService.createRpaTaskCronRule(rpaTaskCronRule_tmp);
            Long myInsertId = rpaTaskCronRule_tmp.getId();  // 获取插入的记录id
            rpaTaskLisuser_tmp.setRuleid(myInsertId);      // 设置工程关联的任务规则id
            this.rpaTaskLisuserService.createRpaTaskLisuser(rpaTaskLisuser_tmp);  // 上传成功后提交数据库  上传的工程信息
            // 存储执行规则信息  todo
            return new FebsResponse().success();
        } catch (Exception e) {
            return new FebsResponse().fail();
        }
    }

    /**
     * RPA 客户端 单个工程上传服务器
     *
     * 1. 项目库上传设计项目到服务器
     * 2. 本api 归 RPA 设计 客户端使用  上传还情RPA设计器的工程文件到RPA SERVER
     * 3. 项目解析格式和服务器端手动上传的工程 zip压缩包一样
     */
    @ControllerEndpoint(operation = "RPA客户端上传单个工程工作流文件到服务器", exceptionMessage = "RPA客户端上传单个工程工作流文件到服务器")
    @PostMapping("api/v1/rpataskuser/update/{id}")
    @ResponseBody
    public FebsResponse updateRpaTaskLisuserApi(@PathVariable String id, @RequestParam("file") MultipartFile file, HttpServletRequest request, QueryRequest qq) {

        try {
            if ((id == null) || (id == null) || (id == "")) {
                return new FebsResponse().fail();
            }

            String storePath = "/opt/data/data/rpa/uploads";   // 服务器上传文件存储的路径
            RpaProjectUpload rpaProjectUpload = ProjectFileUploadUtil.process(file, storePath);
            RpaTaskLisuser rpaTaskLisuser_tmp     = rpaProjectUpload.getRpaTaskLisuser();
            RpaTaskCronRule rpaTaskCronRule_tmp   = rpaProjectUpload.getRpaTaskCronRule();

            // 获取要更新的工程信息
            RpaTaskLisuser  qRpaTaskLisuser = this.rpaTaskLisuserService.findById(id);
            rpaTaskCronRule_tmp.setId((long)qRpaTaskLisuser.getId());

            // 对应的上传的工程的cron 执行调度规则 获取插入的记录id
            this.rpaTaskCronRuleService.updateRpaTaskCronRule(rpaTaskCronRule_tmp);
            Long myInsertId = rpaTaskCronRule_tmp.getId();  // 获取插入的记录id
            rpaTaskLisuser_tmp.setRuleid(myInsertId);      // 设置工程关联的任务规则id
            this.rpaTaskLisuserService.updateRpaTaskLisuser(rpaTaskLisuser_tmp); // 上传成功后更新到数据库
            // 更新执行规则信息  todo

            return new FebsResponse().success();
        } catch (Exception e) {
            return new FebsResponse().fail();
        }
    }

    // 工程页面初始化
    @GetMapping(FebsConstant.VIEW_PREFIX + "rpataskuser")
    public String rpaTaskLisuserIndex(){
        return FebsUtil.view("api/rpataskuser/list");
    }

    // 工程页面数据拉取
    @GetMapping("rpataskuser")
    @ResponseBody
    @RequiresPermissions("rpataskuser:list")
    public FebsResponse getAllRpaTaskLisusers(RpaTaskLisuser rpaTaskLisuser) {
        return new FebsResponse().success().data(rpaTaskLisuserService.findRpaTaskLisusers(rpaTaskLisuser));
    }

    // 我的工程列表 分角色 管理员可以看到所有
    @GetMapping("rpataskuser/list")
    @ResponseBody
    @RequiresPermissions("rpataskuser:list")
    public FebsResponse rpaTaskLisuserList(QueryRequest request, RpaTaskLisuser rpaTaskLisuser) {
        Map<String, Object> dataTable = getDataTable(this.rpaTaskLisuserService.findRpaTaskLisusers(request, rpaTaskLisuser));
        return new FebsResponse().success().data(dataTable);
    }


    // 上传我的工程初始化
    @ControllerEndpoint(operation = "上传RpaTaskLisuser", exceptionMessage = "上传RpaTaskLisuser失败")
    @GetMapping(FebsConstant.VIEW_PREFIX + "rpataskuser/upload_init")
    @ResponseBody
    @RequiresPermissions("rpataskuser:upload")
    public String uploadRpaTaskLisuser() {
        return FebsUtil.view("api/rpataskuser/upload");
    }

    // 上传工程zip文件
    @ControllerEndpoint(operation = "上传RpaTaskLisuser", exceptionMessage = "上传RpaTaskLisuser失败")
    @PostMapping("rpataskuser/upload")
    @ResponseBody
    @RequiresPermissions("rpataskuser:upload")
    public FebsResponse uploadRpaTaskLisuser(RpaTaskLisuser rpaTaskLisuser, @RequestParam("file") MultipartFile file, HttpServletRequest request, QueryRequest qq) {

        try {
            // 获取上传的文件
            String storePath = "/opt/data/data/rpa/uploads";   // 服务器上传文件存储的路径
            RpaProjectUpload rpaProjectUpload = ProjectFileUploadUtil.process(file, storePath);
            RpaTaskLisuser rpaTaskLisuser_tmp     = rpaProjectUpload.getRpaTaskLisuser();
            RpaTaskCronRule rpaTaskCronRule_tmp   = rpaProjectUpload.getRpaTaskCronRule();

            // 对应的上传的工程的cron 执行调度规则 获取插入的记录id
            this.rpaTaskCronRuleService.createRpaTaskCronRule(rpaTaskCronRule_tmp);
            Long myInsertId = rpaTaskCronRule_tmp.getId();  // 获取插入的记录id
            rpaTaskLisuser_tmp.setRuleid(myInsertId);      // 设置工程关联的任务规则id
            this.rpaTaskLisuserService.createRpaTaskLisuser(rpaTaskLisuser_tmp);  // 上传成功后提交数据库  上传的工程信息
            // 存储执行规则信息  todo
            return new FebsResponse().success();
        } catch (Exception e) {
            return new FebsResponse().fail();
        }
    }

    // 删除上传的工程
    @ControllerEndpoint(operation = "删除RpaTaskLisuser", exceptionMessage = "删除RpaTaskLisuser失败")
    @GetMapping("rpataskuser/delete")
    @ResponseBody
    @RequiresPermissions("rpataskuser:delete")
    public FebsResponse deleteRpaTaskLisuser(RpaTaskLisuser rpaTaskLisuser) {
        this.rpaTaskLisuserService.deleteRpaTaskLisuser(rpaTaskLisuser);
        return new FebsResponse().success();
    }

    // 更新上传初始化页面
    @ControllerEndpoint(operation = "上传RpaTaskLisuser", exceptionMessage = "上传RpaTaskLisuser失败")
    @GetMapping(FebsConstant.VIEW_PREFIX + "rpataskuser/upload_update_init")
    @ResponseBody
    @RequiresPermissions("rpataskuser:upload")
    public String uploadRpaTaskLisuser_init() {
        return FebsUtil.view("api/rpataskuser/update");
    }

    // 更新上传的某工程
    @ControllerEndpoint(operation = "修改RpaTaskLisuser", exceptionMessage = "修改RpaTaskLisuser失败")
    @PostMapping("rpataskuser/update/{id}")
    @ResponseBody
    @RequiresPermissions("rpataskuser:update")
    public FebsResponse updateRpaTaskLisuser(@PathVariable String id, @RequestParam("file") MultipartFile file, HttpServletRequest request, QueryRequest qq) {

        try {
            if ((id == null) || (id == null) || (id == "")) {
                return new FebsResponse().fail();
            }

            String storePath = "/opt/data/data/rpa/uploads";   // 服务器上传文件存储的路径
            RpaProjectUpload rpaProjectUpload = ProjectFileUploadUtil.process(file, storePath);
            RpaTaskLisuser rpaTaskLisuser_tmp     = rpaProjectUpload.getRpaTaskLisuser();
            RpaTaskCronRule rpaTaskCronRule_tmp   = rpaProjectUpload.getRpaTaskCronRule();

            // 获取要更新的工程信息
            RpaTaskLisuser  qRpaTaskLisuser = this.rpaTaskLisuserService.findById(id);
            rpaTaskCronRule_tmp.setId((long)qRpaTaskLisuser.getId());

            // 对应的上传的工程的cron 执行调度规则 获取插入的记录id
            this.rpaTaskCronRuleService.updateRpaTaskCronRule(rpaTaskCronRule_tmp);
            Long myInsertId = rpaTaskCronRule_tmp.getId();  // 获取插入的记录id
            rpaTaskLisuser_tmp.setRuleid(myInsertId);      // 设置工程关联的任务规则id
            this.rpaTaskLisuserService.updateRpaTaskLisuser(rpaTaskLisuser_tmp); // 上传成功后更新到数据库
            // 更新执行规则信息  todo

            return new FebsResponse().success();
        } catch (Exception e) {
            return new FebsResponse().fail();
        }
    }

    // 上传的工程下载excel 导出为excel
    @ControllerEndpoint(operation = "修改RpaTaskLisuser", exceptionMessage = "导出Excel失败")
    @PostMapping("rpataskuser/excel")
    @ResponseBody
    @RequiresPermissions("rpataskuser:export")
    public void export(QueryRequest queryRequest, RpaTaskLisuser rpaTaskLisuser, HttpServletResponse response) {
        List<RpaTaskLisuser> rpaTaskLisusers = this.rpaTaskLisuserService.findRpaTaskLisusers(queryRequest, rpaTaskLisuser).getRecords();
        ExcelKit.$Export(RpaTaskLisuser.class, response).downXlsx(rpaTaskLisusers, false);
    }
}
