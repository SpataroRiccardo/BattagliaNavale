package battaglianavale.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Bean che rappresenta una nave: le celle che occupa sul campo
 * e quanti colpi ha ricevuto finora.
 */
public class Nave {

    private int id;
    private int lunghezza;
    private List<Posizione> celle = new ArrayList<>();
    private int colpiRicevuti = 0;

    public Nave() {
    }

    public Nave(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLunghezza() {
        return lunghezza;
    }

    public void setLunghezza(int lunghezza) {
        this.lunghezza = lunghezza;
    }

    public List<Posizione> getCelle() {
        return celle;
    }

    public void setCelle(List<Posizione> celle) {
        this.celle = celle;
        this.lunghezza = celle.size();
    }

    public void aggiungiCella(Posizione posizione) {
        this.celle.add(posizione);
        this.lunghezza = this.celle.size();
    }

    public int getColpiRicevuti() {
        return colpiRicevuti;
    }

    public void setColpiRicevuti(int colpiRicevuti) {
        this.colpiRicevuti = colpiRicevuti;
    }

    /** Registra un colpo andato a segno su questa nave. */
    public void registraColpo() {
        this.colpiRicevuti++;
    }

    /** Una nave è affondata quando i colpi ricevuti coprono tutta la sua lunghezza. */
    public boolean isAffondata() {
        return colpiRicevuti >= lunghezza;
    }

    /** Verifica se una data coordinata appartiene a questa nave. */
    public boolean contieneCella(int riga, int colonna) {
        for (Posizione p : celle) {
            if (p.getRiga() == riga && p.getColonna() == colonna) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Nave{id=" + id +
                ", lunghezza=" + lunghezza +
                ", colpiRicevuti=" + colpiRicevuti +
                ", affondata=" + isAffondata() +
                ", celle=" + celle +
                '}';
    }
}
