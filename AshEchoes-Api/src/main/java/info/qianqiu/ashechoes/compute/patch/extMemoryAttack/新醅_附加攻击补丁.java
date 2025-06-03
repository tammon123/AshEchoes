package info.qianqiu.ashechoes.compute.patch.extMemoryAttack;


import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class 新醅_附加攻击补丁 {

    public static ArrayList<CharacterInfoSkill> patch(List<CharacterInfoSkill> characterInfoSkills,
                                                      HashSet<Behavior> behaviors1,
                                                      Character cc,
                                                      String level1, boolean buff1018) {
        int level = Integer.parseInt(level1);
        int damageData = 1000 * level + 1000;

        Behavior behavior = new Behavior();
        behavior.setBehaviorId(1056L);

        if (behaviors1.contains(behavior)) {
            damageData += new BigDecimal(damageData).multiply(new BigDecimal("0.12")).intValue();
        }

        ArrayList<CharacterInfoSkill> characterInfoSkills1 = new ArrayList<>(characterInfoSkills);
        CharacterInfoSkill.CharacterInfoSkillBuilder builder = CharacterInfoSkill.builder()
                .siId(0L)
                .cId(cc.getCharacterId())
                .cName(characterInfoSkills.getFirst().getCName())
                .sName(characterInfoSkills.getFirst().getCName() + "·追跡痕爆")
                .type("主动1")
                .value(damageData + "")
                .add1("0")
                .add2("0")
                .count("5持续1")
                .element("物理")
                .attr("攻击")
                .behavior("10001")
                .charSkillIndex((byte) 1);
        if (buff1018) {
            builder.behavior("10001,1018");
        }
        CharacterInfoSkill build = builder.build();
        characterInfoSkills1.add(build);
        return characterInfoSkills1;
    }
}
