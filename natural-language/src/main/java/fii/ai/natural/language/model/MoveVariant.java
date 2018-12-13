package fii.ai.natural.language.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    /**
     * The variant score is used to calculate if a mistake has happened.
     */
    private Double score;

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

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MoveVariant that = (MoveVariant) o;
        return Objects.equals(moves, that.moves) &&
                Objects.equals(algorithmName, that.algorithmName) &&
                Objects.equals(strategyNames, that.strategyNames) &&
                Objects.equals(comments, that.comments) &&
                Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moves, algorithmName, strategyNames, comments, score);
    }
}
