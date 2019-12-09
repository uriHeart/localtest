package com.argo.api.controller.board;


import com.argo.common.domain.board.*;
import com.google.gson.JsonArray;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.util.MimeTypeUtils;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import java.util.Date;
import java.util.List;

@Slf4j
@RequestMapping(value = "/board")
@RestController
public class BoardController {

    @Autowired
    MainBoardRepository mainBoardRepository;
    @Autowired
    MainBoardService mainBoardService;

    // all return
    @GetMapping(value = "list/all", produces = { MimeTypeUtils.APPLICATION_JSON_VALUE },
            headers = "Accept=application/json")
    public List<MainBoard> listAll() {
        return mainBoardRepository.findAll();
    }

     /** need specification while retrieving the whole list**/
    @GetMapping(value = "/list")
    @ResponseBody
    public ResponseEntity<BoardReturnParam> getList() {
//        Gson gson = new Gson;
//        String json = gson.toJson(mainBoardService.getNotDeletedList());
        return mainBoardService.getNotDeletedList();
    }

    @GetMapping(value = "/list/test")
        public List<MainBoard> test() {
        return mainBoardRepository.findAllByParentIsNullAndDeletedIsFalse();
    }

//    @PostMapping(value = "list/sample")
//    public void sampleTest(@RequestBody MainBoard sample) {
//        Log.info(" sample param : {}", sample.toString());
//        mainBoardRepository.save(sample);
//    }

    //읽기
    @GetMapping(value = "/read/{boardId}")
    public ResponseEntity<BoardReturnParam> findBoard(@PathVariable Long boardId) {
        log.error("BOARD ID IS" + boardId);
//        if (assertExists(boardId)) {
//            log.error("exists and mapped");
        return mainBoardService.readBoard(boardId);
//        } else {
//            log.error("no board of such board id exists");
//            return null;
//        }
    }

//    @PostMapping(value = "/read/reply")
//    public ResponseEntity<BoardReturnParam> addNewReply(@RequestBody BoardReceiverParam boardReceiverParam) throws InvalidInputException {
//        return mainBoardService.
//
//    }


    @PostMapping(value = "/read/reply")
    public ResponseEntity<BoardReturnParam> addNewReply(@RequestBody BoardReceiverParam boardReceiverParam) throws InvalidInputException {
        return mainBoardService.addNewReply(boardReceiverParam);
    }



    //게시글 등록
    // send Serial board_id at the creation stage //
    @PostMapping(value = "/post/save")
    public ResponseEntity<BoardReturnParam> addNewPostBoard(@RequestBody BoardReceiverParam newPost) throws InvalidInputException {
        log.info(" sample param : {}", newPost.toString());
        return mainBoardService.addNewPostBoard(newPost);
    }

    //게시물 수정
    @PostMapping(value= "/read/modify")
    public ResponseEntity<BoardReturnParam> modifyBoard(@RequestBody BoardReceiverParam newPost) throws InvalidInputException {
        MainBoard targetBoard = mainBoardRepository.findMainBoardByBoardId(newPost.getBoardId());
        targetBoard.setTitle(newPost.getTitle());
        targetBoard.setPost(newPost.getPost());
        targetBoard.setUpdatedAt(new Date());
        mainBoardRepository.save(targetBoard);
        return new ResponseEntity<>(BoardReturnParam.builder()
                .success(true)
                .boardId(targetBoard.getBoardId())
                .build(), HttpStatus.OK);
    }

    //게시물 삭제
    @GetMapping(value= "/read/delete/{boardId}")
    public ResponseEntity<BoardReturnParam> deleteBoard(@PathVariable Long boardId) throws Exception {
        try {
            MainBoard target = mainBoardRepository.findMainBoardByBoardId(boardId);
            target.setDeleted(true);
            mainBoardRepository.saveAndFlush(target);

            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(true)
                    .deleted(target.isDeleted())
                    .build(), HttpStatus.OK);
        } catch (Exception E) {
            throw new Exception("no board of such id has been detected");
        }
    }


    @GetMapping(value = "/list/check/{boardId}")
    public boolean assertExists(@PathVariable Long boardId) {
        if (mainBoardService.assertExists(boardId)) {
            return true;
        };
        return false;
    }
    //    @PostMapping(value = "/post/reply/boardId/parent")
//    public ReplyBoard addReplyToBoard(@RequestBody Long boardId,  String reply_text,
//                                      @ Long parent, @RequestBody boolean admin_reply, @RequestBody boolean user_reply) {
//        return replyBoardService.addReplyBoard(boardId, reply_text, parent, admin_reply, user_reply);
//    }

    @GetMapping(value = "/list/undelete/{boardId}")
    public void undoDelete(@PathVariable Long boardId) {
        MainBoard target = mainBoardRepository.findMainBoardByBoardId(boardId);
        if (target.isDeleted()) {
            target.setDeleted(false);
        }
        mainBoardRepository.saveAndFlush(target);
    }

//    @PostMapping(value = "/post/reply")
//    public void addAdminNewReply(@RequestBody ReplyBoard replyBoard) {
//    }
}
