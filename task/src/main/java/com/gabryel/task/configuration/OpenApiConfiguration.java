package com.gabryel.task.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI myOpenAPI() {
        License license = new License();
        license.setName("MIT License");
        license.setUrl("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Task Manager")
                .description("Gerenciador de tarefas")
                .termsOfService("http://springfox.io")
                .version("1.0.0")
                .license(license);

        return new OpenAPI().info(info);
    }

}
