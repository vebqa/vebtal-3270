package org.vebqa.vebtal.tn3270;

import org.vebqa.vebtal.model.Response;

import net.sf.f3270.Terminal;

public class VerifyText extends AbstractCommand {

	public VerifyText(String aCommand, String aTarget, String aValue) {
		super(aCommand, aTarget, aValue);
	}

	/**
	 * Implements several strategies to verify a text
	 * 1 | verifyText | <empty> | text
	 * 
	 */
	@Override
	protected Response executeImpl(Terminal driver) {
		// TODO Auto-generated method stub
		return null;
	}

}
