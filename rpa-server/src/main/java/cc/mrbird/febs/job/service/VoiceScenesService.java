package cc.mrbird.febs.job.service;


import com.github.pagehelper.PageInfo;
import cc.mrbird.febs.job.entity.AddSceneRule;
import cc.mrbird.febs.job.entity.AddScenes;
import cc.mrbird.febs.job.entity.ScenesRules;
import cc.mrbird.febs.job.entity.VoiceRules;
import cc.mrbird.febs.job.entity.VoiceScenes;

import java.util.List;
import java.util.Map;

public interface VoiceScenesService {

    public PageInfo<VoiceScenes> selectByScenes(VoiceScenes voiceScenes, Integer pageNum, Integer pageSize);
    public int DelectScenesRules(Integer id);
    public int insertSelective(VoiceScenes voiceScenes);
    public  List<Map<String,Object>> selectRuleById(Integer id);

    public int updateByPrimaryKeySelective(VoiceScenes VoiceScenes);

    public int insertScenesRules(ScenesRules scenesRules);

    public int deleteByScenesid(Integer id);

    public int updateScenesRulesStatus(Integer id);

    public List<Map<String,Object>> selectRuleByScenesId(Integer scenesid);

    public int UpdateScenesStatus(AddScenes addScenes);

    public int AddScenesAndRules(AddSceneRule addSceneRule);

}


