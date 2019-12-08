package org.vebqa.vebtal.tn3270restserver;

import java.util.List;
import java.util.TreeSet;

import org.apache.commons.configuration2.CombinedConfiguration;
import org.vebqa.vebtal.AbstractTestAdaptionPlugin;
import org.vebqa.vebtal.CommandAutoComplete;
import org.vebqa.vebtal.GuiManager;
import org.vebqa.vebtal.KeywordEntry;
import org.vebqa.vebtal.KeywordFinder;
import org.vebqa.vebtal.TestAdaptionType;
import org.vebqa.vebtal.model.Command;
import org.vebqa.vebtal.model.CommandResult;
import org.vebqa.vebtal.model.CommandType;
import org.vebqa.vebtal.sut.SutStatus;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

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
		Tab tnTab = createTab(ID, commandList, clData);

		// Add (Test generation)
		Text txtGeneration = new Text();
		txtGeneration.setText("test generation");
		txtGeneration.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 20));

		List<KeywordEntry> allModuleKeywords = KeywordFinder.getinstance()
				.getKeywordsByModule(TN3270TestAdaptionPlugin.ID);
		TreeSet<String> sortedKeywords = new TreeSet<>();
		for (KeywordEntry aKeyword : allModuleKeywords) {
			sortedKeywords.add(aKeyword.getCommand());
		}

		final CommandAutoComplete addCommand = new CommandAutoComplete(sortedKeywords);
		addCommand.setPromptText("Command");
		addCommand.setMaxWidth(200);
		final TextField addTarget = new TextField();
		addTarget.setMaxWidth(350);
		addTarget.setPromptText("Target");
		final TextField addValue = new TextField();
		addValue.setMaxWidth(350);
		addValue.setPromptText("Value");

		final Button addButton = new Button("Go");
		addButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				Command newCmd = new Command(addCommand.getText(), addTarget.getText(), addValue.getText());

				addCommand.clear();
				addTarget.clear();
				addValue.clear();

				Tn3270Resource aResource = new Tn3270Resource();
				GuiManager.getinstance().setTabStatus(TN3270TestAdaptionPlugin.ID, SutStatus.CONNECTED);
				aResource.execute(newCmd);
				GuiManager.getinstance().setTabStatus(TN3270TestAdaptionPlugin.ID, SutStatus.DISCONNECTED);
			}
		});

		HBox hbox = new HBox();

		hbox.getChildren().addAll(txtGeneration, addCommand, addTarget, addValue, addButton);
		hbox.setSpacing(5);
		hbox.setBorder(new Border(
				new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, new CornerRadii(2), new BorderWidths(3))));

		BorderPane pane = (BorderPane) tnTab.getContent();
		pane.setTop(hbox);

		return tnTab;

	}

	public static void addCommandToList(Command aCmd, CommandType aType) {
		String aValue = aCmd.getValue();
		if (aCmd.getCommand().toLowerCase().indexOf("password") > -1) {
			aValue = "*****";
		}
		CommandResult tCR = new CommandResult(aCmd.getCommand(), aCmd.getTarget(), aValue, aType);
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

	@Override
	public CombinedConfiguration loadConfig() {
		return loadConfig(ID);
	}
}
