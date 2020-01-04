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
        return mainBoardService.getNotDeletedList();
    }

    //읽기
    @GetMapping(value = "/read/{boardId}")
    public ResponseEntity<BoardReturnParam> findBoard(@PathVariable Long boardId) throws RuntimeException {
        return mainBoardService.readBoard(boardId);
    }



    @PostMapping(value = "/read/reply")
    public ResponseEntity<BoardReturnParam> addNewReply(@RequestBody BoardReceiverParam boardReceiverParam) throws InvalidInputException {
        return mainBoardService.addNewReply(boardReceiverParam);
    }

    //게시글 등록
    @PostMapping(value = "/post/save")
    public ResponseEntity<BoardReturnParam> addNewPostBoard(@RequestBody BoardReceiverParam newPost) throws InvalidInputException {
        log.info(" sample param : {}", newPost.toString());
        return mainBoardService.addNewPostBoard(newPost);
    }

    //게시물 수정
    @PostMapping(value= "/read/modify")
    public ResponseEntity<BoardReturnParam> modifyBoard(@RequestBody BoardReceiverParam newPost) throws InvalidInputException {
        return mainBoardService.modify(newPost);
    }

    //게시물 삭제
    @GetMapping(value= "/read/delete/{boardId}")
    public ResponseEntity<BoardReturnParam> deleteBoard(@PathVariable Long boardId) throws Exception {
        System.out.println(boardId);
        return mainBoardService.delete(boardId);
    }
}
