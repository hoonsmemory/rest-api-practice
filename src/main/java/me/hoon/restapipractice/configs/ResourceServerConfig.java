package me.hoon.restapipractice.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

/**
 * OAuth Server 랑 연동되어 사용한다.
 * 외부 요청이 Resource 에 접근할 때 토근 정보가 있는지 확인 및 접근 제어한다.
 */
@EnableResourceServer
@Configuration
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("event");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                    .and()
                .authorizeRequests()
                    .mvcMatchers(HttpMethod.GET, "/api/**")
                        .permitAll()
                    .anyRequest()
                        .authenticated()
                    .and()
                .exceptionHandling() //인증이 안되거나, 권한이 없는 경우
                    .accessDeniedHandler(new OAuth2AccessDeniedHandler()); // 접근 권한이 없는 경우 에러 발생

    }
}
