package main;
import main.Graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    // Definindo algumas constantes:
    private static final int nCases = 3; //6;
    // pulando os que estouram a heap por enqto
    private static final int nRuns = 2; //1000;
    // pouco, para testar
    private static final String inputDir = "case-studies/";
    private static final String outDir = "reports/";

    public String stdReportFilename(Graph graph) {
        return outDir + graph.getInputFile() + ".txt";
    }

    public static void main(String[] args) {
        Graph graph;
        BufferedWriter csvWriter;

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

//        String csvHeader = "grafo,memMatriz,memLista," +
//                "tBFSMatriz,tBFSLista,tDFSMatriz,tDFSLista," +
//                "pai10BFS,pai20BFS,pai30BFS," +
//                "pai10DFS,pai20DFS,pai30DFS," +
//                "dist10_20,dist10_30,dist20_30," +
//                "nComponentes,compMax,compMin,diam";

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

        for (int i=1; i <= nCases; i ++) {
            String csvRow = i + ",";  // grafo

            for (int repr=0; repr <= 1; repr++) {
                try {
                    String in = inputDir + "grafo_" + i + ".txt";

                    System.out.print("\nLendo arquivo " + in + " (repr.: ");

                    if (repr == 0) {
                        System.out.print("matriz");
                    } else {
                        System.out.print("lista");
                    }

                    System.out.print(")\n");

                    graph = new Graph(in, repr);

                } catch (InstantiationException exc) {
                    System.err.println("Falha na leitura do arquivo do estudo de " +
                            "caso " + i +"; abortando execucao");
                    graph = null;  // p/ evitar erro de "pode não estar inicializado"
                    // (neste caso não é problema, porque se houver a exceção a
                    // execução é encerrada)
                    System.exit(1);
                }

                csvRow += "-1,";  // pulando o uso de memória por enqto,
                // precisamos pesquisar a melhor forma de fazer

                // tBFS ou tDFS:
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
                    csvRow += graph.calcDiameter();  // diam

                }  // if (repr == 1); os que só precisam rodar uma vez

            }

            try {
                csvWriter.write(csvRow + "\n");
            } catch (IOException exc) {
                System.err.println("Falha na escrita do arquivo csv das saidas " +
                        "das medicoes; abortando execucao");
                System.exit(1);
            }

        }

        try {
            csvWriter.close();
        } catch (IOException exc) {
            System.err.println("Falha na escrita do arquivo csv das saidas " +
                    "das medicoes; abortando execucao");
            System.exit(1);
        }

    }
}