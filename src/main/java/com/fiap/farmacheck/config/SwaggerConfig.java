package com.fiap.farmacheck.config;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springdoc.core.customizers.OpenApiCustomizer;
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
                        //.description("")
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
                        .description("Documentação Técnica Completa")
                        .url("https://github.com/offteuz/farma-check"))
                .addTagsItem(new Tag().name("Auth"));
    }

    /**
     * Este bean roda DEPOIS que o Springdoc já escaneou os DTOs
     */
    @Bean
    public OpenApiCustomizer customApiPaths() {
        return openApi -> {
            openApi
                    // Auth
                    .path("/api/auth/sign-up", new PathItem().post(new Operation()
                                    .summary("Registra um novo usuário")
                                    .addTagsItem("Auth")
                                    .requestBody(new RequestBody()
                                            .description("Dados do usuário para registro")
                                            .required(true)
                                            .content(new Content().addMediaType("application/json", new MediaType()
                                                    .schema(new Schema().$ref("#/components/schemas/UsuarioRequestDTO")))))
                                    .responses(new ApiResponses()
                                            .addApiResponse("201", new ApiResponse().description("Usuário registrado com sucesso"))
                                            .addApiResponse("400", new ApiResponse().description("Requisição inválida"))
                                            .addApiResponse("403", new ApiResponse().description("Acesso negado"))
                                            .addApiResponse("500", new ApiResponse().description("Erro interno do servidor"))
                                    )
                            )
                    )
                    .path("/api/auth/sign-in", new PathItem().post(new Operation()
                                    .summary("Realiza login")
                                    .addTagsItem("Auth")
                                    .requestBody(new RequestBody()
                                            .description("Dados do usuário para login")
                                            .required(true)
                                            .content(new Content().addMediaType("application/json", new MediaType()
                                                    .schema(new Schema().$ref("#/components/schemas/UsuarioLoginRequestDTO")))))
                                    .responses(new ApiResponses()
                                            .addApiResponse("200", new ApiResponse().description("Usuário logado com sucesso"))
                                            .addApiResponse("400", new ApiResponse().description("Requisição inválida"))
                                            .addApiResponse("403", new ApiResponse().description("Acesso negado"))
                                            .addApiResponse("500", new ApiResponse().description("Erro interno do servidor"))
                                    )
                            )
                    )
                    .path("/api/test", new PathItem().get(new Operation()
                                    .summary("Realiza login independente da 'role' (administrador ou paciente)")
                                    .addTagsItem("Testes")
                                    .responses(new ApiResponses()
                                            .addApiResponse("200", new ApiResponse().description("Teste realizado com sucesso"))
                                            .addApiResponse("400", new ApiResponse().description("Requisição inválida"))
                                            .addApiResponse("403", new ApiResponse().description("Acesso negado"))
                                            .addApiResponse("500", new ApiResponse().description("Erro interno do servidor"))
                                    )
                            )
                    )
                    .path("/api/test/administrador", new PathItem().get(new Operation()
                                    .summary("Realiza login como 'administrador'")
                                    .addTagsItem("Testes")
                                    .responses(new ApiResponses()
                                            .addApiResponse("200", new ApiResponse().description("Teste realizado como administrador com sucesso"))
                                            .addApiResponse("400", new ApiResponse().description("Requisição inválida"))
                                            .addApiResponse("403", new ApiResponse().description("Acesso negado"))
                                            .addApiResponse("500", new ApiResponse().description("Erro interno do servidor"))
                                    )
                            )
                    )
                    .path("/api/test/paciente", new PathItem().get(new Operation()
                                    .summary("Realiza login como 'paciente'")
                                    .addTagsItem("Testes")
                                    .responses(new ApiResponses()
                                            .addApiResponse("200", new ApiResponse().description("Teste realizado como paciente com sucesso"))
                                            .addApiResponse("400", new ApiResponse().description("Requisição inválida"))
                                            .addApiResponse("403", new ApiResponse().description("Acesso negado"))
                                            .addApiResponse("500", new ApiResponse().description("Erro interno do servidor"))
                                    )
                            )
                    )
            ;
        };
    }
}