/**
 * 
 */
package com.unyaunya.grid;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unyaunya.grid.ScrollModel;
import com.unyaunya.grid.table.GridTableModel;
import com.unyaunya.swing.JGrid;

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
		scrollModel = new ScrollModel(new JGrid(new GridModel(new GridTableModel())), 0, 0);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testScrollModel() {
		assertEquals(23, scrollModel.getRows().getDefaultHeight());
		assertEquals(60, scrollModel.getColumns().getDefaultWidth());
		assertEquals(0, scrollModel.getRows().getCountOfFixedPart());
		assertEquals(0, scrollModel.getColumns().getCountOfFixedPart());
	}
	
	
	@Test
	public void test() {
		//fail("Not yet implemented");
	}
}
