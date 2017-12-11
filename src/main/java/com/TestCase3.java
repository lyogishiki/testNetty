package com;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class TestCase3 {
	
	public static void main(String[] args) {
		HashSet<Integer> sets1 = new HashSet<Integer>();
		
		sets1.add(1);
		sets1.add(2);
		sets1.add(3);
		sets1.add(4);
		sets1.add(5);
		//System.out.println(sets1);
		
		HashSet<Integer> sets2 = new HashSet<Integer>();
		sets2.add(2);
		sets2.add(4);
		sets2.add(6);
		
		//System.out.println(sets1.retainAll(sets2));
		
		//System.out.println(sets1);
//		ArrayList<Integer> al = null;
//		al.retainAll(c)
//		al.
		
		SetView<Integer> sets3 = Sets.intersection(sets1, sets2);
//		sets3.
		System.out.println(sets3.immutableCopy());
		
		SetView<Integer> sets4 = Sets.difference(sets1, sets2);
		System.out.println(sets4);
		
		SetView<Integer> sets5 = Sets.union(sets1, sets2);
		System.out.println(sets5);
		
		
	}

}
