package cl.duoc.jv0101.foodgo.delivery.service;

import java.util.List;
import java.util.Comparator;
import cl.duoc.jv0101.foodgo.delivery.model.EventoTracking;
import cl.duoc.jv0101.foodgo.delivery.exception.BusinessRuleException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cl.duoc.jv0101.foodgo.delivery.model.Envio;
import cl.duoc.jv0101.foodgo.delivery.repository.EnvioRepository;

@Service
@Transactional
public class EnvioService {

    private final EnvioRepository repository;

    public EnvioService(EnvioRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Envio> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Envio> findById(Long id) {
        return repository.findById(id);
    }

    public Envio create(Envio recurso) {
        recurso.setId(null);
        recurso.getTracking().forEach(item -> item.setId(null));
        if (!recurso.getTracking().isEmpty()) recurso.setEstado(ultimoEstado(recurso.getTracking()));
        return repository.save(recurso);
    }

    public Optional<Envio> update(Long id, Envio datos) {
        return repository.findById(id).map(existente -> {
            existente.setPedido(datos.getPedido());
            existente.setRepartidor(datos.getRepartidor());
            if (!existente.getTracking().isEmpty() && !ultimoEstado(existente.getTracking()).equals(datos.getEstado())) {
                throw new BusinessRuleException("estado", "El estado debe coincidir con el último evento de tracking");
            }
            existente.setEstado(datos.getEstado());
            return repository.save(existente);
        });
    }

    public boolean delete(Long id) {
        return repository.findById(id).map(existente -> {
            repository.delete(existente);
            return true;
        }).orElse(false);
    }
    public static String ultimoEstado(List<EventoTracking> eventos) {
        return eventos.stream().max(Comparator.comparing(EventoTracking::getFechaHora)
                .thenComparing(evento -> evento.getId() == null ? Long.MAX_VALUE : evento.getId()))
                .map(EventoTracking::getEstado).orElse("ASIGNADO");
    }
}
