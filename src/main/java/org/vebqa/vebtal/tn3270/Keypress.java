package org.vebqa.vebtal.tn3270;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.annotations.Keyword;
import org.vebqa.vebtal.command.AbstractCommand;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;
import org.vebqa.vebtal.tn3270restserver.TN3270TestAdaptionPlugin;

import net.sf.f3270.Terminal;

@Keyword(module = TN3270TestAdaptionPlugin.ID, command = "keyPress", description = "press a specific key", hintTarget = "<[key]>")
public class Keypress extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Keypress.class);

	public Keypress(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACTION;
	}

	@Override
	public Response executeImpl(Object aDriver) {
		Terminal driver = (Terminal) aDriver;
		Response tResp = new Response();

		String aButton = this.value.toUpperCase();
		if (aButton.startsWith("[") && aButton.endsWith("]")) {
			aButton = aButton.substring(1, aButton.length());
			aButton = aButton.substring(0, aButton.length() - 1);
		} else {
			tResp.setCode(Response.FAILED);
			tResp.setMessage("Need a Key as Value, e.g. [<Key>]");
			return tResp;
		}

		// fast Generischer Ansatz
		switch (aButton) {
		case "ENTER":
			logger.info("keyPress <enter>");
			driver.enter();
			break;

		case "CLEAR":
			logger.info("keypress <clear>");
			driver.clear();
			break;

		case "F3":
			logger.info("keyPress <f3>");
			driver.pf(3);
			break;

		default:
			logger.info("cannot process command: " + aButton);
			break;
		}
		logger.info(driver.getScreenText());
		tResp.setCode(Response.PASSED);
		return tResp;
	}

}
