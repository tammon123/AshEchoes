package info.qianqiu.ashechoes.controller;

import com.alibaba.fastjson2.JSONObject;
import info.qianqiu.ashechoes.init.InitComputeData;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.http.ReqUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 强制更新内存缓存中的数据
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class SystemInitController {

    private final InitComputeData init;

    /**
     * 刷新缓存
     *
     * @param passwd
     * @return
     */
    @GetMapping("/reload/init/20241131/{passwd}")
    public R initData(@PathVariable("passwd") String passwd) {
        if ("ne".equals(passwd)) {
            init.destroy();
            init.init();
        }
        return R.ok("成功");
    }

    /**
     * 获取H5版本号
     *
     * @return
     */
    @GetMapping("/h5/v")
    public R version() {
        try {
            String get = ReqUtils.get("https://bjhl.qianqiu.info/version.json");
            String app = JSONObject.parseObject(get).getString("app");
            return R.ok(app);
        } catch (Exception e) {
        }
        return R.ok("20241119");
    }

    /**
     * 获取APP版本号
     *
     * @return
     */
    @GetMapping("/app/v")
    public JSONObject appVersion() {
        JSONObject r = new JSONObject();
        r.put("code", 0);
        try {
            String get = ReqUtils.get("https://bjhl.qianqiu.info/version.json");
            String app = JSONObject.parseObject(get).getString("c");
            r.put("data", app);
            r.put("app", JSONObject.parseObject(get).getString("app"));
            r.put("c", app);
            r.put("a", JSONObject.parseObject(get).getString("a"));
            return r;
        } catch (Exception e) {
        }
        r.put("data", "20241211");
        return r;
    }

    /**
     * 获取H5的宣传信息
     */
    @GetMapping("/h5/info")
    public R h5Info() {
        try {
            String get = ReqUtils.get("https://bjhl.qianqiu.info/h5.txt");
            String app = JSONObject.parseObject(get).getString("data");
            return R.ok(app);
        } catch (Exception e) {
        }
        return R.ok("20241119");
    }

    /**
     * 获取友链
     */
    @GetMapping("/friend/list")
    public R friendList() {
        try {
            String get = ReqUtils.get("https://bjhl.qianqiu.info/friendUrl.json");
            return R.ok(JSONObject.parseObject(get).getJSONArray("data"));
        } catch (Exception e) {
        }
        return R.ok("20241119");
    }

    /**
     * 更新日志
     */
    @GetMapping("/update/log")
    public R updateLog() {
        String get = ReqUtils.get("https://bjhl.qianqiu.info/update.json");
        return R.ok(get);
    }

}
