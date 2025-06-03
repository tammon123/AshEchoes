package info.qianqiu.ashechoes.compute;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;
import info.qianqiu.ashechoes.dto.domain.Memory;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ComputeElementBehaviorAndEffect {

    private final InitComputeData init;

    /**
     * 初始化高级元素反应
     *
     * @param characters
     * @param memories
     * @param vo
     * @param collect
     * @return
     */
    HashSet<Behavior> initHignBehavior(ArrayList<Character> characters, ArrayList<Memory> memories, CardVo vo,
                                       Character currentC, HashSet<Behavior> behaviorsInit,
                                       Map<Long, List<CharacterInfoSkill>> collect) {
        HashSet<Behavior> behaviors = new HashSet<>(behaviorsInit);
        // 将所有同调者元素攻击行为拿出来
        for (Character character : characters) {
            if (character.getAttackElement() != null) {
                for (String s : character.getAttackElement().split(",")) {
                    Behavior behavior = new Behavior();
                    behavior.setBehaviorId(init.getElementBehavior(s));
                    behaviors.add(behavior);
                }
            }
        }
        // 将所有角色的技能附带行为也全部拿出来
        for (Long id : collect.keySet()) {
            List<String> cisBeh = collect.get(id).stream().map(CharacterInfoSkill::getBehavior).toList();
            for (String s : cisBeh) {
                if (StringUtils.isNotEmpty(s)) {
                    for (String ss : s.split(",")) {
                        behaviors.add(init.getBehavior(Long.parseLong(ss)));
                    }
                }
            }
        }

        List<Long> allCharacterBehaviorIds = behaviors.stream().map(Behavior::getBehaviorId).toList();
        // 元素反应行为初始化
        // 重置,只保留当前选中角色的行为
        behaviors = new HashSet<>(behaviorsInit);
        elementEffectBehaviorInit(allCharacterBehaviorIds, behaviors, vo, null);
        // 添加角色联动行为
        for (Character c : characters) {
            // 添加芙蕖司危联动行为
            if (c.getCharacterId() == 5006L) {
                // 芙蕖在场
                behaviors.add(init.getBehavior(4102L));
            } else if (c.getCharacterId() == 6007L) {
                // 司危在场
                behaviors.add(init.getBehavior(4101L));
            }
        }
        // 添加烙痕联动行为
        for (Memory m : memories) {
            if (m.getMemoryId() == 3003L) {
                behaviors.add(init.getBehavior(5001L));
            } else if (m.getMemoryId() == 3002L) {
                behaviors.add(init.getBehavior(5002L));
            }
        }
        // 以下为双人激励的判断
        Map<String, List<Character>> cc =
                characters.stream().collect(Collectors.groupingBy(Character::getElement));
        if (cc.size() > 1 && cc.containsKey("蚀")) {
            behaviors.add(init.getBehavior(5003L));
        }
        for (String element : cc.keySet()) {
            int size = cc.get(element).size();
            int plevel = Integer.parseInt(vo.getPLevel().split(",")[0]);
            if ((size >= 2 && "霜".equals(element)) ||
                    (plevel == 9 && characters.getFirst().getElement().equals("霜"))) {
                behaviors.add(init.getBehavior(4001L));
            }
            if ((size >= 2 && "炎".equals(element)) ||
                    (plevel == 9 && characters.getFirst().getElement().equals("炎"))) {
                behaviors.add(init.getBehavior(4002L));
            }
            if ((size >= 2 && "风".equals(element)) ||
                    (plevel == 9 && characters.getFirst().getElement().equals("风"))) {
                behaviors.add(init.getBehavior(4003L));
            }
            if ((size >= 2 && "水".equals(element)) ||
                    (plevel == 9 && characters.getFirst().getElement().equals("水"))) {
                behaviors.add(init.getBehavior(4004L));
            }
            if ((size >= 2 && "蚀".equals(element)) ||
                    (plevel == 9 && characters.getFirst().getElement().equals("蚀"))) {
                behaviors.add(init.getBehavior(4005L));

            }
            if ((size >= 2 && "雷".equals(element)) ||
                    (plevel == 9 && characters.getFirst().getElement().equals("雷"))) {
                behaviors.add(init.getBehavior(4006L));

            }
            if ((size >= 2 && "物理".equals(element)) ||
                    (plevel == 9 && characters.getFirst().getElement().equals("物理"))) {
                behaviors.add(init.getBehavior(4007L));
            }
        }

        return behaviors;
    }

    void elementEffectBehaviorInit(List<Long> behaviorIds, HashSet<Behavior> behaviors, CardVo vo, Character c) {
        // 消融
        if (new HashSet<>(behaviorIds).containsAll(Arrays.asList((2002L), (2006L))) ||
                new HashSet<>(behaviorIds).containsAll(Arrays.asList((2003L), (2004L)))) {
            behaviors.add(init.getBehavior(2021L));
            behaviors.add(init.getBehavior(2027L));
            if (c != null) {
                behaviors.add(init.getBehavior(2031L));
                behaviors.add(init.getBehavior(2037L));
            }
        }
        //汽化
        if (new HashSet<>(behaviorIds).containsAll(Arrays.asList((2002L), (2005L))) ||
                new HashSet<>(behaviorIds).containsAll(Arrays.asList((2001L), (2004L)))) {
            behaviors.add(init.getBehavior(2022L));
            behaviors.add(init.getBehavior(2027L));
            if (c != null) {
                behaviors.add(init.getBehavior(2032L));
                behaviors.add(init.getBehavior(2037L));
            }
        }
        //传导
        if (new HashSet<>(behaviorIds).containsAll(Arrays.asList((2001L), (2009L)))) {
            behaviors.add(init.getBehavior(2023L));
            behaviors.add(init.getBehavior(2027L));
            if (c != null) {
                behaviors.add(init.getBehavior(2033L));
                behaviors.add(init.getBehavior(2037L));
            }
        }
        //电解
        if (new HashSet<>(behaviorIds).containsAll(Arrays.asList((2003L), (2009L)))) {
            behaviors.add(init.getBehavior(2024L));
            behaviors.add(init.getBehavior(2027L));
            if (c != null) {
                behaviors.add(init.getBehavior(2034L));
                behaviors.add(init.getBehavior(2037L));
            }
        }
        //爆燃
        if (new HashSet<>(behaviorIds).containsAll(Arrays.asList((2002L), (2009L)))) {
            behaviors.add(init.getBehavior(2025L));
            behaviors.add(init.getBehavior(2027L));
            if (c != null) {
                behaviors.add(init.getBehavior(2035L));
                behaviors.add(init.getBehavior(2037L));
            }
        }
        //冻结
        if (new HashSet<>(behaviorIds).containsAll(Arrays.asList((2001L), (2006L))) ||
                new HashSet<>(behaviorIds).containsAll(Arrays.asList((2003L), (2005L)))) {
            behaviors.add(init.getBehavior(2026L));
            behaviors.add(init.getBehavior(2027L));
            if (c != null) {
                behaviors.add(init.getBehavior(2036L));
                behaviors.add(init.getBehavior(2037L));
            }
        }
        //以下为地板行为
        //水
        if (behaviorIds.contains(2001L)) {
            behaviors.add(init.getBehavior(2001L));
        }
        //炎
        if (behaviorIds.contains(2002L)) {
            behaviors.add(init.getBehavior(2002L));
        }
        // 产生霜地板条件
        if (new HashSet<>(behaviorIds).containsAll(Arrays.asList((2001L), (2006L))) || ("潮湿".equals(vo.getEnv()) &&
                new HashSet<>(behaviorIds).contains(2006L))) {
            behaviors.add(init.getBehavior(2003L));
        }
    }

}
