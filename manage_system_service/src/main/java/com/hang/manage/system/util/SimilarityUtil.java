package com.hang.manage.system.util;


import com.hankcs.hanlp.HanLP;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SimilarityUtil {


    //余弦相似度
    public static double getSimilarity(String s1, String s2) {
        if (StringUtils.isEmpty(s1) && StringUtils.isEmpty(s2)) {
            return 1;
        }
        if (StringUtils.isEmpty(s1) || StringUtils.isEmpty(s2)) {
            return 0;
        }

        //清洗文本
        String resultStr1= s1.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");
        String resultStr2= s1.replaceAll("[\\pP+~$`^=|<>～｀＄＾＋＝｜＜＞￥×]", "");

//        String regEx="[。？！?.!，。 ]";
//        Pattern p = Pattern.compile(regEx);
        List<String> list1 = HanLP.segment(resultStr1).stream().map(a -> a.word).filter(s -> !"`~!@#$^&*()=|{}':;',./?~！@#￥……&*（）——|‘；：”“'。，、？ ".contains(s)).collect(Collectors.toList());
        List<String> list2 = HanLP.segment(resultStr2).stream().map(a -> a.word).filter(s -> !"`~!@#$^&*()=|{}':;',./?~！@#￥……&*（）——|‘；：”“'。，、？ ".contains(s)).collect(Collectors.toList());
        /*按照句子结束符分割句子*/
//    List<String> list1 = Arrays.asList(p.split(s1));
//
//    List<String> list2 = Arrays.asList(p.split(s2));

        return getSimilarity(list1, list2);
    }

    // 切好词的集合, 直接比较
    public static double getSimilarity(List<String> word1, List<String> word2) {
        if ((null == word1  && null == word2)) {
            return 1;
        }
        if (null == word1  || null == word2) {
            return 0;
        }
        if (word1.size() == 0 && word2.size() == 0) {
            return 1;
        }
        if (word1.size() == 0 || word2.size() == 0) {
            return 0;
        }
        // 算出每个词在自己的句子中出现几次
        Map<String, AtomicInteger> map1 = getFrequency(word1);
        Map<String, AtomicInteger> map2 = getFrequency(word2);
        // 得到两个句子中的所有词语
        Set<String> allWords = new HashSet<>();
        allWords.addAll(word1);
        allWords.addAll(word2);
        AtomicInteger ab = new AtomicInteger();
        AtomicInteger aa = new AtomicInteger();
        AtomicInteger bb = new AtomicInteger();
        // 循环所有词,计算每个词出现的频率,计算向量;
        for (String word : allWords) {
            //看同一词在a、b两个集合出现的次数
            AtomicInteger atomicInteger1 = map1.get(word);
            int x1 = 0;
            if (null != atomicInteger1) {
                x1 = atomicInteger1.intValue();
            }
            AtomicInteger atomicInteger2 = map2.get(word);
            int x2 = 0;
            if (null != atomicInteger2) {
                x2 = atomicInteger2.intValue();
            }
            //x1 * x2
            int f0 = x1 * x2;
            ab.addAndGet(f0);
            //(x1)^2
            int f1 = x1 * x1;
            aa.addAndGet(f1);
            //(x2)^2
            int f2 = x2 * x2;
            bb.addAndGet(f2);
        }
        //|a| 对aa开方
        double aaa = Math.sqrt(aa.doubleValue());
        //|b| 对bb开方
        double bbb = Math.sqrt(bb.doubleValue());
        //使用BigDecimal保证精确计算浮点数
        //double aabb = aaa * bbb;
        BigDecimal aabb = BigDecimal.valueOf(aaa).multiply(BigDecimal.valueOf(bbb));
        //similarity=a.b/|a|*|b|
        //divide参数说明：aabb被除数,2表示小数点后保留2位，最后一个表示用标准的四舍五入法
        double cos = BigDecimal.valueOf(ab.get()).divide(aabb, 10, BigDecimal.ROUND_HALF_UP).doubleValue();
        return cos;
    }

    // 获取每个词语在集合中出现的次数
    private static Map<String, AtomicInteger> getFrequency(List<String> wordList) {
        Map<String, AtomicInteger> freq = new HashMap<>();
        wordList.forEach(word -> freq.computeIfAbsent(word, k -> new AtomicInteger()).incrementAndGet());

        return freq;
    }

}
