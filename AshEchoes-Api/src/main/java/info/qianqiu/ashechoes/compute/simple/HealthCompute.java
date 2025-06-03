package info.qianqiu.ashechoes.compute.simple;


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
public class HealthCompute {

    private String 潜能体质百分比;
    private String 队长修正;
    private String 声望加成;
    private String 刻印体质;
    private String 监督刻印体质;
    private String 角色基础属性百分比;
    //可能有多个属性
    private String 刻印体质百分比;


    public void 刻印体质百分比(String value) {
        BigDecimal add = new BigDecimal(value);
        if (StringUtils.isEmpty(this.刻印体质百分比)) {
            this.刻印体质百分比 = add.add(new BigDecimal("1")).toPlainString();
        } else {
            this.刻印体质百分比 = add.add(new BigDecimal(this.刻印体质百分比)).toPlainString();
        }
    }

    public double compute(LinkedHashMap<String, String> r) {

        BigDecimal c0 = new BigDecimal(潜能体质百分比);
        BigDecimal c1 = new BigDecimal(队长修正);
        if (StringUtils.isEmpty(声望加成)) {
            声望加成 = "1";
        }
        BigDecimal c2 = new BigDecimal(声望加成);
        BigDecimal c3 = new BigDecimal(刻印体质).add(new BigDecimal(监督刻印体质));
        if (StringUtils.isEmpty(刻印体质百分比)) {
            刻印体质百分比 = "1";
        }
        BigDecimal c4 = new BigDecimal(刻印体质百分比);

        BigDecimal s1 = c0.multiply(c1).multiply(c2).multiply(c3).multiply(c4);

        BigDecimal c6 = new BigDecimal(角色基础属性百分比);
        BigDecimal s2 = s1.multiply(c6);
        r.put("最终体质计算公式:",
                "[(刻印体质(" + 刻印体质 + ")+监督刻印体质(" + 监督刻印体质 + "))*潜能体质加成(" + 潜能体质百分比 +
                        ")*队长修正(" + 队长修正 + ")*声望加成("+声望加成+")*刻印体质百分比("+刻印体质百分比+")]*角色基础属性百分比("+角色基础属性百分比+")");
        return s2.setScale(0, RoundingMode.DOWN).doubleValue();
    }

}
