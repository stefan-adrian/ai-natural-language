package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class CastlingStateMetadata implements Metadata {

    private static final String KEY = "CastlingState";

    public CastlingStateMetadata(String castlingState) {
        this.castlingState = castlingState;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    /**
     * Indicates on what side the castling can be made(if can be made in any side)
     * kq for when the castling can be made on both queen and king side
     * k for king side
     * q fot queen side
     * null for when castling can't be made in any side
     */
    private final String castlingState;

    public String getCastlingState() {
        return castlingState;
    }
}
