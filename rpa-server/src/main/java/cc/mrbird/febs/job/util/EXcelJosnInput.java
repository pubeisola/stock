package cc.mrbird.febs.job.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.*;

/*******************************************************************************
 * 导入
 *
 * 读取Excel文本或者json数据。返回需要的文章内容序列
 *
 */
public class EXcelJosnInput {
  /**
   * 本方法返回数据从txt 文件读取
   *
   * Map的key表示句子的需要，mapvalue为句子的内容;
   * 1是客服
   * 2是用户
   * @param
   */
  public static Map<Integer,String> ContentTxt(Integer type,String path) {
      Map<Integer,String> returnMap   = new HashMap<>();
      Map<Integer,String> mapAll      = new HashMap<>();
      Map<Integer,String> mapCustomer = new HashMap<>();
      Map<Integer,String> mapUser     = new HashMap<>();

      List<String[]> ll               = getTxtAllData(path);
      int iAll     = 1;
      int iCustoer = 1;
      int iUser    = 1;

      for (String[]  strings :ll) {
        mapAll.put(iAll, strings[3]);
        iAll  = iAll  + 1;

        if (strings[0].equals("1")) {
          mapCustomer.put(iCustoer, strings[3]);
          iCustoer = iCustoer+1;
        }

        if (strings[0].equals("2")) {
          mapUser.put(iUser, strings[3]);
          iUser= iUser+1;
        }
      }

      if (type==0)
      {
        returnMap.putAll(mapAll);
      }
      else if (type == 1) {
        returnMap.putAll(mapCustomer);
      }
      else if (type == 2) {
        returnMap.putAll(mapUser);
      }

      return returnMap;
  }

  /**
   * 解析纯文本行数据
   * @return List<String[]>
   */
  public static List<String[]> getTxtAllData(String path) {
    List<String[]> dataList = new ArrayList<String[]>();
    String txtContent = EXcelJosnInput.readFileContent(path);

    if (txtContent != null) {
      if (txtContent.length() > 0) {
        String[] lines = txtContent.split("\\r?\\n");
        if (lines != null) {
          for (String line : lines) {
            String[] row = txtContent.split("~~~~~");
            // rl // se //bg // ed   上边的字段顺序
            if (row != null) {
                 dataList.add(row);
            }
          }
        }
      }
    }

    return dataList;
  }

  public static String readFileContent(String fileName) {
      StringBuffer sbf      = new StringBuffer();
      BufferedReader reader = null;

      try {
        File file      = new File(fileName);
        if (!file.exists()) {
            return sbf.toString();
        }

        reader = new BufferedReader(new FileReader(file));
        String tempStr;
        while ((tempStr = reader.readLine()) != null) {
          sbf.append(tempStr);
        }
        reader.close();
        return sbf.toString();
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e1) {
            e1.printStackTrace();
          }
        }
      }
      return sbf.toString();
  }

	/**
	 * 取Excel所有数据，包含header
	 * @return List<String[]>
	 */
	public static List<String[]> getAllData(String path) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		Workbook wb = null;
		List<String[]> dataList = new ArrayList<String[]>();
		try {
			InputStream inp = new FileInputStream(path);
			wb = WorkbookFactory.create(inp);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int columnNum = 0;
		Sheet sheet = wb.getSheetAt(0);
		if (sheet.getRow(0) != null) {
			columnNum = sheet.getRow(0).getLastCellNum()
					- sheet.getRow(0).getFirstCellNum();
		}
		if (columnNum > 0) {
			for (Row row : sheet) {
				String[] singleRow = new String[columnNum];
				int n = 0;
				for (int i = 0; i < columnNum; i++) {
					Cell cell = row.getCell(i, Row.CREATE_NULL_AS_BLANK);
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						singleRow[n] = "";
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						singleRow[n] = Boolean.toString(cell
								.getBooleanCellValue());
						break;
					// 数值
					case Cell.CELL_TYPE_NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							if (cell.getDateCellValue().toString()
									.contains("1899")) {
								singleRow[n] = cell.getDateCellValue()
										.toString().substring(11, 16);
							} else {
								singleRow[n] = f.format(cell.getDateCellValue());
							}
						} else {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							String temp = cell.getStringCellValue();

							singleRow[n] = temp.trim();

						}
						break;
					case Cell.CELL_TYPE_STRING:
						singleRow[n] = cell.getStringCellValue().trim()
								.replaceAll("/", "-");
						break;
					case Cell.CELL_TYPE_ERROR:
						singleRow[n] = "";
						break;
					default:
						singleRow[n] = "";
						break;
					}
					n++;
				}
				if ("".equals(singleRow[0])) {// 如果第一行为空，跳过
					continue;
				}
				dataList.add(singleRow);
			}
		}
		dataList.remove(0); // 删除excel第一列的标题
