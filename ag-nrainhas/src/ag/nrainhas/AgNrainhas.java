package ag.nrainhas;
import java.util.Scanner;

/**
 * Algoritmo genético que resolve o problema das N-Rainhas
 *
 * @author Rafael D'Addio
 */
public class AgNrainhas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Tamanho do grafo: ");
        int ngrafo = scanner.nextInt();
        System.out.print("Tamanho da população: ");
        int npop = scanner.nextInt();
        System.out.print("Número de gerações: ");
        int ngen = scanner.nextInt();
        System.out.print("Tamanho do crossover ");
        int ncross = scanner.nextInt();
        System.out.print("Taxa de mutação(0-100): ");
        int taxa = scanner.nextInt();
        AlgoritmoGenetico ag = new AlgoritmoGenetico(ngrafo, npop); //instancia
        ag.executa(ngen, ncross, taxa, "roleta", 1); //executa
    }
    
}