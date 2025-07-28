package com.forohub.infra.security;

import com.forohub.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta clase como un componente de Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService; // Inyecta tu TokenService

    @Autowired
    private UsuarioRepository usuarioRepository; // Inyecta tu UsuarioRepository

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Obtener el token del header de la solicitud
        var authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.replace("Bearer ", "");
            var subject = tokenService.getSubject(token); // Extrae el sujeto (correo electrónico) del token

            if (subject != null) {
                // Si el sujeto es válido, buscamos el usuario en la BD
                var usuario = usuarioRepository.findByCorreoElectronico(subject);
                if (usuario.isPresent()) {
                    // Forzamos un inicio de sesión para este usuario
                    var authentication = new UsernamePasswordAuthenticationToken(
                            usuario.get(), null, usuario.get().getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}