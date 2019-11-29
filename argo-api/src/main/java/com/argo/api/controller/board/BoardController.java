package com.argo.api.controller.board;


import com.argo.common.domain.board.*;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.List;

@Slf4j
@RequestMapping(value = "/board")
@RestController
public class BoardController {

    @Autowired
    MainBoardRepository mainBoardRepository;
    @Autowired
    MainBoardService mainBoardService;

    //목록게시
    @GetMapping(value = "list/all")
    public List<MainBoard> listAll() {
        return mainBoardRepository.findAllByDeletedIsFalse();
    }

     /** need specification while retrieving the whole list**/
    @GetMapping(value = "/list")
    @ResponseBody
    public List<List<Object>> getList() {
//        Gson gson = new Gson;
//        String json = gson.toJson(mainBoardService.getNotDeletedList());
        return mainBoardService.getNotDeletedList();
    }

//    @PostMapping(value = "list/sample")
//    public void sampleTest(@RequestBody MainBoard sample) {
//        Log.info(" sample param : {}", sample.toString());
//        mainBoardRepository.save(sample);
//    }


    //읽기
    @GetMapping(value = "/read/{boardId}")
    public MainBoard findBoard(@PathVariable Long boardId) {
        if (assertExists(boardId)) {
            return mainBoardService.getBoardById(boardId);
        } else {
            log.error("no board of such board id exists");
            return null;
        }
    }


    //게시글 등록
    // send Serial board_id at the creation stage //
    @PostMapping(value = "/post/save")
    public ResponseEntity<BoardReturnParam> addNewPostBoard(@RequestBody BoardReceiverParam newPost) throws InvalidInputException {
        return mainBoardService.addNewPostBoard(newPost);
    }


//    @PostMapping(value = "/post/reply/boardId/parent")
//    public ReplyBoard addReplyToBoard(@RequestBody Long boardId,  String reply_text,
//                                      @ Long parent, @RequestBody boolean admin_reply, @RequestBody boolean user_reply) {
//        return replyBoardService.addReplyBoard(boardId, reply_text, parent, admin_reply, user_reply);
//    }


//    @PostMapping(value = "/post/reply")
//    public void addAdminNewReply(@RequestBody ReplyBoard replyBoard) {
//    }

    @GetMapping(value = "/list/check/{boardId}")
    public boolean assertExists(@PathVariable Long boardId) {
        if (mainBoardService.assertExists(boardId)) {
            return true;
        };
        return false;
    }


    //게시물 삭제
    @GetMapping(value= "/read/delete/{boardId}")
    public void deleteBoard(@PathVariable Long boardId) throws Exception {
        try {
            mainBoardService.deleteBoardByBoardId(boardId);
        } catch (Exception E) {
            throw new Exception("no board of such id has been detected");
        }
    }
}
