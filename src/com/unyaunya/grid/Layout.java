/**
 * 
 */
package com.unyaunya.grid;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JScrollBar;

import com.unyaunya.swing.JSpread;


/**
 * @author wata
 *
 */
@SuppressWarnings("serial")
public class Layout extends BorderLayout {
	JSpread spread = null;
	JScrollBar horizontalBar = null;
	JScrollBar verticalBar = null;

	@Override
	 public void layoutContainer(Container parent) {
		int hbar_height = 16;
		int vbar_width = 16;
		int sp_height = parent.getHeight(); 
		int sp_width = parent.getWidth(); 
		if(horizontalBar != null) {
			sp_height -= hbar_height; 
		}
		if(verticalBar != null) {
			sp_width -= vbar_width; 
		}
		if(this.spread == null) {
			return;
		}
		this.spread.setBounds(0, 0, sp_width, sp_height);
		if(horizontalBar != null) {
			horizontalBar.setBounds(0, sp_height, sp_width, hbar_height);
		}
		if(verticalBar != null) {
			verticalBar.setBounds(sp_width, 0, vbar_width, sp_height);
		}
	 }

	 @Override
	 public void addLayoutComponent(Component comp,
             Object constraints) {
		 if(constraints.equals(BorderLayout.CENTER)) {
			 if(!(comp instanceof JSpread)) {
				 throw new RuntimeException("CENTERはJSpread専用");
			 }
			 this.spread = (JSpread)comp;
		 }
		 else if(constraints.equals(BorderLayout.SOUTH)) {
			 if(!(comp instanceof JScrollBar)) {
				 throw new RuntimeException("SOUTHはJScrollBar専用");
			 }
			 this.horizontalBar = (JScrollBar)comp;
		 }
		 else if(constraints.equals(BorderLayout.EAST)) {
			 if(!(comp instanceof JScrollBar)) {
				 throw new RuntimeException("EASTはJScrollBar専用");
			 }
			 this.verticalBar = (JScrollBar)comp;
		 }
		 else {
			 throw new RuntimeException("EAST,SOUTH,CENTER以外は使用禁止");
		 }
		 super.addLayoutComponent(comp, constraints);
	 }
}
