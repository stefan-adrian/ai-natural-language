package fii.ai.natural.language.model;

import java.util.ArrayList;
import java.util.List;

import fii.ai.natural.language.model.Node;

/**
 * This is the model for the input only with FEN position and possible moves from that position
 */

public class MovePosition {

    /**
     * Initial position described FEN notation
     */
    private String initialStateFEN;

    /**
     * List of possible moves from the given position
     */
    private List<Node> variants = new ArrayList<>();


    public String getInitialStateFEN() {
        return initialStateFEN;
    }

    public void setInitialStateFEN(String initialStateFEN) {
        this.initialStateFEN = initialStateFEN;
    }

    public List<Node> getVariants() {
        return variants;
    }

    public void setVariants(List<Node> variants) {
        this.variants = variants;
    }

}