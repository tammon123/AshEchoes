package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

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
@TableName("`suggest`")
public class Suggest {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long suggestId;
    @TableField("`desc`")
    private String desc;
    @TableField("`ans`")
    private String ans;
    @TableField("`time`")
    private Date time;
    private Date initTime;
    private String uid;
    private String ip;
    @TableField(exist = false)
    private Long page = 1L;
    @TableField(exist = false)
    private Long size = 1L;

}
