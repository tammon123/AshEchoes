package info.qianqiu.ashechoes.dto.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import info.qianqiu.ashechoes.dto.domain.Story;
import info.qianqiu.ashechoes.dto.mapper.StoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 伤害乘区Service业务层处理
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Service
@RequiredArgsConstructor
public class StoryService extends ServiceImpl<StoryMapper, Story> {
    private final StoryMapper storyMapper;

    public Story getStory(Long id, Story story) {
        Story one = getById(id);
        List<Story> list1 = list(new LambdaQueryWrapper<Story>().eq(Story::getParentId, id).orderByAsc(Story::getSort));
        one.setChildren(list1);
        ArrayList<Story> list =
                storyMapper.getStoryFilterSort(one.getSort(), Arrays.asList(story.getType().split(",")));
        int current = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(id)) {
                current = i;
                break;
            }
        }
        if (list.size() == 3) {
            one.setLastId(list.get(0).getId());
            one.setNextId(list.get(2).getId());
        }
        if (list.size() == 2) {
            if (current == 0) {
                one.setNextId(list.getLast().getId());
            } else {
                one.setLastId(list.getFirst().getId());
            }
        }

        return one;
    }
}
