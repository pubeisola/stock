package cc.mrbird.febs.job.task;

import cc.mrbird.febs.job.utils.RuleAnalyze;

public class QualityTask {

    /**
     * 给定一组规则----》场景
     * 计算Word文档内容在每一个规则下的得分情况
     * @return
     */

    public static int RuleScore(String content,String rule,String score,String rule_appear,String rule_status){
      boolean values=false;
      if (rule.contains("(")){
        values =  RuleAnalyze.main_function(content,rule);
      }else if (rule.contains("AND") ||
        rule.contains("OR") ||
        rule.contains("NEAR") ||
        rule.contains("BEFORE") ||
        rule.contains("AFTER")){
        values = RuleAnalyze.main_function(content ,"( "+rule+ " )");
      }else {
        values = RuleAnalyze.OneRule(content ,rule);
      }
        Integer sc=0;
        if (rule_status.equals("加")){
            sc = Integer.parseInt(score);
        }else if (rule_status.equals("减")){
           sc= -Integer.parseInt(score);
        }
        if (rule_appear.equals("") ||rule_appear.equals("出现")||rule_appear == null){
            return values ? sc:0;
        }else if(rule_appear.equals("未出现")){
            return values ? 0:sc;
        }else {
            return 0;
        }
    }
}
