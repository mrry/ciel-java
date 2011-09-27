package com.asgow.ciel.examples;

import java.io.IOException;

import com.asgow.ciel.references.Reference;
import com.asgow.ciel.executor.Ciel;
import com.asgow.ciel.tasks.FirstClassJavaTask;

public class HelloWorld implements FirstClassJavaTask {

    public Reference[] getDependencies() {
	return new Reference[0];
    }

    public void setup() {

    }

    public void invoke() throws IOException {
	System.out.println("Running the HelloWorld Java task!");
	for (int i = 0; i < Ciel.args.length; ++i)
	    System.out.println("Ciel.args[" + i + "] = " + Ciel.args[i]);
	Ciel.returnPlainString("Hello, world!");
    }

}
