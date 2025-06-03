package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.compute.ComputeDataProcess;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class 烙痕魇境边界补丁 {
    public static boolean patch1(ArrayList<Skill> effectSkill, HashSet<Behavior> behaviors,
                              Character mem, CardVo vo) {

        boolean effect = judegeEffect(effectSkill, behaviors, mem, vo);
        List<Skill> llph = effectSkill.stream().filter(e -> e.getSkillId() == 130220L).toList();
        if (!llph.isEmpty()) {
            Skill s = llph.getFirst();
            if (!effect) {
                s.setMinValue("0");
                s.setMaxValue("0");
                return true;
            }
        }
        return false;
    }

    public static boolean judegeEffect(ArrayList<Skill> effectSkill, HashSet<Behavior> behaviors,
                                       Character mem, CardVo vo) {
        List<Skill> llph = effectSkill.stream().filter(e -> e.getSkillId() == 130220L).toList();
        if (!llph.isEmpty()) {
            //205、204就是刻印攻击
            List<Skill> kygj =
                    effectSkill.stream().filter(e -> e.getDamage().contains("205") || e.getDamage().contains("204"))
                            .toList();
            if (!kygj.isEmpty()) {
                BigDecimal value = new BigDecimal("0");
                BigDecimal attackEwai = new BigDecimal("0");

                for (Skill ss : kygj) {
                    boolean b = ComputeDataProcess.commonJudgeMaxValue(ss,
                            behaviors.stream().map(Behavior::getBehaviorId).toList(), mem, vo,
                            new CharacterInfoSkill());
                    String tvalue = b ? ss.getMaxValue() : ss.getMinValue();
                    for (int i = 0; i < ss.getDamage().split(",").length; i++) {
                        if (ss.getDamage().split(",")[i].equals("205")) {
                            value = value.add(new BigDecimal(tvalue.split("_")[i]));
                        }
                        if (ss.getDamage().split(",")[i].equals("204")) {
                            attackEwai = attackEwai.add(new BigDecimal(tvalue.split("_")[i]));
                        }
                    }
                }
                // 声望攻击
                String[] rLevelAttackValue = {"1", "1.2", "1.2", "1.2", "1.35", "1.35", "1.35", "1.5", "1.5", "1.5"};
                String attackRValue = "";
                if (0 != vo.getRLevel()) {
                    attackRValue = rLevelAttackValue[vo.getRLevel() - 1];
                }
                // 计算当前被除数 攻击的数值
                BigDecimal te = new BigDecimal(vo.getAttribute().split(",")[2]).add(
                                (new BigDecimal(vo.getEattribute().split(",")[2]))).multiply(new BigDecimal("1.2"))
                        .multiply(new BigDecimal(attackRValue));

                BigDecimal divide = attackEwai.divide(te, 4, RoundingMode.HALF_UP);
                if ((value.add(divide)).compareTo(new BigDecimal("0.7")) > 0) {
                    return false;
                }
            }
            if (vo.getSync() != null) {
                // 简单的刻印攻击同调判断
                if (vo.getSync().get当前刻印攻击百分比().compareTo(new BigDecimal("0.7")) > 0) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
