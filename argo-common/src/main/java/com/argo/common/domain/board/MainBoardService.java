package com.argo.common.domain.board;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class MainBoardService {

    private MainBoardRepository mainBoardRepository;

    public MainBoard getBoardById(Long boardNo) {
        return mainBoardRepository.findMainBoardByBoardNumber(boardNo);
    }

    public void addNewBoard(MainBoard newBoard) {
        mainBoardRepository.save(newBoard);
    }

}
