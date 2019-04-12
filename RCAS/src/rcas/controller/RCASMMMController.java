package rcas.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
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

	private CorneringAnalyserUtil util;
	private ArrayList<RaceCar> raceCars;

	public RCASMMMController() {

		this.util     = new CorneringAnalyserUtil();
		this.raceCars = new ArrayList<>();

	}

	@FXML
	private void initialize() {

	}

	public void addRaceCar(RaceCar raceCar) {

		if(!raceCars.contains(raceCar)) {

			MMMChart(raceCar);

			String color = raceCar.getColor();

			addChartData(util.getMMMBalanceValue  (raceCar), color, balanceChart, 1);
			addChartData(util.getMMMGripValue     (raceCar), color, gripChart, 100);
			addChartData(util.getMMMControlValue  (raceCar, 0.0, 0.0, 10.0), color, controlChart, 1);
			addChartData(util.getMMMStabilityValue(raceCar, 0.0, 0.0,  1.0), color, stabilityChart, 1);

			raceCars.add(raceCar);

			changeChartSize();

		}

	}

	private void changeChartSize() {

		switch(raceCars.size()) {

			case  2: setChartSize(120);
					 break;
			case  3: setChartSize(100);
					 break;
			case  4: setChartSize( 80);
					 break;
			case  5: setChartSize( 60);
					 break;
			case  6: setChartSize( 40);
					 break;
			default: break;

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

		setSeriesStyle(dataList, ".chart-series-line", "-fx-stroke: " + raceCar.getColor() + "; -fx-stroke-width: 1px;");

	}

	private void setSeriesStyle(ObservableList<XYChart.Series<Number, Number>> dataList, String styleSelector, String lineStyle) {
		for (Iterator<XYChart.Series<Number, Number>> iterator = dataList.iterator(); iterator.hasNext();) {
			XYChart.Series<Number, Number> curve = (XYChart.Series<Number, Number>) iterator.next();
			curve.getNode().lookup(styleSelector).setStyle(lineStyle);
		}
	}

	private void addChartData(double value, String color, BarChart<String, Number> chart, int factor) {

		XYChart.Series<String, Number> series = new XYChart.Series<>();

		value*=factor;
		value = Math.round(value);
		value/=factor;

		XYChart.Data<String, Number> data = createData(value);
		series.getData().add(data);

		chart.getData().add(series);

		chart.lookup(".default-color" + raceCars.size() + ".chart-bar").setStyle("-fx-bar-fill: " + color + ";");

	}

	private XYChart.Data<String, Number> createData(double value) {
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
