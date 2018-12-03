package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

public class PreCheckMateMetadata implements Metadata {

    private static final String KEY = "PreCheckMate";

    private final Boolean preCheckMate;

    public PreCheckMateMetadata(Boolean preCheckMate) {
        this.preCheckMate = preCheckMate;
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public Boolean getPreCheckMate() {
        return preCheckMate;
    }
}
