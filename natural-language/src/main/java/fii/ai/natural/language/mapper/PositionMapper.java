package fii.ai.natural.language.mapper;

import fii.ai.natural.language.input.InputNode;
import fii.ai.natural.language.input.InputTree;
import fii.ai.natural.language.input.InputVariant;
import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.Node;
import fii.ai.natural.language.model.MovePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the mapper for the input only with FEN position and possible moves from that position
 */

@Service
public class PositionMapper {

    private TreeMapper treeMapper;

    @Autowired
    public PositionMapper(TreeMapper treeMapper) {
        this.treeMapper = treeMapper;
    }

    public MovePosition map(InputTree inputTree) {
        MovePosition position = new MovePosition();
        position.setInitialStateFEN(inputTree.getInitialStateFEN());
        position.setVariants(treeMapper.mapInputVariantsForVariantsInsideEachMove(inputTree.getVariants()));
        return position;
    }
}