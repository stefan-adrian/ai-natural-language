package fii.ai.natural.language.model;

import java.util.List;

public class MoveVariant {

    /**
     * List of moves for the given variant
     */
    private List<Node> moves;

    /**
     * Strategy used for this variant ex:MinMax, AlfaBeta. For the mainVariant si null.
     */
    private String strategyName;

    public List<Node> getMoves() {
        return moves;
    }

    public void setMoves(List<Node> moves) {
        this.moves = moves;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public void setStrategyName(String strategyName) {
        this.strategyName = strategyName;
    }
}
