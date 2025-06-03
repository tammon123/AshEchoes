package info.qianqiu.ashechoes.dto.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.dto.domain.*;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.mapper.CardsMapper;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 默认名称Service业务层处理
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Service
@RequiredArgsConstructor
public class CardsService extends ServiceImpl<CardsMapper, Cards> {

    private final InitComputeData init;
    private final CardsUserService cardsUserService;
    private final CardsMapper cardsMapper;
    private final UserLikesService userLikesService;

    public R cardPage(int page, int size, Cards cards) {
        // 如果有用户ID，说明就是查看用户自己的投稿以及收藏
        List<Cards> records;
        if (StringUtils.isNotEmpty(cards.getUid())) {
            int start = (page - 1) * size;
            int end = start + size;
            records = cardsMapper.pageList(start, end, cards);
        } else {
            records = page(new Page<Cards>(page, size), new LambdaQueryWrapper<Cards>()
                    .eq(StringUtils.isNotEmpty(cards.getUid()), Cards::getUid, cards.getUid())
                    .eq(cards.getStatus() != null, Cards::getStatus, cards.getStatus())
                    .like(Cards::getCharNames, cards.getKeyWord())
                    .orderByDesc(cards.getAgree(), Cards::getLikes)
                    .orderByDesc(Cards::getSort)
                    .orderByDesc(Cards::getCreateTime)).getRecords();
        }

        filterCardsPageData(records, cards.getUid());
        return R.ok(records);
    }

    private void filterCardsPageData(List<Cards> records, String uid) {
        for (Cards c : records) {
            ArrayList<String> charIcons = new ArrayList<>();
            if (StringUtils.isNotEmpty(c.getSpskills())) {
                c.setCharNames(c.getCharNames().replaceAll("，", ",").trim());
                int count = 0;
                for (String name : c.getCharNames().split(",")) {
                    count++;
                    if (count > 5) break;
                    charIcons.add(init.getCharacterAvatar(name));
                }
            }
            if (c.getUid().equals(uid)) {
                c.setModify(true);
            }
            c.setCharIcons(charIcons);
            c.setUid(null);
        }
    }

    public R getCardsShareData(Long id, String uid) {
        Cards byId = getById(id);
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Skill> skills = new ArrayList<>();
        ArrayList<Memory> memorys = new ArrayList<>();
        byId.setCharNames(byId.getCharNames().replaceAll("，", ","));
        byId.setSpskills(byId.getSpskills().replaceAll("，", ","));
        byId.setSp2skills(byId.getSp2skills().replaceAll("，", ","));
        byId.setMemoryNames(byId.getMemoryNames().replaceAll("，", ","));
        for (String s : byId.getCharNames().split(",")) {
            characters.add(init.getSimpleCharacterByNameGroup(s));
        }
        for (String s : byId.getSpskills().split(",")) {
            skills.add(init.getSimpleSkillByNameGroup(s).getFirst());
        }
        for (String s : byId.getMemoryNames().split(",")) {
            memorys.add(init.getSimpleMemoryByNameGroup(s));
        }
        ArrayList<ArrayList<Skill>> chareqs = new ArrayList<>();
        for (String s : byId.getSp2skills().split(",")) {
            ArrayList<Skill> ts = new ArrayList<>();
            for (String ss: s.split("-")) {
                long l = Long.parseLong(ss.split("_")[0]);
                if (l != 0) {
                    ts.add(init.getSkill(l));
                }else {
                    ts.add(Skill.builder().skillId(0L).name("").icons("").build());
                }
            }
            chareqs.add(ts);
        }
        byId.setChareqs(chareqs);
        byId.setChars(characters);
        byId.setSkills(skills);
        byId.setMemories(memorys);
        byId.setUid(null);
        byId.setCharNames(null);
        byId.setMemoryNames(null);
        byId.setSpskills(null);
        byId.setSp2skills(null);
        long type = 1;
        long count = userLikesService.count(
                new LambdaQueryWrapper<UserLikes>().eq(UserLikes::getType, type).eq(UserLikes::getAid, id)
                        .eq(UserLikes::getUid, uid));
        byId.setLikes(count);
        return R.ok(byId);
    }

    public R deleteCard(Long id, String uid) {
        remove(new LambdaQueryWrapper<Cards>()
                .eq(Cards::getCardId, id)
                .eq(Cards::getUid, uid));
        cardsUserService.remove(new LambdaQueryWrapper<CardsUser>()
                .eq(CardsUser::getCardsId, id)
                .eq(CardsUser::getUid, uid));
        return R.ok();
    }

