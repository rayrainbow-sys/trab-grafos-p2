import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
//import main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {
    private static main.Graph pdfGraph;
    private static main.Graph connected7;
    private static main.Graph disconnected6;
    private static main.Graph disconnected15;

    private static main.Graph bfsTreeM6;  // raiz: nó 6
    private static main.Graph bfsTreeL6;

    private static main.Graph dfsTreeM4;  // raiz: nó 4
    private static main.Graph dfsTreeL4;

    private static final double eps = 1e-6;  // p/ comparações de floats

    @BeforeAll
    static void setUp() {
        try {
            pdfGraph = new main.Graph("src/test/input/pdf.txt", 1);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo do pdf");
            // Como lidar com isso nos testes?
            // A rigor, acho que a gente deveria estar testando se cada um é null antes
            // de rodar mais testes, mas, como são sempre os mesmos 4 e já sabemos que
            // estão funcionando, vou evitar ficar enchendo de if (... != null)
        }

        try {
            connected7 = new main.Graph("src/test/input/teste1.txt", 0);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo manual 1");
        }

        try {
            disconnected6 = new main.Graph("src/test/input/teste2.txt", 0);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo manual 2");
        }

        try {
            disconnected15 = new main.Graph("src/test/input/teste3.txt", 1);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo manual 3");
        }

        try {
            bfsTreeM6 = new main.Graph("src/test/input/bfstree_root6.txt", 0);
            bfsTreeL6 = new main.Graph("src/test/input/bfstree_root6.txt", 1);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo da BFS (slide)");
        }

        try {
            dfsTreeM4 = new main.Graph("src/test/input/dfstree_root4.txt", 0);
            dfsTreeL4 = new main.Graph("src/test/input/dfstree_root4.txt", 1);
        } catch (InstantiationException exc) {
            System.err.println("Falha na criacao do grafo da DFS (slide)");
        }
    }

