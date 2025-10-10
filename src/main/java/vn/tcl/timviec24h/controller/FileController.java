package vn.tcl.timviec24h.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.tcl.timviec24h.domain.response.file.ResUploadFileDTO;
import vn.tcl.timviec24h.service.FileService;
import vn.tcl.timviec24h.util.annotation.ApiMessage;
import vn.tcl.timviec24h.util.error.StorageException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class FileController {
    private final FileService fileService;
    @Value("${timviec24h.upload-file.base-uri}")
    private String baseURI;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/files")
    @ApiMessage("Upload single file")
    public ResponseEntity<ResUploadFileDTO> uploadFile(@RequestParam(name = "file", required = false) MultipartFile file, @RequestParam("folder") String folder) throws URISyntaxException, IOException, StorageException {
        //skip validate
        if (file == null || file.isEmpty()) {
            throw new StorageException("File is empty. Please upload file");
        }
        String fileName = file.getOriginalFilename();
        List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx");
        boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
        if (!isValid) {
            throw new StorageException("File is not allowed. Please upload file: " + allowedExtensions.toString());
        }
        //create a directory if not exist
        fileService.createDirectory(baseURI + folder);
        //store file
        String uploadFile = fileService.store(file, folder);
        ResUploadFileDTO res = new ResUploadFileDTO(uploadFile, Instant.now());

        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/files")
    @ApiMessage("Download file")
    public ResponseEntity<Resource> downloadFile(@RequestParam(name = "fileName", required = false) String fileName,
                                                 @RequestParam(name = "folder", required = false) String folder)
            throws StorageException, URISyntaxException, IOException {
        if (fileName == null || folder == null) {
            throw new StorageException("FileName and Folder not null");
        }
        long length = fileService.getFileLength(fileName, folder);
        if (length == 0) {
            throw new StorageException("File is empty. Please upload file");
        }
        //downloadFile
        InputStreamResource resource = fileService.getResource(fileName, folder);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentLength(length)
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(resource);
    }
}
