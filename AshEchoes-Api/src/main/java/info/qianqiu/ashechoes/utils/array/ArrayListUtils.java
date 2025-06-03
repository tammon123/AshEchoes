package info.qianqiu.ashechoes.utils.array;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Character;

import java.util.List;
import java.util.Objects;

public class ArrayListUtils {


    public static int getObjectIndexbyId(List<Long> list, Long o) {
        int r = -1;
        for (int i = 0; i < list.size(); i++) {
            if (Objects.equals(list.get(i), o)) {
                return i;
            }
        }
        return r;
    }

    public static String longArrayToString(List<Long> arr, String split) {
        StringBuilder sb = new StringBuilder();
        for (Long a : arr) {
            sb.append(a).append(split);
        }
        if (!sb.isEmpty()) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    public static String arrayToString(List arr, String split) {
        StringBuilder sb = new StringBuilder();
        for (Object a : arr) {
            sb.append(a).append(split);
        }
        if (!sb.isEmpty()) {
            return sb.substring(0, sb.length() - 1);
        }
        return "";
    }

    public static int getCharIndex(Long id, List<Character> list) {
        for (int i = 0; i < list.size(); i++ ) {
            if (Objects.equals(id, list.get(i).getCharacterId())) {
                return i;
            }
        }
        return -1;
    }
    public static int getCharFlowers(String id, CardVo vo) {

        String characters = vo.getCharacters();
        String flower = vo.getFlower();

        for (int i = 0; i < characters.split(",").length; i++) {
            if (characters.split(",")[i].equals(id)) {
                return Integer.parseInt(flower.split(",")[i]);
            }
        }

        return 0;
    }
}