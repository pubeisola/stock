package cc.mrbird.febs.job.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import cc.mrbird.febs.job.entity.Msg;
import cc.mrbird.febs.job.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import cc.mrbird.febs.job.mapper.VoiceKeywordsMapper;
import cc.mrbird.febs.job.service.VoiceAppService;
import java.util.Map;
import java.util.List;
import static cc.mrbird.febs.job.configure.WordMap.keyvaluesMap;

/**
 * @author gaofeng.cheng@ipsos.com
 */
@Slf4j
@Component
public class QualityControl {
    @Autowired
    VoiceKeywordsMapper voiceKeywordsMapper;

    @Autowired
    VoiceAppService voiceAppService;

    public void test(String params) {
        log.info("我是带参数的test方法，正在被执行，参数为：{}" , params);
    }

    public void test1() {
        log.info("我是不带参数的test1方法，正在被执行");
    }

    public void test2() {
        log.info("我是不带参数的test1方法，正在被执行");
    }

    public Msg RunJob(String params) {
        try {
            loadMap();
            if (voiceAppService.RuleScore().equals("success")) {
                return Msg.success().add("rule_score", "success");
            } else {
                return Msg.fail(null);
            }
        } catch (Exception e) {
            throw e;
            //return Msg.fail(null);
        }
    }

    public void loadMap() {
        List<Map<String,String>> ll =  voiceKeywordsMapper.selectAllNameValues();
        for (Map mm:ll){
            String key   = (String) mm.get("key_names");
            String value = (String) mm.get("key_values");
            keyvaluesMap.put(key,value);
        }
    }
}
