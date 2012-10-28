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

import com.unyaunya.grid.ScrollRangeModel;

/**
 * @author wata
 *
 */
public class ScrollRangeModelTest {
	ScrollRangeModel rangeModel;

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
		rangeModel = new ScrollRangeModel(0);
		rangeModel.reset(10, 10);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRangeModel() {
		assertEquals(0, rangeModel.getComponentSize());
		assertEquals(0, rangeModel.getFixedPartSize());
		assertEquals(0, rangeModel.getValue());
		assertEquals(1, rangeModel.getExtent());
		assertEquals(0, rangeModel.getMinimum());
		assertEquals(10, rangeModel.getMaximum());
		assertEquals(0, rangeModel.getFixedPartNum());
	}

	@Test
	public void testSetComponentSize() {
		rangeModel = new ScrollRangeModel(0);
		rangeModel.reset(201, 16);
		rangeModel.setComponentSize(524);
		assertEquals(524, rangeModel.getComponentSize());
		assertEquals(0, rangeModel.getFixedPartSize());
	}

	@Test
	public void testGetExtent_NoScroll() {
		rangeModel = new ScrollRangeModel(0);
		rangeModel.reset(10, 10);
		//
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
		assertEquals(2, rangeModel.calcExtent());
		assertEquals(2, rangeModel.getExtent());
		rangeModel.setComponentSize(21);
		assertEquals(2, rangeModel.getExtent());
		rangeModel.setComponentSize(25);
		assertEquals(2, rangeModel.getExtent());
		rangeModel.setComponentSize(29);
		assertEquals(2, rangeModel.getExtent());
		rangeModel.setComponentSize(30);
		assertEquals(3, rangeModel.getExtent());
	}

	@Test
	public void testGetExtent_Scroll() {
		//extentは、コンポーネントに表示可能な量を示す。
		
		//headerSize = 0
		rangeModel = new ScrollRangeModel(0);
		rangeModel.reset(10, 10);
		assertEquals(0, rangeModel.getHeaderSize());
		assertEquals(100, rangeModel.getPreferredSize());
		
		//extentの最低値は1．コンポーネント領域がゼロでも1を返す。
		rangeModel.setValue(3);
		rangeModel.setComponentSize(0);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(1);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(9);
		assertEquals(1, rangeModel.getExtent());
		rangeModel.setComponentSize(10);
		assertEquals(1, rangeModel.getExtent());
		//
		rangeModel.setComponentSize(29);
		assertEquals(2, rangeModel.getExtent());
		rangeModel.setComponentSize(30);
		assertEquals(3, rangeModel.getExtent());
		rangeModel.setComponentSize(31);
		assertEquals(3, rangeModel.getExtent());
		rangeModel.setComponentSize(35);
		assertEquals(3, rangeModel.getExtent());
		rangeModel.setComponentSize(45);
		assertEquals(4, rangeModel.getExtent());
		rangeModel.setComponentSize(55);
		assertEquals(5, rangeModel.getExtent());
		
		rangeModel.setComponentSize(65);
		assertEquals(6, rangeModel.getExtent());
		assertEquals(3, rangeModel.getValue());
		rangeModel.setComponentSize(75);
		assertEquals(7, rangeModel.getExtent());
		assertEquals(3, rangeModel.getValue());
		
		rangeModel.setComponentSize(85);
		assertEquals(8, rangeModel.calcExtent());
		assertEquals(8, rangeModel.getExtent());
		assertEquals(2, rangeModel.getValue());
		rangeModel.setComponentSize(95);
		assertEquals(9, rangeModel.getExtent());
		assertEquals(1, rangeModel.getValue());

		//いくら領域が多くなっても、extentの最大値は最大行数
		rangeModel.setComponentSize(105);
		assertEquals(10, rangeModel.getExtent());
		assertEquals(0, rangeModel.getValue());
		rangeModel.setComponentSize(110);
		assertEquals(10, rangeModel.getExtent());
		assertEquals(0, rangeModel.getValue());
		rangeModel.setComponentSize(111);
		assertEquals(10, rangeModel.getExtent());
		assertEquals(0, rangeModel.getValue());
	}

	@Test
	public void testGetIndex() {
		rangeModel = new ScrollRangeModel(0);
		rangeModel.reset(201, 16);
		rangeModel.setComponentSize(524);
		assertEquals(0, rangeModel.getIndex(0));
		assertEquals(0, rangeModel.getIndex(15));
		assertEquals(1, rangeModel.getIndex(16));
		assertEquals(32, rangeModel.getIndex(523));
		//
		rangeModel.setValue(10);
		assertEquals(10, rangeModel.getIndex(rangeModel.viewToModel(0)));
		assertEquals(10, rangeModel.getIndex(rangeModel.viewToModel(15)));
		assertEquals(11, rangeModel.getIndex(rangeModel.viewToModel(16)));
		assertEquals(42, rangeModel.getIndex(rangeModel.viewToModel(523)));

	}

	@Test
	public void testTranslate() {
		rangeModel = new ScrollRangeModel(0);
		rangeModel.reset(201, 16);
		rangeModel.setComponentSize(524);
		assertEquals(0, rangeModel.viewToModel(0));
		assertEquals(15, rangeModel.viewToModel(15));
		assertEquals(16, rangeModel.viewToModel(16));
		assertEquals(31, rangeModel.viewToModel(31));
		assertEquals(32, rangeModel.viewToModel(32));
		assertEquals(523, rangeModel.viewToModel(523));
		//
		rangeModel.setValue(10);
		assertEquals(0+160, rangeModel.viewToModel(0));
		assertEquals(15+160, rangeModel.viewToModel(15));
		assertEquals(16+160, rangeModel.viewToModel(16));
		assertEquals(31+160, rangeModel.viewToModel(31));
		assertEquals(32+160, rangeModel.viewToModel(32));
		assertEquals(523+160, rangeModel.viewToModel(523));
		//
		rangeModel.setFixedPartNum(5);
		assertEquals(0, rangeModel.viewToModel(0));
		assertEquals(15, rangeModel.viewToModel(15));
		assertEquals(16, rangeModel.viewToModel(16));
		assertEquals(31, rangeModel.viewToModel(31));
		assertEquals(32, rangeModel.viewToModel(32));
		assertEquals(79, rangeModel.viewToModel(79));
		assertEquals(80+160, rangeModel.viewToModel(80));
		assertEquals(81+160, rangeModel.viewToModel(81));
		assertEquals(523+160, rangeModel.viewToModel(523));
	}

	@Test
	public void test() {
		//fail("Not yet implemented");
	}
}
