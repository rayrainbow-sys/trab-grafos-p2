package casestudies; //casestudies;
// (não aceitou, e não achei que valia a pena parar para resolver isso agora :P)
import graphs.Graph;

import java.io.*;
import java.util.*;

/**
 * Programa que usamos para gerar os dados requeridos para os estudos de caso
 * . (Este programa não seria necessário para um usuário da classe Graph; até
 * por conta disso, ele está menos detalhadamente documentado.)
 */
public class Main {
    // Definindo algumas constantes:
    private static final int nCases = 5;
    private static int nRuns = 100;
    // retiramos o final para reduzir para 4, 5
    private static final String inputDir = "casestudies/";
    private static final String outDir = "reports/";

    /*public static String stdReportFilename(Graph graph) {
        return outDir + graph.getInputFile() + ".txt";
    }*/

        public static void main(String[] args) throws InstantiationException {
        Graph graph;
        BufferedWriter csvWriter;

        /*try {
            csvWriter =
                    new BufferedWriter(new FileWriter(outDir + "case_studies.csv",
                            false));
            // cf. https://stackoverflow.com/a/52581499 e como Graph.printReport()
        } catch (IOException exc) {
            System.err.println("Falha na escrita do arquivo csv das saidas " +
                    "das medicoes; abortando execucao");
            csvWriter = null;  // p/ evitar erro de "pode não estar inicializado"
            // (neste caso não é problema, porque se houver a exceção a
            // execução é encerrada)
            System.exit(1);
        }*/

        try {
            csvWriter =
                    new BufferedWriter(new FileWriter(outDir + "rede_colab.csv",
                            false));
            // cf. https://stackoverflow.com/a/52581499 e como Graph.printReport()
        } catch (IOException exc) {
            System.err.println("Falha na escrita do arquivo csv das saidas " +
                    "das medicoes; abortando execucao");
            csvWriter = null;  // p/ evitar erro de "pode não estar inicializado"
            // (neste caso não é problema, porque se houver a exceção a
            // execução é encerrada)
            System.exit(1);
        }

        /*String csvHeader = "grafo,avgRuntime,dist1_10,dist1_20,dist1_30,dist1_40,dist1_50," +
                "CM1_10, CM1_20, CM1_30, CM1_40, CM1_50";*/

        String csvHeader = "Pesquisador,Distancia,CaminhoMinimo";

        try {
            csvWriter.write(csvHeader + "\n");
        } catch (IOException exc) {
            System.err.println("Falha na escrita do arquivo csv das saidas " +
                    "das medicoes; abortando execucao");
            System.exit(1);
        }

        try {
            String in = inputDir + "rede_colaboracao.txt";

            graph = new Graph(in);
            System.gc();
            // tentando forçar o coletor de lixo a apagar o anterior da
            // memória
            // (pelo uso de memória antes disso, parecia que não estava
            // liberando o espaço de imediato)

        } catch (InstantiationException exc) {
            System.err.println("Falha na leitura do arquivo do estudo de " +
                    "caso de colaboracao; abortando execucao");
            graph = null;  // p/ evitar erro de "pode não estar inicializado"
            // (neste caso não é problema, porque se houver a exceção a
            // execução é encerrada)
            System.exit(1);
        }

        HashMap<Integer, String> pesquisadores = null;
        try {

            File inputFile = new File("casestudies/rede_colaboracao_vertices.txt");
            Scanner inputReader = new Scanner(inputFile);

            // Obs: não sei se é o melhor caminho, mas, como ele indexa a partir
            // do 1, em ambos os casos incluí o índice 0, mas pra deixar "em
            // branco" (sem uso); me pareceu melhor do que usar um HashMap só
            // pra pular os índices 0

            while (inputReader.hasNextLine()) {
                String link = inputReader.nextLine();
                String[] assoc = link.split(",");

                Integer vertice = Integer.parseInt(assoc[0]);
                String pesquisador = assoc[1];

                pesquisadores = new HashMap<>();

                pesquisadores.put(vertice, pesquisador);
            }
            inputReader.close();
        } catch (FileNotFoundException exc) {
            System.err.println("Falha na leitura do arquivo de colaboraçoes");
            throw new InstantiationException("Arquivo de entrada inexistente; grafo nao instanciado");
        }

        ArrayList<String> dttTu = graph.dijkstraColab(11365, pesquisadores); //Dijkstra a Turing
        ArrayList<String> dttKl = graph.dijkstraColab(264337, pesquisadores); //Dijkstra a Kleinberg
        ArrayList<String> dttKr = graph.dijkstraColab(211706, pesquisadores); //Dijkstra a Kruskal
        ArrayList<String> dttTa = graph.dijkstraColab(11386, pesquisadores); //Dijkstra a Tardos
        ArrayList<String> dttDa = graph.dijkstraColab(264337, pesquisadores); //Dijkstra a Daniel


        String Turing = "Alan M. Turing," + dttTu.subList(0, 1) + "," + dttTu.subList(1, dttTu.size() - 1);
        String Kruskal = "J. B. Kruskal," + dttKr.subList(0, 1) + "," + dttKr.subList(1, dttKr.size() - 1);
        String Kleinberg = "Jon M. Kleinberg," + dttKl.subList(0, 1) + "," + dttKl.subList(1, dttKl.size() - 1);
        String Tardos = "Eva Tardos," + dttTa.subList(0, 1) + "," + dttTa.subList(1, dttTa.size() - 1);
        String Daniel = "Daniel R. Figueiredo," + dttDa.subList(0, 1) + "," + dttDa.subList(1, dttDa.size() - 1);

        try {
            csvWriter.write(Turing + "\n");
            csvWriter.write(Kruskal + "\n");
            csvWriter.write(Kleinberg + "\n");
            csvWriter.write(Tardos + "\n");
            csvWriter.write(Daniel + "\n");

        } catch (IOException exc) {
            System.err.println("Falha na escrita do arquivo csv das saidas " +
                    "das medicoes; abortando execucao");
            System.exit(1);
        }


        /*for (int i=1; i <= nCases; i ++) {
            String csvRow = i + ",";  // grafo

            if (i > 3) {
                // pulando os casos que estouram a heap (por enquanto?)
                csvRow += "-2,-2,-2";
                // sinalizando que não foram feitas as medidas de tempo
                // e uso de memória específicas para a representação por
                // matriz nesses casos
                // (usando -2 para evitar confundir com o -1 retornado
                // por alguns métodos da classe Graph)
                continue;
            }
            try {
                String in = inputDir + "grafo_" + i + ".txt";

                graph = new Graph(in);
                System.gc();
                // tentando forçar o coletor de lixo a apagar o anterior da
                // memória
                // (pelo uso de memória antes disso, parecia que não estava
                // liberando o espaço de imediato)

            } catch (InstantiationException exc) {
                System.err.println("Falha na leitura do arquivo do estudo de " +
                        "caso " + i +"; abortando execucao");
                graph = null;  // p/ evitar erro de "pode não estar inicializado"
                // (neste caso não é problema, porque se houver a exceção a
                // execução é encerrada)
                System.exit(1);
            }

            if (i >= 3) {
                nRuns = 1;
                // na velocidade em que estava, íamos estourar o prazo só
                // esperando o algoritmo rodar...
            }

            // Jesse, let's cook
            long avg = 0;
            long start, end, elapsed;
            ArrayList<Integer> vertices = new ArrayList<>();

            for (int j = 1; j <= graph.getNNodes(); j++) {
                vertices.add(i);
            }

            Collections.shuffle(vertices);

            for (int r=0; r < nRuns; r++) {
                int origin = vertices.get(r);
                // por simplicidade

                start = System.nanoTime();

                graph.dijkstra(origin);

                end = System.nanoTime();
                elapsed = end - start;
                avg += elapsed;
            }

            avg /= nRuns;
            csvRow += avg + ",";


            // As medidas seguintes não dependem da forma de
            // representação, então só precisam ser executadas uma vez

            // Distâncias entre nós:
            csvRow += Arrays.toString(graph.dijkstra(1, 10)) + ",";
            csvRow += Arrays.toString(graph.dijkstra(1, 20)) + ",";
            csvRow += Arrays.toString(graph.dijkstra(1, 30)) + ",";
            csvRow += Arrays.toString(graph.dijkstra(1, 40)) + ",";
            csvRow += Arrays.toString(graph.dijkstra(1, 50)) + ",";

            try {
                csvWriter.write(csvRow + "\n");
            } catch (IOException exc) {
                System.err.println("Falha na escrita do arquivo csv das saidas " +
                        "das medicoes; abortando execucao");
                System.exit(1);
            }

            /*if (i > 2) {
                try {
                    graph.printMST(outDir + "mst_" + i + ".txt");
                } catch (IOException exc) {
                    System.err.println("Falha na escrita do arquivo txt das árvores");
                    System.exit(1);
                }
            }*/


        //}

        try {
            csvWriter.close();
        } catch (IOException exc) {
            System.err.println("Falha na escrita do arquivo csv das saidas " +
                    "das medicoes; abortando execucao");
            System.exit(1);
        }

    }
}