package info.qianqiu.ashechoes.compute.simple;

import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

/**
 * 角色防御属性
 */
@Builder
public class DamageCompute {


    private String 激励区;
    private String 额外伤害区;
    private String 技能增伤区;
    private String 独立增伤区;
    private String 暴击独立增伤区;

    public void 激励(String value) {
        if (StringUtils.isEmpty(this.激励区)) {
            this.激励区 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.激励区 = new BigDecimal(this.激励区).add(new BigDecimal(value)).toPlainString();
        }
    }

    public void 额外伤害(String value) {
        if (StringUtils.isEmpty(this.额外伤害区)) {
            this.额外伤害区 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.额外伤害区 = new BigDecimal(this.额外伤害区).add(new BigDecimal(value)).toPlainString();
        }
    }

    public void 技能增伤(String value) {
        if (StringUtils.isEmpty(this.技能增伤区)) {
            this.技能增伤区 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.技能增伤区 = new BigDecimal(this.技能增伤区).add(new BigDecimal(value)).toPlainString();
        }
    }

    public void 独立增伤(String value) {
        BigDecimal add = new BigDecimal(value).add(new BigDecimal("1"));
        if (StringUtils.isEmpty(this.独立增伤区)) {
            this.独立增伤区 = add.toPlainString();
        } else {
            this.独立增伤区 = new BigDecimal(this.独立增伤区).multiply(add).toPlainString();
        }
    }

    public void 暴击独立增伤(String value) {
        BigDecimal add = new BigDecimal(value).add(new BigDecimal("1"));
        if (StringUtils.isEmpty(this.暴击独立增伤区)) {
            this.暴击独立增伤区 = add.toPlainString();
        } else {
            this.暴击独立增伤区 = new BigDecimal(this.暴击独立增伤区).multiply(add).toPlainString();
        }
    }

    /**
     * 攻击计算公式为:以下方案
     * 最终攻击 = {[队长修正 × 声望加成 × 刻印攻击 × (1 + 刻印攻击百分比) + 刻印攻击增加值] × 角色基础属性 × 终端类型攻击百分比加成 + 额外固定攻击加成} × 额外攻击百分比加成
     * 队长修正:角色作为队长时攻击、专精、体质会获得1.2的该项修正，如果队长潜能达到L1时，全队均获得该项修正。其中体质更为特别，当队长达到L2并在场时，该项会获得共计1.2 × 1.2 = 1.44的修正。
     * 声望加成:随着不同漫巡地区声望等级的提高，在对应漫巡地区的关卡内，攻击、专精、体质会获得从1到1.5不等的该项修正。具体可在游戏内漫巡声望界面查看。
     * 刻印攻击:在跑出的战术刻印中查看。实际计算中，该项会获得监督刻印的数值加成(相当于20%的监督刻印属性)；成长奖励处最多100点的防御和终端的加成。最后的数值为刻印基础属性 + 监督属性加成 + 成长奖励加成。
     * 角色基础属性:在同调者面板详细属性处查看，会受到体能特训的加成，如图:
     * 攻击力加成:游戏内的标识为(属性乘区·攻击力加成)，如芙蕖战斗特性。此外没有标注的终端技能:同调增幅也是这个乘区。
     *
     * @return
     */
    public double compute(LinkedHashMap<String, String> r) {

        if (StringUtils.isEmpty(激励区)) {
            激励区 = "1";
            r.put("警告:激励区为空", "");
        }
        if (new BigDecimal(激励区).compareTo(new BigDecimal("3")) > 0) {
            激励区 = "3";
        }
        if (!"3".equals(激励区)) {
            r.put("警告:激励未满20层，请注意", "");
        }
        BigDecimal c1 = new BigDecimal(激励区);
        if (StringUtils.isEmpty(额外伤害区)) {
            额外伤害区 = "1";
            r.put("警告:额外伤害区为空", "");
        }
        BigDecimal c2 = new BigDecimal(额外伤害区);
        if (StringUtils.isEmpty(技能增伤区)) {
            技能增伤区 = "1";
            r.put("警告:技能增伤区为空", "");
        }
        BigDecimal c3 = new BigDecimal(技能增伤区);
        if (StringUtils.isEmpty(独立增伤区)) {
            独立增伤区 = "1";
        }
        BigDecimal c4 = new BigDecimal(独立增伤区);
        if (StringUtils.isEmpty(暴击独立增伤区)) {
            暴击独立增伤区 = "1";
        }
        BigDecimal c5 = new BigDecimal(暴击独立增伤区);

        r.put("增伤乘区计算公式:",
                "激励区[" + 激励区 + "](最大值:3)*额外伤害区(" + 额外伤害区 + ")*技能增伤区(" + 技能增伤区 +
                        ")*独立增伤区(" + 独立增伤区 + ")*暴击独立增伤区(" + 暴击独立增伤区 + ")");

        return c1.multiply(c2).multiply(c3).multiply(c4).multiply(c5).setScale(3, RoundingMode.DOWN).doubleValue();

    }

}
