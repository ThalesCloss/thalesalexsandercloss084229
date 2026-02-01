package br.com.tcloss.seletivoseplagapi.e2e;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import java.net.URI;
import java.time.Duration;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.AlbumDto;
import br.com.tcloss.seletivoseplagapi.application.dtos.input.album.TrackDto;
import br.com.tcloss.seletivoseplagapi.domain.model.artistprofile.ArtistProfile;
import br.com.tcloss.seletivoseplagapi.domain.model.composition.Composition;
import br.com.tcloss.seletivoseplagapi.domain.model.person.Person;
import br.com.tcloss.seletivoseplagapi.infra.DBCleaner;
import br.com.tcloss.seletivoseplagapi.infra.DBTestScenarioBuilder;
import br.com.tcloss.seletivoseplagapi.infra.TestContainerResource;
import br.com.tcloss.seletivoseplagapi.infra.WebSocketClient;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(TestContainerResource.class)
public class AlbumCreationE2ETest {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    DBCleaner dbCleaner;

    @Inject
    WebSocketClient webSocketClient;

    @Inject
    DBTestScenarioBuilder dbTestScenarioBuilder;

    @TestHTTPResource("/notifications")
    URI websocketUri;

    @BeforeEach
    void setup() {
        dbCleaner.truncateAll();
        webSocketClient.clear();
    }

    @AfterEach
    void tearDown() {
        webSocketClient.close();
    }

    @Test
    @DisplayName("Deve criar um album e enviar a notificação via websocket")
    @TestSecurity(user = "teste")
    void shouldCreateAlbumAndSendWebSocketNotification() throws JsonProcessingException {

        webSocketClient.connectWebSocket(websocketUri);
        final Person person = dbTestScenarioBuilder.createPerson();
        final ArtistProfile artist = dbTestScenarioBuilder.createSoloArtistProfile(person);
        final Composition composition = dbTestScenarioBuilder.createComposition(person);

        final TrackDto trackDto = new TrackDto("Teste faixa 1", composition.getId(), 1, 1, Duration.ofMinutes(3), null,
                null);
        final AlbumDto albumDto = new AlbumDto(
                "Album teste",
                artist.getId(),
                artist.getCurrentLineup().getId(),
                artist.getCurrentLineup().getDuration().startDate(),
                List.of(trackDto));
        final String albumRequest = objectMapper.writeValueAsString(albumDto);

        given()
        .contentType(MediaType.APPLICATION_JSON).body(albumRequest).when()
                .post("/v1/albums").then().statusCode(200);

        await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    final var webSocketMessage = webSocketClient.getLastMessage();
                    System.out.printf("mensagem %s", webSocketMessage.messageType());
                    assertThat(webSocketMessage.messageType()).isEqualTo("ALBUM_CREATED");
                    assertThat(webSocketMessage.payload()).isNotEmpty();
                });
    }

}
