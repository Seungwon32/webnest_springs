package com.app.webnest.api.privateapi;

import com.app.webnest.domain.dto.ApiResponseDTO;
import com.app.webnest.domain.dto.TypingRecordDTO;
import com.app.webnest.service.AuthService;
import com.app.webnest.service.TypingRecordService;
import com.app.webnest.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/typing")
public class TypingRecordApi {
    private final TypingRecordService typingRecordService;
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/save")
    public ResponseEntity<ApiResponseDTO> saveRecord(@RequestBody Map<String, Object> body) {
        double wpm = Double.parseDouble(body.get("wpm").toString());
        double accuracy = Double.parseDouble(body.get("accuracy").toString());
        String time = body.get("time").toString();
        Long userId = Long.parseLong(body.get("userId").toString());
        Long contentsId = Long.parseLong(body.get("typingContentsId").toString());

        typingRecordService.saveRecord(wpm, accuracy, time, userId, contentsId);

        return ResponseEntity.ok(ApiResponseDTO.of("타자 기록 저장 완료", null));
    }

    @GetMapping("/records")
    public ResponseEntity<ApiResponseDTO> getMyRecords(Authentication authentication) {
        String email = authService.getUserEmailFromAuthentication(authentication);
        Long userId = userService.getUserIdByUserEmail(email);
        List<TypingRecordDTO> records = typingRecordService.getUserRecords(userId);
        return ResponseEntity.ok(ApiResponseDTO.of("타자 기록 조회 성공", records));
    }
}
