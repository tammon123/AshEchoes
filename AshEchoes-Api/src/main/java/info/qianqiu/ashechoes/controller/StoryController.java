package info.qianqiu.ashechoes.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import info.qianqiu.ashechoes.dto.domain.Story;
import info.qianqiu.ashechoes.dto.service.StoryService;
import info.qianqiu.ashechoes.utils.http.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 故事线 剧情
 */
@RestController
@RequestMapping("/story")
@RequiredArgsConstructor
@Slf4j
public class StoryController {

    private final StoryService storyService;

    @GetMapping("/list")
    public R storyList(Story story) {
        List<Story> list = storyService.list(new LambdaQueryWrapper<Story>()
                .select(Story::getId, Story::getSort, Story::getTitle, Story::getType, Story::getSTime, Story::getChars,
                        Story::getWorld, Story::getAuthor, Story::getDesc)
                .in(Story::getType, Arrays.asList(story.getType().split(",")))
                .eq(Story::getParentId, 0L)
                .orderByAsc(Story::getSort));
        return R.ok(list);
    }

    @GetMapping("/id/{id}")
    public R storyList(@PathVariable("id") Long id, Story story) {
        Story r = storyService.getStory(id, story);
        return R.ok(r);
    }

}
