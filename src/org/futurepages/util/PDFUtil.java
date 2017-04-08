package org.futurepages.util;

import org.futurepages.core.config.Apps;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.pdmodel.encryption.AccessPermission;
import org.pdfbox.pdmodel.encryption.StandardDecryptionMaterial;
import org.pdfbox.util.PDFText2HTML;
import org.pdfbox.util.PDFTextStripper;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

public class PDFUtil {

    public static void html2pdf(String input, OutputStream out) throws Exception {
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        html2pdf(stream, out);
        stream.close();
    }

    public static void html2pdf(String input, OutputStream out, String pathFontDir) throws Exception {
        ByteArrayInputStream stream = new ByteArrayInputStream(input.getBytes());
        html2pdf(stream, out, pathFontDir);
        stream.close();
    }

    public static void html2pdf(InputStream input, OutputStream out) throws Exception {
       html2pdf(input, out, Apps.get("WEB_REAL_PATH")+"/"+ Apps.get("RESOURCE_PATH")+"/fonts/");
    }

    public static void html2pdf(InputStream input, OutputStream out, String pathFontDir) throws Exception {
        Tidy tidy = new Tidy();
        Document doc = tidy.parseDOM(input, null);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(doc, null);

        addFonts(renderer, pathFontDir);

        renderer.layout();
        renderer.createPDF(out);
    }

    public static void addFonts(ITextRenderer renderer, String pathFontDir) throws Exception {
        File fontDir = new File(pathFontDir);
        File[] fontFiles = fontDir.listFiles();
		if (fontFiles != null)
			for (File fontFile : fontFiles) {
				renderer.getFontResolver().addFont(fontFile.getAbsolutePath(), true);
			}
	}

    /**
     * Extrai o texto de um arquivo pdf em formato String.
     * @param filePath endereço absoluto ou url do arquivo.
     * @return texto limpo do arquivo.
     */
    public static String pdf2Str(String filePath) throws Exception {
        boolean toConsole = false;
        boolean toHTML = false;
        boolean sort = false;
        String password = "";
        String encoding = null;
                        //"ISO-8859-1";
                        //"ISO-8859-6"; //arabic
                        //"US-ASCII";
                        //"UTF-8";
                        //"UTF-16";
                        //"UTF-16BE";
                        //"UTF-16LE";

        int startPage = 1;
        int endPage = Integer.MAX_VALUE;

        PDDocument document = null;
            try {
                //basically try to load it from a url first and if the URL
                //is not recognized then try to load it from the file system.
                URL url = new URL(filePath);
                document = PDDocument.load(url);
                String fileName = url.getFile();
            } catch (MalformedURLException e) {
                document = PDDocument.load(filePath);
            }

            if (document.isEncrypted()) {
                StandardDecryptionMaterial sdm = new StandardDecryptionMaterial(password);
                document.openProtection(sdm);
                AccessPermission ap = document.getCurrentAccessPermission();

                if (!ap.canExtractContent()) {
                    throw new IOException("O Arquivo PDF é criptografado. Não é possível extrair o texto.");
                }
            }

            PDFTextStripper stripper = null;
            if (toHTML) {
                stripper = new PDFText2HTML();
            } else {
                stripper = new PDFTextStripper();
            }
            stripper.setSortByPosition(sort);
            stripper.setStartPage(startPage);
            stripper.setEndPage(endPage);
            String extractedText = stripper.getText(document);
            
            if (document != null) {
                document.close();
            }
            return extractedText;
        }
}