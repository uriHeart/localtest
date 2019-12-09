package com.argo.common.domain.board;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import lombok.AllArgsConstructor;
import org.eclipse.jdt.core.compiler.InvalidInputException;
import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MimeTypeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.xml.ws.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MainBoardService {

    @Autowired
    private MainBoardRepository mainBoardRepository;


    /** retrives all undeleted list **/
    public ResponseEntity<BoardReturnParam> getNotDeletedList() {
//        List<List<Object>> target;
//        List<List<Object>> originalList = mainBoardRepository.findList();
//        JSONArray jsonArray = new JSONArray();
//        for (List<Object> list: originalList) {
//            JSONArray newArray = new JSONArray(list);
//            jsonArray.put(newArray);
//        }
        List<MainBoardShorten> shortenList = listForList(mainBoardRepository.findAllByParentIsNullAndDeletedIsFalseOrderByCreatedAt());
        return new ResponseEntity<>(BoardReturnParam.builder()
                .success(true)
                .rowData(shortenList).build(), HttpStatus.OK);
    }


    public static List<MainBoardShorten> listForList(List<MainBoard> originalList) {
        ArrayList<MainBoardShorten> shortenList = new ArrayList<>();
        for (MainBoard mainboard : originalList) {
            if (mainboard.isDeleted()) {
                continue;
            };
            MainBoardShorten shorten = new MainBoardShorten();
            shorten.boardId = mainboard.getBoardId();
            shorten.user_email = mainboard.getUserEmail();
            shorten.title = mainboard.getTitle();
            shorten.createdAt = mainboard.getCreatedAt();
            shortenList.add(shorten);
        }
        return shortenList;
    }


    public ResponseEntity<BoardReturnParam> readBoard(Long boardId) {
        MainBoard selectedBoard = mainBoardRepository.findMainBoardByBoardId(boardId);
        List<MainBoard> listOfReplies = mainBoardRepository.findAllByDeletedIsFalseAndParentEqualsOrderByCreatedAt(selectedBoard.getBoardId());
        List<String> listRefined = new ArrayList<>();
        for (MainBoard mainboard: listOfReplies) {
            listRefined.add(mainboard.getPost());
        }
        return new ResponseEntity<>(BoardReturnParam.builder()
                .success(true)
                .boardId(selectedBoard.getBoardId())
                .user_email(selectedBoard.getUserEmail())
                .title(selectedBoard.getTitle())
                .post(selectedBoard.getPost())
                .replies(listRefined)
                .build()
                , HttpStatus.OK);
    }



    /** can't understand why ResponseEntity<BoardReturnParam does not work **/
    public ResponseEntity<BoardReturnParam> addNewPostBoard(BoardReceiverParam boardParam) throws InvalidInputException {
        if (boardParam.getPost().length() > 9999 || boardParam.getTitle().length() > 300) {
           throw new InvalidInputException();
       }
        try {
            //need exceptionHandlingmethod
            MainBoard newPost = BoardParamToMainBoard(boardParam);
            mainBoardRepository.save(newPost);
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(true)
                    .boardId(newPost.getBoardId())
                    .user_email(boardParam.getUser_email())
                    .title(boardParam.getTitle())
                    .post(newPost.getPost())
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build(), HttpStatus.OK);
        }
    }


    public MainBoard BoardParamToMainBoard(BoardReceiverParam boardParam) {
        String post = boardParam.getPost().replace("<p>", "");
        post = post.replace("</p>", "");
        MainBoard newBoard = new MainBoard();
        newBoard.setUserEmail(boardParam.getUser_email());
        newBoard.setTitle(boardParam.getTitle());
        newBoard.setPost(post);
        return newBoard;
    }

    public MainBoard BoardParamToReply(BoardReceiverParam boardParam) {
        MainBoard newBoard = new MainBoard();
        newBoard.setUserEmail(boardParam.getUser_email());
        newBoard.setTitle("답글");
        newBoard.setPost(boardParam.getPost());
        newBoard.setParent(boardParam.getParent());
        //if admin_reply setadminreply(true). not, false)
        return newBoard;
    }

    // board id sequence 에 적용 절대 안되야함. Admin인지 아닌지 구분해야함
    public ResponseEntity<BoardReturnParam> addNewReply(BoardReceiverParam boardParam) throws InvalidInputException {
        try {
            //need exceptionHandlingmethod
            Long parent = boardParam.getParent();
            MainBoard newReply = BoardParamToReply(boardParam);
            mainBoardRepository.save(newReply);
            newReply.setBoardId(0L);
            newReply.setParent(boardParam.getBoardId());
            List<MainBoard> listOfReplies = mainBoardRepository.findAllByParentEquals(parent);
            List<String> listRefined = new ArrayList<>();
            for (MainBoard mainboard: listOfReplies) {
                listRefined.add(mainboard.getPost());
            }
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(true)
                    .boardId(parent)
                    .user_email(boardParam.getUser_email())
                    .title(boardParam.getTitle())
                    .post(boardParam.getPost())
                    .reply(boardParam.getPost())
                    .replies(listRefined)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build(), HttpStatus.OK);
        }
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
