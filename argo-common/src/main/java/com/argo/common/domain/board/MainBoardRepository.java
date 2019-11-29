package com.argo.common.domain.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Transactional
public interface MainBoardRepository extends JpaRepository<MainBoard, Long> {
    public static final String FIND_LIST = "SELECT board_id, user_email, title, created_at FROM main_board WHERE deleted = false";
    MainBoard findMainBoardByBoardId(Long boardId);
    boolean existsMainBoardByBoardId(Long boardId);
    ArrayList<MainBoard> findAllByDeletedIsFalse();

    @Query(value = FIND_LIST, nativeQuery = true)
     List<List<Object>> findList();
//
//    @Query(value = RESTART_AFTER, nativeQuery = true)
//    void resetBoardIdAfterDelete();
}
