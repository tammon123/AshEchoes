package info.qianqiu.ashechoes.compute.patch.attribute;

import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;

public class 基础角色暴击补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, Character character) {
        //        一般情况下，尖锋和游徒的初始暴击率为15%，暴击伤害为150%；
//        其他职业的初始暴击率为5%，暴击伤害为130%。
        String val11 = "0.05";
        String val22 = "0.3";
        if ("尖锋".equals(character.getRole()) || "游徒".equals(character.getRole())) {
            val11 = "0.15";
            val22 = "0.5";
        }
        effectSkill.add(Skill.builder().skillId(100000L).name(character.getName()+"·暴击率").damage("211")
                .minValue(val11).maxValue(val11)
                .allowElement("0").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte)0)
                .scopeNum((byte) 8)
                .maxBehavior("0").maxEnv("0").maxElement("0").build());
        effectSkill.add(Skill.builder().skillId(100000L).name(character.getName()+"·暴击伤害").damage("209")
                .minValue(val22).maxValue(val22)
                .allowElement("0").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte)0)
                .scopeNum((byte) 8)
                .maxBehavior("0").maxEnv("0").maxElement("0").build());
    }
}
