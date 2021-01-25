package cc.mrbird.febs.api.entity;

import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;


/**
 *  Entity
 *
 * @author MrBird
 * @date 2020-06-03 18:41:31
 */
@Data
@Excel("任务报表")
@TableName("rpa_task_list")
public class RpaTaskListExcel {

    /**
     * 任务id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField("id")
    private Integer id;

    /**
     * 所属用户 用户  //uiauto
     * 客户端存储关联id  客户端  包括 rpa 机器人登录的客户id
     */
    @TableField("user_id")
    @ExcelField("用户id")
    private String userId;

    /**
     * 公司id // saas平台的公司id
     */
    @TableField("company_id")
    @ExcelField("公司id")
    private Integer companyId;

    /**
     * 服务端用户id for rpa  和客户端可能相同
     */
    @TableField("user_id2")
    @ExcelField("客户端登录id")
    private Integer userId2;

    /**
     * 平台id
     */
    @TableField("plation_id")
    @ExcelField("登录平台id")
    private Integer plationId;

    /**
     * 插件创建日期
     */
    @TableField("createAt")
    @ExcelField("任务创建时间")
    private Date createat;

    /**
     * 插件更新日期
     */
    @TableField("updatedAt")
    @ExcelField("任务更新时间")
    private Date updatedat;

    /**
     * rpa  客户端存储关联id
     */
    @TableField("userId")
    private Integer userid;

    /**
     * 
     */
    @TableField("temporary1")
    @ExcelField("temporary1")
    private String temporary1;

    /**
     * 任务执行状态 // fail   running  success
     */
    @TableField("status")
    @ExcelField("任务状态")
    private String status;

    /**
     * 规则id
     */
    @TableField("ruleId")
    @ExcelField("任务执行规则id")
    private Integer ruleId;
    /**
     * 规则 cron rule
     */
    @TableField("cron_rule")
    @ExcelField("任务执行规则")
    private String cronRule;

    /**
     * 工程类型  //local  server
     */
    @TableField("project_type")
    @ExcelField("任务类型")
    private String projectType;

    /**
     * 工程名称
     */
    @TableField("project_name")
    @ExcelField("任务名称")
    private String projectName;

    /**
     * 工程代码
     */
    @TableField("project_code")
    @ExcelField("任务代码")
    private String projectCode;

    /**
     * 执行错误信息
     */
    @TableField("message")
    @ExcelField("任务执行的最终消息")
    private String message;

    /**
     * 客户端登录名称
     */
    @TableField("loginName")
    @ExcelField("客户端登录名称")
    private String loginname;

    /**
     * 设备id名称
     */
    @TableField("deviceIdName")
    @ExcelField("RPA机器人设备id名称")
    private String deviceidname;

    /**
     * 记录软删除时间
     */
    @TableField("deletedAt")
    @ExcelField("RPA任务软删除时间")
    private Date deletedat;

}
