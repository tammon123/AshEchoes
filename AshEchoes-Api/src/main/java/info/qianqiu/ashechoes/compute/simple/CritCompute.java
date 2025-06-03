package info.qianqiu.ashechoes.compute.simple;


import com.alibaba.fastjson2.JSONObject;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Nan
 * @desc 计算角色暴击属性加成乘区
 * 暴击区期望 = 1 + (暴击伤害-1) × 暴击率
 * 一般情况下，尖锋和游徒的初始暴击率为15%，暴击伤害为150%；
 * 其他职业的初始暴击率为5%，暴击伤害为130%。
 */
@Builder
public class CritCompute {

    private String 暴击率区;
    private String 同调者暴击率区;
    private String 暴击伤害区;

    public void 暴击率(String value) {
        if (StringUtils.isEmpty(this.暴击率区)) {
            this.暴击率区 = new BigDecimal(value).toPlainString();
        } else {
            this.暴击率区 = new BigDecimal(this.暴击率区).add(new BigDecimal(value)).toPlainString();
        }
    }

    public boolean 同调者暴击率(String value) {
        BigDecimal add = new BigDecimal(value);
        if (StringUtils.isEmpty(this.同调者暴击率区)) {
            this.同调者暴击率区 = add.toPlainString();
            return true;
        } else {
            //元素易伤取最大值
            if (add.compareTo(new BigDecimal(同调者暴击率区)) > 0) {
                this.同调者暴击率区 = add.toPlainString();
                return true;
            }
        }
        return false;
    }

    public void 暴击伤害(String value) {
        if (StringUtils.isEmpty(this.暴击伤害区)) {
            this.暴击伤害区 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.暴击伤害区 = new BigDecimal(this.暴击伤害区).add(new BigDecimal(value)).toPlainString();
        }
    }

    /**
     * @return
     */
    public double compute(Map<String, String> r, CardVo vo) {

        BigDecimal c1 = new BigDecimal(暴击率区);
        if (StringUtils.isEmpty(同调者暴击率区)) {
            同调者暴击率区 = "0";
        }
        BigDecimal c2 = new BigDecimal(同调者暴击率区);
        BigDecimal s1 = c1.add(c2);
        if (s1.compareTo(new BigDecimal("1")) > 0) {
            s1 = new BigDecimal("1");
        }
        BigDecimal c3 = new BigDecimal(暴击伤害区);
        if (vo.getSync() != null) {
            if (s1.compareTo(vo.getSync().get当前暴击率()) < 0) {
                s1 = vo.getSync().get当前暴击率();
                r.put("同调-暴击率:_1", vo.getSync().get当前暴击率() + "_来源:属性同调_400000");
            }
            if ((c3.subtract(new BigDecimal(1)).compareTo(vo.getSync().get暴击伤害()) < 0)) {
                c3 = vo.getSync().get暴击伤害().add(new BigDecimal("1"));
                r.put("同调-暴击伤害:_1", vo.getSync().get当前暴击率() + "_来源:属性同调_400000");
            }
        }
        r.put("暴击区计算公式:",
                "基础伤害(1)+(暴击伤害(" + c3 + ")-1)*暴击率(" +
                        s1 + ")(最大值:1)");
        vo.setCritValue(s1.toPlainString());
        return new BigDecimal("1").add(c3.subtract(new BigDecimal("1")).multiply(s1)).setScale(3, RoundingMode.DOWN)
                .doubleValue();

    }

}
