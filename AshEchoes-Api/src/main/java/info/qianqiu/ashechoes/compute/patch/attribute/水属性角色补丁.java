package info.qianqiu.ashechoes.compute.patch.attribute;

import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class 水属性角色补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, Character mem, HashSet<Behavior> behaviors) {
        Set<Long> behaviorIds = behaviors.stream().map(Behavior::getBehaviorId).collect(Collectors.toSet());
        // 如果是水元素同调者，则触发水元素易伤
        if (behaviorIds.contains(2001L) && "水".equals(mem.getElement())) {
            effectSkill.add(Skill.builder().skillId(1000001L).name("水地板·伤害加成").damage("304")
                    .minValue("0.1").maxValue("0.1")
                    .allowElement("水").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte)0)
                    .scopeNum((byte) 8)
                    .maxBehavior("0").maxEnv("0").maxElement("0").build());
        }
    }
}
