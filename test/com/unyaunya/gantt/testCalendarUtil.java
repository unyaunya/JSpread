/**
 * 
 */
package com.unyaunya.gantt;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author wata
 *
 */
public class testCalendarUtil {

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
	 * Test method for {@link com.unyaunya.gantt.CalendarUtil#round(java.util.Calendar)}.
	 */
	@Test
	public void testRound() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.unyaunya.gantt.CalendarUtil#today()}.
	 */
	@Test
	public void testToday() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.unyaunya.gantt.CalendarUtil#getDiffInDate(java.util.Calendar, java.util.Calendar)}.
	 */
	@Test
	public void testGetDiffInDate() {
		//fail("Not yet implemented");
	}

	/**
	 * Test method for {@link com.unyaunya.gantt.CalendarUtil#toDate(java.lang.Object)}.
	 */
	@SuppressWarnings("deprecation")
	@Test
	public void testToDate() {
		assertEquals(new Date(112, 9, 1), CalendarUtil.toDate("2012/10/01"));
		assertEquals(new Date(112, 9, 1), CalendarUtil.toDate("2012-10-01"));
		assertEquals(new Date(112, 9, 1), CalendarUtil.toDate("20121001"));
		//assertEquals(new Date(112, 9, 1), CalendarUtil.toDate("10/01"));
		//assertEquals(new Date(112, 9, 1), CalendarUtil.toDate("10-01"));
		//assertEquals(new Date(112, 9, 1), CalendarUtil.toDate("1001"));
	}

}
