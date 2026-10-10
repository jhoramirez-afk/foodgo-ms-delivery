package cl.duoc.jv0101.foodgo.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CrudIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;

    private String unique(String json) { return json.replace("TEST20261009", UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase()); }

    private long createParent() throws Exception {
        String result = mvc.perform(post("/api/envios").contentType("application/json")
                .content(unique("""
{"pedido":"PED-TEST20261009","repartidor":"Diego Herrera","estado":"ASIGNADO"}
"""))).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return mapper.readTree(result).get("id").asLong();
    }

    private long createChild(String nested) throws Exception {
        String result = mvc.perform(post(nested).contentType("application/json")
                .content("""
{"estado":"ASIGNADO","latitud":-33.43121,"longitud":-70.61975,"fechaHora":"2026-10-01T13:30:00"}
""")).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        return mapper.readTree(result).get("id").asLong();
    }

    @Test
    void crudRelationsAndCascadeThroughHttp() throws Exception {
        long id = createParent();
        String nested = "/api/envios/" + id + "/tracking";
        long childId = createChild(nested);
        mvc.perform(get("/api/envios/" + id)).andExpect(status().isOk()).andExpect(jsonPath("$.tracking[0].id").value(childId));
        mvc.perform(get("/api/envios")).andExpect(status().isOk());
        mvc.perform(get(nested)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(childId));
        mvc.perform(get("/api/tracking/" + childId)).andExpect(status().isOk());
        mvc.perform(put("/api/envios/" + id).contentType("application/json").content(unique("""
{"pedido":"PED-TEST20261009","repartidor":"Diego Herrera Muñoz","estado":"ASIGNADO"}
"""))).andExpect(status().isOk());
        mvc.perform(put("/api/tracking/" + childId).contentType("application/json").content("""
{"estado":"EN_CAMINO","latitud":-33.44712,"longitud":-70.59968,"fechaHora":"2026-10-01T13:35:00"}
""")).andExpect(status().isOk());
        mvc.perform(delete("/api/tracking/" + childId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/tracking/" + childId)).andExpect(status().isNotFound());
        long cascadeId = createChild(nested);
        mvc.perform(delete("/api/envios/" + id)).andExpect(status().isNoContent());
        mvc.perform(get("/api/envios/" + id)).andExpect(status().isNotFound());
        mvc.perform(get("/api/tracking/" + cascadeId)).andExpect(status().isNotFound());
    }

    @Test
    void malformedJsonAndIdsReturnStructuredErrors() throws Exception {
        mvc.perform(post("/api/envios").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
        mvc.perform(get("/api/envios/no-es-numero"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void validationAndMissingResourcesReturnStructuredErrors() throws Exception {
        mvc.perform(post("/api/envios").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.validationErrors").isNotEmpty());
        mvc.perform(get("/api/envios/9223372036854775807"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
        mvc.perform(post("/api/envios/9223372036854775807/tracking").contentType("application/json")
                .content("""
{"estado":"ASIGNADO","latitud":-33.43121,"longitud":-70.61975,"fechaHora":"2026-10-01T13:30:00"}
""")).andExpect(status().isNotFound());
        var longBody = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree("""
{"pedido":"PED-TEST20261009","repartidor":"Diego Herrera","estado":"ASIGNADO"}
""");
        longBody.put("pedido", "X".repeat(300));
        mvc.perform(post("/api/envios").contentType("application/json").content(longBody.toString())).andExpect(status().isBadRequest());
    }

    static Stream<Arguments> invalidInputs() {
        return Stream.of(
            Arguments.of("Estado inválido", "parent", """
{"pedido":"PED-TEST20261009","repartidor":"Diego Herrera","estado":"VOLANDO"}
""", "estado"),
            Arguments.of("Repartidor obligatorio", "parent", """
{"pedido":"PED-TEST20261009","repartidor":"","estado":"ASIGNADO"}
""", "repartidor"),
            Arguments.of("Latitud fuera de rango", "child", """
{"estado":"ASIGNADO","latitud":-91,"longitud":-70.61975,"fechaHora":"2026-10-01T13:30:00"}
""", "latitud"),
            Arguments.of("Longitud fuera de rango", "child", """
{"estado":"ASIGNADO","latitud":-33.43121,"longitud":181,"fechaHora":"2026-10-01T13:30:00"}
""", "longitud"),
            Arguments.of("Coordenada obligatoria", "child", """
{"estado":"ASIGNADO","latitud":null,"longitud":-70.61975,"fechaHora":"2026-10-01T13:30:00"}
""", "latitud"),
            Arguments.of("Fecha futura", "child", """
{"estado":"ASIGNADO","latitud":-33.43121,"longitud":-70.61975,"fechaHora":"2099-10-09T12:00:00"}
""", "fechaHora")
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidInputs")
    void businessValidationReturns400WithField(String name, String target, String body, String field) throws Exception {
        if ("parent".equals(target)) {
            mvc.perform(post("/api/envios").contentType("application/json").content(unique(body)))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.validationErrors." + field).exists());
        } else {
            long id = createParent();
            mvc.perform(post("/api/envios/" + id + "/tracking").contentType("application/json").content(body))
                    .andExpect(status().isBadRequest()).andExpect(jsonPath("$.validationErrors." + field).exists());
            mvc.perform(delete("/api/envios/" + id)).andExpect(status().isNoContent());
        }
    }

    @Test
    void trackingMantieneEstadoYPrecisionDeCoordenadas() throws Exception {
        long id = createParent();
        String nested = "/api/envios/" + id + "/tracking";
        long evento = createChild(nested);
        mvc.perform(get("/api/tracking/" + evento)).andExpect(jsonPath("$.latitud").value(-33.43121));
        mvc.perform(put("/api/tracking/" + evento).contentType("application/json").content("""
{"estado":"EN_CAMINO","latitud":-33.44712,"longitud":-70.59968,"fechaHora":"2026-10-01T13:35:00"}
""")).andExpect(status().isOk());
        mvc.perform(get("/api/envios/" + id)).andExpect(jsonPath("$.estado").value("EN_CAMINO"));
        mvc.perform(put("/api/envios/" + id).contentType("application/json").content("""
{"pedido":"PED-TEST20261009","repartidor":"Diego Herrera Muñoz","estado":"ASIGNADO"}
""")).andExpect(status().isBadRequest());
        createChild(nested);
        mvc.perform(get("/api/envios/" + id)).andExpect(jsonPath("$.estado").value("EN_CAMINO"));
        mvc.perform(delete("/api/tracking/" + evento)).andExpect(status().isNoContent());
        mvc.perform(get("/api/envios/" + id)).andExpect(jsonPath("$.estado").value("ASIGNADO"));
        mvc.perform(delete("/api/envios/" + id)).andExpect(status().isNoContent());
    }

}
