package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("`thanks`")
public class Thanks {

    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long thankId;
    private String name;
    private String nc;
    private String platform;
    private String pc;
    private String reason;
    private String rc;
    private String avatar;
    private String banner;
    private String uid;
    /** 1删除 */
    @TableLogic
    @JsonIgnore
    private Long del;
}
