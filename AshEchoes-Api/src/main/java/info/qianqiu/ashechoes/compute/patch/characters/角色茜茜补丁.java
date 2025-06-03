package info.qianqiu.ashechoes.compute.patch.characters;

import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;

public class 角色茜茜补丁 {

    public static void patch1(ArrayList<Skill> r) {
        for (Skill s: r) {
            if (s.getSkillId().equals(130050L) || s.getSkillId().equals(130053L) ) {
                s.setMaxValue("0");
                s.setMinValue("0");
            }
        }
    }
}
