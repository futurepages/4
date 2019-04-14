package org.futurepages.util;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.sun.media.jai.codec.ByteArraySeekableStream;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.SeekableStream;
import org.apache.commons.lang.NotImplementedException;

import javax.imageio.ImageIO;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.renderable.ParameterBlock;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;

/**
 * Utilidades para manipulação de JPEG - Utiliza o Leitor da biblioteca JAI
 * Ler arquivos que fogem da especificação padrão do JPEG
 */
public class ImageUtil {

	static {
		//necessário para que nunca se busque bibliotecas nativas do SO.
		System.setProperty("com.sun.media.jai.disableMediaLib", "true");
	}

	public static BufferedImage getBufferedImage(File file) throws IOException {
		return bufferedImgWithNoAlpha(imageFrom(file));
	}

	/*
	 * if the image is not opaque, we return the image with
	 * a white background under the image.
	 *
	 */
	public static BufferedImage bufferedImgWithNoAlpha(BufferedImage image) {
			BufferedImage outputImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = outputImage.createGraphics();
			if (image.getTransparency() != Transparency.OPAQUE) {
				graphics2D.setBackground(Color.WHITE);
				graphics2D.setComposite(AlphaComposite.SrcOver);
			}
			graphics2D.fill(new Rectangle2D.Double(0, 0, image.getWidth(), image.getHeight()));
			graphics2D.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
			image.getGraphics().dispose();
			image = outputImage;
		return image;
	}

	public static BufferedImage getBufferedImage(byte[] bytes) throws IOException {
		SeekableStream seekableStream = new ByteArraySeekableStream(bytes);
		ParameterBlock pb = new ParameterBlock();
		pb.add(seekableStream);
		return bufferedImgWithNoAlpha(JAI.create("stream", pb).getAsBufferedImage());
	}

	/**
	 * Retorna a largura em pixels da imagem.
	 * @param file
	 * @return
	 * @throws java.net.MalformedURLException
	 */
	public static int getWidth(File file) throws MalformedURLException {
		Image image = new ImageIcon(file.toURI().toURL()).getImage();
		image.flush();
		return image.getWidth(null);
	}

	/**
	 * Retorna a largura em pixels da imagem.
	 */
	public static int getHeight(File file) throws MalformedURLException {
		Image image = new ImageIcon(file.toURI().toURL()).getImage();
		image.flush();
		return image.getHeight(null);
	}

	public static int[] getWidthAndHeight(File file) throws MalformedURLException {
		Image image = new ImageIcon(file.toURI().toURL()).getImage();
		image.flush();
		int [] wh = new int[2];


		wh[0] = image.getWidth(null);
		wh[1] = image.getHeight(null);

		return wh;
	}

