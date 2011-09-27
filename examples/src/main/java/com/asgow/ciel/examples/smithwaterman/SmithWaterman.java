package com.asgow.ciel.examples.smithwaterman;

import com.asgow.ciel.executor.Ciel;
import com.asgow.ciel.references.Reference;
import com.asgow.ciel.tasks.FirstClassJavaTask;

public class SmithWaterman implements FirstClassJavaTask {

	public void invoke() throws Exception {
		
		Reference horizontalString = Ciel.RPC.packageLookup("x");
		Reference verticalString = Ciel.RPC.packageLookup("y");
		
		int numHorizontalChunks = Integer.parseInt(Ciel.args[0]);
		int numVerticalChunks = Integer.parseInt(Ciel.args[1]);
		
		Reference[] horizontalChunks = Ciel.spawn(new PartitionInputString(horizontalString, numHorizontalChunks));
		Reference[] verticalChunks = Ciel.spawn(new PartitionInputString(verticalString, numVerticalChunks));
		
		Reference[][][] taskResults = new Reference[numVerticalChunks][numHorizontalChunks][];
		taskResults[0][0] = Ciel.spawn(new SmithWatermanBlockTask(horizontalChunks[0], verticalChunks[0],
				                                                  null, null, null, -1, -1, -1, 1));
		
		for (int j = 1; j < horizontalChunks.length; ++j) {
			taskResults[0][j] = Ciel.spawn(new SmithWatermanBlockTask(horizontalChunks[j], verticalChunks[0],
					                                                  null, null, taskResults[0][j-1][2], -1, -1, -1, 1));
		}
		
		for (int i = 1; i < verticalChunks.length; ++i) {
			taskResults[i][0] = Ciel.spawn(new SmithWatermanBlockTask(horizontalChunks[0], verticalChunks[i],
					                                                  null, taskResults[i-1][0][1], null, -1, -1, -1, 1));
			
			for (int j = 1; j < horizontalChunks.length; ++j) {
				taskResults[i][j] = Ciel.spawn(new SmithWatermanBlockTask(horizontalChunks[j], verticalChunks[i],
						                                                  taskResults[i-1][j-1][0], taskResults[i-1][j][1], taskResults[i][j-1][2], -1, -1, -1, 1));
				
			}
		}
		
		Ciel.tailSpawn(new SmithWatermanResultTask(taskResults[verticalChunks.length-1][horizontalChunks.length-1][0]));
	}

	public void setup() {

	}

	public Reference[] getDependencies() {
		return new Reference[0];
	}
	
}
