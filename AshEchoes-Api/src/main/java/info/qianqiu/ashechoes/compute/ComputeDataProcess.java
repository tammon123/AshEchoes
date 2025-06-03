package info.qianqiu.ashechoes.compute;

import com.alibaba.fastjson2.JSONObject;
import info.qianqiu.ashechoes.compute.simple.*;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.utils.array.ArrayListUtils;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class ComputeDataProcess {
    private final InitComputeData initComputeData;

    public ComputeDataProcess(InitComputeData initComputeData) {
        this.initComputeData = initComputeData;
    }

    /**
     * 计算攻击
     *
     * @param skills
     * @param re
     * @param attrList
     */
    String attackCompute(Character character, List<Skill> skills, List<Character> chars,
                         HashSet<Behavior> behaviors, JSONObject re, CardVo vo, ArrayList<Skill> attrList) {
        LinkedHashMap<String, String> r = new LinkedHashMap<>();
        List<Long> behaviorIds = behaviors.stream().map(Behavior::getBehaviorId).toList();
        String[] rLevelValue = {"1", "1.2", "1.2", "1.2", "1.35", "1.35", "1.35", "1.5", "1.5", "1.5"};
        String value = "";
        if (0 != vo.getRLevel()) {
            value = rLevelValue[vo.getRLevel() - 1];
        }
        AttackCompute attack = AttackCompute.builder().队长修正("1.2")
                .角色基础属性(character.getAttack().add(character.getEAttack()).toPlainString()).声望加成(value)
                .刻印攻击(vo.getAttribute().split(",")[2]).监督刻印攻击(vo.getEattribute().split(",")[2])
                .build();

        for (Skill skill : skills) {
            if (skill.getDamage().contains("204")
                    || skill.getDamage().contains("205")
                    || skill.getDamage().contains("207")
                    || skill.getDamage().contains("208")
                    || skill.getDamage().contains("214")
                    || skill.getDamage().contains("215")
            ) {
                if (!skill.getAllowElement().equals("0") && !skill.getAllowElement().contains(character.getElement()) &&
                        (skill.getScope() == (byte) 0)) {
                    continue;
                }
                String[] damages = skill.getDamage().split(",");
                // 判断最大值和最小值是否一致
                if (skill.getMinValue().equals(skill.getMaxValue())) {
                    for (int i = 0; i < damages.length; i++) {
                        String val = skill.getMinValue().split("_")[i];
                        attackComputeForChoose(damages[i], val, r, attack, skill, character, chars, vo, attrList);
                    }
                } else {
                    // 如果最大值和最小值不一样
                    for (int i = 0; i < damages.length; i++) {
                        String min = skill.getMinValue().split("_")[i];
                        String max = skill.getMaxValue().split("_")[i];
                        if ("0".equals(max) || StringUtils.isEmpty(max)) {
                            max = min;
                        }
                        if (min.equals(max)) {
                            attackComputeForChoose(damages[i], max, r, attack, skill, character, chars, vo, attrList);
                            continue;
                        }
                        boolean flag = commonJudgeMaxValue(skill, behaviorIds, character, vo, new CharacterInfoSkill());

                        if (flag) {
                            attackComputeForChoose(damages[i], max, r, attack, skill, character, chars, vo, attrList);
                        } else {
                            attackComputeForChoose(damages[i], min, r, attack, skill, character, chars, vo, attrList);
                        }
                    }
                }
            }
        }

        Map<String, String> sorted = new TreeMap<>(r);
        r = new LinkedHashMap<>(sorted);
        double compute = attack.compute(r, vo);
        r.put("最终计算攻击乘区:", compute + "");
        re.put("attackInfo", r);

        return compute + "";
    }

    private void attackComputeForChoose(String damage, String val, LinkedHashMap<String, String> r,
                                        AttackCompute crit, Skill skill, Character character, List<Character> chars,
                                        CardVo vo, ArrayList<Skill> attrList) {

        String sn = initComputeData.getSkillNameDesc(skill.getSkillId(), vo);
        if (StringUtils.isEmpty(sn)) {
            sn = skill.getName();
        }
        Skill copySkills = new Skill();
        copySkills.setMaxValue(val);
        copySkills.setMinValue(val);
        if ("204".equals(damage)) {
            crit.刻印攻击增加值(val);
            attrList.add(copySkills);
            r.put("刻印攻击增加值:_" + (r.size() + 1),
                    val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("205".equals(damage)) {
            attrList.add(copySkills);
            crit.刻印攻击百分比(val);
            r.put("刻印攻击百分比:_" + (r.size() + 1),
                    val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("207".equals(damage)) {
            attrList.add(copySkills);
            crit.额外攻击百分比(val);
            r.put("额外攻击百分比:_" + (r.size() + 1),
                    val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("208".equals(damage)) {
            attrList.add(copySkills);
            crit.额外攻击增加值(val);
            r.put("额外攻击增加值:_" + (r.size() + 1),
                    val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("214".equals(damage)) {
            attrList.add(copySkills);
            crit.攻击力加成(val);
            r.put("攻击力加成:_" + (r.size() + 1),
                    val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("215".equals(damage)) {
            attrList.add(copySkills);
            if (crit.同调者攻击(val)) {
                r.put("同调者攻击加成:_",
                        val + "_来源:" + sn + "_" + skill.getSkillId());
            }
        }
    }

    /**
     * 体质计算
     *
     * @param skills
     * @param re
     * @param attrList
     */
    String healthCompute(Character character, List<Skill> skills, List<Character> chars,
                         HashSet<Behavior> behaviors, JSONObject re, CardVo vo, ArrayList<Skill> attrList) {
        LinkedHashMap<String, String> r = new LinkedHashMap<>();
        List<Long> behaviorIds = behaviors.stream().map(Behavior::getBehaviorId).toList();
        String[] rLevelValue = {"1.2", "1.2", "1.2", "1.35", "1.35", "1.35", "1.5", "1.5", "1.5", "1.5"};
        String value = "";
        if (0 != vo.getRLevel()) {
            value = rLevelValue[vo.getRLevel() - 1];
        }
        HealthCompute health = HealthCompute.builder().潜能体质百分比("1.2").队长修正("1.2")
                .监督刻印体质(vo.getEattribute().split(",")[0])
                .刻印体质(vo.getAttribute().split(",")[0]).声望加成(value)
                .角色基础属性百分比(character.getHealth().add(character.getEHealth()).toPlainString()).build();

        for (Skill skill : skills) {
            if (skill.getDamage().contains("212")
            ) {
                if (!skill.getAllowElement().equals("0") && !skill.getAllowElement().contains(character.getElement()) &&
                        (skill.getScope() == (byte) 0)) {
                    continue;
                }
                String[] damages = skill.getDamage().split(",");
                // 判断最大值和最小值是否一致
                if (skill.getMinValue().equals(skill.getMaxValue())) {
                    for (int i = 0; i < damages.length; i++) {
                        String val = skill.getMinValue().split("_")[i];
                        healthComputeForChoose(damages[i], val, r, health, skill, character, chars, vo, attrList);
                    }
                } else {
                    // 如果最大值和最小值不一样
                    for (int i = 0; i < damages.length; i++) {
                        String min = skill.getMinValue().split("_")[i];
                        String max = skill.getMaxValue().split("_")[i];
                        if ("0".equals(max) || StringUtils.isEmpty(max)) {
                            max = min;
                        }
                        if (min.equals(max)) {
                            healthComputeForChoose(damages[i], max, r, health, skill, character, chars, vo, attrList);
                            continue;
                        }
                        boolean flag = commonJudgeMaxValue(skill, behaviorIds, character, vo, new CharacterInfoSkill());

                        if (flag) {
                            healthComputeForChoose(damages[i], max, r, health, skill, character, chars, vo, attrList);
                        } else {
                            healthComputeForChoose(damages[i], min, r, health, skill, character, chars, vo, attrList);
                        }
                    }
                }
            }
        }


        Map<String, String> sorted = new TreeMap<>(r);
        r = new LinkedHashMap<>(sorted);
        double compute = health.compute(r);
        r.put("最终计算体质数值:", compute + "");
        re.put("healthInfo", r);

        return compute + "";
    }

    private void healthComputeForChoose(String damage, String val, LinkedHashMap<String, String> r,
                                        HealthCompute crit, Skill skill, Character character, List<Character> chars,
                                        CardVo vo, ArrayList<Skill> attrList) {

        String sn = initComputeData.getSkillNameDesc(skill.getSkillId(), vo);
        Skill copySkills = new Skill();
        copySkills.setMaxValue(val);
        copySkills.setMinValue(val);
        if (StringUtils.isEmpty(sn)) {
            sn = skill.getName();
        }
        if ("212".equals(damage)) {
            attrList.add(copySkills);
            crit.刻印体质百分比(val);
            r.put("刻印体质百分比加成:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }
    }

    /**
     * 专精计算
     *
     * @param skills
     * @param re
     * @param attrList
     */
    String masteryCompute(Character character, List<Skill> skills, List<Character> chars,
                          HashSet<Behavior> behaviors, JSONObject re, CardVo vo, ArrayList<Skill> attrList) {
        LinkedHashMap<String, String> r = new LinkedHashMap<>();
        List<Long> behaviorIds = behaviors.stream().map(Behavior::getBehaviorId).toList();
        String[] rLevelValue = {"1", "1", "1.2", "1.2", "1.2", "1.35", "1.35", "1.35", "1.45", "1.5"};
        String value = "";
        if (0 != vo.getRLevel()) {
            value = rLevelValue[vo.getRLevel() - 1];
        }
        MasteryCompute mastery = MasteryCompute.builder().队长加成("1.2").刻印专精(vo.getAttribute().split(",")[3])
                .监督刻印专精(vo.getEattribute().split(",")[3]).声望加成(value)
                .角色面板专精(character.getMastery().add(character.getEMastery()).toPlainString()).build();

        for (Skill skill : skills) {
            if (skill.getDamage().contains("201")
                    || skill.getDamage().contains("202")
                    || skill.getDamage().contains("203")
                    || skill.getDamage().contains("216")
            ) {
                if (!skill.getAllowElement().equals("0") && !skill.getAllowElement().contains(character.getElement()) &&
                        (skill.getScope() == (byte) 0)) {
                    continue;
                }
                String[] damages = skill.getDamage().split(",");
                // 判断最大值和最小值是否一致
                if (skill.getMinValue().equals(skill.getMaxValue())) {
                    for (int i = 0; i < damages.length; i++) {
                        String val = skill.getMinValue().split("_")[i];
                        masteryComputeForChoose(damages[i], val, r, mastery, skill, character, chars, vo, attrList);
                    }
                } else {
                    // 如果最大值和最小值不一样
                    for (int i = 0; i < damages.length; i++) {
                        String min = skill.getMinValue().split("_")[i];
                        String max = skill.getMaxValue().split("_")[i];
                        if ("0".equals(max) || StringUtils.isEmpty(max)) {
                            max = min;
                        }
                        if (min.equals(max)) {
                            masteryComputeForChoose(damages[i], max, r, mastery, skill, character, chars, vo, attrList);
                            continue;
                        }
                        boolean flag = commonJudgeMaxValue(skill, behaviorIds, character, vo, new CharacterInfoSkill());

                        if (flag) {
                            masteryComputeForChoose(damages[i], max, r, mastery, skill, character, chars, vo, attrList);
                        } else {
                            masteryComputeForChoose(damages[i], min, r, mastery, skill, character, chars, vo, attrList);
                        }
                    }
                }
            }
        }

        Map<String, String> sorted = new TreeMap<>(r);
        r = new LinkedHashMap<>(sorted);
        double compute = mastery.attackCompute(character.getMasteryRate(), r, vo);
        // 是队长
        if (chars.getFirst().getCharacterId().equals(character.getCharacterId())
                && ArrayListUtils.getCharIndex(character.getCharacterId(), chars) == 0) {
            if (Integer.parseInt(vo.getPLevel().split(",")[0]) >= 3) {
                compute = mastery.attackCompute("1.1", r, vo);
            }
        }
        r.put("最终计算专精增伤:", compute + "");
        re.put("masteryInfo", r);

        return r.get("最终计算专精数值:") + "_" + compute;
    }

    private void masteryComputeForChoose(String damage, String val, LinkedHashMap<String, String> r,
                                         MasteryCompute crit, Skill skill, Character character, List<Character> chars,
                                         CardVo vo, ArrayList<Skill> attrList) {
        String sn = initComputeData.getSkillNameDesc(skill.getSkillId(), vo);
        Skill copySkills = new Skill();
        copySkills.setMaxValue(val);
        copySkills.setMinValue(val);
        if (StringUtils.isEmpty(sn)) {
            sn = skill.getName();
        }
        if ("201".equals(damage)) {
            attrList.add(copySkills);
            crit.刻印专精百分比方法(val);
            r.put("刻印专精百分比:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("202".equals(damage)) {
            attrList.add(copySkills);
            crit.刻印专精额外增加方法(val);
            r.put("刻印专精固定值:_" + (r.size() + 1),
                    val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("203".equals(damage)) {
            attrList.add(copySkills);
            boolean 同调者专精 = crit.同调者专精(val);
            if (同调者专精) {
                r.put("同调者专精百分比:_",
                        val + "_来源:" + sn + "_" + skill.getSkillId());
            }
        }
        if ("216".equals(damage)) {
            attrList.add(copySkills);
            crit.专精加成百分比方法(val);
            r.put("专精百分比加成:_" + (r.size() + 1),
                    val + "_来源:" + sn + "_" + skill.getSkillId());
        }
    }

    /**
     * 暴击乘区计算
     *
     * @param skills
     * @param re
     * @param attrList
     */
    String critCompute(Character character, List<Skill> skills, List<Character> chars,
                       HashSet<Behavior> behaviors, JSONObject re, CardVo vo, ArrayList<Skill> attrList) {
        LinkedHashMap<String, String> r = new LinkedHashMap<>();
        List<Long> behaviorIds = behaviors.stream().map(Behavior::getBehaviorId).toList();
        CritCompute crit = CritCompute.builder().build();

        for (Skill skill : skills) {
            if (skill.getDamage().contains("209")
                    || skill.getDamage().contains("210")
                    || skill.getDamage().contains("211")
            ) {
                if (!skill.getAllowElement().equals("0") && !skill.getAllowElement().contains(character.getElement()) &&
                        (skill.getScope() == (byte) 0)) {
                    continue;
                }
                String[] damages = skill.getDamage().split(",");
                // 判断最大值和最小值是否一致
                if (skill.getMinValue().equals(skill.getMaxValue())) {
                    for (int i = 0; i < damages.length; i++) {
                        String val = skill.getMinValue().split("_")[i];
                        critComputeForChoose(damages[i], val, r, crit, skill, character, chars, vo, attrList);
                    }
                } else {
                    // 如果最大值和最小值不一样
                    for (int i = 0; i < damages.length; i++) {
                        String min = skill.getMinValue().split("_")[i];
                        String max = skill.getMaxValue().split("_")[i];
                        if ("0".equals(max) || StringUtils.isEmpty(max)) {
                            max = min;
                        }
                        if (min.equals(max)) {
                            critComputeForChoose(damages[i], max, r, crit, skill, character, chars, vo, attrList);
                            continue;
                        }
                        boolean flag = commonJudgeMaxValue(skill, behaviorIds, character, vo, new CharacterInfoSkill());

                        if (flag) {
                            critComputeForChoose(damages[i], max, r, crit, skill, character, chars, vo, attrList);
                        } else {
                            critComputeForChoose(damages[i], min, r, crit, skill, character, chars, vo, attrList);
                        }
                    }
                }
            }
        }
// 使用TreeMap进行排序
        Map<String, String> sorted = new TreeMap<>(r);
        r = new LinkedHashMap<>(sorted);
        double compute = crit.compute(r, vo);
        r.put("最终计算暴击乘区:", compute + "");
        re.put("critInfo", r);
        return compute + "";
    }

    private void critComputeForChoose(String damage, String val, LinkedHashMap<String, String> r,
                                      CritCompute crit, Skill skill, Character character, List<Character> chars,
                                      CardVo vo, ArrayList<Skill> attrList) {
        // 职业联动

        String sn = initComputeData.getSkillNameDesc(skill.getSkillId(), vo);
        if (StringUtils.isEmpty(sn)) {
            sn = skill.getName();
        }
        Skill copySkills = new Skill();
        copySkills.setMaxValue(val);
        copySkills.setMinValue(val);
        if ("209".equals(damage)) {
            attrList.add(copySkills);
            crit.暴击伤害(val);
            r.put("暴击伤害:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("210".equals(damage)) {
            attrList.add(copySkills);
            if (crit.同调者暴击率(val)) {
                r.put("同调者暴击率:_", val + "_来源:" + sn + "_" + skill.getSkillId());
            }
        }
        if ("211".equals(damage)) {
            attrList.add(copySkills);
            crit.暴击率(val);
            r.put("暴击率:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }
    }

    /**
     * 伤害乘区计算
     *
     * @param skills
     * @param re
     * @param cis
     */
    String damageCompute(Character character, List<Skill> skills, List<Character> chars,
                         HashSet<Behavior> behaviors, JSONObject re, CardVo vo, CharacterInfoSkill cis) {
        LinkedHashMap<String, String> r = new LinkedHashMap<>();

        List<Long> behaviorIds = behaviors.stream().map(Behavior::getBehaviorId).toList();
        DamageCompute damage = DamageCompute.builder().build();

        for (Skill skill : skills) {
            if (skill.getDamage().contains("101")
                    || skill.getDamage().contains("103")
                    || skill.getDamage().contains("105")
                    || skill.getDamage().contains("106")
                    || skill.getDamage().contains("107")
            ) {
                if (!skill.getAllowElement().equals("0") && !skill.getAllowElement().contains(cis.getElement()) &&
                        (skill.getScope() == (byte) 0)) {
                    continue;
                }
                String[] damages = skill.getDamage().split(",");
                // 判断最大值和最小值是否一致
                if (skill.getMinValue().equals(skill.getMaxValue())) {
                    for (int i = 0; i < damages.length; i++) {
                        String val = skill.getMinValue().split("_")[i];
                        damageComputeForChoose(damages[i], val, r, damage, skill, character, chars, re, vo);
                    }
                } else {
                    // 如果最大值和最小值不一样
                    // 可能造成情况:举例:烈焰暴击 ， 普通状态下允许所有类型的攻击
                    for (int i = 0; i < damages.length; i++) {
                        String min = skill.getMinValue().split("_")[i];
                        String max = skill.getMaxValue().split("_")[i];
                        // 最大值不应该为0，如果是0那就按照最小值计算
                        if ("0".equals(max) || StringUtils.isEmpty(max)) {
                            max = min;
                        }
                        // 如果这个乘区 最大值和最小值一致，那么可以直接添加。不需要额外计算
                        if (min.equals(max)) {
                            damageComputeForChoose(damages[i], max, r, damage, skill, character, chars, re, vo);
                            continue;
                        }
                        //如果不一样，需要考虑是否满足行为
                        boolean flag = commonJudgeMaxValue(skill, behaviorIds, character, vo, cis);
                        if (flag) {
                            damageComputeForChoose(damages[i], max, r, damage, skill, character, chars, re, vo);
                        } else {
                            damageComputeForChoose(damages[i], min, r, damage, skill, character, chars, re, vo);
                        }

                    }
                }
            }
        }
        Map<String, String> sorted = new TreeMap<>(r);
        r = new LinkedHashMap<>(sorted);
        double compute = damage.compute(r);
        r.put("最终计算增伤乘区:", compute + "");
        re.put("buffInfo", r);
        return compute + "";
    }

    private void damageComputeForChoose(String damage, String val, LinkedHashMap<String, String> r,
                                        DamageCompute crit, Skill skill, Character character, List<Character> chars,
                                        JSONObject re, CardVo vo) {


        String sn = initComputeData.getSkillNameDesc(skill.getSkillId(), vo);
        if (StringUtils.isEmpty(sn)) {
            sn = skill.getName();
        }

        if ("101".equals(damage)) {
            crit.激励(val);
            r.put("激励增伤:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }

        if ("103".equals(damage)) {
            crit.技能增伤(val);
            r.put("技能增伤:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }

        if ("105".equals(damage)) {
            crit.额外伤害(val);
            r.put("额外增伤:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }

        if ("106".equals(damage)) {
            crit.独立增伤(val);
            r.put("独立增伤:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("107".equals(damage)) {
            BigDecimal critRate = new BigDecimal(vo.getCritValue());
            String plainString = new BigDecimal(val).multiply(critRate).setScale(2, RoundingMode.DOWN)
                    .toPlainString();
            crit.暴击独立增伤(plainString);
            r.put("暴击增伤:_" + (r.size() + 1),
                    plainString + "_来源:" + sn + "_" + skill.getSkillId());
        }
    }

    /**
     * debuff乘区
     *
     * @param skills
     * @param re
     * @param cis
     */
    String debuffCompute(Character character, List<Skill> skills, List<Character> chars,
                         HashSet<Behavior> behaviors, JSONObject re, CardVo vo, CharacterInfoSkill cis) {
        LinkedHashMap<String, String> r = new LinkedHashMap<>();
        List<Long> behaviorIds = behaviors.stream().map(Behavior::getBehaviorId).toList();
        DebuffCompute debuff = DebuffCompute.builder().build();
        for (Skill skill : skills) {
            if (skill.getDamage().contains("301")
                    || skill.getDamage().contains("302")
                    || skill.getDamage().contains("303")
                    || skill.getDamage().contains("304")
                    || skill.getDamage().contains("305")
            ) {
                if (!skill.getAllowElement().equals("0") && !skill.getAllowElement().contains(cis.getElement()) &&
                        (skill.getScope() == (byte) 0)) {
                    if (skill.getSkillId() != 400002L || !skill.getAllowElement().equals(chars.getFirst().getElement())) {
                        continue;
                    }
                }
                String[] damages = skill.getDamage().split(",");
                // 判断最大值和最小值是否一致
                if (skill.getMinValue().equals(skill.getMaxValue())) {
                    for (int i = 0; i < damages.length; i++) {
                        String val = skill.getMinValue().split("_")[i];
                        debuffComputeForChoose(damages[i], val, r, debuff, skill, vo);
                    }
                } else {
                    // 如果最大值和最小值不一样
                    for (int i = 0; i < damages.length; i++) {
                        String min = skill.getMinValue().split("_")[i];
                        String max = skill.getMaxValue().split("_")[i];
                        if ("0".equals(max) || StringUtils.isEmpty(max)) {
                            max = min;
                        }
                        if (min.equals(max)) {
                            debuffComputeForChoose(damages[i], max, r, debuff, skill, vo);
                            continue;
                        }
                        boolean flag = commonJudgeMaxValue(skill, behaviorIds, character, vo, cis);

                        if (flag) {
                            debuffComputeForChoose(damages[i], max, r, debuff, skill, vo);
                        } else {
                            debuffComputeForChoose(damages[i], min, r, debuff, skill, vo);
                        }
                    }
                }
            }
        }
        Map<String, String> sorted = new TreeMap<>(r);
        r = new LinkedHashMap<>(sorted);
        Long dkx = Long.parseLong(vo.getDkx());
        if (dkx != 0L) {
            String l = new BigDecimal(dkx).divide(new BigDecimal(100), MathContext.DECIMAL32).toPlainString();
            r.put("敌方基础抗性增加:_" + (r.size() + 1), l + "_来源:敌方抗性提供");
            debuff.减抗("-" + l);
        }
        double compute = debuff.compute(r, character, vo);
        r.put("最终计算减益乘区:", compute + "");
        re.put("debuffInfo", r);
        return compute + "";
    }

    private void debuffComputeForChoose(String damage, String val, LinkedHashMap<String, String> r,
                                        DebuffCompute debuff, Skill skill, CardVo vo) {
        String sn = initComputeData.getSkillNameDesc(skill.getSkillId(), vo);
        if (StringUtils.isEmpty(sn)) {
            sn = skill.getName();
        }
        if ("301".equals(damage)) {
            if (debuff.消融(val)) {
                r.put("防御降低:_"+ (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
            }
        }
        if ("305".equals(damage)) {
            debuff.无视防御(val);
            r.put("无视防御:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("302".equals(damage)) {
            debuff.易伤(val);
            r.put("易伤:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
        }
        if ("303".equals(damage)) {
            if (debuff.元素易伤(val)) {
                r.put("元素易伤:_", val + "_来源:" + sn + "_" + skill.getSkillId());
            }
        }
        if ("304".equals(damage)) {
            r.put("抗性降低:_" + (r.size() + 1), val + "_来源:" + sn + "_" + skill.getSkillId());
            if (skill.getSkillId().equals(1000001L)) {
                debuff.不计入上限减抗(val);
                return;
            }
            debuff.减抗(val);

        }
    }

    /**
     * 判断是否取最大值，并且判断最大值元素相关
     *
     * @param skill
     * @param behaviorIds
     * @param character
     * @param vo
     * @param cis
     * @return
     */
    public static boolean commonJudgeMaxValue(Skill skill, List<Long> behaviorIds, Character character, CardVo vo,
                                              CharacterInfoSkill cis) {
        boolean flag = true;

        if (!"0".equals(skill.getMaxBehavior()) && StringUtils.isNotEmpty(skill.getMaxBehavior())) {
            // 排除职业联动的行为 职业联动不用计算最大值
            List<Long> bs;
            if (skill.getMaxBehavior().contains(",")) {
                bs = Arrays.stream(skill.getMaxBehavior().split(",")).toList().stream()
                        .filter(e -> !e.equals("1061") && !e.equals("1062")).map(Long::valueOf)
                        .toList();
                if (!new HashSet<Long>(behaviorIds).containsAll(bs)) {
                    flag = false;
                }
            } else if (skill.getMaxBehavior().contains("|")) {
                bs = Arrays.stream(skill.getMaxBehavior().split("\\|")).toList().stream()
                        .filter(e -> !e.equals("1061") && !e.equals("1062")).map(Long::valueOf)
                        .toList();
                for (Long b : bs) {
                    if (!behaviorIds.contains(b)) {
                        flag = false;
                    } else {
                        flag = true;
                        break;
                    }
                }
            } else {
                if (!behaviorIds.contains(Long.parseLong(skill.getMaxBehavior()))) {
                    flag = false;
                }
            }
        }

        if (!skill.getMaxEnv().equals(vo.getEnv()) && !"0".equals(skill.getMaxEnv())) {
            flag = false;
        }
        // 判断增伤(100-200的乘区)等属性，应该用攻击属性来判断
        if (!cis.getCId().equals(0L)) {
            if (!skill.getMaxElement().contains(cis.getElement()) && !"0".equals(skill.getMaxElement())) {
                flag = false;
            }
        } else {
            // 判断属性加成的乘区用角色属性判断
            if (!skill.getMaxElement().contains(character.getElement()) && !"0".equals(skill.getMaxElement())) {
                flag = false;
            }
        }

        return flag;

    }


}
