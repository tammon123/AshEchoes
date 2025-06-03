package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 伤害乘区对象 memory_director
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`memory_director`")
public class MemoryDirector {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long mtId;

    /** 乘区描述 */
    private Long memoryId;
    private Long uid;
    private Long count;

}
