package info.qianqiu.ashechoes.compute.patch.attribute;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class 终端增幅补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, CardVo vo) {
        if (vo.getZf()) {
            int c1 = Integer.parseInt(vo.getAttribute().split(",")[4]);
            int c2 = Integer.parseInt(vo.getEattribute().split(",")[4]);
            String val = "0.65";
            if ((c1 + c2 + 100) >= 1000) {
                val = "1.25";
            }
            effectSkill.add(Skill.builder().skillId(100000L).name("终端增幅").damage("214")
                    .minValue(val).maxValue(val)
                    .allowElement("0").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte) 0)
                    .scopeNum((byte) 8)
                    .maxBehavior("0").maxEnv("0").maxElement("0").build());
        }

    }
}
