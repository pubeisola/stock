package cc.mrbird.febs.job.service;


import com.github.pagehelper.PageInfo;
import cc.mrbird.febs.job.entity.VoiceApp;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface VoiceAppService {
    public  List<Map<String,Object>> selectJobApp();
    public String RuleScore();
    public int updateById(VoiceApp voiceApp);
    public  List<Map<String,Object>> selectJobAppByOrderId(int order_id);

}


