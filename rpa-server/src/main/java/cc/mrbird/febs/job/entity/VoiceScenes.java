package cc.mrbird.febs.job.entity;

import lombok.Data;

@Data
public class VoiceScenes {
    private Integer id;

    private Integer userId;

    private Integer project_id;

    private String projectName;

    private String scenesName;

    private String scenesDesc;

    private Integer scenesStatus;
}
