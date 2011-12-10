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
	ScrollModel scrollModel;

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
		scrollModel = new ScrollModel(sizeModel);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testScrollModel() {
		assertEquals(0, scrollModel.getComponentSize());
		assertEquals(0, scrollModel.getValue());
		assertEquals(1, scrollModel.getExtent());
		assertEquals(0, scrollModel.getMinimum());
		assertEquals(10, scrollModel.getMaximum());
	}

	@Test
	public void testSetComponentSize() {
		scrollModel.setComponentSize(25);
		assertEquals(25, scrollModel.getComponentSize());
	}

	@Test
	public void testGetExtent() {
		//
		scrollModel.setValue(0);
		scrollModel.setComponentSize(0);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(1);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(9);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(10);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(11);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(20);
		assertEquals(2, scrollModel.calcExtent());
		assertEquals(2, scrollModel.getExtent());
		scrollModel.setComponentSize(21);
		assertEquals(2, scrollModel.getExtent());
		scrollModel.setComponentSize(25);
		assertEquals(2, scrollModel.getExtent());
		//
		scrollModel.setValue(3);
		scrollModel.setComponentSize(0);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(1);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(9);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(10);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(11);
		assertEquals(1, scrollModel.getExtent());
		scrollModel.setComponentSize(20);
		assertEquals(2, scrollModel.getExtent());
		scrollModel.setComponentSize(21);
		assertEquals(2, scrollModel.getExtent());
		scrollModel.setComponentSize(25);
		assertEquals(2, scrollModel.getExtent());
		scrollModel.setComponentSize(35);
		assertEquals(3, scrollModel.getExtent());
		scrollModel.setComponentSize(45);
		assertEquals(4, scrollModel.getExtent());
		scrollModel.setComponentSize(55);
		assertEquals(5, scrollModel.getExtent());
		assertEquals(3, scrollModel.getValue());
		scrollModel.setComponentSize(65);
		assertEquals(6, scrollModel.getExtent());
		assertEquals(3, scrollModel.getValue());
		scrollModel.setComponentSize(75);
		assertEquals(7, scrollModel.calcExtent());
		assertEquals(7, scrollModel.getExtent());
		assertEquals(3, scrollModel.getValue());
		scrollModel.setComponentSize(85);
		assertEquals(8, scrollModel.getExtent());
		assertEquals(2, scrollModel.getValue());
		scrollModel.setComponentSize(95);
		assertEquals(9, scrollModel.getExtent());
		assertEquals(1, scrollModel.getValue());
		scrollModel.setComponentSize(100);
		assertEquals(10, scrollModel.getExtent());
		assertEquals(0, scrollModel.getValue());
		scrollModel.setComponentSize(101);
		assertEquals(10, scrollModel.getExtent());
		assertEquals(0, scrollModel.getValue());
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
}
