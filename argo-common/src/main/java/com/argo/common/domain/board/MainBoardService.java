package com.argo.common.domain.board;

import com.argo.common.configuration.ArgoBizException;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeTypeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springfox.documentation.spring.web.json.Json;

import javax.xml.ws.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MainBoardService {

    @Autowired
    private MainBoardRepository mainBoardRepository;


    /** retrives all undeleted list **/
    public ResponseEntity<BoardReturnParam> getNotDeletedList() {
        List<MainBoardShorten> shortenList = listForList(mainBoardRepository.findAllByParentIsNullAndDeletedIsFalseOrderByCreatedAtDesc());
        System.out.println(shortenList);
        return new ResponseEntity<>(BoardReturnParam.builder()
                .success(true)
                .rowData(shortenList)
                .build(), HttpStatus.OK);
    }


    public List<MainBoardShorten> listForList(List<MainBoard> originalList) {
        return originalList.stream().map(MainBoardShorten::from).collect(Collectors.toList());
    }

    public List<MainBoardReply> listForReplyList(List<MainBoard> originalList) {
        return originalList.stream().map(MainBoardReply::from).collect(Collectors.toList());
    }

    public ResponseEntity<BoardReturnParam> readBoard(Long boardId) {
        MainBoard selectedBoard = mainBoardRepository.findMainBoardByBoardId(boardId);
        List<MainBoardReply> replyList = listForReplyList(mainBoardRepository.findAllByDeletedIsFalseAndParentEqualsOrderByCreatedAt(selectedBoard.getBoardId()));
        System.out.println(replyList);
        return new ResponseEntity<>(BoardReturnParam.builder()
                .success(true)
                .boardId(selectedBoard.getBoardId())
                .userEmail(selectedBoard.getUserEmail())
                .title(selectedBoard.getTitle())
                .post(selectedBoard.getPost())
                .replies(replyList)
                .build()
                , HttpStatus.OK);
    }



    /** can't understand why ResponseEntity<BoardReturnParam does not work **/
    @Transactional(readOnly = false)
    public ResponseEntity<BoardReturnParam> addNewPostBoard(BoardReceiverParam boardParam) throws InvalidInputException {
        if (boardParam.getPost().length() > 9999 || boardParam.getTitle().length() > 300) {
            throw new InvalidInputException("제목 또는 글 숫자가 너무 깁니다");
        }
        MainBoard newPost = BoardParamToMainBoard(boardParam);
        mainBoardRepository.save(newPost);
        return new ResponseEntity<>(BoardReturnParam.builder()
                .success(true)
                .boardId(newPost.getBoardId())
                .userEmail(boardParam.getUserEmail())
                .title(boardParam.getTitle())
                .post(newPost.getPost())
                .build(), HttpStatus.OK);
    }


    public MainBoard BoardParamToMainBoard(BoardReceiverParam boardParam) {
//        String post = boardParam.getPost().replace("<p>", "");
//        post = post.replace("</p>", "");
        MainBoard newBoard = new MainBoard();
        newBoard.setUserEmail(boardParam.getUserEmail());
        newBoard.setTitle(boardParam.getTitle());
        newBoard.setPost(boardParam.getPost());
        return newBoard;
    }

    public MainBoard BoardParamToReply(BoardReceiverParam boardParam) {
        MainBoard newBoard = new MainBoard();
        newBoard.setUserEmail(boardParam.getUserEmail());
        newBoard.setTitle("답글");
        newBoard.setPost(boardParam.getPost());
        newBoard.setParent(boardParam.getParent());
        return newBoard;
    }

    @Transactional(readOnly = false)
    public ResponseEntity<BoardReturnParam> addNewReply(BoardReceiverParam boardParam) throws InvalidInputException {
        try {
            Long parent = boardParam.getParent();
            MainBoard selectedBoard = mainBoardRepository.findMainBoardByBoardId(parent);
            MainBoard newReply = BoardParamToReply(boardParam);
            mainBoardRepository.save(newReply);
            List<MainBoard> list = mainBoardRepository.findAllByDeletedIsFalseAndParentEqualsOrderByCreatedAt(selectedBoard.getBoardId());
            List<MainBoardReply> replyList = listForReplyList(list);
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(true)
                    .boardId(parent)
                    .userEmail(boardParam.getUserEmail())
                    .title(boardParam.getTitle())
                    .post(selectedBoard.getPost())
                    .reply(boardParam.getPost())
                    .replies(replyList)
                    .build(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(false)
                    .message(e.getMessage())
                    .build(), HttpStatus.OK);
        }
    }

    @Transactional(readOnly = false)
    public ResponseEntity<BoardReturnParam> modify(BoardReceiverParam newPost) {
        MainBoard targetBoard = mainBoardRepository.findMainBoardByBoardId(newPost.getBoardId());
        targetBoard.setTitle(newPost.getTitle());
        targetBoard.setPost(newPost.getPost());
        targetBoard.setUserEmail(newPost.getUserEmail());
        targetBoard.setUpdatedAt(new Date());
        mainBoardRepository.save(targetBoard);
        return new ResponseEntity<>(BoardReturnParam.builder()
                .success(true)
                .boardId(targetBoard.getBoardId())
                .build(), HttpStatus.OK);
    }

    @Transactional(readOnly = false)
    public ResponseEntity<BoardReturnParam> delete(Long boardId) throws ArgoBizException {
        try {
            MainBoard target = mainBoardRepository.findMainBoardByBoardId(boardId);
            target.setDeleted(true);
            mainBoardRepository.save(target);
            return new ResponseEntity<>(BoardReturnParam.builder()
                    .success(true)
                    .deleted(target.isDeleted())
                    .build(), HttpStatus.OK);
        } catch (RuntimeException E) {
            throw new RuntimeException("no board of such id has been detected");
        }
    }
}
