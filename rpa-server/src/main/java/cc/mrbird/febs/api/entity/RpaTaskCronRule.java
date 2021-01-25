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
 * @author MrBird
 * @date 2020-06-03 18:41:43
 */
@Data
@TableName("rpa_task_cron_rule")
public class RpaTaskCronRule {

    /**
     * 任务执行的cron规则
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 
     */
    @TableField("cron_rule")
    private String cronRule;

    /**
     * 规则创建时间
     */
    @TableField("createAt")
    private Date createat;

    /**
     * 规则更新时间
     */
    @TableField("updatedAt")
    private Date updatedat;

}