	/**
	 * Redimensiona arquivo (File) e retorna a imagem redimensionada (pathNewFile)
	 * File: Arquivo de entrada
	 * width, height: largura e altura da nova imagem
	 * pathNewFile: endereço real completo incluindo o nome do arquivo
	 */
	public static void resizeImage(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, true, true, null);
		image.flush();
		image = null;
		System.gc();
	}

	public static void resizeImage(File file, int width, int height, int quality, String pathNewFile, int[] subimage) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, true, true, subimage);
		image.flush();
		image = null;
		System.gc();
	}

	public static void resizeImagePriorHeight(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, false, true, null);
		image.flush();
		image = null;
		System.gc();
	}

	private static void resize(BufferedImage image, int thumbW, int thumbH, int quality, String pathNewFile, boolean priorWidth, boolean stretchWhenSmaller, int[] subimage) throws FileNotFoundException, IOException {
		// Calculos necessários para manter as propoçoes da imagem, conhecido como "aspect ratio"

		if (subimage != null) {
			image = image.getSubimage(subimage[0], subimage[1], subimage[2], subimage[3]);
		}

		double thumbRatio = (double) thumbW / (double) thumbH;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (priorWidth) {
			if (thumbRatio < imageRatio) {
				thumbH = (int) (thumbW / imageRatio);
			} else {
				thumbW = (int) (thumbH * imageRatio);
			}
		} else {
			if (thumbRatio < imageRatio) {
				thumbW = (int) (thumbH * imageRatio);
			} else {
				thumbH = (int) (thumbW / imageRatio);
			}
		}

		if (thumbW >= imageWidth || thumbH >= imageHeight) {
			//quando imagem é menor que o resultado final, faz um esticamento para crescer até o tamanho desejado.
			//quando imagem é menor que o resultado final, faz um resizer pobre
			if (stretchWhenSmaller) {
				poorResize(image, null, thumbW, thumbH, quality, pathNewFile);
			} else {
				poorResize(image, null, imageWidth, imageHeight, quality, pathNewFile);
			}
		} else {
			image = GraphicsUtilities.createThumbnail(image, thumbW, thumbH);

			createJPEG(image, quality, pathNewFile);
		}
	}

	public static void resizeImageByOneDimension(Color color, boolean byWidth, File file, int dimension, int quality, String pathNewFile, boolean stretchWhenSmaller) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);

		resizeByWidth(byWidth, color, image, dimension, quality, pathNewFile, stretchWhenSmaller);
		image.flush();
	}

	public static void resizeImageByOneDimension(Color colorSquare, boolean byWidth, byte[] bytesOfImageFile, int theDimension, int quality, String pathNewFile, boolean stretchWhenSmaller) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(bytesOfImageFile);
		resizeByWidth(byWidth, colorSquare, image, theDimension, quality, pathNewFile, stretchWhenSmaller);
		image.flush();
	}

	public static void buildImageFile(byte[] bytesOfImageFile, String pathNewFile) throws IOException {
		BufferedImage image = getBufferedImage(bytesOfImageFile);
		if(FileUtil.extensionFormat(pathNewFile).equals("png")){
			ImageIO.write(image, "png", new File(pathNewFile));
		}else{
			createJPEG(image, 100, pathNewFile);
		}
		image.flush();
	}

	public static void resizeCropping(File file,int thumbW, int thumbH, String pathNewFile, boolean stretchWhenSmaller) throws IOException {
		BufferedImage image;
		if(FileUtil.extensionFormat(file.getAbsolutePath()).equals("png") && FileUtil.extensionFormat(pathNewFile).equals("png")){
			image = bufferedCutInRatio(imageFrom(file), thumbW, thumbH);
		} else {
			image = bufferedCutInRatioWithNoAlpha(imageFrom(file), thumbW, thumbH);
		}

		resizeCropping(image, thumbW,thumbH, pathNewFile, stretchWhenSmaller);
	}

	public static BufferedImage imageFrom(File file) throws IOException {
		SeekableStream seekableStream = new FileSeekableStream(file);
		ParameterBlock pb = new ParameterBlock();
		pb.add(seekableStream);
		BufferedImage image;
		try {
			RenderedOp rop = JAI.create("stream", pb);
			image = rop.getAsBufferedImage();
			if(image.getType()>5){
					image = new JpegReader().readImage(file);
			}
		} catch (Exception ex) {
			try {
				image = new JpegReader().readImage(file);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
		return image;
	}

	//se pathNewFile é != null, então espera-se que a image já venha cortada.
	public static BufferedImage resizeCropping(BufferedImage image,int thumbW, int thumbH, String pathNewFile, boolean stretchWhenSmaller) throws IOException {
		if(pathNewFile==null){
			image = bufferedCutInRatioWithNoAlpha(image, thumbW, thumbH);
		}

		//RESIZING....
		if (thumbW >= image.getWidth() || thumbH >= image.getHeight()) {
			//quando imagem é menor que o resultado final, faz um esticamento para crescer até o tamanho desejado.
			//quando imagem é menor que o resultado final, faz um resizer pobre
			if (stretchWhenSmaller) {
				image = poorResize(image, null, thumbW, thumbH, 100, pathNewFile);
			} else {
				image = poorResize(image, null, image.getWidth(), image.getHeight(), 100, pathNewFile);
			}
		} else {
			image = poorResize(image, null, thumbW, thumbH, 100, pathNewFile);
		}

		if(pathNewFile!=null){
			if(FileUtil.extensionFormat(pathNewFile).equals("png")){
				ImageIO.write(image, "png", new File(pathNewFile));
			}else{
				createJPEG(image, 100, pathNewFile);
			}
			image.flush();
			image = null;
			System.gc();
		}
		return image;
	}

	//reduz imagem mantendo o aspect-ratio. As larguras e alturas passadas como parâmetro são os max possíveis de cada um.
	public static void reduceImage(File file,int thumbW, int thumbH, String pathNewFile, boolean stretchWhenSmaller) throws IOException {
		BufferedImage image;
		if(FileUtil.extensionFormat(file.getAbsolutePath()).equals("png") && FileUtil.extensionFormat(pathNewFile).equals("png")){
			image = imageFrom(file);
		} else {
			image = bufferedImgWithNoAlpha(imageFrom(file));
		}

		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);
		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (imageWidth>imageHeight) {
			thumbH = (int) (thumbW / imageRatio);
		} else {
			thumbW = (int) (thumbH * imageRatio);
		}

		//RESIZING....
		if (thumbW >= image.getWidth() || thumbH >= image.getHeight()) {
			//quando imagem é menor que o resultado final, faz um esticamento para crescer até o tamanho desejado.
			//quando imagem é menor que o resultado final, faz um resizer pobre
			if (stretchWhenSmaller) {
				image = poorResize(image, null, thumbW, thumbH, 100, pathNewFile);
			} else {
				image = poorResize(image, null, image.getWidth(), image.getHeight(), 100, pathNewFile);
			}
		} else {
			image = poorResize(image, null, thumbW, thumbH, 100, pathNewFile);
		}

		if(FileUtil.extensionFormat(pathNewFile).equals("png")){
			ImageIO.write(image, "png", new File(pathNewFile));
		}else{
			createJPEG(image, 100, pathNewFile);
		}

		image.flush();
		image = null;
		System.gc();
	}

	public static BufferedImage bufferedCutInRatio(BufferedImage image, int w, int h) {

		int oH = image.getHeight();
		int oW = image.getWidth();

		float r = h / (float) w;

		float oR = oH / (float) oW;
		int hN, wN, xN, yN;
		if (r < oR) {
			wN = oW;
			hN = Math.round(oW * r);
			yN = Math.round((oH - hN) / 2f);
			xN = 0;
		} else {
			hN = oH;
			wN = Math.round(oH / r);
			xN = Math.round((oW - wN) / 2f);
			yN = 0;
		}

		BufferedImage oldImage;
//		if (image.getTransparency() != Transparency.OPAQUE) {
//			BufferedImage outputImage = new BufferedImage(wN, hN, BufferedImage.TYPE_INT_RGB);
//			Graphics2D graphics2D = outputImage.createGraphics();
//			graphics2D.setBackground(Color.WHITE);
//			graphics2D.setComposite(AlphaComposite.SrcOver);
//			graphics2D.fill(new Rectangle2D.Double(0, 0, wN, hN));
//			graphics2D.drawImage(image.getSubimage(xN, yN, wN, hN), 0, 0, wN, hN, null);
//			oldImage = image;
//			image = outputImage;
//			graphics2D.dispose();
//		} else {
			oldImage = image;
			image = image.getSubimage(xN, yN, wN, hN);
//		}
		return image;
	}

	public static BufferedImage bufferedCutInRatioWithNoAlpha(BufferedImage image, int w, int h) {

		int oH = image.getHeight();
		int oW = image.getWidth();

		float r = h / (float) w;

		float oR = oH / (float) oW;
		int hN, wN, xN, yN;
		if (r < oR) {
			wN = oW;
			hN = Math.round(oW * r);
			yN = Math.round((oH - hN) / 2f);
			xN = 0;
		} else {
			hN = oH;
			wN = Math.round(oH / r);
			xN = Math.round((oW - wN) / 2f);
			yN = 0;
		}

		BufferedImage oldImage;
		if (image.getTransparency() != Transparency.OPAQUE) {
			BufferedImage outputImage = new BufferedImage(wN, hN, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphics2D = outputImage.createGraphics();
			graphics2D.setBackground(Color.WHITE);
			graphics2D.setComposite(AlphaComposite.SrcOver);
			graphics2D.fill(new Rectangle2D.Double(0, 0, wN, hN));
			graphics2D.drawImage(image.getSubimage(xN, yN, wN, hN), 0, 0, wN, hN, null);
			oldImage = image;
			image = outputImage;
			graphics2D.dispose();
		} else {
			oldImage = image;
			image = image.getSubimage(xN, yN, wN, hN);
		}
		oldImage.flush();
		oldImage = null;
		return image;
	}

	/**
	 * Redimensiona imagens (criar thubmnails) - prioriza a largura
	 */
//    A) se altura diferente da largura (imageWidth <> imageHeight):
	// 1) se largura original maior ou igual à largura final (imageWidth >= newWidth):
	//1.1) se largura maior que altura (imageWidth > imageHeight):
	//					#### completa com a cor na altura.
	//1.2) se altura maior que a largura (imageWidth < imageHeight):
	//				    #### redimensiona (diminui) para preencher a largura com a cor.
	//2) se a largura original menor que a largura final (imageWidth < newWidth):
	//2.1) se largura maior que altura (imageWidth > imageHeight):
	//		            #### completa com a cor na altura para formar.j
	//2.2) se altura maior que a largura:
	//		            #### redimensiona (aumenta) para preencher a largura com a cor até form um quadrado ou até que alcanse a largura desejada.
	//		            #### somente resize padrão (sem esticar para crescer)
//    B) se altura diferente de largura (imageWidth == imageHeight)
	//		            #### somente resize padrão (sem esticar para crescer)
	private static void resizeByWidth(boolean reallyByWidth, Color colorSquare, BufferedImage image, int theDimension, int quality, String pathNewFile, boolean stretchWhenSmaller) throws FileNotFoundException, IOException {
		// Calculos necessários para manter as propoçoes da imagem, conhecido como "aspect ratio"
		int oW = image.getWidth(null);
		int oH = image.getHeight(null);

		double imageRatio = 0;

		int thumbW = 0, thumbH = 0, oDim1 = 0, oDim2 = 0, dim1 = 0, dim2 = 0;

		if (reallyByWidth) {
			imageRatio = (double) oH / (double) oW;
			thumbW = theDimension;
			thumbH = (int) (thumbW * imageRatio);
			dim1 = thumbW;
			dim2 = thumbH;
			oDim1 = oW;
			oDim2 = oH;

		} else {
			imageRatio = (double) oW / (double) oH;
			thumbH = theDimension;
			thumbW = (int) (thumbH * imageRatio);
			dim1 = thumbH;
			dim2 = thumbW;
			oDim1 = oH;
			oDim2 = oW;
		}

		if (oW != oH) {
			if (colorSquare != null) {
				int pos1 = 0, pos2 = 0, posX = 0, posY = 0, canv1 = 0, canv2 = 0, canvW = 0, canvH = 0;
				if (oDim1 >= dim1) { //se largura original maior ou igual à largura final (imageWidth >= newWidth):
					if (oDim1 > oDim2) { //se largura maior que altura (imageWidth > imageHeight): //	completa com a cor na altura.
						pos1 = 0;
						pos2 = ((dim1-dim2)/2);
						canv1 = dim1;
						canv2 = dim1;
					} else { //se altura maior que a largura (imageWidth < imageHeight): redimensiona (diminui) para preencher a largura com a cor.
						pos1 =  (dim1 - ((dim1*dim1)/dim2) ) /2;
						pos2 = 0;
						canv1 = dim1;
						canv2 = dim1;
						int tempDim2  = dim1;
						dim1 = (dim1*dim1)/dim2; //depois por conta das sobrescritas do valor. necessário ficar aqui.
						dim2 = tempDim2;
					}
				} else { //se a largura original menor que a largura final (oW < oH)
					if (oDim1 > oDim2) {//se largura maior que altura (imageWidth > imageHeight): completa com a cor na altura para formar.
						pos1 = 0;
						pos2 = ((oDim1-oDim2)/2);
						canv1 = oDim1;
						canv2 = oDim1;
						dim1 = oDim1;
						dim2 = oDim2;
					} else { // oDim2 >= oDim1  -> se altura maior que a largura: redimensiona (aumenta) para preencher a largura com a cor até form um quadrado ou até que alcanse a largura desejada.
						int oDim2x = (oDim2>theDimension)? theDimension : oDim2;
						pos1 = ((oDim2x-oDim1)/2);
						pos2 = 0;
						dim1 = oDim1;
						dim2 = oDim2;
						canv1 = oDim2x;
						canv2 = oDim2;
					}
				}
				if(reallyByWidth){
					thumbW = dim1;
					thumbH = dim2;
					canvW = canv1;
					canvH = canv2;
					posX  = pos1;
					posY  = pos2;
				}else{
					thumbH = dim1;
					thumbW = dim2;
					canvH = canv1;
					canvW = canv2;
					posY  = pos1;
					posX  = pos2;
				}

				BufferedImage thumbImage = GraphicsUtilities.createCompatibleImage(canvW, canvH);

				Graphics2D graphics2D = thumbImage.createGraphics();
				graphics2D.setBackground(colorSquare);
				graphics2D.setColor(colorSquare);
				graphics2D.fill(new Rectangle2D.Double(0, 0, canvW, canvH));
				graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
				graphics2D.drawImage(image, posX, posY, thumbW, thumbH, null);
				image.getGraphics().dispose();
				image = thumbImage;
			} else { //colorSquare == null
				if(stretchWhenSmaller && (thumbW > oW || thumbH > oH)){
					poorResize(image, null, thumbW, thumbH, quality, pathNewFile);
					return;
				} else if(!(thumbW >= oW || thumbH >= oH)) {
					image = GraphicsUtilities.createThumbnail(image, thumbW, thumbH); //dont stretchWhenSmaller
				}
			}

			createJPEG(image, quality, pathNewFile);

		} else { //imageWidth == imageHeight
			resize(image, thumbW, thumbH, quality, pathNewFile, reallyByWidth, stretchWhenSmaller, null);
		}
	}

private static BufferedImage poorResize(Image image, Color colorSquare, int width, int height, int quality, String pathNewFile) throws FileNotFoundException, IOException {



		BufferedImage thumbImage;
		if(pathNewFile == null || FileUtil.extensionFormat(pathNewFile).equals("jpg")){
			image = bufferedImgWithNoAlpha((BufferedImage) image);
			thumbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		}else{
			thumbImage = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
		}


		Graphics2D graphics2D = thumbImage.createGraphics();

		graphics2D.setRenderingHint(
				RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		graphics2D.setRenderingHint(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics2D.setRenderingHint(
				RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics2D.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
				RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);

		if(colorSquare!=null){
			//TODO - posicionar no quadrado branco.
			throw new NotImplementedException("Não foi implementado ainda ColorSquare para poorResize");
		}else{
			graphics2D.drawImage(image, 0, 0, width, height, null);
			image.getGraphics().dispose();
		}
		return thumbImage;
	}

	public static void createJPEG(BufferedImage image, int quality, String pathNewFile) throws FileNotFoundException, IOException {
//		ImageIO.write(image, "jpg", new File(pathNewFile)); // TODO um dia pode ser necessário substituir tudo abaixo por este código.

		FileOutputStream fos = new FileOutputStream(pathNewFile);
		BufferedOutputStream out = new BufferedOutputStream(fos);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
		quality = Math.max(0, Math.min(quality, 100));
		param.setQuality((float) quality / 100.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(image);

		image.flush();
		out.flush();
		fos.flush();
		fos.close();
		out.close();
	}

	public static void createJPEG(BufferedImage image, int quality, OutputStream out) throws IOException {
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(image);
		quality = Math.max(0, Math.min(quality, 100));
		param.setQuality((float) quality / 100.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(image);
	}
}
