package cc.mrbird.febs.job.service;

import cc.mrbird.febs.job.entity.VoiceOrder;
import java.util.*;

public interface VoiceOrderService {
  public int updateSerSend(VoiceOrder voiceOrder);

  public int updateSerSendStatus(VoiceOrder voiceOrder);

  public List<Map<String,Object>> selectSerSend(VoiceOrder voiceOrder);
}


