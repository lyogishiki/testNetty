package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public class TestCase4 {

	public static void main(String[] args) {
		
		ArrayList<Integer> set1 = new ArrayList<Integer>();
		for(int i=0,max=100*100*10;i<max;i++){
			set1.add(i);
		}
		
		ImmutableList<Integer> set2 = ImmutableList.copyOf(set1);
		System.out.println("testList1");
		System.out.println("ArrayList");
		testSet(100*100*10,set1);
		System.out.println("ImmutableList");
		testSet(100*100*10,set2);
		
		System.out.println("testList2");
		System.out.println("ArrayList");
		testSet2(100*100*10,set1);
		System.out.println("ImmutableList");
		testSet2(100*100*10,set2);
		
		System.out.println("testList3");
		System.out.println("ArrayList");
		testSet3(100*100*10,set1);
		System.out.println("ImmutableList");
		testSet3(100*100*10,set2);
		
		System.out.println("testList4");
		System.out.println("ArrayList");
		testSet4(100*100*10,set1);
		System.out.println("ImmutableList");
		testSet4(100*100*10,set2);
		
		System.out.println("=============================华丽的分割线=============================");
		
		System.out.println("testList1");
		System.out.println("ArrayList");
		testSet(100*100*100,set1);
		System.out.println("ImmutableList");
		testSet(100*100*100,set2);
		
		System.out.println("testList2");
		System.out.println("ArrayList");
		testSet2(100*100*100,set1);
		System.out.println("ImmutableList");
		testSet2(100*100*100,set2);
		
		System.out.println("testList3");
		System.out.println("ArrayList");
		testSet3(100*100*100,set1);
		System.out.println("ImmutableList");
		testSet3(100*100*100,set2);
		
		System.out.println("testList4");
		System.out.println("ArrayList");
		testSet4(100*100*100,set1);
		System.out.println("ImmutableList");
		testSet4(100*100*100,set2);
	}

	private static void testSet4(int num, List<Integer> set) {
		long start = System.currentTimeMillis();
		long tmp = 0;
		for(int i=0;i<num;i++){
			tmp = set.parallelStream().mapToLong(e -> (long)e)
					.sum();
		}
		
		System.out.println(tmp);
		long end = System.currentTimeMillis();
		long ms = end - start;
		double perMs = (double)num / ms;
		System.out.println("耗时:"+ms+",每豪秒:"+perMs);
	}
	
	private static void testSet3(int num, List<Integer> set) {
		long start = System.currentTimeMillis();
		long tmp = 0;
		for(int i=0;i<num;i++){
			tmp = set.stream().mapToLong(e -> (long)e)
					.sum();
		}
		
		System.out.println(tmp);
		long end = System.currentTimeMillis();
		long ms = end - start;
		double perMs = (double)num / ms;
		System.out.println("耗时:"+ms+",每豪秒:"+perMs);
	}

	private static void testSet2(int num, List<Integer> set) {
		long start = System.currentTimeMillis();
		long sum = 0;
		
		for(int i=0;i<num;i++){
			sum = 0;
			
			for(int j=0;j<set.size();j++){
				sum += set.get(j);
			}
		}
		
		System.out.println(sum);
		long end = System.currentTimeMillis();
		long ms = end - start;
		double perMs = (double)num / ms;
		System.out.println("耗时:"+ms+",每豪秒:"+perMs);
	}

	private static void testSet(int num,List<Integer> set) {
		long start = System.currentTimeMillis();
		long tmp = 0;
		for(int i=0;i<num;i++){
			tmp = 0;
			Iterator<Integer> it = set.iterator();
			while(it.hasNext()){
				tmp += it.next();
			}
		}
		System.out.println(tmp);
		long end = System.currentTimeMillis();
		long ms = end - start;
		double perMs = (double)num / ms;
		System.out.println("耗时:"+ms+",每豪秒:"+perMs);
	}

}
