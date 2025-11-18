package com.app.webnest.api.publicapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/file/*")
@Slf4j
@RequiredArgsConstructor
public class FileApi {

    @Value("${file.upload.path}")      // 예: C:/upload/
    private String uploadPath;

    // 업로드
    @PostMapping("upload")
    @ResponseBody
    public List<String> upload(@RequestParam("uploadFile") List<MultipartFile> uploadFiles) throws IOException {
        // 예: 2025/11/18/
        String datePath = getPath();

        // 예: C:/upload/2025/11/18/
        String rootPath = ensureTrailingSeparator(uploadPath) + datePath;
        log.info("rootPath: {}", rootPath);
        log.info("upload files: {}", uploadFiles);

        List<String> storedPaths = new ArrayList<>();

        // 디렉터리 생성
        File dir = new File(rootPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        for (MultipartFile uploadFile : uploadFiles) {
            // uuid 생성
            String uuid = UUID.randomUUID().toString();
            String originalName = uploadFile.getOriginalFilename();

            // 실제 파일명: uuid_원본이름
            String savedName = uuid + "_" + originalName;

            // 저장
            File saveFile = new File(rootPath, savedName);
            uploadFile.transferTo(saveFile);

            // 썸네일 생성 (이미지인 경우만)
            if (uploadFile.getContentType() != null &&
                    uploadFile.getContentType().startsWith("image")) {

                File thumbFile = new File(rootPath, "t_" + savedName);
                try (FileOutputStream out = new FileOutputStream(thumbFile)) {
                    Thumbnailator.createThumbnail(uploadFile.getInputStream(), out, 100, 100);
                }
            }

            // 프론트/DB에 저장할 상대 경로 (날짜 경로 + 파일명)
            // 예: 2025/11/18/uuid_ara.jpg
            String relativePath = datePath + savedName;
            storedPaths.add(relativePath);
        }

        log.info("uploaded file paths: {}", storedPaths);
        return storedPaths;
    }

    // 날짜 경로 생성
    private String getPath() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd/"));
    }

    // 마지막에 / 붙이기
    private String ensureTrailingSeparator(String path) {
        if (path.endsWith("/") || path.endsWith("\\")) {
            return path;
        }
        return path + "/";
    }

    // 로드
    @GetMapping("display")
    @ResponseBody
    public byte[] display(@RequestParam String fileName) throws IOException {
        // 기본 루트 경로 (ex. C:/upload/)
        String basePath = ensureTrailingSeparator(uploadPath);

        // fileName 앞에 / 있으면 제거
        while (fileName.startsWith("/") || fileName.startsWith("\\")) {
            fileName = fileName.substring(1);
        }

        // 최종 파일 객체 (ex. C:/upload/2025/11/18/uuid_ara.jpg)
        File file = new File(basePath, fileName);

        log.info("Requested file: {}", fileName);
        log.info("Full file path: {}", file.getAbsolutePath());
        log.info("File exists: {}", file.exists());

        if (!file.exists()) {
            log.error("File not found: {}", file.getAbsolutePath());
            throw new IOException("File not found: " + fileName);
        }

        return FileCopyUtils.copyToByteArray(file);
    }
}
