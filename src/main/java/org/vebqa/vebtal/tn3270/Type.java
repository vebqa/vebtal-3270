package org.vebqa.vebtal.tn3270;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import net.sf.f3270.FieldIdentifier;
import net.sf.f3270.Terminal;

public class Type extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Type.class);

	public Type(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
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
			try {
				driver.write(field, value);
				tResp.setCode("0");
			} catch (RuntimeException e) {
				tResp.setCode("1");
				tResp.setMessage(e.getMessage());
			}
		} else if (label == null) {
			if (label == null) {
				try {
					driver.write(value);
					tResp.setCode("0");
				} catch (RuntimeException e) {
					tResp.setCode("1");
					tResp.setMessage(e.getMessage());
				}
			}
		} else {
			// Field not found!
			tResp.setCode("1");
			tResp.setMessage("Field not found!");
		}

		return tResp;
	}
}
