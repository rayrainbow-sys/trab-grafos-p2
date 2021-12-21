package main;
import main.Graph;

public class Main {
    private static final int nCases = 6;
    private static final String inputDir = "case-studies/";
    private static final String outDir = "reports/";

    public String stdReportFilename(Graph graph) {
        return outDir + graph.getInputFile() + ".txt";
    }

    public static void main(String[] args) {
        Graph graph;

        for (int i=1; i <= nCases; i ++) {
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
                            "caso " + i +"; abortando execução");
                    System.exit(1);
                }

            }

        }

    }
}