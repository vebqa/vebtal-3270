package org.vebqa.vebtal.tn3270;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

import net.sf.f3270.Terminal;

@Keyword(module = TN3270TestAdaptionPlugin.ID, command = "verifyText", description = "verify text", hintTarget = "row=;col=", hintValue = "<text>")
public class Verifytext extends AbstractCommand {

	public Verifytext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal) aDriver;

		Response tResp = new Response();

		int rowNumber = 0;
		int columnNumber = 0;

		String searchString = value.toString();
		int searchStringLength = value.length();

		// Beispiel; target: "row=10;column=21"
		String[] parts = target.split(";");

		for (String aToken : parts) {
			String[] part = aToken.split("=");
			switch (part[0]) {
			case "row":
				rowNumber = Integer.valueOf(part[1]);
				break;
			case "column":
				columnNumber = Integer.valueOf(part[1]);
				break;
			}
		}

		String foundString = driver.getLine(rowNumber - 1)
				.substring(columnNumber - 1, columnNumber - 1 + searchStringLength).toString(); // Row & Column values
																								// decremented with 1
																								// for the convenience
																								// of Tosca Users

		if (foundString.equals(searchString)) {
			tResp.setCode(Response.PASSED);
			tResp.setMessage("Matching text found!");
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Text does not match! Found String: '" + foundString + "'");
		}

		return tResp;
	}

}