package com.app.webnest.service;

import com.app.webnest.domain.dto.GameJoinDTO;
import com.app.webnest.domain.vo.GameJoinVO;

import java.util.List;
import java.util.Optional;

public interface GameJoinService {

    // 게임방 참여
    public void join(GameJoinVO gameJoinVO);

    // 게임방 종료
    public void leave(GameJoinVO gameJoinVO);

    // 게임방 플레이어 전체 조회
    public List<GameJoinDTO> getPlayers(Long gameRoomId);

    public void update(GameJoinVO gameJoinVO);

    public Optional<GameJoinVO> getGameJoinDTOByGameRoomId(GameJoinVO gameJoinVO);
}
