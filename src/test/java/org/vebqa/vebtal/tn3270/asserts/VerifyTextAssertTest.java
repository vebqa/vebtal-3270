package org.vebqa.vebtal.tn3270.asserts;

import static org.hamcrest.core.StringStartsWith.startsWith;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.sf.f3270.TerminalModel;
import net.sf.f3270.TerminalType;
import net.sf.f3270.junit.rules.TerminalResource;

public class VerifyTextAssertTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public final TerminalResource terminal = new TerminalResource().withHost("127.0.0.1").withPort(23)
			.withMode(TerminalModel.MODE_80_24).withType(TerminalType.TYPE_3279).showTerminalWindow(true).setDebug(true);

	@Test
	public void findTextAtPage() {
		VerifyTextAssert.assertThat(terminal).hasText("Multi-User System for Interactive Computing / System Product").atPosition(10, 16).check();
	}

	@Test
	public void failFindingTextAtPage() {
		thrown.expect(AssertionError.class);
		thrown.expectMessage(startsWith("Expected text <Multi-User System for Interactive Computing / System Product> not found in the page."));

		VerifyTextAssert.assertThat(terminal).hasText("Multi-User System for Interactive Computing / System Product").atPosition(1, 1).check();
	}

}