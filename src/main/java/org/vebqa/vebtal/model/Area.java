package org.vebqa.vebtal.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Area {

	private static final Logger logger = LoggerFactory.getLogger(Area.class);

	private int x = 0;
	private int y = 0;
	private int height = 0;
	private int width = 0;
	private String needle = "";

	public Area(String aSpecLine) throws Exception {
		parseSpecLine(aSpecLine);
	}

	public Area(int ax, int ay, int aheight, int awidth, String aNeedle) {
		x = ax;
		y = ay;
		height = aheight;
		width = awidth;
		needle= aNeedle;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	public String getNeedle() {
		return needle;
	}
	
	private void parseSpecLine(String aSpecLine) throws Exception {
		boolean invalidArea = false;
		String[] someFragments = aSpecLine.split(";");
		for (String aFragment : someFragments) {
			aFragment = aFragment.trim().toLowerCase();
			String[] someToken = aFragment.split("=");
			if (someToken[0] == null || someToken[1] == null) {
				throw new Exception("Invalid Arguments!");
			}
			switch (someToken[0]) {
			case "x":
				x = Integer.parseInt(someToken[1]);
				break;
			case "y":
				y = Integer.parseInt(someToken[1]);
				break;
			case "height":
				height = Integer.parseInt(someToken[1]);
				break;
			case "width":
				width = Integer.parseInt(someToken[1]);
				break;
			case "needle":
				needle = someToken[1];
				break;
			default:
				invalidArea = true;
				break;
			}
		}
		if (invalidArea) {
			throw new Exception("Invalid area data!");
		}
		logger.info("Area created: {},{} - {},{}", x, y, height, width);
	}

}