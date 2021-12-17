package main;

// Para representação das adjacências:
import java.lang.reflect.Array;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collections;

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
    public HashMap<String, Double> getDegreeOverview() {
        HashMap<String, Double> data = new HashMap<String, Double>();

        data.put("max", 0.0);
        data.put("min", ((double) this.getNNodes()) + 1.0);  // impossível ser >=
        data.put("mean",
                2.0 * ((double) this.getNEdges()) / ((double) this.getNNodes()));

        ArrayList<Double> degrees = new ArrayList<Double>();

        degrees.add(0.0);  // ignorando o índice 0

        int n = this.getNNodes();

        for (int i=1; i <= n; i++) {
            double deg = (double) this.getDegree(i);
            degrees.add(deg);

            if (deg > data.get("max")) {
                data.put("max", deg);
            } else if (deg < data.get("min")) {
                data.put("min", deg);
            }
        }

        Collections.sort(degrees);  // sorts in place

        if (n % 2 == 0) {
            data.put("med", (degrees.get(n /2) + degrees.get(n /2 + 1)) / 2);
        } else {
            data.put("med", degrees.get(n /2 + 1));
        }

        return data;
    }

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
     * Implementa a busca em largura para representação do grafo por lista de adjacência.
     * Retorna uma ArrayList dos vertices marcados a partir do vertice de origem,
     * ou seja, a componente conexa a qual o vertice de origem pertence.
     * @param origin Índice do vértice a ser usado como origem da busca.
     */
    public ArrayList<Integer> BFSList(int origin) {
        //Array booleano com a marcacao dos vertices
        //Todos os vertices sao desmarcados a principio
        boolean visited[] = new Boolean[nNodes];
        Arrays.fill(visited, false);

        LinkedList<Integer> queue = new LinkedList();

        visited[origin] = true;   //Marcamos o vertice origem
        queue.add(origin);        //e o adicionamos na fila

        while (queue.size() != 0) {
            int v = queue.remove();

            Iterator<Integer> iter = adjList.get(v).listIterator();

            while (iter.hasNext()) {
                int w = iter.next();

                if (!visited[w]) {
                    visited[w] = true;
                    queue.add(w);
                }
            }
        }
        ArrayList<Integer> connectedToOrigin = new ArrayList<Integer>();
        for (int i = 0; i < visited.length(); i++) {
            if (visited[i] == true) connectedToOrigin.add(i);
        }
        return connectedToOrigin;
    }
    
    /**
     * Implemente a busca em largura para representacao do grafo em
     * matriz de adjacencia.
     * Retorna a componente conexa a qual o vertice de origem pertence.
     * @param origin
     */    
    public ArrayList<Integer> BFSMatrix(int origin) {
        // o retorno não deve ser void, aind a não definimos
        //throw new UnsupportedOperationException("Ainda nao implementado");

        //Array booleano com a marcacao dos vertices
        //Todos os vertices sao desmarcados a principio
        boolean visited[] = new Boolean[nNodes];
        Arrays.fill(visited, false);

        LinkedList<Integer> queue = new LinkedList();

        //Marcamos o vertice origem e o adicionamos à fila
        queue.add(origin);
        visited[origin] = true;

        ArrayList<Integer> mtxVertexRow = adjMatrix.get(origin);

        while (queue.size() != 0) {
            int v = queue.remove();

            Iterator<Integer> iter = mtxVertexRow.iterator();

            while (iter.hasNext()) {
                int w = iter.next();

                if (w == 1) {
                    if (!visited[w]) {
                        visited[w] = true;
                        queue.add(w);
                    }
                }
            }
        }
        ArrayList<Integer> connectedToOrigin = new ArrayList<Integer>();
        for (int i = 0; i < visited.length(); i++) {
            if (visited[i] == true) connectedToOrigin.add(i);
        }
        return connectedToOrigin;
    }
    
    public Arraylist<Integer> DFSList(int origin) {
        //Array booleano com a marcacao dos vertices
        //Todos os vertices sao desmarcados a principio
        boolean visited[] = new Boolean[nNodes];
        Arrays.fill(visited, false);

        //Marcamos o vertice de origem
        visited[origin] = true;

        Iterator<Integer> iter = adjList.get(origin).listIterator();

        while (iter.hasNext()) {
            int w = iter.next();
            if (!visited[w]) {
                DFSList(w);
            }
        }

        ArrayList<Integer> connectedToOrigin = new ArrayList<Integer>();
        for (int i = 0; i < visited.length(); i++) {
            if (visited[i] == true) connectedToOrigin.add(i);
        }
        return connectedToOrigin;
    }

    /**
     * Implementa a busca em profundidade.
     * @param origin Índice do vértice a ser usado como origem da busca.
     */
    public Arraylist<Integer> DFSMatrix(int origin) {
        //Array booleano com a marcacao dos vertices
        //Todos os vertices sao desmarcados a principio
        boolean visited[] = new Boolean[nNodes];
        Arrays.fill(visited, false);

        //Marcamos o vertice de origem
        visited[origin] = true;

        ArrayList<Integer> mtxVertexRow = adjMatrix.get(origin);
        Iterator<Integer> iter = mtxVertexRow.iterator();

        while (iter.hasNext()) {
            int w = iter.next();
            if (!visited[w]) {
                DFSList(w);
            }
        }

        ArrayList<Integer> connectedToOrigin = new ArrayList<Integer>();
        for (int i = 0; i < visited.length(); i++) {
            if (visited[i] == true) connectedToOrigin.add(i);
        }
        return connectedToOrigin;
    }

    /**
     * Determina as componentes conexas do grafo.
     */
    public void findConnectedComponents() {
        // o retorno não deve ser void, ainda não definimos
        throw new UnsupportedOperationException("Ainda nao implementado");
    }

}
