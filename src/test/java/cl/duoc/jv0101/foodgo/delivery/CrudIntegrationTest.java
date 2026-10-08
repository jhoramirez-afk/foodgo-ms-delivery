package cl.duoc.jv0101.foodgo.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CrudIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper mapper;
    @Autowired cl.duoc.jv0101.foodgo.delivery.repository.EventoTrackingRepository children;

    @Test
    void crudRelationsAndCascadeThroughHttp() throws Exception {
        String created = mvc.perform(post("/api/envios").contentType("application/json")
                .content("""
{"pedido": "PED-EP02", "repartidor": "Repartidor EP02", "estado": "ASIGNADO"}
"""))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long parentId = mapper.readTree(created).get("id").asLong();
        String nested = "/api/envios/%s/tracking".formatted(parentId);
        String child = mvc.perform(post(nested).contentType("application/json")
                .content("""
{"estado": "CREADO", "latitud": 10.0, "longitud": 10.0, "fechaHora": "2026-10-06T20:00:00"}
"""))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long childId = mapper.readTree(child).get("id").asLong();
        mvc.perform(get("/api/envios/" + parentId)).andExpect(status().isOk())
                .andExpect(jsonPath("$.tracking[0].id").value(childId));
        mvc.perform(get("/api/envios")).andExpect(status().isOk());
        mvc.perform(get(nested)).andExpect(status().isOk()).andExpect(jsonPath("$[0].id").value(childId));
        mvc.perform(get("/api/tracking/" + childId)).andExpect(status().isOk());
        mvc.perform(put("/api/envios/" + parentId).contentType("application/json")
                .content("""
{"pedido": "PED-EP02 actualizado", "repartidor": "Repartidor EP02", "estado": "ASIGNADO"}
""")).andExpect(status().isOk());
        mvc.perform(put("/api/tracking/" + childId).contentType("application/json")
                .content("""
{"estado": "CREADO", "latitud": 10.0, "longitud": 10.0, "fechaHora": "2026-10-06T20:00:00"}
""")).andExpect(status().isOk());
        mvc.perform(delete("/api/tracking/" + childId)).andExpect(status().isNoContent());
        mvc.perform(get("/api/tracking/" + childId)).andExpect(status().isNotFound());
        String second = mvc.perform(post(nested).contentType("application/json")
                .content("""
{"estado": "CREADO", "latitud": 10.0, "longitud": 10.0, "fechaHora": "2026-10-06T20:00:00"}
"""))
                .andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        long cascadeId = mapper.readTree(second).get("id").asLong();
        mvc.perform(delete("/api/envios/" + parentId)).andExpect(status().isNoContent());
        assertThat(children.existsById(cascadeId)).isFalse();
        mvc.perform(get("/api/envios/" + parentId)).andExpect(status().isNotFound());
    }

    @Test
    void malformedJsonAndIdsReturnStructuredErrors() throws Exception {
        mvc.perform(post("/api/envios").contentType("application/json").content("{"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
        mvc.perform(get("/api/envios/no-es-numero"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void databaseConstraintReturnsConflict() throws Exception {
        mvc.perform(post("/api/envios").contentType("application/json")
                .content("""
{"pedido": "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "repartidor": "Repartidor EP02", "estado": "ASIGNADO"}
"""))
                .andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void validationAndMissingResourcesReturnStructuredErrors() throws Exception {
        mvc.perform(post("/api/envios").contentType("application/json").content("{}"))
                .andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400));
        mvc.perform(get("/api/envios/9223372036854775807"))
                .andExpect(status().isNotFound()).andExpect(jsonPath("$.status").value(404));
        mvc.perform(post("/api/envios/%s/tracking".formatted(Long.MAX_VALUE)).contentType("application/json")
                .content("""
{"estado": "CREADO", "latitud": 10.0, "longitud": 10.0, "fechaHora": "2026-10-06T20:00:00"}
""")).andExpect(status().isNotFound());
    }
}
