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
        movesTree.setMainVariant(mapInputVariant(inputTree.getMainVariant()));
        return movesTree;
    }

    private MoveVariant mapInputVariant(InputVariant inputVariant) {
        MoveVariant moveVariant = new MoveVariant();
        List<Node> moves = new ArrayList<>();
        for (InputNode inputNode : inputVariant.getMoves()) {
            Node node = new Node();
            node.setMove(inputNode.getMove());
            node.setScore(inputNode.getScore());
            List<MoveVariant> moveVariants = new ArrayList<>();
            for (InputVariant inputVariantForNode : inputNode.getVariants()) {
                MoveVariant moveVariantForNode = new MoveVariant();
                moveVariantForNode.setStrategyName(inputVariantForNode.getStrategyName());
                List<Node> nodes = new ArrayList<>();
                for (InputNode inputNodeForNode : inputVariantForNode.getMoves()) {
                    Node nodeForVariants = new Node();
                    nodeForVariants.setMove(inputNode.getMove());
                    nodeForVariants.setScore(inputNode.getScore());
                    nodeForVariants.setPieceTaken(null);
                    nodeForVariants.setMoveGrade(0);
                    nodes.add(nodeForVariants);
                }
                moveVariantForNode.setMoves(nodes);
                moveVariants.add(moveVariantForNode);
            }
            node.setVariants(moveVariants);
            node.setPieceTaken(null);
            node.setMoveGrade(0);
            moves.add(node);
        }
        moveVariant.setMoves(moves);
        moveVariant.setStrategyName(inputVariant.getStrategyName());
        return moveVariant;
    }
}
