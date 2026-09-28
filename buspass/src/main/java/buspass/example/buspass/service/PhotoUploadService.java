package buspass.example.buspass.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class PhotoUploadService {

    private final Path uploadDirectory =
            Paths.get("uploads");

    public String uploadPhoto(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new RuntimeException("Please upload a photo");
        }

        String contentType = file.getContentType();

        if (contentType == null ||
                !contentType.startsWith("image/")) {
            throw new RuntimeException(
                    "Only image files are allowed"
            );
        }

        try {

            Files.createDirectories(uploadDirectory);

            String originalFileName =
                    file.getOriginalFilename();

            String extension = "";

            if (originalFileName != null &&
                    originalFileName.contains(".")) {

                extension =
                        originalFileName.substring(
                                originalFileName.lastIndexOf(".")
                        );
            }

            String fileName =
                    UUID.randomUUID() + extension;

            Path filePath =
                    uploadDirectory.resolve(fileName);

            Files.copy(
                    file.getInputStream(),
                    filePath
            );

            return fileName;

        } catch (IOException exception) {

            throw new RuntimeException(
                    "Photo upload failed"
            );
        }
    }
}