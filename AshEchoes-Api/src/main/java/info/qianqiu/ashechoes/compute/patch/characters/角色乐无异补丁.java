package info.qianqiu.ashechoes.compute.patch.characters;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.util.ArrayList;

public class 角色乐无异补丁 {
    public static void patch1(int flowers, ArrayList<Skill> r, CardVo vo) {
        BigDecimal zd = new BigDecimal(100).add(new BigDecimal(vo.getAttribute().split(",")[4]))
                .add(new BigDecimal(vo.getEattribute().split(",")[4]));
        BigDecimal cz = new BigDecimal("0");
        if (zd.compareTo(new BigDecimal("1000")) > 0) {
            cz = zd.subtract(new BigDecimal("1000"));
            zd = new BigDecimal("1000");
        }
        for (Skill s : r) {
            if (flowers >= 3) {
                //1技能
                if (s.getSkillId() == 360080L) {
                    s.setMaxValue("0");
                    s.setMinValue("0");
                    s.setDamage("0");
                }
                if (s.getSkillId() == 360081L) {
                    s.setDamage("204");
                    BigDecimal add = zd.multiply(new BigDecimal("0.6")).add(cz.multiply(new BigDecimal("1.2")));
                    s.setMinValue(add.toPlainString());
                    s.setMaxValue(add.toPlainString());
                }
            } else{
                if (s.getSkillId() == 360080L) {
                    s.setDamage("204");
                    BigDecimal add = zd.multiply(new BigDecimal("0.3")).add(cz.multiply(new BigDecimal("0.6")));
                    s.setMinValue(add.toPlainString());
                    s.setMaxValue(add.toPlainString());
                    break;
                }
            }
        }
    }
}
