package in.astro.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface IFileUploadService {
    public Map<?, ?> uploadImage(MultipartFile file);
}
