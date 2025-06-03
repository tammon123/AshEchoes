package com.ruoyi.project.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.system.mapper.StoryKeywordMapper;
import com.ruoyi.project.system.domain.StoryKeyword;
import com.ruoyi.project.system.service.IStoryKeywordService;

/**
 * 故事关键字Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
@Service
public class StoryKeywordServiceImpl implements IStoryKeywordService 
{
    @Autowired
    private StoryKeywordMapper storyKeywordMapper;

    /**
     * 查询故事关键字
     * 
     * @param id 故事关键字主键
     * @return 故事关键字
     */
    @Override
    public StoryKeyword selectStoryKeywordById(Long id)
    {
        return storyKeywordMapper.selectStoryKeywordById(id);
    }

    /**
     * 查询故事关键字列表
     * 
     * @param storyKeyword 故事关键字
     * @return 故事关键字
     */
    @Override
    public List<StoryKeyword> selectStoryKeywordList(StoryKeyword storyKeyword)
    {
        return storyKeywordMapper.selectStoryKeywordList(storyKeyword);
    }

    /**
     * 新增故事关键字
     * 
     * @param storyKeyword 故事关键字
     * @return 结果
     */
    @Override
    public int insertStoryKeyword(StoryKeyword storyKeyword)
    {
        return storyKeywordMapper.insertStoryKeyword(storyKeyword);
    }

    /**
     * 修改故事关键字
     * 
     * @param storyKeyword 故事关键字
     * @return 结果
     */
    @Override
    public int updateStoryKeyword(StoryKeyword storyKeyword)
    {
        return storyKeywordMapper.updateStoryKeyword(storyKeyword);
    }

    /**
     * 批量删除故事关键字
     * 
     * @param ids 需要删除的故事关键字主键
     * @return 结果
     */
    @Override
    public int deleteStoryKeywordByIds(Long[] ids)
    {
        return storyKeywordMapper.deleteStoryKeywordByIds(ids);
    }

    /**
     * 删除故事关键字信息
     * 
     * @param id 故事关键字主键
     * @return 结果
     */
    @Override
    public int deleteStoryKeywordById(Long id)
    {
        return storyKeywordMapper.deleteStoryKeywordById(id);
    }
}
