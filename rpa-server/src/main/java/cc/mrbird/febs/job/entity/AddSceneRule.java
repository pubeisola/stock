package cc.mrbird.febs.job.entity;

import lombok.Data;

import java.util.List;
import cc.mrbird.febs.job.entity.AddScenesRuleList;
/**
 * @Author Huanyu Zhu
 * @Date 2020/6/1 18:46
 * @Version 1.0
 */
@Data
public class AddSceneRule {


    private Integer id;
    private  Integer project_id;
    private String scenes_name;
    private String scenes_desc;
    private List<AddScenesRuleList> rule_list;
 }
