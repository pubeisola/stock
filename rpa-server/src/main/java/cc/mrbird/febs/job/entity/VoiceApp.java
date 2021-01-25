package cc.mrbird.febs.job.entity;

import lombok.Data;

import java.util.Date;

@Data
public class VoiceApp {
    private Integer id;

    private Integer project_id;

    private String appFileName;

    private String appFilePath;

    private Date appUploadTime;

    private Integer appScenes;

    private String appScenesResult;

    private String appRule;

    private Integer appRuleResult;

    private Integer appScenesTime;

    private Integer appStatus;

    private Date startTime;

    private Date endTime;



}