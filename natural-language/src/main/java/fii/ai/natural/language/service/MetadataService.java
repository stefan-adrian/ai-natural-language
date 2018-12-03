package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MovesTree;

/**
 * This is responsible with decorating moves with metadatas
 */
public interface MetadataService {

    void decorateWithMetadata(MovesTree movesTree);
}
