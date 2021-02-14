package jpa.project.controller;

import jpa.project.dto.board.BoardCreateRequestDto;
import jpa.project.dto.board.BoardDto;
import jpa.project.Service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;


    @GetMapping("/board/list")
    public String list(Model model){
        List<BoardDto> boards = boardService.findAll();

        model.addAttribute("boards",boards);
        return "board/list";
    }


    @GetMapping("/board/write")
    public String write(Model model){
        model.addAttribute("boardDto",new BoardDto());
        return "board/write";
    }
    @PostMapping("/board/write")
    public String write(@Valid BoardCreateRequestDto bcrDto , BindingResult result, Model model, Principal principal ){
        if(result.hasErrors()){
            model.addAttribute("boardDto",bcrDto);
            return "board/write";
        }

        boardService.save(principal.getName(),bcrDto);
        return "redirect:/board/list";

    }
    @GetMapping("/board/{id}/detail")
    public String detail(@PathVariable("id")Long id,Model model){
        BoardDto boardDto = boardService.find(id);
        BoardDto board = boardService.boardLikeCount(boardDto);
        model.addAttribute("board",board);


        return "board/detail";
    }

//    @DeleteMapping("/board/delete/{id}")
//    public String delete(@PathVariable("id")Long id){
//        boardService.delete(id);
//        return "redirect:/board/list";
//    }
    @GetMapping("/board/{id}/edit")
    public String update(@PathVariable("id")Long id,Model model){
        BoardDto boardDto = boardService.find(id);
        model.addAttribute("boardDto",boardDto);
        return "board/updateForm";
    }
//    @PutMapping("/post/edit/{id}")
//    public String update(@PathVariable("id")Long id, BoardCreateRequestDto bcrDto){
//        boardService.update(id,bcrDto);
//        return "redirect:/board/list";
//    }
    @PostMapping("/board/like")
    public String boardLike(BoardDto boardDto1, Principal principal,Model model){

       boardService.boardLike(boardDto1,principal.getName());
       BoardDto boardDto = boardService.boardLikeCount(boardDto1);
       model.addAttribute("board",boardDto);

       return "board/detail";

    }


}
