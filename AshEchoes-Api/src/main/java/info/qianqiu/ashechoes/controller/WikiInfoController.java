package info.qianqiu.ashechoes.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import info.qianqiu.ashechoes.dto.service.MemoryDirectorService;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.dto.domain.*;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 基础数据
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class WikiInfoController {

    private final InitComputeData init;
    private final MemoryDirectorService memoryDirectorService;

    /**
     * 获取所有的小技能
     *
     * @return
     */
    @GetMapping("/skill/small/all")
    public R skillSmallAll() {
        List<Skill> allSkill =
                init.getAllSkill().stream().filter(e -> (e.getSkillId() < 200000L && e.getLevel() != 3) ||
                        (e.getSkillId() >= 500000L && e.getSkillId() <= 600000L)).toList();
        ArrayList<String> names = new ArrayList<>();
        ArrayList<Skill> skills = new ArrayList<>();
        for (Skill s : allSkill) {
            if (!names.contains(s.getName())) {
                names.add(s.getName());
                skills.add(s);
            }
        }
        return R.ok(skills);
    }

    /**
     * 获取所有角色
     * 或者获取单独的某个角色详情
     *
     * @param ids
     * @return
     */
    @GetMapping("/character/{ids}/{index}")
    public R getCharacter(@PathVariable("ids") String ids, @PathVariable("index") Integer index) {
        if ("simple".equals(ids)) {
            return R.ok(init.getAllSimpleCharavter());
        } else if (ids.split(",").length == 1) {
            Character characters = init.getCharacters(ids.split(","))[0];
            if (index == 0) {
                List<Skill> characterLeaderSkill = init.getCharacterLeaderSkill(characters.getCharacterId());
                if (!characterLeaderSkill.isEmpty()) {
                    Map<String, List<Skill>> collect =
                            characterLeaderSkill.stream().collect(Collectors.groupingBy(Skill::getName));
                    for (String name : collect.keySet()) {
                        List<Skill> skills = collect.get(name);
                        for (Skill s : skills) {
                            s.setHidden(true);
                        }
                        skills.getFirst().setHidden(false);
                    }
                }
                characters.setLeaderSkills(characterLeaderSkill);
            }
            List<Skill> characterSelfSkill = init.getCharacterSelfSkill(characters.getCharacterId()).stream()
                    .filter(e -> StringUtils.isNotEmpty(e.getDamage()) && !"0".equals(e.getDamage())).toList();
            characterSelfSkill.forEach(e -> {
                String descElement = "";
                if ("0".equals(e.getMaxElement())) {
                    if (!"0".equals(e.getAllowElement())) {
                        descElement = e.getAllowElement();
                    }
                } else {
                    descElement = e.getMaxElement();
                }
                e.setScoped(e.getScope() == 0 ? "本人|" + descElement : "全体|" + descElement);
                if (e.getScoped().endsWith("|")) {
                    e.setScoped(e.getScoped().substring(0, e.getScoped().length() - 1));
                }
                // 如果是全体生效，并且只生效一个人，默认给队长
                if (e.getScope() == 1 && e.getScopeNum() == 1) {
                    e.setScoped("仅限队长生效");
                }
                e.setDamaged(init.getDamageInfo(e.getDamage()));
                String vald = ("0".equals(e.getMaxValue()) || StringUtils.isEmpty(e.getMaxValue())) ? e.getMinValue() :
                        e.getMaxValue();
                if ("0".equals(vald) || StringUtils.isEmpty(vald)) {
                    return;
                } else {
                    String[] split = vald.split(",");
                    vald = split[split.length - 1];
                }
                if (vald.contains("_")) {
                    String temp = "";
                    String[] split = vald.split("_");
                    for (String s : split) {
                        String s1 = new BigDecimal(s).multiply(new BigDecimal("100"))
                                .divide(new BigDecimal(1), 1, RoundingMode.DOWN).toPlainString() + "%";
                        temp += s1 + ",";
                    }
                    e.setVald(temp.substring(0, temp.length() - 1));
                } else {
                    e.setVald(new BigDecimal(vald).multiply(new BigDecimal("100")).setScale(0).toPlainString() + "%");
                }
            });
            characters.setSelfSkills(characterSelfSkill);
            ArrayList<Character> characters1 = new ArrayList<>();
            characters1.add(characters);
            return R.ok(characters1);
        }
        return R.ok(init.getCharacters(ids.split(",")));
    }

    /**
     * 获取所有烙痕
     * 或者获取
     *
     * @param ids
     * @return
     */
    @GetMapping("/memory/{ids}")
    public R getMemory(@PathVariable("ids") String ids) {
        if ("all".equals(ids)) {
            return R.ok(init.getAllMemory().stream().collect(Collectors.groupingBy(Memory::getCategory)));
        } else if ("simple".equals(ids)) {
            Collection<Memory> values = init.getAllMemory();
            ArrayList<Memory> memories = new ArrayList<>();
            for (Memory m : values) {
                Memory simple = new Memory();
                BeanUtils.copyProperties(m, simple);
                simple.setSkills(null);
                memories.add(simple);
            }
            return R.ok(memories.stream().collect(Collectors.groupingBy(Memory::getCategory)));
        }
        return R.ok(init.getMemorys(ids.split(",")));
    }

    /**
     * 致谢列表
     *
     * @return
     */
    @GetMapping("/thanks")
    public R thanks() {
        return R.ok(init.getAllThanks());
    }

    @GetMapping("/memory/director/{uid}")
    public R mineMemoryDirectorData(@PathVariable Long uid) {
        return R.ok(memoryDirectorService.list(new LambdaQueryWrapper<MemoryDirector>()
                .select(MemoryDirector::getCount, MemoryDirector::getMemoryId)
                .eq(MemoryDirector::getUid, uid)));
    }


    @PostMapping("/memory/director")
    public R updateMemoryDirector(@RequestBody MemoryDirector memoryDirector) {
        if (memoryDirector.getMemoryId() == null || memoryDirector.getUid() == null ||
                memoryDirector.getCount() == null) {
            return R.fail("参数缺失");
        }
        if (memoryDirector.getCount() < 0) {
            memoryDirector.setCount(0L);
        }
        if (memoryDirector.getCount() > 6) {
            memoryDirector.setCount(6L);
        }
        long count = memoryDirectorService.count(
                new LambdaQueryWrapper<MemoryDirector>().eq(MemoryDirector::getMemoryId, memoryDirector.getMemoryId())
                        .eq(MemoryDirector::getUid, memoryDirector.getUid()));
        if (count != 0) {
            memoryDirectorService.update(
                    new LambdaUpdateWrapper<MemoryDirector>()
                            .set(MemoryDirector::getCount, memoryDirector.getCount())
                            .eq(MemoryDirector::getUid, memoryDirector.getUid())
                            .eq(MemoryDirector::getMemoryId, memoryDirector.getMemoryId()));
        } else {
            memoryDirectorService.save(memoryDirector);
        }
        return R.ok();
    }

}
