package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class 生命颂歌补丁 {

    public static void patch1(ArrayList<Skill> effectSkill, CardVo vo) {

        List<Skill> list = effectSkill.stream().filter(skill -> skill.getSkillId() == 110031L).toList();
        for (Skill skill : list) {
            if (vo.getSmsg() >= 1) {
                BigDecimal val = new BigDecimal(skill.getMinValue()).multiply(new BigDecimal(vo.getSmsg()));
                skill.setMaxValue(val.toPlainString());
                skill.setMinValue(val.toPlainString());
            } else {
                skill.setMaxValue("0");
                skill.setMinValue("0");
            }
        }
    }
}
