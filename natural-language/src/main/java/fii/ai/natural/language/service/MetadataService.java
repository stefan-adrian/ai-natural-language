package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MovePosition;
import fii.ai.natural.language.model.MovesTree;

/**
 * This is responsible with decorating moves with metadatas
 */
public interface MetadataService {

    void decorateWithMetadata(MovesTree movesTree);

    public void decorateWithMetadataOptimalMoves(MovePosition movePosition);
}
