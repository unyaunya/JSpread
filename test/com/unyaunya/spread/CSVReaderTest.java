/**
 * 
 */
package com.unyaunya.spread;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author wata
 *
 */
public class CSVReaderTest {
	/**
	 * Test method for {@link com.unyaunya.spread.CSVReader#CSVReader(boolean)}.
	 */
	@Test
	public void testCSVReader() {
		CSVReader csv = new CSVReader();
		assertEquals(false, csv.getColTitleSource());
		csv = new CSVReader(true);
		assertEquals(true, csv.getColTitleSource());
		csv = new CSVReader(false);
		assertEquals(false, csv.getColTitleSource());
	}

	/**
	 * Test method for {@link com.unyaunya.spread.CSVReader#getTitleArrayList()}.
	 */
	@Test
	public void testGetTitleArrayList() {
		CSVReader csv = new CSVReader();
		StringReader reader = new StringReader(
				"1,1,1,1,1,1,1,1,1,1,1,1,1,1,1\n"+
				"2,2,2,2,2,2,2,2,2,2,2,2,2,2,2\n"+
				"3,3,3,3,3,3,3,3,3,3,3,3,3,3,3\n"+
				"4,4,4,4,4,4,4,4,4,4,4,4,4,4,4\n"+
				"5,5,5,5,5,5,5,5,5,5,5,5,5,5,5\n"+
				"6,6,6,6,6,6,6,6,6,6,6,6,6,6,6\n"+
				"7,7,7,7,7,7,7,7,7,7,7,7,7,7,7\n"+
				"8,8,8,8,8,8,8,8,8,8,8,8,8,8,8\n"+
				"9,9,9,9,9,9,9,9,9,9,9,9,9,9,9\n"+
				"10,10,10,10,10,10,10,10,10,10,10,10,10,10,10\n"+
				"11,11,11,11,11,11,11,11,11,11,11,11,11,11,11\n"+
				"12,12,12,12,12,12,12,12,12,12,12,12,12,12,12\n"+
				"13,13,13,13,13,13,13,13,13,13,13,13,13,13,13\n"+
				"14,14,14,14,14,14,14,14,14,14,14,14,14,14,14\n"+
				"15,15,15,15,15,15,15,15,15,15,15,15,15,15,15\n"+
				"16,16,16,16,16,16,16,16,16,16,16,16,16,16,16\n"+
				"17,17,17,17,17,17,17,17,17,17,17,17,17,17,17\n"+
				"18,18,18,18,18,18,18,18,18,18,18,18,18,18,18\n"+
				"19,19,19,19,19,19,19,19,19,19,19,19,19,19,19\n"+
				"20,20,20,20,20,20,20,20,20,20,20,20,20,20,20\n"+
				"21,21,21,21,21,21,21,21,21,21,21,21,21,21,21\n"+
				"22,22,22,22,22,22,22,22,22,22,22,22,22,22,22\n"+
				"23,23,23,23,23,23,23,23,23,23,23,23,23,23,23\n"+
				"24,24,24,24,24,24,24,24,24,24,24,24,24,24,24\n"+
				"25,25,25,25,25,25,25,25,25,25,25,25,25,25,25\n"+
				"26,26,26,26,26,26,26,26,26,26,26,26,26,26,26\n"+
				"27,27,27,27,27,27,27,27,27,27,27,27,27,27,27\n"+
				"28,28,28,28,28,28,28,28,28,28,28,28,28,28,28\n"
				);
		try {
			csv.read(new BufferedReader(reader));
			assertEquals(15, csv.getTitleArrayList().size());
			assertEquals(28, csv.getRowArrayList().size());
		} catch (IOException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}
}
