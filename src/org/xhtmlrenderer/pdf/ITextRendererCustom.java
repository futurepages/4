//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.xhtmlrenderer.pdf;

import flyingsaucerproject.com.lowagie.text.DocumentException;
import flyingsaucerproject.com.lowagie.text.pdf.PdfWriter;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.regex.Pattern;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xhtmlrenderer.extend.NamespaceHandler;
import org.xhtmlrenderer.extend.UserInterface;
import org.xhtmlrenderer.layout.BoxBuilder;
import org.xhtmlrenderer.layout.Layer;
import org.xhtmlrenderer.layout.LayoutContext;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.render.BlockBox;
import org.xhtmlrenderer.render.PageBox;
import org.xhtmlrenderer.render.RenderingContext;
import org.xhtmlrenderer.render.ViewportBox;
import org.xhtmlrenderer.resource.XMLResource;
import org.xhtmlrenderer.simple.extend.XhtmlNamespaceHandler;
import org.xhtmlrenderer.util.Configuration;
import org.xml.sax.InputSource;

public class ITextRendererCustom extends ITextRenderer {
	private static final float DEFAULT_DOTS_PER_POINT = 26.666666F;
	private static final int DEFAULT_DOTS_PER_PIXEL = 20;
	private SharedContext _sharedContext;
	private ITextOutputDevice _outputDevice;
	private Document _doc;
	private BlockBox _root;
	private float _dotsPerPoint;
	private flyingsaucerproject.com.lowagie.text.Document _pdfDoc;
	private PdfWriter _writer;
	private PDFEncryption _pdfEncryption;
	private Character _pdfVersion;
	private char[] validPdfVersions;
	private PDFCreationListener _listener;

	public ITextRendererCustom() {
		this(26.666666F, 20);
	}

	public ITextRendererCustom(float dotsPerPoint, int dotsPerPixel) {
		this.validPdfVersions = new char[]{'2', '3', '4', '5', '6', '7'};
		this._dotsPerPoint = dotsPerPoint;
		this._outputDevice = new ITextOutputDevice(this._dotsPerPoint);
		ITextUserAgent userAgent = new ITextUserAgent(this._outputDevice);
		this._sharedContext = new SharedContext(userAgent);
		userAgent.setSharedContext(this._sharedContext);
		this._outputDevice.setSharedContext(this._sharedContext);
		ITextFontResolver fontResolver = new ITextFontResolver(this._sharedContext);
		this._sharedContext.setFontResolver(fontResolver);
		ITextReplacedElementFactory replacedElementFactory = new ITextReplacedElementFactory(this._outputDevice);
		this._sharedContext.setReplacedElementFactory(replacedElementFactory);
		this._sharedContext.setTextRenderer(new ITextTextRenderer());
		this._sharedContext.setDPI(72.0F * this._dotsPerPoint);
		this._sharedContext.setDotsPerPixel(dotsPerPixel);
		this._sharedContext.setPrint(true);
		this._sharedContext.setInteractive(false);
	}

	public ITextFontResolver getFontResolver() {
		return (ITextFontResolver)this._sharedContext.getFontResolver();
	}

	private Document loadDocument(String uri) {
		return this._sharedContext.getUac().getXMLResource(uri).getDocument();
	}

	public void setDocument(String uri) {
		this.setDocument(this.loadDocument(uri), uri);
	}

	public void setDocument(Document doc, String url) {
		this.setDocument(doc, url, new XhtmlNamespaceHandler());
	}

	public void setDocument(File file) throws IOException {
		File parent = file.getAbsoluteFile().getParentFile();
		this.setDocument(this.loadDocument(file.toURI().toURL().toExternalForm()), parent == null ? "" : parent.toURI().toURL().toExternalForm());
	}

	public void setDocumentFromString(String content) {
		InputSource is = new InputSource(new BufferedReader(new StringReader(content)));
		Document dom = XMLResource.load(is).getDocument();
		this.setDocument(dom, (String)null);
	}

	public void setDocument(Document doc, String url, NamespaceHandler nsh) {
		this._doc = doc;
		this.getFontResolver().flushFontFaceFonts();
		this._sharedContext.reset();
		if (Configuration.isTrue("xr.cache.stylesheets", true)) {
			this._sharedContext.getCss().flushStyleSheets();
		} else {
			this._sharedContext.getCss().flushAllStyleSheets();
		}

		this._sharedContext.setBaseURL(url);
		this._sharedContext.setNamespaceHandler(nsh);
		this._sharedContext.getCss().setDocumentContext(this._sharedContext, this._sharedContext.getNamespaceHandler(), doc, new ITextRendererCustom.NullUserInterface());
		this.getFontResolver().importFontFaces(this._sharedContext.getCss().getFontFaceRules());
	}

	public PDFEncryption getPDFEncryption() {
		return this._pdfEncryption;
	}

	public void setPDFEncryption(PDFEncryption pdfEncryption) {
		this._pdfEncryption = pdfEncryption;
	}

