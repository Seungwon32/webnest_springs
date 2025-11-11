package com.app.webnest.mapper;

import com.app.webnest.domain.dto.GameJoinDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GameJoinMapper {

    public List<GameJoinDTO> selectAll();
}
