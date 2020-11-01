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
        System.out.print("Quantidade de vértices: ");
        int ngrafo = scanner.nextInt();
        int[][] comp = new int[ngrafo][ngrafo];
        int i = 0;
        int j = 0;

        System.out.print("Tamanho da população: ");
        int npop = scanner.nextInt();

        System.out.print("Número de gerações: ");
        int ngen = scanner.nextInt();

        System.out.print("Tamanho do crossover ");
        int ncross = scanner.nextInt();

        System.out.print("Taxa de mutação(0-100): ");
        int taxa = scanner.nextInt();

        for (i = 0; i < ngrafo; i++) 
        {
            for (j = 0; j < ngrafo; j++)
            {
                System.out.println("Elemento [" + i + "][" + j +"] do grafo: ");
                comp[i][j] = scanner.nextInt();
            }
        }

        AlgoritmoGenetico ag = new AlgoritmoGenetico(ngrafo, npop); //instancia
        ag.executa(ngen, ncross, taxa, "roleta", 1); //executa
    }
    
}