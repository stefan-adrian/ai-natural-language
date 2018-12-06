package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MoveVariant;

import java.util.List;

public interface ScoreService {

    List<MoveVariant> getMoveVariantsByScore(List<MoveVariant> variants);
}
