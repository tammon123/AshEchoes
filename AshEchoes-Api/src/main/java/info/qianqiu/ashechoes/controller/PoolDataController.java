package info.qianqiu.ashechoes.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import info.qianqiu.ashechoes.dto.domain.*;
import info.qianqiu.ashechoes.dto.service.PoolDataRankService;
import info.qianqiu.ashechoes.dto.service.PoolDataService;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.http.ReqUtils;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import info.qianqiu.ashechoes.utils.thread.VThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 这里主要是抽卡数据交互
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class PoolDataController {

    private final PoolDataService poolDataService;
    private final PoolDataRankService poolDataRankService;

    /**
     * 获取所有卡池信息
     *
     * @return
     */
    @GetMapping("/pool/all")
    public R getAllPool() {
        String json = ReqUtils.get("https://bjhl.qianqiu.info/pool.json");
        JSONObject rr = JSONObject.parseObject(json);
        ArrayList<JSONObject> r = new ArrayList<>();
        for (String s : rr.keySet()) {
            JSONObject jsonObject = rr.getJSONObject(s);
            if (jsonObject.getString("time") != null) {
                if (jsonObject.getString("name").equals("岁暮重明")) {
                    continue;
                }
                if (jsonObject.getString("name").equals("玉照长夜，陈酒新酌")) {
                    continue;
                }
                r.add(jsonObject);
            }
        }
        // 屠苏池子 红玉池子 特殊处理
        JSONObject ts = new JSONObject();
        ts.put("name", "玉照长夜，陈酒新酌 岁暮重明");
        ts.put("type", "1");
        ts.put("time", "2024.02.01 10:00 ~ 2024.02.22 03:50");
        r.add(2, ts);
        return R.ok(r);
    }

    /**
     * 获取当前用户抽了多少SR和SSR卡
     * @param uid
     * @return
     */
    @GetMapping("/pool/memory/groupCount/{uid}")
    public R getMemoryGroupCount(@PathVariable("uid") String uid) {
        if ("undefined".equals(uid)) {
            return R.fail("当前账号异常，请重新登录~");
        }
        try {
            long l = Long.parseLong(uid);
        } catch (Exception e) {
            return R.fail("当前账号异常，请重新登录~");
        }
        return poolDataService.getMemoryGroupCountByUid(uid);
    }

    /**
     * 抽卡页面顶部信息概览
      */
    @GetMapping("/pool/userinfo/{uid}")
    public R getPoolUserInfo(@PathVariable("uid") String uid) {
        if (StringUtils.isEmpty(uid)) {
            return R.fail("参数缺失");
        }
        if ("undefined".equals(uid)) {
            return R.fail("当前账号异常，请重新登录~");
        }
        try {
            long l = Long.parseLong(uid);
        } catch (Exception e) {
            return R.fail("当前账号异常，请重新登录~");
        }
        return poolDataService.getPoolUserInfo(uid);
    }

    @GetMapping("/pool/rank/agree/{uid}")
    public R agreeRank(@PathVariable("uid") String uid) {
        return R.ok(poolDataRankService.update(
                new LambdaUpdateWrapper<PoolDataRank>().set(PoolDataRank::getAllow, 1).eq(PoolDataRank::getUid, uid)));
    }

    @GetMapping("/pool/rank/cancel/{uid}")
    public R cancelRank(@PathVariable("uid") String uid) {
        return R.ok(poolDataRankService.update(
                new LambdaUpdateWrapper<PoolDataRank>().set(PoolDataRank::getAllow, 0).eq(PoolDataRank::getUid, uid)));
    }

    @GetMapping("/poolrank/{type}/{page}/{size}/{sort}/{ignore}/{beh}/{uid}")
    public R getPagePoolDataRank(@PathVariable("type") Integer type, @PathVariable("page") Integer page,
                                 @PathVariable("size") Integer size,
                                 @PathVariable("sort") Integer sort, @PathVariable("ignore") Integer ignore,
                                 @PathVariable("beh") String beh, @PathVariable("uid") String uid) {

        return poolDataService.getPagePoolDataRank(type, page, size, sort, ignore, beh, uid);
    }

    /**
     * 主列表页面
     *
     * @param uid
     * @param type
     * @return
     */
    @GetMapping("/pool/data/new/{uid}/{type}")
    public R getNewPoolData(@PathVariable("uid") String uid, @PathVariable("type") String type) {
        if ("undefined".equals(uid)) {
            return R.fail("当前账号异常，请重新登录~");
        }
        try {
            long l = Long.parseLong(uid);
        } catch (Exception e) {
            return R.fail("当前账号异常，请重新登录~");
        }
        return poolDataService.getNewAllPoolData(uid, type);
    }

    /**
     * 主列表页面
     *
     * @param uid
     * @param type
     * @return
     */
    @GetMapping("/pool/data/group/{uid}/{type}")
    public R getNewPoolGroupData(@PathVariable("uid") String uid, @PathVariable("type") String type) {
        if ("undefined".equals(uid)) {
            return R.fail("当前账号异常，请重新登录~");
        }
        try {
            long l = Long.parseLong(uid);
        } catch (Exception e) {
            return R.fail("当前账号异常，请重新登录~");
        }
        if ("0".equals(type) || "1".equals(type)) {
            return poolDataService.getPoolGroupData(uid, type);
        }
        return poolDataService.getNewAllPoolData(uid, type);
    }

    /**
     * 概览页面
     * 简单获取所有的角色
     *
     * @param uid
     * @return
     */
    @GetMapping("/pool/data/allRank/{uid}")
    public R getDetailPoolData(@PathVariable("uid") String uid) {
        return poolDataService.getAllRankData(uid);
    }

    /**
     * 同步卡池数据
     *
     * @param uid
     * @param data
     * @return
     */
    @PostMapping("/pool/post/{uid}")
    public R poolData(@PathVariable("uid") String uid, @RequestBody String data) {
        if ("undefined".equals(uid)) {
            return R.fail("当前账号异常，请重新登录~");
        }
        try {
            long l = Long.parseLong(uid);
        } catch (Exception e) {
            return R.fail("当前账号异常，请重新登录~");
        }
//        VThread.submit(() -> {
//            poolDataService.postData(uid, data);
//        });
        return R.fail("旧版本APP导入接口已关停，请升级新版本或使用网页端");
    }

    /**
     * 同步卡池
     *
     * @return
     */
    @GetMapping("/pool/token/{uid}")
    public R poolToken(@PathVariable("uid") String uid, String token) {
        if ("undefined".equals(uid)) {
            return R.fail("当前账号异常，请重新登录~");
        }
        try {
            long l = Long.parseLong(uid);
        } catch (Exception e) {
            return R.fail("当前账号异常，请重新登录~");
        }
        return poolDataService.postDataByToken(uid, token);
    }

    @GetMapping("/pool/data/delete/{uid}")
    public R deleteData(@PathVariable("uid") String uid) {
        if ("undefined".equals(uid)) {
            return R.fail("当前账号异常，请重新登录~");
        }
        try {
            long l = Long.parseLong(uid);
        } catch (Exception e) {
            return R.fail("当前账号异常，请重新登录~");
        }
        log.error("删除数据:{}提交申请", uid);
        VThread.submit(() -> {
            poolDataService.remove(new LambdaQueryWrapper<PoolData>().eq(PoolData::getUid, uid));
            poolDataRankService.remove(new LambdaQueryWrapper<PoolDataRank>().eq(PoolDataRank::getUid, uid));
        });
        return R.ok("成功");
    }

    /**
     * 获取当前用户最近更新时间的时间戳 + 1天
     *
     * @param uid
     * @return
     */
    @GetMapping("/pool/query/last/{uid}")
    public String queryLastTime(@PathVariable("uid") String uid) {

        List<PoolData> records = poolDataService.page(new Page<>(1, 1), new LambdaQueryWrapper<PoolData>()
                .select(PoolData::getTime).eq(PoolData::getUid, uid).orderByDesc(PoolData::getTime)).getRecords();
        if (records.isEmpty()) {
            return "0";
        }
        Date time = records.getFirst().getTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        TimeZone chinaTimeZone = TimeZone.getTimeZone("Asia/Shanghai");
        calendar.setTimeZone(chinaTimeZone);
        // 将小时、分钟、秒和毫秒设置为0
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 增加一天，得到明天0点的时间
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        // 获取从 1970 年 1 月 1 日 00:00:00 UTC 到现在的总毫秒数，然后转换为秒
        return calendar.getTimeInMillis() / 1000 + "";
    }

}
