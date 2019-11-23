package com.argo.common.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MainBoardRepository extends JpaRepository<MainBoard, Long> {
    MainBoard findMainBoardByBoardNumber(int BoardNo);
    boolean existsMainBoardByBoardNumber(int BoardNo);
}
