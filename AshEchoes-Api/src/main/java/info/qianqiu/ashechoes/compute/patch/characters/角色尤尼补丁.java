package info.qianqiu.ashechoes.compute.patch.characters;

import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.List;

public class 角色尤尼补丁 {

    public static void patch1(ArrayList<Skill> r, Character c) {
        if (c.getName().equals("尤尼")) {
            List<Skill> list = r.stream().filter(e -> e.getSkillId().equals(350073L)).toList();
            if(!list.isEmpty()) {
                list.getFirst().setMinValue("0");
                list.getFirst().setMaxValue("0");
            }
        }
    }
}
