package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@TableName("`user`")
public class User {

    @TableId
    private Long uid;
    @TableField(exist = false)
    private String uuid;
    private String account;
    @TableField(exist = false)
    private String code;
    private String password;
    private String email;
    private String openid;
    private String nickname;
    private String avatar;
    private String token;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("`time`")
    private Date time;
    @TableLogic
    @JsonIgnore
    private String del;
}
