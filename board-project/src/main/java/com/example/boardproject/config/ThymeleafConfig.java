package com.example.boardproject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {

    /**
     * thymeleafTemplateResolver bean을 등록하는데
     * 이게 Thymeleaf Autoconfiguration을 불렀을 때 즉 타임리프를 스프링 프로젝트에 넣었을때 Auto config으로 잡힐텐데
     * 그 때 들어가는 기본 thymeleafTemplateResolver 이다. 즉, thymeleaf tempalte을 어떻게 resolving 할건지 구현하는 부분
     * 원래 있던 defaultTemplateResolver 에다가 decoupledLogic(이미 기능이 있음) 을 셋팅 해주고 리턴해주는 간단한 로직
     *
     */
    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(
            SpringResourceTemplateResolver defaultTemplateResolver,
            Thymeleaf3Properties thymeleaf3Properties
    ) {
        defaultTemplateResolver.setUseDecoupledLogic(thymeleaf3Properties.decoupledLogic());

        return defaultTemplateResolver;
    }


    /**
     * Use Thymeleaf3 Decoupled Logic
     * 어떻게 이용하냐면 application.yml 에서
     * spring.thymeleaf3.decoupled-logic=true 처럼 설정 해주면 활성화 가능
     * 프로퍼티로 노출시켜주는 방식
     *
     */
    @ConfigurationProperties("spring.thymeleaf3")
    public record Thymeleaf3Properties(boolean decoupledLogic) {}
}