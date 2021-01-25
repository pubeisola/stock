package cc.mrbird.febs.job.mapper;

import cc.mrbird.febs.job.entity.VoiceOrder;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

public interface VoiceOrderMapper {

  int insert(VoiceOrder record);
  List<Map<String,Object>> selectOrder(VoiceOrder voiceOrder);
  int addRuleBatch (@Param("emps") List<VoiceOrder> emps);

  int updateSerSendStatus(VoiceOrder voiceOrder);
  int updateSerSend(VoiceOrder voiceOrder);

  List<Map<String,Object>> selectSerSend(VoiceOrder voiceOrder);
}
