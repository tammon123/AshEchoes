package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 默认名称对象 behavior
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`behavior`")
public class Behavior {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long behaviorId;

    /** 烙痕名称 */
    private String name;

    /** 1删除 */
    @TableLogic
    @JsonIgnore
    private Long del;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Behavior behavior = (Behavior) o;
        return Objects.equals(behaviorId, behavior.behaviorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(behaviorId);
    }
}
