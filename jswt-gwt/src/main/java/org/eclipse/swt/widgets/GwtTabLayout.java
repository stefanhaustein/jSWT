/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

class GwtTabLayout extends Layout {


@Override
protected Point computeSize(Composite composite, int wHint, int hHint, boolean flushCache) {
	Control children[] = composite.getChildren();
	int maxWidth = 0;
	int maxHeight = 0;
	for (int i = 0; i < children.length; i++) {
		Point size = children[i].computeSize(wHint, hHint, flushCache);
		maxWidth = Math.max(size.x, maxWidth);
		maxHeight = Math.max(size.y, maxHeight);
	}
	if (wHint != SWT.DEFAULT) maxWidth = wHint;
	if (hHint != SWT.DEFAULT) maxHeight = hHint;
	return new Point(maxWidth, maxHeight);
}

@Override
protected boolean flushCache(Control control) {
	return true;
}

@Override
protected void layout(Composite composite, boolean flushCache) {
	Control children[] = composite.getChildren();
	Rectangle rect = composite.getClientArea();
	for (int i = 0; i < children.length; i++) {
		children[i].setBounds(rect);
	}
}

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the layout
 */
@Override
public String toString () {
 	return getName();
}
}
