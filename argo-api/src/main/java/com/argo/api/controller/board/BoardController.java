package com.argo.api.controller.board;


import com.argo.common.domain.board.MainBoard;
import com.argo.common.domain.board.MainBoardRepository;
import com.argo.common.domain.board.MainBoardService;
import com.argo.common.domain.board.ReplyBoard;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.applet.Main;

@Slf4j
@RequestMapping(value = "/board")
@RestController
public class BoardController {

    @Autowired
    MainBoardRepository mainBoardRepository;
    @Autowired
    MainBoardService mainBoardService;

    @GetMapping(value = "/list/{boardNo}")
    public MainBoard findBoard(@PathVariable int boardNo) {
        return mainBoardService.getBoardById(boardNo);
    }

    @PostMapping(value = "/post")
    public void addNewBoard(@RequestBody MainBoard mainBoard) {
        mainBoardService.addNewBoard(mainBoard);
    }

//    @PostMapping(value = "/post/reply")
//    public void addAdminNewReply(@RequestBody ReplyBoard replyBoard) {
//    }

    @GetMapping(value = "/list/check/{boardNo}")
    public boolean assertExists(@PathVariable int boardNo) {
        if (mainBoardService.assertExists(boardNo)) {
            return true;
        };
        return false;
    }



}
