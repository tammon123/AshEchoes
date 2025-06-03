package info.qianqiu.ashechoes.compute.patch.attribute;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;

public class 暴击100补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, CardVo vo) {
        if (!vo.getAbj()) {
            return;
        }
        effectSkill.add(Skill.builder().skillId(100000L).name("100%暴击").damage("211")
                .minValue("1").maxValue("1")
                .allowElement("0").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte)0)
                .scopeNum((byte) 8)
                .maxBehavior("0").maxEnv("0").maxElement("0").build());
    }
}
