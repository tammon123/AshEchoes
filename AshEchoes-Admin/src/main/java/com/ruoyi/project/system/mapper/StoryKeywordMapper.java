package com.ruoyi.project.system.mapper;

import java.util.List;
import com.ruoyi.project.system.domain.StoryKeyword;

/**
 * 故事关键字Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
public interface StoryKeywordMapper 
{
    /**
     * 查询故事关键字
     * 
     * @param id 故事关键字主键
     * @return 故事关键字
     */
    public StoryKeyword selectStoryKeywordById(Long id);

    /**
     * 查询故事关键字列表
     * 
     * @param storyKeyword 故事关键字
     * @return 故事关键字集合
     */
    public List<StoryKeyword> selectStoryKeywordList(StoryKeyword storyKeyword);

    /**
     * 新增故事关键字
     * 
     * @param storyKeyword 故事关键字
     * @return 结果
     */
    public int insertStoryKeyword(StoryKeyword storyKeyword);

    /**
     * 修改故事关键字
     * 
     * @param storyKeyword 故事关键字
     * @return 结果
     */
    public int updateStoryKeyword(StoryKeyword storyKeyword);

    /**
     * 删除故事关键字
     * 
     * @param id 故事关键字主键
     * @return 结果
     */
    public int deleteStoryKeywordById(Long id);

    /**
     * 批量删除故事关键字
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteStoryKeywordByIds(Long[] ids);
}
