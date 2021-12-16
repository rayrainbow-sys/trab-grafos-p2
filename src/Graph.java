import java.util.LinkedList;
import java.util.ArrayList;

public class Graph {
    private int nNodes;
    private ArrayList<ArrayList<Integer>> adjMatrix;
    private ArrayList<LinkedList<Integer>> adjList;

    /**
     * Construtor da classe Graph.
     * @param nNodes Número de nós do grafo.
     * @param reprChoice O valor 0 indica escolha pela representação por matriz de
     *                   adjacências; 1 define a opção pela lista de adjacências.
     */
    public Graph(int nNodes, int reprChoice) {
        this.nNodes = nNodes;

        if (reprChoice == 0) {
            this.adjList = null;

            // Inicializa matriz de adjacência de zeros

            throw new UnsupportedOperationException("Ainda nao implementado");

//            for (int i=0; i < nNodes; i++) {
//
//                for (int j=0; j < nNodes; j++) {
//
//                }
//            }

        } else if (reprChoice == 1) {
            this.adjMatrix = null;

            // Inicializa lista de adjacências sem vizinhos
            throw new UnsupportedOperationException("Ainda nao implementado");
        } else {
            throw new IllegalArgumentException("0 para representacao por matriz, 1 para grafo");
        }
    }

    /**
     * Retorna a quantidade de nós do grafo.
     * @return Número de nós.
     */
    public int getNNodes() {
        return nNodes;
    }

    /**
     * Calcula e retorna o grau de um nó, identificado pelo seu índice. O grau é definido
     * como o número de arestas de que um nó participa, ou, equivalentemente, o número de
     * nós ligados a ele.
     *
     * @param node Índice do nó.
     * @return Grau do nó.
     */
    public int getDegree(int node) {
        throw new UnsupportedOperationException("Ainda nao implementado");
//        return -1;
    }

    // max, min, médio, mediano: melhor função ou fazer no main?

    /**
     * Calcula e retorna a distância entre dois nós, definida como o comprimento do caminho
     * mais curto entre eles. O comprimento de um caminho é seu número de arestas.
     * @param node1 Índice de um nó.
     * @param node2 Índice de um nó.
     * @return
     */
    public int calcDistance(int node1, int node2) {
        throw new UnsupportedOperationException("Ainda nao implementado");
//        return -1;
    }

    /**
     * Calcula e retorna o diâmetro do grafo, definido como a maior distância entre dois
     * vértices.
     * @return Diâmetro do grafo.
     */
    public int calcDiameter() {
        throw new UnsupportedOperationException("Ainda nao implementado");
//        return -1;
    }

    /**
     * Implementa a busca em largura.
     * @param origin Índice do vértice a ser usado como origem da busca.
     */
    public void BFS(int origin) {
        // o retorno não deve ser void, ainda não definimos
        throw new UnsupportedOperationException("Ainda nao implementado");
    }

    /**
     * Implementa a busca em profundidade.
     * @param origin Índice do vértice a ser usado como origem da busca.
     */
    public void DFS(int origin) {
        // o retorno não deve ser void, ainda não definimos
        throw new UnsupportedOperationException("Ainda nao implementado");
    }

    /**
     * Determina as componentes conexas do grafo.
     */
    public void findConnectedComponents() {
        // o retorno não deve ser void, ainda não definimos
        throw new UnsupportedOperationException("Ainda nao implementado");
    }

}