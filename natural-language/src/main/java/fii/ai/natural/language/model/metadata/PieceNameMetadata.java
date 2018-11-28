package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class PieceNameMetadata implements Metadata {

    private static final String KEY = "PieceName";

    public PieceNameMetadata(String chessPieceName) {
        this.chessPieceName = chessPieceName;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    /**
     * The complete name of the chess piece Ex: King, Queen
     */
    private final String chessPieceName;

    public String getChessPieceName() {
        return chessPieceName;
    }
}
