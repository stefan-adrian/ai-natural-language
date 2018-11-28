package fii.ai.natural.language.controller;

import fii.ai.natural.language.input.InputTree;
import fii.ai.natural.language.mapper.TreeMapper;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    private TreeMapper treeMapper;
    private GameService gameService;

    @Autowired
    public GameController(TreeMapper treeMapper, GameService gameService) {
        this.treeMapper = treeMapper;
        this.gameService = gameService;
    }

    /**
     * This method maps the inputTree to a MovesTree and after gonna call(when implemented) commentMovesTree method from gameService
     * to generate comments for the inputTree
     * @param inputTree represents the input that is a complete chess game
     * @return the return value gonna be changed to a List of comments
     */
    @PostMapping("/game")
    public MovesTree commentGame(@RequestBody InputTree inputTree) {
        return treeMapper.map(inputTree);
    }

    /**
     * This is a method that I did only for test in case somebody wants to test the comment function.
     * But until the method that decorates the tree with metadata is made the metadata values need to be set manually.
     */
    @PostMapping("/test")
    public MovesTree test(@RequestBody InputTree inputTree) {
        return gameService.commentMovesTree(treeMapper.map(inputTree));
    }
}
