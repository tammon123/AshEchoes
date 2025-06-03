package info.qianqiu.ashechoes.compute.patch.extMemoryAttack;


import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Behavior;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.CharacterInfoSkill;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class 神迹_附加攻击补丁 {

    public static ArrayList<CharacterInfoSkill> patch(List<CharacterInfoSkill> characterInfoSkills,
                                                      List<Character> chars, HashSet<Behavior> behaviors1, CardVo vo1, boolean buff1018) {
        ArrayList<CharacterInfoSkill> characterInfoSkills1 = new ArrayList<>(characterInfoSkills);
        // 便利当前角色，是否有屏障
        List<Long> list = behaviors1.stream().map(Behavior::getBehaviorId).toList();
        if (list.contains(1049L)) {
            List<String> list1 = chars.stream().map(Character::getElement).toList();
            if (list1.contains("水")) {
                CharacterInfoSkill.CharacterInfoSkillBuilder builder = CharacterInfoSkill.builder()
                        .siId(0L)
                        .cId(chars.getFirst().getCharacterId())
                        .cName(characterInfoSkills.getFirst().getCName())
                        .sName(characterInfoSkills.getFirst().getCName() + "·神迹")
                        .type("主动1")
                        .value("1000")
                        .add1("0")
                        .add2("0")
                        .count("1")
                        .element("水")
                        .attr("攻击")
                        .behavior("10001")
                        .charSkillIndex((byte) 1);
                if (buff1018) {
                    builder.behavior("10001, 1018");
                }
                CharacterInfoSkill build = builder.build();
                if ("乐无异".equals(characterInfoSkills.getFirst().getCName())) {
                    build.setCount("6");
                }
                if ("余音".equals(characterInfoSkills.getFirst().getCName())) {
                    build.setCount("4");
                }
                characterInfoSkills1.add(build);
            }
        }
        return characterInfoSkills1;
    }
}
