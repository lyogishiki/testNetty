package com;

import java.util.EnumSet;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public enum Planet {
	EARTH {
		@Override
		void doSomething() {

		}
	},
	MOON {
		@Override
		void doSomething() {

		}
	};

	abstract void doSomething();

	public void doAny() {

	}

	static class User {
		public int id;
		public String name;

		public User(int id, String name) {
			super();
			this.id = id;
			this.name = name;
		}

	}

	public static void main(String[] args) {
		User user1 = new User(1, "zhangsan");
		User user2 = new User(2, "lisi");
		JSONArray array = new JSONArray();
		array.add(user1);
		array.add(user2);
		System.out.println(array.toJSONString());
	}
	
}
