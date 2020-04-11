package co.jp.javatest;

public class BaseQuestion {
	
	public void objectCast(){
		Object obj = new String("STRING");
		System.out.println(obj);
		System.out.println("参照子类的父类可以不用强制类型转换");
	}
	
	public void Q18(){
		Food food = new ButterRice();
	}
	
	interface Food{}
	
	class Rice implements Food {}
	
	class ButterRice extends Rice {}
}
