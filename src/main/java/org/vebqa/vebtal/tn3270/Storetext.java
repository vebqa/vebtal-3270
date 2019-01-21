package org.vebqa.vebtal.tn3270;

import org.h3270.host.Field;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import net.sf.f3270.FieldIdentifier;
import net.sf.f3270.Terminal;

public class Storetext extends AbstractCommand {

	public Storetext(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal)aDriver;
		Response tResp = new Response();

		String label = null;
		FieldIdentifier field = null;

		// konvention: label=x
		String[] parts = target.split("=");
		switch (parts[0]) {
		case "label":
			label = parts[1];
			field = new FieldIdentifier(label);
			break;
		default:
			break;
		}

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
		} else {
			// Field not found!
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Field not found!");
		}

		return tResp;
	}

}
