package cc.mrbird.febs.job.mapper;

import cc.mrbird.febs.job.entity.ScenesRules;
import cc.mrbird.febs.job.entity.VoiceScenes;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VoiceScenesMapper {

    List<VoiceScenes> selectByScenes(VoiceScenes voiceScenes);
    int deleteById(Integer id);
    int insertSelective(VoiceScenes voiceScenes);
    int updateByPrimaryKeySelective(VoiceScenes voiceScenes);
    List<Map<String,Object>> selectRuleById(Integer id);
    int insertScenesRules(ScenesRules scenesRules);
    int deleteByScenesid(Integer id);
    int updateScenesRulesStatus(@Param(value="scenes_id")Integer id);

    List<Map<String,Object>>selectRuleByScenesId(Integer scenesid);

    int UpdateScenesStatus(VoiceScenes voiceScenes);
}