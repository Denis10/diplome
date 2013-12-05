package ua.edu.donntu.cs.service;

import java.io.IOException;

public class TestRef {
	TestRef(){
	try {
		Process process = Runtime.getRuntime().exec("explorer");
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	}
}
