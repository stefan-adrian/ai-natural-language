package fii.ai.natural.language.input;

import java.util.List;

public class InputNode {

    /**
     * Move description string. A simple encoding would be:
     * - move number
     * - '. ' (dot, space) if is white, '... ' (three dots, whitespace) if is black to move
     * - start field, algebraic notation ex: 'e2'
     * - end field, algebraic notation ex: 'e4'
     *
     * Examples:
     *  1. e2e4
     *  1... e7e5
     *  2. g1f3
     *  2... b8c6
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

}
