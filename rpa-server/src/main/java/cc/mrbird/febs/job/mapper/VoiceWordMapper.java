package cc.mrbird.febs.job.mapper;

import cc.mrbird.febs.job.entity.VoiceWord;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VoiceWordMapper {

    List<Map<String,String>> select_list_word();

    int insert(String word);

    int deleteByword( String word);

    int deleteByArray(String[] word);

    int insertSelective(VoiceWord record);

    VoiceWord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(VoiceWord record);

    int updateByPrimaryKey(VoiceWord record);
}