package cc.mrbird.febs.job.mapper;

import cc.mrbird.febs.job.entity.VoiceRules;

import java.util.List;
import java.util.Map;

public interface VoiceRulesMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(VoiceRules voiceRules);

    int insertSelective(VoiceRules voiceRules);

    List<Map<String,Object>> selectByProjectRule(VoiceRules voiceRules);
    int deleteById(Integer id);
    int updateByPrimaryKeySelective(VoiceRules voiceRules);

    //  int updateByPrimaryKey(VoiceRules record);

    List<VoiceRules> rule_score();

    List<Map<String,Object>> SelectScenesRule(VoiceRules voiceRules);

    List<Map<String,Object>> SelectScenesRulep(VoiceRules voiceRules);
}
