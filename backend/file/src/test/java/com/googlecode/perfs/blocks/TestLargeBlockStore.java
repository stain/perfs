package com.googlecode.perfs.blocks;

import static org.junit.Assert.assertTrue;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.Ignore;
import org.junit.Test;

import com.googlecode.perfs.blocks.LargeBlockStore;
import com.googlecode.perfs.blocks.SimpleBlockStore;

public class TestLargeBlockStore extends TestBlockStore {
	
	@Override
	public LargeBlockStore makeBlockStore() {
		SimpleBlockStore blockStore = new SimpleBlockStore(dir);
		return new LargeBlockStore(blockStore);
	}
	
	@Ignore
	@Test
	public void putLargeStuff() throws IOException {
		LargeBlockStore blockStore = makeBlockStore();
		byte[] largeData = new byte[blockStore.blocksize*4-1];
		Arrays.fill(largeData, (byte)'x');
		largeData[0] = 'a';
		largeData[largeData.length-1] = 'b';
		String id = blockStore.put(largeData);
		
		byte[] otherData = IOUtils.toByteArray(blockStore.get(id));
		assertNotSame(largeData, otherData);		
		assertTrue(Arrays.equals(largeData, otherData));
	}
	
	@Ignore	
	@Test
	public void putTwoLargeStuff() throws IOException {
		LargeBlockStore blockStore = makeBlockStore();
		byte[] largeData = new byte[blockStore.blocksize*4];
		Arrays.fill(largeData, (byte)'x');
		largeData[0] = '0';
		largeData[largeData.length-1] = '1';
		String id1 = blockStore.put(largeData);
		largeData[0] = '2';
		largeData[largeData.length-1] = '3';
		String id2 = blockStore.put(largeData);
		assertTrue(Arrays.equals(largeData, IOUtils.toByteArray(blockStore.get(id2))));
		assertFalse(Arrays.equals(largeData, IOUtils.toByteArray(blockStore.get(id1))));
	}
	
	@Ignore
	@Test
	public void manyBlocks() throws IOException {
		SimpleBlockStore backendStore = new SimpleBlockStore(dir);
		// Small blocksize
		int blockSize = 256;
		LargeBlockStore blockStore = new LargeBlockStore(backendStore, blockSize);
		assertEquals(blockSize, blockStore.blocksize);
		// Since one block id is 41 chars, "ae6464dbd630f2df2e435adaa9467c0c61c17e64\n",
		// we'll make sure we have more than 40 blocks worth of data
		// Max bytes 
		int largeSize = blockSize/41 * blockSize;
		byte[] largeData = new byte[largeSize*3];
		Arrays.fill(largeData, (byte)'x');
		largeData[0] = '4';
		largeData[largeData.length-1] = '5';
		String id = blockStore.put(largeData);
		byte[] otherData = IOUtils.toByteArray(blockStore.get(id));
		assertNotSame(largeData, otherData);		
		assertTrue(Arrays.equals(largeData, otherData));
	}

}
