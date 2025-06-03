package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 故事二创对象 story_creation
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
public class StoryCreation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** $column.columnComment */
    private Long id;

    /** 所属故事 */
    @Excel(name = "所属故事")
    private Long storyId;

    /** 下一章的故事ID */
    @Excel(name = "下一章的故事ID")
    private Long nextScId;

    /** 上一章的故事ID */
    @Excel(name = "上一章的故事ID")
    private Long lastScId;

    /** 内容 */
    @Excel(name = "内容")
    private String content;

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
    public void setNextScId(Long nextScId) 
    {
        this.nextScId = nextScId;
    }

    public Long getNextScId() 
    {
        return nextScId;
    }
    public void setLastScId(Long lastScId) 
    {
        this.lastScId = lastScId;
    }

    public Long getLastScId() 
    {
        return lastScId;
    }
    public void setContent(String content) 
    {
        this.content = content;
    }

    public String getContent() 
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("storyId", getStoryId())
            .append("nextScId", getNextScId())
            .append("lastScId", getLastScId())
            .append("content", getContent())
            .toString();
    }
}
