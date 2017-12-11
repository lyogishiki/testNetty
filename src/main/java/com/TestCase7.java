package com;

import org.apache.commons.math3.util.MathUtils;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import com.google.common.math.DoubleMath;
import com.google.common.math.IntMath;
import com.google.common.math.LongMath;

public class TestCase7 {

	public static void main(String[] args) {
		HashFunction hf = 
				Hashing.md5();
		
		HashCode hc = hf.newHasher()
		.putLong(111)
		.putString("", Charsets.UTF_8)
		.putString("", Charsets.UTF_8)
		.putDouble(15.0)
		.hash();
		
		System.out.println(hc);
		System.out.println(hc.asInt());
		System.out.println(hc.asLong());
		
//		Math.
//		IntMath.c
		
//		LongMath.che
//		MathUtils.
//		ImmutableSet.of(e1, e2, e3, e4)
	}
	
}
