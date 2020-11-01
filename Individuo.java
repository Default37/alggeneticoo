package alggeneticonrainhas;

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

    private int aptidao; //função de aptidão -> numero de não-colisões

    /**
     * Constroi um individuo para a população inicial
     *
     * @param n tamanho do genótipo
     * @param aptidaoMax valor máximo de não-colisões para determinado tamanho de tabuleiro
     */
    public Individuo(int n, int aptidaoMax) {
        genotipo = new int[n];
        geraGenotipoAleatorio();
        calcAptidao(aptidaoMax);

    }

    /**
     * Constroi um individuo a partir de um pai e uma mãe
     *
     * @param pai o genótipo do pai
     * @param mae o genótipo da mãe
     * @param aptidaoMax valor máximo de não-colisões para determinado tamanho de tabuleiro
     * @param corteCrossover onde fazer o corte para mistura de genótipos
     */
    public Individuo(int[] pai, int[] mae, int aptidaoMax, int corteCrossover) {
        genotipo = new int[pai.length];
        crossOver(pai, mae, corteCrossover);
        calcAptidao(aptidaoMax);
    }

    /**
     * Constroi um genótipo aleatório para um indivíduo.
     *
     */
    private void geraGenotipoAleatorio() {
        Random r = new Random();
        for (int i = 0; i < getGenotipo().length; i++) { //para cada coluna do tabuleiro
            //gera um valor aleatório correspondente à linha onde a rainha esta
            genotipo[i] = r.nextInt(getGenotipo().length);
        }
    }

    /**
     * Calcula a aptidão do indivíduo. A aptidão é uma função referente à quantidade de pares de
     * rainhas que não se colidem no tabuleiro.
     *
     * @param aptidaoMax valor máximo de não-colisões para determinado tamanho de tabuleiro
     */
    private void calcAptidao(int aptidaoMax) {
        int colisoes = 0;
        for (int i = 0; i < (getGenotipo().length - 1); i++) { //para cada rainha
            int passo = 1;
            for (int j = (i + 1); j < getGenotipo().length; j++) { // para cada rainha à esquerda da rainha atual
                if (getGenotipo()[j] == getGenotipo()[i]) { //colisão na linha
                    colisoes++;
                } else if (getGenotipo()[j] == getGenotipo()[i] + passo) { //colisão na diagonal superior
                    colisoes++;
                } else if (getGenotipo()[j] == getGenotipo()[i] - passo) { //colisão na diagonal inferior
                    colisoes++;
                }
                passo++;
            }
        }
        aptidao = aptidaoMax - colisoes;
    }

    /**
     * Realiza o crossover entre pai e mae produzindo um filho
     *
     * @param pai o genótipo do pai
     * @param mae o genótipo da mãe
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

    /**
     * Exibe o indivíduo.
     */
    public void exibeIndividuo() {
        int[][] m = new int[getGenotipo().length][getGenotipo().length];
        System.out.print("Genótipo: ");
        for (int i = 0; i < getGenotipo().length; i++) {
            m[getGenotipo()[i]][i] = 1;
            System.out.print(getGenotipo()[i] + " ");
        }
        System.out.println("\nFenótipo: ");
        for (int[] linha : m) {
            System.out.println(Arrays.toString(linha));
        }

        System.out.println("Aptidão = " + getAptidao());
    }

    /**
     * @return a aptidao
     */
    public int getAptidao() {
        return aptidao;
    }

    /**
     * @return o genotipo
     */
    public int[] getGenotipo() {
        return genotipo;
    }

}