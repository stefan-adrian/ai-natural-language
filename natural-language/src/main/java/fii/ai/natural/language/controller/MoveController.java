package fii.ai.natural.language.controller;

import fii.ai.natural.language.model.Move;
import fii.ai.natural.language.service.MoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moves")
public class MoveController {

    private MoveService moveService;

    @Autowired
    public MoveController(MoveService moveService) {
        this.moveService = moveService;
    }

    @GetMapping
    public List<Move> getAll() {
        return moveService.getAll();
    }

    @GetMapping("/{id}")
    public Move getById(@PathVariable Long id){
        return moveService.getById(id);
    }

    @PostMapping
    public Move add(@RequestBody Move move){
        return moveService.save(move);
    }
}
