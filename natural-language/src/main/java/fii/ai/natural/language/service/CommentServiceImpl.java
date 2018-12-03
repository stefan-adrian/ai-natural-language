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
        MoveMetadata moveMetadata = mapMetadataListToMoveMetadata(move.getMetadata());
        decorateWithBasicMoveDescriptionComment(moveMetadata, move);
        decorateWithImpactOnGameComment(moveMetadata, move);
        decorateWithCommentIfPieceWasTaken(moveMetadata, move);
        decorateWithCastlingPossibilityComment(movesTree, indexOfMove, moveMetadata, move);
        decorateWithIfCheckComment(moveMetadata, move);
        decorateWithIfPossibleEnPassantComment(moveMetadata, move);
        decorateWithGameStateComment(moveMetadata, move);
    }

    private void decorateWithBasicMoveDescriptionComment(MoveMetadata moveMetdata, Node move) {
        String comment = moveMetdata.getColor() + " moves " + moveMetdata.getName() + " from " + move.getMove().charAt(1)
                + move.getMove().charAt(2) + " to " + move.getMove().charAt(3) + move.getMove().charAt(4);
        move.getComments().add(comment);
    }

    private void decorateWithImpactOnGameComment(MoveMetadata moveMetadata, Node move) {
        int moveGrade = moveMetadata.getMoveGrade();
        String impact = "";
        switch (moveGrade) {
            case -3:
                impact = " massive disadvantage ";
                break;
            case -2:
                impact = " big disadvantage ";
                break;
            case -1:
                impact = " slightly disadvantage ";
                break;
            case 0:
                impact = " no apparent impact on the game ";
                break;
            case 1:
                impact = " slightly advantage ";
                break;
            case 2:
                impact = " big advantage ";
                break;
            case 3:
                impact = " massive advantage ";
                break;
        }
        String comment = "";
        if (moveGrade == 0)
            comment = "The move caused" + impact;
        else
            comment = "The move gave" + impact + "for " + moveMetadata.getColor() + " side.";
        move.getComments().add(comment);
    }

    private void decorateWithCommentIfPieceWasTaken(MoveMetadata moveMetadata, Node move) {
        if (moveMetadata.getPieceTaken() != null) {
            String comment = moveMetadata.getColor() + " has captured " + moveMetadata.getPieceTaken();
            move.getComments().add(comment);
        }
    }

    private void decorateWithCastlingPossibilityComment(MovesTree movesTree, int indexOfMove, MoveMetadata moveMetadata, Node move) {
        if (indexOfMove - 2 >= 0) {
            Node precedentMove = movesTree.getMainVariant().getMoves().get(indexOfMove - 2);
            MoveMetadata precedentMoveMetadata = mapMetadataListToMoveMetadata(precedentMove.getMetadata());
            String currentCastlingState = moveMetadata.getCastlingState();
            String castlingStateComment = null;
            if (currentCastlingState == null && precedentMoveMetadata.getCastlingState() != null) {
                castlingStateComment = moveMetadata.getColor() + " can't do castling any more on either side.";
            } else {
                if (currentCastlingState != null && !currentCastlingState.equals(precedentMoveMetadata.getCastlingState())) {
                    switch (currentCastlingState) {
                        case "kq":
                            castlingStateComment = moveMetadata.getColor() + " can do castling on both sides (of the king and of the queen)";
                            break;
                        case "q":
                            castlingStateComment = moveMetadata.getColor() + " can do castling only on the side of the queen after this move";
                            break;
                        case "k":
                            castlingStateComment = moveMetadata.getColor() + " can do castling only on the side of the king after this move";
                            break;
                    }
                }
            }
            if (castlingStateComment != null) {
                move.getComments().add(castlingStateComment);
            }
        }
    }

    private void decorateWithIfPossibleEnPassantComment(MoveMetadata moveMetadata, Node move) {
        boolean possible = moveMetadata.getEnPassantPossible();
        String comment = null;
        if (possible) {
            comment = moveMetadata.getColor() + " side can do en-passant move. ";
        }
        if (comment != null) {
            move.getComments().add(comment);
        }
    }

    private void decorateWithIfCheckComment(MoveMetadata moveMetadata, Node move) {
        List<String> checkPieces = moveMetadata.getCheckPieces();
        String comment = null;
        if (checkPieces != null) {
            comment = moveMetadata.getColor() + " side checked the opponent from the piece(s): ";
            for (String i : checkPieces) {
                comment += i;
                comment += "; ";
            }
        }
        if (comment != null) {
            move.getComments().add(comment);
        }
    }

    private void decorateWithGameStateComment(MoveMetadata moveMetadata, Node move) {
        String state = moveMetadata.getState();
        String comment = null;
        if (state != null) {
            if (state.equals("equal")) {
                comment = "Game finished as a draw.";
            }
            if (state.equals("checkmate")) {
                comment = moveMetadata.getColor() + " side won the game.";
            }
        }
        if (comment != null) {
            move.getComments().add(comment);
        }
    }

    private MoveMetadata mapMetadataListToMoveMetadata(List<Metadata> metadatas) {
        MoveMetadata moveMetadata = new MoveMetadata();
        for (Metadata metadata : metadatas) {
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
                    moveMetadata.setName(pieceNameMetadata.getChessPieceName());
                    break;
                }
                case "PieceTaken": {
                    PieceTakenMetadata pieceTakenMetadata = (PieceTakenMetadata) metadata;
                    moveMetadata.setPieceTaken(pieceTakenMetadata.getPieceTaken());
                    break;
                }
                case "EnPassant": {
                    EnPassantMetadata enPassantMetadata = (EnPassantMetadata) metadata;
                    moveMetadata.setEnPassantPossible(enPassantMetadata.getEnPassantPossible());
                    break;
                }
                case "GameState": {
                    GameStateMetadata gameStateMetadata = (GameStateMetadata) metadata;
                    moveMetadata.setState(gameStateMetadata.getState());
                    break;
                }
                case "Check": {
                    CheckMetadata checkMetadata = (CheckMetadata) metadata;
                    moveMetadata.setCheckPieces(checkMetadata.getCheckPieces());
                    break;
                }


            }
        }
        return moveMetadata;
    }
}
