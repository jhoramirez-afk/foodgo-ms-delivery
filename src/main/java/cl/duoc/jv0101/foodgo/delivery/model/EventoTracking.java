package cl.duoc.jv0101.foodgo.delivery.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "eventos_tracking")
public class EventoTracking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Estado de tracking es obligatorio")
    @Pattern(regexp = "ASIGNADO|RETIRADO|EN_CAMINO|ENTREGADO|CANCELADO", message = "Estado de tracking debe ser ASIGNADO, RETIRADO, EN_CAMINO, ENTREGADO, CANCELADO")
    @Column(nullable = false)
    private String estado;

    @NotNull(message = "Latitud es obligatoria")
    @DecimalMin(value = "-90", message = "La latitud mínima es -90")
    @DecimalMax(value = "90", message = "La latitud máxima es 90")
    @Digits(integer = 2, fraction = 6)
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitud;

    @NotNull(message = "Longitud es obligatoria")
    @DecimalMin(value = "-180", message = "La longitud mínima es -180")
    @DecimalMax(value = "180", message = "La longitud máxima es 180")
    @Digits(integer = 3, fraction = 6)
    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitud;

    @NotNull(message = "Fecha de tracking es obligatoria")
    @PastOrPresent(message = "La fecha no puede estar en el futuro")
    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "envio_id", nullable = false)
    @JsonBackReference("envio-tracking")
    private Envio envio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public BigDecimal getLatitud() {
        return latitud;
    }

    public void setLatitud(BigDecimal latitud) {
        this.latitud = latitud;
    }

    public BigDecimal getLongitud() {
        return longitud;
    }

    public void setLongitud(BigDecimal longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(LocalDateTime fechaHora) {
        this.fechaHora = fechaHora;
    }

    public Envio getEnvio() {
        return envio;
    }

    public void setEnvio(Envio envio) {
        this.envio = envio;
    }
}
