In documentul asta am sa prezint care este opinia mea cu privire la structura aplicatiei.

# Descrierea problemei

    P8. The chess game
    ✦ A system capable of evaluating situations during a chess game
    and providing suggestions on optimal moves, indicating possible
    mistakes, generating a comment in natural language on a game.
    ✦ Eventually, the system includes several chess strategies and
    indicates the moves suggested by each strategy, explaining the
    decision made.
    ✦ Possible extensions: a mobile device interface that allows the
    system to be used as an assistant in a real-life chess game (with
    or without the automatic recognition of the position of the pieces
    on the board)
    
    Examples of possible interactions
    ✦ User defines a configuration, for instance a given position
        ● The system suggests that the Hf3xg5 is the optimal move
        ● The system explains the suggestion according to MINMAX, 
        AlfaBeta, Negamax, others. The system can indicate the precision
        of the suggestion, the number of anticipated
        moves, the following suggested moves.
    ✦ User introduces a game:
        1.e4 e5 2.Nf3 f6 3.Nxe5 fxe5 4.Qh5+ Ke7 5.Qxe5+ Kf7 6.Bc4+ d5 7.Bxd5+ Kg6 8.h4
        h5 9.Bxb7 Bxb7 10.Qf5+ Kh6 11.d4+ g5 12.Qf7 Qe7 13.hxg5+ Qxg5 14.Rxh5# 1-0
    ✦ System comments / explains the game indicating the optimal moves, mistakes, 
    situations where other moves would lead to victory.


## Care sunt functionalitatile de baza

Toate scenariile din sistemul luat ca intreg se pot reduce la urmatorul scenariul:

* se da ca input o pozitie initiala (poate fi pozitia initiala standard a jocului 
de sah sau poate fi alta), nu este relevant.
* de la pozitia initiala de da o lista de mutari efectuate (aceasta lista de mutari 
efectuate in realitate poate fi si vida, pentru a acomoda rezolvarea scenariului 
in care se cere ca dandu-se o pozitie initiala sa se arate care ar putea variantele
de continuare posibile)
* pentru pozitia initiala dar si pentru oricare dintre mutarile reale se 
efectueaza o analiza si se genereaza:
    * scorul respectivei mutari (pozitii); vom detalia ce ar putea fi scor
    * o lista de variante posibile cu subvariante, eventual
        * fiecare varianta consta din un sir de mutari, poate fi una singura
        * pentru fiecare mutare din sir se da un scor al evaluarii pozitiei daca 
        mutarea ar fi fost efectuata
        * pentru fiecare mutare din varianta exista posibilitatea ca sa existe 
        alternative (de aici sub variante), unde fiecare subvarianta urmeaza 
        definitia unei variante)
* arborele care contine pozitia initiala, mutarile efectuate in realitate si variantele
de continuare (cu mutarile, scorurile si posibilele subvariante ale lor) sunt trimise 
catre modulul de procesare a limbajului natural
* modului e procesare a limbajului natural preia inputul respectiv si produce un text
care contine in esenta cam acelasi arbore fara scoruri, in schimb fiecare mutare 
putand fi decorata cu limbaj natural.
* rezultatul procesarii in limbaj natural este afisat la consola

Aici nu am cuprins tot soiul de interactiuni sau aprecieri ale functionalitatilor de 
pe interfata vizuala, pastram numai cadrul larg care este relevant pentru noi.

## Functionalitatea modului de procesare a limbajului natural

Din cele de mai sus reiese ca singura functionalitate a serviciului de procesare a 
limbajului natural de comunicare cu exteriorul se reduce la o singura procedura:
decorarea unui arbore de mutari evaluat cu limbaj natural

Pentru ca asta sa se intample trebuie sa stabilim un contract cu cei care ne apeleaza, 
respectiv un protocol prin care putem prelua inputul si un protocol prin care dam 
rezultatul. Pentru simplitate putem presupune ca protocolul de returnare a rezultatului 
nu este unul structurat ci este pur si simplu sub forma unui text (sir de caractere)
care va fi afisat pe interfata web. Daca este necesar putem sa facem si ceva 
mai detaliat, dar am mari indoieli ca timpul permite celor care se ocupa cu interfata 
sa mai implementeze si un interpretor pentru rezultat, sa ca relist vorbind putem sa 
consideram ca rezultat un text.

In ceea ce priveste protocolul de intrare, lucrurile sunt putin mai complicate.

## Formatul fisierului de intrare

O sa detaliez mai mult depsre asta. Deocamdata vorbim despre structra 
fundamentala.

Pentru input cred ca trebuie sa folosim json. Eu cred ca json este ok, 
dar cine implementeaza serviciul trebuie sa spuna 
cum ii este mai bine. Voi presupune ca este json. 

Apoi pentru a codifica continutul cred ca trebuie sa folosim chestii 
standard. Motivul este acela ca sunt convins ca exista si in Java si in Python
librarii care sa produca chestii standard.

Exemplu de standarde:

