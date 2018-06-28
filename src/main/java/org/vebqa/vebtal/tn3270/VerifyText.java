package org.vebqa.vebtal.tn3270;

import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.model.Response;

import net.sf.f3270.Terminal;

public class VerifyText extends AbstractCommand {

	public VerifyText(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
		this.type = CommandType.ACCESSOR;
	}

	/**
	 * Implements several strategies to verify a text
	 * 1 | verifyText | empty | text
	 * 
	 * @param	driver	terminal driver
	 * 
	 */
	@Override
	protected Response executeImpl(Terminal driver) {
		return null;
	}

}
