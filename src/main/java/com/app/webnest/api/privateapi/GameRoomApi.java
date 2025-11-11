package com.app.webnest.api.privateapi;

import com.app.webnest.domain.dto.ApiResponseDTO;
import com.app.webnest.domain.dto.GameRoomDTO;
import com.app.webnest.repository.GameRoomDAO;
import com.app.webnest.service.GameRoomService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/game-rooms/*")
public class GameRoomApi {

    private final GameRoomService gameRoomService;

    @GetMapping("")
    public ResponseEntity<ApiResponseDTO<List<GameRoomDTO>>> getRooms() {
        List<GameRoomDTO> rooms = gameRoomService.getRooms();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.of("채팅방 목록조회", rooms));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<GameRoomDTO>> getRoom(@PathVariable Long id) {
        GameRoomDTO room = gameRoomService.getRoom(id);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseDTO.of("채팅방 목록조회", room));
    }


}
