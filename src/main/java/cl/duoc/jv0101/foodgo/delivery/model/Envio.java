package cl.duoc.jv0101.foodgo.delivery.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;


@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El pedido es obligatorio")
    @Column(nullable = false)
    private String pedido;
    @Column
    private String repartidor;
    @Column
    private String estado;

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getPedido() { return pedido; }

    public void setPedido(String pedido) { this.pedido = pedido; }

    public String getRepartidor() { return repartidor; }

    public void setRepartidor(String repartidor) { this.repartidor = repartidor; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

}
