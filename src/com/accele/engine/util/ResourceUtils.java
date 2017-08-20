package com.accele.engine.util;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public final class ResourceUtils {

	private ResourceUtils() {}
	
	public static final ResourceLoader DEFAULT_TEXTURE_LOADER = (path) -> {
		BufferedImage bi = null;
		try {
			bi = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.err.println("Could not load image for given path \"" + path + "\".");
		}
		
		int width = bi.getWidth();
		int height = bi.getHeight();
		
		int[] rgbValues = bi.getRGB(0, 0, width, height, null, 0, width);
		ByteBuffer pixels = BufferUtils.createByteBuffer(width * height * 4);
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				int pixel = rgbValues[i * width + j];
				
				pixels.put((byte) ((pixel >> 16) & 0xFF));
				pixels.put((byte) ((pixel >> 8) & 0xFF));
				pixels.put((byte) (pixel & 0xFF));
				pixels.put((byte) ((pixel >> 24) & 0xFF));
			}
		}
		
		pixels.flip();
		
		return new Object[] { width, height, pixels };
	};
	
	public static final ResourceLoader DEFAULT_TEXTURE_LOADER2 = (path) -> {
		ByteBuffer image = null;
		int width = 0;
		int height = 0;
		
		try (MemoryStack stack = MemoryStack.stackPush()) {
			IntBuffer w = stack.mallocInt(1);
			IntBuffer h = stack.mallocInt(1);
			IntBuffer comp = stack.mallocInt(1);
			
			//STBImage.stbi_set_flip_vertically_on_load(true);
			image = STBImage.stbi_load(path, w, h, comp, 4);
			
			if (image == null) {
				System.err.println("Could not load image for given path \"" + path + "\".");
				return null;
			}
			
			width = w.get();
			height = h.get();
		}
		
		return new Object[] { width, height, image };
	};
	
	public static final ResourceLoader DEFAULT_SHADER_LOADER = (path) -> {
		StringBuilder result = new StringBuilder();
		
		try (BufferedReader br = new BufferedReader(new FileReader(new File(path)))) {
			String line = "";
			while ((line = br.readLine()) != null)
				result.append(line + "\n");
		} catch (IOException e) {
			System.err.println("Could not load shader for given path \"" + path + "\".");
		}
		
		return result.toString();
	};
	
	public static final ResourceLoader DEFAULT_MAP_LOADER = (path) -> {
		BufferedImage map = null;
		int width = 0;
		int height = 0;
		int[] pixels = null;
		
		try {
			map = ImageIO.read(new File(path));
			width = map.getWidth();
			height = map.getHeight();
			pixels = map.getRGB(0, 0, width, height, null, 0, width);
		} catch (Exception e) {
			System.err.println("Could not load map for given path \"" + path + "\".");
			return null;
		}
		
		return new Object[] { width, height, pixels };
	};
	
}
