package cc.mrbird.febs.job.entity;

import lombok.Data;

/**
 * @Author Huanyu Zhu
 * @Date 2020/6/1 18:46
 * @Version 1.0
 */
@Data
public class AddScenes {
    private Integer id;
    private  Integer project_id;
    private String scenes_name;
    private String scenes_desc;
    private Integer scenes_status;
}
