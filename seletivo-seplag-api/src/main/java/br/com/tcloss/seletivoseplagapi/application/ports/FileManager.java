package br.com.tcloss.seletivoseplagapi.application.ports;

import java.nio.file.Path;

public interface FileManager {
    public String upload(Path fileToUpload, String originalName, String contentType);

    public void delete(String fileName);
}
