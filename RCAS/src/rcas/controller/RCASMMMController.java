package rcas.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import rcas.model.MagicFormulaTireModel;
import rcas.model.RaceCar;
import rcas.util.CorneringAnalyserUtil;

import java.util.Iterator;

public class RCASMMMController {


	@FXML
	private LineChart<Number, Number> mainChart;
	@FXML
	private BarChart<Number, Number> infoChart;



	private RaceCar raceCar;
	private CorneringAnalyserUtil util;

	public RCASMMMController(RaceCar raceCar) {

		this.raceCar = raceCar;
		this.util = new CorneringAnalyserUtil();

	}

	public RCASMMMController(RaceCar ...raceCar) {

		RaceCar [] cars = raceCar;
		this.util = new CorneringAnalyserUtil();

	}


	@FXML
	private void initialize() {

		initChart();

	}

	private void initChart() {

		ObservableList<XYChart.Series<Number, Number>> dataList = util.generateMMMChartData(raceCar);
		mainChart.getData().addAll(dataList);

		setSeriesStyle(dataList, ".chart-series-line", "-fx-stroke: blue; -fx-stroke-width: 1px;");


	}

	private void setSeriesStyle(ObservableList<XYChart.Series<Number, Number>> dataList_1, String styleSelector,
	                            String lineStyle) {
		for (Iterator<XYChart.Series<Number, Number>> iterator = dataList_1.iterator(); iterator.hasNext();) {
			XYChart.Series<Number, Number> curve = (XYChart.Series<Number, Number>) iterator.next();
			curve.getNode().lookup(styleSelector).setStyle(lineStyle);
		}
	}

	private void setChartBalance() {
		double balanceValue = this.util.getMMMBalanceValue(this.raceCar);
		double gripValue = this.util.getMMMGripValue(this.raceCar);
		MagicFormulaTireModel tireModel = (MagicFormulaTireModel) this.raceCar.getFrontAxleTireModel();
//		double controlValue = this.util.getMMMControlValue(raceCar,);

	}


}
