package info.qianqiu.ashechoes.compute.patch.skill;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.dto.domain.Skill;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.utils.array.ArrayListUtils;
import info.qianqiu.ashechoes.utils.string.StringUtils;

import java.util.ArrayList;

public class 异核技能等级补丁 {

    public static void patch1(ArrayList<Skill> effectSkill, InitComputeData init, ArrayList<Character> characters,
                              CardVo vo) {
//重新计算同调者异核技能的加成 部分异核技能加成的伤害数值是跟着花数来的，所以这里单独计算一下
        for (Skill si : effectSkill.stream().filter(e -> e.getName().endsWith("·异核")).toList()) {
            // 获取原始技能信息
            Skill origin = init.getSkill(si.getSkillId());
            String minValue = origin.getMinValue();
            String maxValue = origin.getMaxValue();
            if (StringUtils.isEmpty(maxValue)) {
                maxValue = minValue;
            }
            // 如果每个等级不一样
            if (minValue.contains(",") && maxValue.contains(",")) {
                // 找出技能对应的角色
                Character cca = init.getSkillFromCharacter(si.getSkillId());
                int index =
                        ArrayListUtils.getObjectIndexbyId(characters.stream().map(Character::getCharacterId).toList(),
                                cca.getCharacterId());
                //通过花数判断加成
                int flower = Integer.parseInt(vo.getFlower().split(",")[index]);
                si.setMinValue(minValue.split(",")[flower]);
                si.setMaxValue(maxValue.split(",")[flower]);
            }
        }
    }

}
