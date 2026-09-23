package battaglianavale.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servizio responsabile della lettura di un file CSV e della sua
 * trasposizione in matrice (char[][]), e viceversa.
 */
public class CsvService {

    private static final String DELIMITATORE = ",";

    /**
     * Legge un file CSV e lo trasforma in una matrice di caratteri.
     * Ogni cella del CSV deve contenere un singolo carattere (A, N, C oppure M).
     */
    public char[][] leggiCampo(String percorso) throws IOException {
        List<char[]> righe = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(percorso))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.isBlank()) {
                    continue;
                }
                String[] token = linea.split(DELIMITATORE);
                char[] riga = new char[token.length];
                for (int i = 0; i < token.length; i++) {
                    riga[i] = token[i].trim().toUpperCase().charAt(0);
                }
                righe.add(riga);
            }
        }
        return righe.toArray(new char[0][]);
    }

    /** Scrive una matrice di caratteri su file CSV. */
    public void scriviCampo(String percorso, char[][] griglia) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(percorso))) {
            for (char[] riga : griglia) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < riga.length; i++) {
                    sb.append(riga[i]);
                    if (i < riga.length - 1) {
                        sb.append(DELIMITATORE);
                    }
                }
                pw.println(sb);
            }
        }
    }
}
