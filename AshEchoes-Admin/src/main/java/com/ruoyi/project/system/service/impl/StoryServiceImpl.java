package com.ruoyi.project.system.service.impl;

import java.util.Date;
import java.util.List;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.security.LoginUser;
import com.ruoyi.project.system.domain.StoryBack;
import com.ruoyi.project.system.service.IStoryBackService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.StoryMapper;
import com.ruoyi.project.system.domain.Story;
import com.ruoyi.project.system.service.IStoryService;

/**
 * 故事线Service业务层处理
 *
 * @author ruoyi
 * @date 2025-02-11
 */
@Service
public class StoryServiceImpl implements IStoryService {
    @Autowired
    private StoryMapper storyMapper;
    @Autowired
    private IStoryBackService storyBackService;

    /**
     * 查询故事线
     *
     * @param id 故事线主键
     * @return 故事线
     */
    @Override
    public Story selectStoryById(Long id) {
        return storyMapper.selectStoryById(id);
    }

    /**
     * 查询故事线列表
     *
     * @param story 故事线
     * @return 故事线
     */
    @Override
    public List<Story> selectStoryList(Story story) {
        return storyMapper.selectStoryList(story);
    }

    /**
     * 新增故事线
     *
     * @param story 故事线
     * @return 结果
     */
    @Override
    public int insertStory(Story story) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        story.setCreateBy(loginUser.getUsername());
        story.setCreateTime(new Date());
        return storyMapper.insertStory(story);
    }

    /**
     * 修改故事线
     *
     * @param story 故事线
     * @return 结果
     */
    @Override
    public int updateStory(Story story) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        story.setUpdateBy(loginUser.getUsername());
        story.setUpdateTime(new Date());
        StoryBack storyBack = new StoryBack();
        BeanUtils.copyProperties(story, storyBack);
        storyBack.setOriginId(story.getId());
        storyBack.setCreateTime(new Date());
        storyBack.setCreateBy(SecurityUtils.getUsername());
        storyBackService.insertStoryBack(storyBack);
        return storyMapper.updateStory(story);
    }

    /**
     * 批量删除故事线
     *
     * @param ids 需要删除的故事线主键
     * @return 结果
     */
    @Override
    public int deleteStoryByIds(Long[] ids) {
        for (Long id : ids) {
            Story story = selectStoryById(id);
            StoryBack storyBack = new StoryBack();
            BeanUtils.copyProperties(story, storyBack);
            storyBack.setOriginId(id);
            storyBack.setCreateTime(new Date());
            storyBack.setCreateBy(SecurityUtils.getUsername());
            storyBackService.insertStoryBack(storyBack);
        }
        return storyMapper.deleteStoryByIds(ids);
    }

    /**
     * 删除故事线信息
     *
     * @param id 故事线主键
     * @return 结果
     */
    @Override
    public int deleteStoryById(Long id) {
        Story story = selectStoryById(id);
        StoryBack storyBack = new StoryBack();
        BeanUtils.copyProperties(story, storyBack);
        storyBack.setOriginId(id);
        storyBack.setCreateTime(new Date());
        storyBack.setCreateBy(SecurityUtils.getUsername());
        storyBackService.insertStoryBack(storyBack);
        return storyMapper.deleteStoryById(id);
    }
}
