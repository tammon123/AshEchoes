package info.qianqiu.ashechoes.dto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.*;

import java.util.List;

/**
 * 默认名称对象 memory
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`memory`")
public class Memory {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long memoryId;

    /** 烙痕名称 */
    private String name;

    /** 烙痕品质 */
    @TableField("`rank`")
    private String rank;

    /** 烙痕分类 */
    private String category;

    /** 烙痕数值加成 体质_防御_攻击_专精_终端 */
    private String value;
    private String origin;
    @TableField(exist = false)
    private List<Skill> skills;
    @TableField(exist = false)
    private String img;

    /** 1删除 */
    @TableLogic
    @JsonIgnore
    private Long del;

}
