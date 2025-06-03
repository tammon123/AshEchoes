package info.qianqiu.ashechoes.compute.patch.characters;

import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.util.ArrayList;

public class 角色拉波补丁 {

    public static void patch1(ArrayList<Skill> r, ArrayList<Character> characters) {
        for (Skill s : r) {
            if (s.getSkillId() == 370060L) {
                long count = characters.stream().filter(e -> e.getElement().equals("水") || e.getElement().equals("霜"))
                        .count();
                if (count != 0) {
                    BigDecimal total =
                            new BigDecimal("0.05").multiply(new BigDecimal(count)).add(new BigDecimal(s.getMinValue()));
                    if (total.compareTo(new BigDecimal(s.getMaxValue())) < 0) {
                        s.setMinValue(total.toPlainString());
                        s.setMaxValue(total.toPlainString());
                    }else {
                        s.setMaxValue("0.55");
                        s.setMinValue("0.55");
                    }
                }
            }
        }
    }
}
