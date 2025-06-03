package com.ruoyi.project.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 故事线对象 story
 *
 * @author ruoyi
 * @date 2025-02-11
 */
public class Story extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * $column.columnComment
     */
    private Long id;
    private Long parentId;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Long sort;

    /**
     * 标题
     */
    @Excel(name = "标题")
    private String title;

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    private String banner;

    /**
     * 类型
     */
    @Excel(name = "类型")
    private String type;

    /**
     * 同调者
     */
    @Excel(name = "同调者")
    private String chars;

    /**
     * 故事时间
     */
    @Excel(name = "故事时间")
    private String sTime;

    /**
     * 所属世界
     */
    @Excel(name = "所属世界")
    private String world;

    /**
     * 整理者
     */
    @Excel(name = "整理者")
    private String author;

    /**
     * 故事概要
     */
    @Excel(name = "故事概要")
    private String desc;
    private String otext;

    /**
     * 总结
     */
    @Excel(name = "总结")
    private String summary;
    @Excel(name = "创建时间")
    private Date createTime;
    @Excel(name = "更新时间")
    private Date updateTime;
    @Excel(name = "创建者")
    private String createBy;
    @Excel(name = "更新者")
    private String updateBy;

    private boolean hasChildren = false;
    private List<Story> children = new ArrayList<>();

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public boolean isHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<Story> getChildren() {
        return children;
    }

    public void setChildren(List<Story> children) {
        this.children = children;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Override
    public String getUpdateBy() {
        return updateBy;
    }

    @Override
    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setSort(Long sort) {
        this.sort = sort;
    }

    public Long getSort() {
        return sort;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setChars(String chars) {
        this.chars = chars;
    }

    public String getChars() {
        return chars;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getsTime() {
        return sTime;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getWorld() {
        return world;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return author;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSummary() {
        return summary;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("sort", getSort())
                .append("title", getTitle())
                .append("type", getType())
                .append("chars", getChars())
                .append("sTime", getsTime())
                .append("world", getWorld())
                .append("author", getAuthor())
                .append("desc", getDesc())
                .append("summary", getSummary())
                .toString();
    }

    public String getOtext() {
        return otext;
    }

    public void setOtext(String otext) {
        this.otext = otext;
    }
}
