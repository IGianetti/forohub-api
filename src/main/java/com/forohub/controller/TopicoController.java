package com.forohub.controller;

import com.forohub.dto.DatosActualizacionTopico;
import com.forohub.dto.DatosListadoTopico;
import com.forohub.dto.DatosRegistroTopico;
import com.forohub.model.Curso;
import com.forohub.model.Topico;
import com.forohub.model.Usuario;
import com.forohub.repository.CursoRepository;
import com.forohub.repository.TopicoRepository;
import com.forohub.repository.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @PostMapping
    public ResponseEntity<String> registrarTopico(@RequestBody @Valid DatosRegistroTopico datos,
                                                  UriComponentsBuilder uriComponentsBuilder) {

        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            return ResponseEntity.badRequest().body("Error: Ya existe un tópico con el mismo título y mensaje.");
        }

        Optional<Usuario> autorOptional = usuarioRepository.findById(datos.idAutor());
        if (!autorOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Error: El autor especificado no existe.");
        }
        Usuario autor = autorOptional.get();

        Optional<Curso> cursoOptional = cursoRepository.findById(datos.idCurso());
        if (!cursoOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Error: El curso especificado no existe.");
        }
        Curso curso = cursoOptional.get();

        Topico topico = new Topico(datos.titulo(), datos.mensaje(), autor, curso);
        if (topico.getFechaCreacion() == null) {
            topico.setFechaCreacion(LocalDateTime.now());
        }
        if (topico.getStatus() == null || topico.getStatus().isEmpty()) {
            topico.setStatus("ACTIVO");
        }

        topicoRepository.save(topico);

        URI url = uriComponentsBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(url).body("Tópico registrado con éxito. ID: " + topico.getId());
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.ASC) Pageable paginacion,
            @RequestParam(required = false) String nombreCurso,
            @RequestParam(required = false) Integer anio
    ) {
        Page<Topico> paginaDeTopicos;

        if (nombreCurso != null && anio != null) {
            paginaDeTopicos = topicoRepository.findByCursoNombreContainingIgnoreCaseAndFechaCreacionYear(nombreCurso, anio, paginacion);
        } else if (nombreCurso != null) {
            paginaDeTopicos = topicoRepository.findByCursoNombreContainingIgnoreCase(nombreCurso, paginacion);
        } else if (anio != null) {
            paginaDeTopicos = topicoRepository.findByFechaCreacionYear(anio, paginacion);
        } else {
            paginaDeTopicos = topicoRepository.findAll(paginacion);
        }

        Page<DatosListadoTopico> topicosDTO = paginaDeTopicos.map(DatosListadoTopico::new);
        return ResponseEntity.ok(topicosDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosListadoTopico> retornarDatosTopico(@PathVariable Long id) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);

        if (topicoOptional.isPresent()) {
            DatosListadoTopico datosTopico = new DatosListadoTopico(topicoOptional.get());
            return ResponseEntity.ok(datosTopico);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosListadoTopico> actualizarTopico(@PathVariable Long id, @RequestBody @Valid DatosActualizacionTopico datos) {
        Optional<Topico> topicoOptional = topicoRepository.findById(id);

        if (!topicoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Topico topico = topicoOptional.get();

        if (datos.titulo() != null || datos.mensaje() != null) {
            String nuevoTitulo = (datos.titulo() != null) ? datos.titulo() : topico.getTitulo();
            String nuevoMensaje = (datos.mensaje() != null) ? datos.mensaje() : topico.getMensaje();

            if ((!nuevoTitulo.equals(topico.getTitulo()) || !nuevoMensaje.equals(topico.getMensaje())) &&
                    topicoRepository.existsByTituloAndMensajeAndIdIsNot(nuevoTitulo, nuevoMensaje, id)) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        if (datos.titulo() != null) {
            topico.setTitulo(datos.titulo());
        }
        if (datos.mensaje() != null) {
            topico.setMensaje(datos.mensaje());
        }
        if (datos.status() != null) {
            topico.setStatus(datos.status());
        }

        return ResponseEntity.ok(new DatosListadoTopico(topico));
    }


    @DeleteMapping("/{id}") // Mapea a /topicos/{id} para solicitudes DELETE
    @Transactional
    public ResponseEntity<?> eliminarTopico(@PathVariable Long id) {
        // 1. Verificar si el tópico existe
        Optional<Topico> topicoOptional = topicoRepository.findById(id);

        if (topicoOptional.isPresent()) {
            // Si el tópico existe, lo eliminamos
            topicoRepository.deleteById(id);
            // Devolvemos 204 No Content para indicar que la eliminación fue exitosa
            // y no hay contenido para devolver.
            return ResponseEntity.noContent().build();
        } else {
            // Si el tópico no se encuentra, devolvemos 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
}