package br.com.tcloss.seletivoseplagapi.infra;

import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.MinIOContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class TestContainerResource implements QuarkusTestResourceLifecycleManager {

    private static final String DB_USER = "test-user";

    private static final String DB_PASSWORD = "test-password";

    private static final String DB_NAME = "teste-bd";

    private static final String MINIO_USER = "miniouser";

    private static final String MINIO_PASSWORD = "miniopass";

    private static final String MINIO_BUCKET = "uploads";

    static PostgreSQLContainer<?> db = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USER)
            .withPassword(DB_PASSWORD);

    static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:alpine"))
            .withExposedPorts(6379);
    static MinIOContainer minio = new MinIOContainer("minio/minio:RELEASE.2025-09-07T16-13-09Z")
            .withUserName(MINIO_USER)
            .withPassword(MINIO_PASSWORD);

    @Override
    public Map<String, String> start() {
        db.start();
        redis.start();
        minio.start();
        return Map.of(
                "quarkus.minio.access-key", minio.getUserName(),
                "quarkus.minio.secret-key", minio.getPassword(),
                "quarkus.minio.url", minio.getS3URL(),
                "application.uploads.bucket", MINIO_BUCKET,
                "quarkus.datasource.jdbc.url", db.getJdbcUrl(),
                "quarkus.datasource.username", DB_USER,
                "quarkus.datasource.password", DB_PASSWORD,
                "quarkus.redis.hosts", "redis://" + redis.getHost() + ":" + redis.getFirstMappedPort());
    }

    @Override
    public void stop() {
        redis.stop();
        db.stop();
        minio.stop();
    }

}
