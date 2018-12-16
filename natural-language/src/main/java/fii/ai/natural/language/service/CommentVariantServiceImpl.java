package fii.ai.natural.language.service;

import fii.ai.natural.language.mapper.MetadataMapper;
import fii.ai.natural.language.model.MoveMetadata;
import fii.ai.natural.language.model.MovePosition;
import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentVariantServiceImpl implements CommentVariantService {

    private MetadataMapper metadataMapper;

    @Autowired
    public CommentVariantServiceImpl(MetadataMapper metadataMapper) {
        this.metadataMapper = metadataMapper;
    }

    @Override
    public void commentMoveVariant(MoveVariant moveVariant) {
        moveVariant.getComments().add("Algorithm used for this moves is " + moveVariant.getAlgorithmName() + " with strategies " + moveVariant.getStrategyNames());
        commentPiecesTaken(moveVariant);
        commentPreCheckMate(moveVariant);
        commentPromoteToPiece(moveVariant);
        commentGameState(moveVariant);
        commentChecks(moveVariant);
        commentCastlingState(moveVariant);
        commentEqualScope(moveVariant);
    }

    private void commentPiecesTaken(MoveVariant moveVariant) {
        //TODO COSMIN Fix function, this function always add comment with move color, even when is not necessary because no piece was taken
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
        if(playerPiecesTaken.size()>0||oponentPiecesTaken.size()>0) {
            moveVariant.getComments().add(comment);
        }
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
        if(playerChecks.size()>0||oponentChecks.size()>0) {
            moveVariant.getComments().add(comment);
        }
    }

    private void commentCastlingState(MoveVariant moveVariant) {
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
        String comment = null;
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
                    takeComment.add(move);
                } else if (moveMetadata.getPreCheckMate() && !moveMetadata.getColor().equals(moveColor)) {
                    String move = "" + moveIndex;
                    getComment.add(move);
                }
            }
            moveIndex++;
        }

        if(!moveColor.isEmpty()) {
            moveColor = moveColor.substring(0, 1).toUpperCase() + moveColor.substring(1);
        }
        if (getComment.size() > 0 && takeComment.size() > 0) {
            moveVariant.getComments().add("Because of the opponent move, " + moveColor + " was one step away from giving checkmate at position(s):" +
                    getComment + " and one step away from taking checkmate at position(s):" + takeComment + " because of his mistake.");
        } else if (getComment.size() > 0 && takeComment.size() == 0) {
            moveVariant.getComments().add("Because of the opponent move, " + moveColor + " was one step away from giving checkmate at position(s):" +
                    getComment + ".");
        } else if (getComment.size() == 0 && takeComment.size() > 0) {
            moveVariant.getComments().add("Because " + moveColor + " make a mistake, the opponent was one step away from giving CheckMate at position(s):" + takeComment + ".");
        }
    }

    private void commentPromoteToPiece(MoveVariant moveVariant) {
        //TODO COSMIN Same as the one for pieces taken
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

    private void commentEqualScope(MoveVariant moveVariant) {
        List<Boolean> commentColor = new ArrayList<>();
        List<Boolean> commentOpponent = new ArrayList<>();
        Node move = moveVariant.getMoves().get(0);
        MoveMetadata moveMeta = metadataMapper.map(move.getMetadata());
        String moveColor = moveMeta.getColor();
        for (Node node : moveVariant.getMoves()) {
            if (node.getMetadata().size() != 0) {
                MoveMetadata moveMetadata = metadataMapper.map(node.getMetadata());
                //moveMetadata.setEqualScope(true);
                if (moveMetadata.getEqualScope() == true && moveMetadata.getColor().equals(moveColor)) {
                    commentColor.add(moveMetadata.getEqualScope());
                } else if (moveMetadata.getEqualScope() == true && !moveMetadata.getColor().equals(moveColor)) {
                    commentOpponent.add(moveMetadata.getEqualScope());
                }
            }
        }
        moveColor = moveColor.substring(0, 1).toUpperCase() + moveColor.substring(1);
        if (commentColor.size() > 0 && commentOpponent.size() > 0) {
            moveVariant.getComments().add("Both sides play with equal scope.");
        } else if (commentColor.size() > 0 && commentOpponent.size() == 0) {
            moveVariant.getComments().add(moveColor + " side plays with equal scope.");
        } else if (commentColor.size() == 0 && commentOpponent.size() > 0) {
            moveVariant.getComments().add(moveColor + " opponent plays with equal scope.");
        }
    }

    private void commentGameState(MoveVariant moveVariant) {
        //TODO COSMIN This one always adds a empty comment
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
}
