package rcas.controller;

import com.jfoenix.controls.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;
import org.controlsfx.control.Notifications;
import rcas.RCASMain;
import rcas.model.MagicFormulaTireModel;
import rcas.model.RaceCar;

import java.util.ArrayList;


@SuppressWarnings("Duplicates")
public class RCASMainController {

	@FXML
	private JFXButton delete, tm, save, showMMM, saveAxle;

	@FXML
	private JFXSlider cog_Slider, cwFL_Slider, cwFR_Slider, cwRL_Slider, cwRR_Slider, frd_Slider;

	@FXML
	private JFXListView<Label> listView;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXTextField cog, cwFL, cwFR, cwRL, cwRR, frd, wb, fTrack, rTrack, name;

	@FXML
	private JFXDialog jfxDialog;


	private ArrayList<RangeVal> advAxleModelValList = new ArrayList<>();
	private ArrayList<RangeVal> settingsValList     = new ArrayList<>();
	private ArrayList<JFXTextField> valTextList = new ArrayList<>();


	@FXML
	private void initialize() {

		createBindings();
		initValidators();
		initListView();
		initDefaultRaceCars();

	}

	private void initListView() {

		Label label = new Label("New RaceCar Model");
		label.setStyle("-fx-text-fill: forestgreen; -fx-font-weight: bold");

		listView.getItems().add(label);

	}

	private void initValidators() {

		addVal(cog,   10.0,   200.0);
		addVal(wb,     0.5,     6.0);
		addVal(cwFL,  50.0, 1_000.0);
		addVal(cwFR,  50.0, 1_000.0);
		addVal(cwRL,  50.0, 1_000.0);
		addVal(cwRR,  50.0, 1_000.0);
		addVal(frd,   20.0,    80.0);
		addVal(fTrack, 1.2,     2.0);
		addVal(rTrack, 1.2,     2.0);

	}

	private void addVal(JFXTextField tf, double min, double max) {

		RangeVal val = new RangeVal(min, max);
		tf.getValidators().add(val);
		settingsValList.add(val);
		valTextList.add(tf);

		tf.focusedProperty().addListener((o, oldVal, newVal) ->{
			if(!newVal) {

				tf.validate();
				valSaveButtonDisable();

			}
		});

	}

	private void valSaveButtonDisable() {

		save.setDisable(false);

		for(RangeVal val : settingsValList) {
			if(val.getHasErrors()) save.setDisable(true);

		}

	}

	private void createBindings() {

		cwFL.textProperty().bindBidirectional(cwFL_Slider.valueProperty(), new NumberStringConverter());
		cwFR.textProperty().bindBidirectional(cwFR_Slider.valueProperty(), new NumberStringConverter());
		cwRL.textProperty().bindBidirectional(cwRL_Slider.valueProperty(), new NumberStringConverter());
		cwRR.textProperty().bindBidirectional(cwRR_Slider.valueProperty(), new NumberStringConverter());
		cog .textProperty().bindBidirectional( cog_Slider.valueProperty(), new NumberStringConverter("#.0"));
		frd .textProperty().bindBidirectional( frd_Slider.valueProperty(), new NumberStringConverter("#.0"));

		clearAllFields();

	}

	private void clearAllFields() {

		cog    .clear();
		cwFL   .clear();
		cwFR   .clear();
		cwRL   .clear();
		cwRR   .clear();
		frd    .clear();
		wb     .clear();
		fTrack .clear();
		rTrack .clear();
		name   .clear();

	}

	private void initDefaultRaceCars() {

		addRaceCarToList(new RaceCar("Car STD", 420, 420, 370, 370));
		addRaceCarToList(new RaceCar("Car MOD", 350, 350, 270, 270));

	}

	private void addRaceCarToList(RaceCar raceCar) {

		Label label = new Label(raceCar.getName());
		label.setUserData(raceCar);

		ObservableList<Label> labelList = listView.getItems();
		labelList.add(labelList.size() - 1, label);

	}



	@FXML
	private void displaySelectedModel() {

		if (!listView.getSelectionModel().isEmpty()) {

			Label selLabel = listView.getSelectionModel().getSelectedItem();

			if (selLabel.getUserData() != null) {

				displayRaceCar((RaceCar) listView.getSelectionModel().getSelectedItem().getUserData());
				tm     .setDisable(false);
				showMMM.setDisable(false);

				for (JFXTextField tf : valTextList) {
					tf.validate();
				}


			} else {

				// ADD NEW RACE CAR
				clearAllFields();

			}
		}

	}

