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
public class ScrollModelTest {
	SizeModel sizeModel;
	RangeModel rangeModel;

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
		sizeModel = new SizeModel(10, 10);
		rangeModel = new RangeModel(sizeModel);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testScrollModel() {
		assertEquals(0, rangeModel.getComponentSize());
		assertEquals(0, rangeModel.getValue());
		assertEquals(1, rangeModel.getExtent());
		assertEquals(0, rangeModel.getMinimum());
		assertEquals(9, rangeModel.getMaximum());
	}

	@Test
	public void testSetComponentSize() {
		rangeModel.setComponentSize(25);
		assertEquals(25, rangeModel.getComponentSize());
	}

	@Test
	public void testGetExtent_NoScroll() {
		rangeModel.setValue(0);
		rangeModel.setComponentSize(0);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(1);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(9);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(10);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(11);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(20);
		assertEquals(1, rangeModel.calcExtent());
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(21);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(25);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(29);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(30);
		assertEquals(2, rangeModel.getExtent());
	}

	@Test
	public void testGetExtent_Scroll() {
		rangeModel.setValue(3);
		rangeModel.setComponentSize(0);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(1);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(9);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(10);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(29);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(30);
		assertEquals(2, rangeModel.getExtent());
		rangeModel.setComponentSize(31);
		assertEquals(2, rangeModel.getExtent());
		rangeModel.setComponentSize(35);
		assertEquals(2, rangeModel.getExtent());
		rangeModel.setComponentSize(45);
		assertEquals(3, rangeModel.getExtent());
		rangeModel.setComponentSize(55);
		assertEquals(4, rangeModel.getExtent());
		rangeModel.setComponentSize(65);
		assertEquals(5, rangeModel.getExtent());
		assertEquals(3, rangeModel.getValue());
		rangeModel.setComponentSize(75);
		assertEquals(6, rangeModel.getExtent());
		assertEquals(3, rangeModel.getValue());
		rangeModel.setComponentSize(85);
		assertEquals(7, rangeModel.calcExtent());
		assertEquals(7, rangeModel.getExtent());
		assertEquals(3, rangeModel.getValue());
		rangeModel.setComponentSize(95);
		assertEquals(8, rangeModel.getExtent());
		assertEquals(2, rangeModel.getValue());
		rangeModel.setComponentSize(105);
		assertEquals(9, rangeModel.getExtent());
		assertEquals(1, rangeModel.getValue());
		rangeModel.setComponentSize(110);
		assertEquals(10, rangeModel.getExtent());
		assertEquals(0, rangeModel.getValue());
		rangeModel.setComponentSize(111);
		assertEquals(10, rangeModel.getExtent());
		assertEquals(0, rangeModel.getValue());
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
}
