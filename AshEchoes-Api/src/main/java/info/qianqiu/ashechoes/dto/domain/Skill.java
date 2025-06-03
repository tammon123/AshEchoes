package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 技能
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`skill`")
public class Skill {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long skillId;
    private Byte level;

    /** 乘区描述 */
    private String name;
    // 暂时忽略这个
    @TableField(exist = false)
    @JsonIgnore
    private String desc;
    //0 不限 9999排除
    @JsonIgnore
    private String addBehavior;
    @JsonIgnore
    private String behavior;
    @JsonIgnore
    private String allowShape;
    @JsonIgnore
    private String allowElement;
    @JsonIgnore
    private String damage;
    @TableField("`min_value`")
    @JsonIgnore
    private String minValue;
    @JsonIgnore
    @TableField("`max_value`")
    private String maxValue;
    @JsonIgnore
    private String maxBehavior;
    @JsonIgnore
    private String maxEnv;
    @JsonIgnore
    private String maxElement;
    @TableField(exist = false)
    private boolean hidden = false;
    @TableField(exist = false)
    private String icons;
    // 伤害的中文描述
    @TableField(exist = false)
    private String damaged;
    @TableField(exist = false)
    private String scoped;
    @TableField(exist = false)
    private String vald;
    // 比如有的技能加攻击，但是就自己能吃到
    @JsonIgnore
    private Byte scope;
    @JsonIgnore
    private Byte scopeNum;
    @JsonIgnore
    private Byte charSkillIndex;
    /** 1删除 */
    @TableLogic
    @JsonIgnore
    private Long del;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Skill skill = (Skill) o;
        return skillId.equals(skill.skillId);
    }

    @Override
    public int hashCode() {
        return skillId.hashCode();
    }
}
