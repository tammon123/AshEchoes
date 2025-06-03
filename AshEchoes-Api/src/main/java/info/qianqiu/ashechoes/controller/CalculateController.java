package info.qianqiu.ashechoes.controller;

import info.qianqiu.ashechoes.compute.ComputeDataHandle;
import info.qianqiu.ashechoes.controller.vo.CardVo;
import info.qianqiu.ashechoes.utils.http.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class CalculateController {

    private final ComputeDataHandle computeDataInit;

    /**
     * 队伍伤害计算入口 calculate
     * 生效技能以前端返回的数据为准
     *
     * @param cids
     * @param mids
     * @param vo
     * @return
     */
    @GetMapping("/card/{cids}/{mids}")
    public R cardCompute(@PathVariable("cids") String cids, @PathVariable("mids") String mids, CardVo vo) {
        // 将角色、烙痕ID添加到全流程常量Vo中
        vo.setCharacters(cids);
        vo.setMemorys(mids);
        try {
            return computeDataInit.init(vo);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("{}", e.getMessage());
            return R.fail("计算失败，可反馈开发人员~");
        }
    }

}
