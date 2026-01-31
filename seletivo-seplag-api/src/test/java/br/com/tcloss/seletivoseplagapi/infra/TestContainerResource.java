package br.com.tcloss.seletivoseplagapi.infra;

import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class TestContainerResource implements QuarkusTestResourceLifecycleManager {

    private static final String DB_USER = "test-user";

    private static final String DB_PASSWORD = "test-password";

    private static final String DB_NAME = "teste-bd";

    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USER)
            .withPassword(DB_PASSWORD);

    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:alpine"))
            .withExposedPorts(6379);

    @Override
    public Map<String, String> start() {
        db.start();
        redis.start();

        return Map.of(
                "quarkus.datasource.jdbc.url", db.getJdbcUrl(),
                "quarkus.datasource.username", DB_USER,
                "quarkus.datasource.password", DB_PASSWORD,
                "quarkus.redis.hosts", "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
    }

    @Override
    public void stop() {
        redis.stop();
        db.stop();
    }

}
