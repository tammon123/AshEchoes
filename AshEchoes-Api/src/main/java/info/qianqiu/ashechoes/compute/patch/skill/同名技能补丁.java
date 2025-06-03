package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class 同名技能补丁 {

    public static void patch1(ArrayList<Skill> effectSkill) {

        // 带两个·的技能一般都是同一个技能的不同形式 这边取最大值 比如 xx·xx·α型
        Map<String, List<Skill>> collect = effectSkill.stream()
                .filter(e -> e.getName().indexOf("·") != e.getName().lastIndexOf("·") && e.getSkillId() <= 200000)
                .collect(Collectors.groupingBy(e -> e.getName().split("·")[0] + "·" + e.getName().split("·")[1]));
        List<Long> ids = new ArrayList<>();
        //将符合规则的技能记录下来 不要的技能
        for (String s : collect.keySet()) {
            if (collect.get(s).size() != 1) {
                List<Skill> list = collect.get(s).stream().sorted(Comparator.comparingInt(Skill::getLevel)
                ).toList();
                ids.addAll(list.subList(0, list.size()-1).stream().map(Skill::getSkillId).toList());
            }
        }
        for (int i = 0; i < effectSkill.size(); i++) {
            if (ids.contains(effectSkill.get(i).getSkillId())) {
                effectSkill.remove(i);
                i--;
            }
        }

    }

}
