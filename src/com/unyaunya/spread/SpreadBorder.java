package com.unyaunya.spread;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class SpreadBorder {
	public static final Border defaultBorder = BorderFactory.createMatteBorder(0,0,1,1, new Color(0xd0,0xd0,0xd0));
	public static final Border DEFAULT_FOCUS_BORDER = BorderFactory.createMatteBorder(1,1,1,1,Color.BLACK);
}
