package info.qianqiu.ashechoes.dto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.*;
import org.apache.ibatis.type.Alias;

import java.math.BigDecimal;
import java.util.List;

/**
 * 角色对象 character
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`character`")
@Alias("m_character")
public class Character {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long characterId;
    private Byte zhCount;

    /** 名称 */
    private String name;

    /** 基础攻击力加成 */
    private BigDecimal attack;

    /** 基础专精加成 */
    private BigDecimal mastery;

    /** 基础体质加成 */
    private BigDecimal health;

    /** 星级 */
    @TableField("`rank`")
    private String rank;

    /** 元素 */
    private String element;
    // 攻击元素类型
    @JsonIgnore
    @TableField(exist = false)
    private String attackElement;

    private String masteryRate;

    /** 职业 */
    private String role;

    /** 形状 */
    private String shape;
    /** 加成攻击 */
    private BigDecimal eAttack;

    /** 加成专精 */
    private BigDecimal eMastery;

    /** 加成体质 */
    private BigDecimal eHealth;
    @TableField(exist = false)
    private String img;
    @TableField(exist = false)
    private String pic;
    @TableField(exist = false)
    private String avatar;
    @TableField(exist = false)
    private List<Skill> skills;
    @TableField(exist = false)
    private List<Skill> leaderSkills;
    @TableField(exist = false)
    private List<Skill> selfSkills;

    /** 删除 */
    @TableLogic
    @JsonIgnore
    private Long del;

}
