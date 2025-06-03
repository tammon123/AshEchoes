package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 角色拥有的技能
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`skill_character`")
public class SkillCharacter {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long scId;

    private Long skillId;
    private Long charId;
    @TableField("`leader`")
    private Byte leader;

    /** 1删除 */
    @TableLogic
    @JsonIgnore
    private Long del;

}
