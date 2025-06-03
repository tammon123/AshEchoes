package info.qianqiu.ashechoes.compute.patch.characters;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.init.InitComputeData;

import java.math.BigDecimal;
import java.util.ArrayList;

public class 角色珑补丁 {

    public static void patch1(ArrayList<Skill> r, CardVo vo, Character c, InitComputeData init) {
        String leaderElement = init.getCharacter(Long.parseLong(vo.getCharacters().split(",")[0])).getElement();
        for (Skill skill : r) {
            // 如果有这个技能，那么这个角色肯定是1花以上
            if (skill.getSkillId().equals(320080L)) {
                // 如果不是队长
                if (vo.getCharacters().indexOf(c.getCharacterId() + "") != 0) {
                    skill.setMaxValue("0");
                    skill.setMinValue("0");
                }
            }
            // 珑的三花减抗需要线性计算
            if (skill.getSkillId().equals(320081L)) {
                if (!skill.getAllowElement().contains(c.getElement())) {
                    skill.setMinValue("0");
                    skill.setMaxValue("0");
                    continue;
                }
                int i = new BigDecimal(100).add(new BigDecimal(vo.getAttribute().split(",")[4]))
                        .add(new BigDecimal(vo.getEattribute().split(",")[4])).toBigInteger().intValue();
                if (i >= 1350) {
                    skill.setMinValue(skill.getMaxValue());
                } else {
                    // 需要线性增加
                    String value = "0.15";
                    //270, 540, 810, 1080
                    if (i > 1080) {
                        value = "0.19";
                    } else if (i > 810) {
                        value = "0.18";
                    } else if (i > 540) {
                        value = "0.17";
                    } else if (i > 270) {
                        value = "0.16";
                    }
                    skill.setMaxValue(value);
                }
            }
            if (leaderElement.equals("蚀")) {
                if (skill.getSkillId().equals(320084L) || skill.getSkillId().equals(320085L)) {
                    skill.setMaxValue("0");
                    skill.setMinValue("0");
                }
            } else {
                if (skill.getSkillId().equals(320083L)) {
                    skill.setMaxValue("0");
                    skill.setMinValue("0");
                }
            }
        }
    }
}
