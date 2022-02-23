package task1.graph;

public class Graph {
    public static void main(String[] args) {
        int [] [] graphArray = {{1,3,3,5},
                                {2,1,4,7},
                                {0,6,4,6},
                                {3,6,4,5}};

        int [] [] weightArray = new int[graphArray.length][graphArray [0].length];

        for (int i = 0; i <= weightArray.length-1; i++) {
            for (int j = 0; j <= weightArray [0].length-1; j++) {
                weightArray [i][j] = 0;
            }
        }

        int firstPointWeight = 0;

        for (int j = 0; j <= graphArray.length-1; j++) {
            for (int i = 0; i <= graphArray [0].length-1; i++){

                if (j > 0) {
                    firstPointWeight = weightArray [i][j-1];
                }

                if (i > 0) {
                    if (weightArray [i-1][j] < firstPointWeight + graphArray [i-1][j]) {
                        weightArray [i-1][j] = firstPointWeight + graphArray [i-1][j];
                    }
                }
                if (weightArray [i][j] < firstPointWeight + graphArray [i][j]) {
                    weightArray[i][j] = firstPointWeight + graphArray [i][j];
                }
                if (i < graphArray.length-1) {
                    if (weightArray [i+1][j] < firstPointWeight + graphArray [i+1][j]) {
                        weightArray [i+1][j] = firstPointWeight + graphArray [i+1][j];
                    }
                }
            }
        }

        int maxVal = 0;
        for (int i = 0; i <= weightArray.length-1; i++) {
            for (int j = 0; j <= weightArray [0].length-1; j++) {
                if (maxVal < weightArray [i][j]) {
                    maxVal = weightArray [i][j];
                }
                System.out.println("initialVertex -> [" + i + "][" + j + "] = " + weightArray [i][j]);
            }
        }
        System.out.println( "longest path tree from vertex 'initialVertex' = " + maxVal);

    }
}
