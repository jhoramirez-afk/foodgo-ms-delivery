package cl.duoc.jv0101.foodgo.delivery.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import cl.duoc.jv0101.foodgo.delivery.model.Envio;

public interface EnvioRepository extends JpaRepository<Envio, Long> {
    @Override
    @EntityGraph(attributePaths = "tracking")
    List<Envio> findAll();

    @Override
    @EntityGraph(attributePaths = "tracking")
    Optional<Envio> findById(Long id);
}
