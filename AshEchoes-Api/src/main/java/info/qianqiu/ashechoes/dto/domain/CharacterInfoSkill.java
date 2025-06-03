package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`character_info_skill`")
public class CharacterInfoSkill {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long siId;
    private Long cId = 0L;

    /** 乘区描述 */
    private String cName;
    private String sName;
    private String type;
    private String value;
    private String add1;
    private String add2;
    private String count;
    private String element;
    private String attr;
    @TableField("`behavior`")
    private String behavior;
    private Byte charSkillIndex;


}
