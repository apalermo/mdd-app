package com.openclassroom.mddapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global OpenAPI configuration for MDD platform.
 * Defines API metadata, security schemes, and strictly orders UI sections.
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MDD API")
                        .description("Technical sharing platform for developers.")
                        .version("1.0.0"))
                .addTagsItem(new Tag().name("Authentication").description("Registration and login operations"))
                .addTagsItem(new Tag().name("Articles").description("Technical content and community collaboration"))
                .addTagsItem(new Tag().name("Themes").description("Subject-based subscriptions"))
                .addTagsItem(new Tag().name("Users").description("Developer profile and settings management"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}