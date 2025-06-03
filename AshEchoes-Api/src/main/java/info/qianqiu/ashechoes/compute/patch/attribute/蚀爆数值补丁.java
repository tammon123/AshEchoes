package info.qianqiu.ashechoes.compute.patch.attribute;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class 蚀爆数值补丁 {

    public static void patch1(ArrayList<Skill> effectSkill,
                              Character mem, CardVo vo) {
        if ("蚀".equals(mem.getElement())) {
            int val = Integer.parseInt(vo.getAttribute().split(",")[4]);
            try {
                val += (100) + new BigDecimal(vo.getEattribute().split(",")[4]).toBigInteger().intValue();
            } catch (Exception e) {
            }
            int total = 0;
            if (val <= 1000) {
                total = new BigDecimal(val + "").multiply(new BigDecimal("0.06")).multiply(new BigDecimal("5"))
                        .setScale(0, RoundingMode.DOWN).intValue();
            } else {
                total = new BigDecimal("60").add(
                                new BigDecimal((val - 1000) + "").multiply(new BigDecimal("0.3"))).multiply(new BigDecimal("5"))
                        .setScale(0, RoundingMode.DOWN).intValue();
            }
            effectSkill.add(Skill.builder().skillId(100000L).name("蚀爆").damage("204")
                    .minValue(total+"").maxValue(total+"")
                    .allowElement("0").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte) 0)
                    .scopeNum((byte) 8)
                    .maxBehavior("0").maxEnv("0").maxElement("0").build());
        }

    }

}
