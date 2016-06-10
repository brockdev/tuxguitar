package org.herac.tuxguitar.app.action.impl.file;

import org.herac.tuxguitar.action.TGActionContext;
import org.herac.tuxguitar.action.TGActionManager;
import org.herac.tuxguitar.app.action.impl.view.TGOpenViewAction;
import org.herac.tuxguitar.app.graphics.TGPainterImpl;
import org.herac.tuxguitar.app.graphics.TGResourceFactoryImpl;
import org.herac.tuxguitar.app.printer.PrintController;
import org.herac.tuxguitar.app.printer.PrintDocument;
import org.herac.tuxguitar.app.printer.PrintLayout;
import org.herac.tuxguitar.app.printer.PrintStyles;
import org.herac.tuxguitar.app.ui.TGApplication;
import org.herac.tuxguitar.app.view.dialog.printer.TGPrintStylesDialog;
import org.herac.tuxguitar.app.view.dialog.printer.TGPrintStylesDialogController;
import org.herac.tuxguitar.app.view.dialog.printer.TGPrintStylesHandler;
import org.herac.tuxguitar.app.view.dialog.printer.TGPrinterChooserDialog;
import org.herac.tuxguitar.app.view.dialog.printer.TGPrinterChooserDialogController;
import org.herac.tuxguitar.app.view.dialog.printer.TGPrinterChooserHandler;
import org.herac.tuxguitar.document.TGDocumentContextAttributes;
import org.herac.tuxguitar.editor.action.TGActionBase;
import org.herac.tuxguitar.graphics.TGPainter;
import org.herac.tuxguitar.graphics.TGRectangle;
import org.herac.tuxguitar.graphics.control.TGFactoryImpl;
import org.herac.tuxguitar.song.managers.TGSongManager;
import org.herac.tuxguitar.song.models.TGSong;
import org.herac.tuxguitar.ui.printer.UIPrinter;
import org.herac.tuxguitar.ui.printer.UIPrinterJob;
import org.herac.tuxguitar.ui.printer.UIPrinterPage;
import org.herac.tuxguitar.ui.resource.UIRectangle;
import org.herac.tuxguitar.util.TGContext;
import org.herac.tuxguitar.util.TGSynchronizer;

public class TGPrintAction extends TGActionBase{
	
	public static final String NAME = "action.file.print";
	
	public static final String ATTRIBUTE_STYLES = PrintStyles.class.getName();
	public static final String ATTRIBUTE_PRINTER = UIPrinter.class.getName();
	
	public TGPrintAction(TGContext context) {
		super(context, NAME);
	}
	
	protected void processAction(TGActionContext context){
		PrintStyles styles = context.getAttribute(ATTRIBUTE_STYLES);
		if( styles == null ) {
			this.configureStyles(context);
			return;
		}
		
		UIPrinter printer = context.getAttribute(ATTRIBUTE_PRINTER);
		if( printer == null ) {
			this.configurePrinterData(context);
			return;
		}
		
		TGSongManager manager = new TGSongManager(new TGFactoryImpl());
		TGSong sourceSong = context.getAttribute(TGDocumentContextAttributes.ATTRIBUTE_SONG);
		TGSong targetSong = sourceSong.clone(manager.getFactory());
		
		PrintController controller = new PrintController(targetSong, manager, new TGResourceFactoryImpl(printer.getResourceFactory()));
		PrintLayout printLayout = new PrintLayout(controller, styles);
		printLayout.loadStyles(printer.getDpiScale(), 1f);
		printLayout.updateSong();
		printLayout.makeDocument(new PrintDocumentImpl(printLayout, printer, getPrinterArea(printer, 10f)));
		printLayout.getResourceBuffer().disposeAllResources();
	}
	
