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
import com.ruoyi.project.system.domain.StoryCreation;
import com.ruoyi.project.system.service.IStoryCreationService;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.web.page.TableDataInfo;

/**
 * 故事二创Controller
 * 
 * @author ruoyi
 * @date 2025-02-11
 */
@RestController
@RequestMapping("/system/creation")
public class StoryCreationController extends BaseController
{
    @Autowired
    private IStoryCreationService storyCreationService;

    /**
     * 查询故事二创列表
     */
    @PreAuthorize("@ss.hasPermi('system:creation:list')")
    @GetMapping("/list")
    public TableDataInfo list(StoryCreation storyCreation)
    {
        startPage();
        List<StoryCreation> list = storyCreationService.selectStoryCreationList(storyCreation);
        return getDataTable(list);
    }

    /**
     * 导出故事二创列表
     */
    @PreAuthorize("@ss.hasPermi('system:creation:export')")
    @Log(title = "故事二创", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, StoryCreation storyCreation)
    {
        List<StoryCreation> list = storyCreationService.selectStoryCreationList(storyCreation);
        ExcelUtil<StoryCreation> util = new ExcelUtil<StoryCreation>(StoryCreation.class);
        util.exportExcel(response, list, "故事二创数据");
    }

    /**
     * 获取故事二创详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:creation:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(storyCreationService.selectStoryCreationById(id));
    }

    /**
     * 新增故事二创
     */
    @PreAuthorize("@ss.hasPermi('system:creation:add')")
    @Log(title = "故事二创", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody StoryCreation storyCreation)
    {
        return toAjax(storyCreationService.insertStoryCreation(storyCreation));
    }

    /**
     * 修改故事二创
     */
    @PreAuthorize("@ss.hasPermi('system:creation:edit')")
    @Log(title = "故事二创", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody StoryCreation storyCreation)
    {
        return toAjax(storyCreationService.updateStoryCreation(storyCreation));
    }

    /**
     * 删除故事二创
     */
    @PreAuthorize("@ss.hasPermi('system:creation:remove')")
    @Log(title = "故事二创", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(storyCreationService.deleteStoryCreationByIds(ids));
    }
}
