package com.argo.common.domain.board;

import com.argo.api.controller.board.BoardReceiverParam;
import com.argo.api.controller.board.BoardReturnParam;
import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MainBoardService {

    @Autowired
    private MainBoardRepository mainBoardRepository;


    /** retrives all undeleted list **/

    public List<List<Object>> getNotDeletedList() {
//        List<List<Object>> list = mainBoardRepository.findList();
//        JsonArray jsonArray = new JsonArray();
//        for (List<Object> list: listOfLists)
        return mainBoardRepository.findList();
    }


    public MainBoard getBoardById(Long boardId) {
        return mainBoardRepository.findMainBoardByBoardId(boardId);
    }


    /** can't understand why ResponseEntity<BoardReturnParam does not work **/
    public ResponseEntity<BoardReturnParam> addNewPostBoard(BoardReceiverParam boardParam) throws InvalidInputException {
//        if (boardParam.getPost().length() > 9999 || boardParam.getTitle().length() > 300) {
//            throw new InvalidInputException();
//        }
        try {
            //need exceptionHandlingmethod
            MainBoard newPost = BoardParamToMainBoard(boardParam);
            mainBoardRepository.save(newPost);
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(true)
                    .boardId(newPost.getBoardId())
                    .user_email(boardParam.getUser_email())
                    .title(boardParam.getTitle())
                    .post(boardParam.getPost())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build(), HttpStatus.OK);
        }
    }


    public MainBoard BoardParamToMainBoard(BoardReceiverParam boardParam) {
        MainBoard newBoard = new MainBoard();
        newBoard.setUserEmail(boardParam.getUser_email());
        newBoard.setTitle(boardParam.getTitle());
        newBoard.setPost(boardParam.getPost());
        return newBoard;

    }

    public boolean assertExists(Long boardId) {
        return mainBoardRepository.existsMainBoardByBoardId(boardId);
    }

    public void deleteBoardByBoardId(Long boardId) {
//        getBoardById(boardId).Delete(boardId);
        mainBoardRepository.findMainBoardByBoardId(boardId).setDeleted(true);
//        RESTART_AFTER = "ALTER SEQUENCE main_board_board_id_seq RESTART WITH" + boardId;
//        mainBoardRepository.resetBoardIdAfterDelete();
//        RESTART_AFTER = "";
    }



}
