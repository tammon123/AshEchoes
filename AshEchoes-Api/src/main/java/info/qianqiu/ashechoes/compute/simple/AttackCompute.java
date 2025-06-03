package info.qianqiu.ashechoes.compute.simple;


import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

/**
 * @author Nan
 * @desc 计算角色攻击属性加成乘区
 */
@Builder
public class AttackCompute {

    private String 队长修正;
    private String 声望加成;
    private String 刻印攻击;
    private String 监督刻印攻击;
    private String 角色基础属性;
    // 蚀爆也在这里
    private String 刻印攻击百分比;
    private String 刻印攻击增加值;
    private String 额外攻击力加成;
    private String 攻击力加成;
    private String 同调者攻击力加成;
    private String 额外攻击百分比增加;

    public void 刻印攻击增加值(String value) {
        if (StringUtils.isEmpty(this.刻印攻击增加值)) {
            this.刻印攻击增加值 = new BigDecimal(value).toPlainString();
        } else {
            this.刻印攻击增加值 =
                    new BigDecimal(this.刻印攻击增加值).add(new BigDecimal(value)).toPlainString();
        }
    }

    public void 额外攻击增加值(String value) {
        if (StringUtils.isEmpty(this.额外攻击力加成)) {
            this.额外攻击力加成 = new BigDecimal(value).toPlainString();
        } else {
            this.额外攻击力加成 = new BigDecimal(this.额外攻击力加成).add(new BigDecimal(value)).toPlainString();
        }
    }

    public void 攻击力加成(String value) {
        if (StringUtils.isEmpty(this.攻击力加成)) {
            this.攻击力加成 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.攻击力加成 =
                    new BigDecimal(this.攻击力加成).add(new BigDecimal(value)).toPlainString();
        }
    }

    public boolean 同调者攻击(String value) {
        BigDecimal add = new BigDecimal(value).add(new BigDecimal("1"));
        if (StringUtils.isEmpty(this.同调者攻击力加成)) {
            this.同调者攻击力加成 = add.toPlainString();
            return true;
        } else {
            if (add.compareTo(new BigDecimal(this.同调者攻击力加成)) > 0) {
                this.同调者攻击力加成 = add.toPlainString();
                return true;
            }
        }
        return false;
    }

    public void 刻印攻击百分比(String value) {
        if (StringUtils.isEmpty(this.刻印攻击百分比)) {
            this.刻印攻击百分比 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.刻印攻击百分比 =
                    new BigDecimal(this.刻印攻击百分比).add(new BigDecimal(value)).toPlainString();
        }
    }

    public void 额外攻击百分比(String value) {
        if (StringUtils.isEmpty(this.额外攻击百分比增加)) {
            this.额外攻击百分比增加 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.额外攻击百分比增加 =
                    new BigDecimal(this.额外攻击百分比增加).add(new BigDecimal(value)).toPlainString();
        }
        if (new BigDecimal(this.额外攻击百分比增加).compareTo(new BigDecimal("2.2")) > 0) {
            this.额外攻击百分比增加 = "2.2";
        }
    }


