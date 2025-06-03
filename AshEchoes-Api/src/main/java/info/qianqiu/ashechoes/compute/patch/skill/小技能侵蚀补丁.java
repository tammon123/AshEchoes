package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.List;

public class 小技能侵蚀补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, ArrayList<Character> characters) {
        // 侵蚀是菱形就能触发，肯定在这个已有的里面
        List<Skill> qs = effectSkill.stream().filter(e -> e.getSkillId() == 110071L).toList();
        if (!qs.isEmpty()) {
            // 如果没有蚀队角色，就判断失败
            List<Character> li = characters.stream().filter(e -> e.getElement().equals("蚀")).toList();
            Skill first = qs.getFirst();
            if (li.isEmpty()) {
                //如果没有蚀元素的角色，判断不应该生效
                first.setMinValue("0");
                first.setMaxValue("0");
            }
        }
    }
}
