package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("`pool_data_rank`")
public class PoolDataRank {

    @TableId
    private Long pdrId;
    private Long poolType;
    private Long tCount;
    private Long tHasCount;
    private Long upCount;
    private Long upHasCount;
    private String tRate;
    private String tAvg;
    private String uAvg;
    private String uTr;
    private String uid;
    private Long allow;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("`start_time`")
    private Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("`last_time`")
    private Date lastTime;

}
