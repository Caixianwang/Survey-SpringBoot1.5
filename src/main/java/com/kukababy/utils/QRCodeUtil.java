/**
 * 
 */
package com.kukababy.utils;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * <b>描述:</b>: <blockquote>
 * 
 * <pre>
 * </pre>
 * 
 * </blockquote>
 * 
 * @author caixian_wang@sina.com
 * @date 2018年5月21日 下午8:39:44
 */
public class QRCodeUtil {
	private static final String CHARSET = "utf-8";
	private static final String FORMAT_NAME = "PNG";
	// 二维码尺寸
	private static final int QRCODE_SIZE = 300;
	// LOGO宽度
	//private static final int WIDTH = 60;
	// LOGO高度
	//private static final int HEIGHT = 60;

	public static BufferedImage createImage(String content, String imgUrl, boolean needCompress) throws Exception {
		return createImage(content, imgUrl, QRCODE_SIZE, needCompress);
	}

	/**
	 * 生成二维码的方法
	 * 
	 * @param content
	 *            目标URL
	 * @param imgPath
	 *            LOGO图片地址
	 * @param needCompress
	 *            是否压缩LOGO
	 * @return 二维码图片
	 * @throws WriterException
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	public static BufferedImage createImage(String content, String imgUrl, int qrcodeSize, boolean needCompress)
			throws WriterException, MalformedURLException, IOException {
		Hashtable hints = new Hashtable();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		hints.put(EncodeHintType.MARGIN, 1);
		BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, qrcodeSize, qrcodeSize, hints);
		int width = bitMatrix.getWidth();
		int height = bitMatrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
			}
		}
		if (imgUrl == null || "".equals(imgUrl)) {
			return image;
		}
		// 插入图片
		QRCodeUtil.insertImage(image, imgUrl,qrcodeSize, needCompress);
		return image;
	}

	/**
	 * 插入LOGO
	 * 
	 * @param source
	 *            二维码图片
	 * @param imgPath
	 *            LOGO图片地址
	 * @param needCompress
	 *            是否压缩
	 * @throws IOException
	 * @throws MalformedURLException
	 * @throws Exception
	 */
	private static void insertImage(BufferedImage source, String imgUrl, int qrcodeSize,boolean needCompress) throws MalformedURLException, IOException {

		int pos = imgUrl.indexOf("http");
		Image src = null;
		if (pos == 0) {
			src = ImageIO.read(new URL(imgUrl));
		} else {
			src = ImageIO.read(new File(imgUrl));
		}

		int width = src.getWidth(null);
		int height = src.getHeight(null);
		int baseWith = qrcodeSize/5 ;
		if (needCompress) { // 压缩LOGO
			if (width > baseWith) {
				width = baseWith;
			}
			if (height > baseWith) {
				height = baseWith;
			}
			Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = source.createGraphics();
		int x = (qrcodeSize - width) / 2;
		int y = (qrcodeSize - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.setStroke(new BasicStroke(3f));
		graph.draw(shape);
		graph.dispose();
	}

	public static void main(String[] args) throws Exception {

		// 生成二维码
		// String text = "https://www.baidu.com/";

		String logoPath = "D:/workspace_sts/kukaSurvey2/src/main/webapp/resource/img/logo.png";
//		String url="https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxf63021da44864cf1&redirect_uri=";
//		url += "http://www.kukababy.com/wx/wxPageAuthByCode";
//		url += "&response_type=code&scope=snsapi_userinfo&state=";
//		url += "1su2EGN4QR";
//		url += "&connect_redirect=1#wechat_redirect";
		
		String url = "http://www.kukababy.com/wxLoginRedirect";

		BufferedImage image = QRCodeUtil.createImage(url, logoPath,256, true);
		ImageIO.write(image, FORMAT_NAME, new File("d:/wxLogin.png"));
		// 验证图片是否含有二维码

	}
}
