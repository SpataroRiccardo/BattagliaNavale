package battaglianavale.util;

import battaglianavale.model.Campo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Genera una griglia 10x10 con la flotta richiesta, posizionata in modo
 * casuale e senza che le navi si tocchino tra loro (nemmeno in diagonale).
 *
 * Flotta:
 *  1 nave  x 5
 *  2 navi  x 4
 *  3 navi  x 3
 *  5 navi  x 2
 *  5 navi  x 1
 */
public class CampoGenerator {

    private static final int DIMENSIONE = 10;
    private final Random random = new Random();

    public char[][] generaCampo() {
        // Il posizionamento è greedy/casuale: può occasionalmente bloccarsi
        // (soprattutto verso la fine, con le navi più piccole). In tal caso
        // si ricomincia da capo la generazione, invece di propagare l'errore.
        while (true) {
            try {
                return tentaGenerazione();
            } catch (IllegalStateException e) {
                // riprova con una nuova disposizione casuale
            }
        }
    }

    private char[][] tentaGenerazione() {
        char[][] griglia = new char[DIMENSIONE][DIMENSIONE];
        for (char[] riga : griglia) {
            Arrays.fill(riga, Campo.ACQUA);
        }

        List<Integer> lunghezzeFlotta = new ArrayList<>();
        lunghezzeFlotta.add(5);
        for (int i = 0; i < 2; i++) lunghezzeFlotta.add(4);
        for (int i = 0; i < 3; i++) lunghezzeFlotta.add(3);
        for (int i = 0; i < 5; i++) lunghezzeFlotta.add(2);
        for (int i = 0; i < 5; i++) lunghezzeFlotta.add(1);

        for (int lunghezza : lunghezzeFlotta) {
            posizionaNave(griglia, lunghezza);
        }
        return griglia;
    }

    private void posizionaNave(char[][] griglia, int lunghezza) {
        int tentativi = 0;
        while (tentativi < 2000) {
            tentativi++;
            boolean orizzontale = random.nextBoolean();
            int riga = random.nextInt(DIMENSIONE);
            int colonna = random.nextInt(DIMENSIONE);

            if (puoPosizionare(griglia, riga, colonna, lunghezza, orizzontale)) {
                for (int i = 0; i < lunghezza; i++) {
                    int r = orizzontale ? riga : riga + i;
                    int c = orizzontale ? colonna + i : colonna;
                    griglia[r][c] = Campo.NAVE;
                }
                return;
            }
        }
        throw new IllegalStateException("Impossibile posizionare una nave di lunghezza " + lunghezza
                + ": si ricomincia la generazione del campo.");
    }

    /** Verifica che la nave stia dentro la griglia e non tocchi altre navi (nemmeno in diagonale). */
    private boolean puoPosizionare(char[][] griglia, int riga, int colonna, int lunghezza, boolean orizzontale) {
        for (int i = 0; i < lunghezza; i++) {
            int r = orizzontale ? riga : riga + i;
            int c = orizzontale ? colonna + i : colonna;

            if (r < 0 || r >= DIMENSIONE || c < 0 || c >= DIMENSIONE) {
                return false;
            }
            if (griglia[r][c] != Campo.ACQUA) {
                return false;
            }
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int nr = r + dr;
                    int nc = c + dc;
                    if (nr >= 0 && nr < DIMENSIONE && nc >= 0 && nc < DIMENSIONE
                            && griglia[nr][nc] == Campo.NAVE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
