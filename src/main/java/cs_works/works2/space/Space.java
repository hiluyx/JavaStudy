package cs_works.works2.space;

import cs_works.taskPool.EvolveWorkPool;
import cs_works.works2.taskQueue.CellQueue;
import lombok.Data;
import lombok.Setter;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author best lu
 * @since 2020/11/10
 */
@Data
public class Space {
    private static final Logger logger = LoggerFactory.getLogger(Space.class);
    private final Object lock = new Object();
    private static final AtomicInteger atomicInt = new AtomicInteger(0);
    private int numDivBlocks; // 数据划分num份,竖划，生成numDivBlocks个handler
    private int queueSize;
    private int numPerDiv; // spaceBase/numDivBlocks
    private int spaceBase; // 基数n，棋盘大小
    private int evolutionAlg; // 进化代数
    private int[][] cellSpace; // 棋盘
    private int[][] temp; // 空盘，与cellSpace交换

    @Setter
    public class CellsHandler implements Runnable {
        private final Logger cLogger = LoggerFactory.getLogger(CellsHandler.class);
        private int x_lEdge; // x的左界
        private int x_rEdge; // x的右界
        private int y_tEdge; // y的下界
        private int y_bEdge; // y的上界
        private boolean isDone = false;
        private int serialNum; // 序号
        private CellQueue queue = new CellQueue(queueSize); // 定义自己的队伍长度

        @Override
        public String toString() {
            return "CellsHandler{" +
                    "x_lEdge=" + x_lEdge +
                    ", x_rEdge=" + x_rEdge +
                    ", y_tEdge=" + y_tEdge +
                    ", y_bEdge=" + y_bEdge +
                    ", isDone=" + isDone +
                    ", serialNum=" + serialNum +
                    '}';
        }

        public CellsHandler(int numBlocks) {
            this.serialNum = numBlocks;
            x_lEdge = numBlocks * numPerDiv;
            x_rEdge = (numBlocks == numDivBlocks - 1) ? (spaceBase - 1) : ((numBlocks + 1) *numPerDiv - 1);
            y_tEdge = 0;
            y_bEdge = spaceBase - 1;
        }

        @SneakyThrows
        @Override
        public void run() {
            //EvolveWorkPool.execute(()->{ // 不断扫描子棋盘，生产cell进入cellQueue
                try {
                    cLogger.info("CellHandler-{} start.{}",serialNum,this.toString());
                    for (int i = x_lEdge; i <= x_rEdge; i ++) {
                        for (int j = y_tEdge; j <= y_bEdge; j ++) {
                            //while (! queue.add(new Cell(i, j, cellSpace[i][j])));
                            temp[i][j] = evolveCell(i,j,x_lEdge,x_rEdge, y_tEdge,y_bEdge);
                        }
                    }
                    if (atomicInt.incrementAndGet() == numDivBlocks) {
                        synchronized (lock) {
                            atomicInt.set(0);
                            lock.notifyAll();
                        }
                    }
                    isDone = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            //});

//            while (true) { // consumer不断消化cellQueue的任务
//                if (isDone && queue.isEmpty()) {
//                    cLogger.info("CellHandler-{} done.", serialNum);
//                    if (atomicInt.incrementAndGet() == numDivBlocks) {
//                        synchronized (lock) {
//                            atomicInt.set(0);
//                            lock.notifyAll();
//                        }
//                    }
//                    break;
//                }
//                Cell cell = queue.consume();
//                if (cell == null) {
//                    continue;
//                }
//                int _x = cell.getX();
//                int _y = cell.getY();
//                int cellResult = evolveCell(_x,_y, x_lEdge,x_rEdge, y_tEdge,y_bEdge);
//                temp[_x][_y] = cellResult;
//            }
        }
        /**
         * 进化一个cell
         *
         * @param x         坐标x
         * @param y         坐标y
         * @return 生存结果
         */
        private int evolveCell(int x, int y, int x_lEdge,int x_rEdge,int y_tEdge,int y_bEdge) {
            int thisCell = cellSpace[x][y];
            int liveCells = 0;
            int i = -1;
            while (i <= 1) {
                if ((x + i) < x_lEdge) {
                    i++;
                    continue;
                }
                if ((x + i) > x_rEdge) {
                    break;
                }
                liveCells += (y - 1) >= y_tEdge ? cellSpace[x + i][y - 1] : 0;
                liveCells += i != 0 ? cellSpace[x + i][y] : 0;
                liveCells += (y + 1) <= y_bEdge ? cellSpace[x + i][y + 1] : 0;
                i++;
            }
            //System.out.println("x: "+x+",y: "+y+"; "+liveCells);
            return liveCells == 3 ? 1 : (liveCells == 2 ? thisCell : 0);
        }
    }

    /**
     * 生命历程
     */
    public void evolve() throws InterruptedException {
        long sT = System.currentTimeMillis();
        this.temp = new int[spaceBase][spaceBase];
        for (int i = 1; i <= evolutionAlg; i ++) {
            logger.info("evolutionAlg-{}",i);
            for (int j = 0; j < numDivBlocks; j ++) {
                EvolveWorkPool.execute(new CellsHandler(j));
            }
            synchronized (lock) {
                lock.wait();
            }
            for (int j = 0; j < spaceBase; j ++) {
                System.arraycopy(temp[j], 0, cellSpace[j], 0, spaceBase);
            }
            //display(cellSpace);
        }
        long eT = System.currentTimeMillis() - sT;
        logger.info("total time: {}",eT/1000.0 +"s");
    }

    /**
     * 结果展示
     *
     * @param cellSpace 需展示的棋盘
     */
    public static void display(int[][] cellSpace) {
        for (int[] line : cellSpace) {
            for (int cell : line) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return "Space{" +
                "numDivBlocks=" + numDivBlocks +
                ", numPerDiv=" + numPerDiv +
                ", spaceBase=" + spaceBase +
                ", evolutionAlg=" + evolutionAlg +
                '}';
    }
}