	public void setPDFVersion(char _v) {
		for(int i = 0; i < this.validPdfVersions.length; ++i) {
			if (_v == this.validPdfVersions[i]) {
				this._pdfVersion = new Character(_v);
				return;
			}
		}

		throw new IllegalArgumentException("Invalid PDF version character; use valid constants from PdfWriter (e.g. PdfWriter.VERSION_1_2)");
	}

	public char getPDFVersion() {
		return this._pdfVersion == null ? '0' : this._pdfVersion;
	}

	public void layout() {
		LayoutContext c = this.newLayoutContext();
		BlockBox root = BoxBuilder.createRootBox(c, this._doc);
		root.setContainingBlock(new ViewportBox(this.getInitialExtents(c)));
		root.layout(c);
		Dimension dim = root.getLayer().getPaintingDimension(c);
		dim.setSize(dim.getHeight(), dim.getWidth());
		root.getLayer().trimEmptyPages(c, dim.width);
		root.getLayer().layoutPages(c);
		this._root = root;
	}

	private Rectangle getInitialExtents(LayoutContext c) {
		PageBox first = Layer.createPageBox(c, "first");
		return new Rectangle(0, 0, first.getContentWidth(c), first.getContentHeight(c));
	}

	private RenderingContext newRenderingContext() {
		RenderingContext result = this._sharedContext.newRenderingContextInstance();
		result.setFontContext(new ITextFontContext());
		result.setOutputDevice(this._outputDevice);
		this._sharedContext.getTextRenderer().setup(result.getFontContext());
		result.setRootLayer(this._root.getLayer());
		return result;
	}

	private LayoutContext newLayoutContext() {
		LayoutContext result = this._sharedContext.newLayoutContextInstance();
		result.setFontContext(new ITextFontContext());
		this._sharedContext.getTextRenderer().setup(result.getFontContext());
		return result;
	}

	public void createPDF(OutputStream os) throws DocumentException {
		this.createPDF(os, true, 0);
	}

	public void writeNextDocument() throws DocumentException {
		this.writeNextDocument(0);
	}

	public void writeNextDocument(int initialPageNo) throws DocumentException {
		List pages = this._root.getLayer().getPages();
		RenderingContext c = this.newRenderingContext();
		c.setInitialPageNo(initialPageNo);
		PageBox firstPage = (PageBox)pages.get(0);
		flyingsaucerproject.com.lowagie.text.Rectangle firstPageSize = new flyingsaucerproject.com.lowagie.text.Rectangle(0.0F, 0.0F, (float)firstPage.getWidth(c) / this._dotsPerPoint, (float)firstPage.getHeight(c) / this._dotsPerPoint);

		this._outputDevice.setStartPageNo(this._writer.getPageNumber());
		this._pdfDoc.setPageSize(firstPageSize);
		this._pdfDoc.newPage();
		this.writePDF(pages, c, firstPageSize, this._pdfDoc, this._writer);
	}

	public void finishPDF() {
		if (this._pdfDoc != null) {
			this.fireOnClose();
			this._pdfDoc.close();
		}

	}

	public void createPDF(OutputStream os, boolean finish) throws DocumentException {
		this.createPDF(os, finish, 0);
	}

	public void createPDF(OutputStream os, boolean finish, int initialPageNo) throws DocumentException {
		List pages = this._root.getLayer().getPages();
		RenderingContext c = this.newRenderingContext();
		c.setInitialPageNo(initialPageNo);
		PageBox firstPage = (PageBox)pages.get(0);
		flyingsaucerproject.com.lowagie.text.Rectangle firstPageSize = new flyingsaucerproject.com.lowagie.text.Rectangle(0.0F, 0.0F, (float)firstPage.getWidth(c) / this._dotsPerPoint, (float)firstPage.getHeight(c) / this._dotsPerPoint);
		flyingsaucerproject.com.lowagie.text.Document doc = new flyingsaucerproject.com.lowagie.text.Document(firstPageSize, 0.0F, 0.0F, 0.0F, 0.0F);
		PdfWriter writer = PdfWriter.getInstance(doc, os);
		if (this._pdfVersion != null) {
			writer.setPdfVersion(this._pdfVersion);
		}

		if (this._pdfEncryption != null) {
			writer.setEncryption(this._pdfEncryption.getUserPassword(), this._pdfEncryption.getOwnerPassword(), this._pdfEncryption.getAllowedPrivileges(), this._pdfEncryption.getEncryptionType());
		}

		this._pdfDoc = doc;
		this._writer = writer;
		this.firePreOpen();
		doc.open();
		this.writePDF(pages, c, firstPageSize, doc, writer);
		if (finish) {
			this.fireOnClose();
			doc.close();
		}

	}

	private void firePreOpen() {
		if (this._listener != null) {
			this._listener.preOpen(this);
		}

	}

	private void fireOnClose() {
		if (this._listener != null) {
			this._listener.onClose(this);
		}

	}

