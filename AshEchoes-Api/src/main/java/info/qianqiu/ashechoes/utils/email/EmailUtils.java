package info.qianqiu.ashechoes.utils.email;

import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSONObject;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * @author TianYiLuo
 * @create 2023/4/12 11:28
 */
public class EmailUtils {

    private static final JavaMailSender MAIL_SENDER = SpringUtil.getBean(JavaMailSender.class);
    private static final MailProperties MAIL_PROPERTIES = SpringUtil.getBean(MailProperties.class);

    /**
     * @param email   发送目标
     * @param topic   主题
     * @param code  错误提示
     * @param content 发生错误的参数
     */
    public static void sendTemplate(String email, String topic, String code, JSONObject content) {
        MimeMessage message = MAIL_SENDER.createMimeMessage();
        try {
            //邮箱发送内容组成
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject(topic);
            helper.setText(EmailTemplate.buildRequestErrorContent(topic, code, content), true);
            if (StringUtils.isNotEmpty(email)) {
                String[] array = {email};
                helper.setTo(array);
            }
            helper.setFrom(MAIL_PROPERTIES.getUsername());
            MAIL_SENDER.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
