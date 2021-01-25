package cc.mrbird.febs.api.entity;

import java.util.Date;

import com.wuwenze.poi.annotation.Excel;
import com.wuwenze.poi.annotation.ExcelField;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 *  Entity
 *
 * @author MrBird
 * @date 2020-05-27 19:56:26
 */
@Data
@Excel("已安装插件报表")
@TableName("rpa_plugin_user")
public class RpaPluginUser {

    /**
     * 
     */
    @ExcelField("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 所属用户 用户  //uiauto
     */
    @ExcelField("user_id")
    @TableField("user_id")
    private String userId;

    /**
     * 公司id // saas平台的公司id
     */
    @ExcelField("company_id")
    @TableField("company_id")
    private Integer companyId;

    /**
     * 服务端用户id for rpa  和客户端可能相同
     */

    @TableField("user_id2")
    private Integer userId2;

    /**
     * 平台id
     */
    @TableField("plation_id")
    private Integer plationId;

    /**
     * 插件配置表的id
     */
    @TableField("rpa_plugin_config_id")
    private Integer rpaPluginConfigId;

    /**
     * 插件状态   // 0: 全部 1：下载 2：更新
     */
    @ExcelField("plugin_state")
    @TableField("plugin_state")
    private Integer pluginState;

    /**
     * 插件创建日期
     */
    @ExcelField("createAt")
    @TableField("createAt")
    private String createat;

    /**
     * 插件更新日期
     */
    @ExcelField("updatedAt")
    @TableField("updatedAt")
    private String updatedat;

}
