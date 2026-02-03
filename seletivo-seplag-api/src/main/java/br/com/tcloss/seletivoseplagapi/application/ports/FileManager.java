package br.com.tcloss.seletivoseplagapi.application.ports;

import java.net.URI;
import java.nio.file.Path;
import java.time.Duration;

public interface FileManager {
    public String upload(Path fileToUpload, String originalName, String contentType);

    public void delete(String fileName);

    public URI generatePresignedUrl(String fileName, Duration timer);
}
