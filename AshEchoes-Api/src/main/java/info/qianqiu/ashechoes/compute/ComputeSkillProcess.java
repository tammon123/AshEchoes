package info.qianqiu.ashechoes.compute;

import info.qianqiu.ashechoes.compute.patch.SkillPatch;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.utils.array.ArrayListUtils;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ComputeSkillProcess {

    private final InitComputeData init;
    private final ComputeElementBehaviorAndEffect computeElementBehaviorAndEffect;
    private final SkillPatch skillPatch;

    /**
     * @param characters
     * @param vo
     * @param member     将要计算的成员
     * @return
     */
    ArrayList<Skill> initSkill(ArrayList<Character> characters, HashSet<Behavior> behaviors, CardVo vo,
                               Character member) {

        ArrayList<Skill> skills = new ArrayList<>();
        // 默认这里只记录全员生效的小技能
        for (Character character : characters) {
            character.setSkills(init.getCharacterSelfSkill(character.getCharacterId()));
        }
        // 这里添加记录生效的小技能 排除0级的 多个小技能以最高的为准
        for (String sk : vo.getSkillLevel().split(",")) {
            String[] sc = sk.split("_");
            if (!"0".equals(sc[1])) {
                Skill skill = init.getSkill(Long.parseLong(sc[0]));
                skill.setLevel(Byte.valueOf(sc[1]));
                if (skills.contains(skill)) {
                    for (int i = 0; i < skills.size(); i++) {
                        Skill oskill = skills.get(i);
                        if (oskill.getSkillId().equals(skill.getSkillId())) {
                            if (oskill.getLevel() < skill.getLevel()) {
                                skills.set(i, skill);
                                break;
                            }
                        }
                    }
                } else {
                    skills.add(skill);
                }
            }
        }
        HashSet<Skill> tempSkills = new HashSet<>();
        for (Skill s : skills) {
            tempSkills.add(s);
            List<Skill> sg = init.getSimpleSkillByNameGroup(s.getName());
            if (sg.size() > 1) {
                for (int i = 1; i < sg.size(); i++) {
                    Skill skill = sg.get(i);
                    skill.setLevel(s.getLevel());
                    vo.setSkillLevel(vo.getSkillLevel() + "," + skill.getSkillId() + "_" + s.getLevel());
                    tempSkills.add(skill);
                }
            }
        }
        skills = new ArrayList<>(tempSkills);
        // 计算潜能效果
        // 准备排除重复的技能
        ArrayList<Skill> norepeatSkills = new ArrayList<>(initPotentialSkill(characters, vo));
        // 防止技能重复
        for (Skill s : skills) {
            List<Long> list = norepeatSkills.stream().map(Skill::getSkillId).toList();
            // 添加到技能列表中，排除重复的
            if (!"9999".equals(s.getBehavior()) && !"0".equals(s.getDamage()) &&
                    StringUtils.isNotEmpty(s.getDamage())) {
                if (!list.contains(s.getSkillId())) {
                    norepeatSkills.add(s);
                }
            }
        }
        // 这里添加映像
        String sp2skillLevel = vo.getSp2skillLevel();
        if (StringUtils.isNotEmpty(sp2skillLevel) && sp2skillLevel.contains(",")) {
            String[] sp2Skills = sp2skillLevel.split(",")[vo.getCurrentCharIndex()].split("-");
            for (String sl : sp2Skills) {
                try {
                    long sid = Long.parseLong(sl.split("_")[0]);
                    if (sid != 0) {
                        norepeatSkills.add(init.getSkill((sid)));
                    }
                } catch (Exception e) {
                    log.error("获取潜像错误：{},{}", e.getMessage(), sl);
                }
            }
        }
        // 这个仅仅是为了初始化元素行为
        List<Skill> behaviorTempSkill = new ArrayList<>();
        for (Character c : characters) {
            behaviorTempSkill.addAll(c.getSkills());
        }
        for (Skill s : norepeatSkills) {
            if (!behaviorTempSkill.contains(s)) {
                behaviorTempSkill.add(s);
            }
        }
        // 初始化技能附带行为 需要提前判断角色是否满足技能加成行为的前置条件
        for (Skill s : behaviorTempSkill) {
            if (judgeSkillEffect(s, behaviors, member)) {
                String bids = s.getAddBehavior();
                if (StringUtils.isNotEmpty(bids)) {
                    Arrays.stream(bids.split(",")).forEach(e -> {
                        behaviors.add(init.getBehavior(Long.parseLong(e)));
                    });
                }
            }
        }
        // 添加神迹系列的烙痕攻击属性
        ArrayList<CharacterInfoSkill> tempCharSkills = new ArrayList<>();
        tempCharSkills.add(CharacterInfoSkill.builder().type("主动").build());
        List<CharacterInfoSkill> characterInfoSkills =
                skillPatch.extMemoryAttackAdd(tempCharSkills, characters, behaviors, vo, member);
        for (CharacterInfoSkill skill: characterInfoSkills) {
            if (StringUtils.isNotEmpty(skill.getElement())) {
                Long elementBehavior = init.getElementBehavior(skill.getElement());
                behaviors.add(init.getBehavior(elementBehavior));
            }
        }
        //所有行为真正初始化完成 重新计算一下高级元素反应
        computeElementBehaviorAndEffect.elementEffectBehaviorInit(behaviors.stream().
                        map(Behavior::getBehaviorId).
                        toList(),
                behaviors, vo, member);
        // 能用的小技能
        HashSet<Skill> effectSkill = new HashSet<>();
        HashSet<Skill> excludeSkill = new HashSet<>();

        // 确认生效的小技能
        // 初次判断技能列表的技能对该角色是否生效
        packSkillFirst(behaviors, member, effectSkill, excludeSkill, norepeatSkills);
        // 从排除的技能里面找到scope为1的技能
        packSkillSecond(effectSkill, excludeSkill, behaviors, characters);
        // 确认同调者的小技能是否对当前角色有效
        packSkillCharacter(effectSkill, behaviors, characters, member);

        ArrayList<Skill> realSkill = packSkillFinal(effectSkill, vo);

        //处理一下开花/角色逻辑，删除没有达到的开花效果
        ArrayList<Skill> rSkills = skillPatch.chatacterPatchForFlower(realSkill, behaviors, vo, member,characters);
        // 这里单独处理一些特殊的技能
        skillPatch.skillPatchForSpcial(rSkills, excludeSkill, behaviors, characters, member, vo);

        return rSkills;
    }

    /**
     * 第一次简单清洗技能
     * 找出其中符合行为、元素、形状的小技能
     *
     * @param behaviors
     * @param character
     * @param effectSkill
     * @param excludeSkill
     * @param skills
     */
    private void packSkillFirst(HashSet<Behavior> behaviors, Character character, HashSet<Skill> effectSkill,
                                HashSet<Skill> excludeSkill, ArrayList<Skill> skills) {
        for (Skill s : skills) {
            boolean flag = judgeSkillEffect(s, behaviors, character);
            if (flag) {
                effectSkill.add(s);
            } else {
                excludeSkill.add(s);
            }
        }
    }

    /**
     * 生效范围
     * 上一步中未生效的技能，判断其乘区，scope=1 全队生效的技能
     *
     * @param effectSkill
     * @param excludeSkill
     */
    private void packSkillSecond(
            HashSet<Skill> effectSkill,
            HashSet<Skill> excludeSkill,
            HashSet<Behavior> behaviors,
            ArrayList<Character> characters) {
        ArrayList<Skill> effectIds = new ArrayList<>();
        for (Skill s : excludeSkill) {
            if (s.getScope() != 1) {
                continue;
            }
            boolean sFlag = true;
            // 只要标注了是全局buff，就参与重新计算
            //添加队员的行为  判断包括自己在内的队友是否能触发这种buff类型的技能
            boolean leader = true;
            for (Character c : characters) {
                List<Long> behavior =
                        new ArrayList<>(behaviors.stream().map(Behavior::getBehaviorId).sorted().toList());
                // 高级反应 加上 同调者自己的行为
                String behids = init.getCharBehaviors(c.getCharacterId(), leader);
                leader = false;
                if (StringUtils.isNotEmpty(behids)) {
                    for (Long id : Arrays.stream(behids.split(",")).map(Long::parseLong).toList()) {
                        if (!behavior.contains(id)) {
                            behavior.add(id);
                        }
                    }
                }
                HashSet<Behavior> behaviors1 = new HashSet<>();
                // 是否生效
                for (Long l : behavior) {
                    behaviors1.add(init.getBehavior(l));
                }
                boolean effect = judgeSkillEffect(s, behaviors1, c);

                if (effect) {
                    //该技能已经生效，直接推出即可
                    sFlag = true;
                    break;
                } else {
                    sFlag = false;
                }
            }
            if (!sFlag) {
                continue;
            }
            // 对该角色来说，因为第一步技能就过滤了无法自主生效的技能，这一步里面也应该过滤以下，只生效BUFF以及DEBUFF技能
            effectIds.add(s);
            effectSkill.add(s);
        }

        for (Skill effectId : effectIds) {
            excludeSkill.remove(effectId);
        }
    }

    /**
     * 处理同调者的buff技能
     * 注意 这里只计算前四名队员的DPS
     *
     * @param effectSkill
     * @param behaviors
     * @param characters
     * @param cc
     */
    private void packSkillCharacter(HashSet<Skill> effectSkill, HashSet<Behavior> behaviors,
                                    ArrayList<Character> characters, Character cc) {

        // 取出所有角色的技能，让每个角色去判断是否能生效
        List<Skill> skills = new ArrayList<>();
        for (Character c : characters) {
            skills.addAll(c.getSkills());
        }
        for (int i = 0; i < characters.size(); i++) {
            Character character = characters.get(i);

            Set<Long> behIds = behaviors.stream().map(Behavior::getBehaviorId).collect(Collectors.toSet());
            // 添加当前角色的所有行为
            String s1 = init.getCharBehaviors(character.getCharacterId(), i == 0);
            if (StringUtils.isNotEmpty(s1)) {
                behIds.addAll(Arrays.stream(s1.split(","))
                        .map(Long::parseLong).toList());
            }

            // 便利所有能对全场生效的技能
            for (Skill s : skills) {
                // 当前技能来源角色
                boolean begFlag = true;
                // 只需要判断行为，元素、形状是肯定能过的
                //本角色的技能直接过
                if (s.getBehavior().contains(",")) {
                    List<Long> needBids =
                            Arrays.stream(s.getBehavior().split(",")).map(Long::parseLong).toList();
                    if (!behIds.containsAll(needBids)) {
                        begFlag = false;
                    }
                } else if (s.getBehavior().contains("|")) {
                    List<Long> needBids =
                            Arrays.stream(s.getBehavior().split("\\|")).map(Long::parseLong).toList();
                    boolean flag = false;
                    for (Long id : needBids) {
                        if (behIds.contains(id)) {
                            flag = true;
                            break;
                        }
                    }
                    if (!flag) {
                        begFlag = false;
                    }
                } else {
                    boolean flag = true;
                    if (!behIds.contains(Long.parseLong(s.getBehavior()))) {
                        flag = false;
                    }
                    if (s.getBehavior().equals("0")) {
                        flag = true;
                    }

                    if (!flag) {
                        begFlag = false;
                    }
                }
                if (!"0".equals(s.getAllowShape())) {
                    if (!s.getAllowShape().contains(character.getShape())) {
                        begFlag = false;
                    }
                }
                if (!"0".equals(s.getAllowElement())) {
                    List<String> ae = List.of(character.getAttackElement().split(","));
                    boolean tempFlag = false;
                    for (String ss : ae) {
                        if (s.getAllowElement().contains(ss)) {
                            tempFlag = true;
                            break;
                        }
                    }
                    if (!tempFlag) {
                        begFlag = false;
                    }
                }

                if (s.getScope() == 0) {
                    // 非自己角色，一定触发不了的
                    if (!Objects.equals(init.getSkillFromCharacter(s.getSkillId()).getCharacterId(),
                            cc.getCharacterId())) {
                        begFlag = false;
                    }
                }
                // 如果没有通过校验，直接下一个技能
                if (!begFlag) {
                    continue;
                }
                List<Long> list = effectSkill.stream().map(Skill::getSkillId).toList();
                if (!list.contains(s.getSkillId())) {
                    effectSkill.add(s);
                }
            }
        }

    }

    /**
     * 最终清洗打包技能，
     * 这里只需要显示技能最终生效等级的数值结果即可
     *
     * @param effectSkill
     * @param vo
     * @return
     */
    private ArrayList<Skill> packSkillFinal(HashSet<Skill> effectSkill, CardVo vo) {
        ArrayList<Skill> r = new ArrayList<>();
        // 手动选择的技能等级
        HashMap<Long, Byte> sl = new HashMap<>();
        if (StringUtils.isNotEmpty(vo.getSkillLevel())) {
            for (String s : vo.getSkillLevel().split(",")) {
                sl.put(Long.valueOf(s.split("_")[0]), Byte.valueOf(s.split("_")[1]));
            }
        }
        try {
            if (StringUtils.isNotEmpty(vo.getSp2skillLevel())) {
                String split = vo.getSp2skillLevel().split(",")[vo.getCurrentCharIndex()];
                for (String s : split.split("-")) {
                    sl.put(Long.valueOf(s.split("_")[0]), Byte.valueOf(s.split("_")[1]));
                }
            }
        } catch (Exception ignore) {
        }

        for (Skill s : effectSkill) {
            if (StringUtils.isEmpty(s.getMaxValue())) {
                s.setMaxValue(s.getMinValue());
            }
            String minValue = s.getMinValue();
            String maxValue = s.getMaxValue();
            // 如果最小值伤害没有逗号，或者为0，那么就是单纯的三级技能，或者角色自己的技能
            if (!minValue.contains(",") && !"0".equals(minValue)) {
                r.add(s);
            } else {
                byte level = s.getLevel();
                if (s.getSkillId() <= 300000) {
                    try {
                        level = sl.get(s.getSkillId());
                    }catch (Exception e){
                        log.info("{}", e.getMessage());
                        e.printStackTrace();
                    }
                }
                if (level != 0) {
                    String[] minSplit = minValue.split(",");
                    if (level > minSplit.length) {
                        level = (byte) minSplit.length;
                    }
                    minValue = minSplit[level - 1];
                    String[] maxSplit = maxValue.split(",");
                    maxValue = maxSplit[level - 1];
                    // 有的时候偷懒，MAX可能0
                    if (StringUtils.isEmpty(maxValue) || "0".equals(maxValue)) {
                        maxValue = minValue;
                    }
                    s.setMinValue(minValue);
                    s.setMaxValue(maxValue);
                    // 如果三级直接取最大值
                    if (level >= 3) {
                        if (level == 3) {
                            s.setMinValue(minSplit[minSplit.length - 1]);
                            s.setMaxValue(maxSplit[maxSplit.length - 1]);
                        } else {
                            Byte charSkillIndex = s.getCharSkillIndex();
                            Character cca = init.getSkillFromCharacter(s.getSkillId());
                            int charFlowers = ArrayListUtils.getCharFlowers(cca.getCharacterId() + "", vo);
                            if (charFlowers == 5) {
                                s.setMinValue(minSplit[minSplit.length - 1]);
                                s.setMaxValue(maxSplit[maxSplit.length - 1]);
                            } else if (charFlowers == 4) {
                                if (charSkillIndex <= 1) {
                                    s.setMinValue(minSplit[minSplit.length - 1]);
                                    s.setMaxValue(maxSplit[maxSplit.length - 1]);
                                } else {
                                    s.setMinValue(minSplit[minSplit.length - 4]);
                                    s.setMaxValue(maxSplit[maxSplit.length - 4]);
                                }
                            } else {
                                s.setMinValue(minSplit[minSplit.length - 4]);
                                s.setMaxValue(maxSplit[maxSplit.length - 4]);
                            }
                        }
                    }
                    if (!"0".equals(minValue) || !"0".equals(maxValue)) {
                        r.add(s);
                    }
                }
            }
        }

        return r;
    }


    /**
     * 初始化潜能技能
     *
     * @param characters
     * @param vo
     * @return
     */
    private ArrayList<Skill> initPotentialSkill(ArrayList<Character> characters,
                                                CardVo vo) {

        ArrayList<Skill> skills = new ArrayList<>();
        Character leader = characters.getFirst();
        int leaderPlevel = Integer.parseInt(vo.getPLevel().split(",")[0]);
        int currentPlevel = Integer.parseInt(vo.getPLevel().split(",")[vo.getCurrentCharIndex()]);
        // L9潜能
        if (leaderPlevel >= 9) {
            skills.add(Skill.builder().skillId(400003L).name("潜能·L9·" + leader.getElement()).damage("105")
                    .minValue("0.5")
                    .level((byte) 1).desc("").behavior("0").allowElement("0").allowShape("0").maxValue("")
                    .maxBehavior("0").maxEnv("0").maxElement("0").scope((byte) 0)
                    .build());
        }
        // l4潜能
        if (leaderPlevel >= 4) {
            skills.add(Skill.builder().skillId(400001L).name("潜能·L4·" + leader.getElement()).damage("105")
                    .minValue("0.6")
                    .level((byte) 1).desc("").behavior("0").allowElement("0").allowShape("0").maxValue("")
                    .maxBehavior("0").maxEnv("0").maxElement("0").scope((byte) 0)
                    .build());
        }
        // L6 加成暴击率
        if (currentPlevel >= 6) {
            skills.add(Skill.builder().skillId(400004L).name("潜能·L6·" + leader.getElement()).damage("211")
                    .minValue("0.05")
                    .level((byte) 1).desc("").behavior("0").allowElement("0").allowShape("0").maxValue("")
                    .maxBehavior("0").maxEnv("0").maxElement("0").scope((byte) 0)
                    .build());
        }
        // l7潜能
        if (leaderPlevel >= 7) {
            if ("风".equals(leader.getElement())) {
                skills.add(Skill.builder().skillId(400002L).name("潜能·L7·" + leader.getElement()).damage("205")
                        .minValue("0.25")
                        .allowElement("风").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte) 1)
                        .scopeNum((byte) 8)
                        .maxValue("").maxBehavior("0").maxEnv("0").maxElement("0").build());
            } else if ("物理".equals(leader.getElement())) {
                skills.add(Skill.builder().skillId(400002L).name("潜能·L7·" + leader.getElement()).damage("304")
                        .minValue("1")
                        .allowElement("物理").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte) 1)
                        .maxValue("").maxBehavior("0").maxEnv("0").maxElement("0").build());
            } else if (!"蚀".equals(leader.getElement())) {
                skills.add(Skill.builder().skillId(400002L).name("潜能·L7·" + leader.getElement()).damage("305")
                        .minValue("0.1")
                        .maxValue("0.1").level((byte) 1).desc("").behavior("0").allowShape("0").maxEnv("0")
                        .scope((byte) 0)
                        .maxElement(leader.getElement())
                        .maxBehavior("0").allowElement(leader.getElement()).build());
                skills.add(Skill.builder().skillId(400012L).name("潜能·L7·" + leader.getElement()).damage("303")
                        .minValue("0.4")
                        .maxValue("0.4").level((byte) 1).desc("").behavior("2027").allowShape("0").maxEnv("0")
                        .scope((byte) 1)
                        .maxElement("0")
                        .maxBehavior("0").allowElement("水,雷,炎,霜,风,蚀").build());
            }
        }

        return skills;
    }


    /**
     * 判断技能是否生效
     *
     * @param s
     * @param behaviors
     * @param character
     * @return
     */
    private boolean judgeSkillEffect(Skill s, HashSet<Behavior> behaviors, Character character) {
        boolean flag = true;
        List<Long> behaviorIds = new ArrayList<>(behaviors.stream().map(Behavior::getBehaviorId).toList());
        // 同调者附带技能
        if (!"0".equals(s.getBehavior())) {
            if (s.getBehavior().contains(",")) {
                String[] split = s.getBehavior().split(",");
                for (String b : split) {
                    if (!behaviorIds.contains(Long.valueOf(b))) {
                        flag = false;
                        break;
                    }
                }
            } else if (s.getBehavior().contains("|")) {
                String[] split = s.getBehavior().split("\\|");
                for (String b : split) {
                    if (behaviorIds.contains(Long.valueOf(b))) {
                        flag = true;
                        break;
                    } else {
                        flag = false;
                    }
                }
            } else {
                if (!behaviorIds.contains(Long.parseLong(s.getBehavior()))) {
                    flag = false;
                }
            }
        }

        if (!flag) {
            return flag;
        }

        if (!"0".equals(s.getAllowShape())) {
            if (!s.getAllowShape().contains(character.getShape())) {
                flag = false;
                return flag;
            }
        }
        if (!"0".equals(s.getAllowElement())) {
            if (character.getAttackElement() == null) {
                flag = false;
            } else {
                List<String> ae = List.of(character.getAttackElement().split(","));
                flag = false;
                for (String ss : ae) {
                    if (s.getAllowElement().contains(ss)) {
                        flag = true;
                        break;
                    }
                }
            }
        }

        return flag;
    }

}
