package cl.duoc.jv0101.foodgo.delivery.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import cl.duoc.jv0101.foodgo.delivery.model.Envio;

import static org.assertj.core.api.Assertions.assertThat;

public class EnvioSteps {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate rest;

    private Long id;
    private ResponseEntity<Envio> respuesta;
    private ResponseEntity<Void> respuestaVoid;

    private String url() {
        return "http://localhost:" + port + "/api/envios";
    }

    private HttpEntity<Map<String, Object>> body(String valor) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(Map.of("pedido", valor, "repartidor", "Diego Herrera", "estado", "ASIGNADO"), headers);
    }

    @Given("el servicio {string} está disponible")
    public void servicioDisponible(String servicio) {
        ResponseEntity<Envio[]> listado = rest.getForEntity(url(), Envio[].class);
        assertThat(listado.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @When("consulto el listado de {string}")
    public void consultarListado(String recurso) {
        ResponseEntity<Envio[]> listado = rest.getForEntity(url(), Envio[].class);
        assertThat(listado.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Then("el listado responde con código {int}")
    public void listadoCodigo(int codigo) {
        assertThat(codigo).isEqualTo(200);
    }

    @Given("un nuevo {string} con pedido {string}")
    public void crearRecurso(String recurso, String valor) {
        ResponseEntity<Envio> creado = rest.postForEntity(url(), body(valor), Envio.class);
        assertThat(creado.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        id = creado.getBody().getId();
    }

    @When("consulto el {string} recién creado")
    public void consultarCreado(String recurso) {
        respuesta = rest.getForEntity(url() + "/" + id, Envio.class);
    }

    @Then("el recurso tiene pedido {string} y código {int}")
    public void recursoConValor(String valor, int codigo) {
        assertThat(respuesta.getStatusCode().value()).isEqualTo(codigo);
        assertThat(respuesta.getBody().getPedido()).isEqualTo(valor);
    }

    @When("actualizo el {string} con pedido {string}")
    public void actualizar(String recurso, String valor) {
        respuesta = rest.exchange(url() + "/" + id, HttpMethod.PUT, body(valor), Envio.class);
    }

    @Then("el recurso queda con pedido {string} y código {int}")
    public void recursoActualizado(String valor, int codigo) {
        assertThat(respuesta.getStatusCode().value()).isEqualTo(codigo);
        assertThat(respuesta.getBody().getPedido()).isEqualTo(valor);
    }

    @When("elimino el {string}")
    public void eliminar(String recurso) {
        respuestaVoid = rest.exchange(url() + "/" + id, HttpMethod.DELETE, null, Void.class);
    }

    @Then("la eliminación responde con código {int}")
    public void eliminarCodigo(int codigo) {
        assertThat(respuestaVoid.getStatusCode().value()).isEqualTo(codigo);
    }

    @Then("al consultar el {string} eliminado responde {int}")
    public void consultarEliminado(String recurso, int codigo) {
        ResponseEntity<Envio> noExiste = rest.getForEntity(url() + "/" + id, Envio.class);
        assertThat(noExiste.getStatusCode().value()).isEqualTo(codigo);
    }
}
