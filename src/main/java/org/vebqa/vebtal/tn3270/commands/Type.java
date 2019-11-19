package org.vebqa.vebtal.tn3270.commands;

import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

import net.sf.f3270.FieldIdentifier;
import net.sf.f3270.Terminal;

@Keyword(module = TN3270TestAdaptionPlugin.ID, command = "type", description = "type to screen", hintTarget = "label=", hintValue = "<text>")
public class Type extends AbstractCommand {

	public Type(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal) aDriver;
		Response tResp = new Response();

		String label = null;
		FieldIdentifier field = null;

		// konvention: label=x
		String[] parts = target.split("[=]{1}");
		switch (parts[0]) {
		case "label":
			label = parts[1];
			label = label.replace('^', '=');
			field = new FieldIdentifier(label);
			break;
		default:
			break;
		}

		if (field != null) {
			try {
				driver.write(field, value);
				tResp.setCode(Response.PASSED);
			} catch (RuntimeException e) {
				tResp.setCode(Response.FAILED);
				tResp.setMessage(e.getMessage());
			}
		} else {
			// just send the text
			try {
				driver.write(value);
				tResp.setCode(Response.PASSED);
			} catch (RuntimeException e) {
				tResp.setCode(Response.FAILED);
				tResp.setMessage(e.getMessage());
			}
		} 

		return tResp;
	}
}
