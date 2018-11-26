package fii.ai.natural.language.input;

import java.util.List;

public class InputVariant {

    /**
     * List of moves for the given variant
     */
    private List<InputNode> moves;

    /**
     * Strategy used for this variant ex:MinMax, AlfaBeta
     */
    private String strategyName;

    public List<InputNode> getMoves() {
        return moves;
    }

    public void setMoves(List<InputNode> moves) {
        this.moves = moves;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }
}
