package battaglianavale.service;

import battaglianavale.model.Campo;
import battaglianavale.model.Nave;

import java.util.logging.Logger;

/**
 * Servizio che implementa le regole di gioco: riceve una cella da colpire e
 * applica la logica richiesta, loggando ogni passaggio su console.
 */
public class BattagliaService {

    private static final Logger LOG = Logger.getLogger(BattagliaService.class.getName());

    /** Possibili esiti di un tiro. */
    public enum EsitoTiro {
        MANCATO,          // A -> M
        COLPITO,          // N -> C, nave non ancora affondata
        NAVE_AFFONDATA,   // N -> C, e quella nave risulta affondata
        VITTORIA,         // ultima nave rimasta, tutte affondate
        GIA_COLPITA,      // cella già C o M in precedenza
        FUORI_CAMPO       // coordinate non valide
    }

    private final Campo campo;

    public BattagliaService(Campo campo) {
        this.campo = campo;
    }

    /**
     * Esegue un colpo sulla cella (riga, colonna):
     * a) se contiene A -> diventa M
     * b) se contiene N -> diventa C, si verifica se la nave è affondata
     *    e se tutte le navi sono state affondate.
     * Ogni passo viene loggato su console.
     */
    public EsitoTiro spara(int riga, int colonna) {
        if (riga < 0 || riga >= campo.getRighe() || colonna < 0 || colonna >= campo.getColonne()) {
            LOG.warning(() -> "Coordinate fuori dal campo: (" + riga + "," + colonna + ")");
            return EsitoTiro.FUORI_CAMPO;
        }

        char cella = campo.getCella(riga, colonna);
        LOG.info(() -> "Tiro ricevuto in (" + riga + "," + colonna + ") - stato attuale della cella: '" + cella + "'");

        switch (cella) {
            case Campo.ACQUA:
                campo.setCella(riga, colonna, Campo.MANCATO);
                LOG.info(() -> "Acqua! Colpo a vuoto: cella (" + riga + "," + colonna + ") ora e' 'M' (Mancato)");
                return EsitoTiro.MANCATO;

            case Campo.NAVE: {
                campo.setCella(riga, colonna, Campo.COLPITA);
                Nave nave = campo.trovaNaveInPosizione(riga, colonna);
                if (nave != null) {
                    nave.registraColpo();
                }
                LOG.info(() -> "Colpito! Cella (" + riga + "," + colonna + ") ora e' 'C' (Colpita). Nave interessata: " + nave);

                if (nave != null && nave.isAffondata()) {
                    LOG.info(() -> "Nave affondata! " + nave);
                    if (campo.tutteAffondate()) {
                        LOG.info(() -> "Tutte le navi sono state affondate: VITTORIA!");
                        return EsitoTiro.VITTORIA;
                    }
                    return EsitoTiro.NAVE_AFFONDATA;
                }
                return EsitoTiro.COLPITO;
            }

            case Campo.COLPITA:
            case Campo.MANCATO:
                LOG.warning(() -> "Cella (" + riga + "," + colonna + ") era gia' stata colpita in precedenza ('" + cella + "')");
                return EsitoTiro.GIA_COLPITA;

            default:
                LOG.severe(() -> "Carattere non riconosciuto '" + cella + "' in (" + riga + "," + colonna + ")");
                throw new IllegalStateException("Carattere non valido nel campo: " + cella);
        }
    }
}
