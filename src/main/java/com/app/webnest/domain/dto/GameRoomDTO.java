package com.app.webnest.domain.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor @AllArgsConstructor
@Getter @Setter @ToString @EqualsAndHashCode(of = "id")
public class GameRoomDTO {
    private Long id; // erd pk키 확인
    private String gameRoomTitle;
    private boolean gameRoomIsTeam;
    private String gameRoomType; // erd 게임 유형 중복
    private boolean gameRoomIsOpen;
    private Integer gameRoomCurrentPlayer;
    private Integer gameRoomMaxPlayer;
    private boolean gameRoomIsStart;
    private String gameRoomPassKey;
    private LocalDateTime gameRoomCreateAt;

    // 게임방 목록을 조회할 때 참여한 유저를 모두 조회한다.
    private List<GameJoinDTO> players;
}
