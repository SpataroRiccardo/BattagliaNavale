# Battaglia Navale — Fase 1

Progetto Java che implementa la Fase 1 del gioco Battaglia Navale:
lettura/scrittura del campo su CSV, modello dati a bean, e servizio
per gestire i colpi (con log su console).

## Struttura del progetto

```
battaglia-navale/
├── campo_esempio.csv                # campo 10x10 di esempio (16 navi, nessuna adiacente)
└── src/battaglianavale/
    ├── Main.java                    # demo interattiva da console
    ├── model/
    │   ├── Posizione.java           # bean: coordinata (riga, colonna)
    │   ├── Nave.java                 # bean: nave (celle occupate, colpi ricevuti)
    │   └── Campo.java                # bean: griglia + elenco navi (ricostruite dalla griglia)
    ├── service/
    │   ├── CsvService.java          # 1. lettura CSV -> matrice, e matrice -> CSV
    │   └── BattagliaService.java    # 3. logica del colpo (A->M, N->C, affondamento, log)
    └── util/
        └── CampoGenerator.java      # genera un campo casuale valido (navi non adiacenti)
```

## Legenda celle

| Carattere | Significato                          |
|-----------|---------------------------------------|
| `A`       | Acqua (cella libera, mai colpita)     |
| `N`       | Nave (non ancora colpita)             |
| `C`       | Colpita (nave colpita)                |
| `M`       | Mancato (tiro in acqua)               |

## Flotta

| Tipo         | Lunghezza | Quantità |
|--------------|-----------|----------|
| Portaerei    | 5         | 1        |
| Incrociatore | 4         | 2        |
| Cacciatorpediniere | 3   | 3        |
| Sommergibile | 2         | 5        |
| Vedetta      | 1         | 5        |

Totale: 16 navi, 37 celle occupate. Le navi non si toccano tra loro
(nemmeno in diagonale), per rispettare il vincolo "le navi non devono
essere attaccate [tra loro]".

## Come funziona il modello dati

Il file CSV contiene **solo** la griglia (nessun metadato extra sulle navi).
La classe `Campo`, quando riceve la matrice, **ricostruisce automaticamente**
l'elenco delle navi analizzando la griglia: raggruppa le celle `N`/`C`
adiacenti ortogonalmente (flood fill) in oggetti `Nave`, deducendone
lunghezza e stato. Questo evita di dover mantenere un file separato
sincronizzato con la griglia.

## Compilazione

Richiede JDK 11+ (nessuna libreria esterna).

```bash
cd battaglia-navale
javac -encoding UTF-8 -d bin $(find src -name "*.java")
```

## Esecuzione

```bash
java -cp bin battaglianavale.Main campo_esempio.csv
```

Se il file passato come argomento non esiste, ne viene generato
automaticamente uno nuovo (casuale, con la flotta corretta) e salvato
con quel nome.

Durante l'esecuzione:
1. Il campo viene letto/generato e stampato a video.
2. Il programma chiede coordinate nel formato `riga,colonna` (0-based, es. `3,4`).
3. Per ogni colpo viene stampato l'esito (`MANCATO`, `COLPITO`,
   `NAVE_AFFONDATA`, `VITTORIA`, `GIA_COLPITA`, `FUORI_CAMPO`) e i
   dettagli vengono loggati su console tramite `java.util.logging`.
4. Scrivendo `fine` si esce; lo stato finale del campo viene salvato in
   `campo_finale.csv`.

## Esempio di sessione

```
> 0,3
Esito: COLPITO
> 0,4
Esito: COLPITO
> 0,5
Esito: NAVE_AFFONDATA
> 8,8
Esito: MANCATO
> 0,3
Esito: GIA_COLPITA
> fine
```

## Note implementative

- **CsvService**: legge il file riga per riga, fa lo split su `,` e
  costruisce la matrice `char[][]` (trasposizione richiesta dal punto 1).
- **Nave**: bean con getter/setter standard (`getLunghezza`,
  `getCelle`, `getColpiRicevuti`, ecc.) più i metodi di comodo
  `registraColpo()` e `isAffondata()`.
- **Campo**: bean con getter/setter (`getGriglia`/`setGriglia`,
  `getNavi`/`setNavi`) più i metodi di servizio `trovaNaveInPosizione`
  e `tutteAffondate`.
- **BattagliaService.spara(riga, colonna)**: implementa esattamente la
  logica richiesta (A→M; N→C con verifica affondamento nave e verifica
  vittoria complessiva) e logga ogni passaggio con `java.util.logging.Logger`.
