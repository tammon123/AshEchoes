package info.qianqiu.ashechoes.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import info.qianqiu.ashechoes.dto.domain.User;
import info.qianqiu.ashechoes.dto.service.UserService;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 用户操作相关接口
 */
@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    private final UserService userService;

    @PostMapping("/user/post/{b}")
    public R userPost(@PathVariable("b") String b, @RequestBody User user) {

        if ("register".equals(b)) {
            return userService.register(user);
        } else if ("login".equals(b)) {
            return userService.login(user);
        }
        return R.fail("未知命令");
    }

    @PostMapping("/user/modify")
    public R userModify(@RequestBody User user) {
        return userService.modify(user);
    }

    @PostMapping("/user/psw/sendMail")
    public R sendPswEmail(@RequestBody User user) {
        return userService.sendPswEmail(user);
    }

    @PostMapping("/user/psw/find")
    public R sendPswFind(@RequestBody User user) {
        return userService.pswFind(user);
    }

    @PostMapping("/user/complele/info/{uid}")
    public R userCompleteInfo(@PathVariable("uid") String uid, @RequestBody String o) {
        if (StringUtils.isEmpty(uid)) {
            return R.ok();
        }
        if ("undefined".equals(uid)) {
            return R.fail("当前账号异常，请重新登录~");
        }
        try {
            long l = Long.parseLong(uid);
        } catch (Exception e) {
            return R.fail("当前账号异常，请重新登录~");
        }
        User user = JSONObject.parseObject(o, User.class);
        if (StringUtils.isEmpty(user.getAvatar()) && StringUtils.isEmpty(user.getOpenid()) &&
                StringUtils.isEmpty(user.getNickname())) {
            return R.ok();
        }
        userService.update(new LambdaUpdateWrapper<User>()
                .set(StringUtils.isNotEmpty(user.getAvatar()), User::getAvatar, user.getAvatar())
                .set(StringUtils.isNotEmpty(user.getNickname()), User::getNickname, user.getNickname())
                .set(StringUtils.isNotEmpty(user.getOpenid()), User::getOpenid, user.getOpenid())
                .eq(User::getUid, uid));
        return R.ok();
    }

}
