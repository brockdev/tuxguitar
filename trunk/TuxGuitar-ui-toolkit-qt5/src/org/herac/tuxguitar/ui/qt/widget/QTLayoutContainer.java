package org.herac.tuxguitar.ui.qt.widget;

import org.herac.tuxguitar.ui.layout.UILayout;
import org.herac.tuxguitar.ui.resource.UIRectangle;
import org.herac.tuxguitar.ui.resource.UISize;
import org.herac.tuxguitar.ui.widget.UIControl;
import org.herac.tuxguitar.ui.widget.UILayoutContainer;
import org.qtjambi.qt.core.QMargins;
import org.qtjambi.qt.core.QSize;
import org.qtjambi.qt.widgets.QWidget;

public abstract class QTLayoutContainer<T extends QWidget> extends QTAbstractContainer<T> implements UILayoutContainer {
	
	private UILayout layout;
	private UISize packedContentSize;
	private QMargins containerMargins;
	private QMargins containerChildMargins;
	
	public QTLayoutContainer(T control, QTContainer parent, boolean immediatelyShow) {
		super(control, parent, immediatelyShow);
		
		this.packedContentSize = new UISize();
		this.containerMargins = new QMargins(0, 0, 0, 0);
		this.containerChildMargins = new QMargins(0, 0, 0, 0);
	}
	
	public QTLayoutContainer(T control, QTContainer parent) {
		this(control, parent, true);
	}
	
	public QWidget getContainerControl() {
		return this.getControl();
	}
	
	public QSize getContainerSize() {
		return this.getContainerControl().size();
	}
	
	public UILayout getLayout() {
		return layout;
	}

	public void setLayout(UILayout layout) {
		this.layout = layout;
	}
	
	public void setPackedContentSize(UISize packedContentSize) {
		this.packedContentSize.setWidth(packedContentSize.getWidth());
		this.packedContentSize.setHeight(packedContentSize.getHeight());
	}

	public UISize getPackedContentSize() {
		return new UISize(this.packedContentSize.getWidth(), this.packedContentSize.getHeight());
	}
	
	public QMargins getContainerMargins() {
		return this.getMargins(this.containerMargins);
	}

	public void setContainerMargins(QMargins containerMargins) {
		this.setMargins(this.containerMargins, containerMargins);
	}
	
	public QMargins getContainerChildMargins() {
		return this.getMargins(this.containerChildMargins);
	}
	
	public void setContainerChildMargins(QMargins containerChildMargins) {
		this.setMargins(this.containerChildMargins, containerChildMargins);
	}

	public void setMargins(QMargins target, QMargins source) {
		target.setTop(source.top());
		target.setLeft(source.left());
		target.setRight(source.right());
		target.setBottom(source.bottom());
	}
	
	public QMargins getMargins(QMargins source) {
		return new QMargins(source.left(), source.top(), source.right(), source.bottom());
	}
	
	public UIRectangle getChildArea(QSize size, QMargins margins) {
		return new UIRectangle(margins.left(), margins.top(), (size.width() - (margins.left() + margins.right())), (size.height() - (margins.top() + margins.bottom())));
	}
	
	public UIRectangle getChildArea() {
		return this.getChildArea(this.getContainerSize(), this.containerChildMargins);
	}
	
	public void computeMargins() {
		this.setContainerChildMargins(this.getContainerControl().contentsMargins());
	}
	
	public void computePackedSize() {
		this.computeMargins();
		
		for(UIControl uiControl : this.getChildren()) {
			uiControl.computePackedSize();
		}
		
		if( this.layout != null ) {
			UISize packedContentSize = this.layout.computePackedSize(this);
			
			this.setPackedContentSize(packedContentSize);
			this.computePackedSizeFor(packedContentSize);
		}
	}
	
	public void computePackedSizeFor(UISize packedContentSize) {
		int top = (this.containerMargins.top() + this.containerChildMargins.top());
		int left = (this.containerMargins.left() + this.containerChildMargins.left());
		int right = (this.containerMargins.right() + this.containerChildMargins.right());
		int bottom = (this.containerMargins.bottom() + this.containerChildMargins.bottom());
		
		UISize packedSize = new UISize();
		packedSize.setWidth((float) (left + right + packedContentSize.getWidth()));
		packedSize.setHeight((float) (top + bottom + packedContentSize.getHeight()));
		
		this.setPackedSize(packedSize);
	}
	
	public void setBounds(UIRectangle bounds) {
		super.setBounds(bounds);
		
		if( this.layout != null ) {
			this.layout.setBounds(this, this.getChildArea());
		}
		else {
			for(UIControl uiControl : this.getChildren()) {
				uiControl.setBounds(uiControl.getBounds());
			}
		}
	}

	public void layout() {
		this.layout(this.getBounds());
	}

	public void layout(UIRectangle bounds) {
		this.computePackedSize();
		this.setBounds(bounds);
	}

	public void pack() {
		this.computePackedSize();
		this.setBounds(new UIRectangle(this.getBounds().getPosition(), this.getPackedSize()));
	}
}
