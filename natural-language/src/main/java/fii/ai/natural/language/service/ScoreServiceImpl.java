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

    /**
     * In functia asta vreau sa parcurgi variantele care le primesti ca parametru si sa decizi pe baza scorului
     * care sunt cele mai bune variante, poate fi doar una dar pot fi si mai multe care dau scorul maximal.
     * <p>
     * Cum m-am gandit sa calculezi scorul ii ca ar trebui sa parcurgi variantele si sa faci o medie a scorurilor pentru
     * fiecare culoare, prin ce ma refer ar trebui categoriat ceva de genul primaCuloare si aDoua culaore ca pe tine te-ar
     * interesa ca culoarea care face prima mutare sa aiba scorul mai bun decat cea care face a doua mutare deoarece tu vrei
     * sa vezi cea mai buna mutare pentru cea care face prima mutare. Deci un prim lucru ii ca ar trebui sa vezi care are
     * media de primaCuloare minus media de a doua culoare mai mare.
     * Pe langa asta totusi va mai trebui sa tii cont de niste detalii adica va trebui sa te uiti daca la o mutare a fost
     * dat sah mat sau daca cumva ultima mutare de la o varianta este una care aduce jocul la o mutare de sah mat (ca sa verifici
     * daca jocul ii la o mutare de sah mat poti folosi valoarea constat din ScoreInfo, daca ii mai mare decat ea insemana ca
     * era la o mutare de sah mat)
     * Acum legat de faptul ca o mutuare poate fi la fel de buna ca alta ma gandesc ca la asta totusi nu ar trebui sa aiba chiar exact
     * acelasi scor, ai putea sa definesti in ScoreInfo inca o valoare constanta pe care sa o folosim gen equality=0.05 si daca diferenta
     * dintre scoruri ii mai mica decat asta atunci 2 mutari sunt la fel de bune(de ce zic sa folosesti o constanta decalarata in ScoreInfo
     * ii ca nu stim inca valorile astea de la modulul de AI)
     *
     * @param variants a list of move variants
     * @return the variants with maximal score
     */
    public List<MoveVariant> getMoveVariantsByScore(List<MoveVariant> variants,MoveVariant mainVariant,int depthForMainVariant,int indexOfMove) {
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
                bigError = getBigImpact(moves.get(i).getMetadata());
            }

            Node ultima;
            if (moves.size() % 2 == 0) {
                ultima = moves.get(moves.size() - 2);
            } else {
                ultima = moves.get(moves.size() - 1);
            }

            if (ultima.getScore() >= 1/*ScoreInfo.getCheckMateLimit()*/) {
                if (checkMate == false) bestVariants.clear();
                bestVariants.add(variant);
                checkMate = true;
                variant.setScore(1.0);
            }

            else if(bigError == false){
                bigAdv = false;
                avgColor1 = avgColor1 / movesCount;
                movesCount = 0;
                for (int i = 1; i < moves.size(); i += 2) {
                    avgColor2 += moves.get(i).getScore();
                    movesCount++;
                    bigAdv = getBigImpact(moves.get(i).getMetadata());
                }

                avgColor2 = avgColor2 / movesCount;
                variantScore = avgColor1 - avgColor2;

                //Am schimbat aici in 1 ca sa reprezinte sah matul ca de fapt limita aia reprezinta daca la mutarea urmatoare poti da sah mat
                //Am lasat si ce ai scris tu in comentariu ca sa stii tu la ce te-ai gandit cand ai scris pentru cand o sa schimbi
                if (bigAdv == true) {
                    if(existsBigAdv)
                        bestVariants.add(variant);
                    else {
                        bestVariants.clear();
                        bestVariants.add(variant);
                    }
                    variant.setScore(variantScore);
                    existsBigAdv=true;
                }
                else if (!checkMate && bigAdv==false && getEqualityLimit(variantScore, scoreMax)) {
                    bestVariants.add(variant);
                    if (variantScore > scoreMax) {
                        scoreMax = variantScore;
                    }
                    //Am adaugat scorul la variante pentru a putea vedea dupa la calcularea greselilor cat de mare este greseala,
                    //si pentru a calcula cat de mare e greseala am nevoie de scorul pe varianta deci cand modifici tu functia de scor sa tii cont si de asta
                    variant.setScore(variantScore);
                } else if (!checkMate && bigAdv==false && variantScore > scoreMax) {
                    scoreMax = variantScore;
                    bestVariants.clear();
                    bestVariants.add(variant);
                    variant.setScore(variantScore);
                }

            }
        }

        if(mainVariant!=null){
            variantScore = avgColor1 = avgColor2 = movesCount = 0;
            List<Node> moves = mainVariant.getMoves();
            bigError = bigAdv = false;
            int depthToLook=0;
            if(indexOfMove+depthForMainVariant<=moves.size()){
                depthToLook=indexOfMove+depthForMainVariant;
            }else{
                depthToLook= moves.size();
            }
            for (int i = indexOfMove; i < depthToLook; i += 2) {
                avgColor1 += moves.get(i).getScore();
                movesCount++;
                bigError=getBigImpact(moves.get(i).getMetadata());
            }
            avgColor1 /= movesCount;
            movesCount = 0;
            for (int i = indexOfMove+1; i < depthToLook; i += 2) {
                avgColor2 += moves.get(i).getScore();
                movesCount++;
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

            if (ultima.getScore() >= 1/*ScoreInfo.getCheckMateLimit()*/) {
                if (checkMate == false) bestVariants.clear();
                bestVariants.add(mainVariant);
                checkMate = true;
                mainVariant.setScore(1.0);
            }

            else if(!bigError){
                //Am schimbat aici in 1 ca sa reprezinte sah matul ca de fapt limita aia reprezinta daca la mutarea urmatoare poti da sah mat
                //Am lasat si ce ai scris tu in comentariu ca sa stii tu la ce te-ai gandit cand ai scris pentru cand o sa schimbi
                if(bigAdv){
                    if(existsBigAdv)
                        bestVariants.add(mainVariant);
                    else {
                        bestVariants.clear();
                        bestVariants.add(mainVariant);
                    }
                    mainVariant.setScore(variantScore);
                    existsBigAdv=true;
                }
                else if (!checkMate && !bigAdv && getEqualityLimit(variantScore, scoreMax) ) {
                    bestVariants.add(mainVariant);
                    if (variantScore > scoreMax) {
                        scoreMax = variantScore;
                    }
                    //Am adaugat scorul la variante pentru a putea vedea dupa la calcularea greselilor cat de mare este greseala,
                    //si pentru a calcula cat de mare e greseala am nevoie de scorul pe varianta deci cand modifici tu functia de scor sa tii cont si de asta
                    mainVariant.setScore(variantScore);
                    //Model de calcul al scorului cu abs nu merge corect, pentru cazul in care scorul maxim era mare si scorul variantei era negativ dadea scorul
                    //mai mare decat scorul maxim actual ceea ce nu ii ok asa ca momentan am sters aia si am comentat ce mai aveai pentru a putea testa
                } else if (!checkMate && bigAdv==false && variantScore>scoreMax) {
                    scoreMax = variantScore;
                    bestVariants.clear();
                    bestVariants.add(mainVariant);
                    mainVariant.setScore(variantScore);
                }
                mainVariant.setScore(variantScore);
            }
        }

        return bestVariants;
    }

    private final boolean getBigImpact(List<Metadata> meta) {
        for (int i = 0; i < meta.size(); ++i) {
            if (meta.get(i).getKey() == "PreCheckMate")
                return true;
        }
        return false;
    }

    private final boolean getEqualityLimit(double variantScore, double scoreMax) {
        double difference;
        if(variantScore >= scoreMax)
            difference = variantScore - scoreMax;
        else
            difference = scoreMax - variantScore;
        if( difference <= ScoreInfo.getEquality())
            return true;
        return false;
    }

    public MoveVariant getBestVariantInCaseMistakeHappened(MovesTree movesTree,int indexOfMove){
        int depthForMainVariant=getDepthToLookInMainVariant(movesTree,indexOfMove);
        if(depthForMainVariant==0){
            return null;
        }
        List<MoveVariant> bestMoves=getMoveVariantsByScore(movesTree.getMainVariant().getMoves().get(indexOfMove).getVariants(),
                movesTree.getMainVariant(),depthForMainVariant,indexOfMove);
        for(MoveVariant moveVariant:bestMoves){
            if(moveVariant==movesTree.getMainVariant()){
                return null;
            }
        }
        if(bestMoves.size()!=0){
            return bestMoves.get(0);
        }
        return null;
    }

    private int getDepthToLookInMainVariant(MovesTree movesTree,int indexOfMove){
        Node move=movesTree.getMainVariant().getMoves().get(indexOfMove);
        if(move.getVariants()==null){
            return 0;
        }
        int totalNumberOfMoves=0;
        for(MoveVariant moveVariant:move.getVariants()){
            totalNumberOfMoves+=moveVariant.getMoves().size();
        }
        return totalNumberOfMoves/(move.getVariants().size());
    }
}
