package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class PieceColorMetadata implements Metadata {

    private static final String KEY = "PieceColor";

    public PieceColorMetadata(String color) {
        this.color = color;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    /**
     * The color of the piece that is moved ( White or Black )
     */
    private final String color;

    public String getColor() {
        return color;
    }
}
