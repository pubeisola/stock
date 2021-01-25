package cc.mrbird.febs.job.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoiceProject {
    private Integer id;

    private Integer userId;

    private String project;

    private String projectName;

    private String projectDesc;

}
