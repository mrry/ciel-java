package com.asgow.ciel.examples.smithwaterman;

import java.io.DataInputStream;

import com.asgow.ciel.executor.Ciel;
import com.asgow.ciel.references.Reference;
import com.asgow.ciel.tasks.ConstantNumOutputsTask;

public class SmithWatermanResultTask implements ConstantNumOutputsTask {

	private final Reference result;
	
	public SmithWatermanResultTask(Reference result) {
		this.result = result;
	}
	
	public Reference[] getDependencies() {
		return new Reference[] { this.result };
	}
	
	public int getNumOutputs() {
		return 1;
	}

	public void invoke() throws Exception {
		DataInputStream in = new DataInputStream(Ciel.RPC.getStreamForReference(this.result));
		int result = in.readInt();
		Ciel.returnPlainString("Result score: " + result);
	}

	public void setup() {
		
	}

}
