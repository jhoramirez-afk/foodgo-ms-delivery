package cl.duoc.jv0101.foodgo.delivery.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cl.duoc.jv0101.foodgo.delivery.model.EventoTracking;
import cl.duoc.jv0101.foodgo.delivery.service.EventoTrackingService;

@RestController
@RequestMapping("/api")
public class EventoTrackingController {

    private final EventoTrackingService service;

    public EventoTrackingController(EventoTrackingService service) {
        this.service = service;
    }

    @GetMapping("/envios/{envioId}/tracking")
    public ResponseEntity<List<EventoTracking>> listarPorEnvio(@PathVariable Long envioId) {
        return ResponseEntity.ok(service.findByEnvioId(envioId));
    }

    @PostMapping("/envios/{envioId}/tracking")
    public ResponseEntity<EventoTracking> crear(@PathVariable Long envioId, @Valid @RequestBody EventoTracking recurso) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(envioId, recurso));
    }

    @GetMapping("/tracking/{id}")
    public ResponseEntity<EventoTracking> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping("/tracking/{id}")
    public ResponseEntity<EventoTracking> actualizar(@PathVariable Long id, @Valid @RequestBody EventoTracking datos) {
        return ResponseEntity.ok(service.update(id, datos));
    }

    @DeleteMapping("/tracking/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
