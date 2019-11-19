package org.vebqa.vebtal.tn3270.commands;

import org.h3270.host.Field;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

import net.sf.f3270.FieldIdentifier;
import net.sf.f3270.Terminal;

@Keyword(module = TN3270TestAdaptionPlugin.ID, command = "storeText", description = "get text from screen", hintTarget = "label=;row=;column=;length=", hintValue = "<buffername>")
public class Storetext extends AbstractCommand {

	public Storetext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal) aDriver;
		Response tResp = new Response();

		String label = null;
		FieldIdentifier field = null;
		boolean identByLabel = false;
		int rowNumber = -1;
		int columnNumber = -1;
		int length = -1;

		// example:
		// target: "row=10;column=21"
		// alt. target: "label="myLabel"
		// dont mix label and row/column; label is leading resolver
		String[] parts = target.split(";");

		for (String aToken : parts) {
			String[] part = aToken.split("=");
			switch (part[0]) {
			case "label":
				label = part[1];
				identByLabel = true;
				field = new FieldIdentifier(label);
				break;
			case "row":
				rowNumber = Integer.valueOf(part[1]);
				break;
			case "column":
				columnNumber = Integer.valueOf(part[1]);
				break;
			case "length":
				length = Integer.valueOf(part[1]);
				break;

			default:
				break;
			}
		}

		if (field == null && rowNumber == -1 && columnNumber == -1) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("label or row/columns identifier missing. Cannot resolve field!");
			return tResp;
		}

		if (identByLabel && field == null) {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("tried to identify field by label, but it failed: label=" + label);
			return tResp;
		}

		// if we resolve to a label we should get a field already.
		if (field != null) {
			Field resolvedField;
			try {
				resolvedField = driver.getField(field);
				tResp.setCode(Response.PASSED);
				tResp.setStoredValue(resolvedField.getValue());
				tResp.setStoredKey(value);
			} catch (RuntimeException e) {
				tResp.setCode(Response.FAILED);
				tResp.setMessage(e.getMessage());
			}
			return tResp;
		}

		if (!identByLabel) {
			String foundString = null;
			try {
				if (length > 0) {
					foundString = driver.getLine(rowNumber - 1).substring(columnNumber - 1, columnNumber + length - 1); // Row
																														// &
					// Column
					// values
					// decremented
					// with
					// 1 for
					// the
					// convenience
					// of
					// Tosca
					// Users
				} else {
					length = driver.getLine(rowNumber - 1).length();
					foundString = driver.getLine(rowNumber - 1).substring(columnNumber - 1, length - columnNumber - 1); // Row
																														// &
					// Column
					// values
					// decremented
					// with
					// 1 for
					// the
					// convenience
					// of
					// Tosca
					// Users
				}
			} catch (NullPointerException e) {
				tResp.setCode(Response.FAILED);
				tResp.setMessage(e.getMessage());
				return tResp;
			}
			if (foundString != null) {
				tResp.setCode(Response.PASSED);
				tResp.setMessage("Result: " + foundString);
				tResp.setStoredKey(value);
				tResp.setStoredValue(foundString);
			} else {
				tResp.setCode(Response.FAILED);
				tResp.setMessage("Text does not match! Found String: '" + foundString + "'");
			}
		}
		return tResp;
	}
}
