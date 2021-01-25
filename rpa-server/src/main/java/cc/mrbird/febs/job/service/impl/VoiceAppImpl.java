package cc.mrbird.febs.job.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import cc.mrbird.febs.job.task.QualityTask;
import cc.mrbird.febs.job.mapper.VoiceAppMapper;
import cc.mrbird.febs.job.entity.Msg;
import cc.mrbird.febs.job.entity.VoiceApp;
import cc.mrbird.febs.job.service.VoiceAppService;
import cc.mrbird.febs.job.service.VoiceScenesService;

import cc.mrbird.febs.job.util.RuleCut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;
import java.util.List;
import org.springframework.stereotype.Service;
import static cc.mrbird.febs.job.configure.SimpleConfigs.Flag;
import static cc.mrbird.febs.job.util.DateUtils.StrtoDate;
import static cc.mrbird.febs.job.util.DateUtils.getYMD;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Transactional
public class VoiceAppImpl implements VoiceAppService {

    @Autowired
    VoiceAppMapper voiceAppMapper;

    @Autowired
    VoiceScenesService voiceScenesService;

    //@Autowired
    //VoiceAppService voiceAppService;

    @Override
    public  List<Map<String,Object>> selectJobApp() {
        return voiceAppMapper.selectJobApp();
    }

    /**
     * 根据订单id 获取  本订单要处理的任务列表
     * @param order_id
     * @return
     */
    public  List<Map<String,Object>> selectJobAppByOrderId(int order_id) {
        return voiceAppMapper.selectJobAppByOrderId(order_id);
    }

    @Override
    public String RuleScore() {
        long a = System.currentTimeMillis();
        try{
            List<Map<String,Object>> ll = this.selectJobApp();
            for (Map voiceApp:ll){
                String Content = RuleCut.CutSentence(voiceApp.get("app_file_path").toString(),voiceApp.get("rule_position").toString(),voiceApp.get("rule_distance").toString());
                if (Flag) System.out.println("rule_value===="+voiceApp.get("rule_value").toString());
                if (Flag)  System.out.println("rule_score===="+voiceApp.get("rule_score").toString());
                if (Flag) System.out.println("rule_appear===="+voiceApp.get("rule_appear").toString());
                if (Flag)  System.out.println("rule_status===="+voiceApp.get("rule_status").toString());
                int score = QualityTask.RuleScore(Content,voiceApp.get("rule_value").toString(),voiceApp.get("rule_score").toString(),voiceApp.get("rule_appear").toString(),voiceApp.get("rule_status").toString());
                long times = (System.currentTimeMillis() - a)/1000+1;
                if (Flag) System.out.println("-----need time account_buzz:" +(System.currentTimeMillis() - a)+"====score===="+score);
                VoiceApp voiceappupdate = new VoiceApp();
                voiceappupdate.setAppRuleResult(score);
                voiceappupdate.setAppScenesTime((int) times);
                voiceappupdate.setAppStatus(1);
                voiceappupdate.setId(Integer.parseInt(voiceApp.get("id").toString()));
                this.updateById(voiceappupdate);
                if (Flag) System.out.println("id ======="+voiceApp.get("id").toString()+"=========="+score);
            }
            if (Flag) System.out.println("-----RuleScore :" + (System.currentTimeMillis() - a));
            return "success";
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    public int updateById(VoiceApp voiceApp) {
        return voiceAppMapper.updateById(voiceApp);
    }
}
