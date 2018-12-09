package fii.ai.natural.language.service;

import fii.ai.natural.language.mapper.MetadataMapper;
import fii.ai.natural.language.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    private MetadataMapper metadataMapper;

    @Autowired
    public CommentServiceImpl(MetadataMapper metadataMapper) {
        this.metadataMapper = metadataMapper;
    }

    @Override
    public MovesTree commentMovesTree(MovesTree movesTree) {
        for (int index = 0; index < movesTree.getMainVariant().getMoves().size(); index++) {
            commentMove(movesTree, index);
        }
        return movesTree;
    }

    /**
     * In functi asta vreau sa parcurgi lista de mutari din varianta si sa generezi un comentariu cu ce se intampla in
     * toata varianta. La ce ma refer prin asta ii ca fata de celelalte unde doar puneai comentariu pentru fiecare miscare
     * aici va trebui sa pui cumva un comentariu pentru toate mutarile. Doar ca nu ar arata bine doar sa fie comentate mutarile
     * pe rand si apoi puse la un loc, cometariu ar trebui sa arate ceva de genul Dupa algorithmName si strategyNames la aceasta mutare
     * culoare (cea care va fi prima culaore) ia piesele, ii sunt lute piesele, a dat sah de n ori, era la o mutare de a da
     * sah mat, a dat sah mat etc( ceea ce ti se pare si tie relevant si mai putem vorbi detaliile oricum). Ii important ca
     * comentariile sa fie "orientate" spre culoarea care face prima mutarea.
     * Aceste comentarii se vor adauga in lista de comentarii care o are inputVariant.
     * Comentariile trebuie tot in engleza. Functiile ar fi oarecum asemanatoare cu celelalte, daca poti cumva sa le folosesti pe
     * celelalte ar fi ideal dar nu stiu daca vei reusi dar daca nu vrei putea folosi bucati din codul de la celelalte oricum.
     *
     * @param moveVariant
     * @return
     */
    @Override
    public MoveVariant commentMoveVariant(MoveVariant moveVariant, MovePosition movePosition) {
        //Pentru a testa functionalitatea functiei trebuie apelat endpointul optimal-moves din postman pentru a vedea daca functioneaza
        // (body-ul pentru a testa asta ar trebuie sa fie unul cu structura la prima varianta de json din idei.md, evident ii posibil sa fie nevoie sa fie modifiat
        // pentru a aduce jocul in cazul dorit)
        //TODO COSMIN
        moveVariant.getComments().add("This variant uses  algorithm " + moveVariant.getAlgorithmName() + " and strategies " + moveVariant.getStrategyNames());
        commentPiecesTaken(moveVariant);
        //TODO FLORENTINA
        commentPreCheckMate(moveVariant);
        commentPromoteToPiece(moveVariant);
        commentGameState(moveVariant);
        commentChecks(moveVariant);
        commentCastlingState(moveVariant,movePosition);
        return null;
    }

    private void commentPiecesTaken(MoveVariant moveVariant) {
        /*
        Functia asta va adauga la varinata primita ca parametru un comentariu in care zice ce piese au fost luate de culoarea
        care face prima mutare si de asemena ce piese au fost pierdute
         */
        List<String> playerPiecesTaken = new ArrayList<>();
        List<String> oponentPiecesTaken = new ArrayList<>();
        String playerColor = new String();
        int moveIndex = 1;
        for (Node node : moveVariant.getMoves()) {
            if (node.getMetadata().size() != 0) {
                MoveMetadata moveMetadata = metadataMapper.map(node.getMetadata());
                if (moveIndex == 1) {
                    playerColor = moveMetadata.getColor();
                }
                if (moveMetadata.getPieceTaken() != null && moveMetadata.getColor().equals(playerColor)) {
                    playerPiecesTaken.add(moveMetadata.getPieceTaken());
                } else if (moveMetadata.getPieceTaken() != null) {
                    oponentPiecesTaken.add(moveMetadata.getPieceTaken());
                }
            }
            moveIndex++;
        }
        playerColor = playerColor.substring(0, 1).toUpperCase() + playerColor.substring(1);
        String comment = new String(playerColor);
        if (playerPiecesTaken.size() > 0 && oponentPiecesTaken.size() > 0) {
            comment = comment + " took the following pieces: ";
            for (String pieceTaken : playerPiecesTaken) {
                comment = comment + pieceTaken + ", ";
            }
            comment = comment.substring(0, comment.length() - 2);
            comment += "and lost the following pieces: ";
            for (String pieceLost : oponentPiecesTaken) {
                comment = comment + pieceLost + ", ";
            }
            comment = comment.substring(0, comment.length() - 2);
            comment += ".";
        }
        if (playerPiecesTaken.size() > 0 && oponentPiecesTaken.size() == 0) {
            comment = comment + " took the following pieces: ";
            for (String pieceTaken : playerPiecesTaken) {
                comment = comment + pieceTaken + ", ";
            }
            comment = comment.substring(0, comment.length() - 2);
            comment += ".";
        }
        if (playerPiecesTaken.size() == 0 & oponentPiecesTaken.size() > 0) {
            comment += " lost the following pieces: ";
            for (String pieceLost : oponentPiecesTaken) {
                comment = comment + pieceLost + ", ";
            }
            comment = comment.substring(0, comment.length() - 2);
            comment += ".";
        }
        moveVariant.getComments().add(comment);
    }

    private void commentChecks(MoveVariant moveVariant) {
        List<List<String>> playerChecks = new ArrayList<>();
        List<List<String>> oponentChecks = new ArrayList<>();
        String playerColor = new String();
        String oponentColor = new String();
        int moveIndex = 1;
        for (Node node : moveVariant.getMoves()) {
            if (node.getMetadata().size() != 0) {
                MoveMetadata moveMetadata = metadataMapper.map(node.getMetadata());
                if (moveIndex == 1) {
                    playerColor = moveMetadata.getColor();
                }
                if (moveIndex == 2) {
                    oponentColor = moveMetadata.getColor();
                }
                if (moveMetadata.getPieceTaken() != null && moveMetadata.getColor().equals(playerColor)) {
                    playerChecks.add(moveMetadata.getCheckPieces());
                } else if (moveMetadata.getPieceTaken() != null) {
                    oponentChecks.add(moveMetadata.getCheckPieces());
                }
            }
            moveIndex++;
        }
        playerColor = playerColor.substring(0, 1).toUpperCase() + playerColor.substring(1);
        oponentColor = oponentColor.substring(0, 1).toUpperCase() + oponentColor.substring(1);

        String comment = new String(playerColor);
        if (playerChecks.size() > 0 && oponentChecks.size() > 0) {
            comment = comment += " checked " + oponentColor + " " + ((Integer) oponentChecks.size()).toString() + " times and " + oponentColor + " checked " + playerColor;
            comment += " " + (Integer) playerChecks.size() + " times.";
        }
        if (playerChecks.size() > 0 && oponentChecks.size() == 0) {
            comment = new String(oponentColor);
            comment += " checked " + playerColor + " " + (Integer) playerChecks.size() + " times.";
        }
        if (playerChecks.size() == 0 && oponentChecks.size() > 0) {
            comment += " checked " + oponentColor + " " + (Integer) oponentChecks.size() + " times.";
        }
        moveVariant.getComments().add(comment);
    }

    private void commentCastlingState(MoveVariant moveVariant, MovePosition movePosition) {
        Node firstMove = moveVariant.getMoves().get(0);
        MoveMetadata firstMoveMetadata = metadataMapper.map(firstMove.getMetadata());
        String playerColor = firstMoveMetadata.getColor();
        int moveIndex = 1;
        int kingSideIndex = 0;
        int queenSideIndex = 0;
        int noSideIndex = 0;
        boolean kingSideOk = false;
        boolean queenSideOk = false;
        boolean noSideOk = false;
        for (Node node : moveVariant.getMoves()) {
            MoveMetadata moveMetadata = metadataMapper.map(node.getMetadata());
            if (moveIndex % 2 == 1 && moveMetadata.getCastlingState().equals(firstMoveMetadata.getCastlingState()) == false) {
                if (moveMetadata.getCastlingState().equals("k") && kingSideOk == false) {
                    kingSideIndex = moveIndex;
                    kingSideOk = true;
                }
                if (moveMetadata.getCastlingState().equals("q") && queenSideOk == false) {
                    queenSideIndex = moveIndex;
                    queenSideOk = true;
                }
                if (moveMetadata.getCastlingState() == null) {
                    noSideIndex = moveIndex;
                    break;
                }
            }
            moveIndex++;
        }
        String comment = new String();
        if (kingSideIndex > 0 && noSideIndex > 0) {
            comment = new String(playerColor);
            comment += " can only do the king side castle since move " + (Integer) kingSideIndex + " can't do any castle since " + (Integer) noSideIndex + ".";
        }
        if (queenSideIndex > 0 && noSideIndex > 0) {
            comment = new String(playerColor);
            comment += " can only do the queen side castle since move " + (Integer) kingSideIndex + " can't do any castle since " + (Integer) noSideIndex + ".";
        }
        if (kingSideIndex == 0 && queenSideIndex == 0 && noSideIndex > 0) {
            comment = new String(playerColor);
            comment += " can no longer do any castle since move " + noSideIndex;
        }
        if (comment != null) {
            moveVariant.getComments().add(comment);
        }
    }


    private void commentPreCheckMate(MoveVariant moveVariant) {
        int moveIndex = 1;
        String moveColor = new String();
        List<String> getComment = new ArrayList<>();
        List<String> takeComment = new ArrayList<>();
        for (Node node : moveVariant.getMoves()) {
            if (node.getMetadata().size() != 0) {
                MoveMetadata moveMetadata = metadataMapper.map(node.getMetadata());
                if (moveIndex == 1) {
                    moveColor = moveMetadata.getColor();
                }
                if (moveMetadata.getPreCheckMate() && moveMetadata.getColor().equals(moveColor)) {
                    String move = "" + moveIndex;
                    getComment.add(move);
                } else if (moveMetadata.getPreCheckMate() && !moveMetadata.getColor().equals(moveColor)) {
                    String move = "" + moveIndex;
                    takeComment.add(move);
                }
            }
            moveIndex++;
        }
        moveColor = moveColor.substring(0, 1).toUpperCase() + moveColor.substring(1);
        if (getComment.size() > 0 && takeComment.size() > 0) {
            moveVariant.getComments().add(moveColor + " was one step away from giving checkmate at position(s):" +
                    getComment + " and one step away from taking checkmate at position(s):" + takeComment + ".");
        } else if (getComment.size() > 0 && takeComment.size() == 0) {
            moveVariant.getComments().add(moveColor + " was one step away from giving checkmate at position(s):" +
                    getComment + ".");
        } else if (getComment.size() == 0 && takeComment.size() > 0) {
            moveVariant.getComments().add(moveColor + " was one step away from taking CheckMate at position(s):" + takeComment + ".");
        }
    }

    private void commentPromoteToPiece(MoveVariant moveVariant) {
        String moveColor = new String();
        List<String> commentColor = new ArrayList<>();
        List<String> commentOpponent = new ArrayList<>();
        Node move = moveVariant.getMoves().get(0);
        MoveMetadata moveMeta = metadataMapper.map(move.getMetadata());
        moveColor = moveMeta.getColor();
        for (Node node : moveVariant.getMoves()) {
            if (node.getMetadata().size() != 0) {
                MoveMetadata moveMetadata = metadataMapper.map(node.getMetadata());
                if (moveMetadata.getPromoteToPiece() != null && moveMetadata.getColor().equals(moveColor)) {
                    commentColor.add(moveMetadata.getPromoteToPiece());
                } else if (moveMetadata.getPromoteToPiece() != null && !moveMetadata.getColor().equals(moveColor)) {
                    commentOpponent.add(moveMetadata.getPromoteToPiece());
                }
            }
        }
        moveColor = moveColor.substring(0, 1).toUpperCase() + moveColor.substring(1);
        if (commentColor.size() > 0 && commentOpponent.size() > 0) {
            moveVariant.getComments().add(moveColor + " side promoted pawns to " +
                    commentColor + " and his opponent promoted pawns to " + commentOpponent + ".");
        } else if (commentColor.size() > 0 && commentOpponent.size() == 0) {
            moveVariant.getComments().add(moveColor + " side promoted pawns to " + commentColor + ".");
        } else if (commentColor.size() == 0 && commentOpponent.size() > 0) {
            moveVariant.getComments().add(moveColor + " opponent promoted pawns to " + commentOpponent + ".");
        }
    }

    private void commentGameState(MoveVariant moveVariant) {
        int moveIndex = 1;
        for (Node node : moveVariant.getMoves()) {
            if (node.getMetadata().size() != 0) {
                MoveMetadata moveMetadata = metadataMapper.map(node.getMetadata());
                if (moveMetadata.getState() != null) {
                    if (moveMetadata.getState().equals("equal")) {
                        String move = "" + moveIndex;
                        moveVariant.getComments().add("Game finished as a draw at move " + move + ".");
                        break;
                    }
                    if (moveMetadata.getState().equals("checkmate")) {
                        String moveColor = moveMetadata.getColor();
                        String move = "" + moveIndex;
                        moveColor = moveColor.substring(0, 1).toUpperCase() + moveColor.substring(1);
                        moveVariant.getComments().add(moveColor + " side won the game by getting checkmate at move " + move + ".");
                        break;
                    }
                }
            }
            moveIndex++;
        }
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
        MoveMetadata moveMetadata = metadataMapper.map(move.getMetadata());
        decorateWithBasicMoveDescriptionComment(moveMetadata, move);
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
