package br.com.tcloss.seletivoseplagapi.infra.adapters;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.github.f4b6a3.uuid.UuidCreator;

import br.com.tcloss.seletivoseplagapi.application.ports.FileManager;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.http.Method;
import io.quarkus.logging.Log;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MinioFileManager implements FileManager {

    private final MinioClient minioClient;
    private final String bucket;
    private final String externalUrl;
    private final String internalUrl;
    private MinioClient minioPresigned;
    private final String accessKey;
    private final String secretKey;

    public MinioFileManager(MinioClient minioClient,
            @ConfigProperty(name = "application.uploads.bucket") String bucket,
            @ConfigProperty(name = "quarkus.minio.access-key") String accessKey,
            @ConfigProperty(name = "quarkus.minio.secret-key") String secretKey,
            @ConfigProperty(name = "application.uploads.external.url", defaultValue = " ") String externalUrl,
            @ConfigProperty(name = "application.uploads.internal.url", defaultValue = " ") String internalUrl) {
        this.minioClient = minioClient;
        this.bucket = bucket;
        this.externalUrl = externalUrl;
        this.internalUrl = internalUrl;
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    @PostConstruct
    void init() {
        if (externalUrl.isBlank()) {
            minioPresigned = minioClient;
            return;
        }
        minioPresigned = MinioClient.builder().region("us-east-1").endpoint(externalUrl).credentials(accessKey, secretKey).build();
        
    }

    @Override
    public String upload(Path fileToUpload, String originalName, String contentType) {
        final String extension = originalName.substring(originalName.lastIndexOf("."));
        final String key = String.format("albums/%s%s", UuidCreator.getTimeOrderedEpoch(), extension);
        try {
            final UploadObjectArgs uploadObject = UploadObjectArgs.builder()
                    .bucket(bucket)
                    .object(key)
                    .contentType(contentType)
                    .filename(fileToUpload.toString())
                    .build();
            minioClient.uploadObject(uploadObject);

        } catch (Exception e) {
            Log.errorf(e, "Erro no upload do arquivo %s", key);
            throw new InternalError();
        }
        return key;
    }

    @Override
    public void delete(String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucket).object(fileName).build());
        } catch (Exception e) {
            Log.errorf(e, "Erro ao excluir o arquivo %s", fileName);
            throw new InternalError();
        }
    }

    @Override
    public URI generatePresignedUrl(String fileName, Duration timer) {
        try {
            final var url = minioPresigned.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucket)
                            .object(fileName)
                            .expiry((int) timer.getSeconds())
                            .build());
            return URI.create(url);
        } catch (Exception e) {
            Log.errorf(e, "Erro ao assinar a url do arquivo %s", fileName);
            return null;
        }
    }

}
