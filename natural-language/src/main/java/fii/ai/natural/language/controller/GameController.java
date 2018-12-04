package fii.ai.natural.language.controller;

import fii.ai.natural.language.input.InputTree;
import fii.ai.natural.language.mapper.PositionMapper;
import fii.ai.natural.language.mapper.TreeMapper;
import fii.ai.natural.language.model.MoveComment;
import fii.ai.natural.language.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GameController {
    private TreeMapper treeMapper;
    private GameService gameService;
    private PositionMapper positionMapper;

    @Autowired
    public GameController(TreeMapper treeMapper, GameService gameService) {
        this.treeMapper = treeMapper;
        this.gameService = gameService;
    }

    /**
     * This method maps the inputTree to a MovesTree and after gonna call(when implemented) commentMovesTree method from gameService
     * to generate comments for the inputTree
     *
     * @param inputTree represents the input that is a complete chess game
     * @return the return value gonna be changed to a List of comments
     */
    @PostMapping("/game")
    public List<MoveComment> game(@RequestBody InputTree inputTree) {
        return gameService.commentMovesTree(treeMapper.map(inputTree));
    }

    @PostMapping("/optimal-moves")
    public List<MoveComment> optimalMoves(@RequestBody InputTree inputTree) {
        return gameService.commentOptimalMoves(positionMapper.map(inputTree));
    }
}
