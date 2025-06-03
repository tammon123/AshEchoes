package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class 联动不同职业补丁 {

    public static void patch1(ArrayList<Skill> effectSkill, Character mem, ArrayList<Character> chars) {
        List<Skill> list = effectSkill.stream().filter(skill -> skill.getMaxBehavior().contains("1061")).toList();
        for (Skill skill : list) {
            String val = skill.getMinValue();
            List<Character> charss;
            if (chars.size() >= 4) {
                charss = chars.subList(0, 4);
            } else {
                charss = chars.subList(0, chars.size());
            }
            Set<String> collect = charss.stream().map(Character::getRole).collect(Collectors.toSet());
            val = new BigDecimal(val).multiply(new BigDecimal(collect.size())).toPlainString();
            skill.setMinValue(val);
            skill.setMaxValue(val);
        }
    }
}
