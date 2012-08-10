/**
 * 
 */
package com.unyaunya.spread;

import static org.junit.Assert.*;

import javax.swing.JPanel;

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
		scrollModel = new ScrollModel(new JPanel());
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testScrollModel() {
		scrollModel = new ScrollModel(new JPanel());
		assertEquals(23, scrollModel.getDefaultRowHeight());
		assertEquals(60, scrollModel.getDefaultColumnWidth());
		assertEquals(1, scrollModel.getFixedRowNum());
		assertEquals(1, scrollModel.getFixedColumnNum());
	}
	
	
	@Test
	public void test() {
		//fail("Not yet implemented");
	}
}
