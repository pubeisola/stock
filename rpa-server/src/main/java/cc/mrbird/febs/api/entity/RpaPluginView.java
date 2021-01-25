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
@TableName("rpa_plugin_config")
public class RpaPluginView {

    /**
     * 记录自增id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 插件语言
     */
    @TableField("language")
    private String language;

    /**
     * 插件id
     */
    @TableField("plugin_id")
    private String plugin_id;

    /**
     * 插件名称
     */
    @TableField("plugin_name")
    private String plugin_name;

    /**
     * 插件作者
     */
    @TableField("author")
    private String author;

    /**
     * 插件版本
     */
    @TableField("version")
    private String version;


    /**
     * 插件描述
     */
    @TableField("plugin_description")
    private String plugin_description;



    /**
     * 插件md5
     */
    @TableField("attachment_md5")
    private String attachment_md5;

    /**
     * 插件更新日期
     */
    @TableField("updatedAt")
    private String updatedAt;

}
