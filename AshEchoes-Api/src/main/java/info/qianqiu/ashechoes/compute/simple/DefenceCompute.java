package info.qianqiu.ashechoes.compute.simple;


import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Nan
 * @desc 计算角色攻击属性加成乘区
 */
@Builder
public class DefenceCompute {

    private String 刻印防御;
    private String 场外加成;
    private String 监督防御;


    /**
     * 刻印终端加成 + 场外加成 + 监督终端
     * @return
     */
    public double compute() {
        BigDecimal add = new BigDecimal(刻印防御).add(new BigDecimal(场外加成)).add(new BigDecimal(监督防御));
        return add.setScale(0, RoundingMode.DOWN).doubleValue();
    }

}
