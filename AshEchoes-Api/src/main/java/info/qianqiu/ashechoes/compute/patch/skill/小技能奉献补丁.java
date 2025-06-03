package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.List;

public class 小技能奉献补丁 {
    public static void patch1(ArrayList<Skill> effectSkill,
                              Character mem) {
        // 侵蚀是菱形就能触发，肯定在这个已有的里面
        List<Skill> qs = effectSkill.stream().filter(e -> e.getSkillId() == 110052L).toList();
        if (!qs.isEmpty()) {
            if (mem.getShape().equals("菱形"))
                effectSkill.add(Skill.builder().skillId(1100520L).name("奉献·暴击减少").damage("211")
                        .minValue("-0.025")
                        .maxValue("-0.025").level((byte) 1).desc("").behavior("0").allowShape("菱形").maxEnv("0")
                        .scope((byte) 0)
                        .maxElement("0")
                        .maxBehavior("0").allowElement("0").build());
        }
    }
}
