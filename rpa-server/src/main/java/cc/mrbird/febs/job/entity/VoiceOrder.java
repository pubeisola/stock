package cc.mrbird.febs.job.entity;

import lombok.Data;

@Data
public class VoiceOrder {
    private Integer id;

    private String appId;

    private Integer orderId;

    private String name;  // 是否显示管理员添加的通用数据

    private Integer projectId;

    private String sceneId;

    private String callBackUrl;

    private Integer rl;

    private String sePath;

    private String ctime;

    private String ser;

    private Integer status;

    private String serSend;

    private Integer serSendStatus;

    private Integer jobId;
}
