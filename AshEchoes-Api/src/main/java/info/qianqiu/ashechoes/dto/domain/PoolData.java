package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`pool_data`")
public class PoolData {

    @TableId
    private Long id;
    private String uid;
    private String pool;
    private String name;
    @TableField("`type`")
    private Byte type;
    @TableField("`rank`")
    private Byte rank;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("`time`")
    private Date time;

}
