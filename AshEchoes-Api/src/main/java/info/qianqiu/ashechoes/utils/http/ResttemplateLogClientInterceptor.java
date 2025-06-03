package info.qianqiu.ashechoes.utils.http;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@Slf4j
@NoArgsConstructor
public class ResttemplateLogClientInterceptor implements ClientHttpRequestInterceptor {

    private boolean needReqLog = true;
    private boolean needResLog = true;

    ResttemplateLogClientInterceptor(boolean needReqLog, boolean needResLog) {
        this.needReqLog = needReqLog;
        this.needResLog = needResLog;
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        tranceRequest(request, body);
        ClientHttpResponse response = execution.execute(request, body);
        traceResponse(response);
        return response;
    }

    private void tranceRequest(HttpRequest request, byte[] body) throws UnsupportedEncodingException {
        log.debug("=========================== request begin ===========================");
        log.debug("uri : {}", request.getURI());
        log.debug("headers : {}", request.getHeaders());
        if (needReqLog) {
            log.debug("request body : {}", new String(body, StandardCharsets.UTF_8));
        } else {
            log.debug("request body : 当前配置已省略日志");
        }
        log.debug("============================ request end ============================");
    }

    private void traceResponse(ClientHttpResponse httpResponse) throws IOException {
        StringBuilder inputStringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getBody(), StandardCharsets.UTF_8));
        String line = bufferedReader.readLine();
        while (line != null) {
            inputStringBuilder.append(line);
            line = bufferedReader.readLine();
        }
        log.debug("============================ response begin ============================");
        log.debug("Status code  : {}", httpResponse.getStatusCode());
        log.debug("Headers      : {}", httpResponse.getHeaders());
        if (needResLog) {
            log.debug("Response body: {}", inputStringBuilder);
        } else {
            log.debug("Response body: 当前配置已省略");
        }
        log.debug("============================= response end =============================");
    }
}
