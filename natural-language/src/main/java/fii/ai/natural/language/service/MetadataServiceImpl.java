package fii.ai.natural.language.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fii.ai.natural.language.model.metadata.*;
import fii.ai.natural.language.utils.ScoreInfo;
import org.springframework.stereotype.Service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.CastleRight;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.PieceType;
import com.github.bhlangonijr.chesslib.Side;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import fii.ai.natural.language.model.Metadata;
import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.model.Node;

import static java.lang.Math.abs;

@Service
public class MetadataServiceImpl implements MetadataService {

    private final Map<String, String> pieceCode;
    private boolean whiteStartedPlayingForEqual = false;
    private boolean blackStartedPlayingForEqual = false;

    public MetadataServiceImpl() {
        pieceCode = new HashMap<>();
        pieceCode.put("q", "QUEEN");
        pieceCode.put("r", "ROOK");
        pieceCode.put("b", "BISHOP");
        pieceCode.put("n", "KNIGHT");
    }

    @Override
    public void decorateWithMetadata(MovesTree movesTree) {

        String initialPosition = movesTree.getInitialStateFEN();
        Board start = new Board();
        start.loadFromFen(initialPosition);

        if (movesTree.getMainVariant() != null) {
            decorateVariant(start.clone(), movesTree.getMainVariant(), true);
        }
    }

    private void decorateVariant(Board board, MoveVariant variant, boolean decorateForMainVariantOnly) {
        for (Node node : variant.getMoves()) {
            // get move
            String moveString = node.getMove();
            Move move = moveFromText(moveString);

            // decorate node with metadata
            updateMetadata(board, move, node.getMetadata(), node.getScore(), decorateForMainVariantOnly);

            // check for variants
            List<MoveVariant> variants = node.getVariants();
            if (variants != null) {
                for (MoveVariant subvariant : variants) {
                    decorateVariant(board.clone(), subvariant, false);
                }
            }

            // update the board with given move and continue the variant
            board = board.clone();
            board.doMove(move);
        }
    }

    private Move moveFromText(String moveString) {
        boolean promote = moveString.length() == 6;
        Square from = Square.fromValue(moveString.substring(1, 3).toUpperCase());
        Square to = Square.fromValue(moveString.substring(3, 5).toUpperCase());
        if (promote) {
            String colorPrefix = moveString.substring(0, 1).equals("w") ? "WHITE_" : "BLACK_";
            String pieceName = pieceCode.get(moveString.substring(5, 6));
            Piece promotion = Piece.fromValue(colorPrefix + pieceName);
            return new Move(from, to, promotion);
        }
        return new Move(from, to);
    }

    private void updateMetadata(Board board, Move move, List<Metadata> nodeMetadata, double score, boolean decorateForMainVariantOnly) {
        updatePieceNameMetadata(board, move, nodeMetadata);
        updatePieceColorMetadata(board, move, nodeMetadata);
        updatePieceTakenMetadata(board, move, nodeMetadata);
        updateMoveGradeMetadata(board, move, nodeMetadata, score);
        updateGameStateMetadata(board, move, nodeMetadata);
        updateEnPassantMetadata(board, move, nodeMetadata);
        updateCheckMetadata(board, move, nodeMetadata);
        updateCastlingMetadata(board, move, nodeMetadata);
        updatePromotionMetadata(move, nodeMetadata);
        updatePreCheckMateMetadata(nodeMetadata,score);
        if(decorateForMainVariantOnly) {
            updateEqualScopeMetadata(board, move, nodeMetadata, score);
        }
    }

