package cc.mrbird.febs.job.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cc.mrbird.febs.job.mapper.VoiceScenesMapper;
import cc.mrbird.febs.job.entity.*;
import cc.mrbird.febs.job.entity.ScenesRules;
import cc.mrbird.febs.job.entity.VoiceScenes;
import cc.mrbird.febs.job.service.VoiceScenesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cc.mrbird.febs.job.util.Constants;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;

@Service
@Transactional
public class VoiceScenesImpl implements VoiceScenesService {

    @Autowired
    VoiceScenesMapper voiceScenesMapper;

    /**
     * 场景列表查询
     * @param voiceScenes
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<VoiceScenes> selectByScenes(VoiceScenes voiceScenes, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);//这行是重点，表示从pageNum页开始，每页pageSize条数据

        JSONObject user_info = (JSONObject)SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_USER_INFO);
        int user_id = -1;
        if (user_info != null) {
          user_id = user_info.getInteger("userId");   // 获取当前登录用户id
        }
        voiceScenes.setUserId(user_id);
        List<VoiceScenes> ll = voiceScenesMapper.selectByScenes(voiceScenes);
        PageInfo<VoiceScenes> pageInfo = new PageInfo<>(ll);
        return pageInfo;
    }

    @Override
    public int insertSelective(VoiceScenes voiceScenes) {
        int insertcount = voiceScenesMapper.insertSelective(voiceScenes);
        return insertcount;
    }

    @Override
    public int updateByPrimaryKeySelective(VoiceScenes voiceScenes) {
        int updatecount = voiceScenesMapper.updateByPrimaryKeySelective(voiceScenes);
        return updatecount;
    }

    @Override
    public int insertScenesRules(ScenesRules scenesRules) {
        int count = voiceScenesMapper.insertScenesRules(scenesRules);
        return count;
    }

    @Override
    public int deleteByScenesid(Integer id) {
        int count = voiceScenesMapper.deleteByScenesid(id);
        return count;
    }

    @Override
    public int updateScenesRulesStatus(Integer id) {
        int count = voiceScenesMapper.updateScenesRulesStatus(id);
        return count;
    }

    @Override
    public List<Map<String, Object>> selectRuleByScenesId(Integer scenesid) {
        return voiceScenesMapper.selectRuleByScenesId(scenesid);
    }

    @Override
    public int UpdateScenesStatus(AddScenes addScenes) {
        VoiceScenes voiceScenes = new VoiceScenes();
        voiceScenes.setId(addScenes.getId());
        voiceScenes.setScenesStatus(addScenes.getScenes_status());
        int count = voiceScenesMapper.UpdateScenesStatus(voiceScenes);
        return count;
    }

  /**
   * 添加场景
   * 场景所属的人
   * @param addSceneRule
   * @return
   */
    @Override
    public int AddScenesAndRules(AddSceneRule addSceneRule) {
        try{
            // 获取用户id
            JSONObject user_info = (JSONObject)SecurityUtils.getSubject().getSession().getAttribute(Constants.SESSION_USER_INFO);
            int user_id = -1;
            if (user_info != null) {
              user_id = user_info.getInteger("userId");   // 获取当前登录用户id
            }

            int count= 0;
            VoiceScenes vs = new VoiceScenes();
            vs.setId(addSceneRule.getId());
            vs.setUserId(user_id);
            vs.setProject_id(addSceneRule.getProject_id());
            vs.setScenesName(addSceneRule.getScenes_name());
            vs.setScenesDesc(addSceneRule.getScenes_desc());
            if (addSceneRule.getId() != null){
                count=voiceScenesMapper.updateByPrimaryKeySelective(vs);
                voiceScenesMapper.updateScenesRulesStatus(addSceneRule.getId());
                vs.setScenesStatus(1);
                ScenesRules scenesRules = new ScenesRules();
                for ( AddScenesRuleList ruleList:addSceneRule.getRule_list()){
                    scenesRules.setScenesid(addSceneRule.getId());
                    scenesRules.setRuleid(ruleList.getRule_id());
                    scenesRules.setRuleStatus(ruleList.getRule_status());
                    scenesRules.setRuleScore(ruleList.getRule_score());
                    scenesRules.setStatus(0);
                    voiceScenesMapper.insertScenesRules(scenesRules);
                }
                return count;
            }else if ((voiceScenesMapper.selectByScenes(vs)).size() <= 0){
                vs.setScenesStatus(1);
                count = voiceScenesMapper.insertSelective(vs);
                Integer scenesid = vs.getId();
                ScenesRules scenesRules = new ScenesRules();
                for ( AddScenesRuleList ruleList:addSceneRule.getRule_list()){
                    scenesRules.setScenesid(scenesid);
                    scenesRules.setRuleid(ruleList.getRule_id());
                    scenesRules.setRuleStatus(ruleList.getRule_status());
                    scenesRules.setRuleScore(ruleList.getRule_score());
                    scenesRules.setStatus(0);
                    voiceScenesMapper.insertScenesRules(scenesRules);
                }
                return count;
            }else {
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public int DelectScenesRules(Integer id) {
        try {
            int deletecount =  voiceScenesMapper.deleteById(id);
            voiceScenesMapper.deleteByScenesid(id);
            return deletecount;
        }catch (Exception e){
            return 0;
        }
    }

    @Override
    public List<Map<String,Object>>  selectRuleById(Integer id) {
        return voiceScenesMapper.selectRuleById(id);
    }
}
