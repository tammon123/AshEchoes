package info.qianqiu.ashechoes.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverviewDataVo {
    //这里面的顺序会影响前端的显示顺序
    private CData 专精增伤 = new CData(0);
    private CData 激励增伤 = new CData(0);
    private CData 基础防御降低 = new CData(3);
    private CData 目标受伤害增加 = new CData(3);
    private CData 抗性降低 = new CData(3);
    private CData 额外攻击百分比 = new CData(1);

    private CData 独立增伤 = new CData(0);
    private CData 专精百分比加成 = new CData(1);
    private CData 攻击力加成 = new CData(1);
    private CData 技能增伤 = new CData(0);

    //属性乘区
    private CData 刻印攻击增加值 = new CData(1);
    private CData 刻印攻击百分比 = new CData(1);
    private CData 额外攻击增加值 = new CData(1);
    private CData 同调者攻击加成 = new CData(1);

    private CData 刻印体质百分比加成 = new CData(1);

    private CData 同调者专精百分比 = new CData(1);
    private CData 刻印专精百分比 = new CData(1);
    private CData 刻印专精固定值 = new CData(1);

    private CData 暴击伤害 = new CData(1);
    private CData 同调者暴击率 = new CData(1);
    private CData 暴击率 = new CData(1);

    //增伤乘区
    private CData 额外增伤 = new CData(0);
    private CData 暴击独立增伤 = new CData(0);

    //减益乘区
    private CData 元素易伤 = new CData(3);

    private CData 无视防御 = new CData(3);

    @AllArgsConstructor
    @Data
    @NoArgsConstructor
    static public class CData {
        CData(int type) {
            this.type = type;
        }

        // 0 代表buff区 1代表属性区 2代表debuff区
        private int type = 0;
        private String total = "0";
        private String limit = "0";
        private ArrayList<String> detail = new ArrayList<>();
    }
}
