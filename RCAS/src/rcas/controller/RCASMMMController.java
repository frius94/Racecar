package rcas.controller;

import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import rcas.model.RaceCar;
import rcas.util.CorneringAnalyserUtil;
import java.util.ArrayList;
import java.util.Iterator;


public class RCASMMMController {


	@FXML
	private LineChart<Number, Number> mainChart;
	@FXML
	private BarChart<String, Number> balanceChart, gripChart, controlChart, stabilityChart;
	@FXML
	private JFXTextField ctrl_B, ctrl_D, ctrl_toD, stab_D, stab_B, stab_toB;

	private CorneringAnalyserUtil util;
	private ArrayList<RaceCar> raceCars;

	public RCASMMMController() {

		this.util     = new CorneringAnalyserUtil();
		this.raceCars = new ArrayList<>();

	}

	@FXML
	private void initialize() {

		initDoubleValTextFields();

	}
	
	private void initDoubleValTextFields() {

		initDoubleVal(ctrl_B  ).setText( "0");
		initDoubleVal(ctrl_D  ).setText( "0");
		initDoubleVal(ctrl_toD).setText("10");
		initDoubleVal(stab_D  ).setText( "0");
		initDoubleVal(stab_B  ).setText( "0");
		initDoubleVal(stab_toB).setText( "1");

	}
	
	@SuppressWarnings("Duplicates")
	private JFXTextField initDoubleVal(JFXTextField tf) {

		ChangeListener<String> doubleValidator = new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("[-]?\\d{0,7}[\\.]?\\d{0,4}")) {
					tf.setText(oldValue);
				}
			}

		};

		tf.textProperty().addListener(doubleValidator);

		return tf;

	}

	private double [] getTFData() {

		double [] data = new double[6];

		data [0] = Double.valueOf(ctrl_B  .getText());
		data [1] = Double.valueOf(ctrl_D  .getText());
		data [2] = Double.valueOf(ctrl_toD.getText());
		data [3] = Double.valueOf(stab_D  .getText());
		data [4] = Double.valueOf(stab_B  .getText());
		data [5] = Double.valueOf(stab_toB.getText());

		return data;
	}
	
	public void addRaceCar(RaceCar raceCar) {

		if(!raceCars.contains(raceCar)) {

			MMMChart(raceCar);

			String color     = raceCar.getColor();
			double [] tfData = getTFData();

			addChartData(util.getMMMBalanceValue  (raceCar), color, balanceChart);
			addChartData(util.getMMMGripValue     (raceCar), color, gripChart   );
			addChartData(util.getMMMControlValue  (raceCar, tfData[0], tfData[1], tfData[2]), color, controlChart  );
			addChartData(util.getMMMStabilityValue(raceCar, tfData[3], tfData[4], tfData[5]), color, stabilityChart);

			raceCars.add(raceCar);

			changeChartSize();

		}

	}

	@FXML
	private void upDateChart(){

		controlChart  .getData().clear();
		stabilityChart.getData().clear();

		double [] tfData = getTFData();

		for(RaceCar raceCar: raceCars) {

			addChartData(util.getMMMControlValue  (raceCar, tfData[0], tfData[1], tfData[2]), raceCar.getColor(), controlChart  );
			addChartData(util.getMMMStabilityValue(raceCar, tfData[3], tfData[4], tfData[5]), raceCar.getColor(), stabilityChart);

		}

	}
	
	private void changeChartSize() {

		switch(raceCars.size()) {

			case  2: setChartSize(120); break;
			case  3: setChartSize(100); break;
			case  4: setChartSize( 80); break;
			case  5: setChartSize( 60); break;
			case  6: setChartSize( 40); break;

		}

	}

	private void setChartSize(double len) {
		balanceChart  .setCategoryGap(len);
		gripChart     .setCategoryGap(len);
		controlChart  .setCategoryGap(len);
		stabilityChart.setCategoryGap(len);
	}

	private void MMMChart(RaceCar raceCar) {

		ObservableList<XYChart.Series<Number, Number>> dataList = util.generateMMMChartData(raceCar);
		mainChart.getData().addAll(dataList);

		for (Iterator<XYChart.Series<Number, Number>> iterator = dataList.iterator(); iterator.hasNext();) {
			XYChart.Series<Number, Number> curve = (XYChart.Series<Number, Number>) iterator.next();
			curve.getNode().lookup(".chart-series-line").setStyle("-fx-stroke: " + raceCar.getColor() + "; -fx-stroke-width: 1px;");
		}

	}

	private void addChartData(double value, String color, BarChart<String, Number> chart) {

		value  = (Math.round(value * 100)) / 100;
	
		XYChart.Series<String, Number> series = new XYChart.Series<>();
		XYChart.Data  <String, Number> data   = new XYChart.Data<>("", value);/*createData(value, color);*/

		data.nodeProperty().addListener(new ChangeListener<Node>() {
			@Override
			public void changed(ObservableValue<? extends Node> observable, Node oldValue, final Node node) {
				data.getNode().setStyle("-fx-bar-fill: " + color + ";");
			}
		});

		series.getData().add(data  );
		chart .getData().add(series);

	}

	private XYChart.Data<String, Number> createData(double value, String color) {
		XYChart.Data<String, Number> data =  new XYChart.Data<>("", value);

		StackPane node = new StackPane();
		Label label = new Label(String.valueOf(value));
		label.setRotate(-90);
		Group group = new Group(label);
		StackPane.setAlignment(group, Pos.TOP_CENTER);
		StackPane.setMargin(group, new Insets(0, 0, 5, 0));
		node.getChildren().add(group);
		data.setNode(node);
		
		return data;

	}

}
