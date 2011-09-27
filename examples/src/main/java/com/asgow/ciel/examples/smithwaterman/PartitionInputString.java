package com.asgow.ciel.examples.smithwaterman;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.asgow.ciel.executor.Ciel;
import com.asgow.ciel.references.Reference;
import com.asgow.ciel.tasks.ConstantNumOutputsTask;
import com.asgow.ciel.tasks.FirstClassJavaTask;

public class PartitionInputString implements ConstantNumOutputsTask {

	private final Reference input;
	private final int numChunks;
	
	public PartitionInputString(Reference input, int numChunks) {
		this.input = input;
		this.numChunks = numChunks;
	}
	
	public int getNumOutputs() {
		return this.numChunks;
	}
	
	public void invoke() throws Exception {
		
			/**
			 * inputs: full input string. Whole thing will be read into memory.
			 */
			ByteArrayOutputStream inputStringBuffer = new ByteArrayOutputStream();
			InputStream in = Ciel.RPC.getStreamForReference(this.input);
			int c;
			while ((c = in.read()) != -1) {
				inputStringBuffer.write(c);
			}
			byte[] inputString = inputStringBuffer.toByteArray();
			in.close();
			
			/**
			 * outputs: n output blocks. 
			 */
			int blockLength = (inputString.length / this.numChunks) + ((inputString.length % this.numChunks == 0) ? 0 : 1);
			
			int currentPos = 0;
			for (int i = 0; i < this.numChunks; ++i) {
				OutputStream out = Ciel.RPC.getOutputFilename(i).open();
				for (int j = 0; j < blockLength && currentPos < inputString.length; ++j, ++currentPos) {
					out.write(inputString[currentPos]);
				}
				out.close();
			}
		
	}

	public Reference[] getDependencies() {
		return new Reference[] { this.input };
	}

	public void setup() {
		
	}

}
