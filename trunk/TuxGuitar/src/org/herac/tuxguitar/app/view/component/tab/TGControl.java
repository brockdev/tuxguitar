package org.herac.tuxguitar.app.view.component.tab;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;
import org.herac.tuxguitar.app.TuxGuitar;
import org.herac.tuxguitar.app.graphics.TGPainterImpl;
import org.herac.tuxguitar.app.system.keybindings.KeyBindingActionManager;
import org.herac.tuxguitar.app.view.util.TGBufferedPainterListenerLocked;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.graphics.control.TGBeatImpl;
import org.herac.tuxguitar.graphics.control.TGMeasureImpl;
import org.herac.tuxguitar.player.base.MidiPlayer;
import org.herac.tuxguitar.util.TGContext;

public class TGControl extends Composite {
	
//	private static final int SCROLL_DELAY = 15;
	private static final int SCROLL_INCREMENT = 50;
	
	private TGContext context; 
	
	private Tablature tablature;
	private int width;
	private int height;
	
	private TGBeatImpl playedBeat;
	private TGMeasureImpl playedMeasure;
	private int scrollX;
	private int scrollY;
	private boolean resetScroll;
	protected long lastVScrollTime;
	protected long lastHScrollTime;
	
	private boolean painting;
	
	public TGControl(TGContext context, Composite parent) {
		super(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER | SWT.DOUBLE_BUFFERED);
		this.context = context;
		this.tablature = TablatureEditor.getInstance(this.context).getTablature();
		this.initialize();
	}
	
	public void initialize() {
		this.addPaintListener(new TGBufferedPainterListenerLocked(this.context, new TGControlPaintListener(this)) );
		this.addMouseListener(this.tablature.getEditorKit().getMouseKit());
		this.addMouseMoveListener(this.tablature.getEditorKit().getMouseKit());
		this.addMouseTrackListener(this.tablature.getEditorKit().getMouseKit());
		
		final ScrollBar hBar = getHorizontalBar();
		hBar.setIncrement(SCROLL_INCREMENT);
		hBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
//				if(TGControl.this.lastHScrollTime + SCROLL_DELAY < System.currentTimeMillis()){
					redraw();
//					TGControl.this.lastHScrollTime = System.currentTimeMillis();
//				}
			}
		});
		
		final ScrollBar vBar = getVerticalBar();
		vBar.setIncrement(SCROLL_INCREMENT);
		vBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
//				if(TGControl.this.lastVScrollTime + SCROLL_DELAY < System.currentTimeMillis()){
					redraw();
