package fii.ai.natural.language.service;

import fii.ai.natural.language.mapper.MetadataMapper;
import fii.ai.natural.language.model.MoveMetadata;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentMoveServiceImpl implements CommentMoveService {

    private MetadataMapper metadataMapper;

    @Autowired
    public CommentMoveServiceImpl(MetadataMapper metadataMapper) {
        this.metadataMapper = metadataMapper;
    }

    @Override
    public MovesTree commentMovesTree(MovesTree movesTree) {
        for (int index = 0; index < movesTree.getMainVariant().getMoves().size(); index++) {
            commentMove(movesTree, index);
        }
        return movesTree;
    }


    private void commentMove(MovesTree movesTree, int indexOfMove) {
        Node move = movesTree.getMainVariant().getMoves().get(indexOfMove);
        MoveMetadata moveMetadata = metadataMapper.map(move.getMetadata());
        decorateWithImpactOnGameComment(moveMetadata, move);
        decorateWithCommentIfPieceWasTaken(moveMetadata, move);
        decorateWithCastlingPossibilityComment(movesTree, indexOfMove, moveMetadata, move);
        decorateWithIfCheckComment(moveMetadata, move);
        decorateWithIfPossibleEnPassantComment(moveMetadata, move);
        decorateWithGameStateComment(moveMetadata, move);
        decorateWithPromotionComment(moveMetadata, move);
        decorateWithEqualScopeComment(movesTree, indexOfMove, moveMetadata, move);
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
            MoveMetadata precedentMoveMetadata = metadataMapper.map(precedentMove.getMetadata());
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

    private void decorateWithPromotionComment(MoveMetadata moveMetadata, Node move) {
        if (moveMetadata.getPromoteToPiece() != null) {
            String comment = "The pawn was promoted to " + moveMetadata.getPromoteToPiece();
            move.getComments().add(comment);
        }
    }

    private void decorateWithEqualScopeComment(MovesTree movesTree, int indexOfMove, MoveMetadata moveMetadata, Node move) {
        if (indexOfMove - 2 >= 0) {
            Node precedentMove = movesTree.getMainVariant().getMoves().get(indexOfMove - 2);
            MoveMetadata precedentMoveMetadata = metadataMapper.map(precedentMove.getMetadata());
            if (moveMetadata.getEqualScope() != precedentMoveMetadata.getEqualScope()) {
                move.getComments().add("Because of the big disadvantage " + moveMetadata.getColor() + " started playing in scope of equal.");
            }
        }

    }
}
