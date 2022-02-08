package casestudies; //casestudies;
// (não aceitou, e não achei que valia a pena parar para resolver isso agora :P)
import graphs.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Programa que usamos para gerar os dados requeridos para os estudos de caso
 * . (Este programa não seria necessário para um usuário da classe Graph; até
 * por conta disso, ele está menos detalhadamente documentado.)
 */
public class Main {
    // Definindo algumas constantes:
    private static final int nCases = 6;
    private static int nRuns = 1000;
    // retiramos o final para reduzir para 4, 5, 6
    private static final String inputDir = "case-studies/";
    private static final String outDir = "reports/";

    public static String stdReportFilename(Graph graph) {
        return outDir + graph.getInputFile() + ".txt";
    }

    /**
     * Memória de fato sendo utilizada pela JVM. Ver https://stackoverflow.com/questions/3571203/what-are-runtime-getruntime-totalmemory-and-freememory
     * @param runtime
     * @return
     */
    public static long usedMemory(Runtime runtime) {
        return runtime.totalMemory() - runtime.freeMemory();
    }

    /**
     * Memória de fato disponível para a JVM. Ver https://stackoverflow.com/questions/3571203/what-are-runtime-getruntime-totalmemory-and-freememory
     * @param runtime
     * @return
     */
    public static long actualFreeMemory(Runtime runtime) {
        return runtime.maxMemory() - usedMemory(runtime);
    }

    public static void printMemUsage(Runtime runtime) {
        long max = runtime.maxMemory();
        long used = usedMemory(runtime);
        long freeActual = actualFreeMemory(runtime);

        System.out.println("Memoria disponivel p/ a JVM: " + max / 1024);
        System.out.println("Memoria em uso pela JVM: " + used / 1024);
        System.out.println("Memoria livre: " + freeActual / 1024);
        System.out.println("% em uso:" + ((double) used) /  ((double) max));
        System.out.println("% livre:" + ((double) freeActual) /  ((double) max));
        System.out.println("(Obs: todos os valores absolutos em MB.)");
    }

    public static void main(String[] args) {
        Graph graph;
        BufferedWriter csvWriter;
        long currentMem;
        Runtime runtime = Runtime.getRuntime();
        // p/ medir o uso de memória
        // cf https://stackoverflow.com/a/74763

        printMemUsage(runtime);

        try {
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
        }

        String csvHeader = "grafo,memMatriz,tBFSMatriz,tDFSMatriz," +
                "memLista,tBFSLista,tDFSLista," +
                "pai10BFS,pai20BFS,pai30BFS," +
                "pai10DFS,pai20DFS,pai30DFS," +
                "dist10_20,dist10_30,dist20_30," +
                "nComponentes,compMax,compMin,diam";

        // melhor separar em várias tabelas na hora de fazer o relatório, mas
        // talvez seja mais simples/conveniente imprimir pra um arquivo só aqui

        try {
            csvWriter.write(csvHeader + "\n");
        } catch (IOException exc) {
            System.err.println("Falha na escrita do arquivo csv das saidas " +
                    "das medicoes; abortando execucao");
            System.exit(1);
        }

        /*for (int i=1; i <= nCases; i ++) {
            String csvRow = i + ",";  // grafo

            for (int repr=0; repr <= 1; repr++) {
                if (i > 3 && repr == 0) {
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

                    System.out.print("\nLendo arquivo " + in + " (repr.: lista de adjacências com pesos)\n";

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

                printMemUsage(runtime);

                currentMem = usedMemory(runtime) / 1024;

                csvRow += currentMem + ",";

                // tBFS ou tDFS:
                if (i >= 4) {
                    nRuns = 10;
                    // na velocidade em que estava, íamos estourar o prazo só
                    // esperando o algoritmo rodar...
                }

                for (int meth=0; meth <= 1; meth++) {  // Jesse, let's cook
                    long avg = 0;
                    long start, end, elapsed;

                    for (int r=0; r < nRuns; r++) {
                        int origin = r % graph.getNNodes();
                        // por simplicidade

                        start = System.nanoTime();

                        if (meth == 0) {
                            graph.BFS(origin);
                        } else {
                            graph.DFS(origin);
                        }

                        end = System.nanoTime();
                        elapsed = end - start;
                        avg += elapsed;
                    }

                    avg /= nRuns;
                    csvRow += avg + ",";
                }

                // As medidas seguintes não dependem da forma de
                // representação, então só precisam ser executadas uma vez

                if (repr == 1) {
                    // Pais de 10, 20 e 30 nas árvores:
                    for (int s=0; s <= 1; s++) {
                        for (int n=1; n <= 3; n++) {
                            HashMap<Integer, Integer[]> tree;

                            if (s == 0) {
                                tree = graph.BFS(n);
                            } else {
                                tree = graph.DFS(n);
                            }

                            try {
                                csvRow += tree.get(10 * n)[0] + ",";
                            } catch (NullPointerException exc) {
                                // chave não existe; não está na componente
                                // conexa da origem
                                csvRow += "N/A,";
                            }
                        }
                    }

                    // Distâncias entre nós:
                    csvRow += graph.calcDistance(10, 20) + ",";
                    csvRow += graph.calcDistance(10, 30) + ",";
                    csvRow += graph.calcDistance(20, 30) + ",";

                    // Número de componentes conexas:
                    ArrayList<ArrayList<Integer>> components =
                            graph.findConnectedComponents();

                    csvRow += components.size() + ",";  //nComponentes
                    csvRow += components.get(0).size() + ",";  //compMax
                    csvRow += components.get(components.size()-1).size() +
                            ",";  //compMin
                    csvRow += graph.calcDiameter(10000);  // diam

                }  // if (repr == 1); os que só precisam rodar uma vez

            }

            try {
                csvWriter.write(csvRow + "\n");
            } catch (IOException exc) {
                System.err.println("Falha na escrita do arquivo csv das saidas " +
                        "das medicoes; abortando execucao");
                System.exit(1);
            }

        }*/

        try {
            csvWriter.close();
        } catch (IOException exc) {
            System.err.println("Falha na escrita do arquivo csv das saidas " +
                    "das medicoes; abortando execucao");
            System.exit(1);
        }

    }
}