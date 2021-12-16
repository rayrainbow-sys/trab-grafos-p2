package main;

// Para representação das adjacências:
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

// Para ler o arquivo de entrada:
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Graph {
    private int nNodes;
    private int nEdges;
    private ArrayList<ArrayList<Integer>> adjMatrix;
    private ArrayList<LinkedList<Integer>> adjList;

    /**
     * Construtor da classe Graph.
     * @param filepath Arquivo de entrada no formato especificado. O caminho pode
     *                 ser especificado de forma absoluta ou relativa, a partir da
     *                 raiz do projeto.
     * @param reprChoice O valor 0 indica escolha pela representação por matriz de
     *                   adjacências; 1 define a opção pela lista de adjacências.
     */
    public Graph(String filepath, int reprChoice) throws InstantiationException {
        if (reprChoice != 1 && reprChoice !=0) {
            System.err.println("Argumento invalido: use 0 para representacao " +
                    "por matriz, 1 para grafo");
            throw new InstantiationException("Arquivo de entrada inexistente; grafo nao instanciado");
        }  // else

        try {
            File inputFile = new File(filepath);
            Scanner inputReader = new Scanner(inputFile);

            this.nNodes = Integer.parseInt(inputReader.nextLine());
            this.nEdges = 0;  // acumulador

            if (reprChoice == 0) {
                this.adjList = null;
                this.adjMatrix = new ArrayList<ArrayList<Integer>>();

                // Inicializa matriz de adjacência com zeros
                for (int i=0; i <= this.getNNodes(); i++) {
                    ArrayList<Integer> row = new ArrayList<Integer>();

                    for (int j = 0; j <= this.getNNodes(); j++) {
                        row.add(0);
                    }

                    this.adjMatrix.add(row);
                }

            } else {
                this.adjMatrix = null;
                this.adjList = new ArrayList<LinkedList<Integer>>();

                // Inicializa lista de adjacências sem vizinhos
                for (int i=0; i <= this.getNNodes(); i++) {
                    LinkedList<Integer> ll = new LinkedList<Integer>();
                    ll.add(i);

                    this.adjList.add(ll);
                }
            }

            // Obs: não sei se é o melhor caminho, mas, como ele indexa a partir
            // do 1, em ambos os casos incluí o índice 0, mas pra deixar "em
            // branco" (sem uso); me pareceu melhor do que usar um HashMap só
            // pra pular os índices 0

            while (inputReader.hasNextLine()) {
                this.nEdges++;

                String link = inputReader.nextLine();
                String[] nodeStrings = link.split(" ");

                int node1 = Integer.parseInt(nodeStrings[0]);
                int node2 = Integer.parseInt(nodeStrings[1]);

                if (reprChoice == 0) {
                    this.adjMatrix.get(node1).set(node2, 1);
                    this.adjMatrix.get(node2).set(node1, 1);
                } else {
                    this.adjList.get(node1).add(node2);
                    this.adjList.get(node2).add(node1);
                }
            }

            inputReader.close();
        } catch (FileNotFoundException exc) {
            System.err.println("Falha na leitura de " + filepath);
            throw new InstantiationException("Arquivo de entrada inexistente; grafo nao instanciado");
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
     * Retorna a quantidade de arestas do grafo.
     * @return Número de arestas.
     */
    public int getNEdges() {
        return nEdges;
    }

    /**
     * Retorna os vizinhos de um nó num formato unificado tanto para a representação por
     * matriz quanto por lista, para possibilitar a composição com outros métodos.
     *
     * (Chegamos a discutir sobre as ineficiências introduzidas por essa abstração, por
     * exigir uma etapa extra de conversão. Só depois percebemos que, como era pedido que
     * medíssemos o tempo separadamente para cara forma de representação, o uso deste método
     * seria incompatível com o trabalho, pois, justamente, eliminaria essas diferenças. Como
     * o método já estava pronto e testado, deixamos aqui caso viesse a ser necessário, mas
     * não o estamos usando na BFS e DFS.)
     *
     * @param node Índice do nó.
     * @return ArrayList de inteiros, com os índices dos vizinhos do nó.
     */
    public ArrayList<Integer> getNeighbors(int node) {
        ArrayList<Integer> neighbors;

        if (this.adjList == null) {
            neighbors = new ArrayList<Integer>();
            ArrayList<Integer> mtxRow = adjMatrix.get(node);

            for (int i=1; i<=nNodes; i++) {
                if (mtxRow.get(i) == 1) {
                    neighbors.add(i);
                }
            }
        } else {
            LinkedList<Integer> ll = adjList.get(node);
            neighbors = new ArrayList<Integer>(ll);
            neighbors.remove(0); // o próprio nó
        }

        return neighbors;
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
        ArrayList<Integer> neighbors = this.getNeighbors(node);
        return neighbors.size();
    }

    /**
     * Fornece alguns dados com caráter de "resumo numérico" sobre os valores
     * dos graus dos nós -- nominalmente, os graus máximo, mínimo, médio e
     * mediano do grafo.
     *
     * @return HashMap em que o índice "min" corresponde ao grau mínimo, "max" ao
     * máximo, "mean" ao grau médio e "med" ao grau mediano.
     */
    public HashMap<String, Integer> getDegreeOverview() {
        HashMap<String, Integer> data = new HashMap<String, Integer>();

//        data.put("")

        return data;
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