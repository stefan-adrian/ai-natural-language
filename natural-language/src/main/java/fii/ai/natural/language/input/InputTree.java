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

    public String getInitialPositionFEN() {
        return initialPositionFEN;
    }

    public void setInitialPositionFEN(String initialPositionFEN) {
        this.initialPositionFEN = initialPositionFEN;
    }

    public InputVariant getMainVariant() {
        return mainVariant;
    }

    public void setMainVariant(InputVariant mainVariant) {
        this.mainVariant = mainVariant;
    }

    public List<InputVariant> getVariants() {
        return variants;
    }

    public void setVariants(List<InputVariant> variants) {
        this.variants = variants;
    }
}
