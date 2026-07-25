package com.example.demo_jenkins;

import static org.junit.Assert.assertEquals;

import javax.inject.Inject;

import org.junit.Test;

public class HelloTest {
	
	@Inject
	Hello hola;
	
	String mensaje = hola.sayHello();
	
	@Test
	void probarClase(){
		assertEquals("Hello World", mensaje);
	}

}
