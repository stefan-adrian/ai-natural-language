package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MovePosition;
import fii.ai.natural.language.model.MoveVariant;

public interface CommentVariantService {

    void commentMoveVariant(MoveVariant moveVariant, MovePosition movePosition);
}
