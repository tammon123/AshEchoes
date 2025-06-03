package com.ruoyi.project.system.service;

import java.util.List;
import com.ruoyi.project.system.domain.StoryBack;

/**
 * 故事线Service接口
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
public interface IStoryBackService 
{
    /**
     * 查询故事线
     * 
     * @param id 故事线主键
     * @return 故事线
     */
    public StoryBack selectStoryBackById(Long id);

    /**
     * 查询故事线列表
     * 
     * @param storyBack 故事线
     * @return 故事线集合
     */
    public List<StoryBack> selectStoryBackList(StoryBack storyBack);

    /**
     * 新增故事线
     * 
     * @param storyBack 故事线
     * @return 结果
     */
    public int insertStoryBack(StoryBack storyBack);

    /**
     * 修改故事线
     * 
     * @param storyBack 故事线
     * @return 结果
     */
    public int updateStoryBack(StoryBack storyBack);

    /**
     * 批量删除故事线
     * 
     * @param ids 需要删除的故事线主键集合
     * @return 结果
     */
    public int deleteStoryBackByIds(Long[] ids);

    /**
     * 删除故事线信息
     * 
     * @param id 故事线主键
     * @return 结果
     */
    public int deleteStoryBackById(Long id);
}
