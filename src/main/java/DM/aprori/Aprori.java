package DM.aprori;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Aprori {
    static boolean endTag = false;
    static Map<Integer, Integer> dCountMap = new HashMap<>(); // k-1频繁集的记数表
    static Map<Integer, Integer> dkCountMap = new HashMap<>();// k频繁集的记数表
    static List<List<String>> record = new ArrayList<>();// 数据记录表
    final static double MIN_SUPPORT = 0.2;// 最小支持度
    final static double MIN_CONF = 0.8;// 最小置信度
    static int lable = 1;// 用于输出时的一个标记，记录当前在打印第几级关联集
    static List<Double> confCount = new ArrayList<>();// 置信度记录表
    static List<List<String>> confItemset = new ArrayList<>();// 满足支持度的集合

    public static void main(String[] args) {
        record = getRecord();// 获取原始数据记录
        List<List<String>> cItemset = findFirstCandidate();// 获取第一次的备选集
        List<List<String>> lItemset = getSupportedItemset(cItemset);// 获取备选集cItemset满足支持的集合

        while (!endTag) {// 只要能继续挖掘
            List<List<String>> ckItemset = getNextCandidate(lItemset);// 获取第下一次的备选集
            List<List<String>> lkItemset = getSupportedItemset(ckItemset);// 获取备选集cItemset满足支持的集合
            getConfidencedItemset(lkItemset, lItemset, dkCountMap, dCountMap);// 获取备选集cItemset满足置信度的集合
            if (confItemset.size() != 0)// 满足置信度的集合不为空
                printConfItemset(confItemset);// 打印满足置信度的集合
            confItemset.clear();// 清空置信度的集合
            cItemset = ckItemset;// 保存数据，为下次循环迭代准备
            lItemset = lkItemset;
            dCountMap.clear();
            dCountMap.putAll(dkCountMap);
        }
    }

    /**
     * @param confItemset2
     * 输出满足条件的频繁集
     */
    private static void printConfItemset(List<List<String>> confItemset2) {
        System.out.print("*********频繁模式挖掘结果***********\n");
        for (List<String> strings : confItemset2) {
            int j;
            for (j = 0; j < strings.size() - 3; j++)
                System.out.print(strings.get(j) + " ");
            System.out.print("--> ");
            System.out.print(strings.get(j++));
            System.out.print(" 相对支持度：" + strings.get(j++));
            System.out.print(" 置信度：" + strings.get(j) + "\n");
        }

    }

    /**
     * @param lkItemset l
     * @param lItemset l
     * @param dkCountMap2 d
     * @param dCountMap2 d
     */
    private static void getConfidencedItemset(
            List<List<String>> lkItemset, List<List<String>> lItemset,
            Map<Integer, Integer> dkCountMap2, Map<Integer, Integer> dCountMap2) {
        for (int i = 0; i < lkItemset.size(); i++) {
            getConfItem(lkItemset.get(i), lItemset, dkCountMap2.get(i),
                    dCountMap2);

        }
    }

    /**
     * @param list l
     * @param lItemset l
     * @param count c
     * @param dCountMap2
* 检验集合list是否满足最低自信度要求
* 若满足则在全局变量confItemset添加list
     */
    private static void getConfItem(List<String> list,
                                    List<List<String>> lItemset, Integer count,
                                    Map<Integer, Integer> dCountMap2) {
        for (int i = 0; i < list.size(); i++) {
            List<String> testList = new ArrayList<>();
            for (int j = 0; j < list.size(); j++)
                if (i != j)
                    testList.add(list.get(j));
            int index = findConf(testList, lItemset);//查找testList中的内容在lItemset的位置
            double conf = count * 1.0 / dCountMap2.get(index);
            if (conf > MIN_CONF) {//满足自信度要求
                testList.add(list.get(i));
                double relativeSupport = count * 1.0 / (record.size() - 1);
                testList.add(Double.toString(relativeSupport));
                testList.add(Double.toString(conf));
                confItemset.add(testList);//添加到满足自信度的集合中
            }
        }
    }

    /**
     * @param testList t
     * @param lItemset
     * 查找testList中的内容在lItemset的位置
     */
    private static int findConf(List<String> testList,
                                List<List<String>> lItemset) {
        for (int i = 0; i < lItemset.size(); i++) {
            boolean notHaveTag = false;
            for (String s : testList) {
                if (!haveThisItem(s, lItemset.get(i))) {
                    notHaveTag = true;
                    break;
                }
            }
            if (!notHaveTag)
                return i;
        }
        return -1;
    }

    /**
     * @param string s
     * @param list
     * 检验list中是否包含string
     * @return boolean
     */
    private static boolean haveThisItem(String string, List<String> list) {
        for (String s : list)
            if (string.equals(s))
                return true;
        return false;
    }
    /**
     获取数据库记录
     */
    private static List<List<String>> getRecord() {
        return TxtReader.getAproriRecord();
    }

    /**
     * @param cItemset
     * 求出cItemset中满足最低支持度集合
     */
    private static List<List<String>> getSupportedItemset(
            List<List<String>> cItemset) {
        boolean end = true;
        List<List<String>> supportedItemset = new ArrayList<>();
        int k = 0;
        for (List<String> strings : cItemset) {
            int count = countFrequent(strings);//统计记录数
            if (count >= MIN_SUPPORT * (record.size() - 1)) {// count值大于支持度与记录数的乘积，即满足支持度要求
                if (cItemset.get(0).size() == 1)
                    dCountMap.put(k++, count);
                else
                    dkCountMap.put(k++, count);
                supportedItemset.add(strings);
                end = false;
            }
        }
        endTag = end;
        return supportedItemset;
    }

    /**
     * @param list
     * 统计数据库记录record中出现list中的集合的个数
     */
    private static int countFrequent(List<String> list) {
        int count = 0;
        for (int i = 1; i < record.size(); i++) {
            boolean notHavaThisList = false;
            for (String s : list) {
                boolean thisRecordHave = false;
                for (int j = 1; j < record.get(i).size(); j++) {
                    if (s.equals(record.get(i).get(j))) {
                        thisRecordHave = true;
                        break;
                    }
                }
                if (!thisRecordHave) {// 扫描一遍记录表的一行，发现list.get(i)不在记录表的第j行中，即list不可能在j行中
                    notHavaThisList = true;
                    break;
                }
            }
            if (!notHavaThisList)
                count++;
        }
        return count;
    }

    /**
     * @param cItemset n
     * @return nextItemset
     * 根据cItemset求出下一级的备选集合组，求出的备选集合组中的每个集合的元素的个数比cItemset中的集合的元素大1
     */
    private static List<List<String>> getNextCandidate(
            List<List<String>> cItemset) {
        List<List<String>> nextItemset = new ArrayList<>();
        for (int i = 0; i < cItemset.size(); i++) {
            List<String> tempList = new ArrayList<>(cItemset.get(i));
            for (int h = i + 1; h < cItemset.size(); h++) {
                for (int j = 0; j < cItemset.get(h).size(); j++) {
                    tempList.add(cItemset.get(h).get(j));
                    if (isSubsetInC(tempList, cItemset)) {// tempList的子集全部在cItemset中
                        List<String> copyValueHelpList = new ArrayList<>(tempList);
                        if (isHave(copyValueHelpList, nextItemset))//nextItemset还没有copyValueHelpList这个集合
                            nextItemset.add(copyValueHelpList);
                    }
                    tempList.remove(tempList.size() - 1);
                }
            }
        }

        return nextItemset;
    }

    /**
     * @param copyValueHelpList n
     * @param nextItemset n
     * @return boolean
     * 检验nextItemset中是否包含copyValueHelpList
     */
    private static boolean isHave(List<String> copyValueHelpList,
                                  List<List<String>> nextItemset) {
        for (List<String> strings : nextItemset)
            if (copyValueHelpList.equals(strings))
                return false;
        return true;
    }

    /**
     * @param tempList n
     * @param cItemset n
     * @return
     *检验 tempList是不是cItemset的子集
     */
    private static boolean isSubsetInC(List<String> tempList,
                                       List<List<String>> cItemset) {
        boolean haveTag = false;
        for (int i = 0; i < tempList.size(); i++) {// k集合tempList的子集是否都在k-1级频繁级中
            List<String> testList = new ArrayList<>();
            for (int j = 0; j < tempList.size(); j++)
                if (i != j)
                    testList.add(tempList.get(j));
            for (List<String> strings : cItemset) {
                if (testList.equals(strings)) {// 子集存在于k-1频繁集中
                    haveTag = true;
                    break;
                }
            }
            if (!haveTag)// 其中一个子集不在k-1频繁集中
                return false;
        }

        return haveTag;
    }

    /**
     *根据数据库记录求出第一级备选集
     */
    private static List<List<String>> findFirstCandidate() {
        List<List<String>> tableList = new ArrayList<>();
        List<String> lineList = new ArrayList<>();

        int size;
        for (int i = 1; i < record.size(); i++) {
            for (int j = 1; j < record.get(i).size(); j++) {
                if (lineList.isEmpty()) {
                    lineList.add(record.get(i).get(j));
                } else {
                    boolean haveThisItem = false;
                    size = lineList.size();
                    for (int k = 0; k < size; k++) {
                        if (lineList.get(k).equals(record.get(i).get(j))) {
                            haveThisItem = true;
                            break;
                        }
                    }
                    if (!haveThisItem)
                        lineList.add(record.get(i).get(j));
                }
            }
        }
        for (String s : lineList) {
            List<String> helpList = new ArrayList<>();
            helpList.add(s);
            tableList.add(helpList);
        }
        return tableList;
    }

}
