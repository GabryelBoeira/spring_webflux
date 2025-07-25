package com.gabryel.task.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {


    @Autowired
    private MessageConfiguration messages;

    @Bean
    public OpenAPI myOpenAPI() {
        License license = new License();
        license.setName(messages.getMessage("swagger.license.description"));
        license.setUrl(messages.getMessage("swagger.license.url"));

        Info info = new Info()
                .title(messages.getMessage("swagger.title"))
                .description(messages.getMessage("swagger.description"))
                .termsOfService(messages.getMessage("swagger.terms.service"))
                .version(messages.getMessage("swagger.version"))
                .license(license);

        return new OpenAPI().info(info);
    }

}
