package fii.ai.natural.language.controller;

import fii.ai.natural.language.input.InputTree;
import fii.ai.natural.language.mapper.TreeMapper;
import fii.ai.natural.language.model.MovesTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    public TreeMapper treeMapper;

    @Autowired
    public GameController(TreeMapper treeMapper) {
        this.treeMapper = treeMapper;
    }

    @PostMapping("/game")
    public MovesTree commentGame(@RequestBody InputTree inputTree) {
        return treeMapper.map(inputTree);
    }
}
