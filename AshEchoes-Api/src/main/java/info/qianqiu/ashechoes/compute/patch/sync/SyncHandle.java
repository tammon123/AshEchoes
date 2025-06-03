package info.qianqiu.ashechoes.compute.patch.sync;

import info.qianqiu.ashechoes.compute.patch.skill.烙痕魇境边界补丁;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class SyncHandle {


    /**
     * 处理同调机制
     *
     * @param rSkillList
     * @param currentCharacter
     * @param vo
     * @return
     */
    public CardVo process(ArrayList<ArrayList<Skill>> rSkillList, Character currentCharacter, CardVo vo) {
        CardVo v = new CardVo();
        BeanUtils.copyProperties(vo, v);
        SyncData sync = new SyncData();

        // 如果有【默】这张烙痕
        // 因为魇镜需要特殊判断，因此单独吧攻击计算拉出来
        sync.set刻印攻击(sync.get刻印攻击().add(new BigDecimal("0.15")));
        for (ArrayList<Skill> skills : rSkillList) {
            BigDecimal 总刻印攻击百分比 = new BigDecimal(0);
            BigDecimal 总刻印攻击值 = new BigDecimal(0);

            List<Skill> rl = skills.stream().filter(s -> new SyncData().allowDamage.contains(s.getDamage())).toList();

            for (Skill s : rl) {
                String did = s.getDamage();
                if ("204".equals(did.trim())) {
                    总刻印攻击值 = 总刻印攻击值.add(new BigDecimal(s.getMinValue()));
                }
                if ("205".equals(did.trim())) {
                    总刻印攻击百分比 = 总刻印攻击百分比.add(new BigDecimal(s.getMinValue()));
                }
            }

            // 声望攻击
            String[] rLevelAttackValue = {"1", "1.2", "1.2", "1.2", "1.35", "1.35", "1.35", "1.5", "1.5", "1.5"};
            String attackRValue = "1";
            if (0 != vo.getRLevel()) {
                attackRValue = rLevelAttackValue[vo.getRLevel() - 1];
            }
            //刻印攻击需要单独处理
            String attack1 = vo.getAttribute().split(",")[2];
            String attack2 = vo.getEattribute().split(",")[2];
            BigDecimal totalAttack = new BigDecimal(attack1).add(new BigDecimal(attack2));
            // 计算当前被除数 攻击的数值
            BigDecimal te = totalAttack.multiply(new BigDecimal("1.2"))
                    .multiply(new BigDecimal(attackRValue));
            BigDecimal divide = 总刻印攻击值.divide(te, 4, RoundingMode.HALF_UP);
            总刻印攻击百分比 = 总刻印攻击百分比.add(divide);

            // 将刻印攻击值转换为百分比
            if (sync.get当前刻印攻击百分比().compareTo(总刻印攻击百分比) < 0) {
                if (总刻印攻击百分比.compareTo(sync.get刻印攻击()) > 0) {
                    sync.set当前刻印攻击百分比(sync.get刻印攻击());
                } else {
                    sync.set当前刻印攻击百分比(总刻印攻击百分比);
                }
            }
        }
        v.setSync(sync);
        烙痕魇境边界补丁.patch1(rSkillList.get(vo.getCurrentCharIndex()), new HashSet<Behavior>(),
                currentCharacter,
                v);
        for (ArrayList<Skill> skills : rSkillList) {
            BigDecimal 总额外攻击 = new BigDecimal(0);
            BigDecimal 总刻印专精 = new BigDecimal(0);
            BigDecimal 总暴击率 = new BigDecimal(0);
            BigDecimal 总同调暴击率 = new BigDecimal(0);
            BigDecimal 总暴击伤害 = new BigDecimal(0);

            List<Skill> rl = skills.stream().filter(s -> new SyncData().allowDamage.contains(s.getDamage())).toList();

            for (Skill s : rl) {
                String did = s.getDamage();
                if ("207".equals(did.trim())) {
                    总额外攻击 = 总额外攻击.add(new BigDecimal(s.getMinValue()));
                }
                if ("201".equals(did.trim())) {
                    总刻印专精 = 总刻印专精.add(new BigDecimal(s.getMinValue()));
                }
                if ("211".equals(did.trim())) {
                    总暴击率 = 总暴击率.add(new BigDecimal(s.getMinValue()));
                }
                if ("210".equals(did.trim())) {
                    if (new BigDecimal(s.getMinValue()).compareTo(总同调暴击率) > 0) {
                        总同调暴击率 = new BigDecimal(s.getMinValue());
                    }
                }
                if ("209".equals(did.trim())) {
                    总暴击伤害 = 总暴击伤害.add(new BigDecimal(s.getMinValue()));
                }
            }

            if (sync.get当前额外攻击().compareTo(总额外攻击) < 0) {
                if (总额外攻击.compareTo(sync.get额外攻击()) > 0) {
                    sync.set当前额外攻击(sync.get额外攻击());
                } else {
                    sync.set当前额外攻击(总额外攻击);
                }
            }
            if (sync.get当前刻印专精().compareTo(总刻印专精) < 0) {
                if (总刻印专精.compareTo(sync.get刻印专精()) > 0) {
                    sync.set当前刻印专精(sync.get刻印专精());
                } else {
                    sync.set当前刻印专精(总刻印专精);
                }
            }
            if (sync.get当前暴击率().compareTo(总暴击率.add(总同调暴击率)) < 0) {
                if ((总暴击率.add(总同调暴击率)).compareTo(sync.get暴击率()) > 0) {
                    sync.set当前暴击率(sync.get暴击率());
                } else {
                    sync.set当前暴击率(总暴击率.add(总同调暴击率));
                }
            }
            if (sync.get当前暴击伤害().compareTo(总暴击伤害) < 0) {
                if (总暴击伤害.compareTo(sync.get暴击伤害()) > 0) {
                    sync.set当前暴击伤害(sync.get暴击伤害());
                } else {
                    sync.set当前暴击伤害(总暴击伤害);
                }
            }
        }
        // 再修复一下
        v.setSync(sync);
        return v;
    }

}
