package battaglianavale.model;

/**
 * Bean che rappresenta una singola coordinata (riga, colonna) del campo di battaglia.
 */
public class Posizione {

    private int riga;
    private int colonna;

    public Posizione() {
    }

    public Posizione(int riga, int colonna) {
        this.riga = riga;
        this.colonna = colonna;
    }

    public int getRiga() {
        return riga;
    }

    public void setRiga(int riga) {
        this.riga = riga;
    }

    public int getColonna() {
        return colonna;
    }

    public void setColonna(int colonna) {
        this.colonna = colonna;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Posizione)) return false;
        Posizione that = (Posizione) o;
        return riga == that.riga && colonna == that.colonna;
    }

    @Override
    public int hashCode() {
        return 31 * riga + colonna;
    }

    @Override
    public String toString() {
        return "(" + riga + "," + colonna + ")";
    }
}
