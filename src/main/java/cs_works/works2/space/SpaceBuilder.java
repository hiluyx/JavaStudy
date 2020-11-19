package cs_works.works2.space;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * @author best lu
 * @since 2020/11/17
 */
public class SpaceBuilder {
    private static final Logger logger = LoggerFactory.getLogger(SpaceBuilder.class);
    private File file;
    private final Space space = new Space();

    public static List<Cell> getDataSet(BufferedReader bufferedReader) throws Exception {
        List<Cell> listCell = new ArrayList<>();
        do {
            String str = bufferedReader.readLine();
            if (str == null) break;
            String[] xy = str.split(",");
            if (xy.length != 2) {
                //logger.error("init File Exception: each line contain x,y");
                throw new Exception("init File Exception: each line contain x,y");
            }
            int x = Integer.parseInt(xy[0]);
            int y = Integer.parseInt(xy[1]);
            logger.info("get data: {},{}",x,y);
            listCell.add(new Cell(x, y, 1));
        } while (true);
        return listCell;
    }

    /**
     * 输出结果到output.txt
     *
     * @throws IOException io异常
     */
    public void output2File() throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
        StringBuilder stringB = new StringBuilder();
        for (int i = 0; i < space.getSpaceBase(); i++) {
            for (int j = 0; j < space.getSpaceBase(); j++) {
                if (space.getCellSpace()[i][j] == 1) {
                    if (stringB.length() > 0) {
                        stringB.delete(0, stringB.length());
                    }
                    stringB.append(i).append(",").append(j);
                    bufferedWriter.write(stringB.toString());
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            }
        }
    }

    public SpaceBuilder setNumDivBlocks(int numDivBlocks) {
        space.setNumDivBlocks(numDivBlocks);
        return this;
    }
    public SpaceBuilder setSpaceBase(int cellBase) {
        space.setSpaceBase(cellBase);
        return this;
    }

    public SpaceBuilder setDataFile(String path){
        file = new File(path);
        return this;
    }

    public SpaceBuilder setEvolutionAlg(int evolutionAlg) {
        space.setEvolutionAlg(evolutionAlg);
        return this;
    }

    public SpaceBuilder setQueueSize(int size) {
        space.setQueueSize(size);
        return this;
    }

    public SpaceBuilder setCellSpace(int[][] cellSpace, List<Cell> data) {
        space.setCellSpace(cellSpace);
        for (Cell cell:data) {
            space.getCellSpace()[cell.getX()][cell.getY()] = 1;
        }
        return this;
    }

    public Space build() throws Exception {
        if (space.getNumDivBlocks() == 0 | space.getSpaceBase() == 0) {
            logger.error("初始化失败，数据不全");
            throw new Exception("初始化失败，初始化数据不全");
        } else {
            space.setNumPerDiv(space.getSpaceBase() / space.getNumDivBlocks());
            logger.info("{} \n----初始化成功----",space.toString());
            return space;
        }
    }
}
