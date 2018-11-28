package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MovesTree;

/**
 * The reason for the interface for the comment service is in case we will do also a CommentServiceTechnical
 * That would be another service that's pretty similar to the CommentServiceImpl but the comments are made in a more technical way
 * The reason we would do the Technical comment service is in case we don't have enough things to do for all of us, otherwise we don't need to do it.
 */
public interface CommentService {

    MovesTree commentMovesTree(MovesTree movesTree);
}
