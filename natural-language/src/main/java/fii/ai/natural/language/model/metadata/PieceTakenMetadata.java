package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class PieceTakenMetadata implements Metadata {

    private static final String KEY = "PieceTaken";

    /**
     * Is null in case no piece was taken or the name of the piece in case a piece was taken in this move.
     */
    private final String pieceTaken;

    public PieceTakenMetadata(String pieceTaken) {
        this.pieceTaken = pieceTaken;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public String getPieceTaken() {
        return pieceTaken;
    }
}
