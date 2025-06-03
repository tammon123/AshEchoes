package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.List;

public class 小技能庇护共鸣补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, ArrayList<Character> characters) {
        // 侵蚀是菱形就能触发，肯定在这个已有的里面
        List<Skill> qs = effectSkill.stream().filter(e -> e.getSkillId() == 130261L).toList();
        if (!qs.isEmpty()) {
            // 如果没有蚀队角色，就判断失败
            List<Character> qc =
                    characters.stream().filter(e -> e.getRole().equals("轻卫") || e.getRole().equals("铁御")).toList();
            Skill first = qs.getFirst();
            if (qc.isEmpty()) {
                //如果没有蚀元素的角色，判断不应该生效
                first.setMinValue("0");
                first.setMaxValue("0");
            } else if (qc.size() == 1) {
                first.setMaxValue(first.getMinValue());
            }
        }
    }
}