	private void writePDF(List pages, RenderingContext c, flyingsaucerproject.com.lowagie.text.Rectangle firstPageSize, flyingsaucerproject.com.lowagie.text.Document doc, PdfWriter writer) throws DocumentException {
		this._outputDevice.setRoot(this._root);
		this._outputDevice.start(this._doc);
		this._outputDevice.setWriter(writer);
		this._outputDevice.initializePage(writer.getDirectContent(), firstPageSize.getHeight());
		this._root.getLayer().assignPagePaintingPositions(c, (short)2);
		int pageCount = this._root.getLayer().getPages().size();
		c.setPageCount(pageCount);

		for(int i = 0; i < pageCount; ++i) {
			PageBox currentPage = (PageBox)pages.get(i);
			c.setPage(i, currentPage);
			this.paintPage(c, writer, currentPage);
			this._outputDevice.finishPage();
			if (i != pageCount - 1) {
				PageBox nextPage = (PageBox)pages.get(i + 1);
				flyingsaucerproject.com.lowagie.text.Rectangle nextPageSize = new flyingsaucerproject.com.lowagie.text.Rectangle(0.0F, 0.0F, (float)nextPage.getWidth(c) / this._dotsPerPoint, (float)nextPage.getHeight(c) / this._dotsPerPoint);
				doc.setPageSize(nextPageSize);
				doc.newPage();
				this._outputDevice.initializePage(writer.getDirectContent(), nextPageSize.getHeight());
			}
		}

		this._outputDevice.finish(c, this._root);
	}

	private void paintPage(RenderingContext c, PdfWriter writer, PageBox page) {
		this.provideMetadataToPage(writer, page);
		page.paintBackground(c, 0, (short)2);
		page.paintMarginAreas(c, 0, (short)2);
		page.paintBorder(c, 0, (short)2);
		Shape working = this._outputDevice.getClip();
		Rectangle content = page.getPrintClippingBounds(c);
		this._outputDevice.clip(content);
		int top = -page.getPaintingTop() + page.getMarginBorderPadding(c, 3);
		int left = page.getMarginBorderPadding(c, 1);
		this._outputDevice.translate((double)left, (double)top);
		this._root.getLayer().paint(c);
		this._outputDevice.translate((double)(-left), (double)(-top));
		this._outputDevice.setClip(working);
	}

	private void provideMetadataToPage(PdfWriter writer, PageBox page) {
		byte[] metadata = null;
		if (page.getMetadata() != null) {
			try {
				String metadataBody = this.stringfyMetadata(page.getMetadata());
				if (metadataBody != null) {
					metadata = this.createXPacket(this.stringfyMetadata(page.getMetadata())).getBytes("UTF-8");
				}
			} catch (UnsupportedEncodingException var5) {
				throw new RuntimeException(var5);
			}
		}

		writer.setPageXmpMetadata(metadata);
	}

	private String stringfyMetadata(Element element) {
		Element target = getFirstChildElement(element);
		if (target == null) {
			return null;
		} else {
			try {
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();
				transformer.setOutputProperty("omit-xml-declaration", "yes");
				StringWriter output = new StringWriter();
				transformer.transform(new DOMSource(target), new StreamResult(output));
				return output.toString();
			} catch (TransformerConfigurationException var6) {
				throw new RuntimeException(var6);
			} catch (TransformerException var7) {
				throw new RuntimeException(var7);
			}
		}
	}

	private static Element getFirstChildElement(Element element) {
		for(Node n = element.getFirstChild(); n != null; n = n.getNextSibling()) {
			if (n.getNodeType() == 1) {
				return (Element)n;
			}
		}

		return null;
	}

	private String createXPacket(String metadata) {
		StringBuffer result = new StringBuffer(metadata.length() + 50);
		result.append("<?xpacket begin='\ufeff' id='W5M0MpCehiHzreSzNTczkc9d'?>\n");
		result.append(metadata);
		result.append("\n<?xpacket end='r'?>");
		return result.toString();
	}

	public ITextOutputDevice getOutputDevice() {
		return this._outputDevice;
	}

	public SharedContext getSharedContext() {
		return this._sharedContext;
	}

	public void exportText(Writer writer) throws IOException {
		RenderingContext c = this.newRenderingContext();
		c.setPageCount(this._root.getLayer().getPages().size());
		this._root.exportText(c, writer);
	}

	public BlockBox getRootBox() {
		return this._root;
	}

	public float getDotsPerPoint() {
		return this._dotsPerPoint;
	}

	public List findPagePositionsByID(Pattern pattern) {
		return this._outputDevice.findPagePositionsByID(this.newLayoutContext(), pattern);
	}

	public PDFCreationListener getListener() {
		return this._listener;
	}

	public void setListener(PDFCreationListener listener) {
		this._listener = listener;
	}

	public PdfWriter getWriter() {
		return this._writer;
	}

	private static final class NullUserInterface implements UserInterface {
		private NullUserInterface() {
		}

		public boolean isHover(Element e) {
			return false;
		}

		public boolean isActive(Element e) {
			return false;
		}

		public boolean isFocus(Element e) {
			return false;
		}
	}
}