	private void displayRaceCar(RaceCar raceCar) {

		name.setText(raceCar.getName());

		cwFL_Slider.setValue(raceCar.getCornerWeightFL());
		cwFR_Slider.setValue(raceCar.getCornerWeightFR());
		cwRL_Slider.setValue(raceCar.getCornerWeightRL());
		cwRR_Slider.setValue(raceCar.getCornerWeightRR());

		frd_Slider .setValue(raceCar.getFrontRollDist () * 100);
		cog_Slider .setValue(raceCar.getCogHeight     () * 100);

		fTrack.setText(String.valueOf(raceCar.getFrontTrack()));
		rTrack.setText(String.valueOf(raceCar.getRearTrack ()));
		wb    .setText(String.valueOf(raceCar.getWheelbase ()));

	}

	@FXML
	private void advancedAxleModelPopUp() {

		advAxleModelValList.clear();

		JFXDialogLayout content = new JFXDialogLayout();
		content.setHeading(new Text("Tire Model Configuration"));
		content.setPrefWidth(600);

		GridPane pane = new GridPane();
		pane.setVgap(35);
		pane.setHgap(50);
		pane.setAlignment(Pos.CENTER);

		Label titleFront = new Label("Front Axle Tire Model");
		titleFront.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
		pane.add(titleFront, 0,0);

		JFXTextField cF  = createAdvInfoTF("Slip Angle Coefficient C", 0.1,15.0);
		pane.add(cF,  0,1);
		JFXTextField bF  = createAdvInfoTF("Slip Angle Coefficient B", 1.0, 45.0);
		pane.add(bF,  0,2);
		JFXTextField eF  = createAdvInfoTF("Slip Angle Coefficient E", -15.0, 20.0);
		pane.add(eF,  0,3);
		JFXTextField kaF = createAdvInfoTF("Load Coefficient KA", 1.0, 5.0);
		pane.add(kaF, 0,4);
		JFXTextField kbF = createAdvInfoTF("Load Coefficient KA", 0.1, 1.5);
		pane.add(kbF, 0,5);

		cF.getValidators().get(0).getHasErrors();

		Label titleRear = new Label("Rear Axle Tire Model");
		titleRear.setStyle("-fx-font-size: 15; -fx-font-weight: bold");
		pane.add(titleRear, 2,0);

		JFXTextField cR  = createAdvInfoTF("Slip Angle Coefficient C", 0.1,15.0);
		pane.add(cR,  2,1);
		JFXTextField bR  = createAdvInfoTF("Slip Angle Coefficient B", 1.0, 45.0);
		pane.add(bR,  2,2);
		JFXTextField eR  = createAdvInfoTF("Slip Angle Coefficient E", -15.0, 20.0);
		pane.add(eR,  2,3);
		JFXTextField kaR = createAdvInfoTF("Load Coefficient KA", 1.0, 5.0);
		pane.add(kaR, 2,4);
		JFXTextField kbR = createAdvInfoTF("Load Coefficient KA", 0.1, 1.5);
		pane.add(kbR, 2,5);

		Separator separator = new Separator();
		separator.setOrientation(Orientation.VERTICAL);

		pane.add(separator, 1,0,1,6);

		/**
		 *
		 */
		RaceCar raceCar = (RaceCar) listView.getSelectionModel().getSelectedItem().getUserData();

		MagicFormulaTireModel tmF = (MagicFormulaTireModel) raceCar.getFrontAxleTireModel();
		MagicFormulaTireModel tmR = (MagicFormulaTireModel) raceCar.getRearAxleTireModel ();

		cF .setText(String.valueOf(tmF.getSlipAngleCoefficientC()));
		bF .setText(String.valueOf(tmF.getSlipAngleCoefficientB()));
		eF .setText(String.valueOf(tmF.getSlipAngleCoefficientE()));
		kaF.setText(String.valueOf(tmF.getLoadCoefficientKA()));
		kbF.setText(String.valueOf(tmF.getLoadCoefficientKB()));

		cR .setText(String.valueOf(tmR.getSlipAngleCoefficientC()));
		bR .setText(String.valueOf(tmR.getSlipAngleCoefficientB()));
		eR .setText(String.valueOf(tmR.getSlipAngleCoefficientE()));
		kaR.setText(String.valueOf(tmR.getLoadCoefficientKA()));
		kbR.setText(String.valueOf(tmR.getLoadCoefficientKB()));

		/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

		jfxDialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);


