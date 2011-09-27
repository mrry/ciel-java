package com.asgow.ciel.examples.smithwaterman;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;

import com.asgow.ciel.executor.Ciel;
import com.asgow.ciel.references.Reference;
import com.asgow.ciel.references.WritableReference;
import com.asgow.ciel.tasks.ConstantNumOutputsTask;
import com.asgow.ciel.tasks.FirstClassJavaTask;

public class SmithWatermanBlockTask implements ConstantNumOutputsTask {

	private final Reference horizontalChunk;
	private final Reference verticalChunk;
	
	private final Reference upLeftNeighbour;
	private final Reference upNeighbour;
	private final Reference leftNeighbour;
	
	private final int insertionScore;
	private final int deletionScore;
	private final int mismatchScore;
	private final int matchScore;
	
	public SmithWatermanBlockTask(Reference horizontalChunk,
								  Reference verticalChunk,
								  Reference upLeftNeighbour,
			                      Reference upNeighbour,
			                      Reference leftNeighbour,
			                      int insertionScore,
			                      int deletionScore,
			                      int mismatchScore,
			                      int matchScore) {
		this.horizontalChunk = horizontalChunk;
		this.verticalChunk = verticalChunk;
		this.upLeftNeighbour = upLeftNeighbour;
		this.upNeighbour = upNeighbour;
		this.leftNeighbour = leftNeighbour;
		this.insertionScore = insertionScore;
		this.deletionScore = deletionScore;
		this.mismatchScore = mismatchScore;
		this.matchScore = matchScore;
	}
	
	public int getNumOutputs() {
		return 3;
	}
	
	public Reference[] getDependencies() {
		return new Reference[] { this.horizontalChunk, this.verticalChunk, this.upLeftNeighbour, this.upNeighbour, this.leftNeighbour };
	}

	public void invoke() throws Exception {

		// Read input chunks.
		int c;
		
		ByteArrayOutputStream horizontalChunkBuffer = new ByteArrayOutputStream();
		InputStream horizontalChunkInput = Ciel.RPC.getStreamForReference(this.horizontalChunk);
		while ((c = horizontalChunkInput.read()) != -1) {
			horizontalChunkBuffer.write(c);
		}
		horizontalChunkInput.close();
		byte[] horizontalChunkArray = horizontalChunkBuffer.toByteArray();
		
		//System.err.printf("Horizontal chunk is length: %d\n", horizontalChunkArray.length);
		
		ByteArrayOutputStream verticalChunkBuffer = new ByteArrayOutputStream();
		InputStream verticalChunkInput = Ciel.RPC.getStreamForReference(this.verticalChunk);
		while ((c = verticalChunkInput.read()) != -1) {
			verticalChunkBuffer.write(c);
		}
		verticalChunkInput.close();
		byte[] verticalChunkArray = verticalChunkBuffer.toByteArray();
		
		//System.err.printf("Vertical chunk is length: %d\n", verticalChunkArray.length);
		
		// Read in the up-left and up haloes (if available, otherwise set them to be zero).
		int[] previousRow = new int[horizontalChunkArray.length + 1];
		int left;
		if (this.upLeftNeighbour == null) {
			left = 0;
			previousRow[0] = 0;
		} else {
			DataInputStream upLeftNeighbourInput = new DataInputStream(Ciel.RPC.getStreamForReference(this.upLeftNeighbour));
			previousRow[0] = upLeftNeighbourInput.readInt();
			left = previousRow[0];
			upLeftNeighbourInput.close();
		}
		if (this.upNeighbour == null) {
			for (int i = 1; i <= horizontalChunkArray.length; ++i) {
				previousRow[i] = 0;
			}
		} else {
			DataInputStream upNeighbourInput = new DataInputStream(Ciel.RPC.getStreamForReference(this.upNeighbour));
			for (int i = 1; i <= horizontalChunkArray.length; ++i) {
				previousRow[i] = upNeighbourInput.readInt();
			}
			upNeighbourInput.close();
		}
		
		// If we don't have a left neighbour, stream in zeroes instead.
		DataInputStream leftNeighbourInput;
		if (this.leftNeighbour == null) {
			leftNeighbourInput = new DataInputStream(new ZeroInputStream());
		} else {
			leftNeighbourInput = new DataInputStream(Ciel.RPC.getStreamForReference(this.leftNeighbour));
		}
		
		// Output 2 is the right halo.
		WritableReference rightHalo = Ciel.RPC.getOutputFilename(2);
		DataOutputStream rightHaloOutput = new DataOutputStream(rightHalo.open());
		
		int[] currentRow = new int[previousRow.length];

		// Now actually execute the Smith-Waterman algorithm.
		for (int i = 0; i < verticalChunkArray.length; ++i) {
			int aboveLeft = left;
			left = leftNeighbourInput.readInt();
			
			previousRow[0] = aboveLeft;
			currentRow[0] = left;
			
			for (int j = 1; j <= horizontalChunkArray.length; ++j) {
				if (verticalChunkArray[i] == horizontalChunkArray[j-1]) {
					// Characters match at this position.
					currentRow[j] = previousRow[j-1] + matchScore;
				} else {
					// Characters don't match at this position.
					int bestOption = 0;
					if (bestOption < previousRow[j-1] + mismatchScore) {
						bestOption = previousRow[j-1] + mismatchScore;
					}
					if (bestOption < currentRow[j-1] + insertionScore) {
						bestOption = currentRow[j-1] + insertionScore;
					}
					if (bestOption < previousRow[j] + deletionScore) {
						bestOption = previousRow[j] + deletionScore;
					}
					currentRow[j] = bestOption;
				}
			}
			
			rightHaloOutput.writeInt(currentRow[horizontalChunkArray.length]);
			
			int[] temp;
			temp = currentRow;
			currentRow = previousRow;
			previousRow = temp;
		}
		
		rightHaloOutput.close();
		leftNeighbourInput.close();
		
		// Write output halos.
		{
			DataOutputStream bottomRightHaloOutputStream = new DataOutputStream(Ciel.RPC.getOutputFilename(0).open());
			bottomRightHaloOutputStream.writeInt(previousRow[horizontalChunkArray.length]);
			bottomRightHaloOutputStream.close();
		}
		
		{
			DataOutputStream bottomHaloOutputStream = new DataOutputStream(Ciel.RPC.getOutputFilename(1).open());
			for (int j = 1; j <= horizontalChunkArray.length; ++j) {
				bottomHaloOutputStream.writeInt(previousRow[j]);
			}
			bottomHaloOutputStream.close();
		}
				
	}

	public void setup() {

	}

}
