package info.qianqiu.ashechoes.init;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import info.qianqiu.ashechoes.config.env.EnvConfig;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.*;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.service.*;
import info.qianqiu.ashechoes.utils.http.ReqUtils;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: 南
 * @desc: 初始化伤害乘区、角色、烙痕
 * 注：获取数据的时候最好使用复制的数据，不要直接使用本类里面的数据，避免修改源数据
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class InitComputeData implements CommandLineRunner {

    // 调用的时候不要更改里面的属性
    private final LinkedHashMap<Long, Character> character = new LinkedHashMap<>();
    private final LinkedHashMap<String, Character> characterByName = new LinkedHashMap<>();
    private final LinkedHashMap<String, String> characterAvatar = new LinkedHashMap<>();
    private final ArrayList<Character> simpleCharacter = new ArrayList<>();
    private final LinkedHashMap<String, Character> simpleCharacterByNameGroup = new LinkedHashMap<>();
    private final HashMap<Long, String> characterAllAttackBehavor = new HashMap<>();
    private final LinkedHashMap<Long, Memory> memory = new LinkedHashMap<>();
    private final LinkedHashMap<String, String> memoryAvatar = new LinkedHashMap<>();
    private final LinkedHashMap<String, Memory> simpleMemoryByNameGroup = new LinkedHashMap<>();
    private final LinkedHashMap<Long, Skill> skill = new LinkedHashMap<>();
    private final LinkedHashMap<String, ArrayList<String>> skillAvatar = new LinkedHashMap<>();
    private final LinkedHashMap<String, ArrayList<Skill>> simpleSkillByNameGroup = new LinkedHashMap<>();
    private final LinkedHashMap<Long, Damage> damage = new LinkedHashMap<>();
    private final LinkedHashMap<Long, ArrayList<SkillMemory>> skillMemory = new LinkedHashMap<>();
    private final ArrayList<String> originSkillFrom = new ArrayList<>();
    private final LinkedHashMap<Long, ArrayList<SkillCharacter>> charactersSkill = new LinkedHashMap<>();
    private final HashMap<Long, Behavior> behavior = new HashMap<>();
    private final HashMap<String, String> roleToBehavior = new HashMap<>();
    private final HashMap<String, Long> elementBehavior = new HashMap<>();
    private final ArrayList<Thanks> thanks = new ArrayList<>();
    private final ArrayList<CharacterInfoSkill> characterInfoSkills = new ArrayList<>();

    private final String ASSETS_URL = SpringUtil.getBean(EnvConfig.class).getAssetsUrl();
    private final String ASSETS_VERSION = SpringUtil.getBean(EnvConfig.class).getAssetsVersion();

    public JSONObject poolData;
    public JSONObject charsData;
    public JSONObject memoryData;
    public JSONObject localUpChar;

    private final CharacterService characterService;
    private final MemoryService memoryService;
    private final SkillService skillService;
    private final SkillMemoryService skillMemoryService;
    private final SkillCharacterService skillCharacterService;
    private final BehaviorService behaviorService;
    private final ThanksService thanksService;
    private final DamageService damageService;
    private final CharacterInfoSkillService characterInfoSkillService;

    public void destroy() {
        character.clear();
        characterAllAttackBehavor.clear();
        characterByName.clear();
        roleToBehavior.clear();
        simpleCharacterByNameGroup.clear();
        simpleMemoryByNameGroup.clear();
        simpleSkillByNameGroup.clear();
        simpleCharacter.clear();
        characterInfoSkills.clear();
        memory.clear();
        skill.clear();
        damage.clear();
        behavior.clear();
        skillMemory.clear();
        originSkillFrom.clear();
        charactersSkill.clear();
        thanks.clear();
        localUpChar.clear();
        characterAvatar.clear();
        memoryAvatar.clear();
        skillAvatar.clear();
        log.error("数据销毁");
    }

    public void init() {
        initDamage(damageService.list());
        initThanks(thanksService.list());
        initCharacter(
                characterService.list(new LambdaQueryWrapper<Character>().orderByDesc(Character::getRank)
                        .orderByDesc(Character::getCharacterId)));
        initSkill(skillService.list());
        initSkillMemory(skillMemoryService.list());
        initMemory(memoryService.list(new LambdaQueryWrapper<Memory>().orderByDesc(Memory::getMemoryId)));
        initBehavior(behaviorService.list());
        initSkillCharacter(skillCharacterService.list());
        initCharacterInfoSkill(characterInfoSkillService.list());
        initCharacterAllAttackBehavor();
        initWebPoolDataInfo();
        initCommonAttrBehavior();
        log.error("数据初始化成功");
    }

    public List<Thanks> getAllThanks() {
        return Collections.unmodifiableList(thanks);
    }

    /**
     * @param cids 如果传Cid，就返回对应Cid的列表
     * @return
     */
    public List<CharacterInfoSkill> getCharacterInfoSkills(List<Long> cids) {
        if (cids != null && !cids.isEmpty()) {
            return characterInfoSkills.stream().filter(e -> cids.contains(e.getCId())).toList();
        }
        return Collections.unmodifiableList(characterInfoSkills);
    }

    public List<Skill> getAllSkill() {
        List<Skill> list = skill.values().stream().toList();
        return list;
    }

    public Long getElementBehavior(String vv) {
        return elementBehavior.get(vv);
    }

    public String getRoleToBehavior(String v) {
        return roleToBehavior.get(v);
    }

    public List<String> getSkillAvatar(String skillName) {
        return Collections.unmodifiableList(skillAvatar.get(skillName));
    }

    public String getMemoryAvatar(String name) {
        return memoryAvatar.get(name);
    }

    public String getCharacterAvatar(String name) {
        return characterAvatar.get(name);
    }

    public List<Character> getAllSimpleCharavter() {
        return Collections.unmodifiableList(simpleCharacter);
    }

    public List<Memory> getAllMemory() {
        return memory.values().stream().toList();
    }

    public Character getSimpleCharacterByNameGroup(String name) {
        Character character1 = simpleCharacterByNameGroup.get(name);
        Character character2 = new Character();
        BeanUtils.copyProperties(character1, character2);
        return character2;
    }

    public Memory getSimpleMemoryByNameGroup(String name) {
        Memory memory1 = simpleMemoryByNameGroup.get(name);
        Memory memory2 = new Memory();
        BeanUtils.copyProperties(memory1, memory2);
        return memory2;
    }

    public List<Skill> getSimpleSkillByNameGroup(String name) {
        List<Skill> source = simpleSkillByNameGroup.get(name);
        ArrayList<Skill> skills = new ArrayList<>();
        for (Skill skill : source) {
            Skill skill1 = new Skill();
            BeanUtils.copyProperties(skill, skill1);
            skills.add(skill1);
        }
        return skills;
    }

    public String getCharBehaviors(Long cid, boolean leader) {
        Character ccc = getCharacter(cid);
        String beids = "";
        String beid = characterAllAttackBehavor.get(cid);
        // 获取通用行为信息
        if (StringUtils.isNotEmpty(beid)) {
            beids = beid;
        }
        // 获取增益buff行为信息
        List<Skill> characterSelfSkill = getCharacterSelfSkill(cid);
        if (!characterSelfSkill.isEmpty()) {
            beids += "," + characterSelfSkill.stream().map(Skill::getAddBehavior).collect(Collectors.joining(","));
        }
        if (leader) {
            List<Skill> characterLeaderSkill = getCharacterLeaderSkill(cid);
            if (!characterLeaderSkill.isEmpty()) {
                beids +=
                        "," + characterLeaderSkill.stream().map(Skill::getAddBehavior).collect(Collectors.joining(","));
            }
        }
        String t = "";
        for (String s : beids.split(",")) {
            try {
                long l = Long.parseLong(s);
                t += l + ",";
            } catch (Exception e) {
            }
        }
        String s = roleToBehavior.get(ccc.getRole());
        t += s;
        return t;
    }

    public String getDamageInfo(String ids) {
        List<Damage> list = damage.values().stream().filter(e -> ids.contains(e.getDamageId() + "")).toList();
        String damaged = "";
        if (list.isEmpty()) {
            return damaged;
        }
        for (Damage d : list) {
            damaged += d.getName() + ",";
        }
        return damaged.substring(0, damaged.length() - 1);
    }

    public Character getCharacter(long id) {
        Character character1 = character.get(id);
        Character character2 = new Character();
        BeanUtils.copyProperties(character1, character2);
        return character2;
    }

    public Character getCharacterByName(String name) {
        Character character1 = characterByName.get(name);
        Character character2 = new Character();
        BeanUtils.copyProperties(character1, character2);
        return character2;
    }

    public Memory getMemory(long id) {
        Memory memory1 = memory.get(id);
        Memory memory2 = new Memory();
        BeanUtils.copyProperties(memory1, memory2);
        return memory2;
    }

    public Skill getSkill(long id) {
        Skill source = skill.get(id);
        Skill copy = new Skill();
        BeanUtils.copyProperties(source, copy);
        return copy;
    }

    public Behavior getBehavior(long id) {
        Behavior source = behavior.get(id);
        Behavior copy = new Behavior();
        BeanUtils.copyProperties(source, copy);
        return copy;
    }

    public Memory[] getMemorys(String[] id) {
        Memory[] arr = new Memory[id.length];
        for (int i = 0; i < id.length; i++) {
            arr[i] = memory.get(Long.valueOf(id[i]));
        }
        return arr;
    }

    public Character[] getCharacters(String[] id) {
        Character[] arr = new Character[id.length];
        for (int i = 0; i < id.length; i++) {
            Character character1 = getCharacter(Long.valueOf(id[i]));
            Character c = new Character();
            BeanUtils.copyProperties(character1, c);
            arr[i] = c;
        }
        return arr;
    }

    // 修改这里面的技能数据不会影响外面
    public List<Skill> getSkill(List<Long> id) {
        ArrayList<Skill> arr = new ArrayList<>();
        for (int i = 0; i < id.size(); i++) {
            Skill source = skill.get(id.get(i));
            Skill copy = new Skill();
            BeanUtils.copyProperties(source, copy);
            arr.add(copy);
        }
        return arr;
    }

    public List<Skill> getCharacterSelfSkill(long id) {
        List<SkillCharacter> skillCharacters = charactersSkill.get(id);
        if (skillCharacters == null) {
            return new ArrayList<>();
        }
        List<Long> list =
                skillCharacters.stream().filter(e -> e.getLeader() != 1).map(SkillCharacter::getSkillId).toList();

        return getSkill(list);
    }

    public List<Skill> getCharacterLeaderSkill(long id) {
        List<SkillCharacter> skillCharacters = charactersSkill.get(id);
        if (skillCharacters == null) {
            return new ArrayList<>();
        }
        List<Long> list =
                skillCharacters.stream().filter(e -> e.getLeader() == 1).map(SkillCharacter::getSkillId).toList();

        return getSkill(list);
    }

    /**
     * 查询该技能是那个同调者的
     *
     * @param skillId
     * @return
     */
    public Character getSkillFromCharacter(Long skillId) {
        List<String> list = originSkillFrom.stream().filter(e -> e.indexOf(skillId + "_c_") == 0).toList();
        if (list.isEmpty()) {
            Character character1 = new Character();
            character1.setCharacterId(0L);
            return character1;
        }
        return getCharacter(Long.parseLong(list.getFirst().split("_c_")[1]));
    }

    // 描述该技能名称，以及来源烙痕|角色后缀
    public String getSkillNameDesc(Long skillId, CardVo vo) {
        List<String> list = originSkillFrom.stream().filter(e -> e.indexOf(skillId + "") == 0).toList();
        String skName = "";
        String remark = "";
        for (String li : list) {
            if (StringUtils.isEmpty(li)) {
                continue;
            }
            if (li.contains("_c_")) {
                skName = skill.get(Long.parseLong(li.split("_c_")[0])).getName();
                Character c = getCharacter(Long.parseLong(li.split("_c_")[1]));
                if (vo.getCharacters().contains(c.getCharacterId() + "")) {
                    remark += c.getName() + ",";
                }
            } else {
                skName = skill.get(Long.parseLong(li.split("_m_")[0])).getName();
                Memory c = memory.get(Long.parseLong(li.split("_m_")[1]));
                if (vo.getMemorys().contains(c.getMemoryId() + "")) {
                    remark += c.getName() + ",";
                }
            }
        }
        if (!remark.isEmpty()) {
            remark = remark.substring(0, remark.length() - 1);
        }
        if (remark.isEmpty() && skName.isEmpty()) {
            return "";
        }
        return skName + "(" + remark + ")";
    }

    /**
     * 查询该技能所属角色以及烙痕
     */
    public JSONObject getSkillFromCharAndMemory(String skillName) {
        JSONObject jo = new JSONObject();
        ArrayList<Character> characters = new ArrayList<>();
        ArrayList<Memory> memories = new ArrayList<>();
        skillName = skillName.trim();
        // 先将名称转为id
        ArrayList<Skill> sk = simpleSkillByNameGroup.get(skillName);

        List<String> list =
                originSkillFrom.stream().filter(e -> e.indexOf(sk.getFirst().getSkillId() + "") == 0).toList();

        for (String s : list) {
            if (s.contains("_c_")) {
                Character c = getCharacter(Long.parseLong(s.split("_c_")[1]));
                c.setSkills(null);
                c.setAttack(null);
                c.setHealth(null);
                c.setMastery(null);
                c.setEHealth(null);
                c.setEMastery(null);
                c.setEAttack(null);
                characters.add(c);
            } else if (s.contains("_m_")) {
                Memory memory1 = getMemory(Long.parseLong(s.split("_m_")[1]));
                memory1.setSkills(null);
                memories.add(memory1);
            }
        }

        jo.put("memory", memories);
        jo.put("chars", characters);

        return jo;
    }

    private void initThanks(List<Thanks> t) {
        thanks.addAll(t);
    }

    private void initBehavior(List<Behavior> list) {
        Map<Long, List<Behavior>> collect = list.stream().collect(Collectors.groupingBy(Behavior::getBehaviorId));
        for (Long id : collect.keySet()) {
            behavior.put(id, collect.get(id).getFirst());
        }
    }

    private void initSkill(List<Skill> list) {
        for (Skill d : list) {
            if (d != null) {
                d.setIcons(ASSETS_URL + "skill/icons/" + d.getSkillId() + ".png?v=" + ASSETS_VERSION);
                skill.put(d.getSkillId(), d);
                ArrayList<String> nans = skillAvatar.get(d.getName());
                if (nans == null) {
                    nans = new ArrayList<>();
                }
                nans.add(d.getIcons());
                skillAvatar.put(d.getName(), nans);
                ArrayList<Skill> skills = simpleSkillByNameGroup.get(d.getName());
                if (skills == null) {
                    skills = new ArrayList<>();
                }
                skills.add(d);
                simpleSkillByNameGroup.put(d.getName(), skills);
            }
        }

    }

    private void initSkillMemory(List<SkillMemory> list) {
        Map<Long, List<SkillMemory>> collect = list.stream().collect(Collectors.groupingBy(SkillMemory::getMemoryId));
        for (long d : collect.keySet()) {
            skillMemory.put(d, (ArrayList<SkillMemory>) collect.get(d));
        }

        for (SkillMemory s : list) {
            originSkillFrom.add(s.getSkillId() + "_m_" + s.getMemoryId());
        }
    }

    private void initSkillCharacter(List<SkillCharacter> list) {
        for (SkillCharacter sc : list) {
            originSkillFrom.add(sc.getSkillId() + "_c_" + sc.getCharId());
        }
        Map<Long, List<SkillCharacter>> collect =
                list.stream().collect(Collectors.groupingBy(SkillCharacter::getCharId));
        for (long d : collect.keySet()) {
            charactersSkill.put(d, (ArrayList<SkillCharacter>) collect.get(d));
        }
    }

    private void initCharacter(List<Character> list) {
        for (Character d : list) {
            d.setAvatar(ASSETS_URL + "char/avatar/" + d.getCharacterId() + ".png?v=" + ASSETS_VERSION);
            d.setImg(ASSETS_URL + "char/" + d.getCharacterId() + "_s4.png?v=" + ASSETS_VERSION);
            d.setPic(ASSETS_URL + "char/normal/" + d.getCharacterId() + ".png?v=" + ASSETS_VERSION);
            character.put(d.getCharacterId(), d);
            characterByName.put(d.getName(), d);
            Character simple = new Character();
            simple.setAvatar(d.getAvatar());
            simple.setCharacterId(d.getCharacterId());
            simple.setElement(d.getElement());
            simple.setRole(d.getRole());
            simple.setName(d.getName());
            simple.setRank(d.getRank());
            simple.setShape(d.getShape());
            characterAvatar.put(d.getName(), d.getAvatar());
            simpleCharacter.add(simple);
            simpleCharacterByNameGroup.put(d.getName(), simple);
        }
    }

    private void initMemory(List<Memory> list) {
        for (Memory d : list) {
            List<SkillMemory> sk = skillMemory.get(d.getMemoryId());
            d.setImg(ASSETS_URL + "memory/origin/" + d.getMemoryId() + ".png?v=" + ASSETS_VERSION);
            if (sk != null) {
                ArrayList<Skill> o = new ArrayList<>();
                ArrayList<String> existNames = new ArrayList<>();
                for (SkillMemory sm : sk) {
                    Skill ss = skill.get(sm.getSkillId());
                    if (!"0".equals(ss.getDamage()) && !"9999".equals(ss.getBehavior())) {
                        if (ss.getSkillId() >= 400000) {
                            ss.setHidden(true);
                        }
                        if (existNames.contains(ss.getName())) {
                            ss.setHidden(true);
                        } else {
                            existNames.add(ss.getName());
                        }
                        o.add(ss);
                    }
                }
                d.setSkills(o);
            } else {
                d.setSkills(null);
            }
            memoryAvatar.put(d.getName(), d.getImg());
            Memory simple = new Memory();
            BeanUtils.copyProperties(d, simple);
            simple.setSkills(null);
            simple.setValue(null);
            simpleMemoryByNameGroup.put(simple.getName(), simple);
            memory.put(d.getMemoryId(), d);
        }
    }

    private void initCharacterInfoSkill(List<CharacterInfoSkill> list) {
        characterInfoSkills.addAll(list);
    }

    /**
     * 获取该同调者技能中的行为
     */
    private void initCharacterAllAttackBehavor() {

        Map<Long, List<CharacterInfoSkill>> collect =
                characterInfoSkills.stream().collect(Collectors.groupingBy(CharacterInfoSkill::getCId));
        // 获取通用行为信息
        for (long cid : collect.keySet()) {
            String beids =
                    collect.get(cid).stream().map(CharacterInfoSkill::getBehavior).collect(Collectors.joining(","));
            characterAllAttackBehavor.put(cid, beids);
        }
    }

    private void initCommonAttrBehavior() {
        elementBehavior.put("炎", 2004L);
        elementBehavior.put("水", 2005L);
        elementBehavior.put("霜", 2006L);
        elementBehavior.put("蚀", 2007L);
        elementBehavior.put("风", 2008L);
        elementBehavior.put("雷", 2009L);
        elementBehavior.put("物理", 2010L);

        roleToBehavior.put("护佑者", "9006");
        roleToBehavior.put("尖锋", "9005");
        roleToBehavior.put("轻卫", "9004");
        roleToBehavior.put("铁御", "9003");
        roleToBehavior.put("游徒", "9001");
        roleToBehavior.put("战术家", "9007");
        roleToBehavior.put("筑术师", "9002");

    }

    private void initWebPoolDataInfo() {
        localUpChar = JSONObject.parseObject(ReqUtils.get("http://bjhl.qianqiu.info/poolAim.json"));
        memoryData = JSONObject.parseObject(ReqUtils.get("http://bjhl.qianqiu.info/memory.json"));
        charsData = JSONObject.parseObject(ReqUtils.get("http://bjhl.qianqiu.info/chars.json"));
        poolData = JSONObject.parseObject(ReqUtils.get("http://bjhl.qianqiu.info/pool.json"));
    }

    private void initDamage(List<Damage> list) {
        Map<Long, List<Damage>> collect = list.stream().collect(Collectors.groupingBy(Damage::getDamageId));
        for (Long a : collect.keySet()) {
            damage.put(a, collect.get(a).getFirst());
        }
    }

    @Override
    public void run(String... args) throws Exception {
        init();
    }
}
