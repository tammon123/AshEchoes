package info.qianqiu.ashechoes.compute;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import info.qianqiu.ashechoes.compute.patch.sync.SyncData;
import info.qianqiu.ashechoes.compute.patch.sync.SyncHandle;
import info.qianqiu.ashechoes.compute.patch.SkillPatch;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.controller.vo.OverviewDataVo;
import info.qianqiu.ashechoes.dto.domain.*;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.utils.array.ArrayListUtils;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 通过传递进来的角色ID以及烙痕ID
 * 计算所有伤害
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ComputeDataHandle {

    private final InitComputeData init;
    private final ComputeDataProcess computeDataProcess;
    private final ComputeSkillProcess computeSkillProcess;
    private final ComputeElementBehaviorAndEffect computeElementBehaviorAndEffect;
    private final SkillPatch skillPatch;
    private final SyncHandle syncHandle;

    public R init(CardVo vo) {
        JSONObject result = new JSONObject();
        // 初始化数据，以及修复错误数据
        dataFix(vo);
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Memory> memories = new ArrayList<>();
        // 初始化角色以及信息
        initCharAndMemoryData(characters, memories, vo);
        // 初始化角色从所有技能中的行为
        Map<Long, List<CharacterInfoSkill>> collect = initCharacterInfoSkillBehavior(characters);
        //取出参与计算的角色
        List<Character> computeCharacter = characters;
        if (characters.size() >= 4) {
            computeCharacter = characters.subList(0, 4);
        }
        ArrayList<ArrayList<Skill>> rSkillList = new ArrayList<>();
        ArrayList<HashSet<Behavior>> behaviorList = new ArrayList<>();
        for (int charIndex = 0; charIndex < computeCharacter.size(); charIndex++) {
            Character currentCharacter = computeCharacter.get(charIndex);
            vo.setCurrentCharIndex(charIndex);
            // 初始化所有行为
            HashSet<Behavior> behaviors =
                    initCurrentCharacterBehavior(currentCharacter, charIndex, vo, characters, memories, collect);
            // 判断生效的技能
            ArrayList<Skill> skills = computeSkillProcess.initSkill(characters, behaviors, vo, currentCharacter);
            rSkillList.add(skills);
            behaviorList.add(behaviors);
            //技能处理完成，准备处理计算结果
        }
        OverviewDataVo overviewDataVo = computeHandle(characters, collect, rSkillList, behaviorList, result, vo);
        if (overviewDataVo != null) {
            return R.ok(overviewDataVo);
        }
        return R.ok(result);
    }

    private OverviewDataVo computeHandle(ArrayList<Character> characters, Map<Long, List<CharacterInfoSkill>> collect,
                                         ArrayList<ArrayList<Skill>> rSkillList,
                                         ArrayList<HashSet<Behavior>> behaviorList,
                                         JSONObject result, CardVo vo1) {
        for (int i = 0; i < rSkillList.size(); i++) {
            Character currentCharacter = characters.get(i);
            vo1.setCurrentCharIndex(i);
            CardVo vo = vo1;
            // 处理同调机制
            if ((vo1.getTd() && i == 0) || new SyncData().allow.contains(currentCharacter.getName())) {
                vo = syncHandle.process(rSkillList, currentCharacter, vo1);
            }
            ArrayList<Skill> rSkills = rSkillList.get(i);
            HashSet<Behavior> behaviors = behaviorList.get(i);

            JSONObject charJo = new JSONObject();
            // 计算加成数据概览
            if (vo.isOverview()) {
                Character overviewChar = characters.get(vo.getCoverview());

                //如果不符合同调标准 就删除
                if (!(vo1.getTd() && vo.getCoverview() == 0) &&
                        !new SyncData().allow.contains(overviewChar.getName())) {
                    vo.setSync(null);
                }

                rSkills = rSkillList.get(vo.getCoverview());
                behaviors = behaviorList.get(vo.getCoverview());
                String mastery =
                        computeDataProcess.masteryCompute(overviewChar, rSkills, characters,
                                behaviors,
                                new JSONObject(), vo1, new ArrayList<>());
                return overview(rSkills, overviewChar, vo, behaviors, mastery.split("_")[1]);
            }


            // 删除behavior中，角色的攻击行为。后续判断使用技能附带的行为判断
            behaviors.removeIf(behavior -> behavior.getBehaviorId() >= 1011 && behavior.getBehaviorId() <= 1019);

            List<CharacterInfoSkill> characterInfoSkills = collect.get(currentCharacter.getCharacterId());
            List<CharacterInfoSkill> bb1 = null;
            List<CharacterInfoSkill> bb2 = null;
            List<CharacterInfoSkill> bb3 = null;
            List<CharacterInfoSkill> bb4 = null;
            if (characterInfoSkills != null && !characterInfoSkills.isEmpty()) {
                bb1 = characterInfoSkills.stream().filter(e -> e.getType().contains("普攻")).toList();
                bb2 = characterInfoSkills.stream().filter(e -> e.getType().contains("主动")).toList();
                bb3 = characterInfoSkills.stream().filter(e -> e.getType().contains("异核")).toList();
                bb4 = characterInfoSkills.stream().filter(e -> e.getType().contains("自动")).toList();
            }
            // 普攻类型计算
            String bn = "普攻";
            JSONObject jo1 = new JSONObject();
            ArrayList<Skill> b1l = new ArrayList<>();
            b1l.addAll(rSkills);
            BigDecimal totalDamage = new BigDecimal(0);
            totalDamage = totalDamage.add(compute(b1l, characters, behaviors, jo1, vo, currentCharacter,
                    bb1));
            if (totalDamage.compareTo(new BigDecimal(0)) != 0) {
                charJo.put(bn, jo1);
            }
            // 主动类型计算
            bn = "指令";
            JSONObject jo2 = new JSONObject();
            ArrayList<Skill> b2l = new ArrayList<>();
            b2l.addAll(rSkills);
            totalDamage = totalDamage.add(compute(b2l, characters, behaviors, jo2, vo, currentCharacter,
                    bb2));
            if (totalDamage.compareTo(new BigDecimal(0)) != 0) {
                charJo.put(bn, jo2);
            }
            //自动类型计算
            bn = "自动";
            JSONObject jo4 = new JSONObject();
            ArrayList<Skill> b4l = new ArrayList<>();
            b4l.addAll(rSkills);
            totalDamage = totalDamage.add(compute(b4l, characters, behaviors, jo4, vo, currentCharacter,
                    bb4));
            if (totalDamage.compareTo(new BigDecimal(0)) != 0) {
                charJo.put(bn, jo4);
            }
            //异核类型计算
            bn = "异核";
            JSONObject jo3 = new JSONObject();
            ArrayList<Skill> b3l = new ArrayList<>();
            b3l.addAll(rSkills);
            totalDamage = totalDamage.add(compute(b3l, characters, behaviors, jo3, vo, currentCharacter,
                    bb3));

            if (totalDamage.compareTo(new BigDecimal("0")) > 0) {
//                jo3.put("总结:所有伤害总和:", "以上所有技能伤害数据："+formatNumber(Long.parseLong(String.valueOf(totalDamage))));
            }
            if (totalDamage.compareTo(new BigDecimal(0)) != 0) {
                charJo.put(bn, jo3);
            }
            charJo.put("总计", formatNumber(Long.parseLong(String.valueOf(totalDamage))));
            result.put(currentCharacter.getName(), charJo);
        }
        return null;
    }

    /**
     * 开始计算技能以及乘区伤害
     *
     * @param oSkills
     * @param chars
     * @param rr
     * @param characterInfoSkills
     */
    private BigDecimal compute(List<Skill> oSkills, List<Character> chars,
                               HashSet<Behavior> behaviors1, JSONObject rr, CardVo vo1, Character cc,
                               List<CharacterInfoSkill> characterInfoSkills) {
        if (characterInfoSkills == null) {
            return new BigDecimal(0);
        }
        // 属性乘区直接计算就行  错误（需要修改，比如崔狗的技能是属性乘区加成
        String damageMethods = "";
        List<CharacterInfoSkill> modifyCiSkillList =
                skillPatch.extMemoryAttackAdd(characterInfoSkills, chars, behaviors1, vo1, cc);
        BigDecimal totalDamage = new BigDecimal(0);
        for (CharacterInfoSkill cis : modifyCiSkillList) {
            CardVo vo = new CardVo();
            BeanUtils.copyProperties(vo1, vo);
            // 每次循环都初始化一个新的行为状态
            HashSet<Behavior> behaviors = new HashSet<>(behaviors1);
            // 初始化该次攻击的行为
            if (StringUtils.isNotEmpty(cis.getBehavior())) {
                for (String ss : cis.getBehavior().split(",")) {
                    behaviors.add(init.getBehavior(Long.parseLong(ss)));
                }
            }
            skillPatch.enemyAttributeFilter(cis, vo);
            // 最后一次筛选技能  举例：1、排除不带召唤词条却享受了召唤词条加成的攻击模式2、秘密音律的连击技能
            ArrayList<Skill> skills = skillPatch.excludeSpecialBehaviorSkill(cis, oSkills, behaviors);
            // 部分角色攻击行为需要特殊处理
            JSONObject r = new JSONObject();
            // 未实现，为了添加属性乘区加成，暂时不修改
            ArrayList<Skill> attrList = new ArrayList<>();
            String attack = computeDataProcess.attackCompute(cc, skills, chars, behaviors, r, vo, attrList);
            String mastery = computeDataProcess.masteryCompute(cc, skills, chars, behaviors, r, vo, attrList);
            String health = computeDataProcess.healthCompute(cc, skills, chars, behaviors, r, vo, attrList);
            String crit = computeDataProcess.critCompute(cc, skills, chars, behaviors, r, vo, attrList);
            String debuff = computeDataProcess.debuffCompute(cc, skills, chars, behaviors, r, vo, cis);
            String damage = computeDataProcess.damageCompute(cc, skills, chars, behaviors, r, vo, cis);

            ArrayList<String> warnings = new ArrayList<>();
            for (String key : r.keySet()) {
                for (String k : r.getJSONObject(key).keySet()) {
                    if (k.contains("警告")) {
                        warnings.add(k);
                    }
                }
                JSONObject jo = r.getJSONObject(key);
                final JSONObject je = jo.clone();
                for (String k : jo.keySet()) {
                    if (k.contains("警告")) {
                        je.remove(k);
                    }
                    if (Objects.equals(jo.getString(k).split("_")[0], "0") ||Objects.equals(jo.getString(k).split("_")[0], "0.00") ) {
                        je.remove(k);
                    }
                }
                r.put(key, je);
            }
            String bn = cis.getSName();
            String compute = cis.getAttr();
            // 以下为技能加成计算过程
            ArrayList<BigDecimal> arr = new ArrayList<>();
            String attackName = "";
            String realJineng = "";
            int attackCount = 1;
            String 攻击频率 = "单次攻击";

            if (cis.getCount().contains("持续")) {
                attackCount = Integer.parseInt(cis.getCount().split("持续")[1]);
                攻击频率 = cis.getCount().split("持续")[0] + "秒内攻击" + attackCount + "次";
            } else {
                attackCount = Integer.parseInt(cis.getCount());
                攻击频率 = attackCount + "次攻击";
            }
            String[] sp = compute.split("_");

            for (int i = 0; i < sp.length; i++) {
                String d = sp[i];
                BigDecimal jineng = new BigDecimal("1");
                if (cis.getType().equals("异核")) {
                    int charFlowers = ArrayListUtils.getCharFlowers(cc.getCharacterId() + "", vo);
                    BigDecimal startt = new BigDecimal(cis.getValue().split("_")[i]);
                    if (charFlowers >= 3) {
                        startt = startt.add(
                                        new BigDecimal(3).multiply(new BigDecimal(cis.getAdd1().split("_")[i])))
                                .add(new BigDecimal(charFlowers - 3).multiply(
                                        new BigDecimal(cis.getAdd2().split("_")[i])));
                    } else {
                        startt = startt.add(
                                new BigDecimal(charFlowers).multiply(new BigDecimal(cis.getAdd1().split("_")[i])));
                    }
                    jineng = startt.divide(new BigDecimal(100), MathContext.DECIMAL32);
                } else {
                    // 这个10 是烛龙 12级的数据。直接X10级就行
                    String skillLevel = "10";
                    if (cc.getRank().equals("6星")) {
                        skillLevel = "12";
                    } else if (cc.getRank().equals("5星")) {
                        skillLevel = "11.5";
                    } else {
                        skillLevel = "11";
                    }
                    int flowers = ArrayListUtils.getCharFlowers(cc.getCharacterId() + "", vo);
                    if (flowers >= 4) {
                        if (cis.getCharSkillIndex() != 1 && flowers == 4) {
                            skillLevel = "10";
                        }
                    } else {
                        skillLevel = "10";
                    }
                    jineng = new BigDecimal(cis.getValue().split("_")[i]).add(
                                    new BigDecimal(cis.getAdd1().split("_")[i]).multiply(new BigDecimal(skillLevel)))
                            .divide(new BigDecimal(100), MathContext.DECIMAL32);
                }
                if (d.equals("体质")) {
                    BigDecimal multiply = new BigDecimal(health);
                    attackName += "体质(" + multiply + ")+";
                    multiply = multiply.multiply(jineng);
                    arr.add(multiply);
                    realJineng += "体质(" + jineng.multiply(new BigDecimal("100")) + "%)+";
                }
                if (d.equals("攻击")) {
                    BigDecimal multiply = new BigDecimal(attack);
                    attackName += "攻击(" + multiply + ")+";
                    multiply = multiply.multiply(jineng);
                    arr.add(multiply);
                    realJineng += "攻击(" + jineng.multiply(new BigDecimal("100")) + "%)+";
                }
                if (d.equals("专精")) {
                    BigDecimal multiply = new BigDecimal(mastery.split("_")[0]);
                    attackName += "专精(" + multiply + ")+";
                    multiply = multiply.multiply(jineng);
                    arr.add(multiply);
                    realJineng += "专精(" + jineng.multiply(new BigDecimal("100")) + "%)+";
                }
                if (d.equals("终端")) {
                    String s1 = vo.getEattribute().split(",")[4];
                    String s2 = vo.getAttribute().split(",")[4];
                    BigDecimal multiply = new BigDecimal(s1).add(new BigDecimal(s2));
                    attackName += "终端(" + multiply + ")+";
                    multiply = multiply.multiply(jineng);
                    arr.add(multiply);
                    realJineng += "终端(" + jineng.multiply(new BigDecimal("100")) + "%)+";
                }
            }

            BigDecimal addMastery = new BigDecimal(mastery.split("_")[1]).add(new BigDecimal("1"));
            attackName = attackName.substring(0, attackName.length() - 1);
            r.put("attackCount", 攻击频率);
            r.put("finalCompute",
                    bn + ":攻击次数(" + attackCount + ") x 真实倍率(" +
                            realJineng.substring(0, realJineng.length() - 1) + ") * 加成区(" + attackName +
                            ") x 减益区(" + debuff +
                            ") x 暴击区(" + crit +
                            ") x 增伤区(" +
                            damage +
                            ") x 专精增伤区(" + addMastery.toPlainString() + ")");
            BigDecimal total = new BigDecimal("0");
            for (BigDecimal aa : arr) {
                total = total.add(aa.multiply(new BigDecimal(attackCount)).multiply(new BigDecimal(debuff))
                        .multiply(new BigDecimal(crit))
                        .multiply(new BigDecimal(damage)).multiply(addMastery)
                        .setScale(0, RoundingMode.DOWN));
                try {
                    String[] c = vo.getDjs().split(",");
                    if (c.length != 0 && !"0".equals(c[0])) {
                        for (String js : c) {
                            int i = Integer.parseInt(js);
                            total = total.multiply(new BigDecimal((100 - i) / 100.00));
                            warnings.add("敌方拥有为:" + i + "% 独立减伤");
                        }
                    }
                } catch (Exception e) {
                }
            }
            total = total.setScale(0, RoundingMode.DOWN);
            totalDamage = totalDamage.add(total);
            r.put("suggesion", warnings);
            r.put("finalDamage", total.toPlainString());
            damageMethods +=
                    (cis.getSName() + ":" + 攻击频率 + " " + realJineng.substring(0, realJineng.length() - 1) + "、");
            damageMethods += "此技能伤害:" + formatNumber(Long.parseLong(String.valueOf(total))) + "、";
            rr.put(cis.getSName(), r);
        }
        if (StringUtils.isNotEmpty(damageMethods)) {
            rr.put("总结:技能伤害概览:", damageMethods.substring(0, damageMethods.length() - 1));
        }
        return totalDamage;
    }

    /**
     * 修复初始化数据
     *
     * @param vo
     */
    private void dataFix(CardVo vo) {
        // 修复数据
        if (vo.getSmsg() < 0) {
            vo.setSmsg((byte) 0);
        }
        if (vo.getRLevel() >= 10) {
            vo.setRLevel((byte) 10);
        }
        if (vo.getRLevel() < 0) {
            vo.setRLevel((byte) 0);
        }
        if (vo.getDfy() > 100L) {
            vo.setDfy(100L);
        }
        if (vo.getDfy() < 0) {
            vo.setDfy(0L);
        }
        int clength = vo.getCharacters().split(",").length;
        //修复潜能数据
        int pllevels = vo.getPLevel().split(",").length;
        StringBuilder ff = new StringBuilder(vo.getFlower());
        if (pllevels < clength) {
            ff.append(",0".repeat(clength - pllevels));
            vo.setFlower(ff.toString());
        }
        // 修复开花数据
        int flowerLength = vo.getFlower().split(",").length;
        // 如果开花数据跟角色数量不同步，修复
        if (flowerLength < clength) {
            ff = new StringBuilder(vo.getFlower());
            ff.append(",5".repeat(clength - flowerLength));
            vo.setFlower(ff.toString());
        }
        ff = new StringBuilder();
        for (String s : vo.getFlower().split(",")) {
            try {
                ff.append(Integer.parseInt(s)).append(",");
            } catch (Exception e) {
                ff.append(5 + ",");
            }
        }
        vo.setFlower(ff.substring(0, ff.length() - 1));
    }

    /**
     * 初始化角色以及烙痕数据
     *
     * @param characters
     * @param memories
     * @param vo
     */
    private void initCharAndMemoryData(ArrayList<Character> characters, ArrayList<Memory> memories, CardVo vo) {
        for (String id : vo.getCharacters().split(",")) {
            characters.add(init.getCharacter(Long.parseLong(id)));
        }
        for (String id : vo.getMemorys().split(",")) {
            memories.add(init.getMemory(Long.parseLong(id)));
        }
    }

    private Map<Long, List<CharacterInfoSkill>> initCharacterInfoSkillBehavior(ArrayList<Character> characters) {
        // 初始化角色自带的攻击行为
        List<CharacterInfoSkill> list =
                init.getCharacterInfoSkills(characters.stream().map(Character::getCharacterId).toList());
        Map<Long, List<CharacterInfoSkill>> collect =
                list.stream().collect(Collectors.groupingBy(CharacterInfoSkill::getCId));
        // 初始化所有同调者的攻击元素
        for (Character c : characters) {
            List<CharacterInfoSkill> characterInfoSkills = collect.get(c.getCharacterId());
            if (characterInfoSkills != null && !characterInfoSkills.isEmpty()) {
                List<String> elements =
                        characterInfoSkills.stream().map(CharacterInfoSkill::getElement).toList();
                c.setAttackElement(StringUtils.join(elements, ","));
            }
        }
        return collect;
    }

    private HashSet<Behavior> initCurrentCharacterBehavior(Character currentCharacter, int charIndex, CardVo vo,
                                                           ArrayList<Character> characters, ArrayList<Memory> memories,
                                                           Map<Long, List<CharacterInfoSkill>> collect) {
        HashSet<Behavior> behaviors = new HashSet<>();
        // 初始化技能中附带的个人行为
        String charBehaviors = init.getCharBehaviors(currentCharacter.getCharacterId(), charIndex == 0);
        if (StringUtils.isNotEmpty(charBehaviors)) {
            for (String ss : charBehaviors.split(",")) {
                behaviors.add(init.getBehavior(Long.parseLong(ss)));
            }
        }
        for (Character character : characters) {
            boolean leader = false;
            if (charIndex == 0) {
                leader = Objects.equals(character.getCharacterId(), currentCharacter.getCharacterId());
            }
            String beh = init.getCharBehaviors(character.getCharacterId(), leader);
            if (StringUtils.isNotEmpty(beh)) {
                // 群体自回血
                if (beh.contains("1027")) {
                    behaviors.add(init.getBehavior(1021L));
                }
                //群体隐身
                if (beh.contains("1026")) {
                    // 如果是伊朗相思的群体隐身，那么就排除自己
                    if (currentCharacter.getCharacterId() != 4009L) {
                        behaviors.add(init.getBehavior(1056L));
                    } else {
                        if (!currentCharacter.getCharacterId().equals(character.getCharacterId())) {
                            behaviors.add(init.getBehavior(1056L));
                        }
                    }
                }
            }
        }
        // 添加这个角色的职业行为
        behaviors.add(init.getBehavior(Long.parseLong(init.getRoleToBehavior(currentCharacter.getRole()))));
        // 初始化高级行为，比如消融 等
        behaviors = computeElementBehaviorAndEffect.initHignBehavior(characters, memories, vo, currentCharacter,
                behaviors,
                collect);
        // 添加所有攻击类型附带的行为
        if (currentCharacter.getAttackElement() != null) {
            for (String s : currentCharacter.getAttackElement().split(",")) {
                Behavior behavior = new Behavior();
                behavior.setBehaviorId(init.getElementBehavior(s));
                behaviors.add(behavior);
            }
        }

        return behaviors;
    }

    private static String formatNumber(long num) {
        // 将数字转换为字符串
        String numStr = Long.toString(num);

        // 如果数字小于1000，直接返回
        if (numStr.length() <= 3) {
            return numStr;
        }

        // 使用StringBuilder来构建最终的字符串
        StringBuilder formattedNum = new StringBuilder();

        // 从字符串末尾开始处理，每隔三位插入一个逗号
        for (int i = numStr.length() - 1, j = 1; i >= 0; i--, j++) {
            if (j > 1 && j % 3 == 1) {
                formattedNum.insert(0, ',');
            }
            formattedNum.insert(0, numStr.charAt(i));
        }

        return formattedNum.toString();
    }

    // 这个是预览属性乘区，不一定准确
    private OverviewDataVo overview(ArrayList<Skill> rSkills, Character character, CardVo vo,
                                    HashSet<Behavior> behaviors, String mastery) {
        List<Long> list = behaviors.stream().map(Behavior::getBehaviorId).toList();
        OverviewDataVo r = new OverviewDataVo();
        HashMap<String, BigDecimal> vls = new HashMap<>();
        for (Skill skill : rSkills) {
            boolean max = ComputeDataProcess.commonJudgeMaxValue(skill, list, character, vo, new CharacterInfoSkill());
            String damage = skill.getDamage();
            BigDecimal old = vls.get(skill.getDamage());
            if (old == null) {
                old = new BigDecimal(0);
                vls.put(damage, old);
            }
            String s = max ? skill.getMaxValue() : skill.getMinValue();
            if (StringUtils.isEmpty(s)) {
                s = skill.getMinValue();
            }
            BigDecimal val = new BigDecimal(s);
            BigDecimal oval = val;
            if (oval.compareTo(new BigDecimal("0")) == 0) {
                continue;
            }
            switch (damage) {
                case "101":
                    val = old.add(val);
                    packOverviewVo(r.get激励增伤(), val, oval, skill);
                    break;
                case "103":
                    val = old.add(val);
                    packOverviewVo(r.get技能增伤(), val, oval, skill);
                    break;
                case "105":
                    val = old.add(val);
                    packOverviewVo(r.get额外增伤(), val, oval, skill);
                    break;
                case "106":
                    if (old.compareTo(new BigDecimal(0)) == 0) {
                        old = new BigDecimal(1);
                    }
                    val = val.add(new BigDecimal("1")).multiply(old);
                    packOverviewVo(r.get独立增伤(), val, oval, skill);
                    break;
                case "107":
                    if (old.compareTo(new BigDecimal(0)) == 0) {
                        old = new BigDecimal(1);
                    }
                    val = val.add(new BigDecimal("1")).multiply(old);
                    packOverviewVo(r.get暴击独立增伤(), val, oval, skill);
                    break;
                case "201":
                    val = old.add(val);
                    packOverviewVo(r.get刻印专精百分比(), val, oval, skill);
                    break;
                case "202":
                    val = old.add(val);
                    packOverviewVo(r.get刻印专精固定值(), val, oval, skill);
                    break;
                case "203":
                    if (val.compareTo(old) < 1) {
                        val = old;
                    }
                    packOverviewVo(r.get同调者专精百分比(), val, oval, skill);
                    break;
                case "204":
                    val = old.add(val);
                    packOverviewVo(r.get刻印攻击增加值(), val, oval, skill);
                    break;
                case "205":
                    val = old.add(val);
                    packOverviewVo(r.get刻印攻击百分比(), val, oval, skill);
                    break;
                case "207":
                    val = old.add(val);
                    packOverviewVo(r.get额外攻击百分比(), val, oval, skill);
                    break;
                case "209":
                    val = old.add(val);
                    packOverviewVo(r.get暴击伤害(), val, oval, skill);
                    break;
                case "210":
                    if (val.compareTo(old) < 1) {
                        val = old;
                    }
                    packOverviewVo(r.get同调者暴击率(), val, oval, skill);
                    break;
                case "211":
                    val = old.add(val);
                    packOverviewVo(r.get暴击率(), val, oval, skill);
                    break;
                case "212":
                    val = old.add(val);
                    packOverviewVo(r.get刻印体质百分比加成(), val, oval, skill);
                    break;
                case "214":
                    val = old.add(val);
                    packOverviewVo(r.get攻击力加成(), val, oval, skill);
                    break;
                case "215":
                    if (val.compareTo(old) < 1) {
                        val = old;
                    }
                    packOverviewVo(r.get同调者攻击加成(), val, oval, skill);
                    break;
                case "216":
                    val = old.add(val);
                    packOverviewVo(r.get专精百分比加成(), val, oval, skill);
                    break;
                case "301":
                    val = old.add(val);
                    packOverviewVo(r.get基础防御降低(), val, oval, skill);
                    break;
                case "302":
                    val = old.add(val);
                    packOverviewVo(r.get目标受伤害增加(), val, oval, skill);
                    break;
                case "303":
                    if (val.compareTo(old) < 1) {
                        val = old;
                    }
                    packOverviewVo(r.get元素易伤(), val, oval, skill);
                    break;
                case "304":
                    val = old.add(val);
                    packOverviewVo(r.get抗性降低(), val, oval, skill);
                    break;
                case "305":
                    val = old.add(val);
                    packOverviewVo(r.get无视防御(), val, oval, skill);
                    break;
            }
            vls.put(damage, val);
        }
        // 部分属性直接合并处理
        OverviewDataVo.CData r1 = r.get同调者专精百分比();
        OverviewDataVo.CData rr = r.get专精百分比加成();
        rr.setTotal(new BigDecimal(rr.getTotal()).add(new BigDecimal(r1.getTotal())).toPlainString());
        rr.getDetail().addAll(r1.getDetail());
        r.set同调者专精百分比(null);

        r1 = r.get同调者攻击加成();
        rr = r.get攻击力加成();
        rr.setTotal(new BigDecimal(rr.getTotal()).add(new BigDecimal(r1.getTotal())).toPlainString());
        rr.getDetail().addAll(r1.getDetail());
        r.set同调者攻击加成(null);

        r1 = r.get同调者暴击率();
        rr = r.get暴击率();
        rr.setTotal(new BigDecimal(rr.getTotal()).add(new BigDecimal(r1.getTotal())).toPlainString());
        rr.getDetail().addAll(r1.getDetail());
        rr.setLimit("1");
        r.set同调者暴击率(null);

        r1 = r.get元素易伤();
        rr = r.get目标受伤害增加();
        rr.setTotal(new BigDecimal(rr.getTotal()).add(new BigDecimal(r1.getTotal())).toPlainString());
        rr.getDetail().addAll(r1.getDetail());
        rr.setLimit("1.5");
        r.set元素易伤(null);

        r1 = r.get无视防御();
        rr = r.get基础防御降低();
        rr.setTotal(new BigDecimal(rr.getTotal()).add(new BigDecimal(r1.getTotal())).toPlainString());
        rr.getDetail().addAll(r1.getDetail());
        rr.setLimit("1");
        r.set无视防御(null);

        rr = r.get抗性降低();
        rr.setLimit("1");

        rr = r.get额外攻击百分比();
        rr.setLimit("1.2");

        rr = r.get激励增伤();
        rr.setLimit("2");

        r.get专精增伤().setTotal(mastery);
        r.get专精增伤().setLimit("50");
        SyncData sync = vo.getSync();
        if (sync != null) {
            if (sync.get当前刻印攻击百分比().compareTo(new BigDecimal(r.get刻印攻击百分比().getTotal())) > 0) {
                r.get刻印攻击百分比().setTotal(sync.get当前刻印攻击百分比().toPlainString());
                r.get刻印攻击百分比().getDetail().add("同调_" + sync.get当前刻印攻击百分比().toPlainString());
                r.get刻印攻击增加值().setTotal("0");
            }
            if (sync.get当前额外攻击().compareTo(new BigDecimal(r.get额外攻击百分比().getTotal())) > 0) {
                r.get额外攻击百分比().setTotal(sync.get当前额外攻击().toPlainString());
                r.get额外攻击百分比().getDetail().add("同调_" + sync.get当前额外攻击().toPlainString());
            }
            if (sync.get当前刻印专精().compareTo(new BigDecimal(r.get刻印专精百分比().getTotal())) > 0) {
                r.get刻印专精百分比().setTotal(sync.get当前刻印专精().toPlainString());
                r.get刻印专精百分比().getDetail().add("同调_" + sync.get当前刻印专精().toPlainString());
            }
            if (sync.get当前暴击率().compareTo(new BigDecimal(r.get暴击率().getTotal())) > 0) {
                r.get暴击率().setTotal(sync.get当前暴击率().toPlainString());
                r.get暴击率().getDetail().add("同调_" + sync.get当前暴击率().toPlainString());
            }
            if (sync.get当前暴击伤害().compareTo(new BigDecimal(r.get暴击伤害().getTotal())) > 0) {
                r.get暴击伤害().setTotal(sync.get当前暴击伤害().toPlainString());
                r.get暴击伤害().getDetail().add("同调_" + sync.get当前暴击伤害().toPlainString());
            }
        }

        return r;
    }

    private void packOverviewVo(OverviewDataVo.CData c, BigDecimal add, BigDecimal oval, Skill skill) {
        c.setTotal(add.toPlainString());
        c.getDetail().add(skill.getName() + "_" + oval);
    }

    // 暂时不需要使用 这里收集的是计算完成后的数据
    private JSONObject compare(JSONObject init) {
        JSONObject r = new JSONObject();
        OverviewDataVo vo = new OverviewDataVo();
        JSONArray arr = new JSONArray();
        arr.add(init.getJSONObject("attackInfo"));
        arr.add(init.getJSONObject("masteryInfo"));
        arr.add(init.getJSONObject("healthInfo"));
        arr.add(init.getJSONObject("debuffInfo"));
        arr.add(init.getJSONObject("critInfo"));
        arr.add(init.getJSONObject("buffInfo"));
        for (Object ar : arr) {
            JSONObject ar1 = (JSONObject) ar;
            for (String k : ar1.keySet()) {
                String item = ar1.getString(k);
                String value = "0";
                String[] itemSplit = item.split("_");
                // 标准是3个字段用下划线分割，例： 伤害_来源_技能ID
                if (itemSplit.length == 3) {
                    try {
                        long id = Long.parseLong(itemSplit[2]);
//                        if (id < 400000L) {
                        value = itemSplit[0];
//                        }
                    } catch (Exception e) {
                        value = "0";
                    }
                }
                if ("0".equals(value)) {
                    continue;
                }
                if (k.contains("刻印攻击增加值")) {
                    OverviewDataVo.CData c = vo.get刻印攻击增加值();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("刻印攻击百分比")) {
                    OverviewDataVo.CData c = vo.get刻印攻击百分比();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("额外攻击百分比")) {
                    OverviewDataVo.CData c = vo.get额外攻击百分比();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("额外攻击增加值")) {
                    OverviewDataVo.CData c = vo.get额外攻击增加值();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("攻击力加成")) {
                    OverviewDataVo.CData c = vo.get攻击力加成();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("同调者攻击加成")) {
                    OverviewDataVo.CData c = vo.get同调者攻击加成();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("刻印体质百分比加成")) {
                    OverviewDataVo.CData c = vo.get刻印体质百分比加成();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("专精百分比加成")) {
                    OverviewDataVo.CData c = vo.get专精百分比加成();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("同调者专精百分比")) {
                    OverviewDataVo.CData c = vo.get同调者专精百分比();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("刻印专精百分比")) {
                    OverviewDataVo.CData c = vo.get刻印专精百分比();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("刻印专精固定值")) {
                    OverviewDataVo.CData c = vo.get刻印专精固定值();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("暴击伤害")) {
                    OverviewDataVo.CData c = vo.get暴击伤害();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("同调者暴击率")) {
                    OverviewDataVo.CData c = vo.get同调者暴击率();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("暴击率")) {
                    OverviewDataVo.CData c = vo.get暴击率();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("激励增伤")) {
                    OverviewDataVo.CData c = vo.get激励增伤();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("技能增伤")) {
                    OverviewDataVo.CData c = vo.get技能增伤();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("额外增伤")) {
                    OverviewDataVo.CData c = vo.get额外增伤();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("独立增伤")) {
                    OverviewDataVo.CData c = vo.get独立增伤();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("暴击增伤")) {
                    OverviewDataVo.CData c = vo.get暴击独立增伤();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("防御降低")) {
                    OverviewDataVo.CData c = vo.get基础防御降低();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("无视防御")) {
                    OverviewDataVo.CData c = vo.get无视防御();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("易伤")) {
                    OverviewDataVo.CData c = vo.get目标受伤害增加();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("元素易伤")) {
                    OverviewDataVo.CData c = vo.get元素易伤();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                } else if (k.contains("抗性降低")) {
                    OverviewDataVo.CData c = vo.get抗性降低();
                    BigDecimal add = new BigDecimal(value).add(new BigDecimal(c.getTotal()));
                    c.setTotal(add.toString());
                    c.getDetail().add(itemSplit[0] + "_" + itemSplit[1]);
                }
            }
        }

        r.put("compareList", vo);
        r.put("attackCount", init.getString("attackCount"));
        r.put("finalCompute", init.getString("finalCompute"));
        r.put("suggesion", init.get("suggesion"));
        r.put("finalDamage", init.getString("finalDamage"));
        return r;
    }

}
