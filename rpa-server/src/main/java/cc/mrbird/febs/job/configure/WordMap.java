package cc.mrbird.febs.job.configure;

import cc.mrbird.febs.job.mapper.VoiceKeywordsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cc.mrbird.febs.job.configure.SimpleConfigs.Flag;


@Configuration
public class WordMap {
    public static  Map<String,String> keyvaluesMap = new HashMap<>();

    @Autowired
    VoiceKeywordsMapper voiceKeywordsMapper;


    @Bean
    public void loadMap(){
        List<Map<String,String>> ll =  voiceKeywordsMapper.selectAllNameValues();
        for (Map mm:ll){
            String key = (String) mm.get("key_names");
            String value = (String) mm.get("key_values");
            keyvaluesMap.put(key,value);
        }
        if (Flag) System.out.println("load keyvlaues map ===================="+keyvaluesMap);
    }
}
