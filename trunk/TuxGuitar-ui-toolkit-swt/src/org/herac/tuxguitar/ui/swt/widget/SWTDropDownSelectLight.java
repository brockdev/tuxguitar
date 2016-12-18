package org.herac.tuxguitar.ui.swt.widget;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.herac.tuxguitar.ui.event.UISelectionListener;
import org.herac.tuxguitar.ui.swt.event.SWTSelectionListenerManager;
import org.herac.tuxguitar.ui.widget.UIDropDownSelect;
import org.herac.tuxguitar.ui.widget.UISelectItem;

public class SWTDropDownSelectLight<T> extends SWTControl<Combo> implements UIDropDownSelect<T> {
	
	private List<UISelectItem<T>> uiItems;
	private SWTDropDownComboEventListener selectionListener;
	private int currentIndex;
	
	public SWTDropDownSelectLight(SWTContainer<? extends Composite> parent) {
		super(new Combo(parent.getControl(), SWT.DROP_DOWN), parent);
		
		this.uiItems = new ArrayList<UISelectItem<T>>();
		this.selectionListener = new SWTDropDownComboEventListener(this);
		
		this.setCurrentIndex(-1);
		this.getControl().addModifyListener(this.selectionListener);
		this.getControl().addSelectionListener(this.selectionListener);
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public T getSelectedValue() {
		UISelectItem<T> selectedItem = this.getSelectedItem();
		return (selectedItem != null ? selectedItem.getValue() : null);
	}
	
	public void setSelectedValue(T value) {
		this.setSelectedItem(new UISelectItem<T>(null, value));
	}
	
	public UISelectItem<T> getSelectedItem() {
		int index = this.getControl().getSelectionIndex();
		return (index >= 0 && index < this.uiItems.size() ? this.uiItems.get(index) : null);
	}

	public void setSelectedItem(UISelectItem<T> item) {
		this.setCurrentIndex(item != null ? this.uiItems.indexOf(item) : -1);
		if( this.getCurrentIndex() != this.getControl().getSelectionIndex() ) {
			this.getControl().select(this.getCurrentIndex());
		}
	}

	public void addItem(UISelectItem<T> item) {
		this.uiItems.add(item);
		this.getControl().add(item.getText());
	}
	
	public void removeItem(UISelectItem<T> item) {
		int index = (item != null ? this.uiItems.indexOf(item) : -1);
		if( index >= 0 && index < this.uiItems.size() ) {
			this.getControl().remove(index);
			this.uiItems.remove(item);
		}
	}
	
	public void removeItems() {
		List<UISelectItem<T>> uiItems = new ArrayList<UISelectItem<T>>(this.uiItems);
		for(UISelectItem<T> uiItem : uiItems) {
			this.removeItem(uiItem);
		}
	}
	
	public int getItemCount() {
		return this.uiItems.size();
	}

	public void addSelectionListener(UISelectionListener listener) {
		this.selectionListener.addListener(listener);
	}

	public void removeSelectionListener(UISelectionListener listener) {
		this.selectionListener.removeListener(listener);
	}
	
	private static class SWTDropDownComboEventListener extends SWTSelectionListenerManager implements ModifyListener {
		
		public SWTDropDownComboEventListener(SWTDropDownSelectLight<?> control) {
			super(control);
		}
		
		public void widgetSelected(SelectionEvent e) {
			this.getDropDownControl().setCurrentIndex(this.getDropDownControl().getControl().getSelectionIndex());
			
			super.widgetSelected(e);
		}

		public void modifyText(ModifyEvent e) {
			SWTDropDownSelectLight<?> control = this.getDropDownControl();
			if( control.getControl().getSelectionIndex() == -1 ) {
				control.getControl().select(control.getCurrentIndex());
			}
		}
		
		public SWTDropDownSelectLight<?> getDropDownControl() {
			return (SWTDropDownSelectLight<?>) this.getControl();
		}
	}
}