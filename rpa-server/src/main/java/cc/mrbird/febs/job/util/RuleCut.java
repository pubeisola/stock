package cc.mrbird.febs.job.util;

import java.util.Map;

import static cc.mrbird.febs.job.configure.SimpleConfigs.Flag;

public class RuleCut {

    public static String CutSentence(String path,String rule_position,String numbers){

        int number = Integer.parseInt(numbers);

        /**
         *  如果是exce l按excel 处理
         *  如果是 txt 按txt 处理
         */
        String filePart[] = path.split("\\.");
        String fileExt = "";
        if (filePart != null) {
            if (filePart.length == 2) {
                if (filePart[1] != null) {
                    fileExt = filePart[1].toLowerCase();

                }
            }
        }

        // 如果是txt 就处理为txt 否则就处理为excel
        Map<Integer,String> content = fileExt.compareToIgnoreCase("txt") == 0 ? EXcelJosnInput.ContentTxt(0, path)  :  EXcelJosnInput.ContentExcle(0, path);

        if (Flag) System.out.println("mapcontent ============================"+content);
        StringBuffer buffer = new StringBuffer();
        if (rule_position.equals("") || rule_position == null){
          rule_position ="全文";
        }

        if (rule_position.equals("全文")){
            for (String ss:content.values()){
                buffer.append(ss);
            }
        }
        if (rule_position.equals("开头")){
            if (content.size()>number){
                for (int i=1;i<=number;i++){
                    buffer.append(content.get(i));
                }
            }else {
                for (String ss:content.values()){
                    buffer.append(ss);
                }
            }
        }
        if (rule_position.equals("结束")){
            if (content.size()>number) {
                for (int i=content.size()-number+1;i<=content.size();i++){
                    buffer.append(content.get(i));
                }
            }else {
                for (String ss:content.values()){
                    buffer.append(ss);
                }
            }
        }
        return buffer.toString();
    }


    public static void main(String args[]){
        String path ="D:\\upload\\20200522\\00f4f3f62d6cf50075d57469ffd12221-25898.xlsx";

        String values = CutSentence(path,"结束","1");
        System.out.println(values);
    }



}
