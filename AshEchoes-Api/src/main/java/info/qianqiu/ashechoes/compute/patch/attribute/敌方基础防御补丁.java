package info.qianqiu.ashechoes.compute.patch.attribute;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;

public class 敌方基础防御补丁 {
    public static void patch1(ArrayList<Skill> effectSkill, CardVo vo) {
// boss防御相关数据，优先计算
        Long dfy = vo.getDfy();
        if (dfy != 0L) {
            String l = new BigDecimal(dfy).divide(new BigDecimal(100), MathContext.DECIMAL32).toPlainString();
            effectSkill.add(Skill.builder().skillId(100000L).name("敌方基础减伤值").damage("301")
                    .minValue("-" + l).maxValue("-" + l)
                    .allowElement("0").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte) 0)
                    .scopeNum((byte) 8)
                    .maxBehavior("0").maxEnv("0").maxElement("0").build());
        }
    }
}
