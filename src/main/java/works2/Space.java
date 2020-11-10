package works2;

import lombok.Data;

import java.io.*;
import java.util.Objects;

/**
 * @author best lu
 * @since 2020/11/10
 */
@Data
public class Space {
    private static int cellsBase;// 基数n
    private static int evolutionAlg;// 进化代数
    private static int[][] cellSpace;// 棋盘
    private static Space space;//

    private Space(){
        cellSpace = new int[cellsBase][cellsBase];
    }

    public static Space getSpace() throws Exception {
        if (space == null) {
            synchronized (Space.class) {
                if (space == null) {
                    File file = new File((Space.class.getClassLoader().getResource("")+"input.txt").substring(6));
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                    cellsBase = Integer.parseInt(Objects.requireNonNull(bufferedReader.readLine()));
                    evolutionAlg = Integer.parseInt(Objects.requireNonNull(bufferedReader.readLine()));
                    space = new Space();
                    do {
                        String str = bufferedReader.readLine();
                        if (str == null) break;
                        String[] xy = str.split(",");
                        if (xy.length != 2) throw new Exception("init File Exception: each line contain x,y");
                        int x = Integer.parseInt(xy[0]);
                        int y = Integer.parseInt(xy[1]);
                        cellSpace[x][y] = 1;
                    }while (true);
                    return space;
                }
            }
        }
        return space;
    }

    /**
     * 结果展示
     * @param cellSpace 需展示的棋盘
     */
    public static void display(int[][] cellSpace) {
        for (int[] line:cellSpace) {
            for (int cell:line) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    /**
     * 生命历程
     */
    public void evolve(){
        //System.out.println("Space-0:");
        //display(cellSpace);
        int i = 0;
        int[][] oldSpace = cellSpace;
        while (i < evolutionAlg) {
            int[][] generateSpace = new int[cellsBase][cellsBase];
            for (int j = 0; j < cellsBase;j ++){
                for (int k = 0;k < cellsBase;k ++){
                    generateSpace[j][k] = evolveCell(oldSpace,j,k);
                }
            }
            oldSpace = generateSpace;
            System.out.println("Space-" + (i+1) + ":");
            display(generateSpace);
            i++;
        }
        cellSpace = oldSpace;
    }

    /**
     * 输出结果到output.txt
     * @throws IOException io异常
     */
    public static void output2File() throws IOException {
        File file = new File((Space.class.getClassLoader().getResource("")+"output.txt").substring(6));
        if (!file.exists()) {
            if (file.createNewFile()) {
                System.out.println("create file");
            }
        }
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        StringBuilder stringB = new StringBuilder();
        for (int i = 0; i < cellsBase; i++) {
            for (int j = 0; j < cellsBase; j++) {
                if (cellSpace[i][j] == 1) {
                    if (stringB.length() > 0){
                        stringB.delete(0,stringB.length());
                    }
                    stringB.append(i).append(",").append(j);
                    bufferedWriter.write(stringB.toString());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        }
    }

    /**
     * 进化一个cell
     * @param cellSpace 迭代的棋盘
     * @param x 坐标x
     * @param y 坐标y
     * @return 生存结果
     */
    private int evolveCell(int[][]cellSpace, int x, int y) {
        int thisCell = cellSpace[x][y];
        int liveCells = 0;
        int i = -1;
        while(i <= 1) {
            if ((x + i) < 0) {
                i++;
                continue;
            }
            if ((x + i) >= cellsBase) {
                break;
            }
            liveCells += (y - 1) >= 0         ? cellSpace[x + i][y - 1] : 0;
            liveCells += i != 0               ? cellSpace[x + i][y]     : 0;
            liveCells += (y + 1) < cellsBase  ? cellSpace[x + i][y + 1] : 0;
            i++;
        }
        //System.out.println("x: "+x+",y: "+y+"; "+liveCells);
        return liveCells == 3 ? 1 : (liveCells == 2 ? thisCell : 0);
    }
}
