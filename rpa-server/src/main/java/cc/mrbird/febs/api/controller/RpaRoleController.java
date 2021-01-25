package cc.mrbird.febs.api.controller;


import cc.mrbird.febs.common.annotation.ControllerEndpoint;
import cc.mrbird.febs.common.entity.DeptTree;
import cc.mrbird.febs.common.entity.FebsResponse;
import cc.mrbird.febs.common.entity.QueryRequest;
import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.system.entity.Dept;
import cc.mrbird.febs.system.service.IDeptService;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.wuwenze.poi.ExcelKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author chenggafeng
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class RpaRoleController {

    private final IDeptService deptService;

    /**
    * 获取工程权限
    **/
    @GetMapping("/api/v1/roles/getProjectPermission")
    @ControllerEndpoint(exceptionMessage = "获取工程权限")
    public List<DeptTree<Dept>> getProjectPermission() throws FebsException {
        return this.deptService.findDepts();
    }
}
