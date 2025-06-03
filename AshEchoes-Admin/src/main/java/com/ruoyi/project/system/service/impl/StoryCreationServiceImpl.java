package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.StoryCreationMapper;
import com.ruoyi.project.system.domain.StoryCreation;
import com.ruoyi.project.system.service.IStoryCreationService;

/**
 * 故事二创Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
@Service
public class StoryCreationServiceImpl implements IStoryCreationService 
{
    @Autowired
    private StoryCreationMapper storyCreationMapper;

    /**
     * 查询故事二创
     * 
     * @param id 故事二创主键
     * @return 故事二创
     */
    @Override
    public StoryCreation selectStoryCreationById(Long id)
    {
        return storyCreationMapper.selectStoryCreationById(id);
    }

    /**
     * 查询故事二创列表
     * 
     * @param storyCreation 故事二创
     * @return 故事二创
     */
    @Override
    public List<StoryCreation> selectStoryCreationList(StoryCreation storyCreation)
    {
        return storyCreationMapper.selectStoryCreationList(storyCreation);
    }

    /**
     * 新增故事二创
     * 
     * @param storyCreation 故事二创
     * @return 结果
     */
    @Override
    public int insertStoryCreation(StoryCreation storyCreation)
    {
        return storyCreationMapper.insertStoryCreation(storyCreation);
    }

    /**
     * 修改故事二创
     * 
     * @param storyCreation 故事二创
     * @return 结果
     */
    @Override
    public int updateStoryCreation(StoryCreation storyCreation)
    {
        return storyCreationMapper.updateStoryCreation(storyCreation);
    }

    /**
     * 批量删除故事二创
     * 
     * @param ids 需要删除的故事二创主键
     * @return 结果
     */
    @Override
    public int deleteStoryCreationByIds(Long[] ids)
    {
        return storyCreationMapper.deleteStoryCreationByIds(ids);
    }

    /**
     * 删除故事二创信息
     * 
     * @param id 故事二创主键
     * @return 结果
     */
    @Override
    public int deleteStoryCreationById(Long id)
    {
        return storyCreationMapper.deleteStoryCreationById(id);
    }
}
