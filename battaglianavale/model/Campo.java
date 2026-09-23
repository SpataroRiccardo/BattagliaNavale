package battaglianavale.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Bean che rappresenta il campo di battaglia: la griglia di caratteri
 * e l'elenco delle navi presenti su di essa.
 *
 * Legenda celle:
 *  A = Acqua
 *  N = Nave
 *  C = Colpita
 *  M = Mancato (tiro in acqua)
 */
public class Campo {

    public static final char ACQUA = 'A';
    public static final char NAVE = 'N';
    public static final char COLPITA = 'C';
    public static final char MANCATO = 'M';

    private char[][] griglia;
    private List<Nave> navi = new ArrayList<>();

    public Campo(char[][] griglia) {
        setGriglia(griglia);
    }

    public char[][] getGriglia() {
        return griglia;
    }

    /** Impostando una nuova griglia, le navi vengono ricostruite automaticamente. */
    public void setGriglia(char[][] griglia) {
        this.griglia = griglia;
        this.navi = identificaNavi();
    }

    public int getRighe() {
        return griglia.length;
    }

    public int getColonne() {
        return griglia.length == 0 ? 0 : griglia[0].length;
    }

    public List<Nave> getNavi() {
        return navi;
    }

    public void setNavi(List<Nave> navi) {
        this.navi = navi;
    }

    public char getCella(int riga, int colonna) {
        return griglia[riga][colonna];
    }

    public void setCella(int riga, int colonna, char valore) {
        griglia[riga][colonna] = valore;
    }

    /** Restituisce la nave a cui appartiene una certa coordinata, o null se non esiste. */
    public Nave trovaNaveInPosizione(int riga, int colonna) {
        for (Nave nave : navi) {
            if (nave.contieneCella(riga, colonna)) {
                return nave;
            }
        }
        return null;
    }

    /** True se tutte le navi del campo risultano affondate. */
    public boolean tutteAffondate() {
        for (Nave nave : navi) {
            if (!nave.isAffondata()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Ricostruisce l'elenco delle navi analizzando la griglia: raggruppa le celle
     * 'N' (nave) o 'C' (già colpita) adiacenti ortogonalmente in un'unica nave,
     * tramite una visita in ampiezza (flood fill).
     */
    private List<Nave> identificaNavi() {
        List<Nave> risultato = new ArrayList<>();
        boolean[][] visitato = new boolean[getRighe()][getColonne()];
        int idCounter = 1;

        int[][] direzioni = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int r = 0; r < getRighe(); r++) {
            for (int c = 0; c < getColonne(); c++) {
                char valore = griglia[r][c];
                if ((valore == NAVE || valore == COLPITA) && !visitato[r][c]) {
                    Nave nave = new Nave(idCounter++);
                    Deque<int[]> pila = new ArrayDeque<>();
                    pila.push(new int[]{r, c});
                    visitato[r][c] = true;

                    while (!pila.isEmpty()) {
                        int[] attuale = pila.pop();
                        int cr = attuale[0];
                        int cc = attuale[1];

                        nave.aggiungiCella(new Posizione(cr, cc));
                        if (griglia[cr][cc] == COLPITA) {
                            nave.registraColpo();
                        }

                        for (int[] d : direzioni) {
                            int nr = cr + d[0];
                            int nc = cc + d[1];
                            if (nr >= 0 && nr < getRighe() && nc >= 0 && nc < getColonne() && !visitato[nr][nc]) {
                                char vicino = griglia[nr][nc];
                                if (vicino == NAVE || vicino == COLPITA) {
                                    visitato[nr][nc] = true;
                                    pila.push(new int[]{nr, nc});
                                }
                            }
                        }
                    }
                    risultato.add(nave);
                }
            }
        }
        return risultato;
    }
}
