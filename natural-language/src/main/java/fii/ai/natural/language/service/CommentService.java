package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.MovesTree;

public interface CommentService {

    MovesTree commentMovesTree(MovesTree movesTree);

    MoveVariant commentMoveVariant(MoveVariant moveVariant);
}
