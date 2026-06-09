package com.edu.silva.users.infra.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Usuários")
                        .version("1.0.0")
                        .description("Documentação detalhada sobre o módulo de usuários, incluindo autenticação, gerenciamento de perfis e rotas da API."));
    }

    @Bean
    OpenApiCustomizer docsCustomizer() {
        return openApi -> {
            openApi.getInfo().addExtension("x-docs-objective", "O módulo de **usuários** tem como principal objetivo gerenciar o ciclo de vida e a autenticação dos usuários na plataforma. Ele fornece uma base segura e robusta para controle de acesso, garantindo que apenas usuários autorizados possam interagir com os recursos do sistema.");
            openApi.getInfo().addExtension("x-docs-responsibilities", List.of(
                    "Este módulo é responsável por um conjunto de funcionalidades críticas relacionadas à identidade e ao acesso dos usuários. As principais responsabilidades incluem:",
                    "**Autenticação de Usuários:** Validar credenciais (e-mail e senha) para permitir o acesso ao sistema.",
                    "**Emissão e Validação de JWT:** Gerar e verificar JSON Web Tokens (JWT) para autenticação stateless em requisições subsequentes.",
                    "**Gerenciamento de Papéis e Status:** Atribuir e controlar os papéis (roles) e o status (ativo, pendente, etc.) de cada usuário.",
                    "**Integração com Módulo de E-mail:** Coordenar com o serviço de e-mails para enviar notificações, como a confirmação de cadastro."
            ));
            openApi.getInfo().addExtension("x-docs-notes", List.of(
                    "Os papéis (roles) disponíveis são definidos no `enum UserRole` e devem ser atualizados manualmente no código-fonte sempre que um novo módulo for adicionado ao sistema.",
                    "Se o módulo de e-mail não estiver configurado ou em uso, a confirmação de novos usuários deverá ser realizada manualmente através do endpoint `confirm` ou por uma intervenção direta no banco de dados."
            ));
        };
    }
}