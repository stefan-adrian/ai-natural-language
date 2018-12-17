package fii.ai.natural.language.service;

import fii.ai.natural.language.model.Metadata;
import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.MovesTree;
import fii.ai.natural.language.model.Node;
import fii.ai.natural.language.model.metadata.PreCheckMateMetadata;
import fii.ai.natural.language.utils.ScoreInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.abs;

@Service
public class ScoreServiceImpl implements ScoreService {


    public List<MoveVariant> getMoveVariantsByScore(List<MoveVariant> variants, MoveVariant mainVariant, int depthForMainVariant, int indexOfMove) {
        double variantScore, avgColor1, avgColor2;
        double scoreMax = -5;
        int movesCount;
        boolean checkMate = false, bigError, bigAdv, existsBigAdv = false;
        List<MoveVariant> bestVariants = new ArrayList<>();

        for (MoveVariant variant : variants) {
            variantScore = avgColor1 = avgColor2 = movesCount = 0;
            bigError = false;

            List<Node> moves = variant.getMoves();

            List<Metadata> m = new ArrayList<>();
            for (int i = 0; i < moves.size(); i += 2) {
                avgColor1 += moves.get(i).getScore();
                movesCount++;
                if(!bigError)
                    bigError = getBigImpact(moves.get(i).getMetadata());
            }

            Node ultima;
            if (moves.size() % 2 == 0) {
                ultima = moves.get(moves.size() - 2);
            } else {
                ultima = moves.get(moves.size() - 1);
            }

            if (ultima.getScore() >= 0.99) {
                if (!checkMate) {
                    bestVariants.clear();
                }

                bestVariants.add(variant);
                checkMate = true;
                variant.setScore(1.0);
            } else if (!bigError && !checkMate) {
                bigAdv = false;
                avgColor1 = avgColor1 / movesCount;
                movesCount = 0;
                for (int i = 1; i < moves.size(); i += 2) {
                    avgColor2 += moves.get(i).getScore();
                    movesCount++;
                    if(!bigAdv)
                        bigAdv = getBigImpact(moves.get(i).getMetadata());
                }

                avgColor2 = avgColor2 / movesCount;
                variantScore = avgColor1 - avgColor2;

                if (bigAdv) {
                    if (existsBigAdv) {
                        bestVariants.add(variant);
                    } else {
                        bestVariants.clear();
                        bestVariants.add(variant);
                    }
                    variant.setScore(0.9);
                    existsBigAdv = true;
                } else if (!existsBigAdv && getEqualityLimit(variantScore, scoreMax)) {
                    bestVariants.add(variant);
                    if (variantScore > scoreMax) {
                        scoreMax = variantScore;
                    }
                    variant.setScore(variantScore);
                } else if (!existsBigAdv && variantScore > scoreMax) {
                    scoreMax = variantScore;
                    bestVariants.clear();
                    bestVariants.add(variant);
                    variant.setScore(variantScore);
                }
                //System.out.println("Big adv: "+bigAdv+", Score: "+variantScore);

            }
        }
        if (mainVariant != null) {
            variantScore = avgColor1 = avgColor2 = movesCount = 0;
            List<Node> moves = mainVariant.getMoves();
            bigError = bigAdv = false;
            int depthToLook = 0;
            if (indexOfMove + depthForMainVariant <= moves.size()) {
                depthToLook = indexOfMove + depthForMainVariant;
            } else {
                depthToLook = moves.size();
            }
            for (int i = indexOfMove; i < depthToLook; i += 2) {
                avgColor1 += moves.get(i).getScore();
                movesCount++;
                if(bigError)
                    bigError = getBigImpact(moves.get(i).getMetadata());
            }
            avgColor1 /= movesCount;
            movesCount = 0;
            for (int i = indexOfMove + 1; i < depthToLook; i += 2) {
                avgColor2 += moves.get(i).getScore();
                movesCount++;
                if(!bigAdv)
                    bigAdv = getBigImpact(moves.get(i).getMetadata());
            }
            avgColor2 /= movesCount;
            variantScore = avgColor1 - avgColor2;

            Node ultima;
            if (moves.size() % 2 == 0) {
                ultima = moves.get(moves.size() - 2);
            } else {
                ultima = moves.get(moves.size() - 1);
            }

            if (ultima.getScore() >= 0.99) {
                if (!checkMate) {
                    bestVariants.clear();
                }
                bestVariants.add(mainVariant);
                checkMate = true;
                mainVariant.setScore(1.0);
            } else if (!bigError && !checkMate) {
                if (bigAdv) {
                    if (existsBigAdv) {
                        bestVariants.add(mainVariant);
                    } else {
                        bestVariants.clear();
                        bestVariants.add(mainVariant);
                    }
                    mainVariant.setScore(0.9);
                    existsBigAdv = true;
                } else if (!existsBigAdv && getEqualityLimit(variantScore, scoreMax)) {
                    bestVariants.add(mainVariant);
                    if (variantScore > scoreMax) {
                        scoreMax = variantScore;
                    }
                    mainVariant.setScore(variantScore);
                } else if (!existsBigAdv && variantScore > scoreMax) {
                    scoreMax = variantScore;
                    bestVariants.clear();
                    bestVariants.add(mainVariant);
                    mainVariant.setScore(variantScore);
                }
                mainVariant.setScore(variantScore);
            }
            mainVariant.setScore(variantScore);
        }

        if (bestVariants.size() == 0 && mainVariant != null) {
            bestVariants.add(mainVariant);
        } else if (bestVariants.size() == 0) {
            variants.get(0).setScore(0.0);
            bestVariants.add(variants.get(0));
        }

        return bestVariants;
    }

    private boolean getBigImpact(List<Metadata> meta) {
        for (int i = 0; i < meta.size(); ++i) {
            if (meta.get(i).getKey().equals("PreCheckMate")) {
                return true;
            }
        }
        return false;
    }

    private boolean getEqualityLimit(double variantScore, double scoreMax) {
        double difference;
        if (variantScore >= scoreMax) {
            difference = variantScore - scoreMax;
        } else {
            difference = scoreMax - variantScore;
        }
        if (difference <= ScoreInfo.getEquality()) {
            return true;
        }
        return false;
    }

    public MoveVariant getBestVariantInCaseMistakeHappened(MovesTree movesTree, int indexOfMove) {
        int depthForMainVariant = getDepthToLookInMainVariant(movesTree, indexOfMove);
        if (depthForMainVariant == 0) {
            return null;
        }
        List<MoveVariant> bestMoves = getMoveVariantsByScore(movesTree.getMainVariant().getMoves().get(indexOfMove).getVariants(),
                movesTree.getMainVariant(), depthForMainVariant, indexOfMove);
        for (MoveVariant moveVariant : bestMoves) {
            if (moveVariant == movesTree.getMainVariant()) {
                return null;
            }
        }
        if (bestMoves.size() != 0) {
            return bestMoves.get(0);
        }
        return null;
    }

    private int getDepthToLookInMainVariant(MovesTree movesTree, int indexOfMove) {
        Node move = movesTree.getMainVariant().getMoves().get(indexOfMove);
        if (move.getVariants() == null) {
            return 0;
        }
        int totalNumberOfMoves = 0;
        for (MoveVariant moveVariant : move.getVariants()) {
            totalNumberOfMoves += moveVariant.getMoves().size();
        }
        return totalNumberOfMoves / (move.getVariants().size());
    }
}
