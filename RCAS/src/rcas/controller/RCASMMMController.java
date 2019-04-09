package rcas.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
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

			String name = raceCar.getName();

			addChartData(util.getMMMBalanceValue  (raceCar), name, balanceChart);
			addChartData(util.getMMMGripValue     (raceCar), name, gripChart);
			addChartData(util.getMMMControlValue  (raceCar, 0.0, 0.0, 10.0), name, controlChart);
			addChartData(util.getMMMStabilityValue(raceCar, 0.0, 0.0,  1.0), name, stabilityChart);

			raceCars.add(raceCar);

		}

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

	private void addChartData(double value, String name, BarChart<String, Number> chart) {

		XYChart.Series<String, Number> series = new XYChart.Series<>();
		series.setName(name);
		series.getData().add(new XYChart.Data<>("", value));
		chart.getData().add(series);

	}

}
