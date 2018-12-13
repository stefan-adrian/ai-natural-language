package fii.ai.natural.language.model;

import java.util.ArrayList;
import java.util.List;

public class MoveVariant {

    /**
     * List of moves for the given variant
     */
    private List<Node> moves;

    /**
     * Algorithm used for this variant ex:MinMax, AlfaBeta. For the mainVariant si null.
     */
    private String algorithmName;

    /**
     * Strategies like PieceRemained, AttackPieces
     */
    private List<String> strategyNames = new ArrayList<>();

    private List<String> comments = new ArrayList<>();

    public List<Node> getMoves() {
        return moves;
    }

    public void setMoves(List<Node> moves) {
        this.moves = moves;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public List<String> getStrategyNames() {
        return strategyNames;
    }

    public void setStrategyNames(List<String> strategyNames) {
        this.strategyNames = strategyNames;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }
}
