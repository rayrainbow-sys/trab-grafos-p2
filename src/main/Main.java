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
//        Graph[] caseStudiesMtx = new Graph[nCases];
//        Graph[] caseStudiesList = new Graph[nCases];

        Graph graph;

        for (int i=1; i <= nCases; i ++) {
            for (int repr=0; repr <= 1; repr++) {
                try {
                    String in = inputDir + "grafo_" + i + ".txt";

                    System.out.println("\nLendo arquivo " + in);

                    graph = new Graph(in, repr);
//                    caseStudiesMtx[i - 1] = graphM;
//
//                    Graph graphL = new Graph(in, 1);
//                    caseStudiesList[i - 1] = graphL;

                } catch (InstantiationException exc) {
                    System.err.println("Falha na leitura do arquivo do estudo de " +
                            "caso " + i +"; abortando execução");
                    System.exit(1);
                }
            }

        }

        System.out.println(outDir);
    }
}