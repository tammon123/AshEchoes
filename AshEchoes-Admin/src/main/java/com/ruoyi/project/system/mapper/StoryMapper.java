package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.Story;

/**
 * 故事线Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
public interface StoryMapper 
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
     * 删除故事线
     * 
     * @param id 故事线主键
     * @return 结果
     */
    public int deleteStoryById(Long id);

    /**
     * 批量删除故事线
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoryByIds(Long[] ids);
}
