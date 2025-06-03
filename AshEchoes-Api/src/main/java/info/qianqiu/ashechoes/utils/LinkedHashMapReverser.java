package info.qianqiu.ashechoes.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collections;

public class LinkedHashMapReverser {
    public static <K, V> LinkedHashMap<K, V> reverseLinkedHashMap(LinkedHashMap<K, V> originalMap) {
        // 创建一个ArrayList来存储Map的entry
        ArrayList<Map.Entry<K, V>> entries = new ArrayList<>(originalMap.entrySet());
        
        // 反转ArrayList
        Collections.reverse(entries);
        
        // 创建一个新的LinkedHashMap来存储反转后的entry
        LinkedHashMap<K, V> reversedMap = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            reversedMap.put(entry.getKey(), entry.getValue());
        }
        
        return reversedMap;
    }
}
