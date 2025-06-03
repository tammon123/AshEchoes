package info.qianqiu.ashechoes.compute.patch.characters;

import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;

public class 角色红玉补丁 {
    public static void patch1(int flowers, ArrayList<Skill> r) {
        if (flowers >= 3) {
            for (Skill s: r) {
                //1技能
                if (s.getSkillId()==330051L) {
                    s.setMaxValue("0");
                    s.setMinValue("0");
                    break;
                }
            }
        }
    }
}
