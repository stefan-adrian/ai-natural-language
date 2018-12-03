package fii.ai.natural.language.model.metadata;

import fii.ai.natural.language.model.Metadata;

import java.util.List;

public class CheckMetadata implements Metadata {
    private static final String KEY = "Check";
    /**
     * checkPiece the name of the pieces that checks the king
     */
    private List<String> checkPieces;

    public CheckMetadata(List<String> checkPieces) {
        this.checkPieces = checkPieces;
    }

    public List<String> getCheckPieces() {
        return checkPieces;
    }


    @Override
    public String getKey() {
        return KEY;
    }

}
