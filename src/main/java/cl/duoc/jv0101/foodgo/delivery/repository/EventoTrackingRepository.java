package cl.duoc.jv0101.foodgo.delivery.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.jv0101.foodgo.delivery.model.EventoTracking;

public interface EventoTrackingRepository extends JpaRepository<EventoTracking, Long> {
    List<EventoTracking> findByEnvio_Id(Long envioId);
}
