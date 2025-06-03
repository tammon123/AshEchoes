package info.qianqiu.ashechoes.compute.patch.eqskill;

import info.qianqiu.ashechoes.compute.ComputeDataProcess;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;
import info.qianqiu.ashechoes.dto.domain.Skill;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class 杜望 {

    public static boolean patch1(ArrayList<Skill> effectSkill, HashSet<Behavior> behaviors,
                                 Character mem, CardVo vo) {

        BigDecimal bigDecimal = judegeEffect(effectSkill, behaviors, mem, vo);
        List<Skill> llph = effectSkill.stream().filter(e -> e.getSkillId() == 500011L).toList();
        if (!llph.isEmpty()) {
            Skill s = llph.getFirst();
            BigDecimal finalData = new BigDecimal("0.45");
            s.setMinValue("0.2");
            s.setMaxValue("0.2");
            if (bigDecimal.compareTo(finalData) < 0 && mem.getRole().equals("游徒")) {
                BigDecimal subtract = finalData.subtract(bigDecimal);
                if (subtract.compareTo(new BigDecimal("0.25")) > 0) {
                    s.setMinValue(subtract.setScale(2, RoundingMode.HALF_UP).toPlainString());
                    s.setMaxValue(subtract.setScale(2, RoundingMode.HALF_UP).toPlainString());
                }
            }
            return true;
        }
        return false;
    }

    public static BigDecimal judegeEffect(ArrayList<Skill> effectSkill, HashSet<Behavior> behaviors,
                                          Character mem, CardVo vo) {
        BigDecimal attack = BigDecimal.ZERO;
        //205、204就是刻印攻击
        List<Skill> kygj =
                effectSkill.stream().filter(e -> (e.getDamage().contains("205") || e.getDamage().contains("204")) && e.getSkillId() != 500011L)
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

            attack = value.add(attackEwai.divide(te, 4, RoundingMode.HALF_UP));

        }
        if (vo.getSync() != null) {
            // 简单的刻印攻击同调判断
            if (vo.getSync().get当前刻印攻击百分比().compareTo(attack) > 0) {
                return vo.getSync().get当前刻印攻击百分比();
            }
        }
        return attack;
    }

}
