package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.MovesTree;

import java.util.List;

public interface ScoreService {

    List<MoveVariant> getMoveVariantsByScore(List<MoveVariant> variants, MoveVariant mainVariant, int depthForMainVariant, int indexOfMove);

    MoveVariant getBestVariantInCaseMistakeHappened(MovesTree movesTree, int indexOfMove);
}