	public void configureStyles(final TGActionContext context) {
		context.setAttribute(TGOpenViewAction.ATTRIBUTE_CONTROLLER, new TGPrintStylesDialogController());
		context.setAttribute(TGPrintStylesDialog.ATTRIBUTE_HANDLER, new TGPrintStylesHandler() {
			public void updatePrintStyles(PrintStyles styles) {
				context.setAttribute(ATTRIBUTE_STYLES, styles);
				executeActionInNewThread(TGPrintAction.NAME, context);
			}
		});
		executeActionInNewThread(TGOpenViewAction.NAME, context);
	}
	
	public void configurePrinterData(final TGActionContext context) {
		context.setAttribute(TGOpenViewAction.ATTRIBUTE_CONTROLLER, new TGPrinterChooserDialogController());
		context.setAttribute(TGPrinterChooserDialog.ATTRIBUTE_HANDLER, new TGPrinterChooserHandler() {
			public void updatePrinter(UIPrinter printer) {
				context.setAttribute(ATTRIBUTE_PRINTER, printer);
				executeActionInNewThread(TGPrintAction.NAME, context);
			}
		});
		executeActionInNewThread(TGOpenViewAction.NAME, context);
	}
	
	public void executeActionInNewThread(final String id, final TGActionContext context) {
		new Thread(new Runnable() {
			public void run() {
				TGActionManager.getInstance(getContext()).execute(id, context);
			}
		}).start();
	}
	
	protected TGRectangle getPrinterArea(UIPrinter printer, float margin) {
		Float scale = printer.getDpiScale();
		Float scaledMargin = (margin * (scale != null ? scale : 1f));
		UIRectangle bounds = printer.getBounds();
		
		return new TGRectangle(bounds.getX() + scaledMargin, bounds.getY() + scaledMargin, bounds.getWidth() - (scaledMargin * 2f), bounds.getHeight() - (scaledMargin * 2f));
	}
	
	private class PrintDocumentImpl implements PrintDocument{
		
		private PrintLayout layout;
		private UIPrinter printer;
		private UIPrinterJob printerJob;
		private UIPrinterPage printerPage;
		
		private TGPainterImpl painter;
		private TGRectangle bounds;
		
		public PrintDocumentImpl(PrintLayout layout, UIPrinter printer, TGRectangle bounds){
			this.layout = layout;
			this.printer = printer;
			this.bounds = bounds;
			this.painter = new TGPainterImpl(printer.getResourceFactory());
		}
		
		public TGPainter getPainter() {
			return this.painter;
		}
		
		public TGRectangle getBounds(){
			return this.bounds;
		}
		
		public void pageStart() {
			if( this.printerJob != null && !this.printerJob.isDisposed() ){
				this.printerPage = this.printerJob.createPage();
				this.painter.setHandle(this.printerPage.getPainter());
			}
		}
		
		public void pageFinish() {
			if( this.printerPage != null && !this.printerPage.isDisposed() ){
				this.printerPage.dispose();
			}
		}
		
		public void start() {
			this.printerJob = this.printer.createJob(getJobName());
		}
		
		public void finish() {
			if( this.printerJob != null && !this.printerJob.isDisposed() ){
				this.printerJob.dispose();
				this.printerJob = null;
				TGSynchronizer.getInstance(getContext()).executeLater(new Runnable(){
					public void run() {
						dispose();
					}
				});
			}
		}
		
		public boolean isPaintable(int page){
			Integer startPage = this.printer.getStartPage();
			if( startPage != null && startPage > 0 && startPage > page){
				return false;
			}
			
			Integer endPage = this.printer.getEndPage();
			if( endPage != null && endPage > 0 && endPage < page){
				return false;
			}
			return true;
		}
		
		public String getJobName(){
			String prefix = TGApplication.NAME;
			String song = this.layout.getSong().getName();
			return ( song != null && song.length() > 0 ? (prefix + "-" + song) : prefix );
		}
		
		public void dispose(){
			if(!this.printer.isDisposed()){
				this.printer.dispose();
			}
		}
	}
}