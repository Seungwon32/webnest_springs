package com.app.webnest.service;

import com.app.webnest.domain.dto.GameRoomDTO;
import com.app.webnest.exception.RoomException;
import com.app.webnest.mapper.GameRoomMapper;
import com.app.webnest.repository.GameRoomDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class GameRoomServiceImpl implements GameRoomService {

    private final GameRoomDAO gameRoomDAO;

    // 게임방 목록
    @Override
    public List<GameRoomDTO> getRooms() {
        return gameRoomDAO.getRooms();
    }

    // 게임방
    @Override
    public GameRoomDTO getRoom(Long id) {
        return gameRoomDAO.getRoom(id).orElseThrow(() -> new RuntimeException("채팅방 조회 오류"));
    }
}
