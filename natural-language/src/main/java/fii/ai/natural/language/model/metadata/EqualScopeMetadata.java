package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class EqualScopeMetadata implements Metadata {
    private static final String KEY = "EqualScope";

    private Boolean playingForEqual;

    public EqualScopeMetadata(Boolean playingForEqual) {
        this.playingForEqual = playingForEqual;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public Boolean getPlayingForEqual() {
        return playingForEqual;
    }
}
