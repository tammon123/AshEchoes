package info.qianqiu.ashechoes.compute.simple;

import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.dto.domain.Character;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;

/**
 * @author Nan
 * 目前Debuff有易伤、减抗、融甲
 * @desc 计算角色debuff赋予乘区
 */
@Builder
@Data
public class DebuffCompute {

    private String 消融区;
    private String 无视防御区;
    private String 易伤区;
    private String 元素易伤区;
    private String 减抗区;
    private String 抗性弱点区;
    private String 敌方防御区;
    private String 不计入上限减抗区;

    public boolean 消融(String value) {
        if (value.contains("-")) {
            敌方防御区 = value;
            return true;
        }
        BigDecimal add = new BigDecimal(value).add(new BigDecimal("1"));
        if (StringUtils.isEmpty(this.消融区)) {
            this.消融区 = add.toPlainString();
            return true;
        } else {
            if (add.compareTo(new BigDecimal(this.消融区)) > 0) {
                this.消融区 = add.toPlainString();
                return true;
            }
        }
        return false;
    }

    public void 无视防御(String value) {
        if (StringUtils.isEmpty(this.无视防御区)) {
            this.无视防御区 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.无视防御区 = new BigDecimal(this.无视防御区).add(new BigDecimal(value)).toPlainString();
        }
    }

    public void 易伤(String value) {
        if (StringUtils.isEmpty(this.易伤区)) {
            this.易伤区 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.易伤区 = new BigDecimal(this.易伤区).add(new BigDecimal(value)).toPlainString();
        }
    }

    public boolean 元素易伤(String value) {
        BigDecimal add = new BigDecimal(value).add(new BigDecimal("1"));
        if (StringUtils.isEmpty(this.元素易伤区)) {
            this.元素易伤区 = add.toPlainString();
            return true;
        } else {
            //元素易伤取最大值
            if (add.compareTo(new BigDecimal(元素易伤区)) > 0) {
                this.元素易伤区 = add.toPlainString();
                return true;
            }
        }
        return false;
    }

    public void 不计入上限减抗(String value) {
        if (StringUtils.isEmpty(this.不计入上限减抗区)) {
            this.不计入上限减抗区 = new BigDecimal(value).toPlainString();
        } else {
            this.不计入上限减抗区 = new BigDecimal(this.不计入上限减抗区).add(new BigDecimal(value)).toPlainString();
        }
    }

    public void 减抗(String value) {
        if (StringUtils.isEmpty(this.减抗区)) {
            this.减抗区 = new BigDecimal(value).add(new BigDecimal("1")).toPlainString();
        } else {
            this.减抗区 = new BigDecimal(this.减抗区).add(new BigDecimal(value)).toPlainString();
        }
        if (new BigDecimal(this.减抗区).compareTo(new BigDecimal("2")) > 0) {
            this.减抗区 = "2";
        }
    }

    /**
     * 各个易伤区相乘
     * 易伤和元素易伤总和最大为1.5,计算出和之后，在减去boss免伤
     *
     * @return
     */
    public double compute(LinkedHashMap<String, String> r, Character character, CardVo vo) {

        if (StringUtils.isEmpty(易伤区)) {
            易伤区 = "1";
        }
        if (new BigDecimal(this.易伤区).compareTo(new BigDecimal("2.5")) > 0) {
            this.易伤区 = "2.5";
        }
        if (StringUtils.isEmpty(元素易伤区)) {
            元素易伤区 = "1";
        }
        if ("1".equals(易伤区) && "1".equals(元素易伤区)) {
            r.put("警告:易伤区为空", "");
        }
        BigDecimal s1 = new BigDecimal(易伤区).add(new BigDecimal(元素易伤区)).subtract(new BigDecimal("1"));
        if (s1.compareTo(new BigDecimal("2.5")) > 0) {
            s1 = new BigDecimal("2.5");
        }
        if (StringUtils.isEmpty(减抗区)) {
            减抗区 = "1";
            r.put("警告:减抗区为空", "");
        }
        if (StringUtils.isEmpty(不计入上限减抗区)) {
            不计入上限减抗区 = "0";
        }
        BigDecimal s2 = new BigDecimal(减抗区).add(new BigDecimal(不计入上限减抗区));
        if (StringUtils.isEmpty(消融区)) {
            消融区 = "1";
        }
        if (敌方防御区 == null) {
            敌方防御区 = "0";
        }
        BigDecimal s3 = new BigDecimal(消融区).add(new BigDecimal(敌方防御区));
        if (StringUtils.isEmpty(无视防御区)) {
            无视防御区 = "1";
        }
        s3 = new BigDecimal(无视防御区).add(s3).subtract(new BigDecimal("1"));
        if ("1".equals(无视防御区) && "1".equals(消融区)) {
            r.put("警告:减防区为空", "");
        }

        // 消融防御区最多为2
        if (s3.compareTo(new BigDecimal("2")) > 0) {
            s3 = new BigDecimal("2");
        }
        if (StringUtils.isNotEmpty(vo.getDrd()) && !"0".equals(vo.getDrd())) {
                r.put("敌方" + vo.getDrd() + "属性弱点:",
                        new BigDecimal(vo.getDrd()).divide(new BigDecimal("100"),2, RoundingMode.DOWN).toPlainString()+"_来源:敌方属性弱点_400000");
                s2 = new BigDecimal(vo.getDrd()).divide(new BigDecimal("100"),2, RoundingMode.DOWN).add(s2);
        }
        r.put("减益乘区计算公式:",
                "[易伤(" + 易伤区 + ")+元素易伤(" + 元素易伤区 + ")-1](最大值:2.5)*减抗区[" + s2 +
                        "](最大值:2)*防御降低区[" + s3 + "](最大值:2)");

        return s1.multiply(s2).multiply(s3).setScale(3, RoundingMode.DOWN).doubleValue();

    }


}
