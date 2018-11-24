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
  "initialPositionFEN": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
  "variants" : [
    [ {"move":"1. e2e4","score":0.01}, {"move":"1... e7e5","score":0.005} ],
    [ {"move":"1. d2d4","score":0.01}, {"move":"1... g8f6","score":0.0},{"move":"2. c2c4","score":0.01} ]
  ] 
}
```

Un joc cu mutari alternative:

```json
{
  "initialPositionFEN": "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1",
  "mainVariant": [
    {
      "move":"1. e2e4","score":0.01,
      "variants": [
        [ {"move":"1. d2d4","score":0.01}, {"move":"1... g8f6","score":0.0} ],
        [ {"move":"1. c2c4","score":0}, {"move":"1... g8f6","score":0.01} ]
      ]
    },
    {
      "move":"1... e7e5","score":0.01,
      "variants": [
        [ {"move":"1... f7f5","score":0.1} ],
        [ {"move":"1... c7c6","score":0.01},{"move":"2. d2d4","score":0.01} ]
      ]
    }
  ]
}
```

cele de mai sus sunt exemple, dar cam asa le vad eu.