* FEN [Wikipedia page for FEN](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation)
* PGN [Wikipedia page for PGN](https://en.wikipedia.org/wiki/Portable_Game_Notation)


Pentru descrierea formatului am creat niste clase in pachetul: `fii.ai.natural.language.input`. 
Ele sunt urmatoarele:

* **InputTree** este clasa care descrie complet inputul. Contine pozitia principala, varianta principala daca exista)
si variantele initiale daca acestea exista)
* **InputVariant** reprezinta o varianta. O varianta este o lista de mutari unde prima mutare 
este o alternativa la mutarea sau pozitia curenta. O varianta poate avea subvariante. 
Posibilele subvariante sunt atasate fiecarei mutari
* **InputNode** reprezinta o mutare. O mutare este descrisa prin numarul si culoarea celui 
care face mutarea, notarea mutar proriu zise, scorul evaluat al pozitiei daca mutarea
ar fi efectuata, variante alternative la mutare.

Clasele pe care le-am scris nu contin decat campuri private. Ele trebuie
decorate cu getters si setters si apoi folosite cu un Jackson sau ceva alternativ 
pentru a fi serializate/deserializate in format json.

**Observatie:**

Este de preferat ca aceste clase sa fie folosite numai pentru a citi inputul. 
Nu este indicat pentru a fi extinse si folosite la calculele si interpretarile 
noastre chiar daca este tentant. Motivul este acela ca pentru calculele noastre
cel mai probabil vom stoca informatia in clase diferit, pentru a raspunde 
diverselor nevoi si in consecinta unele informatii vor ajunge sa fie redundanta
in diverse campuri. Asta ar crea problema sincronizarii informatiei.

## Scenarii si exemple aproximatve pentru fisierul de intrare

O pozitie initiala si variantele posibile de mutari din acea pozitie:

```json
{
  "initialStateFEN": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
  "variants": [
                      {
                          "moves": [
                              {
                                  "move": "wd2d4",
                                  "score": 0.01
                              },
                              {
                                  "move": "bg8f6",
                                  "score": 0.0
                              }
                          ],
                          "strategyName": "MinMax"
                      },
                      {
                          "moves": [
                              {
                                  "move": "wc2c4",
                                  "score": 0.0
                              },
                              {
                                  "move": "bg8f6",
                                  "score": 0.01
                              }
                          ],
                          "strategyName": "AlfaBeta"
                      }
                  ] 
}
```

Un joc cu mutari alternative:

```json
{
    "initialStateFEN": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
    "mainVariant": {
        "moves": [
            {
                "move": "we2e4",
                "score": 0.01,
                "variants": [
                    {
                        "moves": [
                            {
                                "move": "wd2d4",
                                "score": 0.01
                            },
                            {
                                "move": "bg8f6",
                                "score": 0.0
                            }
                        ],
                        "strategyName": "MinMax"
                    },
                    {
                        "moves": [
                            {
                                "move": "wc2c4",
                                "score": 0.0
                            },
                            {
                                "move": "bg8f6",
                                "score": 0.01
                            }
                        ],
                        "strategyName": "AlfaBeta"
                    }
                ]
            },
            {
                "move": "be7e5",
                "score": 0.01,
                "variants": [
                    {
                        "moves": [
                            {
                                "move": "bf7f5",
                                "score": 0.01
                            }
                        ],
                        "strategyName": "AlfaBeta"
                    },
                    {
                        "moves": [
                            {
                                "move": "bc7c6",
                                "score": 0.01
                            },
                            {
                                "move": "wd2d4",
                                "score": 0.01
                            }
                        ],
                        "strategyName": "MinMax"
                    }
                ]
            }
        ],
        "strategyName": null
    }
}
```

cele de mai sus sunt exemple, dar cam asa le vad eu.

## Functionalitatea

Deci avem ca input un arbore cu variante si ca output un text. Cum ar trebui sa arate acest text?
O varianta la care ma gandesc este in genul comentariilor unei partide de sah. 

Un exemplu:

