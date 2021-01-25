package cc.mrbird.febs.job.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class VoiceKeywords {
    private Integer id;

    private Integer range;

    private Integer isRange3;  // 是否显示管理员添加的通用数据

    private Integer userId;

    private Integer project_id;

    private String keyNames;

    private String keyValues;
//    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date utime;

}
