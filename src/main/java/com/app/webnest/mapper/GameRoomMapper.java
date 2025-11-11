package com.app.webnest.mapper;

import com.app.webnest.domain.dto.GameRoomDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface GameRoomMapper {

    // 게임방 전체 조회
    public List<GameRoomDTO> selectAll();

    // 게임방 단일 조회
    public Optional<GameRoomDTO> select(Long id);

}
