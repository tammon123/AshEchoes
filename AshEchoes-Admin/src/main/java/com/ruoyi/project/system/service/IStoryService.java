package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.Story;

/**
 * 故事线Service接口
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
public interface IStoryService 
{
    /**
     * 查询故事线
     * 
     * @param id 故事线主键
     * @return 故事线
     */
    public Story selectStoryById(Long id);

    /**
     * 查询故事线列表
     * 
     * @param story 故事线
     * @return 故事线集合
     */
    public List<Story> selectStoryList(Story story);

    /**
     * 新增故事线
     * 
     * @param story 故事线
     * @return 结果
     */
    public int insertStory(Story story);

    /**
     * 修改故事线
     * 
     * @param story 故事线
     * @return 结果
     */
    public int updateStory(Story story);

    /**
     * 批量删除故事线
     * 
     * @param ids 需要删除的故事线主键集合
     * @return 结果
     */
    public int deleteStoryByIds(Long[] ids);

    /**
     * 删除故事线信息
     * 
     * @param id 故事线主键
     * @return 结果
     */
    public int deleteStoryById(Long id);
}
