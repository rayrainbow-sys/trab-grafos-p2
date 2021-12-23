package main;

// Estruturas de dados etc.:
import java.lang.reflect.Array;
import java.util.*;
import java.lang.Math;

// Leitura e escrita de arquivos:
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class Graph {
    /**
     * Nome do arquivo de origem, sem caminho e sem extensão.
     */
    private String inputFile;
    private int nNodes;
    private int nEdges;
    private ArrayList<ArrayList<Boolean>> adjMatrix;
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
                    "por matriz, 1 para lista");
            throw new InstantiationException("Arquivo de entrada inexistente; grafo nao instanciado");
        }  // else

        try {
            inputFile = filepath.substring(filepath.lastIndexOf("/") + 1);
            inputFile = inputFile.substring(0, inputFile.lastIndexOf("."));

            File inputFile = new File(filepath);
            Scanner inputReader = new Scanner(inputFile);

            this.nNodes = Integer.parseInt(inputReader.nextLine());
            this.nEdges = 0;  // acumulador

            if (reprChoice == 0) {
                this.adjList = null;
                this.adjMatrix = new ArrayList<ArrayList<Boolean>>();

                // Inicializa matriz de adjacência com zeros
                for (int i=0; i <= this.getNNodes(); i++) {
                    ArrayList<Boolean> row = new ArrayList<Boolean>();

                    for (int j = 0; j <= this.getNNodes(); j++) {
                        row.add(false);
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
                    this.adjMatrix.get(node1).set(node2, true);
                    this.adjMatrix.get(node2).set(node1, true);
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
     * Retorna o nome do arquivo de texto que deu origem ao grafo.
     * @return Nome do arquivo (com caminho absoluto ou a partir da raiz do
     * diretório de trabalho, conforme tenha sido fornecido).
     */
    public String getInputFile() {
        return inputFile;
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
            ArrayList<Boolean> mtxRow = adjMatrix.get(node);

            for (int i=1; i<=nNodes; i++) {
                if (mtxRow.get(i)) {
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

        data.put("mean",
                2.0 * ((double) this.getNEdges()) / ((double) this.getNNodes()));

        ArrayList<Double> degrees = new ArrayList<Double>();

        degrees.add(0.0);  // ignorando o índice 0

        int n = this.getNNodes();

        for (int i=1; i <= n; i++) {
            double deg = (double) this.getDegree(i);
            degrees.add(deg);
        }

        Collections.sort(degrees);  // sorts in place

        data.put("max", degrees.get(n));
        data.put("min", degrees.get(1));  // pula o 0

        if (n % 2 == 0) {
            data.put("med", (degrees.get(n/2) + degrees.get(n/2 + 1)) / 2);
        } else {
            data.put("med", degrees.get(n/2 + 1));
        }

        return data;
    }

    /**
     * Calcula e retorna a distância entre dois nós, definida como o comprimento do caminho
     * mais curto entre eles. O comprimento de um caminho é seu número de arestas.
     * @param node1 Índice de um nó.
     * @param node2 Índice de um nó.
     * @return Distância entre node1 e node2; retorna -1 se esta for infinita.
     */
    public int calcDistance(int node1, int node2) {
        HashMap<Integer, Integer[]> spanningTree = this.BFS(node1, node2);
        Integer[] node2Value = spanningTree.get(node2);

        if (node2Value == null) {
            return -1;  // não conectado; grau seria infinito
        }

        return node2Value[1];  // nível na árvore geradora
    }

    /**
     * Seleciona uma amostra randomizada, a partir de um dado grafo, que terá uma quantidade
     * k de vértices.
     * @return
     */
    public Integer[] getRandomSample() {
        int k = (int) (Math.log(this.getNNodes()) / Math.log(2)); //log_2 do número de vértices do grafo
        ArrayList<Integer> allVertices = new ArrayList<Integer>();
        Integer[] randomSample = (Integer[]) Array.newInstance(Integer.class.getComponentType(), k);
        Random r = new Random();

        for (int i = 1; i <= this.getNNodes(); i++) {
            allVertices.add(i);
        }

        int kPicked = 0, j = 0, kLeft = this.getNNodes();

        /* Queremos que, em cada posição do array de amostra randomizada, a probabilidade
        * de um vértice do total de vértices ser selecionado seja o número de vértices que
        * ainda faltam para completar nossa amostra sobre o número de vértices que ainda
        * não foram escolhidos. Por exemplo, se queremos escolher 3 vértices aleatórios
        * de um total de 15 vértices, a probabilidade de um vértice qualquer ser escolhido
        * para a primeira posição (índice 0) deve ser 3/15. Na próxima posição (índice 1)
        * um vértice que ainda não foi escolhido teria 2/14 de probabilidade e assim
        * por diante. */
        while (k > 0) {
            //Escolhe um número aleatório entre 0 e kLeft;
            int rand = r.nextInt(kLeft);

            if (rand < k) {
                randomSample[kPicked++] = allVertices.get(j);
                k--;
            }
            kLeft--;
            j++;
        }
        return randomSample;
    }

    /**
     * Calcula e retorna o diâmetro do grafo, definido como a maior distância entre dois
     * vértices.
     * @return Diâmetro do grafo; se o grafo for desconexo, retorna o maior diâmetro dentre
     * as suas componentes.
     */
    public int calcDiameter() {
        int n = this.getNNodes();
        int maxDist = 0;
        //int nMAX = 1000000;

        // neste caso não precisaríamos pular o índice zero, porque só
        // queremos saber _qual_ a "maior menor" distância, e não a que par
        // de vértices ela está associada, mas usei n + 1 mesmo assim por
        // clareza, só para lidar sempre com os mesmos índices

        /*if (n > nMAX) {
            Integer[] chosenOnes = this.getRandomSample();
            Collections.sort(chosenOnes);

            for (int i = 1; i <= chosenOnes)
        }*/

        for (int i=1; i <= n; i++) {
            HashMap<Integer, Integer[]> bfsTree = this.BFS(i);
            int j = i + 1;

            // se j = i, a dist é 0 mesmo, então pode pular
            // se j < i, já verificou em iteração anterior
            // (análogo a estar considerando apenas as entradas acima da
            // diag principal numa matriz de distâncias entre nós)
            while (j <= n) {
                //Se a componente conexa do vértice então analisado não contém
                //o vértice até o qual estamos tentando calcular o menor caminho,
                //pulamos para o próximo.
                if (!bfsTree.containsKey(j)) {
                    j++;
                } else {

                    if (bfsTree.get(j)[1] > maxDist) {
                        maxDist = bfsTree.get(j)[1];
                    }
                }
                j++;
            }
        }
        return maxDist;
    }

    /**
     * Implementa a busca em largura a partir do vértice de origem
     * especificado, retornando sua árvore geradora. Interrompe a BFS ao
     * chegar ao vértice-alvo fornecido, caso este exista e esteja ligado
     * à origem por algum caminho.
     * @param origin Índice do vértice a ser usado como origem da busca.
     * @param goal Índice do vértice buscado.
     * @return HashMap cujas chaves são os índices dos nós presentes na
     *          árvore geradora da BFS interrompida ao chegar ao nó-alvo
     *          (caso chegue). Suas chaves são os índices dos nós (incluindo a
     *          própria raiz e o alvo) e seus valores são arrays de inteiros
     *          cuja primeira posição indica o pai de cada nó na árvore
     *          geradora e cuja segunda posição indica o nível desse nó.
     */
    public HashMap<Integer, Integer[]> BFS(int origin, int goal) {
        //Array booleano com a marcacao dos vertices
        //Todos os vertices sao desmarcados a principio
        Boolean known[] = new Boolean[this.getNNodes() + 1];
        //nNodes + 1 p/ pular o índice 0, já
        // que eles indexam a partir do 1 (consistente com alguns outros
        // métodos)

        Arrays.fill(known, false);

        LinkedList<Integer> queue = new LinkedList<Integer>();
        HashMap<Integer, Integer[]> connectedToOrigin = new HashMap<Integer,
        Integer[]>();

        //Marcamos o vertice origem e o adicionamos na fila
        queue.add(origin);        
        known[origin] = true;   
        connectedToOrigin.put(origin, new Integer[]{0, 0});

        // Tentei agrupar os aspectos comuns a ambas as representações para
        // o código ficar mais enxuto/limpo, mas os testes passaram a falhar;
        // acho que estou esbarrando em problemas de escopo na hora de alterar os
        // valores (mesmo tendo tentado definir tudo fora dos blocos), e algumas
        // outras coisas também. Como a versão "mais feia"/com duplicações
        // funciona, optamos por deixar assim a versao final.

        if (this.adjMatrix == null) {  // repr por lista
            while (queue.size() != 0) {
                int v = queue.remove();
                int vLvl = connectedToOrigin.get(v)[1];

                Iterator<Integer> iter = adjList.get(v).listIterator();

                while (iter.hasNext()) {
                    int w = iter.next();

                    if (!known[w]) {
                        known[w] = true;
                        connectedToOrigin.put(w, new Integer[]{v,
                                vLvl + 1});

                        queue.add(w);

                        if (w == goal) {
                            return connectedToOrigin;
                        }
                    }
                }
            }
        } else {  // repr por matriz
            while (queue.size() != 0) {
                int v = queue.remove();
                int vLvl = connectedToOrigin.get(v)[1];

                ArrayList<Boolean> mtxVertexRow = adjMatrix.get(v);

                Iterator<Boolean> iter = mtxVertexRow.iterator();

                int colCounter = 0;

                while (iter.hasNext()) {
                    Boolean w = iter.next();

                    if (w) {
                        if (!known[colCounter]) {
                            known[colCounter] = true;
                            connectedToOrigin.put(colCounter, new Integer[]{v,
                                    vLvl + 1});
                            queue.add(colCounter);

                            if (colCounter == goal) {
                                return connectedToOrigin;
                            }
                        }
                    }
                    colCounter++;
                }
            }
        }
        return connectedToOrigin;
    }

    /**
     * Implementa a busca em largura a partir do vértice de origem
     * especificado, retornando sua árvore geradora.
     * @param origin Índice do vértice a ser usado como origem da busca.
     * @return HashMap cujas chaves são os índices dos nós presentes na
     *          componente conexa que contém a raiz (incluindo a própria) e
     *          cujos valores são arrays de inteiros cuja primeira posição
     *          indica o pai de cada nó na árvore geradora e cuja segunda
     *          posição indica o nível desse nó.
     */
    public HashMap<Integer, Integer[]> BFS(int origin) {
        return this.BFS(origin, -1);  // índice que certamente não existe
    }

    /**
     * Implementa a busca em profundidade a partir do vértice de origem
     * especificado, retornando sua árvore geradora. Interrompe a BFS ao
     * chegar ao vértice-alvo fornecido, caso este exista e esteja ligado
     * à origem por algum caminho.
     * @param origin Índice do vértice a ser usado como origem da busca.
     * @param goal Índice do vértice buscado.
     * @return HashMap cujas chaves são os índices dos nós presentes na
     *          árvore geradora da BFS interrompida ao chegar ao nó-alvo
     *          (caso chegue). Suas chaves são os índices dos nós (incluindo a
     *          própria raiz e o alvo) e seus valores são arrays de inteiros
     *          cuja primeira posição indica o pai de cada nó na árvore
     *          geradora e cuja segunda posição indica o nível desse nó.
     */
    public HashMap<Integer, Integer[]> DFS(int origin, int goal) {
        //Array booleano com a marcacao dos vertices
        //Todos os vertices sao desmarcados a principio
        Boolean explored[] = new Boolean[this.getNNodes() + 1];

        //No caso da DFS, a marcacao significa que um vertice foi explorado

        //A principio, desmarcamos todos os vertices do grafo menos o de origem.
        Arrays.fill(explored, false);

        //Criamos uma pilha com apenas vertice de origem e instanciamos o HashMap de
        //relacoes pai/filho/nivel
        Stack<Integer> stack = new Stack<Integer>();
        stack.push(origin);

        HashMap<Integer, Integer[]> connectedToOrigin = new HashMap<Integer, Integer[]>();
        connectedToOrigin.put(origin, new Integer[]{0, 0});
        //vertex, [parent, level]

        while (stack.size() > 0) {
            int v = stack.pop();
            int vLvl = connectedToOrigin.get(v)[1];
            explored[v] = true;

            //Fiz uma cópia da lista de adjacencia de v para por os vertices adjacentes
            //em ordem decrescente sem modificar a lista original.
            ArrayList<Integer> adjCopia = this.getNeighbors(v);
            Collections.sort(adjCopia);
            Collections.reverse(adjCopia);

            //Percorrendo os vertices adjacentes de v em ordem decrescente.
            //O de menor indice fica no topo da pilha e é analisado primeiro.
            for (int w : adjCopia) {
                if (!explored[w]) {
                    stack.push(w);
                    connectedToOrigin.put(w, new Integer[]{v, vLvl + 1});
                    if (w == goal) {
                        return connectedToOrigin;
                    }
                }
            }
        }

        return connectedToOrigin;
    }

    /**
     * Implementa a busca em profundidade a partir do vértice de origem
     * especificado, retornando sua árvore geradora.
     * @param origin Índice do vértice a ser usado como origem da busca.
     * @return HashMap cujas chaves são os índices dos nós presentes na
     *          componente conexa que contém a raiz (incluindo a própria) e
     *          cujos valores são arrays de inteiros cuja primeira posição
     *          indica o pai de cada nó na árvore geradora e cuja segunda
     *          posição indica o nível desse nó.
     */
    public HashMap<Integer, Integer[]> DFS(int origin) {
        return this.DFS(origin, -1);  // índice que certamente não existe
    }

    /**
     * Obtém a componente conexa do grafo contendo o nó indicado.
     * @param node Índice do nó.
     * @return ArrayList ordenada com os índices dos nós da componente.
     */
    public ArrayList<Integer> findConnectedComponent(int node) {
        Set<Integer> elements = this.BFS(node).keySet();
        ArrayList<Integer> sorted = new ArrayList<Integer>();

        sorted.addAll(elements);
        Collections.sort(sorted);

        return sorted;
    }

    /**
     * Determina as componentes conexas do grafo.
     * @return ArrayList de componentes conexas, ordenadas da maior para a
     *          menor. Os índices dos nós de cada componente estão em ordem
     *          crescente, conforme saída da função findConnectedComponent().
     */
    public ArrayList<ArrayList<Integer>> findConnectedComponents() {
        ArrayList<ArrayList<Integer>> components =
                new ArrayList<ArrayList<Integer>>();

        int n = this.getNNodes();
        int found[] = new int[n + 1];

        Arrays.fill(found, 0);

        for (int i=1; i <= n; i++) {
            if (found[i] == 0) {
                ArrayList<Integer> component = this.findConnectedComponent(i);
                components.add(component);

                for (int node : component) {
                    found[node] = 1;
                }  // senão, pula
            }
        }

        // Para ordenar ArrayLists de inteiros de acordo com seu comprimento:
        Comparator<ArrayList<Integer>> onLength = new Comparator<ArrayList<Integer>>() {
            @Override
            public int compare(ArrayList<Integer> l1, ArrayList<Integer> l2) {
                return l1.size() - l2.size();
            }
        };

        Collections.sort(components, onLength);
        Collections.reverse(components);  // ordem decrescente de tamanho

        return components;
    }

    /**
     * Imprime um relatório sobre o grafo num arquivo de texto.
     * @param outfile Arquivo de saída, incluindo caminho absoluto ou a
     *                partir da raiz do diretório de trabalho. Será
     *                sobrescrito caso já exista.
     */
    public void printReport (String outfile) throws IOException {
        BufferedWriter bw =
                new BufferedWriter(new FileWriter(outfile, false));
        // cf. https://stackoverflow.com/a/52581499
        // Para criar o arquivo caso não exista, e sobrescrevê-lo caso exista

        bw.write("Relatorio sobre o grafo " + this.getInputFile());
        bw.write("\n(Representacao interna: ");

        if (this.adjMatrix == null) {
            bw.write("lista ");
        } else {
            bw.write("matriz ");
        }

        bw.write("de adjacências)");

        bw.write("\n\nNumero de nos: " + this.getNNodes());
        bw.write("\nNumero de arestas: " + this.getNEdges());

        HashMap<String, Double> data = this.getDegreeOverview();

        bw.write("\nGrau maximo: " + data.get("max"));
        bw.write("\nGrau minimo: " + data.get("min"));
        bw.write("\nGrau medio: " + data.get("mean"));
        bw.write("\nMediana de grau: " + data.get("med"));

        bw.write("\n\nComponentes conexas");

        ArrayList<ArrayList<Integer>> components =
                this.findConnectedComponents();

        int qty = components.size();

        bw.write("\nQuantidade: " + qty);
        bw.write("\nFormato: [tamanho] nó1 nó2...");

        for (ArrayList<Integer> component : components) {
            int len = component.size();
            bw.write("\n[" + len + "]");

            for (Integer node : component) {
                bw.write(" " + node);
            }
        }

        bw.close();
    }

}
