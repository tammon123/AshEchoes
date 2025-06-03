package info.qianqiu.ashechoes.compute.patch.characters;

import info.qianqiu.ashechoes.compute.ComputeDataProcess;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.utils.string.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class 角色景补丁 {
    public static void patch1(int flowers, ArrayList<Skill> r, HashSet<Behavior> behaviors, Character c, CardVo vo) {
        //获取所有减抗技能
        List<Skill> list = r.stream().filter(s -> s.getDamage().contains("304") &&
                (s.getAllowElement().contains("雷") || s.getMaxElement().contains("雷"))).toList();
        BigDecimal vakye = new BigDecimal("0");
        for (Skill skill : list) {
            String[] damages = skill.getDamage().split(",");
            // 判断最大值和最小值是否一致
            if (skill.getMinValue().equals(skill.getMaxValue())) {
                for (int i = 0; i < damages.length; i++) {
                    if (damages[i].equals("304")) {
                        String val = skill.getMinValue().split("_")[i];
                        vakye = vakye.add(new BigDecimal(val));
                        break;
                    }
                }
            } else {
                // 如果最大值和最小值不一样
                for (int i = 0; i < damages.length; i++) {
                    if (damages[i].equals("304")) {
                        boolean b = ComputeDataProcess.commonJudgeMaxValue(skill,
                                behaviors.stream().map(Behavior::getBehaviorId).toList(), c,
                                vo, new CharacterInfoSkill());
                        String min = skill.getMinValue().split("_")[i];
                        String max = skill.getMaxValue().split("_")[i];
                        if ("0".equals(max) || StringUtils.isEmpty(max)) {
                            max = min;
                        }
                        String val = min;
                        if (b) {
                            val = max;
                        }
                        vakye = vakye.add(new BigDecimal(val));
                        break;
                    }
                }
            }
        }

        String jc = "0.4";
        if (flowers >= 3) {
            jc = "0.8";
        }
        r.add(Skill.builder().skillId(600901L).name("裁决权柄·无视防御").damage("305")
                .minValue(vakye.multiply(new BigDecimal(jc)).setScale(2, RoundingMode.DOWN).toPlainString())
                .allowElement("0").level((byte) 1).desc("").behavior("0").allowShape("0").scope((byte)0)
                .scopeNum((byte) 8)
                .maxValue("").maxBehavior("0").maxEnv("0").maxElement("0").build());
    }
}