```text
1.c4 e6 2.Nc3 Nf6 3.e4 c5 
    (Since 4.e5 did not seem to be dangerous.) 
4.g3 
    (There was also to be considered 4. Nf3 Nc6 5. d4 cxd4 6. Nxd4 Bb4 7. Qd3 at Bogolyubov’s suggestion)
4... d5 5. e5d4 6.exf6 dxc3 7.dxc3 
    (An interesting idea. He, so to speak, sacrifices a pawn, in that he makes his pawn majority on the 
    queen side of no value; but he hopes, by occupying certain central points, to be able to bring 
    counter-pressure to bear) 
7... Qxf6 8. Nf3 h6 9.Bg2 Bd7! 10.Nd2 
    (White’s command of the diagonal g2 to b7 coupled with that of the point e4 is no small embarrasment to Black. 
    If now 10...Qe5+? then 11. Ne4 and 12. Bf4.) 
10... Bc6 11. Ne4 Qg6 12. Qe2 Be7 
    (Not 12...f5 because of the reply 13. Bf3 followed by 13... Nd2 and Black’s e5 will remain a weak point.) 
13. O-O O-O 14. h4 
    (An ingenious move, which, however, brings about a disturbance of the equilibrium which up to now may be 
    said to have existed. Better was 14. f4! Nd7 15. Bd2 Kh8! 16. Rae1 Nf6 17 Bc1. After the text move the 
    balance weighs in Black’s favor.) 
14... f5 15. Nd2 Bxg2 
    (Not 15... Bxh4 because of 16. Nf3!) 
16. Kxg2 Nc6 17. Nf3 f4 
    (Otherwise 18... Bf4 and the balance is readjusted.) 
18. Re1 Rf6 19. Qe4 
    (the game is already lost for White, for the occupation of the point e4 which seems to consolidate the 
    position proves to be deceptive. White’s g3 is in fact sick unto death.) 
19... fxg3 20. fxg3 Bd6 21. g4 Qxe4 22. Rxe4 Raf8 23. Re3 Rf4 24. g5 
    (24. Rxe6 Rxg4+ 25. Kf2 Ne5 would lead to a debacle.) 
24... Rg4+ 25. Kh1 
    (Or 25. Kf2 Ne5 26. Ke2 Rg2+ 27. Kf1 Rg3 winning a piece.) 
25... hxg5 26. hxg5 Kf7 27. Ng1 
    (If 27. g6+ Kf6 
        (not 27...Ke7 because of 28. Nh2 Rh8 29. Re2 Rgh4?? 30. Bg5+.)
    ) 
27... Rh8+ 28. Nh3 Ke7 29. b3 Bf4 30. Rf3 Ne5 
    (0-1)
```

Se poate distinge linia principala si, folosind indentarile, se pot distinge variantele si sub variantele.

Cum se poate ajunge la astfel de output? 

In opinia mea a incerca sa scoatem un astfel de output cu comentarii atat de umane este o intreprindere 
foarte complicata. Insa cred ca problema se poate reduce la urmatoarea sub problema:

*Pentru fiecare arbore de intrare avem un numar de mutari in varianta principala sau in subvariante. Pentru
fiecare mutare din arbore avem un drum de la pozitia initiala la acea mutare. Pentru fiecare mutare posibila
putem incerca sa generam un comentariu tinand cont de ceea ce s-a intamplat anterior si de mutarea curenta. 
Daca nu gasim fundament pentru a genera un comentariu la mutarea curenta, pur si simplu nu il facem. Daca
comentam mutarea curenta si la mutarile anterioare nu s-au facut comentarii, putem considera comentariul 
pentru toate mutarile anterioare mutarii curente. Atunci cand generam comentariul pentru mutarea curenta
putem sterge eventual comentarii de la mutarile anterioare (un exemplu ar fi: la mutarea anterioara observam 
ca s-a capturat un pion, comentariul pentru el ar fi sa zicem captura de pion, dar la mutarea curenta 
observam ca s-a facut iar captura de pion, atunci din analiza mutarii curente putem spune ca s-a facut un schimb 
si stergem comentariul de la mutarea anterioara). Apoi nu ramane decat sa le aliniem.* 

### Subproblema curenta

Dandu-se o pozitie initiala si un sir de mutari, cum putem genera comentariul pentru mutarea curenta?

Cred ca rezolvarea acestei probleme in mod direct, prin comentarii, nu este eficienta. Ceea ce propun este 
o rezolvare in trei faze. Astfel, analiza unui arbore cred ca trebuie sa aiba trei mari etape:

* Decorarea arborelui de mutari cu diverse metadate. Metadatele nu sunt inca limbaj uman, ci doar elemente 
pe baza caruia se poate construi un comentariu uman. Metadatele vor fi codificate printr-un string de 
caractere, putem sa le dam si o explicatie eventual, dar asta e numai pentru noi. Exemplu de metadate:

    * Albul da sah
    * Negrul nu mai poate face rocada
    * Albul captureaza un turn
    * Albul transforma pionul in cal
    * Negrul are avantaj masiv
    * Pozitie de egalitate
    * Mutare foarte buna
    * Greseala foarte mare
    
* O a doua analiza a arborelui de mutari astfel decorat unde transformam efectiv metadatele in comentarii. 
Aici practic putem obtine o propozitie dintr-o metadata sau putem obtine o fraza mai complexa din cateva 
metadate. De exemplu din:
    
    * Albul captureaza un cal
    * Albul are avantaj masiv
    
    Putem obtine un comentariu de genul

    * *White captures a knight and get massive advantage*

* Ultima etapa consta din transformarea structurii arborescente cu comentarii intr-un sir de caractere
(text) formatat ca in exemplu de output pe care l-am prezentat. Aceasta etapa este mai mult tehnica.

## Taskuri separate

Din cele pe care le-am prezentat rezulta cateva activitati la care ma gandesc, si la care cred ca
daca definim bine ce intra in task si ce iese, putem sa le executam separat de catre fiecare.

 - mapare spre modelele folosite pentru a lucra
 - identificare subprobleme(mutarea curenta cu ceea ce sa intamplat pana la ea)
 - generare metadate pentru subproblema
 - generare comentariu propriu zis din metadate
 - generarea raspunsului concret in format text(punerea subproblemelor la un loc,identare etc)