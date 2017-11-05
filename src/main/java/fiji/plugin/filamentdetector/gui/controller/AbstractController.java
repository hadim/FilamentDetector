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
package fiji.plugin.filamentdetector.gui.controller;

import fiji.plugin.filamentdetector.gui.GUIUtils;
import javafx.scene.layout.Pane;

public abstract class AbstractController implements Controller {

	private String FXMLPath;
	protected Pane pane;

	@Override
	public void setPane(Pane pane) {
		this.pane = pane;
	}

	@Override
	public Pane getPane() {
		return pane;
	}

	protected String getFXMLPath() {
		return FXMLPath;
	}

	protected void setFXMLPath(String fXMLPath) {
		this.FXMLPath = fXMLPath;
	}

	@Override
	public Pane loadPane() {
		Pane pane = GUIUtils.loadFXML(getFXMLPath(), this);
		return pane;
	}

}
