/**
 * 
 */
package com.unyaunya.spread;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author wata
 *
 */
public class SizeModelTest {
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#setSizes(int[])}.
	 */
	@Test
	public void testSetSizesIntArray() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#getIndex(int)}.
	 */
	@Test
	public void testGetIndex() {
		SizeModel sizeModel = new SizeModel(10, 10);
		assertEquals(0, sizeModel.getIndex(-1));
		assertEquals(0, sizeModel.getIndex(0));
		assertEquals(0, sizeModel.getIndex(9));
		assertEquals(1, sizeModel.getIndex(10));
		assertEquals(1, sizeModel.getIndex(11));
		assertEquals(1, sizeModel.getIndex(19));
		assertEquals(2, sizeModel.getIndex(20));
		assertEquals(3, sizeModel.getIndex(30));
		assertEquals(9, sizeModel.getIndex(90));
		assertEquals(9, sizeModel.getIndex(99));
		assertEquals(10, sizeModel.getIndex(100));
		assertEquals(10, sizeModel.getIndex(101));
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#getPosition(int)}.
	 */
	@Test
	public void testGetPosition() {
		SizeModel sizeModel = new SizeModel(10, 10);
		assertEquals(0, sizeModel.getPosition(-1));
		assertEquals(0, sizeModel.getPosition(0));
		assertEquals(10, sizeModel.getPosition(1));
		assertEquals(20, sizeModel.getPosition(2));
		assertEquals(90, sizeModel.getPosition(9));
		assertEquals(100, sizeModel.getPosition(10));
		assertEquals(100, sizeModel.getPosition(11));
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#SizeModel()}.
	 */
	@Test
	public void testSizeModel() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#SizeModel(int)}.
	 */
	@Test
	public void testSizeModelInt() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#SizeModel(int[])}.
	 */
	@Test
	public void testSizeModelIntArray() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#SizeModel(int, int)}.
	 */
	@Test
	public void testSizeModelIntInt() {
		SizeModel sizeModel = new SizeModel(10, 10);
		assertEquals(10, sizeModel.getLength());
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#getLength()}.
	 */
	@Test
	public void testGetLength() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.unyaunya.spread.SizeModel#getPreferredSize()}.
	 */
	@Test
	public void testGetPreferredSize() {
		//fail("Not yet implemented");
	}

}
