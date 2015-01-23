package org.futurepages.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.imageio.ImageIO;

/**
 * Utilidades para manipulação de JPEG
 */
public class JPEGUtil {
	
	public static BufferedImage getBufferedImage(File file) throws IOException {
		return ImageIO.read(file);
	}

	/**
	 * Retorna a largura em pixels da imagem.
	 * @param file
	 * @return
	 * @throws java.net.MalformedURLException
	 */
	public static int getWidth(File file) throws MalformedURLException {
		Image image = new ImageIcon(file.toURI().toURL()).getImage();
		return image.getWidth(null);
	}

	/**
	 * Retorna a largura em pixels da imagem.
	 */
	public static int getHeight(File file) throws MalformedURLException {
		Image image = new ImageIcon(file.toURI().toURL()).getImage();
		return image.getHeight(null);
	}

	/**
	 * Redimensiona arquivo (File) e retorna a imagem redimensionada (pathNewFile)
	 * File: Arquivo de entrada
	 * width, height: largura e altura da nova imagem
	 * pathNewFile: endereço real completo incluindo o nome do arquivo
	 */
	public static void resizeImage(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, true, null);
		image.flush();
	}

	public static void resizeImage(File file, int width, int height, int quality, String pathNewFile, int[] subimage) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, true, subimage);
		image.flush();
	}

	public static void resizeImagePriorHeight(File file, int width, int height, int quality, String pathNewFile) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		resize(image, width, height, quality, pathNewFile, false, null);
		image.flush();
	}


	private static void resize(BufferedImage image, int width, int height, int quality, String pathNewFile, boolean priorWidth, int[] subimage) throws FileNotFoundException, IOException {
		// Calculos necessários para manter as propoçoes da imagem, conhecido como "aspect ratio"

		if(subimage!=null){
			image = image.getSubimage(subimage[0], subimage[1], subimage[2], subimage[3]);
		}

		double thumbRatio = (double) width / (double) height;
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		double imageRatio = (double) imageWidth / (double) imageHeight;

		if (priorWidth) {
			if (thumbRatio < imageRatio) {
				height = (int) (width / imageRatio);
			} else {
				width = (int) (height * imageRatio);
			}
		} else {
			if (thumbRatio < imageRatio) {
				width = (int) (height * imageRatio);
			} else {
				height = (int) (width / imageRatio);
			}
		}

		if (width >= imageWidth || height >= imageHeight) {
			//quando imagem é menor que o resultado final, faz um resizer pobre
			poorResize(image, width, height, quality, pathNewFile);
		} else {
			image = GraphicsUtilities.createThumbnail(image, width, height);

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
	}

	public static void resizeImageByOneDimension(boolean byWidth, File file, int width, int quality, String pathNewFile, boolean poorWhenSmaller) throws MalformedURLException, FileNotFoundException, IOException {
		BufferedImage image = getBufferedImage(file);
		
		resizeByWidth(byWidth, image, width, quality, pathNewFile, poorWhenSmaller);
		image.flush();
	}

	/**
	 * Redimensiona imagens (criar thubmnails) - prioriza a largura
	 */
	private static void resizeByWidth(boolean reallyByWidth, BufferedImage image, int theDimension, int quality, String pathNewFile, boolean poorWhenSmaller) throws FileNotFoundException, IOException {
		// Calculos necessários para manter as propoçoes da imagem, conhecido como "aspect ratio"
		int imageWidth = image.getWidth(null);
		int imageHeight = image.getHeight(null);

		double imageRatio = 0;
		
		int width = 0;
		int height = 0;
		if(reallyByWidth){
			imageRatio = (double) imageHeight / (double) imageWidth ;
			width = theDimension;
			height = (int) (width * imageRatio);
		} else {
			imageRatio = (double) imageWidth / (double) imageHeight ;
			height = theDimension;
			width = (int) (height * imageRatio);
		}

		if (width >= imageWidth || height >= imageHeight) {
			//quando imagem é menor que o resultado final, faz um resizer pobre
			if(poorWhenSmaller){
				poorResize(image, width, height, quality, pathNewFile);
			}else{
				poorResize(image, imageWidth, imageHeight, quality, pathNewFile);
			}
		} else {
			image = GraphicsUtilities.createThumbnail(image, width, height);

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
	}

	//TODO - Refatorar no futuro. (leandro)
	private static void poorResize(Image image, int width, int height, int quality, String pathNewFile) throws FileNotFoundException, IOException {

		BufferedImage thumbImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

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

		graphics2D.drawImage(image, 0, 0, width, height, null);

		FileOutputStream fos = new FileOutputStream(pathNewFile);
		BufferedOutputStream out = new BufferedOutputStream(fos);

		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(thumbImage);
		quality = Math.max(0, Math.min(quality, 100));
		param.setQuality((float) quality / 100.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(thumbImage);
		thumbImage.flush();
		out.flush();
		fos.flush();
		fos.close();
		out.close();
	}
}