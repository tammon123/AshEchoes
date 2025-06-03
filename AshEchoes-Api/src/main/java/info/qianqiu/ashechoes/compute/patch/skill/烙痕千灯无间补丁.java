package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.init.InitComputeData;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class 烙痕千灯无间补丁 {
    public static void patch1(ArrayList<Skill> effectSkill,
                              ArrayList<Character> characters, CardVo vo, InitComputeData init) {
        // 判断当前角色是否可以触发无罪，判断触发效果是否有效
        List<Skill> list =
                effectSkill.stream().filter(e -> e.getSkillId() == 150210L || e.getSkillId() == 150211L).toList();
        if (list.isEmpty()) {
            return;
        }

        Set<Character> chars = characters.stream().filter(e -> e.getShape().equals("菱形")).collect(Collectors.toSet());

        for (Skill s : list) {
            if (s.getSkillId().equals(150210L)) {
                boolean flag = false;
                for (Character c : chars) {
                    String behids = init.getCharBehaviors(c.getCharacterId(),
                            vo.getCharacters().indexOf(c.getCharacterId() + "") == 0);
                    if (!behids.contains("1071")) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    s.setMaxValue("0");
                    s.setMinValue("0");
                }
            }
            if (s.getSkillId().equals(150211L)) {
                boolean flag = false;
                for (Character c : chars) {
                    String behids = init.getCharBehaviors(c.getCharacterId(),
                            vo.getCharacters().indexOf(c.getCharacterId() + "") == 0);
                    if (behids.contains("1022") && !behids.contains("1071")) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    s.setMaxValue("0");
                    s.setMinValue("0");
                }
            }
        }
    }
}
