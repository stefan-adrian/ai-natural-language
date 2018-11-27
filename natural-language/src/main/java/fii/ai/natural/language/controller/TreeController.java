package fii.ai.natural.language.controller;

import fii.ai.natural.language.input.InputTree;
import fii.ai.natural.language.mapper.TreeMapper;
import fii.ai.natural.language.model.MovesTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TreeController {
    public TreeMapper treeMapper;

    @Autowired
    public TreeController(TreeMapper treeMapper) {
        this.treeMapper = treeMapper;
    }

    @PostMapping("/game")
    public MovesTree commandGame(@RequestBody InputTree inputTree) {
        MovesTree movesTree = treeMapper.map(inputTree);
        return movesTree;
    }
}