    private void updatePieceNameMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        Piece piece = board.getPiece(move.getFrom());
        String pieceName = piece.value().split("_")[1];
        pieceName = pieceName.substring(0, 1).toUpperCase() + pieceName.substring(1).toLowerCase();
        nodeMetadata.add(new PieceNameMetadata(pieceName));
    }

    private void updatePieceColorMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        Piece piece = board.getPiece(move.getFrom());
        nodeMetadata.add(new PieceColorMetadata(piece.value().split("_")[0].toLowerCase()));
    }

    private void updatePieceTakenMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        Piece takenPiece = board.getPiece(move.getTo());
        if (!takenPiece.equals(Piece.NONE)) {
            String pieceName = takenPiece.value().split("_")[1];
            nodeMetadata.add(new PieceTakenMetadata(pieceName.toLowerCase()));
        }
    }

    private int signum(double value) {
        return value >= 0 ? 1 : -1;
    }

    private void updateMoveGradeMetadata(Board board, Move move, List<Metadata> nodeMetadata, double score) {
        if (abs(score) < ScoreInfo.getSmallImpactMove()) {
            nodeMetadata.add(new MoveGradeMetadata(0));
        } else if (abs(score) < ScoreInfo.getMediumImpactMove()) {
            nodeMetadata.add(new MoveGradeMetadata(signum(score)));
        } else {
            nodeMetadata.add(new MoveGradeMetadata(signum(score) * 2));
        }
    }

    private void updateGameStateMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        Board result = board.clone();
        result.doMove(move);
        if (result.isDraw()) {
            nodeMetadata.add(new GameStateMetadata("equal"));
        }
        if (result.isMated()) {
            nodeMetadata.add(new GameStateMetadata("checkmate"));
        }
    }

    private void updateEnPassantMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        Board after = board.clone();
        after.doMove(move);
        boolean ep = !after.getEnPassant().equals(Square.NONE);
        if (ep) {
            ep = false;
            Square[] sides = after.getEnPassant().getSideSquares();
            for (Square side : sides) {
                Side pside = board.getPiece(side).getPieceSide();
                PieceType ptype = board.getPiece(side).getPieceType();
                if (PieceType.PAWN.equals(ptype) && pside.equals(board.getSideToMove())) {
                    ep = true;
                    break;
                }
            }
        }
        nodeMetadata.add(new EnPassantMetadata(ep));
    }

    private void updateCheckMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        Board after = board.clone();
        after.doMove(move);
        if (after.isKingAttacked()) {
            Side side = after.getSideToMove();
            Square kingSquare = after.getKingSquare(side);
            List<String> attackingPieces = new ArrayList<>();
            if (after.squareAttackedByPieceType(kingSquare, side.flip(), PieceType.QUEEN) != 0) {
                attackingPieces.add("queen");
            }
            if (after.squareAttackedByPieceType(kingSquare, side.flip(), PieceType.BISHOP) != 0) {
                attackingPieces.add("bishop");
            }
            if (after.squareAttackedByPieceType(kingSquare, side.flip(), PieceType.KNIGHT) != 0) {
                attackingPieces.add("knight");
            }
            if (after.squareAttackedByPieceType(kingSquare, side.flip(), PieceType.ROOK) != 0) {
                attackingPieces.add("rook");
            }
            if (after.squareAttackedByPieceType(kingSquare, side.flip(), PieceType.PAWN) != 0) {
                attackingPieces.add("pawn");
            }
            nodeMetadata.add(new CheckMetadata(attackingPieces));
        }
    }

    private void updateCastlingMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        Board after = board.clone();
        after.doMove(move);
        CastleRight castleRight = after.getCastleRight(after.getSideToMove());
        CastlingStateMetadata meta = null;
        switch (castleRight) {
            case KING_AND_QUEEN_SIDE:
                meta = new CastlingStateMetadata("kq");
                break;
            case KING_SIDE:
                meta = new CastlingStateMetadata("k");
                break;
            case QUEEN_SIDE:
                meta = new CastlingStateMetadata("q");
                break;
            default:
                meta = new CastlingStateMetadata(null);
        }
        nodeMetadata.add(meta);
    }

    private void updatePromotionMetadata(Move move, List<Metadata> nodeMetadata) {
        if (move.getPromotion().getPieceType() != null) {
            nodeMetadata.add(new PromotionMetadata(move.getPromotion().getPieceType().value()));
        }
    }

    private void updateEqualScopeMetadata(Board board, Move move, List<Metadata> nodeMetadata, double score) {
        if (updateAndGetEqualStateByColor(board, move, score)) {
            nodeMetadata.add(new EqualScopeMetadata(true));
        }
    }

    private boolean updateAndGetEqualStateByColor(Board board, Move move, double score) {
        Piece piece = board.getPiece(move.getFrom());
        String color = piece.getPieceSide().value();
        if (color.equals("WHITE")) {
            if (score <= ScoreInfo.getEqualLimit()) {
                whiteStartedPlayingForEqual = true;
            }
            return whiteStartedPlayingForEqual;
        }
        if (color.equals("BLACK")) {
            if (score <= ScoreInfo.getEqualLimit()) {
                blackStartedPlayingForEqual = true;
            }
            return blackStartedPlayingForEqual;
        }
        return false;
    }

    private void updatePreCheckMateMetadata(List<Metadata> nodeMetadata, double score){
        if(score>=ScoreInfo.getCheckMateLimit()){
            nodeMetadata.add(new PreCheckMateMetadata(true));
        }
    }
}
