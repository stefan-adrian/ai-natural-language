package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class EnPassantMetadata implements Metadata {
    private static final String KEY = "EnPassant";

    private Boolean enPassantPossible;

    public EnPassantMetadata(Boolean enPassantPossible) {
        this.enPassantPossible = enPassantPossible;
    }

    public Boolean getEnPassantPossible() {
        return enPassantPossible;
    }

    @Override
    public String getKey() {
        return KEY;
    }

}
