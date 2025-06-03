package info.qianqiu.ashechoes.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import info.qianqiu.ashechoes.dto.domain.Suggest;
import info.qianqiu.ashechoes.dto.service.SuggestService;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.http.ReqUtils;
import info.qianqiu.ashechoes.utils.http.ServletUtils;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 意见反馈
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class SuggestController {

    private final SuggestService suggestService;

    @PostMapping("/suggest/post")
    public R userPost(@RequestBody Suggest suggest) {

        if (StringUtils.isNotEmpty(suggest.getDesc()) && suggest.getDesc().length() <= 250) {
            suggest.setSuggestId(0L);
            suggest.setIp(ServletUtils.getIpAddr());
            suggest.setInitTime(new Date());
            suggestService.save(suggest);
            return R.ok();
        }

        return R.fail("未知命令");
    }

    @PostMapping("/suggest/page")
    public R suggestPage(@RequestBody Suggest suggest) {
        if ("mini".equals(suggest.getDesc())) {
            String url = "https://bjhl.qianqiu.info/v.txt";
            JSONObject jo = JSONObject.parseObject(ReqUtils.get(url));
            if ("暂无更新日志".equals(jo.getString("data"))) {
                return R.ok();
            }
        }
        List<Suggest> records = suggestService.page(new Page<Suggest>(suggest.getPage(), suggest.getSize()),
                new LambdaQueryWrapper<Suggest>()
                        .select(Suggest::getUid, Suggest::getTime, Suggest::getAns, Suggest::getDesc)
                        .orderByDesc(Suggest::getTime)).getRecords();
        for (Suggest s : records) {
            String uid = s.getUid();
            s.setPage(null);
            s.setSize(null);
            if (StringUtils.isNotEmpty(uid)) {
                s.setUid("******" + uid.substring(uid.length() - 6));
            }
        }
        return R.ok(records);
    }

}
