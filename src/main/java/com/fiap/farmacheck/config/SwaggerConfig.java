package com.fiap.farmacheck.config;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Hidden
@RestController
@Configuration
public class SwaggerConfig {
    @GetMapping("/")
    public void redirectSwagger(HttpServletResponse response) {
        String url = "/swagger-ui/index.html";
        response.setHeader("Location", url);
        response.setStatus(302);
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FIAP - Tech Challenge - Fase 5 de Arquitetura e Desenvolvimento Java")
                        .version("v1")
                        .contact(new Contact()
                                .name("Farma Check")
                                .email("contato@farmacheck.com")
                                .url("https://github.com/offteuz/farma-check"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html"))
                )
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-key"))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentacao Tecnica Completa")
                        .url("https://github.com/offteuz/farma-check"))
                .addTagsItem(new Tag().name("Auth"))
                .addTagsItem(new Tag().name("Medicamentos"))
                .addTagsItem(new Tag().name("Disponibilidade"))
                .addTagsItem(new Tag().name("Estoques"))
                .addTagsItem(new Tag().name("Unidades"))
                .addTagsItem(new Tag().name("Movimentacoes"))
                .addTagsItem(new Tag().name("Testes"));
    }
}