    public R getCardsShareComputeData(Long id) {
        Cards byId = getById(id);
        if (byId == null) {
            return R.fail("当前分享ID错误~");
        }
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Memory> memorys = new ArrayList<>();
        byId.setCharNames(byId.getCharNames().replaceAll("，", ","));
        byId.setSpskills(byId.getSpskills().replaceAll("，", ","));
        byId.setMemoryNames(byId.getMemoryNames().replaceAll("，", ","));
        for (String s : byId.getCharNames().split(",")) {
            characters.add(init.getSimpleCharacterByNameGroup(s));
        }
        for (String s : byId.getMemoryNames().split(",")) {
            memorys.add(init.getMemory(init.getSimpleMemoryByNameGroup(s).getMemoryId()));
        }
        ArrayList<ArrayList<Skill>> chareqs = new ArrayList<>();
        for (String s : byId.getSp2skills().split(",")) {
            ArrayList<Skill> ts = new ArrayList<>();
            for (String ss: s.split("-")) {
                long l = Long.parseLong(ss.split("_")[0]);
                if (l != 0) {
                    ts.add(init.getSkill(l));
                }else {
                    ts.add(Skill.builder().skillId(0L).name("").icons("").build());
                }
            }
            chareqs.add(ts);
        }
        byId.setChareqs(chareqs);
        byId.setChars(characters);
        byId.setMemories(memorys);
        byId.setUid(null);
        byId.setCharNames(null);
        byId.setMemoryNames(null);
        byId.setSp2skills(null);
        byId.setCardId(null);
        return R.ok(byId);
    }

    public R postCards(Cards cards, boolean update) {
        if (StringUtils.isEmpty(cards.getTitle())) {
            return R.fail("标题不允许为空");
        }
        if (cards.getTitle().length() >= 11) {
            return R.fail("标题不允许超过10个字");
        }
        if (cards.getNickname().split("-")[0].length() >= 11) {
            return R.fail("投稿人名称不允许超过10个字");
        }
        if (cards.getRemark().length() >= 201) {
            return R.fail("配队说明不允许超过200个字");
        }
        if (StringUtils.isEmpty(cards.getCharNames())) {
            return R.fail("队员不允许为空");
        }
        if (StringUtils.isEmpty(cards.getMemoryNames())) {
            return R.fail("烙痕不允许为空");
        }
        if (StringUtils.isEmpty(cards.getSpskills())) {
            return R.fail("基点技能不允许为空");
        }
        if (StringUtils.isEmpty(cards.getUid())) {
            return R.fail("用户ID不允许为空");
        }
        if (cards.getStatus() == 0) {
            cards.setNickname(
                    cards.getNickname().split("-")[0] + "-" + cards.getUid().substring(cards.getUid().length() - 6));
        }
        boolean post;
        if (update) {
            post = update(new LambdaUpdateWrapper<Cards>()
                    .set(Cards::getTitle, cards.getTitle())
                    .set(Cards::getCharNames, cards.getCharNames())
                    .set(Cards::getMemoryNames, cards.getMemoryNames())
                    .set(Cards::getSpskills, cards.getSpskills())
                    .set(Cards::getSp2skills, cards.getSp2skills())
                    .set(Cards::getRemark, cards.getRemark())
                    .set(Cards::getNickname, cards.getNickname())
                    .set(Cards::getUpdateTime, new Date())
                    .eq(Cards::getCardId, cards.getCardId())
                    .eq(Cards::getUid, cards.getUid()));
        } else {
            cards.setCardId(0L);
            cards.setCreateTime(new Date());
            post = save(cards);
        }
        if (cards.getStatus() == 0 && post && !update) {
            CardsUser cu = new CardsUser();
            cu.setCuId(0L);
            cu.setUid(cards.getUid());
            cu.setCardsId(cards.getCardId());
            cu.setStatus(cards.getStatus());
            cu.setCreateTime(new Date());
            cardsUserService.save(cu);
        }
        cards.setUid(null);
        return post ? R.ok(cards) : R.fail();
    }

    // 收藏
    public R scCards(Cards cards) {
        long count = cardsUserService.count(new LambdaQueryWrapper<CardsUser>()
                .eq(CardsUser::getUid, cards.getUid()).eq(CardsUser::getCardsId, cards.getCardId()));
        if (count != 0) {
            return R.fail("已经收藏这个配队了~");
        }
        CardsUser cu = new CardsUser();
        cu.setCuId(0L);
        cu.setUid(cards.getUid());
        cu.setCardsId(cards.getCardId());
        cu.setStatus(cards.getStatus());
        cu.setCreateTime(new Date());
        boolean save = cardsUserService.save(cu);
        return save ? R.ok(cards) : R.fail();
    }

    /**
     * 点赞配队type为1
     * 点赞了就+1 取消点赞就-1
     *
     * @param id
     * @param uid
     * @return
     */
    public R likeCard(Long id, String uid) {
        long type = 1;
        LambdaQueryWrapper<UserLikes> userLikeQuery =
                new LambdaQueryWrapper<UserLikes>().eq(UserLikes::getType, type).eq(UserLikes::getAid, id)
                        .eq(UserLikes::getUid, uid);
        long count = userLikesService.count(userLikeQuery);
        long likes = 0;
        if (count == 0) {
            likes = 1;
            userLikesService.save(new UserLikes(0L, uid, id, type, new Date()));
        } else {
            likes = -1;
            userLikesService.remove(userLikeQuery);
        }
        // 锁一下更新
        synchronized (this) {
            Cards one = getOne(new LambdaQueryWrapper<Cards>().select(Cards::getLikes).eq(Cards::getCardId, id));
            likes += one.getLikes();
            if (likes < 0) {
                likes = 0;
            }
            update(new LambdaUpdateWrapper<Cards>().set(Cards::getLikes, likes).eq(Cards::getCardId, id));
        }
        return R.ok();
    }
}
