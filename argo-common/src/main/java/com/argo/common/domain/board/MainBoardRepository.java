package com.argo.common.domain.board;

import com.google.gson.JsonArray;
import org.json.simple.JSONArray;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import springfox.documentation.spring.web.json.Json;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Repository
public interface MainBoardRepository extends JpaRepository<MainBoard, Long> {
    MainBoard findMainBoardByBoardId(Long boardId);
    boolean existsMainBoardByBoardId(Long boardId);
    List<MainBoard> findAllByParentIsNullAndDeletedIsFalseOrderByCreatedAtDesc();
    List<MainBoard> findAllByDeletedIsFalseAndParentEqualsOrderByCreatedAt(Long boardId);
}