//					TGControl.this.lastVScrollTime = System.currentTimeMillis();
//				}
			}
		});
		
		this.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent arg0) {
				updateScroll();
			}
		});
		
		KeyBindingActionManager.getInstance(this.context).appendListenersTo(this);
		
		this.setMenu(TuxGuitar.getInstance().getItemManager().getPopupMenu());
		this.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				TGControl.this.setMenu(null);
			}
		});
	}
	
	public void paintTablature(TGPainter painter){
		this.setPainting(true);
		try{
			this.checkScroll();
			
			int oldWidth = this.width;
			int oldHeight = this.height;
			
			TGRectangle area = createRectangle(getClientArea());
			ScrollBar xScroll = getHorizontalBar();
			ScrollBar yScroll = getVerticalBar();
			this.scrollX = xScroll.getSelection();
			this.scrollY = yScroll.getSelection();
			
			this.tablature.paintTablature(painter, area, -this.scrollX, -this.scrollY);
			
			this.width = Math.round(this.tablature.getViewLayout().getWidth());
			this.height = Math.round(this.tablature.getViewLayout().getHeight());
			
			this.updateScroll();
			
			if( MidiPlayer.getInstance(this.context).isRunning()){
				this.redrawPlayingMode(painter,true);
			}
			// Si no estoy reproduciendo y hay cambios
			// muevo el scroll al compas que tiene el caret
			else if(this.tablature.getCaret().hasChanges() || (this.width != oldWidth || this.height != oldHeight)){
				// Mover el scroll puede necesitar redibujar
				// por eso es importante desmarcar los cambios antes de hacer el moveScrollTo
				this.tablature.getCaret().setChanges(false);
				
				this.moveScrollTo(this.tablature.getCaret().getMeasure(), xScroll, yScroll, area);
			}
		}catch(Throwable throwable){
			throwable.printStackTrace();
		}
		this.setPainting(false);
	}
	
	public void resetScroll(){
		this.resetScroll = true;
	}
	
	public void checkScroll(){
		if( this.resetScroll ){
			getHorizontalBar().setSelection(0);
			getVerticalBar().setSelection(0);
			this.resetScroll = false;
		}
	}
	
	public void updateScroll(){
		Rectangle bounds = getBounds();
		Rectangle client = getClientArea();
		ScrollBar hBar = getHorizontalBar();
		ScrollBar vBar = getVerticalBar();
		hBar.setMaximum(this.width);
		vBar.setMaximum(this.height);
		hBar.setThumb(Math.min(bounds.width, client.width));
		vBar.setThumb(Math.min(bounds.height, client.height));
	}
	
	public boolean moveScrollTo(TGMeasureImpl measure){
		return moveScrollTo(measure, getHorizontalBar(), getVerticalBar(), createRectangle(getClientArea()));
	}
	
	public boolean moveScrollTo(TGMeasureImpl measure,ScrollBar xScroll,ScrollBar yScroll,TGRectangle area){
		boolean success = false;
		if( measure != null && measure.getTs() != null ){
			int mX = Math.round(measure.getPosX());
			int mY = Math.round(measure.getPosY());
			int mWidth = Math.round(measure.getWidth(this.tablature.getViewLayout()));
			int mHeight = Math.round(measure.getTs().getSize());
			int marginWidth = Math.round(this.tablature.getViewLayout().getFirstMeasureSpacing());
			int marginHeight = Math.round(this.tablature.getViewLayout().getFirstTrackSpacing());
			boolean forceRedraw = false;
			
			//Solo se ajusta si es necesario
			//si el largo del compas es mayor al de la pantalla. nunca se puede ajustar a la medida.
			if( mX < 0 || ( (mX + mWidth ) > area.getWidth() && (area.getWidth() >= mWidth + marginWidth || mX > marginWidth) ) ){
				xScroll.setSelection((this.scrollX + mX) - marginWidth );
				success = true;
			}
			
			//Solo se ajusta si es necesario
			//si el alto del compas es mayor al de la pantalla. nunca se puede ajustar a la medida.
			if( mY < 0 || ( (mY + mHeight ) > area.getHeight() && (area.getHeight() >= mHeight + marginHeight || mY > marginHeight) ) ){
				yScroll.setSelection( (this.scrollY + mY)  - marginHeight );
				success = true;
			}
			
			if(!success){
				// Si la seleccion "real" del scroll es distinta a la anterior, se fuerza el redraw
				forceRedraw = (this.scrollX != xScroll.getSelection() || this.scrollY != yScroll.getSelection());
			}
			
			if(forceRedraw || success){
				redraw();
			}
		}
		return success;
	}
	
	public void redraw(){
		if(!super.isDisposed() ){
			this.playedBeat = null;
			this.playedMeasure = null;
			this.setPainting(true);
			super.redraw();
		}
	}
	
	public void redrawPlayingMode(){
		if(!super.isDisposed() && !isPainting()){
			if( TuxGuitar.getInstance().getPlayer().isRunning() ){
				this.setPainting(true);
				
				TGPainter painter = new TGPainterImpl(new GC(this));
				redrawPlayingMode(painter, false);
				painter.dispose();
				
				this.setPainting(false);
			}
		}
	}
	
	private void redrawPlayingMode(TGPainter painter, boolean force){
		if(!super.isDisposed()){
			try{
				TGMeasureImpl measure = TuxGuitar.getInstance().getEditorCache().getPlayMeasure();
				TGBeatImpl beat = TuxGuitar.getInstance().getEditorCache().getPlayBeat();
				if(measure != null && measure.hasTrack(this.tablature.getCaret().getTrack().getNumber())){
					if(!moveScrollTo(measure) || force){
						boolean paintMeasure = (force || this.playedMeasure == null || !this.playedMeasure.equals(measure));
						if(this.playedMeasure != null && this.playedBeat != null && !this.playedMeasure.isOutOfBounds() && this.playedMeasure.hasTrack(this.tablature.getCaret().getTrack().getNumber())){
							this.tablature.getViewLayout().paintPlayMode(painter, this.playedMeasure, this.playedBeat,paintMeasure);
						}
						if(!measure.isOutOfBounds()){
							this.tablature.getViewLayout().paintPlayMode(painter, measure, beat, paintMeasure);
						}
						this.playedBeat = beat;
						this.playedMeasure =  measure;
					}
				}
			}catch(Throwable throwable){
				throwable.printStackTrace();
			}
		}
	}
	
	public boolean isPainting() {
		return this.painting;
	}
	
	public void setPainting(boolean painting) {
		this.painting = painting;
	}

	public TGContext getContext() {
		return this.context;
	}
	
	public Tablature getTablature() {
		return tablature;
	}

	public TGRectangle createRectangle( Rectangle rectangle ){
		return new TGRectangle(rectangle.x,rectangle.y,rectangle.width,rectangle.height);
	}
}