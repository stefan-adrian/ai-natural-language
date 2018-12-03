package fii.ai.natural.language.input;

import java.util.List;

public class InputVariant {

    /**
     * List of moves for the given variant
     */
    private List<InputNode> moves;

    /**
     * Algorithm used for this variant ex:MinMax
     */
    private String algorithmName;

    /**
     * Strategies like PieceRemained, AttackPieces
     */
    private List<String> strategyNames;

    public List<InputNode> getMoves() {
        return moves;
    }

    public void setMoves(List<InputNode> moves) {
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
}
