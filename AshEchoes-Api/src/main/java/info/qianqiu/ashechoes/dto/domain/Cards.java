package info.qianqiu.ashechoes.dto.domain;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.Date;

/**
 * 默认名称对象 behavior_effect
 * 
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("`cards`")
public class Cards {
    @TableField(exist = false)
    private ArrayList<Character> chars;
    @TableField(exist = false)
    private ArrayList<ArrayList<Skill>> chareqs;
    @TableField(exist = false)
    private ArrayList<Memory> memories;
    @TableField(exist = false)
    private ArrayList<Skill> skills;
    @TableField(exist = false)
    private ArrayList<String> charIcons;
    @TableField(exist = false)
    private String keyWord;
    @TableField(exist = false)
    private Boolean modify = false;
    @TableField(exist = false)
    private Boolean agree = true;
    /** 主键 */
    @TableId(type = IdType.AUTO)
    private Long cardId;
    private String charNames;
    private String memoryNames;
    private String remark;
    private String spskills;
    private String sp2skills;
    private String uid;
    private String nickname;
    private String title;
    private Byte status;
    @JsonIgnore
    private Long sort;
    @TableField("`likes`")
    private Long likes;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonIgnore
    private Date updateTime;
    /** 1删除 */
    @TableLogic
    @JsonIgnore
    private Long del;

}
