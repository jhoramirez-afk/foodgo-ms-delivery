package cl.duoc.jv0101.foodgo.delivery.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import jakarta.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Pedido es obligatorio")
    @Size(max = 255, message = "El campo admite hasta 255 caracteres")
    @Column(nullable = false)
    private String pedido;
    @NotBlank(message = "Repartidor es obligatorio")
    @Size(max = 255, message = "El campo admite hasta 255 caracteres")
    @Column(nullable = false)
    private String repartidor;
    @NotBlank(message = "Estado de envío es obligatorio")
    @Pattern(regexp = "ASIGNADO|RETIRADO|EN_CAMINO|ENTREGADO|CANCELADO", message = "Estado de envío debe ser ASIGNADO, RETIRADO, EN_CAMINO, ENTREGADO, CANCELADO")
    @Column(nullable = false)
    private String estado;

    @Valid
    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("envio-tracking")
    private List<EventoTracking> tracking = new ArrayList<>();

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getPedido() { return pedido; }

    public void setPedido(String pedido) { this.pedido = pedido; }

    public String getRepartidor() { return repartidor; }

    public void setRepartidor(String repartidor) { this.repartidor = repartidor; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public List<EventoTracking> getTracking() {
        return tracking;
    }

    public void setTracking(List<EventoTracking> items) {
        this.tracking.clear();
        if (items != null) {
            items.forEach(this::addEventoTracking);
        }
    }

    public void addEventoTracking(EventoTracking item) {
        tracking.add(item);
        item.setEnvio(this);
    }

    public void removeEventoTracking(EventoTracking item) {
        tracking.remove(item);
        item.setEnvio(null);
    }
}
