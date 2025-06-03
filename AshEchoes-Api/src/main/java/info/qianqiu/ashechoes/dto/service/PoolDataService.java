package info.qianqiu.ashechoes.dto.service;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import info.qianqiu.ashechoes.dto.domain.PoolDataRank;
import info.qianqiu.ashechoes.dto.mapper.PoolDataRankMapper;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.controller.vo.PoolDataUserinfoVo;
import info.qianqiu.ashechoes.controller.vo.PoolDataVo;
import info.qianqiu.ashechoes.dto.domain.PoolData;
import info.qianqiu.ashechoes.dto.domain.User;
import info.qianqiu.ashechoes.dto.mapper.PoolDataMapper;
import info.qianqiu.ashechoes.dto.mapper.UserMapper;
import info.qianqiu.ashechoes.utils.LinkedHashMapReverser;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.id.Id;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import info.qianqiu.ashechoes.utils.thread.VThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.aop.framework.AopContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 默认名称Service业务层处理
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PoolDataService extends ServiceImpl<PoolDataMapper, PoolData> {

    private final static int INSERT_COUNT = 300;
    private final UserService userService;
    private final PoolDataMapper poolDataMapper;
    private final PoolDataRankMapper poolDataRankMapper;
    private final PoolDataRankService poolDataRankService;
    private final UserMapper userMapper;
    private final InitComputeData init;
    private static final String url = "https://comm.ams.game.qq.com/ide/";
    private final ConcurrentHashMap<String, Boolean> userStatusMap = new ConcurrentHashMap<>();

    /**
     * 屏蔽掉常驻非UP卡池
     *
     * @param comm
     */
    private void neCommonPool(LambdaQueryWrapper<PoolData> comm) {
        comm.ne(PoolData::getPool, "限域巡回")
                .ne(PoolData::getPool, "识海甄录·消夏归航")
                .ne(PoolData::getPool, "海域同游")
                .notLikeRight(PoolData::getPool, "常态共鸣")
                .notLikeRight(PoolData::getPool, "联合共鸣")
                .notLikeRight(PoolData::getPool, "寻迹潜航")
                .notLikeRight(PoolData::getPool, "既定回响")
                .notLikeRight(PoolData::getPool, "先觉潜航");
    }

    public R getPoolUserInfo(String uid) {
        User count = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getUid, uid));

        if (count == null) {
            return R.fail("未查询到该用户，请重新注册");
        }

        JSONObject r = new JSONObject();
        User one = userService.getOne(new LambdaQueryWrapper<User>()
                .select(User::getNickname, User::getAvatar)
                .eq(User::getUid, uid));
        // 查找所有的SSR 这里如果卡池更新了，后面也要改
        LambdaQueryWrapper<PoolData> poolDataLambdaQueryWrapper = new LambdaQueryWrapper<PoolData>()
                .select(PoolData::getPool, PoolData::getName, PoolData::getType, PoolData::getRank)
                .eq(PoolData::getUid, uid)
                .in(PoolData::getRank, 3, 6)
                .orderByDesc(PoolData::getTime)
                .orderByDesc(PoolData::getId);
        neCommonPool(poolDataLambdaQueryWrapper);
        List<PoolData> list = list(poolDataLambdaQueryWrapper);
        ArrayList<Long> mflag = new ArrayList<>();
        ArrayList<Long> cflag = new ArrayList<>();
        // 在限定池出限定角色/烙痕的数量
        int xiandinc = 0;
        int xiandinm = 0;
        // 总共在限定池出了多少数量
        for (PoolData pd : list.reversed()) {
            //0是角色 1是烙痕
            if (pd.getType() == 0) {
                if (pd.getName().equals(init.localUpChar.get(pd.getPool()))) {
                    xiandinc += 1;
                    if (!cflag.isEmpty() && cflag.getLast() == 0L) {
                        cflag.add(-1L);
                    } else {
                        cflag.add(1L);
                    }
                } else {
                    cflag.add(0L);
                }
            } else {
                if (pd.getName().equals(pd.getPool())) {
                    xiandinm += 1;
                    if (!mflag.isEmpty() && mflag.getLast() == 0L) {
                        mflag.add(-1L);
                    } else {
                        mflag.add(1L);
                    }
                } else {
                    mflag.add(0L);
                }
            }
        }
        // 计算当前是否歪了 角色
        String c = computeRate(cflag);
        // 计算当前是否歪了 烙痕
        String m = computeRate(mflag);
        // todo 这里是判断用户顶部抽卡数据的地方，如果要屏蔽卡池！一定要改这里！！！！！
        ArrayList<PoolDataUserinfoVo> data = userMapper.groupByUserinfoData(uid);
        r.put("userinfo", one);
        boolean isXianyu = false;
        if (!data.isEmpty()) {
            Map<Integer, List<PoolDataUserinfoVo>> collect =
                    data.stream().collect(Collectors.groupingBy(PoolDataUserinfoVo::getType));
            JSONObject o1 = new JSONObject();
            JSONObject o2 = new JSONObject();
            for (Integer i : collect.keySet()) {
                if (i == 0) {
                    Integer r6count = 0;
                    Integer total = 0;
                    int xdtoTal = 0;
                    for (PoolDataUserinfoVo v : collect.get(i)) {
                        total += v.getCount();
                        if (!v.getPool().contains("常态共鸣") && !v.getPool().contains("识海甄录·消夏归航") &&
                                !v.getPool().contains("联合共鸣")) {
                            xdtoTal += v.getCount();
                        }
                        if (v.getRank() == 6) {
                            r6count += v.getCount();
                        }
                    }
                    o1.put("size", total);
                    if (isXianyu) {
                        total -= 10;
                        o1.put("size", total);
                    }
                    o1.put("r6count", r6count);
                    o1.put("r6xdcount", xiandinc);
                    o1.put("r6xdtotal", xdtoTal);
                    String pj = "";
                    if (xiandinc != 0) {
                        pj = new BigDecimal(xdtoTal)
                                .divide(new BigDecimal(xiandinc), 2, RoundingMode.HALF_UP)
                                .toPlainString();
                    }
                    o1.put("pj", pj);
                    String rate = "";
                    try {
                        rate =
                                new BigDecimal(r6count).multiply(new BigDecimal(100))
                                        .divide(new BigDecimal(total), 2, RoundingMode.HALF_UP)
                                        .toPlainString() + "%";
                    } catch (Exception e) {
                    }

                    o1.put("rate", rate);
                    o1.put("tr", c);
                    r.put("0", o1);
                }
                if (i == 1) {
                    Integer r6count = 0;
                    Integer total = 0;
                    int xdtotal = 0;
                    for (PoolDataUserinfoVo v : collect.get(i)) {
                        total += v.getCount();
                        if (!v.getPool().contains("寻迹潜航") && !v.getPool().contains("先觉潜航")) {
                            xdtotal += v.getCount();
                        }
                        if (v.getRank() == 3) {
                            r6count += v.getCount();
                        }
                    }
                    o2.put("size", total);
                    o2.put("r6count", r6count);
                    o2.put("r6xdcount", xiandinm);
                    o2.put("r6xdtotal", xdtotal);
                    String pj = "";
                    if (xiandinm != 0) {
                        pj = new BigDecimal(xdtotal)
                                .divide(new BigDecimal(xiandinm), 2, RoundingMode.HALF_UP)
                                .toPlainString();
                    }
                    o2.put("pj", pj);
                    String rate = "";
                    try {
                        rate =
                                new BigDecimal(r6count).multiply(new BigDecimal(100))
                                        .divide(new BigDecimal(total), 2, RoundingMode.HALF_UP)
                                        .toPlainString() + "%";
                    } catch (Exception e) {
                    }
                    o2.put("rate", rate);
                    o2.put("tr", m);
                    r.put("1", o2);
                }
            }

            initPoolDataRank(r, uid);
            long ccc = poolDataRankService.count(
                    new LambdaQueryWrapper<PoolDataRank>().eq(PoolDataRank::getAllow, 1).eq(PoolDataRank::getUid, uid));
            r.put("allow", ccc);

            return R.ok(r);
        }


        return R.fail("没有数据，请下载APP或在网页右上角导入数据");
    }

    public R getPagePoolDataRank(int type, int page, int size, int sort, int ignore, String beh, String uid) {
        LambdaQueryWrapper<PoolDataRank> lambda = new LambdaQueryWrapper<PoolDataRank>()
                .orderBy("tRate".equals(beh), sort == 1, PoolDataRank::getTRate)
                .orderBy("tCount".equals(beh), sort == 1, PoolDataRank::getTCount)
                .orderBy("uTr".equals(beh), sort == 1, PoolDataRank::getUTr)
                .orderBy("tAvg".equals(beh), sort == 1, PoolDataRank::getTAvg)
                .orderBy("uAvg".equals(beh), sort == 1, PoolDataRank::getUAvg)
                .gt(PoolDataRank::getTCount, ignore)
                .eq(PoolDataRank::getAllow, 1)
                .eq(PoolDataRank::getPoolType, type);

        long count = poolDataRankService.count(new LambdaQueryWrapper<PoolDataRank>()
                .eq(PoolDataRank::getPoolType, type)
                .gt(PoolDataRank::getTCount, ignore)
                .eq(PoolDataRank::getAllow, 1));

        if ("tRate".equals(beh)) beh = "t_rate";
        if ("tCount".equals(beh)) beh = "t_count";
        if ("uTr".equals(beh)) beh = "u_tr";
        if ("tAvg".equals(beh)) beh = "t_avg";
        if ("uAvg".equals(beh)) beh = "u_avg";
        Integer rank = poolDataRankMapper.getUserRank(type, sort, beh, uid, ignore);

        if (size >= 50) {
            return R.fail("分页大小过大~");
        }
        Page<PoolDataRank> page1 =
                poolDataRankService.page(new Page<PoolDataRank>(page, size), lambda);

        if (page1.getRecords().isEmpty()) {
            return R.fail("筛选条件过高，请调整");
        }

        List<PoolDataRank> records = page1.getRecords();
        for (PoolDataRank c : records) {
            c.setUid("****" + c.getUid().substring(c.getUid().length() - 8));
        }
        JSONObject r = new JSONObject();
        BigDecimal divide = new BigDecimal(rank - 1).divide(new BigDecimal(count - 1), 4, RoundingMode.HALF_UP);
        BigDecimal multiply = new BigDecimal("1").subtract(divide).multiply(new BigDecimal(100));

        r.put("ar", records);
        r.put("t", count);
        r.put("r", rank == 0 ? 0 : rank + 1);
        String uuid = "****" + uid.substring(uid.length() - 8);
        if (records.getFirst().getUid().equals(uuid)) {
            r.put("r", 1);
        }
        r.put("e", multiply);
        r.put("ti", uuid);
        return R.ok(r);
    }

    private void initPoolDataRank(JSONObject j, String uid) {

        VThread.submit(() -> {
            Integer re = poolDataRankMapper.queryTodayUpdated(new Date(), uid);
            JSONObject chars = j.getJSONObject("0");
            JSONObject memory = j.getJSONObject("1");
            // 数据库没有该用户的数据，需要增加
            // 添加记录
            PoolDataRank charr = PoolDataRank.builder()
                    .pdrId(Id.id()).poolType(0L)
                    .uAvg(chars.getString("pj"))
                    .tAvg(new BigDecimal(chars.getString("size"))
                            .divide(new BigDecimal(chars.getString("r6count")), 2, RoundingMode.HALF_UP)
                            .toPlainString())
                    .tRate(chars.getString("rate").replaceAll("%", ""))
                    .uTr(chars.getString("tr").replaceAll("%", ""))
                    .tCount(chars.getLong("size")).tHasCount(chars.getLong("r6count"))
                    .upCount(chars.getLong("r6xdtotal")).upHasCount(chars.getLong("r6xdcount")).uid(uid)
                    .allow(0L).startTime(new Date()).lastTime(new Date())
                    .build();
            // 添加记录
            PoolDataRank memorys = PoolDataRank.builder()
                    .pdrId(Id.id()).poolType(1L).tCount(memory.getLong("size"))
                    .uAvg(memory.getString("pj"))
                    .tAvg(new BigDecimal(memory.getString("size"))
                            .divide(new BigDecimal(memory.getString("r6count")), 1, RoundingMode.HALF_UP)
                            .toPlainString())
                    .tRate(memory.getString("rate").replaceAll("%", ""))
                    .uTr(memory.getString("tr").replaceAll("%", ""))
                    .tHasCount(memory.getLong("r6count"))
                    .upCount(memory.getLong("r6xdtotal")).upHasCount(memory.getLong("r6xdcount")).uid(uid)
                    .allow(0L).startTime(new Date()).lastTime(new Date())
                    .build();
            ArrayList<PoolDataRank> poolDataRanks = new ArrayList<>();
            poolDataRanks.add(charr);
            poolDataRanks.add(memorys);
            if (re == null) {
                poolDataRankService.saveBatch(poolDataRanks);
            } else {
                // 最后一次数据更新非今天
                List<PoolDataRank> list = poolDataRankService.list(new LambdaQueryWrapper<PoolDataRank>()
                        .select(PoolDataRank::getUid, PoolDataRank::getPoolType, PoolDataRank::getPdrId)
                        .eq(PoolDataRank::getUid, uid));
                for (PoolDataRank pdr : list) {
                    if (pdr.getPoolType() == 0L) {
                        charr.setStartTime(null);
                        charr.setPdrId(pdr.getPdrId());
                        charr.setAllow(null);
                    } else {
                        memorys.setStartTime(null);
                        memorys.setPdrId(pdr.getPdrId());
                        memorys.setAllow(null);
                    }
                }
                poolDataRankService.updateBatchById(poolDataRanks);
            }
        });
    }

    /**
     * 获取所有抽到得数据
     *
     * @return
     */
    public R getAllRankData(String uid) {
        List<PoolData> list =
                list(new LambdaQueryWrapper<PoolData>().select(PoolData::getPool, PoolData::getName, PoolData::getType,
                                PoolData::getTime)
                        .eq(PoolData::getUid, uid).in(PoolData::getRank, 3, 6)
                        .orderByAsc(PoolData::getTime));

        LinkedHashMap<String, JSONObject> data = new LinkedHashMap<>();
        for (PoolData pd : list) {
            JSONObject j = data.get(pd.getName());
            if (j == null) {
                j = new JSONObject();
                j.put("c", 1);
                ArrayList<PoolData> poolData = new ArrayList<>();
                poolData.add(pd);
                j.put("r", poolData);
            } else {
                ArrayList<PoolData> o = (ArrayList<PoolData>) j.get("r");
                o.add(pd);
                j.put("r", o);
                j.put("c", j.getIntValue("c") + 1);
            }
            String avatar;
            if (pd.getType() == 0) {
                avatar = init.getSimpleCharacterByNameGroup(pd.getName()).getAvatar();
            } else {
                avatar = init.getSimpleMemoryByNameGroup(pd.getName()).getImg();
            }
            j.put("a", avatar);
            j.put("t", pd.getType());
            data.put(pd.getName(), j);

        }

        Collection<JSONObject> values = data.values();
        for (JSONObject o : values) {
            ArrayList<PoolData> poolDatas = (ArrayList<PoolData>) o.get("r");
            for (PoolData poolData : poolDatas) {
                poolData.setName(null);
            }
            o.put("r", poolDatas.reversed());
        }

        return R.ok(data.reversed());
    }

    public R getPoolGroupData(String uid, String type) {
        // 屠苏 红玉池子要特殊处理  岁暮重明 玉照长夜，陈酒新酌
        LambdaQueryWrapper<PoolData> poolDataLambdaQueryWrapper = new LambdaQueryWrapper<PoolData>()
                .select(PoolData::getPool, PoolData::getId, PoolData::getTime, PoolData::getName, PoolData::getRank)
                .eq(PoolData::getUid, uid)
                .orderByDesc(PoolData::getTime)
                .orderByDesc(PoolData::getId);
        neCommonPool(poolDataLambdaQueryWrapper);
        boolean isCharPool = "0".equals(type);
        if (isCharPool) {
            poolDataLambdaQueryWrapper.eq(PoolData::getType, 0);
        } else {
            poolDataLambdaQueryWrapper.eq(PoolData::getType, 1);
        }
        List<PoolData> list = list(poolDataLambdaQueryWrapper);

        if (list.isEmpty()) {
            return R.fail("没有找到数据，数据可能未加载完成，请等待完成后在来~");
        }
        LinkedHashMap<String, List<PoolData>> groupPoolList = list.stream()
                .collect(Collectors.groupingBy(PoolData::getPool, LinkedHashMap::new, Collectors.toList()));
        List<PoolData> sp1 = groupPoolList.get("玉照长夜，陈酒新酌");
        List<PoolData> sp2 = groupPoolList.get("岁暮重明");
        List<PoolData> sp3 = groupPoolList.get("游光澄明");
        List<PoolData> spa = new ArrayList<>();
        if (sp1 != null) {
            spa.addAll(sp1);
        }
        if (sp2 != null) {
            spa.addAll(sp2);
        }
        spa.sort(Comparator.comparing(PoolData::getTime)
                .thenComparing(PoolData::getId).reversed());
        groupPoolList.remove("玉照长夜，陈酒新酌");
        groupPoolList.remove("岁暮重明");
        groupPoolList.remove("游光澄明");
        groupPoolList.put("玉照长夜，陈酒新酌 岁暮重明", spa);
        groupPoolList.put("游光澄明", sp3);

        LinkedHashMap<String, List<PoolData>> oolist =
                LinkedHashMapReverser.reverseLinkedHashMap(groupPoolList);
        JSONObject jos = new JSONObject();
        AtomicReference<Long> lastUp = new AtomicReference<>(null);
        for (String pool : oolist.keySet()) {
            List<PoolData> poolData = oolist.get(pool);
            if (poolData != null && !poolData.isEmpty()) {
                JSONObject jsonObject = groupByPoolName(poolData, type, lastUp);
                jos.put(pool, jsonObject);
            }
        }

        return R.ok(jos);
    }

    private JSONObject groupByPoolName(List<PoolData> list, String type, AtomicReference<Long> lastUp) {
        JSONObject jo = new JSONObject();
        // 上期抽到了当期UP
        int count = 0;
        List<PoolDataVo> pools = new ArrayList<>();
        // 记录是否为大保底
        ArrayList<Long> rData = new ArrayList<>();
        if (lastUp.get() != null) {
            rData.add(lastUp.get());
        }
        for (PoolData pd : list.reversed()) {
            // 总计算从1开始好点
            count += 1;
            if (pd.getRank() % 3 == 0) {
                PoolDataVo j = new PoolDataVo();
                long tempBaodiFlag = 0;
                //0 就是歪了
                j.setName(pd.getName());
                j.setCount(count);
                j.setTime(pd.getTime());
                j.setPpol(pd.getPool());
                if ("玉照长夜，陈酒新酌 岁暮重明".contains(pd.getPool())) {
                    j.setPpol("玉照长夜，陈酒新酌 岁暮重明");
                }
                j.setId(pd.getId());
                // 同调者
                if ("0".equals(type)) {
                    if (pd.getName().equalsIgnoreCase(init.localUpChar.getString(pd.getPool()))) {
                        // 抽到了，但是需要看看前一个是不是0,这个还是大保底
                        if (!rData.isEmpty() && rData.getLast() == 0L) {
                            tempBaodiFlag = -1L;
                        } else {
                            tempBaodiFlag = 1L;
                        }
                    } else if ("玉照长夜，陈酒新酌 岁暮重明".contains(pd.getPool())) {
                        if ("百里屠苏、红玉".contains(pd.getName())) {
                            if (!rData.isEmpty() && rData.getLast() == 0L) {
                                tempBaodiFlag = -1L;
                            } else {
                                tempBaodiFlag = 1L;
                            }
                        }
                    }
                    j.setAvatar(init.getCharacterAvatar(pd.getName()));
                } else if ("1".equals(type)) {
                    if (pd.getPool().equals(pd.getName())) {
                        // 抽到了，但是需要看看前一个是不是0,这个还是大保底
                        if (!rData.isEmpty() && rData.getLast() == 0L) {
                            tempBaodiFlag = -1L;
                        } else {
                            tempBaodiFlag = 1L;
                        }
                    }
                    j.setAvatar(init.getMemoryAvatar(pd.getName()));
                }
                rData.add(tempBaodiFlag);
                String text = "";
                if (tempBaodiFlag == -1) {
                    text = "";
                } else if (tempBaodiFlag == 1) {
                    text = "";
                } else {
                    text = "歪";
                }
                j.setInit(text);
                try {
                    pools.add(j);
                } catch (Exception e) {
                }
                count = 0;
            }
        }
        JSONObject info = new JSONObject();

        int total = list.size();
        info.put("total", list.size());
        if (list.size() != rData.size()) {
            if (rData != null && rData.size() > 1) {
                rData.removeFirst();
            }
        }
        String rate = computeRate(rData);
        info.put("rate", rate);
        info.put("win", rData.size());
        info.put("dian", count);
        if (total == 0 || pools.isEmpty()) {
            info.put("avg", 0);
        } else {
            List<PoolDataVo> upNum = pools.stream().filter(e -> !e.getInit().equals("歪")).toList();
            if (upNum == null || upNum.isEmpty()) {
                info.put("avg", 0);
            } else {
                info.put("avg", new BigDecimal(total).divide(new BigDecimal(upNum.size()), 2, RoundingMode.HALF_UP));
            }
        }
        info.put("pool", list.getFirst().getPool());
        jo.put("info", info);
        jo.put("ui", pools.reversed());
        if (!rData.isEmpty()) {
            lastUp.set(rData.getLast());
        }
        return jo;
    }

    public R getNewAllPoolData(String uid, String type) {
        JSONObject jo = new JSONObject();
        // 屠苏 红玉池子要特殊处理  岁暮重明 玉照长夜，陈酒新酌
        LambdaQueryWrapper<PoolData> poolDataLambdaQueryWrapper = new LambdaQueryWrapper<PoolData>()
                .select(PoolData::getPool, PoolData::getId, PoolData::getTime, PoolData::getName, PoolData::getRank)
                .eq(PoolData::getUid, uid)
                .orderByDesc(PoolData::getTime)
                .orderByDesc(PoolData::getId)
                .ne(PoolData::getPool, "海域同游")
                .notLikeRight(PoolData::getPool, "限域巡回")
                .notLikeRight(PoolData::getPool, "既定回响");
        if (!"6".equals(type)) {
            poolDataLambdaQueryWrapper.ne(PoolData::getPool, "识海甄录·消夏归航");
        }
        boolean isCharPool = ("0".equals(type) || "2".equals(type) || "4".equals(type) || "6".equals(type));
        if (isCharPool) {
            poolDataLambdaQueryWrapper.eq(PoolData::getType, 0);
        } else {
            poolDataLambdaQueryWrapper.eq(PoolData::getType, 1);
        }
        boolean isCz = Integer.parseInt(type) >= 2;
        if (isCz) {
            if (isCharPool) {
                if ("2".equals(type)) {
                    poolDataLambdaQueryWrapper.likeRight(PoolData::getPool, "常态共鸣");
                } else if ("4".equals(type)) {
                    poolDataLambdaQueryWrapper.likeRight(PoolData::getPool, "联合共鸣");
                } else {
                    poolDataLambdaQueryWrapper.eq(PoolData::getPool, "识海甄录·消夏归航");
                }
            } else {
                poolDataLambdaQueryWrapper.and(
                        e -> e.likeRight(PoolData::getPool, "寻迹潜航").or().likeRight(PoolData::getPool, "先觉潜航"));
            }
        } else {
            neCommonPool(poolDataLambdaQueryWrapper);
        }
        List<PoolData> list = list(poolDataLambdaQueryWrapper);

        if (list.isEmpty()) {
            return R.fail("没有找到数据，数据可能未加载完成，请等待完成后在来~");
        }
        LinkedHashMap<String, List<PoolDataVo>> pools = new LinkedHashMap<>();
        for (PoolData pool : list) {
            if (!pools.containsKey(pool.getPool())) {
                pools.put(pool.getPool(), new ArrayList<>());
            }
        }
        // 上期抽到了当期UP
        int count = 0;
        // 记录是否为大保底
        ArrayList<Long> rData = new ArrayList<>();
        for (PoolData pd : list.reversed()) {
            // 总计算从1开始好点
            count += 1;
            if (pd.getRank() % 3 == 0) {
                PoolDataVo j = new PoolDataVo();
                long tempBaodiFlag = 0;
                j.setName(pd.getName());
                j.setCount(count);
                j.setTime(pd.getTime());
                j.setPpol(pd.getPool());
                j.setId(pd.getId());
                // 同调者
                if (isCharPool) {
                    if (pd.getName().equalsIgnoreCase(init.localUpChar.getString(pd.getPool()))) {
                        // 抽到了，但是需要看看前一个是不是0,这个还是大保底
                        if (!rData.isEmpty() && rData.getLast() == 0L) {
                            tempBaodiFlag = -1L;
                        } else {
                            tempBaodiFlag = 1L;
                        }
                    }
                    if (isCz) {
                        tempBaodiFlag = 1L;
                    }
                    j.setAvatar(init.getCharacterAvatar(pd.getName()));
                } else {
                    if (pd.getPool().equals(pd.getName())) {
                        // 抽到了，但是需要看看前一个是不是0,这个还是大保底
                        if (!rData.isEmpty() && rData.getLast() == 0L) {
                            tempBaodiFlag = -1L;
                        } else {
                            tempBaodiFlag = 1L;
                        }
                    }
                    if (isCz) {
                        tempBaodiFlag = 1L;
                    }
                    j.setAvatar(init.getMemoryAvatar(pd.getName()));
                }
                rData.add(tempBaodiFlag);
                String text = "";
                if (tempBaodiFlag == -1) {
                    text = "";
                } else if (tempBaodiFlag == 1) {
                    text = "";
                } else {
                    text = "歪";
                }
                j.setInit(text);
                try {
                    pools.get(j.getPpol()).add(j);
                } catch (Exception e) {

                }
                count = 0;
            }
        }
        JSONObject info = new JSONObject();

        info.put("total", list.size());
        String rate = computeRate(rData);

        info.put("rate", rate);
        info.put("win", rData.size());
        info.put("dian", count);
        jo.put("info", info);
        for (String s : pools.keySet()) {
            pools.put(s, pools.get(s).reversed());
        }
        List<PoolDataVo> 游光澄明 = pools.get("游光澄明");
        List<PoolDataVo> c1 = pools.get("岁暮重明");
        List<PoolDataVo> c2 = pools.get("玉照长夜，陈酒新酌");
        if (c1 != null && c2 != null) {
            c1.addAll(c2);
            List<PoolDataVo> collect = c1.stream().sorted(Comparator.comparing(PoolDataVo::getTime).reversed()
                            .thenComparing(Comparator.comparing(PoolDataVo::getId).reversed()))
                    .collect(Collectors.toList());
            // 删除岑樱池子，是因为排序问题
            pools.remove("游光澄明");
            pools.remove("岁暮重明");
            pools.remove("玉照长夜，陈酒新酌");
            pools.put("玉照长夜，陈酒新酌 岁暮重明", collect);
            pools.put("游光澄明", 游光澄明);
        }
        jo.put("ui", pools);
        return R.ok(jo);
    }

    private String computeRate(ArrayList<Long> rdata) {
        int f = 0;
        int s = 0;
        for (Long a : rdata) {
            if (a == 1L) {
                s += 1;
            }
            if (a == 0L) {
                f += 1;
            }
        }
        String rate = "0";
        try {
            rate = new BigDecimal(s).multiply(new BigDecimal(100))
                    .divide(new BigDecimal(f + s), 2, RoundingMode.HALF_UP)
                    .toPlainString() + "%";
        } catch (Exception e) {
        }
        return rate;
    }

    public void postData(String uid, String data) {
        log.error("安卓用户{}申请导入", uid);
        ArrayList<PoolData> poolData = new ArrayList<>();
        JSONArray jr = JSONArray.parse(data);
        log.error("用户{}总预插入数据:{}条", uid, jr.size());
        int count = 0;
        for (Object o : jr) {
            JSONObject e = (JSONObject) o;
            PoolData pd = new PoolData();
            pd.setUid(uid);
            pd.setType(e.getByte("e"));
            long time = e.getLong("i") * 1000L;
            pd.setTime(DateUtil.date(time));
            pd.setName(e.getString("t"));
            pd.setPool(e.getString("p"));
            pd.setRank(e.getByte("r"));
            Long id = e.getLong("l");
            if (id == null) {
                id = Id.id();
            }
            pd.setId(id);
            poolData.add(pd);
            // 每收集300条记录就进行一次批量保存
            if (poolData.size() >= INSERT_COUNT) {
                count += INSERT_COUNT;
                log.error("用户{}插入:{}条", uid, count);
                ((PoolDataService) AopContext.currentProxy()).saveBatch(poolData); // 批量保存
                poolData.clear(); // 清空列表以准备下一批
            }
        }

        // 处理剩余的数据（如果不足100条）
        if (!poolData.isEmpty()) {
            log.error("用户{}总插入数据结束:{}条", uid, count + poolData.size());
            ((PoolDataService) AopContext.currentProxy()).saveBatch(poolData); // 保存最后一批数据
        }
    }

    public R postDataByToken(String uid, String token) {
        log.error("H5用户{}申请导入:{}", uid, token);
        boolean b = userStatusMap.putIfAbsent(uid, true) == null;
        if (!b) {
            return R.fail("请勿重复提交");
        }
        try {
            User byId = userService.getById(uid);
            if (byId == null) {
                return R.fail("该用户不存在");
            }
            if (StringUtils.isEmpty(token)) {

                token = byId.getToken();
                if (StringUtils.isEmpty(token)) {
                    return R.fail("未检测到token，请重新导入token");
                }
            }
            String cookie =
                    "token=" + token; // 替换为实际的Cookie值
            List<PoolData> records = page(new Page<>(1, 1), new LambdaQueryWrapper<PoolData>()
                    .eq(PoolData::getUid, uid)
                    .orderByDesc(PoolData::getTime)
                    .orderByDesc(PoolData::getId)).getRecords();
            long starttime = getInitStartOfDayInSeconds();
            if (!records.isEmpty()) {
                TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
                Calendar c = Calendar.getInstance();
                c.setTime(records.getFirst().getTime());
                c.setTimeZone(chinaTimeZone);
                c.set(Calendar.HOUR_OF_DAY, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND, 0);
                c.set(Calendar.MILLISECOND, 0);
                c.add(Calendar.DAY_OF_MONTH, 1);
                log.warn("{}:当前增量更新时间:{}", uid, DateUtil.formatDate(c.getTime()));
                starttime = c.getTimeInMillis() / 1000;// 替换为实际的开始时间
            }
            long endtime = starttime + 86400 * 30;   // 替换为实际的结束时间
            ArrayList<PoolData> poolData = new ArrayList<>();
            AtomicReference<String> lastCharKey = new AtomicReference<String>("");
            AtomicReference<String> lastMemoryKey = new AtomicReference<String>("");
            while (starttime < getStartOfDayInSeconds()) {
                ArrayList<PoolData> memoryData = getMemoryData(starttime, endtime, cookie, uid, lastMemoryKey);
                ArrayList<PoolData> charsData = getCharData(starttime, endtime, cookie, uid, lastCharKey);
                if (memoryData != null && !memoryData.isEmpty()) {
                    poolData.addAll(memoryData);
                }
                if (charsData != null && !charsData.isEmpty()) {
                    poolData.addAll(charsData);
                }
                starttime = endtime;
                endtime += 86400 * 30;
            }
            VThread.submit(() -> {
                try {
                    log.error("用户{}总预插入数据:{}条", uid, poolData.size());
                    int count = 0;
                    ArrayList<PoolData> temp = new ArrayList<>();
                    for (PoolData o : poolData) {
                        temp.add(o);
                        // 每收集300条记录就进行一次批量保存
                        if (temp.size() >= INSERT_COUNT) {
                            count += INSERT_COUNT;
                            log.error("用户{}插入:{}条", uid, count);
                            poolDataMapper.insert(temp); // 批量保存
                            temp.clear(); // 清空列表以准备下一批
                        }
                    }
                    // 处理剩余的数据（如果不足100条）
                    if (!temp.isEmpty()) {
                        log.error("用户{}总插入数据结束:{}条", uid, count + temp.size());
                        poolDataMapper.insert(temp); // 批量保存
                    }
                } catch (Exception e) {

                } finally {
                    userStatusMap.remove(uid);
                }
            });
            userService.update(new LambdaUpdateWrapper<User>().set(User::getToken, token).eq(User::getUid, uid));
            return R.ok();
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            e.printStackTrace();
        } finally {
        }
        userStatusMap.remove(uid);
        return R.fail("数据导入失败，请联系开发人员");

    }

    /**
     * 获取当前用户抽了多少SR和SSR卡
     *
     * @param uid
     * @return
     */
    public R getMemoryGroupCountByUid(String uid) {
        List<PoolDataVo> vos = poolDataRankMapper.getMemoryGroupCountByUid(uid);
        for (PoolDataVo vo : vos) {
            vo.setId(init.getSimpleMemoryByNameGroup(vo.getName()).getMemoryId());
            vo.setName(null);
        }
        return R.ok(vos);
    }

    // 获取当天开始的秒数
    Long getStartOfDayInSeconds() {
        Calendar calendar = Calendar.getInstance();
        // 设置时间为当天的开始，即 00:00:00
        val chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        calendar.setTimeZone(chinaTimeZone);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取从 1970 年 1 月 1 日 00:00:00 UTC 到现在的总毫秒数，然后转换为秒
        return calendar.getTimeInMillis() / 1000;
    }

    Long getInitStartOfDayInSeconds() {
        val calendar = Calendar.getInstance();
        val chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        calendar.setTimeZone(chinaTimeZone);
        // 设置年、月、日为指定的日期
        // 注意:月份是从 0 开始计数的，所以1月对应的是 0
        calendar.set(2024, 0, 12);
        // 设置时间为当天的开始，即 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取从 1970 年 1 月 1 日 00:00:00 UTC 到指定日期开始时刻的总毫秒数，然后转换为秒
        return calendar.getTimeInMillis() / 1000;
    }

    public ArrayList<PoolData> getCharData(long starttime, long endtime, String cookie, String uid,
                                           AtomicReference<String> lastCharKey) {
        // 定义URL
        // 构建请求体
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("iChartId", "323543");
        formData.add("iSubChartId", "323543");
        //mhi97c 角色  Q4rDBY
        formData.add("sIdeToken", "mhi97c");
        formData.add("startTime", Long.toString(starttime));
        formData.add("endTime", Long.toString(endtime));

        // 创建 HttpHeaders 并设置头部
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookie);
        headers.set("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 AshEchoesAssistant");
        // 创建 HttpEntity，包含头部和请求体
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        // 使用 RestTemplate 发送请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );
        JSONObject r = JSONObject.parseObject(response.getBody());
        JSONObject jsonArray = r.getJSONObject("jData").getJSONObject("data");
        ArrayList<PoolData> result = new ArrayList<>();
        try {
            Set<String> strings = jsonArray.keySet();
            for (String key : strings) {
                // 当前数据重复
                if (lastCharKey.get().equals(key)) {
                    log.error("角色重复{}:{}:{}", uid, key, jsonArray.getJSONArray(key));
                    continue;
                }
                for (Object o : jsonArray.getJSONArray(key)) {
                    JSONObject oo = (JSONObject) o;
                    JSONObject cc = init.charsData.getJSONObject(oo.getString("tid"));
                    if (cc == null) {
                        cc = new JSONObject();
                        cc.put("rarity", 6);
                        cc.put("name", "角色" + oo.getString("tid") + "(待开发者同步)");
                    }
                    JSONObject pol = init.poolData.getJSONObject(oo.getString("poolId"));
                    if (pol == null) {
                        log.error("没有找到对应角色池子:{}", oo.getString("poolId"));
                        pol = new JSONObject();
                        pol.put("name", "角色池" + oo.getString("poolId") + "(待开发者同步)");
                    }
                    String time = oo.getString("time");
                    PoolData pd = new PoolData();
                    pd.setId(Id.id());
                    pd.setUid(uid);
                    pd.setType((byte) 0);
                    pd.setTime(DateUtil.parseDateTime(time));
                    pd.setRank(cc.getByte("rarity"));
                    pd.setName(cc.getString("name"));
                    pd.setPool(pol.getString("name"));
                    result.add(pd);
                }
                lastCharKey.set(key);
            }
        } catch (Exception e) {
            log.info("{}", e.getMessage());
            e.printStackTrace();
            log.error("人物数据导入出错{}:{}:{}", uid, starttime, endtime);
        }

        // 打印响应
        return result;
    }

    public ArrayList<PoolData> getMemoryData(long starttime, long endtime, String cookie, String uid,
                                             AtomicReference<String> lastMemoryKey) {
        // 构建请求体
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("iChartId", "323691");
        formData.add("iSubChartId", "323691");
        //mhi97c 角色  Q4rDBY
        formData.add("sIdeToken", "Q4rDBY");
        formData.add("startTime", Long.toString(starttime));
        formData.add("endTime", Long.toString(endtime));

        // 创建 HttpHeaders 并设置头部
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", cookie);
        headers.set("user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 AshEchoesAssistant");

        // 创建 HttpEntity，包含头部和请求体
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(formData, headers);

        // 使用 RestTemplate 发送请求
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                String.class
        );

        JSONObject r = JSONObject.parseObject(response.getBody());
        ArrayList<PoolData> result = new ArrayList<>();
        JSONObject jsonArray = r.getJSONObject("jData").getJSONObject("data");
        try {

            Set<String> strings = jsonArray.keySet();
            for (String key : strings) {
                // 当前数据重复
                if (lastMemoryKey.get().equals(key)) {
                    log.error("烙痕重复:{}:{}:{}", uid, key, jsonArray.getJSONArray(key));
                    continue;
                }
                for (Object o : jsonArray.getJSONArray(key)) {
                    JSONObject oo = (JSONObject) o;
                    try {
                        JSONObject cc = init.memoryData.getJSONObject(oo.getString("tid"));
                        if (cc == null) {
                            cc = new JSONObject();
                            cc.put("rarity", 3);
                            cc.put("name", "烙痕" + oo.getString("tid") + "(待开发者同步)");
                        }
                        JSONObject pol = init.poolData.getJSONObject(oo.getString("poolId"));
                        if (pol == null) {
                            log.error("没有找到对应烙痕池子:{}", oo.getString("poolId"));
                            pol = new JSONObject();
                            pol.put("name", "烙痕池" + oo.getString("poolId") + "(待开发者同步)");
                        }
                        String time = oo.getString("time");
                        PoolData pd = new PoolData();
                        pd.setId(Id.id());
                        pd.setType((byte) 1);
                        pd.setUid(uid);
                        pd.setTime(DateUtil.parseDateTime(time));
                        pd.setRank(cc.getByte("rarity"));
                        pd.setName(cc.getString("name"));
                        pd.setPool(pol.getString("name"));
                        result.add(pd);
                    } catch (Exception e) {
                        log.info("{}", e.getMessage());
                        e.printStackTrace();
                    }
                }
                lastMemoryKey.set(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("烙痕数据导入出错{}:{}:{}", uid, starttime, endtime);
        }
        return result;
    }
}
