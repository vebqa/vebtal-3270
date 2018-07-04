package org.vebqa.vebtal.tn3270restserver;

import org.vebqa.vebtal.AbstractTestAdaptionPlugin;
import org.vebqa.vebtal.TestAdaptionType;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.CommandResult;
import org.vebqa.vebtal.model.TN3270Session;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;

@SuppressWarnings("restriction")
public class TN3270TestAdaptionPlugin extends AbstractTestAdaptionPlugin {

	/**
	 * unique id of the test adapter
	 */
	public static final String ID = "tn3270";
	
	/**
	 * tableview with commands
	 */
	protected static final TableView<CommandResult> commandList = new TableView<>();
	
	/**
	 * results after execution
	 */
	protected static final ObservableList<CommandResult> clData = FXCollections.observableArrayList();		
	
	public TN3270TestAdaptionPlugin() {
		super(TestAdaptionType.ADAPTER);
	}

	public String getName() {
		return "TN3270 Plugin for RoboManager";
	}

	@Override
	public Tab startup() {
		return createTab(ID, commandList, clData);
	}

	public static void addCommandToList(TN3270Session aSess) {
		CommandResult tCR = new CommandResult("TN3270CreateSession", aSess.getHost() + ":" + aSess.getPort(),
				aSess.getType());
		Platform.runLater(() -> clData.add(tCR));
	}

	public static void addCommandToList(Command aCmd) {
		String aValue = aCmd.getValue();
		if (aCmd.getCommand().toLowerCase().indexOf("password") > 0) {
			aValue = "*****";
		}
		CommandResult tCR = new CommandResult(aCmd.getCommand(), aCmd.getTarget(), aValue);
		Platform.runLater(() -> clData.add(tCR));
	}

	public static void setLatestResult(Boolean success, final String aResult) {
		Platform.runLater(() -> clData.get(clData.size() - 1).setLogInfo(aResult));
		Platform.runLater(() -> clData.get(clData.size() - 1).setResult(success));

		commandList.refresh();
		Platform.runLater(() -> commandList.scrollTo(clData.size() - 1));
	}

	@Override
	public boolean shutdown() {
		return true;
	}
	
	/**
	 * this is the new service provider implementation.
	 */
	public Class<?> getImplementation() {
		return null;
	}
	
	@Override
	public String getAdaptionID() {
		return ID;
	}	
}
