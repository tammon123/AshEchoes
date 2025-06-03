package com.ruoyi.project.system.controller;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.framework.aspectj.lang.annotation.Log;
import com.ruoyi.framework.aspectj.lang.enums.BusinessType;
import com.ruoyi.project.system.domain.StoryKeyword;
import com.ruoyi.project.system.service.IStoryKeywordService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 故事关键字Controller
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
@RestController
@RequestMapping("/system/keyword")
public class StoryKeywordController extends BaseController
{
    @Autowired
    private IStoryKeywordService storyKeywordService;

    /**
     * 查询故事关键字列表
     */
    @PreAuthorize("@ss.hasPermi('system:keyword:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoryKeyword storyKeyword)
    {
        startPage();
        List<StoryKeyword> list = storyKeywordService.selectStoryKeywordList(storyKeyword);
        return getDataTable(list);
    }

    /**
     * 导出故事关键字列表
     */
    @PreAuthorize("@ss.hasPermi('system:keyword:export')")
    @Log(title = "故事关键字", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoryKeyword storyKeyword)
    {
        List<StoryKeyword> list = storyKeywordService.selectStoryKeywordList(storyKeyword);
        ExcelUtil<StoryKeyword> util = new ExcelUtil<StoryKeyword>(StoryKeyword.class);
        util.exportExcel(response, list, "故事关键字数据");
    }

    /**
     * 获取故事关键字详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:keyword:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(storyKeywordService.selectStoryKeywordById(id));
    }

    /**
     * 新增故事关键字
     */
    @PreAuthorize("@ss.hasPermi('system:keyword:add')")
    @Log(title = "故事关键字", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoryKeyword storyKeyword)
    {
        return toAjax(storyKeywordService.insertStoryKeyword(storyKeyword));
    }

    /**
     * 修改故事关键字
     */
    @PreAuthorize("@ss.hasPermi('system:keyword:edit')")
    @Log(title = "故事关键字", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoryKeyword storyKeyword)
    {
        return toAjax(storyKeywordService.updateStoryKeyword(storyKeyword));
    }

    /**
     * 删除故事关键字
     */
    @PreAuthorize("@ss.hasPermi('system:keyword:remove')")
    @Log(title = "故事关键字", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storyKeywordService.deleteStoryKeywordByIds(ids));
    }
}
