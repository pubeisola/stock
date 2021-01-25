package  cc.mrbird.febs.job.mapper;

import  cc.mrbird.febs.job.entity.VoiceApp;

import java.util.List;
import java.util.Map;

public interface VoiceAppMapper {
    int insert(VoiceApp record);
    List<Map<String,Object>> selectJobApp();
    int updateById(VoiceApp voiceApp);
    List<Map<String,Object>>  selectByTerm(VoiceApp voiceApp);
    List<Map<String,Object>> SelectFileDetailById(Integer id);
    List<Map<String,Object>> SelectFilePathById(Integer id);

    List<Map<String,Object>> selectJobAppByOrderId(int order_id);
}