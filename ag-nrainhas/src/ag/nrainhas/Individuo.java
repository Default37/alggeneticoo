package ag.nrainhas;

import java.util.Arrays;
import java.util.Random;

/**
 * Um indivíduo que representa um estado final do tabuleiro (todas as rainhas posicionadas)
 *
 * @author Rafael D'Addio
 */
public class Individuo {

    //representação decimal -> 0..n colunas com valores correspondentes a 
    //posição da rainha em uma das 0..n linhas
    private int[] genotipo;

    private int aptidao; //função de aptidão -> número de não-colisões

    /**
     * Constrói um indivíduo para a população inicial
     *
     * @param n tamanho do genótipo
     * @param aptidaoMax valor máximo de não-colisões para determinado tamanho de tabuleiro
     */
    public Individuo(int n, int aptidaoMax, int[][] grafo) {
        aptidao = 0;
        genotipo = new int[n];
        geraGenotipoAleatorio();
        calcAptidao(grafo);
    }

    /**
     * Constrói um indivíduo a partir de um pai e uma mãe
     *
     * @param pai genótipo do pai
     * @param mae genótipo da mãe
     * @param aptidaoMax valor máximo de não-colisões para determinado tamanho de tabuleiro
     * @param corteCrossover onde fazer o corte para mistura de genótipos
     */
    public Individuo(int[] pai, int[] mae, int aptidaoMax, int corteCrossover, int[][] grafo) {
        aptidao = 0;
        genotipo = new int[pai.length];
        crossOver(pai, mae, corteCrossover);
        calcAptidao(grafo);
    }

    /**
     * Constrói um genótipo aleatório para um indivíduo.
     *
     */
    private void geraGenotipoAleatorio() {
        Random r = new Random();
        for (int i = 0; i < getGenotipo().length; i++) { //para cada coluna do tabuleiro
            //gera um valor aleatório correspondente à linha onde a rainha está
            genotipo[i] = r.nextInt(getGenotipo().length);
        }
    }

    /**
     * Calcula a aptidão do indivíduo. A aptidão é uma função referente à quantidade de pares de
     * rainhas que não se colidem no tabuleiro.
     *
     * @param aptidaoMax valor máximo de não-colisões para determinado tamanho de tabuleiro
     */
    private void calcAptidao(int[][] grafo) {
        
        for (int i = 0; i < getGenotipo().length; i++){
            
            if (grafo[i][getGenotipo()[i]] == 0){
                i = getGenotipo().length;
                aptidao = Integer.MAX_VALUE;
            } else {
                aptidao += grafo[i][getGenotipo()[i]];
            }
        }
    }

    /**
     * Realiza o crossover entre pai e mae produzindo um filho
     *
     * @param pai genótipo do pai
     * @param mae genótipo da mãe
     * @param corteCrossover onde fazer o corte para mistura de genótipos
     */
    private void crossOver(int[] pai, int[] mae, int corteCrossover) {
        System.arraycopy(pai, 0, genotipo, 0, corteCrossover); //copia a parte inicial do pai
        System.arraycopy(mae, corteCrossover, genotipo, corteCrossover, genotipo.length - corteCrossover); //copia a parte final da mãe
    }

    /**
     * Realiza a mutação da posição de uma rainha em uma coluna aleatória.
     */
    public void muta() {
        Random r = new Random();

        genotipo[r.nextInt(genotipo.length)] = r.nextInt(genotipo.length);
    }

    public void exibeIndividuo(int[][] grafo) {
        int[][] m = new int[getGenotipo().length][getGenotipo().length];
        
        for (int[] m1 : m) {
            for (int j = 0; j < m1.length; j++) {
                m1[j] = -1; // conferir se ta certo
            }
        }
        
        System.out.print("Genótipo: ");
        for (int i = 0; i < getGenotipo().length; i++) {
            m[i][getGenotipo()[i]] = grafo[i][getGenotipo()[i]];
            System.out.print(getGenotipo()[i] + " ");
        }
        System.out.println("\nFenótipo: ");
        for (int[] linha : m) {
            System.out.println(Arrays.toString(linha));
        }

        System.out.println("Aptidão = " + getAptidao());
    }

    public int getAptidao() {
        return aptidao;
    }

    public int[] getGenotipo() {
        return genotipo;
    }

}