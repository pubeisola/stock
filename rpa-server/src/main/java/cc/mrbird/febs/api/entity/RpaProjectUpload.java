package cc.mrbird.febs.api.entity;

import java.util.Date;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import cc.mrbird.febs.api.entity.RpaTaskLisuser;
import cc.mrbird.febs.api.entity.RpaTaskCronRule;

/**
 *  Entity
 *
 * @author chenggaofeng
 * @date 2020-06-18 15:05:56
 */
@Data
@TableName("rpa_task_list_user")
public class RpaProjectUpload {

    /**
     * rpa 上传的工程信息
     */
    private RpaTaskLisuser rpaTaskLisuser;

    /**
     * rpa 上传的工程对应的cron 规则信息
     */
    private RpaTaskCronRule rpaTaskCronRule;
}
