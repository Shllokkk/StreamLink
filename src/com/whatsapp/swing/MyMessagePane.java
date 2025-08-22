/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.whatsapp.swing;

import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BoxView;
import javax.swing.text.ComponentView;
import javax.swing.text.Element;
import javax.swing.text.IconView;
import javax.swing.text.LabelView;
import javax.swing.text.ParagraphView;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;


public class MyMessagePane extends JTextPane {

    public MyMessagePane() {
        super();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setEditorKit(new WarpEditorKit());
    }

    private static class WarpEditorKit extends StyledEditorKit {
        private final ViewFactory defaultFactory = new WarpColumnFactory();

        @Override
        public ViewFactory getViewFactory() {
            return defaultFactory;
        }
    }

    private static class WarpColumnFactory implements ViewFactory {
        @Override
        public View create(Element elem) {
            switch (elem.getName()) {
                case AbstractDocument.ContentElementName:
                    return new WarpLabelView(elem);
                case AbstractDocument.ParagraphElementName:
                    return new ParagraphView(elem);
                case AbstractDocument.SectionElementName:
                    return new BoxView(elem, View.Y_AXIS);
                case StyleConstants.ComponentElementName:
                    return new ComponentView(elem);
                case StyleConstants.IconElementName:
                    return new IconView(elem);
                default:
                    return new LabelView(elem);  // Default to text display
            }
        }
    }

    private static class WarpLabelView extends LabelView {
        public WarpLabelView(Element elem) {
            super(elem);
        }

        @Override
        public float getMinimumSpan(int axis) {
            if (axis == View.X_AXIS) {
                return 0;
            }
            if (axis == View.Y_AXIS) {
                return super.getMinimumSpan(axis);
            }
            throw new IllegalArgumentException("Invalid axis: " + axis);
        }
    }
}
