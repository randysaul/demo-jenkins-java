package com.example.demo_jenkins;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

public class HelloTest {

	Hello hola = new Hello();
	
	String mensaje = hola.sayHello();
	
	@Test
	public void probarClase(){
		assertEquals("Hello World", mensaje);
	}

}
