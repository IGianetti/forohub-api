package com.forohub.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Table(name = "usuarios")
@Entity
@Data // Lombok: genera getters, setters, toString, equals y hashCode
@NoArgsConstructor // Lombok: genera constructor sin argumentos
@AllArgsConstructor // Lombok: genera constructor con todos los argumentos
public class Usuario implements UserDetails { // Implementa UserDetails para Spring Security
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String correoElectronico; // Corregido a camelCase para Java
    private String contrasena; // Corregido a camelCase para Java

    @ManyToMany(fetch = FetchType.EAGER) // Carga los perfiles inmediatamente
    @JoinTable(
            name = "usuario_perfiles", // Nombre de la tabla de unión
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "perfil_id")
    )
    private Set<Perfil> perfiles = new HashSet<>(); // Inicializa para evitar NullPointer

    // Métodos de UserDetails (implementación básica por ahora, se completará con seguridad)
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.perfiles; // Los perfiles actúan como GrantedAuthority
    }

    @Override
    public String getPassword() {
        return contrasena;
    }

    @Override
    public String getUsername() {
        return correoElectronico; // Usamos el correo electrónico como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Por ahora, las cuentas no expiran
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Por ahora, las cuentas no están bloqueadas
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Por ahora, las credenciales no expiran
    }

    @Override
    public boolean isEnabled() {
        return true; // Por ahora, las cuentas están habilitadas
    }
}
