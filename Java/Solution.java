package Java;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.io.*;

/*

    intervals[i][0] = start point of i'th interval
    intervals[i][1] = finish point of i'th interval

*/
public class Solution {
    public static ArrayList<ArrayList<Integer>> mergeIntervals(ArrayList<ArrayList<Integer>> intervals) {
        // WRITE YOUR CODE HERE

        TreeMap<Integer, Integer> intvlMap = new TreeMap<>();
        ArrayList<ArrayList<Integer>>res= new ArrayList<>();
        intervals.stream().forEach(arr -> intvlMap.put(arr.get(0), arr.get(1)));
        System.out.println(intvlMap);
        AtomicInteger start = new AtomicInteger(intvlMap.firstKey());
        AtomicInteger end = new AtomicInteger(intvlMap.firstEntry().getValue());
        intervals.stream()
                .sorted((a, b) -> Integer.compare(a.get(0), b.get(0))).forEach(

                        e -> {
                            System.out.println("element " + e.get(0) + " " + e.get(1));
                            start.set(Math.min(start.get(), e.get(0)));
                            //end.set(end.get()==Integer.MIN_VALUE?e.get(1):end.get());
                            if(e.get(0)>=start.get() && e.get(0)<=end.get())
                            end.set(Math.max(end.get(),e.get(1)));
                            else
                            {
                                    res.add(new ArrayList<Integer>(Arrays.asList(start.get(),end.get())));
                                    start.set(e.get(0));
                                    end.set(e.get(1));
                            }
                        })
        // .forEach(a-> {
        // Integer start1= Math.min(start, a.get(0));
        // }

        ;
        res.add(new ArrayList<Integer>(Arrays.asList(start.get(),end.get())));
        return res;
    }

    public static void main(String arges[]) {
        ArrayList<ArrayList<Integer>> interval = new ArrayList();
        interval.add(new ArrayList<Integer>(Arrays.asList(1, 3)));
        interval.add(new ArrayList<Integer>(Arrays.asList(2, 7)));
        interval.add(new ArrayList<Integer>(Arrays.asList(3, 5)));
        interval.add(new ArrayList<Integer>(Arrays.asList(1, 8)));
        interval.add(new ArrayList<Integer>(Arrays.asList(9, 10)));
        interval.add(new ArrayList<Integer>(Arrays.asList(5, 11)));
        interval.add(new ArrayList<Integer>(Arrays.asList(11, 12)));
        interval.add(new ArrayList<Integer>(Arrays.asList(7,10)));
        interval.add(new ArrayList<Integer>(Arrays.asList(12, 12)));
        interval.add(new ArrayList<Integer>(Arrays.asList(14, 17)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(9, 10)));
        // interval.add(new ArrayList<Integer>(Arrays.asList(5, 11)));

        System.out.println("response - " +mergeIntervals(interval));

    }
}