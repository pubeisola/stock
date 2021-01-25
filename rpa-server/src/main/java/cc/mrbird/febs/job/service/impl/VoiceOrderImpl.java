package cc.mrbird.febs.job.service.impl;

import cc.mrbird.febs.job.mapper.VoiceOrderMapper;
import cc.mrbird.febs.job.entity.VoiceOrder;
import cc.mrbird.febs.job.service.VoiceOrderService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import java.util.Date;
import java.text.SimpleDateFormat;


@Service
@Transactional
public class VoiceOrderImpl implements VoiceOrderService {

    @Autowired
    VoiceOrderMapper voiceOrderMapper;

    public int updateSerSend(VoiceOrder voiceOrder) {
      return voiceOrderMapper.updateSerSend(voiceOrder);
    }

    public List<Map<String,Object>> selectSerSend(VoiceOrder voiceOrder) {
      return voiceOrderMapper.selectSerSend(voiceOrder);
    }

    public int updateSerSendStatus(VoiceOrder voiceOrder) {
        return voiceOrderMapper.updateSerSendStatus(voiceOrder);
    }
}
