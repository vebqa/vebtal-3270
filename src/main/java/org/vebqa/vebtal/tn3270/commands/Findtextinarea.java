package org.vebqa.vebtal.tn3270.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.Area;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

import net.sf.f3270.Terminal;

@Keyword(module = TN3270TestAdaptionPlugin.ID, command = "findTextInArea", description = "find text in a given area and return row number", hintTarget = "x=;y=;height=;width=;needle=", hintValue = "<variable>")
public class Findtextinarea extends AbstractCommand {

	public Findtextinarea(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
	}

	/**
	 * | command | target | value | | verifyTextByArea | x:1;y:1;height:1;width:1 |
	 * text |
	 */
	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal) aDriver;

		Response tResp = new Response();

		if (target == null || target.contentEquals("")) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Command needs an area specified in taret.");
			return tResp;
		}
		if (value == null || value.contentEquals("")) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Command nees a variable specified in value.");
			return tResp;
		}

		// target to area
		Area area;
		try {
			area = new Area(target);
		} catch (Exception e) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Could not create area definition!");
			return tResp;
		}

		boolean found = false;
		
		// intial or default if not found
		int row = -1;

		// we are zero based here
		int startRow = area.getY() - 1;
		int endRow = startRow + area.getHeight();
		int columnStart = area.getX() - 1;
		int columnEnd = columnStart + area.getWidth();

		for (int rowNumber = startRow; rowNumber <= endRow; rowNumber++) {
			String rowArea = driver.getLine(rowNumber).substring(columnStart, columnEnd).toString();
			row = rowArea.indexOf(area.getNeedle());
			// found!
			if (row >= 0) {
				tResp.setCode(Response.PASSED);
				tResp.setMessage("Needle: " + area.getNeedle() + " found in row: " + row);
				tResp.setStoredKey(value);
				tResp.setStoredValue(String.valueOf(row));
				break;
			}
		}
		
		if (row < 0) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Needle not found.");
			tResp.setStoredKey(value);
			tResp.setStoredValue(String.valueOf(row));
		}
		
		return tResp;
	}

}
