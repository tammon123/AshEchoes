package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.StoryCreation;

/**
 * 故事二创Service接口
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
public interface IStoryCreationService 
{
    /**
     * 查询故事二创
     * 
     * @param id 故事二创主键
     * @return 故事二创
     */
    public StoryCreation selectStoryCreationById(Long id);

    /**
     * 查询故事二创列表
     * 
     * @param storyCreation 故事二创
     * @return 故事二创集合
     */
    public List<StoryCreation> selectStoryCreationList(StoryCreation storyCreation);

    /**
     * 新增故事二创
     * 
     * @param storyCreation 故事二创
     * @return 结果
     */
    public int insertStoryCreation(StoryCreation storyCreation);

    /**
     * 修改故事二创
     * 
     * @param storyCreation 故事二创
     * @return 结果
     */
    public int updateStoryCreation(StoryCreation storyCreation);

    /**
     * 批量删除故事二创
     * 
     * @param ids 需要删除的故事二创主键集合
     * @return 结果
     */
    public int deleteStoryCreationByIds(Long[] ids);

    /**
     * 删除故事二创信息
     * 
     * @param id 故事二创主键
     * @return 结果
     */
    public int deleteStoryCreationById(Long id);
}
