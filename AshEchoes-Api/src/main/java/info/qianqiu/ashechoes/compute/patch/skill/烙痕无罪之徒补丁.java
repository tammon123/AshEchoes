package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.utils.array.ArrayListUtils;
import info.qianqiu.ashechoes.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class 烙痕无罪之徒补丁 {
    public static void patch1(ArrayList<Skill> effectSkill,
                              ArrayList<Character> characters, Character mem, InitComputeData init) {
        // 判断当前角色是否可以触发无罪，判断触发效果是否有效
        List<Skill> list = effectSkill.stream().filter(e -> e.getSkillId() == 130200L).toList();
        if (list.isEmpty()) {
            return;
        }
        Skill skil = list.getFirst();
        //单独如果是130200L 万象侵蚀，无罪之徒-需要判断是否有同调者是菱形并且元素同自身一样，
        if (skil != null) {
            String element = "";
            boolean effect = false;
            for (Character ccc : characters) {
                boolean judge = true;
                // 这个队员是菱形，且可以进行攻击
                if (init.getCharBehaviors(ccc.getCharacterId(), false).contains("1071") ||
                        !skil.getAllowShape().contains(ccc.getShape())) {
                    continue;
                }
                // 还需要判断是否这个菱形的元素跟当前角色一样
                String cccattackElement = ccc.getAttackElement();
                String memattackElement = mem.getAttackElement();
                // 如果其中一方攻击属性为空 就跳过
                if (StringUtils.isEmpty(cccattackElement) || StringUtils.isEmpty(memattackElement)) {
                    continue;
                } else {
                    // 变量两个角色的元素属性，如果有一个相同就是成功
                    for (String c1 : cccattackElement.split(",")) {
                        boolean ttemp = false;
                        for (String c2 : memattackElement.split(",")) {
                            if (c1.trim().equals(c2.trim())) {
                                ttemp = true;
                                judge = true;
                                break;
                            } else {
                                judge = false;
                            }
                        }
                        if (ttemp) {
                            break;
                        }
                    }
                }
                //特殊判断有云无月的蚀元素角色,就能触发
                if ("蚀".equals(mem.getElement())) {
                    List<Long> clist = characters.stream().map(Character::getCharacterId).toList();
                    String s1 = ArrayListUtils.longArrayToString(clist, ",");
                    // 如果有云无月 2005就是云无月的ID
                    if (s1.contains("2005")) {
                        judge = true;
                        element += "蚀,";
                    }
                }
                if (judge) {
                    element += ccc.getAttackElement() + ",";
                    effect = true;
                }
            }
            // 如果实际上不生效，那么就删除这个技能
            if (!effect) {
                skil.setMaxValue("0");
                skil.setMinValue("0");
            } else {
                skil.setMaxElement(element);
            }
        }
    }
}
