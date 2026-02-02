package br.com.tcloss.seletivoseplagapi.infra.adapters;

import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import com.github.f4b6a3.uuid.UuidCreator;

import br.com.tcloss.seletivoseplagapi.application.ports.FileManager;
import io.minio.MinioClient;
import io.minio.RemoveObjectArgs;
import io.minio.UploadObjectArgs;
import io.minio.messages.DeleteObject;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MinioFileManager implements FileManager {

    private final MinioClient minioClient;
    private final String bucket;

    public MinioFileManager(MinioClient minioClient,
            @ConfigProperty(name = "application.uploads.bucket") String bucket) {
        this.minioClient = minioClient;
        this.bucket = bucket;
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

}
