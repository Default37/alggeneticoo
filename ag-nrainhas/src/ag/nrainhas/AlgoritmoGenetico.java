package ag.nrainhas;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

/**
 * Algoritmo genético
 *
 * @author Rafael D'Addio
 */
public class AlgoritmoGenetico {

    private ArrayList<Individuo> populacao;
    private final int tamPopulacao;
    private int aptidaoTotal, aptidaoMaxIndivido;

    /**
     * Constrói um algoritmo genético.
     *
     * @param tamGrafo o tamanho do tabuleiro n x n
     * @param tamPopulacao o número de indivíduos
     */
    public AlgoritmoGenetico(int tamGrafo, int tamPopulacao, int[][] grafo) {
        this.tamPopulacao = tamPopulacao;
        populacao = new ArrayList<>();
        calculaAptidaoMax(grafo);
        geraPopulacaoInicial(tamGrafo, grafo);
    }

    /**
     * Calcula a aptidão máxima que um indivíduo pode atingir. Para cada rainha, o numero máximo de
     * não-colisões que ela pode ter é baseado no número de rainhas a sua direita.
     *
     * @param grafo
     */
    private void calculaAptidaoMax(int[][] grafo) {
        
        int custoTotalGrafo = 0;
        
        for (int i = 0; i < grafo.length; i++){
            for (int j = 0; j < grafo[i].length; j++){
                custoTotalGrafo += grafo[i][j];
            }
        }
        aptidaoMaxIndivido = custoTotalGrafo;
    }

    /**
     * A soma de aptidão de todos os indivíduos da população.
     */
    private void calculaAptidaoTotal() {
        aptidaoTotal = 0;

        for (Individuo i : populacao) {
            aptidaoTotal += i.getAptidao();
        }
    }

    /**
     * Ordena uma população por sua aptidão de maneira decrescente.
     *
     * @param populacao população
     */
    private void ordenaPopulacao(ArrayList<Individuo> populacao) {
        Collections.sort(populacao, new Comparator<Individuo>() {
            @Override
            public int compare(Individuo i1, Individuo i2) {
                return i2.getAptidao() - i1.getAptidao();
            }
        });
    }

    /**
     * Gera a população inicial.
     *
     * @param tamTabuleiro o tamanho do tabuleiro n x n
     */
    private void geraPopulacaoInicial(int tamTabuleiro, int[][] grafo) {

        for (int i = 0; i < tamPopulacao; i++) {
            populacao.add(new Individuo(tamTabuleiro, aptidaoMaxIndivido, grafo));
        }
        ordenaPopulacao(populacao);
        calculaAptidaoTotal();
    }

    /**
     * Executa o algoritmo genético com base nas suas configurações.
     *
     * @param nGeracoes número de gerações máximo que deve ser computado
     * @param corteCrossover ponto de corte para o crossover na geração de novos indivíduos
     * @param taxaMutacao taxa de mutação (0..100)
     * @param metodo método de seleção de indivíduos para crossover -> "roleta" ou "torneio"
     * @param elitismo quantos elementos serão movidos para a próxima geração através do elitismo
     */
    public void executa(int nGeracoes, int corteCrossover, int taxaMutacao, String metodo, int elitismo, int[][] grafo) {
        int i = 0;
        while (i < nGeracoes && populacao.get(0).getAptidao() != aptidaoMaxIndivido) {
            System.out.println("Geração " + i + "\nTamanho população: " + populacao.size() + "\nAptidão média:" + (aptidaoTotal / populacao.size()));
            System.out.println("Melhor indivíduo: ");
            populacao.get(0).exibeIndividuo(grafo);
            System.out.println("--------------------------------\n");
            geraNovaPopulacao(corteCrossover, taxaMutacao, metodo, elitismo, grafo);
            i++;
        }
        System.out.println("Solução vencedora: ");
        populacao.get(0).exibeIndividuo(grafo);
    }

    /**
     * Gera uma nova população e substitui a antiga.
     *
     * @param corteCrossover ponto de corte para o crossover na geração de novos indivíduos
     * @param taxaMutacao taxa de mutação (0..100)
     * @param metodo método de seleção de indivíduos para crossover -> "roleta" ou "torneio"
     * @param elitismo quantos elementos serão movidos para a próxima geração através do elitismo
     */
    private void geraNovaPopulacao(int corteCrossover, int taxaMutacao, String metodo, int elitismo, int[][] grafo) {
        ArrayList<Individuo> novaPopulacao = new ArrayList<>();
        Random r = new Random();

        if (elitismo > 0) { // se houver elitismo, transfere o número designado para a próxima população
            int i = 0;
            while (i < elitismo) {
                novaPopulacao.add(populacao.get(i));
                i++;
            }
        }

        //enquanto a nova população não tiver o tamanho da original 
        //ou for maior (pode ocorrer devido elitismo ou população de tamanho ímpar).
        while (novaPopulacao.size() < populacao.size()) {
            Individuo i1, i2;

            //seleciona dos indivíduos através de roleta ou torneio
            if (metodo.equals("roleta")) {
                i1 = selecionaRoleta();
                i2 = selecionaRoleta();
            } else {
                i1 = selecionaTorneio();
                i2 = selecionaTorneio();
            }

            //cria dois novos indivíduos, e efetua mutação caso necessário
            novaPopulacao.add(new Individuo(i1.getGenotipo(), i2.getGenotipo(), aptidaoMaxIndivido, corteCrossover, grafo));
            if (r.nextInt(100) < taxaMutacao) {
                novaPopulacao.get(novaPopulacao.size() - 1).muta();
            }
            novaPopulacao.add(new Individuo(i2.getGenotipo(), i1.getGenotipo(), aptidaoMaxIndivido, corteCrossover, grafo));
            if (r.nextInt(100) < taxaMutacao) {
                novaPopulacao.get(novaPopulacao.size() - 1).muta();
            }
        }
        ordenaPopulacao(novaPopulacao);

        //se a nova população for maior que a original, remove o(s) mais inapto(s)
        while (novaPopulacao.size() < populacao.size()) {
            novaPopulacao.remove(novaPopulacao.size() - 1);
        }

        populacao = novaPopulacao;
        calculaAptidaoTotal();
    }

    /**
     * Implementa o método da roleta.
     *
     * @return um indivíduo
     */
    private Individuo selecionaRoleta() {
        Random r = new Random();
        int valor = r.nextInt(aptidaoTotal); //escolhe um valor da roleta
        int i;

        //vai reduzindo o valor até achar o indivíduo correspondente
        for (i = 0; i < populacao.size(); i++) {
            valor -= populacao.get(i).getAptidao();
            if (valor <= 0) {
                break;
            }
        }

        return populacao.get(i);
    }

    /**
     * Implementa o método do torneio, com apenas dois indivíduos competindo.
     *
     * @return um indivíduo
     */
    private Individuo selecionaTorneio() {
        Random r = new Random();

        //seleciona dois indivíduos aleatoriamente
        int i = r.nextInt(populacao.size());
        int j = r.nextInt(populacao.size());

        //escolhe qual é o maior e o retorna
        if (populacao.get(i).getAptidao() <= populacao.get(j).getAptidao()) {
            return populacao.get(i);
        } else {
            return populacao.get(j);
        }
    }
}