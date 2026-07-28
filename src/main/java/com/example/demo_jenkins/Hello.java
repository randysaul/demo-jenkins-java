package com.example.demo_jenkins;

import javax.enterprise.context.Dependent;

@Dependent
public class Hello {
	String message = "Hello World";
	
	public String sayHello(){
		return message;
	} 

}
