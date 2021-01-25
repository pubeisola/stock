package cc.mrbird.febs.job.mapper;

import cc.mrbird.febs.job.entity.VoiceKeywords;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VoiceKeywordsMapper {

    int insert(VoiceKeywords record);

    int insertSelective(VoiceKeywords record);

    VoiceKeywords selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VoiceKeywords record);

    int updateByid(VoiceKeywords record);

    List<Map<String,Object>> selectByProjectKeyword(VoiceKeywords voiceKeywords);

    List<Map<String,String>> selectAllNameValues();

    List<Map<String,String>> selectNameValuesByProject(VoiceKeywords VoiceKeywords);

    int deleteById(Integer id);

}
