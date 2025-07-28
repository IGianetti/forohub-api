package com.forohub.controller;

import com.forohub.dto.DatosAutenticacionUsuario;
import com.forohub.dto.DatosJwtToken;
import com.forohub.infra.security.TokenService;
import com.forohub.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired // Inyecta tu TokenService
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity autenticarUsuario(@RequestBody @Valid DatosAutenticacionUsuario datosAutenticacionUsuario) {
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                datosAutenticacionUsuario.correoElectronico(),
                datosAutenticacionUsuario.contrasena()
        );
        Authentication usuarioAutenticado = authenticationManager.authenticate(authToken);

        // Generar el token JWT y devolverlo en la respuesta
        String jwtToken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal());
        return ResponseEntity.ok(new DatosJwtToken(jwtToken)); // Devolvemos el token en un DTO
    }

}