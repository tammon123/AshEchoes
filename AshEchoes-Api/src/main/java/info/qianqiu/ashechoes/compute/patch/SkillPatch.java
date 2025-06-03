package info.qianqiu.ashechoes.compute.patch;

import info.qianqiu.ashechoes.compute.patch.attribute.*;
import info.qianqiu.ashechoes.compute.patch.eqskill.杜望;
import info.qianqiu.ashechoes.compute.patch.extMemoryAttack.*;
import info.qianqiu.ashechoes.compute.patch.characters.*;
import info.qianqiu.ashechoes.compute.patch.skill.*;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.utils.array.ArrayListUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SkillPatch {
    private final InitComputeData init;

    /**
     * 角色开花补丁
     */
    public ArrayList<Skill> chatacterPatchForFlower(ArrayList<Skill> skills, HashSet<Behavior> behaviors, CardVo vo,
                                                    Character c, ArrayList<Character> characters) {
        ArrayList<Skill> r = new ArrayList<>();
        //部分角色异核效果是根据开花数据来判断的
        异核技能等级补丁.patch1(skills, init, characters, vo);
        // 这里的技能是可以进行修改的
        for (Skill s : skills) {
            // 通用角色 花数梳理 不满足花数的剔除
            Character localC = init.getSkillFromCharacter(s.getSkillId());
            int flowers = ArrayListUtils.getCharFlowers(localC.getCharacterId() + "", vo);
            if (s.getName().contains("一花")) {
                if (flowers < 1) {
                    continue;
                }
            } else if (s.getName().contains("三花")) {
                if (flowers < 3) {
                    continue;
                }
            }
            if (s.getName().contains("·特性强化")) {
                Character cca = init.getSkillFromCharacter(s.getSkillId());
                List<Long> list = Arrays.stream(vo.getCharacters().split(",")).map(Long::parseLong).toList();
                int index =
                        ArrayListUtils.getObjectIndexbyId(list, cca.getCharacterId());
                int currentPlevel = Integer.parseInt(vo.getPLevel().split(",")[index]);
                if (currentPlevel < 5) {
                    continue;
                }
            }
            r.add(s);
        }
        // 特殊处理风晴雪技能
        if (c.getCharacterId() == 7005L) {
            int flowers = Integer.parseInt(vo.getFlower().split(",")[vo.getCurrentCharIndex()]);
            角色风晴雪补丁.patch1(flowers, r);
        }
        //如果是红玉三花，那么删除1技能18%独立效果，添加到3花技能上
        if (c.getCharacterId() == 3005L) {
            int flowers = Integer.parseInt(vo.getFlower().split(",")[vo.getCurrentCharIndex()]);
            角色红玉补丁.patch1(flowers, r);
        }
        if (c.getCharacterId() == 6009L) {
            int flowers = Integer.parseInt(vo.getFlower().split(",")[vo.getCurrentCharIndex()]);
            角色景补丁.patch1(flowers, r, behaviors, c, vo);
        }
        if (c.getCharacterId() == 6008L) {
            int flowers = Integer.parseInt(vo.getFlower().split(",")[vo.getCurrentCharIndex()]);
            角色乐无异补丁.patch1(flowers, r, vo);
        }
        if (c.getCharacterId() == 5007L) {
            角色尤尼补丁.patch1(r, c);
        }
        // 特殊处理，需要给其他角色BUFF
        if (vo.getCharacters().contains("2008")) {
            角色珑补丁.patch1(r, vo, c, init);
        }
        // 特殊处理，需要给其他角色BUFF
        if (vo.getCharacters().contains("7006")) {
            角色拉波补丁.patch1(r,characters);
        }
        // 特殊处理，需要给其他角色BUFF
        if (c.getCharacterId() == 2009L) {
            // 屏蔽狙击
            角色茜茜补丁.patch1(r);
        }

        return r;
    }

    /**
     * 潜像
     *
     * @param effectSkill
     * @param excludeSkill
     * @param behaviors
     * @param characters
     * @param mem
     * @param vo
     */
    public void eqSkillPatch(ArrayList<Skill> effectSkill, HashSet<Skill> excludeSkill,
                             HashSet<Behavior> behaviors, ArrayList<Character> characters, Character
                                     mem, CardVo vo) {
        杜望.patch1(effectSkill, behaviors, mem, vo);
    }

    /**
     * 技能效果补丁
     */
    public void skillPatchForSpcial(ArrayList<Skill> effectSkill, HashSet<Skill> excludeSkill,
                                    HashSet<Behavior> behaviors, ArrayList<Character> characters, Character
                                            mem, CardVo vo) {
        敌方基础防御补丁.patch1(effectSkill, vo);
        // 属性加成相关技能补丁置顶
        蚀爆数值补丁.patch1(effectSkill, mem, vo);
        小技能奉献补丁.patch1(effectSkill, mem);
        // 如果前端设定一定暴击
        暴击100补丁.patch1(effectSkill, vo);
        基础角色暴击补丁.patch1(effectSkill, mem);
        终端增幅补丁.patch1(effectSkill, vo);
        生命颂歌补丁.patch1(effectSkill, vo);
        消融补丁.patch1(effectSkill, behaviors);
        水属性角色补丁.patch1(effectSkill, mem, behaviors);

        // 技能相关补丁
        联动不同职业补丁.patch1(effectSkill, mem, characters);

        联动相同属性补丁.patch1(effectSkill, mem, characters);

        同名技能补丁.patch1(effectSkill);

        烙痕残照补丁.patch1(effectSkill, excludeSkill, behaviors, mem, vo, init);

        烙痕谎言之下补丁.patch1(effectSkill, mem);

        烙痕无罪之徒补丁.patch1(effectSkill, characters, mem, init);

        烙痕千灯无间补丁.patch1(effectSkill, characters, vo, init);

        烙痕浊雨补丁.patch1(effectSkill, vo);

        小技能侵蚀补丁.patch1(effectSkill, characters);

        小技能庇护共鸣补丁.patch1(effectSkill, characters);

        烙痕野风补丁.patch1(effectSkill, vo);
        eqSkillPatch(effectSkill, excludeSkill, behaviors, characters, mem, vo);
        //属性冲突相关的计算放在最后面
        烙痕魇境边界补丁.patch1(effectSkill, behaviors, mem, vo);

    }

    /**
     * 排除部分特殊的技能情况，比如攻击方式以及是否为召唤,是否为特定攻击模式
     *
     * @param cis
     * @param oSkills
     * @param behaviors
     * @return
     */
    public ArrayList<Skill> excludeSpecialBehaviorSkill(CharacterInfoSkill cis, List<Skill> oSkills,
                                                        HashSet<Behavior> behaviors) {
        ArrayList<Skill> skills = new ArrayList<>();
        for (Skill origin : oSkills) {
            Skill s = new Skill();
            BeanUtils.copyProperties(origin, s);
            // 白名单乘区的技能直接添加
            int damage = Integer.parseInt(s.getDamage());
            // 属性增加 以及 敌方debuff 是白名单
            if (damage == 101) {
                skills.add(s);
                continue;
            }
            if (origin.getBehavior().equals("0")) {
                skills.add(s);
                continue;
            }
//            if (damage >= 201 && damage <= 304) {
//                skills.add(s);
//                continue;
//            }
            // 处理召唤相关的计算
            if (s.getBehavior().contains("1051")) {
                // 该技能需要召唤物
                if (s.getBehavior().contains("|") && !cis.getBehavior().contains("1051")) {
                    for (String bid : s.getBehavior().split("\\|")) {
                        if (behaviors.contains(init.getBehavior(Long.parseLong(bid)))) {
                            skills.add(s);
                            break;
                        }
                    }
                }
                if (!cis.getBehavior().contains("1051")) {
                    continue;
                }
            }
            // 1011- 1019 是技能模式，批量判断一下 该次攻击是否满足技能要求的行为
            boolean flag = true;
            for (long behavior = 1011; behavior <= 1019; behavior++) {
                if (s.getBehavior().contains(behavior + "")) {
                    if (s.getBehavior().contains("|")) {
                        for (String bid : s.getBehavior().split("\\|")) {
                            if (cis.getBehavior().contains(bid)) {
                                flag = true;
                                break;
                            } else {
                                flag = false;
                            }
                        }
                    } else {
                        if (!cis.getBehavior().contains(behavior + "")) {
                            flag = false;
                        }
                    }
                }
            }
            if (flag) {
                skills.add(s);
            }
        }
        return skills;
    }

    //地方属性弱点计算
    public void enemyAttributeFilter(CharacterInfoSkill cis, CardVo vo) {
        // 判断属性弱点 并 赋值属性弱点
        String drd = "0";
        for (String attr : vo.getDrd().split(",")) {
            String[] drr = attr.split("=");
            if (drr.length == 2 && drr[0].equals(cis.getElement())) {
                drd = drr[1];
                break;
            }
        }
        vo.setDrd(drd);
        String dkx = "0";
        for (String attr : vo.getDkx().split(",")) {
            String[] drr = attr.split("=");
            if (drr.length == 2 && drr[0].equals(cis.getElement())) {
                dkx = drr[1];
                break;
            }
        }
        vo.setDkx(dkx);

    }

    public List<CharacterInfoSkill> extMemoryAttackAdd(List<CharacterInfoSkill> characterInfoSkills,
                                                       List<Character> chars, HashSet<Behavior> behaviors1,
                                                       CardVo vo1, Character cc) {

        String charBehaviors = init.getCharBehaviors(cc.getCharacterId(), vo1.getCurrentCharIndex() == 0);
        // 是否为变身状态
        boolean buff1018 = charBehaviors.contains("1018");
        ArrayList<CharacterInfoSkill> r = new ArrayList<>(characterInfoSkills);
        if (vo1.getMemorys().contains("3024")) {
            if (!r.isEmpty() && characterInfoSkills.getFirst().getType().contains("主动")) {
                r = 神迹_附加攻击补丁.patch(characterInfoSkills, chars, behaviors1, vo1, buff1018);
            }
        }
        if (vo1.getMemorys().contains("1024")) {
            List<String> list =
                    Arrays.stream(vo1.getSkillLevel().split(",")).filter(e -> e.contains("110240")).toList();
            if (!list.isEmpty() && cc.getElement().equals("风") && !r.isEmpty() &&
                    characterInfoSkills.getFirst().getType().contains("主动")) {
                r = 须臾浮生_附加攻击补丁.patch(characterInfoSkills, chars, cc, list.getFirst().split("_")[1],
                        buff1018);
            }
        }

        if (vo1.getMemorys().contains("4025")) {
            List<String> list =
                    Arrays.stream(vo1.getSkillLevel().split(",")).filter(e -> e.contains("140250")).toList();
            if (!list.isEmpty() && !r.isEmpty() &&
                    characterInfoSkills.getFirst().getType().contains("主动")) {
                r = 形与神_附加攻击补丁.patch(characterInfoSkills, cc, list.getFirst().split("_")[1], buff1018);
            }
        }
        if (vo1.getMemorys().contains("2024")) {
            if (cc.getShape().equals("三角")) {
                List<String> list =
                        Arrays.stream(vo1.getSkillLevel().split(",")).filter(e -> e.contains("120240")).toList();
                if (!list.isEmpty() && !r.isEmpty() &&
                        characterInfoSkills.getFirst().getType().contains("主动")) {
                    r = 泅游_附加攻击补丁.patch(characterInfoSkills, cc, list.getFirst().split("_")[1], buff1018);
                }
            }
        }
        if (vo1.getMemorys().contains("3026")) {
            if (cc.getShape().equals("方块")) {
                List<String> list =
                        Arrays.stream(vo1.getSkillLevel().split(",")).filter(e -> e.contains("130260")).toList();
                if (!list.isEmpty() && !r.isEmpty() &&
                        characterInfoSkills.getFirst().getType().contains("主动")) {
                    r = 新醅_附加攻击补丁.patch(characterInfoSkills, behaviors1, cc, list.getFirst().split("_")[1],
                            buff1018);
                }
            }
        }
        if (vo1.getMemorys().contains("5023")) {
            if (cc.getRole().equals("游徒")) {
                List<String> list =
                        Arrays.stream(vo1.getSkillLevel().split(",")).filter(e -> e.contains("150230")).toList();
                if (!list.isEmpty() && !r.isEmpty() &&
                        characterInfoSkills.getFirst().getType().contains("主动")) {
                    r = 欢声萦回_附加攻击补丁.patch(characterInfoSkills, cc, list.getFirst().split("_")[1], buff1018);
                }
            }
        }

        return r;
    }
}
