package fii.ai.natural.language.service;

import fii.ai.natural.language.model.Move;
import fii.ai.natural.language.repository.MoveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoveServiceImpl implements MoveService {

    private MoveRepository moveRepository;

    @Autowired
    public MoveServiceImpl(MoveRepository moveRepository) {
        this.moveRepository = moveRepository;
    }

    @Override
    public List<Move> getAll() {
        return moveRepository.findAll();
    }

    @Override
    public Move getById(Long id) {
        return moveRepository.findById(id).get();
    }

    @Override
    public Move save(Move move) {
        return moveRepository.save(move);
    }
}
