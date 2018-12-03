package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MoveComment;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.model.metadata.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Here I tried to create a structure for the service that gets the input(mapped to model) from the controller and
 * decorates the model with metadata and comments, in the scope to return a list of comments to the controller.
 */
@Service
public class GameService {

    private MetadataService metadataService;
    private CommentService commentService;
    private List<MoveComment> moveCommentList;

    @Autowired
    public GameService(CommentService commentService, MetadataService metadataService) {
        this.commentService = commentService;
        this.metadataService = metadataService;
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

        //This commented code is only yo check to test comment functionality until the real decoration with metadata is made
        /*movesTree.getMainVariant().getMoves().get(0).getMetadata().add(new PieceColorMetadata("White"));
        movesTree.getMainVariant().getMoves().get(0).getMetadata().add(new PieceNameMetadata("Queen"));
        movesTree.getMainVariant().getMoves().get(0).getMetadata().add(new PieceTakenMetadata("Queen"));
        movesTree.getMainVariant().getMoves().get(0).getMetadata().add(new EnPassantMetadata(true));
        movesTree.getMainVariant().getMoves().get(0).getMetadata().add(new CheckMetadata(Arrays.asList("Queen")));
        movesTree.getMainVariant().getMoves().get(0).getMetadata().add(new MoveGradeMetadata(2));
        movesTree.getMainVariant().getMoves().get(0).getMetadata().add(new CastlingStateMetadata("kq"));

        movesTree.getMainVariant().getMoves().get(1).getMetadata().add(new PieceColorMetadata("Black"));
        movesTree.getMainVariant().getMoves().get(1).getMetadata().add(new PieceNameMetadata("King"));
        movesTree.getMainVariant().getMoves().get(1).getMetadata().add(new MoveGradeMetadata(-2));

        movesTree.getMainVariant().getMoves().get(2).getMetadata().add(new PieceColorMetadata("White"));
        movesTree.getMainVariant().getMoves().get(2).getMetadata().add(new PieceNameMetadata("Queen"));
        movesTree.getMainVariant().getMoves().get(2).getMetadata().add(new CheckMetadata(Arrays.asList("Queen")));
        movesTree.getMainVariant().getMoves().get(2).getMetadata().add(new MoveGradeMetadata(3));
        movesTree.getMainVariant().getMoves().get(2).getMetadata().add(new CastlingStateMetadata("q"));
        movesTree.getMainVariant().getMoves().get(2).getMetadata().add(new GameStateMetadata("checkmate"));*/


        metadataService.decorateWithMetadata(movesTree);
        commentService.commentMovesTree(movesTree);
        return concatentateComments(movesTree);
    }

    private List<MoveComment> concatentateComments(MovesTree movesTree) {
        moveCommentList = new ArrayList<>();
        for (int index = 0; index < movesTree.getMainVariant().getMoves().size(); index++) {
            moveCommentList.add(new MoveComment(index + 1, movesTree.getMainVariant().getMoves().get(index).getComments()));
        }
        return moveCommentList;
    }
}
