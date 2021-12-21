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
        Graph[] caseStudies = new Graph[2 * nCases];

        for (int i=1; i <= nCases; i ++) {
            try {
                Graph graph = new Graph(inputDir + "grafo_" + i + ".txt");
                caseStudies[i] = graph;
            } catch (InstantiationException exc) {
                System.err.println("Falha na leitura do arquivo do estudo de " +
                        "caso " + i +"; abortando execução");
                System.exit(1);
            }

        }

        System.out.println(outDir);
    }
}