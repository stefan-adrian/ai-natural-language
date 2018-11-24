package fii.ai.natural.language.service;

import fii.ai.natural.language.input.Move;

import java.util.List;

public interface MoveService {

    List<Move> getAll();

    Move getById(Long id);

    Move save(Move move);
}
