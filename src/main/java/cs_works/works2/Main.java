package cs_works.works2;

import cs_works.works2.space.Cell;
import cs_works.works2.space.Space;
import cs_works.works2.space.SpaceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * @author best lu
 * @since 2020/11/10
 */
public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {
        String path = (Space.class.getClassLoader().getResource("") + "cs_works_data/in_1000.txt").substring(6);
        File file = new File(path);
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        // 初始化读取数据
        int spaceBase = Integer.parseInt(Objects.requireNonNull(bufferedReader.readLine()));
        int evolutionAlg = Integer.parseInt(Objects.requireNonNull(bufferedReader.readLine()));
        List<Cell> listCell = SpaceBuilder.getDataSet(bufferedReader);
        int[][] cellSpace = new int[spaceBase][spaceBase];
        // 建造space
        SpaceBuilder spaceBuilder = new SpaceBuilder();
        Space space = spaceBuilder.setDataFile(
                (Space.class.getClassLoader().getResource("") + "output.txt").substring(6))
                .setCellSpace(cellSpace,listCell) // 棋盘
                .setEvolutionAlg(evolutionAlg) // 进化代数
                .setNumDivBlocks(4) // 数据划分
                .setQueueSize(100000) // 缓冲队列
                .build();
        // 开始模拟
        space.evolve();
        // 数据保存
        spaceBuilder.output2File();
    }
}
