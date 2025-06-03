package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.utils.array.ArrayListUtils;
import info.qianqiu.ashechoes.utils.string.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class 烙痕残照补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, HashSet<Skill> excludeSkill, HashSet<Behavior> behaviors,
                              Character mem, CardVo vo, InitComputeData init) {
        //单独处理残照的技能效果 残照肯定没有生效，再这个排除的技能名单种  残照大技能ID 110140
        List<Skill> cz = excludeSkill.stream().filter(e -> e.getSkillId() == 110140L).toList();
        // 力量平衡效果

        // 如果有残照
        if (!cz.isEmpty()) {
            if (!mem.getElement().equals("物理")) {
                Skill skill = cz.getFirst();
                //所有减抗的技能拿出来看看
                List<Skill> list = effectSkill.stream()
                        .filter(e -> e.getDamage().contains("304")
                                && (!e.getMaxElement().equals("0") || !e.getAllowElement().equals("0"))
                                && (e.getAllowShape().equals("方块") || e.getAllowShape().equals("0"))).toList();
                String maxElement = "";
                for (Skill s : list) {
                    String e = s.getAllowElement().equals("0") ? s.getMaxElement() : s.getAllowElement();
                    maxElement += (e + "|");
                }
                String[] charaters = vo.getCharacters().split(",");
                ArrayList<Character> cc = new ArrayList<>();
                for (String id : charaters) {
                    cc.add(init.getCharacter(Long.parseLong(id)));
                }
                String shapes = ArrayListUtils.arrayToString(cc.stream().map(Character::getShape).toList(), ",");
                if (StringUtils.isNotEmpty(maxElement) && shapes.contains(skill.getAllowShape())) {
                    // 计算伤害
                    for (String s : vo.getSkillLevel().split(",")) {
                        if (s.split("_")[0].equals("110140")) {
                            int level = Integer.parseInt(s.split("_")[1]);
                            skill.setMinValue(skill.getMaxValue().split(",")[level - 1]);
                            skill.setMaxValue(skill.getMinValue());
                            break;
                        }
                    }
                    maxElement = maxElement.substring(0, maxElement.length() - 1);
                    skill.setMaxElement(maxElement);
                    // 初始化行为判断为0
                    skill.setBehavior("0");
                    skill.setAllowShape("0");
                    effectSkill.add(skill);
                }
            }
        }
    }
}
