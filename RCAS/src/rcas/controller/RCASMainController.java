package rcas.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class RCASMainController {

	@FXML
	private JFXButton delete, fatm, ratm, save, showMMM;

	@FXML
	private JFXSlider cog_Slider, cwFL_Slider, cwFR_Slider, cwRL_Slider, cwRR_Slider, frd_Slider;

	@FXML
	private JFXListView<Label> listView;

	@FXML
	private StackPane stackPane;

	@FXML
	private JFXTextField cog, cwFL, cwFR, cwRL, cwRR, frd, wb, fTrack, rTrack, name;

}