    /**
     * 攻击计算公式为:以下方案
     * 最终攻击 = {[队长修正 × 声望加成 × 刻印攻击 × (1 + 刻印攻击百分比) + 刻印攻击增加值] × 角色基础属性 × 攻击力加成 + 额外固定攻击加成} × 额外攻击百分比增加
     * 队长修正:角色作为队长时攻击、专精、体质会获得1.2的该项修正，如果队长潜能达到L1时，全队均获得该项修正。其中体质更为特别，当队长达到L2并在场时，该项会获得共计1.2 × 1.2 = 1.44的修正。
     * 声望加成:随着不同漫巡地区声望等级的提高，在对应漫巡地区的关卡内，攻击、专精、体质会获得从1到1.5不等的该项修正。具体可在游戏内漫巡声望界面查看。
     * 刻印攻击:在跑出的战术刻印中查看。实际计算中，该项会获得监督刻印的数值加成(相当于20%的监督刻印属性)；成长奖励处最多100点的防御和终端的加成。最后的数值为刻印基础属性 + 监督属性加成 + 成长奖励加成。
     * 角色基础属性:在同调者面板详细属性处查看，会受到体能特训的加成，如图:
     * 攻击力加成:游戏内的标识为(属性乘区·攻击力加成)，如芙蕖战斗特性。此外没有标注的终端技能:同调增幅也是这个乘区。
     *
     * @return
     */
    public double compute(LinkedHashMap<String, String> r, CardVo vo) {

        BigDecimal c1 = new BigDecimal(队长修正);
        if (StringUtils.isEmpty(声望加成)) {
            声望加成 = "1";
        }
        BigDecimal c2 = new BigDecimal(声望加成);
        BigDecimal c3 = new BigDecimal(刻印攻击).add(new BigDecimal(监督刻印攻击));
        if (StringUtils.isEmpty(刻印攻击百分比)) {
            刻印攻击百分比 = "1";
            r.put("警告:刻印攻击百分比乘区为空", "");
        }
        BigDecimal c4 = new BigDecimal(刻印攻击百分比);
        if (StringUtils.isEmpty(刻印攻击增加值)) {
            刻印攻击增加值 = "0";
        }
        BigDecimal c5 = new BigDecimal(刻印攻击增加值);

        // 计算当前被除数 攻击的数值
        if (vo.getSync() != null) {
            BigDecimal te = c3.multiply(c1).multiply(c2);
            BigDecimal divide = c5.divide(te, 2, RoundingMode.HALF_UP);
            if ((divide.add(c4).subtract(new BigDecimal("1"))).compareTo(vo.getSync().get当前刻印攻击百分比()) < 0) {
                c4 = vo.getSync().get当前刻印攻击百分比().add(new BigDecimal("1"));
                c5 = new BigDecimal(0);
                r.put("同调-刻印攻击:_1",vo.getSync().get当前刻印攻击百分比()+"_来源:属性同调_400000");
            }
        }

        BigDecimal s1 = c1.multiply(c2).multiply(c3).multiply(c4).add(c5);

        BigDecimal c6 = new BigDecimal(角色基础属性);
        if (StringUtils.isEmpty(攻击力加成)) {
            攻击力加成 = "1";
        }
        BigDecimal c7 = new BigDecimal(攻击力加成);
        if (StringUtils.isEmpty(同调者攻击力加成)) {
            同调者攻击力加成 = "1";
        }
        c7 = c7.add(new BigDecimal(同调者攻击力加成)).subtract(new BigDecimal("1"));

        if (StringUtils.isEmpty(额外攻击力加成)) {
            额外攻击力加成 = "0";
        }
        BigDecimal c8 = new BigDecimal(额外攻击力加成);
        BigDecimal s2 = s1.multiply(c6).multiply(c7).add(c8);
        if (StringUtils.isEmpty(额外攻击百分比增加)) {
            额外攻击百分比增加 = "1";
            r.put("警告:额外攻击百分比增加加成乘区为空", "");
        }
        BigDecimal s3 = new BigDecimal(额外攻击百分比增加);
        if (vo.getSync() != null) {
            if ((s3.subtract(new BigDecimal(1)).compareTo(vo.getSync().get当前额外攻击()) < 0)) {
                s3 = vo.getSync().get当前额外攻击().add(new BigDecimal("1"));
                r.put("同调-额外攻击:_1",vo.getSync().get当前额外攻击()+"_来源:属性同调_400000");
            }
        }
        r.put("攻击力乘区计算公式:",
                "{[队长修正(" + 队长修正 + ")*声望加成(" + 声望加成 + ")*(刻印攻击(" + 刻印攻击 + ")+监督刻印攻击(" +
                        监督刻印攻击 +
                        "))*刻印攻击百分比(" + c4 + ")+刻印攻击固定增加值(" + c5 +
                        ")]*角色基础加成(" + 角色基础属性 + ")*攻击力加成(" + c7 +
                        ")+额外攻击固定增加值(" + 额外攻击力加成 + ")}*额外攻击百分比(" + s3 + ")");
        return s2.multiply(s3).setScale(0, RoundingMode.DOWN).doubleValue();
    }

}
