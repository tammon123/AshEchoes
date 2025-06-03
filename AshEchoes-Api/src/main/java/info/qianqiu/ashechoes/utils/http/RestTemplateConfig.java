package info.qianqiu.ashechoes.utils.http;


import cn.hutool.core.util.NumberUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import info.qianqiu.ashechoes.utils.string.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpRequest;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.apache.hc.core5.util.TimeValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class RestTemplateConfig {

    private static final int HTTP_MAX_RETRY = 2;

    private static final int CONNECTION_VALIDATE_AFTER_INACTIVITY_MS = 2000;

    private static final int MAXIMUM_TOTAL_CONNECTION = 30;

    private static final int MAXIMUM_CONNECTION_PER_ROUTE = 4;
    private static final long KEEP_ALIVE_SECONDS = 60;

    /**
     * @param connectionTimeoutMs milliseconds/毫秒
     * @param readTimeoutMs       milliseconds/毫秒
     * @return
     */
    public static RestTemplate createRestTemplate(int connectionTimeoutMs, int readTimeoutMs,
                                                  boolean needReqLogs, boolean needResLogs, ObjectMapper objectMapper) {

        HttpClientBuilder clientBuilder = HttpClients.custom();

        PoolingHttpClientConnectionManager connectionManager =
                PoolingHttpClientConnectionManagerBuilder.create()
                        .setSSLSocketFactory(getSslConnectionSocketFactory())
                        .build();
        // 整个连接池最大连接数
        connectionManager.setMaxTotal(MAXIMUM_TOTAL_CONNECTION);
        // 设置每个路由的最大并发连接数,默认为2.
        connectionManager.setDefaultMaxPerRoute(MAXIMUM_CONNECTION_PER_ROUTE);
        clientBuilder.setConnectionManager(connectionManager);
        // 长连接
        clientBuilder.setKeepAliveStrategy((response, context) -> {
            long timeout = Arrays.stream(response.getHeaders("Keep-Alive"))
                    .filter(h -> StringUtils.equalsIgnoreCase(h.getName(), "timeout")
                            && StringUtils.isNumeric(h.getValue()))
                    .findFirst()
                    .map(h -> NumberUtil.parseLong(h.getValue(), KEEP_ALIVE_SECONDS))
                    .orElse(KEEP_ALIVE_SECONDS);
            return TimeValue.of(timeout, TimeUnit.SECONDS);
        });
        //设置重连操作次数，这里设置了2次
        clientBuilder.setRetryStrategy(new HttpRequestRetryStrategy() {
            @Override
            public boolean retryRequest(HttpRequest request, IOException exception, int execCount,
                                        HttpContext context) {
                return execCount < HTTP_MAX_RETRY;
            }

            @Override
            public boolean retryRequest(HttpResponse response, int execCount, HttpContext context) {
                if (response.getCode() == 200) {
                    return false;
                }
                return execCount < HTTP_MAX_RETRY;
            }

            @Override
            public TimeValue getRetryInterval(HttpResponse response, int execCount, HttpContext context) {
                return TimeValue.of((long) CONNECTION_VALIDATE_AFTER_INACTIVITY_MS * execCount, TimeUnit.MILLISECONDS);
            }
        });

        //使用httpClient创建一个ClientHttpRequestFactory的实现
        HttpComponentsClientHttpRequestFactory httpRequestFactory =
                new HttpComponentsClientHttpRequestFactory(clientBuilder.build());
        httpRequestFactory.setConnectTimeout(connectionTimeoutMs);
        httpRequestFactory.setConnectionRequestTimeout(readTimeoutMs);

        RestTemplate restTemplate = new RestTemplate(httpRequestFactory);
        // 添加自定义拦截器
        List<ClientHttpRequestInterceptor> interceptors =
                new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new ResttemplateLogClientInterceptor(needReqLogs, needResLogs));
        restTemplate.setInterceptors(interceptors);
        //提供对传出/传入流的缓冲,可以让响应body多次读取(如果不配置,拦截器读取了Response流,再响应数据时会返回body=null)
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpRequestFactory));

        MappingJackson2HttpMessageConverter messageConverter =
                restTemplate.getMessageConverters().stream()
                        .filter(MappingJackson2HttpMessageConverter.class::isInstance)
                        .map(MappingJackson2HttpMessageConverter.class::cast).findFirst()
                        .orElseThrow(() -> new RuntimeException("MappingJackson2HttpMessageConverter not found"));
        messageConverter.setObjectMapper(objectMapper);

        //防止响应中文乱码
        restTemplate.getMessageConverters().stream().filter(StringHttpMessageConverter.class::isInstance)
                .map(StringHttpMessageConverter.class::cast).forEach(a -> {
                    a.setWriteAcceptCharset(false);
                    a.setDefaultCharset(StandardCharsets.UTF_8);
                });

        return restTemplate;
    }

    /**
     * 支持SSL
     *
     * @return SSLConnectionSocketFactory
     */
    private static SSLConnectionSocketFactory getSslConnectionSocketFactory() {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
        SSLContext build = null;
        try {
            build = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (KeyManagementException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        }
        return new SSLConnectionSocketFactory(build, new NoopHostnameVerifier());
    }

    @Bean("restTemplate")
    public RestTemplate commonReq() {
        return restTemplate(30000, 30000, true, true);
    }

    @Bean("restNoReceive")
    public RestTemplate restTemplate2() {
        return restTemplate(2000, 2000, false, false);
    }

    @Bean("restNoLog")
    public RestTemplate restTemplate3() {
        return restTemplate(30000, 30000, false, false);
    }

    private RestTemplate restTemplate(int conTimeout, int readTimeout, boolean reqLog, boolean resLog) {
        RestTemplate restTemplate = RestTemplateConfig.createRestTemplate(conTimeout, readTimeout, reqLog, resLog,
                new ObjectMapper());
        //配置自定义的interceptor拦截器
        //使用restTemplate远程调用防止400和401导致报错而获取不到正确反馈信息
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getStatusCode().value() != 400 && response.getStatusCode().value() != 401) {
                }                    super.handleError(response);

            }
        });
        return restTemplate;
    }

}
