package com.edu.silva.users.controllers;

import com.edu.silva.common.DefaultResponse;
import com.edu.silva.users.domain.dtos.requests.AuthRequestDTO;
import com.edu.silva.users.domain.dtos.requests.RegisterRequestDTO;
import com.edu.silva.users.domain.dtos.responses.LoginResponseDTO;
import com.edu.silva.users.domain.dtos.responses.UserResponseDTO;
import com.edu.silva.users.domain.entities.User;
import com.edu.silva.users.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.extensions.Extension;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autentificação")
@RestController
@RequestMapping("auth")
public class AuthController {

    private final UserService service;

    public AuthController(UserService service) {
        this.service = service;
    }

    @Operation(
            summary = "Verificar validade de token",
            description = "Esta rota é utilizada para verificar a validade de um token de autenticação. É ideal para ser usada em middlewares de proteção de rotas, garantindo que o usuário esteja autenticado antes de acessar recursos restritos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Token valido"),
            @ApiResponse(responseCode = "401", description = "Token invalido")
    })
    @GetMapping("me")
    public ResponseEntity<?> me(@AuthenticationPrincipal User user) {
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO(user.getId(), user.getUsername(), user.getEmail(), null);
        return ResponseEntity.ok(new DefaultResponse("Auth successfully verified", loginResponseDTO));
    }

    @Operation(
            summary = "Registrar usuário",
            description = "Rota destinada ao registro de um novo usuário.",
            extensions = {
                    @Extension(
                            name = "x-business-rules",
                            properties = {
                                    @ExtensionProperty(
                                            name = "RN1",
                                            value = "Esta rota **não permite** a criação de usuários com o papel (role) de `ADMIN`. Para mais detalhes, consulte a [regra de negócio](#rn1-criação-de-administradores)."
                                    ),
                                    @ExtensionProperty(
                                            name = "RN2",
                                            value = "Todo usuário é criado com o status `WAITING_CONFIRMATION` e precisa confirmar seu e-mail para ativar a conta. Para mais detalhes, consulte a [regra de negócio](#rn2-confirmação-de-usuário)."
                                    ),
                                    @ExtensionProperty(
                                            name = "RN3",
                                            value = "Caso a empresa não seja informada, é criada a partir do nome do usuário."
                                    )
                            }
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Registrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados informados inválidos")
    })
    @PostMapping("register")
    ResponseEntity<@NonNull DefaultResponse> save(@RequestBody @Valid RegisterRequestDTO dto) {
        UserResponseDTO u = service.save(dto);
        String BASE_URL_USERS = "/users";
        return ResponseEntity.status(HttpStatus.CREATED).body(
                new DefaultResponse("User successfully saved", u, BASE_URL_USERS, u.getId())
        );
    }

    @Operation(
            summary = "Login",
            description = "Rota destinada à autenticação do usuário no sistema. O usuário fornece suas credenciais e, se válidas, recebe um token de acesso.",
            extensions = {
                    @Extension(
                            name = "x-business-rules",
                            properties = {
                                    @ExtensionProperty(
                                            name = "RN4",
                                            value = "O usuário só poderá realizar o login no sistema após ter confirmado seu e-mail com sucesso."
                                    )
                            }
                    )
            }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados informados inválidos")
    })
    @PostMapping("login")
    public ResponseEntity<DefaultResponse> login(@RequestBody @Valid AuthRequestDTO dto) {
        return ResponseEntity.ok(new DefaultResponse("Auth successfully verified", service.login(dto)));
    }
}
