package fii.ai.natural.language.mapper;

import fii.ai.natural.language.input.InputNode;
import fii.ai.natural.language.input.InputTree;
import fii.ai.natural.language.input.InputVariant;
import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.Node;
import fii.ai.natural.language.model.MovePosition;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the mapper for the input only with FEN position and possible moves from that position
 */

@Service
public class PositionMapper {
    public MovePosition map(InputTree inputTree) {
        MovePosition position = new MovePosition();
        position.setInitialStateFEN(inputTree.getInitialStateFEN());
        List<Node> nodes = new ArrayList<>();

        for (InputVariant inputVariant : inputTree.getVariants()) {
            MoveVariant moveVariant = new MoveVariant();
            moveVariant.setAlgorithmName(inputVariant.getAlgorithmName());
            moveVariant.setStrategyNames(inputVariant.getStrategyNames());
            for (InputNode inputNode : inputVariant.getMoves()) {
                Node node = new Node();
                node.setMove(inputNode.getMove());
                node.setScore(inputNode.getScore());
                node.setComments(new ArrayList<>());
                nodes.add(node);
            }
        }
        position.setVariants(nodes);
        return position;
    }
}