//		dataList.remove(1);
		return dataList;
	}

	/**
	 * Map的key表示句子的需要，mapvalue为句子的内容;
	 * 1是客服
	 * 2是用户
	 * @param
	 */

	public static Map<Integer,String> ContentExcle(Integer type,String path){
		Map<Integer,String> returnMap = new HashMap<>();
		Map<Integer,String> mapAll = new HashMap<>();
		Map<Integer,String> mapCustomer = new HashMap<>();
		Map<Integer,String> mapUser = new HashMap<>();
		List<String[]> ll = getAllData(path);
		int iAll=1;
		int iCustoer=1;
		int iUser=1;
		for (String[]  strings :ll){
			mapAll.put(iAll,strings[3]);
			iAll=iAll+1;
			if (strings[0].equals("1")){
				mapCustomer.put(iCustoer,strings[3]);
				iCustoer = iCustoer+1;
			}
			if (strings[0].equals("2")){
				mapUser.put(iUser,strings[3]);
				iUser= iUser+1;
			}
		}
		if (type==0)
			returnMap.putAll(mapAll);
		else if (type == 1)
			returnMap.putAll(mapCustomer);
		else if (type == 2)
			returnMap.putAll(mapUser);
		return returnMap;
	}


	public static void main(String args[]){
		String path = "D:\\项目\\语音质检\\2020-05-18\\0a6c32373c9065ed60e54decff0e3013-25861.xlsx";
//		Map<Integer,String> mm = ContentExcle(0,path);
		List<Map<String,String>> mm = DiaContent(path);
		System.out.println(mm);
//		String json ="[{\"bg\":1.64,\"ed\":3.51,\"rl\":\"1\",\"se\":\"\\u5509\\u4f60\\u597d\\uff0c\\u4ed6\\u4e0d\\u7406\\u89e3\\u3002\"},{\"bg\":3.9,\"ed\":7.6,\"rl\":\"1\",\"se\":\"\\u5509\\u60a8\\u597d\\uff0c\\u8bf7\\u95ee\\u662f\\u65b0Q72H\\u5e7a\\u7684\\u8f66\\u4e3b\\u662f\\u5417\\uff1f\"},{\"bg\":9.539999999999999,\"ed\":10.79,\"rl\":\"1\",\"se\":\"why\\uff1f\"},{\"bg\":11.5,\"ed\":12.77,\"rl\":\"1\",\"se\":\"\\u5582\\u4f60\\u597d\\uff01\"},{\"bg\":17.21,\"ed\":18.45,\"rl\":\"2\",\"se\":\"\\u5582\\u4f60\\u597d\\uff01\"},{\"bg\":18.46,\"ed\":22.58,\"rl\":\"1\",\"se\":\"\\u5582\\uff0c\\u5148\\u751f\\uff0c\\u60a8\\u597d\\uff0c\\u8bf7\\u95ee\\u662f\\u91d1QC2H1\\u7684\\u8f66\\u4e3b\\u5417\\uff1f\"},{\"bg\":23.23,\"ed\":24.65,\"rl\":\"2\",\"se\":\"\\u8f66\\u4e3b\\uff01\"},{\"bg\":24.76,\"ed\":28.76,\"rl\":\"1\",\"se\":\"\\u55ef\\u5bf9\\uff0c\\u4e00\\u8f86\\u8def\\u8def\\u864e\\u6781\\u5149\\u4ec0\\u4e48\\u662f\\u60a8\\u5728\\u4f7f\\u7528\\u5417\\uff1f\"},{\"bg\":29.7,\"ed\":31.27,\"rl\":\"2\",\"se\":\"\\u6211\\u4e0d\\u77e5\\u9053\\u3002\"},{\"bg\":31.28,\"ed\":33.39,\"rl\":\"1\",\"se\":\"\\u554a\\u4f60\\u6ca1\\u6709\\u8fd9\\u4e2a\\u8f66\\u662f\\u5417\\uff1f\"},{\"bg\":34.08,\"ed\":36.53,\"rl\":\"2\",\"se\":\"\\u6211\\u4e0d\\u4e0d\\u662f\\u5317\\u4eac\\u7684\\uff01\"},{\"bg\":36.77,\"ed\":41.54,\"rl\":\"1\",\"se\":\"\\u5662\\u884c\\u884c\\uff0c\\u90a3\\u53ef\\u80fd\\u6211\\u8fd9\\u8fb9\\u7cfb\\u7edf\\u51fa\\u9519\\u4e86\\uff0c\\u6253\\u6270\\u60a8\\u4e86\\uff0c\\u5148\\u751f\\uff0c\\u554a\\u518d\\u89c1\\uff01\"}]";
//		JSONArray jsonArray= JSON.parseArray(json);
//		System.out.println(jsonArray);
//		Map<Integer, String> a = ContentJson(1,json);
//		System.out.println(a);
	}

	public static Map<Integer,String> ContentJson(Integer type,String json){
		Map<Integer,String> returnMap = new HashMap<>();
		Map<Integer,String> mapAll = new HashMap<>();
		Map<Integer,String> mapCustomer = new HashMap<>();
		Map<Integer,String> mapUser = new HashMap<>();
		JSONArray jsonArray= JSON.parseArray(json);
		int iAll=1;
		int iCustoer=1;
		int iUser=1;
		for (Object jj:jsonArray){
			JSONObject jsonObject = JSON.parseObject(String.valueOf(jj));
			mapAll.put(iAll,jsonObject.get("se").toString());
			iAll=iAll+1;
			if (jsonObject.get("rl").equals("1")){
				mapCustomer.put(iCustoer,jsonObject.get("se").toString());
				iCustoer = iCustoer+1;
			}
			if (jsonObject.get("rl").equals("2")){
				mapUser.put(iUser,jsonObject.get("se").toString());
				iUser= iUser+1;
			}
		}
		if (type==0)
			returnMap.putAll(mapAll);
		else if (type == 1)
			returnMap.putAll(mapCustomer);
		else if (type == 2)
			returnMap.putAll(mapUser);
		return returnMap;
	}


	public static List<Map<String,String>> DiaContent(String path){
		List<Map<String,String>> ruturnList = new ArrayList<>();
		List<String[]> ll = getAllData(path);
		for (String[] str :ll){
			Map<String,String> mv = new HashMap<>();
			mv.put("roles",str[0]);
			mv.put("content",str[3]);
			ruturnList.add(mv);
		}
		return ruturnList;
	}
}
