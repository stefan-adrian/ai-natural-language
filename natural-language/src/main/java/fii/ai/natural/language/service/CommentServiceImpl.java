package fii.ai.natural.language.service;

import fii.ai.natural.language.model.Metadata;
import fii.ai.natural.language.model.MoveMetadata;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.model.Node;
import fii.ai.natural.language.model.metadata.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Override
    public MovesTree commentMovesTree(MovesTree movesTree) {
        for (int index = 0; index < movesTree.getMainVariant().getMoves().size(); index++) {
            commentMove(movesTree, index);
        }
        return movesTree;
    }

    /**
     * This function generates comments for the move for movesTree at the index given
     * The way I thought this method is that will call many more methods that will generate a comment(and add it to comment list)
     * similar to the way that decorateWithBasicMoveDescriptionComment does.
     * The decorateWithBasicMoveDescriptionComment method is pretty simple but other methods will need to do more complicated things:
     * Ex1: a method that checks if a piece has been taken in this move, if the pieceTaken metadata is null then it will do nothing otherwise will generate comment
     * Ex2: a method that checks if this move caused the castling possibility(this method will need to check with the previous move castling state to generate comment if needed)
     */
    private void commentMove(MovesTree movesTree, int indexOfMove) {
        Node move = movesTree.getMainVariant().getMoves().get(indexOfMove);
        MoveMetadata moveMetadata=mapMetadataListToMoveMetadata(move.getMetadata());
        decorateWithBasicMoveDescriptionComment(moveMetadata,move);
    }

    private void decorateWithBasicMoveDescriptionComment(MoveMetadata moveMetdata,Node move) {
        String comment = moveMetdata.getColor() + " moves " + moveMetdata.getName() + " from " + move.getMove().charAt(1)
                + move.getMove().charAt(2) + " to " + move.getMove().charAt(3) + move.getMove().charAt(4);
        move.getComments().add(comment);
    }

    private MoveMetadata mapMetadataListToMoveMetadata(List<Metadata> metadatas){
        MoveMetadata moveMetadata=new MoveMetadata();
        for(Metadata metadata:metadatas){
            switch (metadata.getKey()) {
                case "CastlingState": {
                    CastlingStateMetadata castlingStateMetadata = (CastlingStateMetadata) metadata;
                    moveMetadata.setCastlingState(castlingStateMetadata.getCastlingState());
                    break;
                }
                case "MoveGrade": {
                    MoveGradeMetadata moveGradeMetadata = (MoveGradeMetadata) metadata;
                    moveMetadata.setMoveGrade(moveGradeMetadata.getMoveGrade());
                    break;
                }
                case "PieceColor": {
                    PieceColorMetadata pieceColorMetadata = (PieceColorMetadata) metadata;
                    moveMetadata.setColor(pieceColorMetadata.getColor());
                    break;
                }
                case "PieceName": {
                    PieceNameMetadata pieceNameMetadata = (PieceNameMetadata) metadata;
                    moveMetadata.setName(((PieceNameMetadata) metadata).getChessPieceName());
                    break;
                }
                case "PieceTaken": {
                    PieceTakenMetadata pieceTakenMetadata = (PieceTakenMetadata) metadata;
                    moveMetadata.setPieceTaken(pieceTakenMetadata.getPieceTaken());
                    break;
                }
            }
        }
        return moveMetadata;
    }
}