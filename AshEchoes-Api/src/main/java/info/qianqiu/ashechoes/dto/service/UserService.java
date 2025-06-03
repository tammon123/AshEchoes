package info.qianqiu.ashechoes.dto.service;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import info.qianqiu.ashechoes.dto.domain.User;
import info.qianqiu.ashechoes.dto.mapper.UserMapper;
import info.qianqiu.ashechoes.utils.email.EmailUtils;
import info.qianqiu.ashechoes.utils.http.R;
import info.qianqiu.ashechoes.utils.id.Id;
import info.qianqiu.ashechoes.utils.id.MD5;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认名称Service业务层处理
 *
 * @author TianYiLuo
 * @date 2024-08-30
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    public R login(User user) {
        User one = getOne(new LambdaQueryWrapper<User>()
                .eq(User::getAccount, user.getAccount()));
        if (one == null) {
            return R.fail("账号不存在");
        }
        if (one.getPassword().equals(MD5.ToMD5(user.getPassword()))) {
            User user1 = new User();
            user1.setNickname(one.getNickname());
            user1.setUuid("" + one.getUid());
            if (StringUtils.isEmpty(user1.getNickname())) {
                user1.setNickname("匿名小监督");
            }
            return R.ok(user1, "成功");
        }
        return R.fail("账号或密码错误");
    }

    public R register(User user) {
        synchronized (UserService.class) {
            if (StringUtils.isEmpty(user.getAccount()) ||
                    StringUtils.isEmpty(user.getEmail()) ||
                    StringUtils.isEmpty(user.getPassword())
            ) {
                return R.fail("参数缺失");
            }
            if (!EmailValidator.isValidEmail(user.getEmail())) {
                return R.fail("邮箱格式错误");
            }
            long c = count(new LambdaQueryWrapper<User>()
                    .eq(User::getAccount, user.getAccount()));
            if (c != 0) {
                return R.fail("账号已存在");
            }
            user.setUid(Id.id());
            user.setPassword(MD5.ToMD5(user.getPassword()));
            user.setTime(new Date());
            save(user);
        }
        User one = getOne(new LambdaQueryWrapper<User>().select(User::getNickname).eq(User::getUid, user.getUid()));
        if (one == null || StringUtils.isEmpty(one.getNickname())) {
            one = new User();
            one.setNickname("匿名小监督");
        }
        one.setUuid("" + user.getUid());
        return R.ok(one, "成功");
    }

    public R modify(User user) {
        LambdaUpdateWrapper<User> uw = new LambdaUpdateWrapper<>();
        if (user.getUid() == null) {
            return R.ok("用户ID禁止为空");
        }
        if (StringUtils.isNotEmpty(user.getNickname())) {
            uw.set(User::getNickname, user.getNickname());
        }
        LambdaUpdateWrapper<User> eq = uw.eq(User::getUid, user.getUid());
        update(eq);
        return R.ok(user);
    }

    public R sendPswEmail(User user) {

        if (!EmailValidator.isValidEmail(user.getEmail())) {
            return R.fail("邮箱格式错误");
        }
        List<User> list =
                list(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail()).orderByDesc(User::getTime));
        if (list.isEmpty()) {
            return R.fail("该邮箱下没有找到小助手账号");
        }
        List<String> list1 = list.stream().map(User::getAccount).toList();
        JSONObject jsonObject = new JSONObject();
        int count = 0;
        for (String s : list1) {
            jsonObject.put((count++) + "c", s);
        }
        jsonObject.put("final", "修改密码成功后，以上所有账号密码将全部同步为修改后的密码。");

        EmailUtils.sendTemplate(user.getEmail(), "白荆小助手", list.getFirst().getPassword().substring(0, 6),
                jsonObject);

        return R.ok("发送成功~");
    }

    public R pswFind(User user) {
        List<User> list = list(new LambdaQueryWrapper<User>().eq(User::getEmail, user.getEmail())
                .orderByDesc(User::getTime));
        if (list.isEmpty()) {
            return R.fail("该邮箱下没有找到小助手账号");
        }
        User u = list.getFirst();

        String substring = u.getPassword().substring(0, 6);

        if (!user.getCode().equals(substring)) {
            return R.fail("验证码错误~");
        }

        update(new LambdaUpdateWrapper<User>().eq(User::getEmail, user.getEmail())
                .set(User::getPassword, MD5.ToMD5(user.getPassword())));

        return R.ok("该邮箱下所有账号密码已更新，请使用最新的密码登录");
    }

    static class EmailValidator {

        // 用于匹配电子邮件地址的正则表达式
        private static final String EMAIL_PATTERN =
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        // 编译正则表达式
        private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

        // 校验方法
        public static boolean isValidEmail(String email) {
            if (email == null) {
                return false;
            }
            Matcher matcher = pattern.matcher(email);
            return matcher.matches();
        }
    }
}
