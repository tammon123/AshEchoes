package info.qianqiu.ashechoes.utils.http;

import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * @author admin
 */
@Slf4j
public class ReqUtils {

    private static final RestTemplate restTemplate = SpringUtil.getBean("restTemplate");

    public static String get(String url) {
        return get(url, true);
    }

    public static String get(String url, boolean needLog) {
        // 请求头
        HttpHeaders headers = new HttpHeaders();
        return get(url, headers, needLog);
    }

    /**
     * 自定义超时时间
     *
     * @param url
     * @return
     */
    public static String get(String url, HttpHeaders headers, boolean needLog) {
        URI uri = URI.create(url);
        // 请求头
        if (headers.getContentType() == null) {
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        }
        HttpEntity<MultiValueMap<String, Object>> formEntity = new HttpEntity<MultiValueMap<String, Object>>(headers);
        ResponseEntity<String> exchange = restTemplate.exchange(uri, HttpMethod.GET, formEntity, String.class);
        return exchange.getBody();
    }

}
