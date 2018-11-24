package fii.ai.natural.language.repository;

import fii.ai.natural.language.input.Move;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MoveRepository extends JpaRepository<Move, Long> {
}
