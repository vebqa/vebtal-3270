package org.vebqa.vebtal.tn3270;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vebqa.vebtal.model.Response;

import net.sf.f3270.Terminal;

public class Keypress extends AbstractCommand {

	private static final Logger logger = LoggerFactory.getLogger(Keypress.class);
	
	public Keypress(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	@Override
	public Response executeImpl(Terminal driver) {
		Response tResp = new Response();
		
		String aButton = this.value.toUpperCase();
		if (aButton.startsWith("[") && aButton.endsWith("]")) {
			aButton = aButton.substring(1, aButton.length());
			aButton = aButton.substring(0, aButton.length() - 1);
		} else {
			tResp.setCode("1");
			tResp.setMessage("Need a Key as Value, e.g. [<Key>]");
			return tResp;
		}		
		
		// fast Generischer Ansatz
		switch(aButton) {
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
		tResp.setCode("0");
		return tResp;
	}

}
