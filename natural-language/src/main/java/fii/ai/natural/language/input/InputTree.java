package fii.ai.natural.language.input;

import java.util.List;

public class InputTree {

    /**
     * Initial position described FEN notation
     */
    private String initialPositionFEN;

    /**
     * Describes the main variant
     */
    private InputVariant mainVariant;

    /**
     * Possible variants/continuations in the initial position
     */
    private List<InputVariant> variants;
}
