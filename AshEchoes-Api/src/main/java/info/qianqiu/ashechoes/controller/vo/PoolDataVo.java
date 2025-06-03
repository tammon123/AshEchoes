package info.qianqiu.ashechoes.controller.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class PoolDataVo {

    private String name;
    private Long id;
    private String avatar;
    private String rank;
    private Integer count;
    private String init;
    private String ppol;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date time;

}
