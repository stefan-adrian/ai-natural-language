package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class PromotionMetadata implements Metadata {

    private static final String KEY = "Promotion";

    /**
     * Name of the piece that is promoted to
     */
    private final String promotedToPiece;

    public PromotionMetadata(String promotedToPiece) {
        this.promotedToPiece = promotedToPiece;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public String getPromotedToPiece() {
        return promotedToPiece;
    }
}
