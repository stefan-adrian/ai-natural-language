package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.model.Node;
import fii.ai.natural.language.model.metadata.PieceColorMetadata;
import fii.ai.natural.language.model.metadata.PieceNameMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Here I tried to create a structure for the service that gets the input(mapped to model) from the controller and
 * decorates the model with metadata and comments, in the scope to return a list of comments to the controller.
 */
@Service
public class GameService {

    private CommentService commentService;
    private List<MoveComment> moveCommentList;

    @Autowired
    public GameService(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * This should be called from the controller and decorate the moves tree first with metadata and
     * after that should generate a list of comments for the chess game(first should decorate the moves
     * three with comments individually for every move and after that should put them together)
     *
     * @param movesTree represents the input from AI module mapped to MovesTree object
     * @return the return value gonna be changed in a list of comments
     */

    public List<MoveComment> commentMovesTree(MovesTree movesTree) {
        decorateMovesTree(movesTree);
        commentService.commentMovesTree(movesTree);
        //TODO call method that that puts comments from the moves tree together is a pretty format.
        //parcurg arborele si returnez o lista cu numarul mutarii si comentariile
        return concatentateComments(movesTree);
    }

    private void decorateMovesTree(MovesTree movesTree) {
        for (int index = 0; index < movesTree.getMainVariant().getMoves().size(); index++) {
            MovesTree partialMovesTree = getMovesTreeUpToGivenIndex(movesTree, index);
            // TODO create and call method that does the decoration of the partialMovesTree with metadata(generate metadata for sub-problem)
        }
    }

    private MovesTree getMovesTreeUpToGivenIndex(MovesTree movesTree, int index) {
        MovesTree partialMovesTree = new MovesTree();
        partialMovesTree.setMainVariant(new MoveVariant());
        partialMovesTree.setInitialStateFEN(movesTree.getInitialStateFEN());
        partialMovesTree.getMainVariant().setMoves(new ArrayList<>(movesTree.getMainVariant().getMoves()));
        partialMovesTree.getMainVariant().getMoves().subList(index + 1, partialMovesTree.getMainVariant().getMoves().size()).clear();
        return partialMovesTree;
    }

    private List<MoveComment> concatentateComments(MovesTree movesTree) {
        moveCommentList = new ArrayList<>();
        for (int index = 0; index < movesTree.getMainVariant().getMoves().size(); index++) {
            moveCommentList.add(new MoveComment(index + 1, movesTree.getMainVariant().getMoves().get(index).getComments()));
        }
        return moveCommentList;
    }

}
