package fii.ai.natural.language.model;

public class MovesTree {

    /**
     * Initial position described FEN notation
     */
    private String initialStateFEN;

    /**
     * Describes the main variant
     */
    private MoveVariant mainVariant;

    public String getInitialStateFEN() {
        return initialStateFEN;
    }

    public void setInitialStateFEN(String initialStateFEN) {
        this.initialStateFEN = initialStateFEN;
    }

    public MoveVariant getMainVariant() {
        return mainVariant;
    }

    public void setMainVariant(MoveVariant mainVariant) {
        this.mainVariant = mainVariant;
    }

    @Override
    public String toString() {
        return "MovesTree{" +
                "initialStateFEN='" + initialStateFEN + '\'' +
                ", mainVariant=" + mainVariant +
                '}';
    }
}