//    @AfterEach
//    void tearDown() {
//    }

    @Test
    @DisplayName("Numero de nos")
    void getNNodes() {
        assertEquals(5, pdfGraph.getNNodes());
        assertEquals(7, connected7.getNNodes());
        assertEquals(6, disconnected6.getNNodes());
        assertEquals(15, disconnected15.getNNodes());
    }

    @Test
    @DisplayName("Numero de arestas")
    void getNEdges() {
        assertEquals(5, pdfGraph.getNEdges());
    }

    @Test
    @DisplayName("Lista de vizinhos")
    void getNeighbors() {
        ArrayList<Integer> neighbors = pdfGraph.getNeighbors(1);

        for (int i=1; i<=pdfGraph.getNNodes(); i++) {
            if (i == 2 || i == 5) {
                assertEquals(true, neighbors.contains(i));
            } else {
                assertEquals(false, neighbors.contains(i));
            }
        }
    }

    @Test
    @DisplayName("Grau")
    void getDegree() {
      assertEquals(4, pdfGraph.getDegree(5));
    }

    @Test
    @DisplayName("\"Resumo\" grau")
    void getDegreeOverview() {
        HashMap<String, Double> pdfData = pdfGraph.getDegreeOverview();

        assertEquals(4.0, pdfData.get("max"));
        assertEquals(1.0, pdfData.get("min"));
        assertEquals(true, Math.abs(pdfData.get("mean") - 2) < eps, String.valueOf(pdfData.get("mean")));
        assertEquals(true, Math.abs(pdfData.get("med") - 2) < eps,
                String.valueOf(pdfData.get("med")));
        // 1, 1, 2, 2, 4

        HashMap<String, Double> c7Data = connected7.getDegreeOverview();

        assertEquals(3.0, c7Data.get("max"));
        assertEquals(1.0, c7Data.get("min"));
        assertEquals(true, Math.abs(c7Data.get("mean") - 12.0/7.0) < eps, String.valueOf(c7Data.get("mean")));
        assertEquals(true, Math.abs(c7Data.get("med") - 1.0) < eps,
                String.valueOf(c7Data.get("med")));
        // (estava errado no doc? Rever o grafo)

        HashMap<String, Double> d6Data = disconnected6.getDegreeOverview();

        assertEquals(2.0, d6Data.get("max"));
        assertEquals(1.0, d6Data.get("min"));
        assertEquals(true, Math.abs(d6Data.get("mean") - 8.0/6.0) < eps, String.valueOf(d6Data.get("mean")));
        assertEquals(true, Math.abs(d6Data.get("med") - 1.0) < eps,
                String.valueOf(d6Data.get("med")));

        HashMap<String, Double> d15Data = disconnected15.getDegreeOverview();

        assertEquals(4.0, d15Data.get("max"));
        assertEquals(1.0, d15Data.get("min"));
        assertEquals(true, Math.abs(d15Data.get("mean") - 24.0/15.0) < eps, String.valueOf(d15Data.get("mean")));
        assertEquals(true, Math.abs(d15Data.get("med") - 1.0
                ) < eps,
                String.valueOf(d15Data.get("med")));
        // (estava errado no doc? Rever o grafo)
    }

    @Test
    @DisplayName("Identificação de componente conexa pela BFS")  // por
        // enquanto
    void BFSComponent() {
        Set<Integer> d6con5 = disconnected6.BFS(5).keySet();

        for (int i=1; i<=disconnected6.getNNodes(); i++) {
            if (i >= 5) {
                assertEquals(true, d6con5.contains(i), "Faltou " + i);
            } else {
                assertEquals(false, d6con5.contains(i), i + " a mais");
            }
        }

        Set<Integer> d6con2 = disconnected6.BFS(2).keySet();

        for (int i=1; i<=disconnected6.getNNodes(); i++) {
            if (i < 5) {
                assertEquals(true, d6con2.contains(i), "Faltou " + i);
            } else {
                assertEquals(false, d6con2.contains(i), i + " a mais");
            }
        }
    }

    @Test
    @DisplayName("Arvore geradora da BFS")
    void BFSTree() {
        // comparação com o slide da aula

        HashMap<Integer, Integer[]> mtxTree = bfsTreeM6.BFS(6);

        for (int i=1; i<=bfsTreeM6.getNNodes(); i++) {
            assertTrue(mtxTree.containsKey(i), "Faltou " + i);
            // é conexo
        }

        // Testando níveis:
        assertEquals(mtxTree.get(6)[1], 0);  // raiz tem nível 0
        assertEquals(mtxTree.get(5)[1], 1);
        assertEquals(mtxTree.get(1)[1], 2);
        assertEquals(mtxTree.get(3)[1], 3);

        // Testando pais:
        assertEquals(mtxTree.get(2)[0], 6);
        assertEquals(mtxTree.get(4)[0], 2);
        assertEquals(mtxTree.get(3)[0], 1);
    }


    @Test
    @DisplayName("Identificação de componente conexa pela DFS")  // por
        // enquanto
    void DFSComponent() {
        ArrayList<Integer> d6con5 = disconnected6.DFS(5);

        for (int i=1; i<=disconnected6.getNNodes(); i++) {
            if (i >= 5) {
                assertEquals(true, d6con5.contains(i), "Faltou " + i);
            } else {
                assertEquals(false, d6con5.contains(i), i + " a mais");
            }
        }

        ArrayList<Integer> d6con2 = disconnected6.DFS(2);

        for (int i=1; i<=disconnected6.getNNodes(); i++) {
            if (i < 5) {
                assertEquals(true, d6con2.contains(i), "Faltou " + i);
            } else {
                assertEquals(false, d6con2.contains(i), i + " a mais");
            }
        }
    }

    @Test
    void calcDistance() {
        assertEquals(2, pdfGraph.calcDistance(5, 2));
        assertEquals(1, pdfGraph.calcDistance(5, 3));
    }

    @Test
    void calcDiameter() {
         assertEquals(2, pdfGraph.calcDiameter());
    }
