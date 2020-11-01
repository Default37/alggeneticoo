package ag.nrainhas;

/**
 * Algoritmo genético que resolve o problema das N-Rainhas
 *
 * @author Rafael D'Addio
 */
public class AgNrainhas {

    public static void main(String[] args) {
        AlgoritmoGenetico ag = new AlgoritmoGenetico(8, 1000); //instancia
        ag.executa(100, 4, 15, "roleta", 1); //executa
    }
    
}
