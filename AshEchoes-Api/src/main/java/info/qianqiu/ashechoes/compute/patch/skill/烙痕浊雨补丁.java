package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class 烙痕浊雨补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, CardVo vo) {
        // 判断当前角色是否可以触发无罪，判断触发效果是否有效
        List<Skill> list =
                effectSkill.stream().filter(e -> e.getSkillId() == 130230L).toList();
        if (list.isEmpty()) {
            return;
        }
        Skill s = list.getFirst();
        int i = new BigDecimal(100).add(new BigDecimal(vo.getAttribute().split(",")[4]))
                .add(new BigDecimal(vo.getEattribute().split(",")[4])).toBigInteger().intValue();
        if (i <= 1000) {
            s.setMinValue("0");
            s.setMaxValue("0");
            return;
        }
        int max = 90;

        int i1 = new BigDecimal(i).subtract(new BigDecimal(1000)).multiply(new BigDecimal(s.getMinValue()))
                .toBigInteger().intValue();

        if (i1 > max) {
            i1 = max;
        }

        int count = 5;

        s.setMinValue(i1 * count + "");
        s.setMaxValue(i1 * count + "");

    }
}
