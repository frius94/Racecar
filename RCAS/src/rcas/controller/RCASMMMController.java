package rcas.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import rcas.model.MagicFormulaTireModel;
import rcas.model.RaceCar;
import rcas.util.CorneringAnalyserUtil;

import java.util.Iterator;
@SuppressWarnings("Duplicates")
public class RCASMMMController {


	@FXML
	private LineChart<Number, Number> mainChart;
	@FXML
	private BarChart<String, Number> balanceChart, gripChart, controlChart, stabilityChart;


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
		setBalanceChart();
		setGripChart();
		initChart();
        setControlChart();
        setStabilityChart();
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

	private void setBalanceChart() {
		double balanceValue = this.util.getMMMBalanceValue(raceCar);
		balanceChart.setTitle("Balance in Nm");
		XYChart.Series<String, Number> balanceSeries = new XYChart.Series<>();
		balanceSeries.setName(raceCar.getName());
		balanceSeries.getData().add(new XYChart.Data<>("", balanceValue));
		balanceChart.getData().add(balanceSeries);

	}

	private void setGripChart() {
        double gripValue = this.util.getMMMGripValue(raceCar);
        gripChart.setTitle("Grip in m/s^2");
        XYChart.Series<String, Number> gripSeries = new XYChart.Series<>();
        gripSeries.setName(raceCar.getName());
        gripSeries.getData().add(new XYChart.Data<>("", gripValue));
        gripChart.getData().add(gripSeries);
    }


    private void setControlChart() {
        double controlValue = this.util.getMMMControlValue(raceCar, 0.0, 0.0, 10.0);
        controlChart.setTitle("Control in Nm/degree");
        XYChart.Series<String, Number> controlSeries = new XYChart.Series<>();
        controlSeries.setName(raceCar.getName());
        controlSeries.getData().add(new XYChart.Data<>("", controlValue));
        controlChart.getData().add(controlSeries);
    }

    private void setStabilityChart() {
        double stabilityValue = this.util.getMMMStabilityValue(raceCar, 0.0, 0.0, 1.0);
        stabilityChart.setTitle("Stability in Nm/degree");
        XYChart.Series<String, Number> stabilitySeries = new XYChart.Series<>();
        stabilitySeries.setName(raceCar.getName());
        stabilitySeries.getData().add(new XYChart.Data<>("", stabilityValue));
        stabilityChart.getData().add(stabilitySeries);
    }
}
