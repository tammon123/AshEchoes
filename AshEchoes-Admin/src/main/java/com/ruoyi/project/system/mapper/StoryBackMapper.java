package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.StoryBack;

/**
 * 故事线Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
public interface StoryBackMapper 
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
     * 删除故事线
     * 
     * @param id 故事线主键
     * @return 结果
     */
    public int deleteStoryBackById(Long id);

    /**
     * 批量删除故事线
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoryBackByIds(Long[] ids);
}
