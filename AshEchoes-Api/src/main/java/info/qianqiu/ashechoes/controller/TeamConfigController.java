package info.qianqiu.ashechoes.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import info.qianqiu.ashechoes.dto.domain.Cards;
import info.qianqiu.ashechoes.dto.domain.CardsComputeTemplate;
import info.qianqiu.ashechoes.dto.service.CardsComputeTemplateService;
import info.qianqiu.ashechoes.dto.service.CardsService;
import info.qianqiu.ashechoes.utils.http.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 配队相关的接口 配队
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class TeamConfigController {

    private final CardsService cardsService;
    private final CardsComputeTemplateService cardsComputeTemplateService;

    @GetMapping("cards/page/{page}/{size}")
    public R cardPage(@PathVariable("page") Integer page, @PathVariable("size") Integer size, Cards cards) {
        if (page == null) {
            page = 1;
        }
        if (size == null || size >= 6) {
            size = 6;
        }
        return cardsService.cardPage(page, size, cards);
    }

    @GetMapping("cards/share/{id}")
    public R getCardsShareData(@PathVariable("id") Long id,String uid) {
        return cardsService.getCardsShareData(id, uid);
    }

    @GetMapping("cards/share/compute/{id}")
    public R getCardsShareComputeData(@PathVariable("id") Long id) {
        return cardsService.getCardsShareComputeData(id);
    }

    @GetMapping("cards/delete/{id}/{uid}")
    public R deleteCard(@PathVariable("id") Long id, @PathVariable("uid") String uid) {
        return cardsService.deleteCard(id, uid);
    }

    @GetMapping("cards/like/{id}/{uid}")
    public R likeCard(@PathVariable("id") Long id, @PathVariable("uid") String uid) {
        return cardsService.likeCard(id, uid);
    }

    @PostMapping("cards/update")
    public R cardUpdate(@RequestBody Cards cards) {
        if (cards.getCardId() == null) {
            return R.fail("配队ID为空");
        }
        return cardsService.postCards(cards, true);
    }

    @PostMapping("cards/post")
    public R cardPost(@RequestBody Cards cards) {
        // 如果是收藏
        String uid = cards.getUid();
        if (cards.getStatus() == 2 && cards.getCardId() != null) {
            return cardsService.scCards(cards);
        }
        R r = cardsService.postCards(cards, false);
        if (cards.getStatus() == 2) {
            cards.setUid(uid);
            cardsService.scCards(cards);
        }
        cards.setUid(null);
        return r;
    }

    @PostMapping("cards/compute/template")
    public R addComputeTemplate(@RequestBody CardsComputeTemplate template) {
        long count = cardsComputeTemplateService.count(
                new LambdaQueryWrapper<CardsComputeTemplate>().eq(CardsComputeTemplate::getUid, template.getUid()));
        if (count >= 50) {
            return R.fail("总模板记录已经添加50条了，请删除后再添加");
        }
        count = cardsComputeTemplateService.count(
                new LambdaQueryWrapper<CardsComputeTemplate>()
                        .eq(CardsComputeTemplate::getUid, template.getUid())
                        .eq(CardsComputeTemplate::getTitle, template.getTitle())
                        .eq(CardsComputeTemplate::getType, template.getType())
        );
        if (count != 0) {
            return R.fail("同类型下禁止名称重复");
        }
        template.setCreateTime(new Date());
        cardsComputeTemplateService.save(template);
        return R.ok();
    }

    @GetMapping("cards/compute/template/all/{uid}")
    public R getAllComputeTemplate(@PathVariable("uid") String uid) {
        List<CardsComputeTemplate> list =
                cardsComputeTemplateService.list(new LambdaQueryWrapper<CardsComputeTemplate>()
                        .select(CardsComputeTemplate::getCcId, CardsComputeTemplate::getTitle,
                                CardsComputeTemplate::getType, CardsComputeTemplate::getParam,
                                CardsComputeTemplate::getUse)
                        .eq(CardsComputeTemplate::getUid, uid).orderByDesc(CardsComputeTemplate::getCreateTime));
        Map<Byte, List<CardsComputeTemplate>> collect =
                list.stream().collect(Collectors.groupingBy(CardsComputeTemplate::getType));
        HashMap<Byte, Map<String, CardsComputeTemplate>> result = new HashMap<>();

        for (Byte type : collect.keySet()) {
            List<CardsComputeTemplate> cardsComputeTemplates = collect.get(type);
            Map<String, List<CardsComputeTemplate>> collect1 =
                    cardsComputeTemplates.stream().collect(Collectors.groupingBy(CardsComputeTemplate::getTitle));
            HashMap<String, CardsComputeTemplate> mmap = new HashMap<>();
            for (String title : collect1.keySet()) {
                mmap.put(title, collect1.get(title).getFirst());
            }
            result.put(type, mmap);
        }


        return R.ok(result);
    }

    @GetMapping("cards/compute/template/id/{id}")
    public R getComputeTemplate(@PathVariable("id") Long uid) {
        CardsComputeTemplate byId = cardsComputeTemplateService.getById(uid);
        return byId != null ? R.ok(byId) : R.fail();
    }

    @GetMapping("cards/compute/template/delete/{uid}/{type}/{title}")
    public R deleteComputeTemplate(@PathVariable("uid") String uid, @PathVariable("type") Long type,
                                   @PathVariable("title") String title) {
        boolean remove = cardsComputeTemplateService.remove(new LambdaQueryWrapper<CardsComputeTemplate>()
                .eq(CardsComputeTemplate::getType, type).eq(CardsComputeTemplate::getTitle, title)
                .eq(CardsComputeTemplate::getUid, uid));
        return remove ? R.ok() : R.fail();
    }

    @GetMapping("cards/compute/template/use/{uid}/{type}/{title}")
    public R useComputeTemplate(@PathVariable("uid") String uid, @PathVariable("type") Long type,
                                @PathVariable("title") String title) {
        long count = cardsComputeTemplateService.count(new LambdaQueryWrapper<CardsComputeTemplate>()
                .eq(CardsComputeTemplate::getUse, 1)
                .eq(CardsComputeTemplate::getUid, uid).eq(CardsComputeTemplate::getTitle, title)
                .eq(CardsComputeTemplate::getType, type));
        if (count != 0) {
            return R.ok();
        }
        boolean remove = cardsComputeTemplateService.update(new LambdaUpdateWrapper<CardsComputeTemplate>()
                .set(CardsComputeTemplate::getUse, 0)
                .eq(CardsComputeTemplate::getUid, uid)
                .eq(CardsComputeTemplate::getType, type));
        if ("预设".equals(title)) {
            return R.ok();
        }
        cardsComputeTemplateService.update(new LambdaUpdateWrapper<CardsComputeTemplate>()
                .set(CardsComputeTemplate::getUse, 1)
                .eq(CardsComputeTemplate::getTitle, title)
                .eq(CardsComputeTemplate::getUid, uid)
                .eq(CardsComputeTemplate::getType, type));
        return remove ? R.ok() : R.fail();
    }


}
