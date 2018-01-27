/*-
 * #%L
 * A Fiji plugin that allow easy, fast and accurate detection and tracking of biological filaments.
 * %%
 * Copyright (C) 2016 - 2017 Hadrien Mary
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */
package sc.fiji.filamentdetector.gui.controller.imagepreprocessor;

import java.net.URL;
import java.util.ResourceBundle;

import org.scijava.Context;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import sc.fiji.filamentdetector.imagepreprocessor.Convert8BitPreprocessor;
import sc.fiji.filamentdetector.imagepreprocessor.ImagePreprocessor;

public class Convert8BitController extends AbstractImagePreprocessorController {

	public static String FXML_PATH = "/sc/fiji/filamentdetector/gui/view/preprocessor/Convert8BitView.fxml";

	@FXML
	private CheckBox convert8BitCheckbox;

	public Convert8BitController(Context context, ImagePreprocessor imagePreprocessor) {
		super(context, imagePreprocessor);
		setFXMLPath(FXML_PATH);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Convert8BitPreprocessor imagePreprocessor = (Convert8BitPreprocessor) getImagePreprocessor();
		convert8BitCheckbox.setSelected(imagePreprocessor.isDoPreprocess());
	}

	public void updateParameters() {
		Convert8BitPreprocessor imagePreprocessor = (Convert8BitPreprocessor) getImagePreprocessor();
		imagePreprocessor.setDoPreprocess(convert8BitCheckbox.isSelected());
	}

}