package fii.ai.natural.language.service;

import fii.ai.natural.language.model.*;
import fii.ai.natural.language.utils.ScoreInfo;
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

    private MetadataService metadataService;
    private CommentMoveService commentMoveService;
    private List<MoveComment> moveCommentList;
    private ScoreService scoreService;
    private CommentVariantService commentVariantService;

    @Autowired
    public GameService(MetadataService metadataService, CommentMoveService commentMoveService, ScoreService scoreService, CommentVariantService commentVariantService) {
        this.metadataService = metadataService;
        this.commentMoveService = commentMoveService;
        this.scoreService = scoreService;
        this.commentVariantService = commentVariantService;
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

        metadataService.decorateWithMetadata(movesTree);
        commentMoveService.commentMovesTree(movesTree);
        return concatenateComments(movesTree);
    }

    private List<MoveComment> concatenateComments(MovesTree movesTree) {
        moveCommentList = new ArrayList<>();
        for (int index = 0; index < movesTree.getMainVariant().getMoves().size(); index++) {
            if (movesTree.getMainVariant().getMoves().get(index).getComments().size() != 0) {
                moveCommentList.add(new MoveComment(index + 1, movesTree.getMainVariant().getMoves().get(index).getComments(), getEvaluationForAMove(movesTree.getMainVariant().getMoves().get(index).getScore())));
            }
        }
        return moveCommentList;
    }

    private String getEvaluationForAMove(double score) {
        if (score < -1 * ScoreInfo.getMediumImpactMove())
            return "??";
        if (score >= -1 * ScoreInfo.getMediumImpactMove() && score < -1 * ScoreInfo.getSmallImpactMove())
            return "?";
        if (score >= -1 * ScoreInfo.getSmallImpactMove() && score < 0)
            return "?!";
        if (score >= 0 && score <= ScoreInfo.getSmallImpactMove())
            return "!?";
        if (score > ScoreInfo.getSmallImpactMove() && score <= ScoreInfo.getMediumImpactMove())
            return "!";
        if (score >= ScoreInfo.getMediumImpactMove())
            return "!!";
        return null;
    }

    public List<OptimalMove> commentOptimalMoves(MovePosition movePosition) {
        List<OptimalMove> optimalMoves = new ArrayList<>();
        metadataService.decorateWithMetadataOptimalMoves(movePosition);
        List<MoveVariant> bestVariants = scoreService.getMoveVariantsByScore(movePosition.getVariants(), null, 0, 0);

        for (MoveVariant moveVariant : bestVariants) {
            commentVariantService.commentMoveVariant(moveVariant);
            OptimalMove optimalMove = new OptimalMove();
            optimalMove.setMovement(moveVariant.getMoves().get(0).getMove());
            optimalMove.setComments(moveVariant.getComments());
            optimalMoves.add(optimalMove);
        }
        return optimalMoves;
    }

}
