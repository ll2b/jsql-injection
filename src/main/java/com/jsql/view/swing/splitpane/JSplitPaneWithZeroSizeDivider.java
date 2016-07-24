package com.jsql.view.swing.splitpane;
/*
 * Copyright (c) 2011 Karl Tauber <karl at jformdesigner dot com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.jsql.view.swing.HelperUi;

/**
 * A JSplitPane that uses a 1 pixel thin visible divider,
 * but a 9 pixel wide transparent drag area.
 */
@SuppressWarnings("serial")
public class JSplitPaneWithZeroSizeDivider extends JSplitPane {
    /**
     * The size of the transparent drag area.
     */
    private int dividerDragSize = 9;
    
    /**
     * The offset of the transparent drag area relative to the visible divider line.
     * Positive offset moves the drag area left/top to the divider line.
     * If zero then the drag area is right/bottom of the divider line.
     * Useful values are in the range 0 to {@link #dividerDragSize}.
     * Default is centered.
     */
    private int dividerDragOffset = 4;
    
    public JSplitPaneWithZeroSizeDivider(int orientation) {
        super(orientation, true);
    }
    
    public void disableDragSize() {
        dividerDragSize = 0;
    }
    
    public void enableDragSize() {
        dividerDragSize = 9;
    }

    public int getDividerDragSize() {
        return dividerDragSize;
    }

    public void setDividerDragSize(int dividerDragSize) {
        this.dividerDragSize = dividerDragSize;
        revalidate();
    }

    public int getDividerDragOffset() {
        return dividerDragOffset;
    }

    public void setDividerDragOffset(int dividerDragOffset) {
        this.dividerDragOffset = dividerDragOffset;
        revalidate();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void layout() {
        super.layout();

        // increase divider width or height
        BasicSplitPaneDivider divider = ((BasicSplitPaneUI) getUI()).getDivider();
        Rectangle bounds = divider.getBounds();
        if (orientation == HORIZONTAL_SPLIT) {
            bounds.x -= dividerDragOffset;
            bounds.width = dividerDragSize;
        } else {
            bounds.y -= dividerDragOffset;
            bounds.height = dividerDragSize;
        }
        divider.setBounds(bounds);
    }

    @Override
    public void updateUI() {
        setUI(new SplitPaneWithZeroSizeDividerUI());
        revalidate();
    }

    private class SplitPaneWithZeroSizeDividerUI extends BasicSplitPaneUI {
        @Override
        public BasicSplitPaneDivider createDefaultDivider() {
            return new ZeroSizeDivider(this);
        }
    }

    private class ZeroSizeDivider extends BasicSplitPaneDivider {
        public ZeroSizeDivider(BasicSplitPaneUI ui) {
            super(ui);
            super.setBorder(null);
            setBackground(HelperUi.COMPONENT_BORDER);
        }

        @Override
        public void setBorder(Border border) {
            // ignore
        }

        @Override
        public void paint(Graphics g) {
            g.setColor(getBackground());
            if (orientation == HORIZONTAL_SPLIT) {
                g.drawLine(
                    dividerDragOffset,
                    0,
                    dividerDragOffset,
                    getHeight() - 1
                );
            } else {
                g.drawLine(
                    0,
                    dividerDragOffset,
                    getWidth() - 1,
                    dividerDragOffset
                );
            }
        }

        @Override
        protected void dragDividerTo(int location) {
            super.dragDividerTo(location + dividerDragOffset);
        }

        @Override
        protected void finishDraggingTo(int location) {
            super.finishDraggingTo(location + dividerDragOffset);
        }
    }
}
