package info.qianqiu.ashechoes.compute.patch.sync;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
public class SyncData {

    // 哪些角色可以同调
    public ArrayList<String> allow = new ArrayList<>();
    public ArrayList<String> allowDamage = new ArrayList<>();

    private BigDecimal 当前刻印攻击百分比 = new BigDecimal("0");
    private BigDecimal 当前额外攻击 = new BigDecimal("0");
    private BigDecimal 当前刻印专精 = new BigDecimal("0");
    private BigDecimal 当前暴击率 = new BigDecimal("0");
    private BigDecimal 当前暴击伤害 = new BigDecimal("0");

    private BigDecimal 刻印攻击 = new BigDecimal("0.6");
    private BigDecimal 额外攻击 = new BigDecimal("0.9");
    private BigDecimal 刻印专精 = new BigDecimal("0.2");
    private BigDecimal 暴击率 = new BigDecimal("0.6");
    private BigDecimal 暴击伤害 = new BigDecimal("0.6");

    public SyncData() {
        allow.add("玄戈");
        //刻印专精
        allowDamage.add("201");
        //刻印攻击增加值
        allowDamage.add("204");
        // 刻印攻击百分比
        allowDamage.add("205");
        // 额外百分比
        allowDamage.add("207");
        // 暴击伤害
        allowDamage.add("209");
        // 同调者暴击率
        allowDamage.add("210");
        // 暴击率
        allowDamage.add("211");
    }

}
