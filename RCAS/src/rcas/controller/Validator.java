package rcas.controller;

import com.jfoenix.validation.base.ValidatorBase;
import javafx.beans.DefaultProperty;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Field validation, that is applied on text input
 * controls such as {@link TextField} and {@link TextArea}
 * with a min and max Double value
 *
 * @author Christopher O'Connor
 * @version 1.0
 * @since 2019-03-26
 */

@DefaultProperty(value = "icon")
public class Validator extends ValidatorBase {

	private double min;
	private double max;

	public Validator(double min, double max) {
		this.min = min;
		this.max = max;
	}


	@Override
	protected void eval() {

		hasErrors.set(false);

		TextInputControl textField = (TextInputControl) srcControl.get();

		if (textField.getText() == null || textField.getText().isEmpty()) {

			super.setMessage("Required");
			hasErrors.set(true);

		}

		else if (!isDouble(textField.getText())) {

			super.setMessage("Number");
			hasErrors.set(true);

		}

		else if (Double.valueOf(textField.getText()) < min || Double.valueOf(textField.getText()) > max) {

			NumberFormat nf = new DecimalFormat("####.#");

			super.setMessage(nf.format(min) + " - " + nf.format(max));
			hasErrors.set(true);

		}

	}

	private boolean isDouble(String s) {

		try {

			Double.valueOf(s);
			return true;

		} catch (NumberFormatException e) { return false; }

	}

	public void hasNoErrors() {
		this.hasErrors.set(false);
	}

}
