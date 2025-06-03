package info.qianqiu.ashechoes.compute.patch.extMemoryAttack;


import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;

import java.util.ArrayList;
import java.util.List;

public class 欢声萦回_附加攻击补丁 {



    public static ArrayList<CharacterInfoSkill> patch(List<CharacterInfoSkill> characterInfoSkills,
                                                      Character cc,
                                                      String level1, boolean buff1018) {
        int level = Integer.parseInt(level1);
        int damageData = 5000 * level;

        ArrayList<CharacterInfoSkill> characterInfoSkills1 = new ArrayList<>(characterInfoSkills);
        CharacterInfoSkill.CharacterInfoSkillBuilder builder = CharacterInfoSkill.builder()
                .siId(0L)
                .cId(cc.getCharacterId())
                .cName(characterInfoSkills.getFirst().getCName())
                .sName(characterInfoSkills.getFirst().getCName() + "·深邃蚀渊")
                .type("主动1")
                .value(damageData + "")
                .add1("0")
                .add2("0")
                .count("1")
                .element("蚀")
                .attr("终端")
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
