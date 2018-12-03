package fii.ai.natural.language.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.Move;

import fii.ai.natural.language.model.Metadata;
import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.model.Node;
import fii.ai.natural.language.model.metadata.GameStateMetadata;
import fii.ai.natural.language.model.metadata.MoveGradeMetadata;
import fii.ai.natural.language.model.metadata.PieceColorMetadata;
import fii.ai.natural.language.model.metadata.PieceNameMetadata;
import fii.ai.natural.language.model.metadata.PieceTakenMetadata;

import static java.lang.Math.abs;

@Service
public class MetadataServiceImpl implements MetadataService {

    private final Map<String, String> pieceCode;

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
        MoveVariant mainVariant = movesTree.getMainVariant();

        Board board = new Board();
        board.loadFromFen(initialPosition);

        for (Node node : mainVariant.getMoves()) {
            String moveString = node.getMove();
            Move move = moveFromText(moveString);
            List<Metadata> nodeMetadata = node.getMetadata();
            updateMetadata(board, move, nodeMetadata, node.getScore());
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

    private void updateMetadata(Board board, Move move, List<Metadata> nodeMetadata, double score) {
        updatePieceNameMetadata(board, move, nodeMetadata);
        updatePieceColorMetadata(board, move, nodeMetadata);
        updatePieceTakenMetadata(board, move, nodeMetadata);
        updateMoveGradeMetadata(board, move, nodeMetadata, score);
        updateGameStateMetadata(board, move, nodeMetadata);
        updateEnPassantMetadata(board, move, nodeMetadata);
        updateCheckMetadata(board, move, nodeMetadata);
        updateCastlingMetadata(board, move, nodeMetadata);
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
        // this is subject to change, eventually
        // 0 \in [0,0.1)
        // 1 \in [0.1,0.3)
        // 2 \in [0.3,0.6)
        // 3 \in [0.6,1]
        if (abs(score) < 0.1) {
            nodeMetadata.add(new MoveGradeMetadata(0));
        }
        if (abs(score) < 0.3) {
            nodeMetadata.add(new MoveGradeMetadata(signum(score)));
        }
        if (abs(score) < 0.7) {
            nodeMetadata.add(new MoveGradeMetadata(signum(score) * 2));
        }
        nodeMetadata.add(new MoveGradeMetadata(signum(score * 3)));
    }

    private void updateGameStateMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        Board result = board.clone();
        result.doMove(move);
        if(result.isDraw()) {
            nodeMetadata.add(new GameStateMetadata("equal"));
        }
        if(result.isMated()) {
            nodeMetadata.add(new GameStateMetadata("checkmate"));
        }
    }

    private void updateEnPassantMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        // TODO
    }

    private void updateCheckMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        // TODO
    }

    private void updateCastlingMetadata(Board board, Move move, List<Metadata> nodeMetadata) {
        // TODO
    }
}
