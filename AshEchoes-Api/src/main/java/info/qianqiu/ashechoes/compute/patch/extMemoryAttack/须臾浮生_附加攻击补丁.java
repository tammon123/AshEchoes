package info.qianqiu.ashechoes.compute.patch.extMemoryAttack;


import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;

import java.util.ArrayList;
import java.util.List;

public class 须臾浮生_附加攻击补丁 {

    public static ArrayList<CharacterInfoSkill> patch(List<CharacterInfoSkill> characterInfoSkills,
                                                      List<Character> chars,
                                                      Character cc,
                                                      String level1, boolean buff1018) {
        int level = Integer.parseInt(level1);
        int[] damageData = {1200 * level, 600 * level, 300 * level, 150 * level};
        int realDamage = 0;
        ArrayList<Character> chas = new ArrayList<>();
        for (int i = 0; i < chars.size(); i++) {
            if (i < 4) {
                chas.add(chars.get(i));
            } else {
                break;
            }
        }
        List<Character> lis = chas.stream().filter(e -> e.getElement().equals("风")).toList();
        for (int i = 0; i < lis.size(); i++) {
            if (lis.get(i).getCharacterId().equals(cc.getCharacterId())) {
                realDamage = damageData[i];
                break;
            }
        }
        ArrayList<CharacterInfoSkill> characterInfoSkills1 = new ArrayList<>(characterInfoSkills);
        CharacterInfoSkill.CharacterInfoSkillBuilder builder = CharacterInfoSkill.builder()
                .siId(0L)
                .cId(chars.getFirst().getCharacterId())
                .cName(characterInfoSkills.getFirst().getCName())
                .sName(characterInfoSkills.getFirst().getCName() + "·风影追击")
                .type("主动1")
                .value(realDamage + "")
                .add1("0")
                .add2("0")
                .count("1")
                .element("风")
                .attr("攻击")
                .behavior("10001")
                .charSkillIndex((byte) 1);
        if (buff1018) {
            builder.behavior("10001,1018");
        }
        characterInfoSkills1.add(builder.build());
        return characterInfoSkills1;
    }
}
