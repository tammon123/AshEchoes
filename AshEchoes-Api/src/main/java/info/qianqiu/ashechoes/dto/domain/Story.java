package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 伤害乘区对象 damage
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`story`")
public class Story {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parentId;
    private String banner;
    private String title;
    private String type;
    private String chars;
    private String sTime;
    private String world;
    private String author;
    @TableField(value = "`desc`")
    private String desc;
    private String summary;
    private String otext;

    @TableField(exist = false)
    private Long nextId = -1L;
    @TableField(exist = false)
    private Long lastId = -1L;
    @TableField(exist = false)
    private List<Story> children;

    /** 乘区描述 */
    private Long sort;

}
