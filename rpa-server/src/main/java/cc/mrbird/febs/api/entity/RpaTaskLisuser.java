package cc.mrbird.febs.api.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author chenggaofeng
 * @date 2020-06-18 15:05:56
 */
@Data
@TableName("rpa_task_list_user")
public class RpaTaskLisuser {

    /**
     * rpa 任务列表
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属用户 用户  //uiauto
     */
    @TableField("user_id")
    private String userId;

    /**
     * 公司id // saas平台的公司id
     */
    @TableField("company_id")
    private Integer companyId;

    /**
     * 服务端用户id for rpa  和客户端可能相同
     */
    @TableField("user_id2")
    private Integer userId2;

    /**
     * 平台id
     */
    @TableField("plation_id")
    private Integer plationId;

    /**
     * 插件创建日期
     */
    @TableField("createAt")
    private Date createat;

    /**
     * 插件更新日期
     */
    @TableField("updatedAt")
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
    private String temporary1;

    /**
     * 任务执行状态 // fail   running  success
     */
    @TableField("status")
    private String status;

    /**
     * 规则id
     */
    @TableField("ruleId")
    private Long ruleid;

    /**
     * 工程类型  //local  server
     */
    @TableField("project_type")
    private String projectType;

    /**
     * 工程名称
     */
    @TableField("project_name")
    private String projectName;

    /**
     * 工程代码
     */
    @TableField("project_code")
    private String projectCode;

    /**
     * 执行错误信息
     */
    @TableField("message")
    private String message;

    /**
     * 客户端登录名称
     */
    @TableField("loginName")
    private String loginname;

    /**
     * 设备id名称
     */
    @TableField("deviceIdName")
    private String deviceidname;

    /**
     * 记录软删除时间
     */
    @TableField("deletedAt")
    private Date deletedat;

    /**
     * 是否记录屏幕 // 0: 不记录  1：记录
     */
    @TableField("is_record_screen")
    private Integer isRecordScreen;

    /**
     * 重试次数
     */
    @TableField("retry_count")
    private Integer retryCount;

    /**
     * 重试间隔
     */
    @TableField("retry_range")
    private Integer retryRange;

    /**
     * 超时时间
     */
    @TableField("timeout")
    private Integer timeout;

}
