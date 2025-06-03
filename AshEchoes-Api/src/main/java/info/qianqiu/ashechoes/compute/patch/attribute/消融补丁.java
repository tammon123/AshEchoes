package info.qianqiu.ashechoes.compute.patch.attribute;

import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class 消融补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, HashSet<Behavior> behaviors) {
        Set<Long> behaviorIds = behaviors.stream().map(Behavior::getBehaviorId).collect(Collectors.toSet());
        // 特殊情况下，比如融甲，当冰地板+火元素，或者火地板+冰元素都会触发融甲
        if (new HashSet<>(behaviorIds).contains(2021L)) {
            effectSkill.add(Skill.builder().skillId(100000L).name("元素反应:消融").damage("301")
                    .minValue("0.6").maxValue("0.6")
                    .allowElement("0").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte) 0)
                    .scopeNum((byte) 8)
                    .maxBehavior("0").maxEnv("0").maxElement("0").build());
        }
    }
}
