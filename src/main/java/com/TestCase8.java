package com;

import java.io.IOException;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;









import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.common.reflect.Invokable;

public class TestCase8 implements Serializable{

	private String name;
	private static String age;
	
	public static void main(@Nullable String[] args) throws NoSuchMethodException,
			SecurityException, IOException {

		Class<?> clazz = args.getClass();
		System.out.println(clazz);

		Method method = TestCase8.class.getDeclaredMethod("main",
				String[].class);

		Invokable<?, ?> invokable = Invokable.from(method);

		System.out.println(invokable.isPublic());
		System.out.println(invokable.isStatic());
		System.out.println(invokable.isFinal());
		System.out.println(invokable.isOverridable());

		// Modifier.isSynchronized(method.getModifiers())
		System.out.println(method.getDeclaringClass());

		Annotation[][] annotations = method.getParameterAnnotations();

		System.out.println(annotations);

		for(int i=0;i<annotations.length;i++){
			for(int j=0;j<annotations[i].length;j++){
				System.out.println(annotations[i][j]);
			}
		}
		
		System.out.println(TestCase8.class.getDeclaredClasses());
		
		printArray(TestCase8.class.getInterfaces());
		printArray(TestCase8.class.getDeclaredClasses());
//		Proxy.newProxyInstance(loader, , h)
		
		ImmutableSet<ClassInfo> ss = ClassPath.from(TestCase8.class.getClassLoader())
			.getAllClasses();
//		System.out.println(ss);
		ss.forEach(ci -> System.out.println(ci));
		test01(null);
	}
	
	public static void test01(@Nonnull String oldAss) throws NoSuchMethodException, SecurityException{
		
		Method method = TestCase8.class.getDeclaredMethod("test01", String.class);
		Nonnull nn = method.getParameters()[0].getAnnotation(Nonnull.class);
		
		System.out.println(nn.when());
		System.out.println(oldAss);
		
		//Objects.requireNonNull(oldAss, "require not null");
	}
	
	static <T> void printArray(T[] ts){
		for(T t : ts){
			System.out.println("-" + t);
		}
	}

}
