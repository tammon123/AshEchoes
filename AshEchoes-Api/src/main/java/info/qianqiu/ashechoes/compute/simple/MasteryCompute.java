package info.qianqiu.ashechoes.compute.simple;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

/**
 * @author Nan
 * @desc 计算角色伤害加成乘区
 */
@Builder
public class MasteryCompute {

    //单次计算
    private String 刻印专精;
    private String 监督刻印专精;
    private String 队长加成;
    private String 声望加成;
    private String 角色面板专精;
    //可能会出现多次计算情况
    private String 同调者专精加成;
    private String 专精加成;
    private String 刻印专精百分比;
    private String 刻印专精额外增加值;

    public boolean 同调者专精(String value) {
        BigDecimal add = new BigDecimal(value).add(new BigDecimal("1"));
        if (StringUtils.isEmpty(this.同调者专精加成)) {
            this.同调者专精加成 = add.toPlainString();
            return true;
        } else {
            if (add.compareTo(new BigDecimal(this.同调者专精加成)) > 0) {
                this.同调者专精加成 = add.toPlainString();
                return true;
            }
        }
        return false;
    }

    public void 刻印专精额外增加方法(String value) {
        BigDecimal add = new BigDecimal(value);
        if (StringUtils.isEmpty(this.刻印专精额外增加值)) {
            this.刻印专精额外增加值 = add.toPlainString();
        } else {
            this.刻印专精额外增加值 = add.add(new BigDecimal(this.刻印专精额外增加值)).toPlainString();
        }
    }

    public void 刻印专精百分比方法(String value) {
        BigDecimal add = new BigDecimal(value);
        if (StringUtils.isEmpty(this.刻印专精百分比)) {
            this.刻印专精百分比 = add.add(new BigDecimal("1")).toPlainString();
        } else {
            this.刻印专精百分比 = new BigDecimal(this.刻印专精百分比).add(add).toPlainString();
        }
    }

    public void 专精加成百分比方法(String value) {
        BigDecimal add = new BigDecimal(value);
        if (StringUtils.isEmpty(this.专精加成)) {
            this.专精加成 = add.add(new BigDecimal("1")).toPlainString();
        } else {
            this.专精加成 = new BigDecimal(this.专精加成).add(add).toPlainString();
        }
    }


    /**
     * 专精计算公式:最终专精=((刻印专精+监督刻印专精)*队长加成*声望加成*刻印专精百分比+刻印专精额外增加值)*角色面板专精*同调者专精加成
     * 来源:[数据分析] 论攻击C的专精收益 https://nga.178.com/read.php?tid=39959848
     * 作者:l364616656
     */
    public double compute(LinkedHashMap<String, String> r, CardVo vo) {

        BigDecimal c1 = new BigDecimal(刻印专精).add(new BigDecimal(监督刻印专精));
        BigDecimal c2 = new BigDecimal(队长加成);
        if (StringUtils.isEmpty(声望加成)) {
            声望加成 = "1";
        }
        BigDecimal c3 = new BigDecimal(声望加成);
        if (StringUtils.isEmpty(刻印专精百分比)) {
            刻印专精百分比 = "1";
        }
        BigDecimal c4 = new BigDecimal(刻印专精百分比);
        if (vo.getSync() != null) {
            if ((c4.subtract(new BigDecimal(1)).compareTo(vo.getSync().get当前刻印专精()) < 0)) {
                c4 = vo.getSync().get当前刻印专精().add(new BigDecimal("1"));
                r.put("同调-刻印专精:_1",vo.getSync().get当前刻印专精()+"_来源:属性同调_400000");
            }
        }
        if (StringUtils.isEmpty(刻印专精额外增加值)) {
            刻印专精额外增加值 = "0";
        }
        BigDecimal c5 = new BigDecimal(刻印专精额外增加值);
        BigDecimal c6 = new BigDecimal(角色面板专精);
        if (StringUtils.isEmpty(同调者专精加成)) {
            同调者专精加成 = "1";
        }
        BigDecimal c7 = new BigDecimal(同调者专精加成);
        if (StringUtils.isEmpty(专精加成)) {
            专精加成 = "1";
        }
        c7 = c7.add(new BigDecimal(专精加成)).subtract(new BigDecimal("1"));

        BigDecimal s1 = c1.multiply(c2).multiply(c3).multiply(c4).add(c5);
        BigDecimal s2 = s1.multiply(c6).multiply(c7);
        double v = s2.setScale(0, RoundingMode.DOWN).doubleValue();

        r.put("专精数值计算公式:", "[(刻印专精(" + 刻印专精 + ")+监督刻印专精(" + 监督刻印专精 +
                "))*队长加成(" + 队长加成 + ")*声望加成(" + 声望加成 + ")*刻印专精百分比(" + c4 +
                ")+刻印专精额外增加值(" + 刻印专精额外增加值 + ")]*角色面板专精(" + 角色面板专精 + ")*专精加成(" +
                c7 + ")");

        return v;

    }

    /**
     * 计算增伤率
     * 标准专精增伤 = (专精-30) × 0.25% (专精<750)；
     * 或 = 180%+(专精-750) × 0.16% (750<专精<1500)；
     * 或 = 300%+(专精-1500) × 0.1% (1500<专精<3000)；
     * 或 = 450%+(专精-3000) × 0.05% (3000<专精)。
     * 增伤转换率一般为1，队长训练后为1.1
     */
    public double attackCompute(String 增伤转换率, LinkedHashMap<String, String> r, CardVo vo) {
        double result = 0.0;
        double base = 0.0;
        double result1;
        double mastery = compute(r, vo);
        BigDecimal c1;
        if (mastery < 750.0) {
            c1 = new BigDecimal("0.0025");
            result = c1.multiply(new BigDecimal(mastery - 30)).doubleValue();
            result1 = new BigDecimal(mastery - 30).doubleValue();
            base = 0;
        } else if (mastery < 1500.0) {
            c1 = new BigDecimal("0.0016");
            result = c1.multiply(new BigDecimal(mastery - 750)).doubleValue();
            result1 = new BigDecimal(mastery - 750).doubleValue();
            base = 1.8;
            result += 1.8;
        } else if (mastery < 3000.0) {
            c1 = new BigDecimal("0.001");
            result = c1.multiply(new BigDecimal(mastery - 1500)).doubleValue();
            result1 = new BigDecimal(mastery - 1500).doubleValue();
            base = 3;
            result += 3;
        } else {
            c1 = new BigDecimal("0.0005");
            result = c1.multiply(new BigDecimal(mastery - 3000)).doubleValue();
            result1 = new BigDecimal(mastery - 3000).doubleValue();
            base = 4.5;
            result += 4.5;
        }
        double v = new BigDecimal(result).multiply(new BigDecimal(增伤转换率)).setScale(3, RoundingMode.DOWN)
                .doubleValue();
        r.put("最终专精伤害加成公式:",
                "{专精基准加成(" + base + ")+[专精阶梯差值(" + result1 + ")*专精阶梯伤害转换率(" + c1.toPlainString() +
                        ")]}*增伤转换率(" + 增伤转换率 + ")");
        r.put("最终计算专精数值:", mastery + "");
        return v;
    }

}
