package info.qianqiu.ashechoes.utils.email;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;

/**
 * @author TianYiLuo
 * @create 2023/4/12 12:53
 */
@Slf4j
public class EmailTemplate {

    public static String buildRequestErrorContent(String title, String errMsg, JSONObject content) {
        //加载邮件html模板
        InputStreamReader inputStream = null;
        BufferedReader fileReader = null;
        StringBuilder buffer = new StringBuilder();
        String line = "";
        try {
            inputStream = new InputStreamReader(new ClassPathResource("mail.ftl").getInputStream(),
                StandardCharsets.UTF_8);
            fileReader = new BufferedReader(inputStream);
            while ((line = fileReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            log.info("发送邮件读取模板失败 {}", e);
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    log.info("{}", e.getMessage());
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.info("{}", e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        String template = "<p>{value}</p>";
        StringBuilder contentTemplate = new StringBuilder();
        for (String key : content.keySet()) {
            if (content.getString(key) != null) {
                String c = template.replaceAll("\\{key}", key).replaceAll("\\{value}", content.getString(key));
                contentTemplate.append(c);
            }
        }

        //替换html模板中的参数
        return MessageFormat.format(buffer.toString(), title, errMsg, contentTemplate.toString(), "四川中屹互联信息技术有限公司");
    }

}
