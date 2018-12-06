package fii.ai.natural.language.service;

import fii.ai.natural.language.model.MoveVariant;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScoreServiceImpl implements ScoreService {

    /**
     * In functia asta vreau sa parcurgi variantele care le primesti ca parametru si sa decizi pe baza scorului
     * care sunt cele mai bune varainte, poate fi doar una dar pot fi si mai multe care dau scorul maximal.
     *
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
     * @param variants a list of move variants
     * @return the variants with maximal score
     */
    public List<MoveVariant> getMoveVariantsByScore(List<MoveVariant> variants){
        //TODO IOAN
        return null;
    }
}
