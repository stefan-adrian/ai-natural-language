package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MoveVariant;
import fii.ai.natural.language.model.Node;
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
    public List<MoveVariant> getMoveVariantsByScore(List<MoveVariant> variants) {
        double variantScore;
        double scoreMax = -5;
        boolean checkMate = false;
        List<MoveVariant> bestVariants = new ArrayList<>();

        for (MoveVariant variant : variants) {
            variantScore = 0;
            List<Node> moves = variant.getMoves();


            for (int i = 0; i < moves.size(); i += 2) {
                variantScore += moves.get(i).getScore();
            }
            for (int i = 1; i < moves.size(); i += 2) {
                variantScore -= moves.get(i).getScore();
            }
            Node ultima;
            if (moves.size() % 2 == 0) {
                ultima = moves.get(moves.size() - 2);
            } else {
                ultima = moves.get(moves.size() - 1);
            }

            //Am schimbat aici in 1 ca sa reprezinte sah matul ca de fapt limita aia reprezinta daca la mutarea urmatoare poti da sah mat
            //Am lasat si ce ai scris tu in comentariu ca sa stii tu la ce te-ai gandit cand ai scris pentru cand o sa schimbi
            if (ultima.getScore() >= 1/*ScoreInfo.getCheckMateLimit()*/) {
                if (checkMate == false) bestVariants.clear();
                bestVariants.add(variant);
                checkMate = true;
            } else if (!checkMate && abs(variantScore - scoreMax) <= ScoreInfo.getEquality()) {
                bestVariants.add(variant);
                if (variantScore > scoreMax) {
                    scoreMax = variantScore;
                }
            } else if (!checkMate && abs(variantScore - scoreMax) > ScoreInfo.getEquality()) {
                scoreMax = variantScore;
                bestVariants.clear();
                bestVariants.add(variant);
            }
        }

        return bestVariants;
    }
}
