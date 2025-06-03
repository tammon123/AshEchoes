package info.qianqiu.ashechoes.config.env;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class EnvConfig {
    @Value("${spring.application.name}")
    private String appName;
    @Value("${env.spanId}")
    private String spanId;
    @Value("${env.assetsUrl}")
    private String assetsUrl;
    @Value("${env.assetsVersion}")
    private String assetsVersion;
}
