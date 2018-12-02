package fii.ai.natural.language.mapper;

import fii.ai.natural.language.input.InputNode;
import fii.ai.natural.language.input.InputTree;
import fii.ai.natural.language.input.InputVariant;
import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.model.Node;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TreeMapper {
    public MovesTree map(InputTree inputTree) {
        MovesTree movesTree = new MovesTree();
        movesTree.setInitialStateFEN(inputTree.getInitialStateFEN());
        movesTree.setMainVariant(mapMainVariant(inputTree.getMainVariant()));
        return movesTree;
    }

    private MoveVariant mapMainVariant(InputVariant inputVariant) {
        MoveVariant moveVariant = new MoveVariant();
        List<Node> moves = new ArrayList<>();
        for (InputNode inputNode : inputVariant.getMoves()) {
            Node node = new Node();
            node.setMove(inputNode.getMove());
            node.setScore(inputNode.getScore());
            node.setVariants(mapInputVariantsForVariantsInsideEachMove(inputNode.getVariants()));
            moves.add(node);
        }
        moveVariant.setMoves(moves);
        moveVariant.setStrategyName(inputVariant.getStrategyName());
        return moveVariant;
    }

    private List<MoveVariant> mapInputVariantsForVariantsInsideEachMove(List<InputVariant> nodeVariants){
        List<MoveVariant> moveVariants = new ArrayList<>();
        for (InputVariant inputVariantForNode : nodeVariants) {
            MoveVariant moveVariantForNode = new MoveVariant();
            moveVariantForNode.setStrategyName(inputVariantForNode.getStrategyName());
            List<Node> nodes = new ArrayList<>();
            for (InputNode inputNodeForNode : inputVariantForNode.getMoves()) {
                Node nodeForVariants = new Node();
                nodeForVariants.setMove(inputNodeForNode.getMove());
                nodeForVariants.setScore(inputNodeForNode.getScore());
                nodes.add(nodeForVariants);
            }
            moveVariantForNode.setMoves(nodes);
            moveVariants.add(moveVariantForNode);
        }
        return moveVariants;
    }
}
