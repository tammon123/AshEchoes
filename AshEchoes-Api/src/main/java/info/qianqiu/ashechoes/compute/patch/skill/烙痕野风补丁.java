package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class 烙痕野风补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, CardVo vo) {
        //获取防御值
        String def = vo.getAttribute().split(",")[1];
        String def1 = vo.getEattribute().split(",")[1];
        int i = new BigDecimal(def).add(new BigDecimal(def1)).add(new BigDecimal(100)).intValue();
        // 判断当前角色是否可以触发无罪，判断触发效果是否有效
        List<Skill> list =
                effectSkill.stream().filter(e -> e.getSkillId() == 120200L).toList();
        if (list.isEmpty()) {
            return;
        }
        Skill e = list.getFirst();
        BigDecimal total = new BigDecimal(e.getMinValue()).multiply(
                        new BigDecimal(i).divide(new BigDecimal(100), 3, RoundingMode.DOWN))
                .divide(new BigDecimal(1), 3, RoundingMode.DOWN);
        e.setMinValue(total.toPlainString());
        e.setMaxValue(total.toPlainString());
    }
}
