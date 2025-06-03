package info.qianqiu.ashechoes.compute.patch.characters;

import info.qianqiu.ashechoes.dto.domain.Skill;

import java.util.ArrayList;
import java.util.Objects;

public class 角色风晴雪补丁 {
    public static void patch1(int flowers, ArrayList<Skill> r) {
        // 11 3花之前可以用   20 三花之后用
        ArrayList<Long> longs = new ArrayList<>();
        if (flowers < 3) {
            // 移除三花使用的20 技能:370054
            longs.add(370053L);
            longs.add(370054L);
        } else {
            //溢出1花使用的11 技能
            longs.add(370050L);
            longs.add(370051L);
            longs.add(370056L);
            longs.add(370057L);
        }
        for (Long id : longs) {
            for (int i = 0; i < r.size(); i++) {
                if (Objects.equals(r.get(i).getSkillId(), id)) {
                    r.remove(i);
                    break;
                }
            }
        }
    }
}
