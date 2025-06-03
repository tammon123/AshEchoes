package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 烙痕拥有的技能
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`skill_memory`")
public class SkillMemory {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long scId;

    private Long skillId;
    private Long memoryId;

    /** 1删除 */
    @TableLogic
    @JsonIgnore
    private Long del;

}
