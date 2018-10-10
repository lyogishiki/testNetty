package netty3.spring;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import netty3.spring.annotation.Action;
import netty3.spring.controller.UserController;

public class TestCase {

	@Test
	public void testCast() {
		Void v = Void.class.cast(null);
		System.out.println(v);
		
//	String str =	String.class.cast("123456");
//	System.out.println(str);
	}
	
	public static void main(String[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		UserController userController = new UserController();
		
		Method[] methods = userController.getClass().getDeclaredMethods();
		
		Map<String, Method> actionMap = new HashMap<>();
		
		for(Method method : methods) {
			if(method.isAnnotationPresent(Action.class)) {
				System.out.println(method);
				Action action = method.getDeclaredAnnotation(Action.class);
				String value = action.value();
				actionMap.put(value, method);
				
				if(value.equals("getUser")) {
					List<Object> list = new ArrayList<>();
					System.out.println(method.invoke(userController, list.toArray()));
				}else {
					List<String> list = Arrays.asList("zhangsan","123456");
					method.invoke(userController, list.toArray());
				}
			}
		}
		
		System.out.println(actionMap);
		
		
		
	}
	
}
