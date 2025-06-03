package info.qianqiu.ashechoes.dto.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.*;

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
@TableName("`damage`")
public class Damage {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long damageId;

    /** 乘区描述 */
    private String name;

    /** 1删除 */
    @TableLogic
    @JsonIgnore
    private Long del;

}
