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
import com.ruoyi.project.system.domain.Story;
import com.ruoyi.project.system.service.IStoryService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 故事线Controller
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
@RestController
@RequestMapping("/system/story")
public class StoryController extends BaseController
{
    @Autowired
    private IStoryService storyService;

    /**
     * 查询故事线列表
     */
    @PreAuthorize("@ss.hasPermi('system:story:list')")
    @GetMapping("/list")
    public TableDataInfo list(Story story)
    {
        startPage();
        List<Story> list = storyService.selectStoryList(story);
        return getDataTable(list);
    }

    /**
     * 导出故事线列表
     */
    @PreAuthorize("@ss.hasPermi('system:story:export')")
    @Log(title = "故事线", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Story story)
    {
        List<Story> list = storyService.selectStoryList(story);
        ExcelUtil<Story> util = new ExcelUtil<Story>(Story.class);
        util.exportExcel(response, list, "故事线数据");
    }

    /**
     * 获取故事线详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:story:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(storyService.selectStoryById(id));
    }

    /**
     * 新增故事线
     */
    @PreAuthorize("@ss.hasPermi('system:story:add')")
    @Log(title = "故事线", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Story story)
    {
        return toAjax(storyService.insertStory(story));
    }

    /**
     * 修改故事线
     */
    @PreAuthorize("@ss.hasPermi('system:story:edit')")
    @Log(title = "故事线", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Story story)
    {
        return toAjax(storyService.updateStory(story));
    }

    /**
     * 删除故事线
     */
    @PreAuthorize("@ss.hasPermi('system:story:remove')")
    @Log(title = "故事线", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storyService.deleteStoryByIds(ids));
    }
}
