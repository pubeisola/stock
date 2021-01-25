package cc.mrbird.febs.job.entity;

import lombok.Data;

@Data
public class VoiceRules {
    private Integer id;

    private Integer range;

    private Integer isRange3;  // 是否显示管理员添加的通用数据

    private Integer userId;

    private Integer project_id;

    private String ruleName;

    private String ruleDesc;

    private String ruleStatus;

    private Integer ruleScore;

    private String ruleRoles;

    private String rulePosition;

    private Integer ruleDistance;

    private String ruleAppear;

    private String ruleValue;
}
