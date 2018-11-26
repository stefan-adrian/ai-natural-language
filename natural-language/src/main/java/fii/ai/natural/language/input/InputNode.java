package fii.ai.natural.language.input;

import java.util.List;

public class InputNode {

    /**
     * Move description string. A simple encoding would be:
     * - w if is white, b if is black to move
     * - start field, algebraic notation ex: 'e2'
     * - end field, algebraic notation ex: 'e4'
     * - in case the pawn is at the end of the table the chess piece to be switch with qrbn
     *
     * Examples:
     *  we2e4
     *  be7e5
     *  wg1f3
     *  wa7a8q
     *
     */
    private String move;


    /**
     * Position score: 0 means equilibrium, negative means black has advantage, positive means
     * white has advantage
     */
    private double score;

    /**
     * Variants to the current move
     */
    private List<InputVariant> variants;

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public List<InputVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<InputVariant> variants) {
        this.variants = variants;
    }
}
