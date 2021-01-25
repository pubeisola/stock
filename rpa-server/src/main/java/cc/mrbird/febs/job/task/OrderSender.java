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
import java.util.UUID;

import cc.mrbird.febs.job.service.VoiceOrderService;
import cc.mrbird.febs.job.entity.VoiceOrder;
import java.util.ArrayList;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import cc.mrbird.febs.job.utils.OKHttpUtil;


/**
 * @author gaofeng.cheng@ipsos.com
 */
@Slf4j
@Component
public class OrderSender {

    @Autowired
    VoiceAppService voiceAppService;

    @Autowired
    VoiceOrderService voiceOrderService;

    public void test(String params) {
        log.info("我是带参数的test方法，正在被执行，参数为：{}" , params);
    }

    public void test1() {
        log.info("我是不带参数的test1方法，正在被执行");
    }

    public void test2() {
        log.info("我是不带参数的test1方法，正在被执行");
    }

    // params 是job id
    public Msg RunJob(String params) {
        try {
            int curProcessJobId = 0;
            try {
                if (params != null) {
                    curProcessJobId = Integer.parseInt(params);   // 本脚本的 job id
                } else {
                    return Msg.success().add("job", "success");  // 无效参数  脚本退出
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            // 计算 uuid
            long timeStamp = System.currentTimeMillis();  //获取当前时间戳,也可以是你自已给的一个随机的或是别人给你的时间戳(一定是long型的数据)

            boolean stop = false;
            while (!stop) {

                String uuid = UUID.randomUUID().toString().replaceAll("-", "");
                // 更新本批次获取的记录
                VoiceOrder voiceOrder = new VoiceOrder();
                voiceOrder.setSerSend(uuid);
                voiceOrder.setJobId(curProcessJobId);
                voiceOrderService.updateSerSend(voiceOrder);

                // 本批次要处理的订单列表
                List<Map<String, Object>> curOrderList = voiceOrderService.selectSerSend(voiceOrder);
                if (curOrderList != null) {
                    if (curOrderList.size() <= 0) {
                        stop = true;
                        continue;
                    }

                    for (Map<String, Object> row : curOrderList) {

                        if (row != null) {

                            System.out.println("current exe --111---: " + row.toString());
                            // 取每条记录的处理结果
                            long curOrderId = 0L;
                            long iiid = (long)row.get("id");
                            System.out.println("current exe --111--222-: " + iiid);
                            curOrderId = iiid;
                            System.out.println("current exe --111-4444-: " + curOrderId);
                            String curOrderUuid = "";
                            Object curOrderUuid2 = row.get("uuid");
                            curOrderUuid = (String)curOrderUuid2;
                            if (curOrderUuid == null) {
                                curOrderUuid = "";
                            }

                            String curCallBackUrl = "";
                            Object curCallBackUrl2 = row.get("call_back_url");
                            curCallBackUrl = (String)curCallBackUrl2;
                            if (curCallBackUrl == null) {
                                curCallBackUrl = "";
                            }

                            if (curOrderId > 0) {
                                // 调用回调接口
                                List<Map<String,Object>>  curAppList = voiceAppService.selectJobAppByOrderId((int)curOrderId);
                                System.out.println("current exe -222---: " + curAppList.toString());

                                boolean isSubmit = true;
                                if (curAppList != null) {
                                    for (Map<String,Object> app : curAppList) {
                                        int curAppStatus = (int)app.get("app_status");

                                        if (curAppStatus == 0) {
                                            isSubmit = false;
                                            break;
                                        }
                                    }
                                }

                                if (!isSubmit) {
                                    continue;
                                }
                                System.out.println("current exe ---3333---: ");
                                // 如果可以提交
                                // 计算订单处理结果 更新订单的处理结果
                                Map<Integer, Integer> sceneScore  = new HashMap<Integer, Integer>();

                                List<Map<String,Object>> returnResult = new ArrayList<Map<String, Object>>();
                                if (curAppList != null) {
                                    for (Map<String,Object> app : curAppList) {
                                        int app_scenes = (int)app.get("app_scenes");

                                        String   app_scenes_result =app.get("app_scenes_result") != null ? (String)app.get("app_scenes_result") : "";

                                        String   app_rule =  app.get("app_rule") != null ?    (String)app.get("app_rule")  : "";

                                        int   app_rule_result = (int)app.get("app_rule_result");

                                        String scene_name = (String)app.get("scene_name");
                                        if(scene_name == null)
                                        {
                                            scene_name = "";
                                        }

                                        String   rule_name = (String)app.get("rule_name");;
                                        if(rule_name == null)
                                        {
                                            rule_name = "";
                                        }

                                        String   rule_status = (String)app.get("rule_status");
                                        if(rule_status == null)
                                        {
                                            rule_status = "";
                                        }

                                        int   rule_score = (int)app.get("rule_score");

                                        Map<String,Object> tmp  = new HashMap<String,Object>();
                                        tmp.put("scene_id", app_scenes);
                                        tmp.put("scene_score", app_scenes_result);
                                        tmp.put("rule_id", app_rule);
                                        tmp.put("order_id", curOrderId);
                                        tmp.put("uuid", curOrderUuid);
                                        tmp.put("scene_name", scene_name);
                                        tmp.put("rule_name", rule_name);
                                        tmp.put("rule_status", rule_status);
                                        tmp.put("rule_score", app_rule_result);
                                        System.out.println("current exe ---3444---: " + tmp.toString());

                                        int plus_score = 0;
                                        if (rule_status.compareToIgnoreCase("加") == 0) {
                                            if (app_rule_result > 0) {
                                                tmp.put("rule_result", "pass");
                                                plus_score = Math.abs(app_rule_result);     // 加分数
                                            } else {
                                                tmp.put("rule_result", "down");
                                                plus_score = -1 * Math.abs(app_rule_result);     // 加分数
                                            }
                                        } else if (rule_status.compareToIgnoreCase("减") == 0) {
                                            if (app_rule_result < 0) {
                                                tmp.put("rule_result", "pass");
                                                plus_score = -1 * Math.abs(app_rule_result);  // 减分数
                                            } else {
                                                tmp.put("rule_result", "down");
                                                plus_score = Math.abs(app_rule_result);  // 减分数
                                            }
                                        }

                                        int scene_score_cur = sceneScore.get(app_scenes) != null ? (int)sceneScore.get(app_scenes) : 0; // 获取上次迭代的分数

                                        sceneScore.put(app_scenes, plus_score + scene_score_cur);   // 计算每个场景的总分数
                                        returnResult.add(tmp);
                                    }
                                }
                                System.out.println("current exe ---555555---: " + returnResult.toString());
                                if (returnResult != null) {
                                    for (Map<String, Object> app : returnResult) {

                                        int   app_scenes = app.get("app_scenes") != null ?  (int)app.get("app_scenes") : 0;

                                        int   scene_score_cur = sceneScore.get(app_scenes) != null ? (int) sceneScore.get(app_scenes) : 0;

                                        app.put("scene_score", scene_score_cur);
                                    }
                                }

                                // 通过回调接口发送出去
                                //todo
                                Map<String,Object> returnData =  new HashMap<String,Object>();

                                returnData.put("status", 1);
                                returnData.put("fail_reason", "");
                                returnData.put("id", curOrderId);           // 订单id
                                returnData.put("uuid", curOrderUuid);       // 提交订单时的uuid
                                returnData.put("scene_content", returnResult);

                                String jsonParams = JSON.toJSONString(returnData);
                                System.out.println("current exe ---555555---: " + jsonParams.toString());
                                System.out.println("current ----- second: " + jsonParams);


                                if (curCallBackUrl.length() > 0) {
                                    System.out.println("current exe ---666666---: " + jsonParams.toString());
                                    String myToken = "e5cHLWScbto3VfvYTU1llVZgl/WniA4QZZ8epmn8k/o=";
                                    // 发送失败时退避 再重新发送两次
                                    if (jsonParams != null) {
                                        Boolean isUpdate = false;
                                        Object retStatus = OKHttpUtil.postJsonParams(curCallBackUrl, jsonParams, myToken);

                                        if ((retStatus == null) || (retStatus == "")) {
                                            Thread.sleep(500);
                                             retStatus = OKHttpUtil.postJsonParams(curCallBackUrl, jsonParams, myToken);

                                            if ((retStatus == null) || (retStatus == "")) {
                                                Thread.sleep(500);
                                                retStatus = OKHttpUtil.postJsonParams(curCallBackUrl, jsonParams, myToken);

                                                if ((retStatus == null) || (retStatus == "")) {
                                                    Thread.sleep(500);
                                                } else {
                                                    System.out.println("current 3 exe ---999999---: " + retStatus.toString());
                                                    String ret = (String) retStatus;
                                                    if (ret == null) {
                                                        ret = "";
                                                    }

                                                    JSONObject retJsonObject = JSONObject.parseObject(ret);
                                                    if (retJsonObject != null) {
                                                        String name =  retJsonObject.getString("code");  // 文档名称

                                                        String curName = "";// 记录的唯一标识【可以使用质检任务id】
                                                        if (name != null) {
                                                            curName = String.valueOf(name.toString());  // 项目id
                                                        }

                                                        if (curName.compareToIgnoreCase("0") == 0) {
                                                            isUpdate = true;
                                                        }
                                                    }
                                                }
                                            }  else {
                                                System.out.println("current 2 exe ---999999---: " + retStatus.toString());
                                                String ret = (String) retStatus;
                                                if (ret == null) {
                                                    ret = "";
                                                }

                                                JSONObject retJsonObject = JSONObject.parseObject(ret);
                                                if (retJsonObject != null) {
                                                    String name = retJsonObject.getString("code");  // 文档名称

                                                    String curName = "";// 记录的唯一标识【可以使用质检任务id】
                                                    if (name != null) {
                                                        curName = String.valueOf(name.toString());  // 项目id
                                                    }

                                                    if (curName.compareToIgnoreCase("0") == 0) {
                                                        isUpdate = true;
                                                    }
                                                }
                                            }

                                            System.out.println("current exe ---777777---: " + jsonParams.toString());
                                            // 数据发送成功 更新数据库状态
                                            if (isUpdate) {
                                                // 更新订单表处理状态
                                                VoiceOrder voiceOrderStatus = new VoiceOrder();
                                                voiceOrderStatus.setId((int)curOrderId);
                                                voiceOrderService.updateSerSendStatus(voiceOrderStatus);
                                            }
                                        } else {
                                            System.out.println("current 1 exe ---999999---: " + retStatus.toString());
                                            String ret = (String) retStatus;
                                            if (ret == null) {
                                                ret = "";
                                            }

                                            JSONObject retJsonObject = JSONObject.parseObject(ret);
                                            if (retJsonObject != null) {
                                                String name = retJsonObject.getString("code");  // 文档名称

                                                String curName = "";// 记录的唯一标识【可以使用质检任务id】
                                                if (name != null) {
                                                    curName = String.valueOf(name.toString());  // 项目id
                                                }

                                                if (curName.compareToIgnoreCase("0") == 0) {
                                                    isUpdate = true;
                                                }
                                            }

                                            System.out.println("current exe ---8888888---: " + jsonParams.toString());
                                            // 数据发送成功 更新数据库状态
                                            if (isUpdate) {
                                                // 更新订单表处理状态
                                                VoiceOrder voiceOrderStatus = new VoiceOrder();
                                                voiceOrderStatus.setId((int)curOrderId);
                                                voiceOrderService.updateSerSendStatus(voiceOrderStatus);
                                            }
                                        }
                                    }
                                } else {
                                    System.out.println("current exe ---9999999---: " + jsonParams.toString());
                                    // 回调url 不存储在不进行处理
                                    // 更新订单表处理状态
                                    // VoiceOrder voiceOrderStatus = new VoiceOrder();
                                    // voiceOrderStatus.setId(curOrderId);
                                    // voiceOrderService.updateSerSendStatus(voiceOrderStatus);
                                }
                            }
                        }
                    }
                }


                long endtimeStamp = System.currentTimeMillis();
                System.out.println("current exe second: " + (endtimeStamp - timeStamp) / 1000 + "s");
                long diff = endtimeStamp - timeStamp;
                if (diff >= 4.8 * 60 * 1000) {
                    stop = true; // 本次超过最大运行时间就停止
                }
                Thread.sleep(500);
            }

            return Msg.success().add("job", "success");
        } catch (java.lang.InterruptedException e) {
            return Msg.fail(null);
        } catch (Exception e) {
            throw e;
            //return Msg.fail(null);
        }
    }
}
