package com;

import java.util.EnumSet;

public enum Planet {
	EARTH {
		@Override
		void doSomething() {
			
		}
	},MOON{
		@Override
		void doSomething() {
			
		}
	};	
	
	
	abstract void doSomething();
	
	public void doAny(){
		
		
	}
}
