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
 * @date 2020-05-27 19:12:10
 */
@Data
@Excel("插件报表")
@TableName("rpa_plugin_config")
public class RpaPluginConfig {

    /**
     * 记录自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelField("id")
    private Integer id;

    /**
     * 插件id
     */
    @TableField("plugin_id")
    @ExcelField("plugin_id")
    private String pluginId;

    /**
     * 插件描述
     */
    @TableField("plugin_description")
    @ExcelField("plugin_description")
    private String pluginDescription;

    /**
     * 插件语言
     */
    @TableField("language")
    @ExcelField("language")
    private String language;

    /**
     * 插件md5
     */
    @TableField("attachment_md5")
    @ExcelField("attachment_md5")
    private String attachmentMd5;

    /**
     * 插件作者
     */
    @TableField("author")
    @ExcelField("author")
    private String author;

    /**
     * 插件名称
     */
    @TableField("plugin_name")
    @ExcelField("plugin_name")
    private String pluginName;

    /**
     * 插件版本
     */
    @TableField("version")
    @ExcelField("version")
    private String version;

    /**
     * 插件创建日期
     */
    @TableField("createAt")
    @ExcelField("createAt")
    private String createat;

    /**
     * 插件更新日期
     */
    @TableField("updatedAt")
    @ExcelField("updatedAt")
    private String updatedat;

    /**
     * 全部  // 0: 全部 1：下载 2：更新
     */
    @TableField("plugin_state")
    @ExcelField("plugin_state")
    private Byte pluginState;

    /**
     * 所属用户 用户  //uiauto
     */
    @TableField("user_id")
    private String userId;

    /**
     * 公司id // saas平台的公司id
     */
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

}
