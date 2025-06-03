package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 故事关键字对象 story_keyword
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
public class StoryKeyword extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 所属故事 */
    @Excel(name = "所属故事")
    private Long storyId;

    /** 关键字 */
    @Excel(name = "关键字")
    private String keyword;

    /** 跳转链接 */
    @Excel(name = "跳转链接")
    private String redirectUrl;

    /** banner */
    @Excel(name = "banner")
    private String bannel;

    /** 关联类型 */
    @Excel(name = "关联类型")
    private String connType;

    /** 关联ID */
    @Excel(name = "关联ID")
    private String connId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStoryId(Long storyId) 
    {
        this.storyId = storyId;
    }

    public Long getStoryId() 
    {
        return storyId;
    }
    public void setKeyword(String keyword) 
    {
        this.keyword = keyword;
    }

    public String getKeyword() 
    {
        return keyword;
    }
    public void setRedirectUrl(String redirectUrl) 
    {
        this.redirectUrl = redirectUrl;
    }

    public String getRedirectUrl() 
    {
        return redirectUrl;
    }
    public void setBannel(String bannel) 
    {
        this.bannel = bannel;
    }

    public String getBannel() 
    {
        return bannel;
    }
    public void setConnType(String connType) 
    {
        this.connType = connType;
    }

    public String getConnType() 
    {
        return connType;
    }
    public void setConnId(String connId) 
    {
        this.connId = connId;
    }

    public String getConnId() 
    {
        return connId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("storyId", getStoryId())
            .append("keyword", getKeyword())
            .append("redirectUrl", getRedirectUrl())
            .append("bannel", getBannel())
            .append("connType", getConnType())
            .append("connId", getConnId())
            .toString();
    }
}
