package org.vebqa.vebtal.tn3270.asserts;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import net.sf.f3270.TerminalModel;
import net.sf.f3270.TerminalType;
import net.sf.f3270.junit.rules.TerminalResource;

public class ActionAssertTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@Rule
	public final TerminalResource terminal = new TerminalResource().withHost("127.0.0.1").withPort(23)
			.withMode(TerminalModel.MODE_80_24).withType(TerminalType.TYPE_3279).showTerminalWindow(true).setDebug(true);

	@Test
	public void OpenMusicAndGoToLoginPage() {
		ActionAssert.assertThat(terminal).doKeypress("[ENTER]");
		terminal.getDriver().moveCursor(10, 10);
		terminal.getDriver().moveCursor(2, 17);
		ActionAssert.assertThat(terminal).forLabel("  MUSIC Userid:").doType("$0000");
		
		ActionAssert.assertThat(terminal).forLabel("Password").doType("music");
		
		ActionAssert.assertThat(terminal).doKeypress("[ENTER]");
		
		VerifyTextAssert.assertThat(terminal).atPosition(1, 4).hasText("Press ENTER to continue...").check();
		
		ActionAssert.assertThat(terminal).doKeypress("[ENTER]");
		
		ActionAssert.assertThat(terminal).doKeypress("[ENTER]");
		
		ActionAssert.assertThat(terminal).forLabel("^^^^>").doType("7");

		ActionAssert.assertThat(terminal).doKeypress("[ENTER]");

		// find "CHANNEL STATISTICS"
		// press F8 as long as name does not appear
		
		ActionAssert.assertThat(terminal).doKeypress("[F3]");
	}
}