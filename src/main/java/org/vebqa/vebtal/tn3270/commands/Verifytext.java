package org.vebqa.vebtal.tn3270.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

import net.sf.f3270.Terminal;

@Keyword(module = TN3270TestAdaptionPlugin.ID, command = "verifyText", description = "verify text", hintTarget = "row=;col=;label=;", hintValue = "<text>")
public class Verifytext extends AbstractCommand {

	public Verifytext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ASSERTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal) aDriver;

		Response tResp = new Response();

		int rowNumber = 0;
		int columnNumber = 0;
		String label = "";

		String searchString = value.toString();
		int searchStringLength = value.length();

		// example: target: "row=10;column=21"
		// example: target: "label=<text>"
		String[] parts = target.split(";");

		boolean confMismatch = false;
		
		for (String aToken : parts) {
			String[] part = aToken.split("=");
			switch (part[0]) {
			case "row":
				rowNumber = Integer.valueOf(part[1]);
				break;
			case "column":
				columnNumber = Integer.valueOf(part[1]);
				break;
			case "label":
				label = part[1];
				break;
			default:
				confMismatch = true;
				break;
			}
		}

		if (confMismatch) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("There is a configuration error in the attribute list.");
			return tResp;
		}
		
		// dont mix different strategies for identification
		if ((rowNumber > 0 || columnNumber > 0) && !label.isEmpty()) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Do not mix identification by row/column and label!");
			return tResp;
		}

		if (label.isEmpty()) {
			// identification by row/column needs both attributes
			if (rowNumber == 0 || columnNumber == 0) {
				tResp.setCode(Response.FAILED);
				tResp.setMessage("Identification by position needs both row and column!");
			} else {
				String foundString = driver.getLine(rowNumber - 1)
						.substring(columnNumber - 1, columnNumber + searchStringLength -1).toString(); 

				if (foundString.equals(searchString)) {
					tResp.setCode(Response.PASSED);
					tResp.setMessage("Matching text found!");
				} else {
					tResp.setCode(Response.FAILED);
					tResp.setMessage("Text does not match! Found String: '" + foundString + "'");
				}
			}
		} else {
			// identification by label
			// @todo
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Not yet implemented. Use identification by row/column instead.");
		}
		return tResp;
	}
}