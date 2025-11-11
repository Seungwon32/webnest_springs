package com.app.webnest.repository;

import com.app.webnest.domain.dto.GameJoinDTO;
import com.app.webnest.domain.dto.GameRoomDTO;
import com.app.webnest.mapper.GameJoinMapper;
import com.app.webnest.mapper.GameRoomMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GameJoinDAO {

    private final GameJoinMapper gameJoinMapper;

    // 플레이어 전체조회
    public List<GameJoinDTO> getPlayers() {
        return gameJoinMapper.selectAll();
    }

}
