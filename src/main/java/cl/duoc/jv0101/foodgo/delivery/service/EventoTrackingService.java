package cl.duoc.jv0101.foodgo.delivery.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.duoc.jv0101.foodgo.delivery.exception.ResourceNotFoundException;
import cl.duoc.jv0101.foodgo.delivery.model.EventoTracking;
import cl.duoc.jv0101.foodgo.delivery.model.Envio;
import cl.duoc.jv0101.foodgo.delivery.repository.EventoTrackingRepository;
import cl.duoc.jv0101.foodgo.delivery.repository.EnvioRepository;

@Service
@Transactional
public class EventoTrackingService {

    private final EventoTrackingRepository repository;
    private final EnvioRepository envioRepository;

    public EventoTrackingService(EventoTrackingRepository repository, EnvioRepository envioRepository) {
        this.repository = repository;
        this.envioRepository = envioRepository;
    }

    @Transactional(readOnly = true)
    public List<EventoTracking> findByEnvioId(Long envioId) {
        if (!envioRepository.existsById(envioId)) {
            throw new ResourceNotFoundException("Envio no encontrado con id " + envioId);
        }
        return repository.findByEnvio_Id(envioId);
    }

    @Transactional(readOnly = true)
    public EventoTracking findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("EventoTracking no encontrado con id " + id));
    }

    public EventoTracking create(Long envioId, EventoTracking recurso) {
        Envio envio = envioRepository.findById(envioId)
                .orElseThrow(() -> new ResourceNotFoundException("Envio no encontrado con id " + envioId));
        recurso.setId(null);
        recurso.setEnvio(envio);
        EventoTracking guardado = repository.save(recurso);
        actualizarEstado(envio);
        return guardado;
    }

    public EventoTracking update(Long id, EventoTracking datos) {
        EventoTracking existente = findById(id);
        existente.setEstado(datos.getEstado());
        existente.setLatitud(datos.getLatitud());
        existente.setLongitud(datos.getLongitud());
        existente.setFechaHora(datos.getFechaHora());
        EventoTracking guardado = repository.save(existente);
        actualizarEstado(existente.getEnvio());
        return guardado;
    }

    public void delete(Long id) {
        EventoTracking existente = findById(id);
        Envio envio = existente.getEnvio();
        repository.delete(existente);
        actualizarEstado(envio);
    }
    private void actualizarEstado(Envio envio) {
        envio.setEstado(EnvioService.ultimoEstado(repository.findByEnvio_Id(envio.getId())));
        // La entidad está administrada; JPA persiste el cambio al confirmar la transacción.
    }
}
