package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class GameStateMetadata implements Metadata {
    private static final String KEY = "GameState";
    /**
     * state is equal for equal,checkmate for checkmate
     */
    private String state;

    public GameStateMetadata(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    @Override
    public String getKey() {
        return KEY;
    }

}
