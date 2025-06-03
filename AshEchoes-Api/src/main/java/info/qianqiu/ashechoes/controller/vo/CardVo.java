package info.qianqiu.ashechoes.controller.vo;

import info.qianqiu.ashechoes.compute.patch.sync.SyncData;
import lombok.Data;

@Data
public class CardVo {

    //技能ID_技能等级,作为分隔符
    private String skillLevel;
    // 潜像系统
    private String sp2skillLevel;
    //声望等级
    private Byte rLevel = 0;
    private Byte smsg = 0;
    // 潜能等级
    private String pLevel;
    //消融、基础防御
    private Long dfy = 0L;
    //独立减伤
    private String djs = "0";
    // 敌方抗性
    private String dkx = "";
    // 地方弱点
    private String drd = "";
    //终端增幅
    private Boolean zf = false;
    //同调特性
    private Boolean td = false;
    //100%暴击
    private Boolean abj = false;
    // 体质,防御,攻击,专精,终端
    private String attribute;
    // 监督体质,防御,攻击,专精,终端
    private String eattribute;
    private String characters;
    private String memorys;
    private String env;
    private String flower;
    private boolean compare = false;
    private boolean overview = false;
    private int coverview = 0;
    private String critValue = "0";
    private SyncData sync;
    // 全局临时属性
    private int currentCharIndex;

}
