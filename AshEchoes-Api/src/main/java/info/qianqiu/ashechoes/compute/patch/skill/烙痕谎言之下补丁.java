package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.List;

public class 烙痕谎言之下补丁 {
    public static void patch1(ArrayList<Skill> effectSkill,
                              Character mem) {
        List<Skill> cz = effectSkill.stream().filter(e -> e.getSkillId() == 130129L).toList();
        if (!cz.isEmpty()) {
            Skill skill = cz.getFirst();
            // 该大技能特殊处理
            if (mem.getZhCount() != null && mem.getZhCount() != 0) {
                byte zhCount = mem.getZhCount();
                String s1 = skill.getMaxValue();
                String s = s1.split("\\.")[1];
                if (Integer.parseInt(s) <= zhCount) {
                    zhCount = Byte.parseByte(s);
                }
                skill.setMinValue("0." + zhCount);
                skill.setMaxValue("0." + zhCount);
            } else {
                skill.setMinValue("0");
                skill.setMaxValue("0");
            }
            skill.setBehavior("0");
            skill.setMaxBehavior("0");
        }
    }
}