		saveAxle = new JFXButton("Save Axle Tire Models");
		saveAxle.setPrefWidth(155);
		saveAxle.setPrefHeight(26);
		saveAxle.setButtonType(JFXButton.ButtonType.RAISED);
		saveAxle.setStyle("-fx-background-color:  lightgreen");

		// TODO: Save Tire Models to RaceCar
		saveAxle.setOnAction(e -> {

			System.out.println("PRESSED");

			raceCar.setFrontAxleTireModel(
					new MagicFormulaTireModel(
							Double.valueOf(cF .getText()),
							Double.valueOf(bF .getText()),
							Double.valueOf(eF .getText()),
							Double.valueOf(kaF.getText()),
							Double.valueOf(kbF.getText()) / 10_000));

			raceCar.setRearAxleTireModel(
					new MagicFormulaTireModel(
							Double.valueOf(cR .getText()),
							Double.valueOf(bR .getText()),
							Double.valueOf(eR .getText()),
							Double.valueOf(kaR.getText()),
							Double.valueOf(kbR.getText()) / 10_000));


			jfxDialog.close();

		});


		pane.add(saveAxle, 2,6);

		content.setBody(pane);

		jfxDialog.show();

	}

	private JFXTextField createAdvInfoTF(String name, double min, double max) {

		JFXTextField tf = new JFXTextField();
		tf.setPromptText(name);
		tf.setLabelFloat(true);
		tf.setPrefWidth(155);

		RangeVal val = new RangeVal(min, max);
		tf.getValidators().add(val);
		advAxleModelValList.add(val);

		tf.focusedProperty().addListener((o, oldVal, newVal) ->{
			if(!newVal) {

				tf.validate();
				advAxleModelValButtonDisable();

			}
		});

		return tf;

	}

	private void advAxleModelValButtonDisable() {

		saveAxle.setDisable(false);

		for(RangeVal val : advAxleModelValList) {
			if(val.getHasErrors()) saveAxle.setDisable(true);

		}

	}

	@FXML
	private void MMMDiagram() throws Exception {


		RCASMMMController mmmController = new RCASMMMController((RaceCar) listView.getSelectionModel().getSelectedItem().getUserData());

		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(RCASMain.class.getResource("view/RCASMMMView.fxml"));
		fxmlLoader.setController(mmmController);

		Stage mmmStage = new Stage();

		BorderPane mmmPane = (BorderPane) fxmlLoader.load();

		mmmStage.setScene(new Scene(mmmPane));
		mmmStage.setTitle("MMM Diagram Mz / Fy (Milliken Moment Method)");
		mmmStage.setResizable(false);
		mmmStage.centerOnScreen();

		mmmStage.show();

	}

	@FXML
	private void deleteRaceCar() {
		if (listView.getSelectionModel().getSelectedItem().getText().equals("New RaceCar Model")) {
			Notifications.create()
					.position(Pos.TOP_RIGHT)
					.title("Invalid action")
					.text("Please choose a racecar")
					.showError();
		} else {
			clearAllFields();
			listView.getItems().remove(listView.getSelectionModel().getSelectedIndex());
		}
	}

	@FXML
	private void saveRaceCar() {
		RaceCar raceCar = new RaceCar(name.getText(),Double.parseDouble(fTrack.getText()), Double.parseDouble(rTrack.getText()), Double.parseDouble(wb.getText()), Double.parseDouble(cog.getText()) / 100, Double.parseDouble(frd.getText()) / 100, Double.parseDouble(cwFL.getText()), Double.parseDouble(cwFR.getText()), Double.parseDouble(cwRL.getText()), Double.parseDouble(cwRR.getText()));
		Label label = new Label(raceCar.getName());
		label.setUserData(raceCar);
		listView.getItems().add(listView.getItems().size() - 1, label);
	}
}
