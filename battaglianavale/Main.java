package battaglianavale;

import battaglianavale.model.Campo;
import battaglianavale.model.Nave;
import battaglianavale.service.BattagliaService;
import battaglianavale.service.CsvService;
import battaglianavale.util.CampoGenerator;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Programma dimostrativo Fase 1:
 *  1) legge (o genera se non esiste) il campo da file CSV e lo trasforma in matrice
 *  2) costruisce il modello dati (Campo + Navi)
 *  3) permette di sparare colpi da console, mostrando log ed esiti
 */
public class Main {

    public static void main(String[] args) throws IOException {
        String percorsoCsv = args.length > 0 ? args[0] : "campo_esempio.csv";
        CsvService csvService = new CsvService();

        File file = new File(percorsoCsv);
        if (!file.exists()) {
            System.out.println("File '" + percorsoCsv + "' non trovato: genero un nuovo campo casuale...");
            char[][] nuovaGriglia = new CampoGenerator().generaCampo();
            csvService.scriviCampo(percorsoCsv, nuovaGriglia);
        }

        // 1. Lettura del CSV e trasposizione in matrice
        char[][] griglia = csvService.leggiCampo(percorsoCsv);

        // 2. Costruzione del modello dati (le navi vengono ricostruite analizzando la griglia)
        Campo campo = new Campo(griglia);
        System.out.println("Campo caricato da: " + percorsoCsv);
        System.out.println("Navi rilevate: " + campo.getNavi().size());
        for (Nave nave : campo.getNavi()) {
            System.out.println("  " + nave);
        }

        // 3. Servizio di gioco
        BattagliaService battaglia = new BattagliaService(campo);

        stampaCampo(campo);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Inserisci le coordinate da colpire nel formato 'riga,colonna' (es. 3,4).");
        System.out.println("Scrivi 'fine' per uscire.");

        while (true) {
            System.out.print("> ");
            if (!scanner.hasNextLine()) {
                break;
            }
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("fine")) {
                break;
            }

            String[] parti = input.split(",");
            if (parti.length != 2) {
                System.out.println("Formato non valido. Usa: riga,colonna (es. 2,5)");
                continue;
            }

            try {
                int r = Integer.parseInt(parti[0].trim());
                int c = Integer.parseInt(parti[1].trim());

                BattagliaService.EsitoTiro esito = battaglia.spara(r, c);
                System.out.println("Esito: " + esito);
                stampaCampo(campo);

                if (esito == BattagliaService.EsitoTiro.VITTORIA) {
                    System.out.println("Complimenti, hai affondato tutte le navi!");
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Coordinate non numeriche.");
            }
        }

        csvService.scriviCampo("campo_finale.csv", campo.getGriglia());
        System.out.println("Stato finale del campo salvato in campo_finale.csv");
    }

    private static void stampaCampo(Campo campo) {
        System.out.println();
        for (int r = 0; r < campo.getRighe(); r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < campo.getColonne(); c++) {
                sb.append(campo.getCella(r, c)).append(' ');
            }
            System.out.println(sb);
        }
        System.out.println();
    }
}
