package me.hoon.restapipractice.common;

import org.springframework.boot.test.autoconfigure.restdocs.RestDocsMockMvcConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentationConfigurer;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

//RestDocMockMvc 커스터마이징
@TestConfiguration
public class RestDocsConfiguration {

    @Bean
    public RestDocsMockMvcConfigurationCustomizer restDocsMockMvcConfigurationCustomizer() {
        return new RestDocsMockMvcConfigurationCustomizer() {
            @Override
            public void customize(MockMvcRestDocumentationConfigurer configurer) {
                //요청 응답 포맷팅하기
                configurer.operationPreprocessors()
                        .withRequestDefaults(prettyPrint())
                        .withResponseDefaults(prettyPrint())
                ;
            }
        };
    }
}
