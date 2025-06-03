package info.qianqiu.ashechoes.compute.simple;


import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author Nan
 * @desc 计算角色终端属性加成乘区
 */
@Builder
public class TerminalCompute {

    private String 刻印终端;
    private String 场外加成;
    private String 监督终端;


    /**
     * 刻印终端加成 + 场外加成 + 监督终端
     * @return
     */
    public double compute() {
        BigDecimal add = new BigDecimal(刻印终端).add(new BigDecimal(场外加成)).add(new BigDecimal(监督终端));
        return add.setScale(0, RoundingMode.DOWN).doubleValue();
    }

}
