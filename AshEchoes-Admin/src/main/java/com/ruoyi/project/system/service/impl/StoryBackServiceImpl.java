package com.ruoyi.project.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.StoryBackMapper;
import com.ruoyi.project.system.domain.StoryBack;
import com.ruoyi.project.system.service.IStoryBackService;

/**
 * 故事线Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
@Service
public class StoryBackServiceImpl implements IStoryBackService 
{
    @Autowired
    private StoryBackMapper storyBackMapper;

    /**
     * 查询故事线
     * 
     * @param id 故事线主键
     * @return 故事线
     */
    @Override
    public StoryBack selectStoryBackById(Long id)
    {
        return storyBackMapper.selectStoryBackById(id);
    }

    /**
     * 查询故事线列表
     * 
     * @param storyBack 故事线
     * @return 故事线
     */
    @Override
    public List<StoryBack> selectStoryBackList(StoryBack storyBack)
    {
        return storyBackMapper.selectStoryBackList(storyBack);
    }

    /**
     * 新增故事线
     * 
     * @param storyBack 故事线
     * @return 结果
     */
    @Override
    public int insertStoryBack(StoryBack storyBack)
    {
        storyBack.setCreateTime(DateUtils.getNowDate());
        return storyBackMapper.insertStoryBack(storyBack);
    }

    /**
     * 修改故事线
     * 
     * @param storyBack 故事线
     * @return 结果
     */
    @Override
    public int updateStoryBack(StoryBack storyBack)
    {
        storyBack.setUpdateTime(DateUtils.getNowDate());
        return storyBackMapper.updateStoryBack(storyBack);
    }

    /**
     * 批量删除故事线
     * 
     * @param ids 需要删除的故事线主键
     * @return 结果
     */
    @Override
    public int deleteStoryBackByIds(Long[] ids)
    {
        return storyBackMapper.deleteStoryBackByIds(ids);
    }

    /**
     * 删除故事线信息
     * 
     * @param id 故事线主键
     * @return 结果
     */
    @Override
    public int deleteStoryBackById(Long id)
    {
        return storyBackMapper.deleteStoryBackById(id);
    }
}
