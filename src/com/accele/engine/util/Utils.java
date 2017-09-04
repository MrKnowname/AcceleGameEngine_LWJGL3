package com.accele.engine.util;

import java.awt.Color;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import com.accele.engine.gfx.Tile;

public final class Utils {

	private Utils() {}
	
	@SafeVarargs
	public static <T> T[] genericArray(T ... elements) {
		return elements;
	}
	
	public static double getCurrentTime() {
		return (double) System.nanoTime() / (double) 1000000000L;
	}
	
	public static <T> T getElementFromSortedSet(SortedSet<T> set, int index) {
		int i = 0;
		for (T t : set) {
			if (i == index)
				return t;
			i++;
		}
		
		return null;
	}
	
	public static <T> IndexedSet<T> unmodifiableIndexedSet(IndexedSet<T> set) {
		return new ImmutableIndexedSet<T>(set);
	}
	
	private static class ImmutableIndexedSet<E> extends IndexedSet<E> {
		
		private static final long serialVersionUID = 4223175239202640439L;

		public ImmutableIndexedSet(IndexedSet<E> set) {
			super(set);
		}
		
		@Override
		public boolean add(E e) {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public boolean addAll(Collection<? extends E> c) {
			throw new UnsupportedOperationException();
		}
		
	}
	
	public static Vector3f toTileCoordinates(Vector3f pos) {
		return new Vector3f(pos.x * 2, pos.y * 2, pos.z * 2);
	}
	
	public static Matrix4f transformationMatrix(Vector3f pos, Vector3f rot, Vector3f scale) {
		return new Matrix4f().translate(pos).rotateX(rot.x).rotateY(rot.y).rotateZ(rot.z).scale(scale);
	}
	
	public static Vector2f entityToTileCoordinates(Vector2f position, int tileX, int tileY) {
		return new Vector2f((((position.x / 2) + 0.5f) - 5 / 2) + tileX, (((-position.y / 2) + 0.5f) - 5 / 2) + tileY);
	}
	
	public static Vector2f xy(Vector3f vec) {
		return new Vector2f(vec.x, vec.y);
	}
	
	public static Vector2f xy(Vector4f vec) {
		return new Vector2f(vec.x, vec.y);
	}
	
	public static Vector3f xyz(Vector4f vec) {
		return new Vector3f(vec.x, vec.y, vec.z);
	}
	
	public static Tile[] interpolateMapFromPixels(int[] pixels, int width, int height, Map<Integer, Tile> reference, Tile defaultTile) {
		Tile[] result = new Tile[pixels.length];
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				//int r = (pixels[x + y * width] >> 16) & 0xFF;
				//int g = (pixels[x + y * width] >> 8) & 0xFF;
				//int b = (pixels[x + y * width] >> 0) & 0xFF;
				
				//System.out.println("r: " + r + ", g: " + g + ", b: " + b);
				
				result[x + y * width] = reference.getOrDefault(pixels[x + y * width], defaultTile);
			}
		}
		
		return result;
	}
	
	public static int rgbaToHex(int r, int g, int b, int a) {
		return new Color(r, g, b, a).getRGB();
	}

	public static int getIndexForEntry(IndexedSet<String> attributes, String attributeName) {
		for (int i = 0; i < attributes.size(); i++)
			if (attributes.get(i).equals(attributeName))
				return i;
		return -1;
	}
	
	public static <K, V> HashMap<K, V> hashMap(Pair<K, V>[] entries) {
		HashMap<K, V> map = new HashMap<>();
		for (Pair<K, V> entry : entries)
			map.put(entry.getFirst(), entry.getSecond());
		return map;
	}
	
	public static <K, V> Map<K, V> immutableMap(Map<K, V> map) {
		Map<K, V> result = new HashMap<>();
		
		for (Map.Entry<K, V> entry : map.entrySet())
			result.put(entry.getKey(), entry.getValue());
		
		return Collections.unmodifiableMap(result);
	}
	
	public static int randomIntExcluding(int min, int max, int excludedValue, Random rand) {
		int result = Math.min(min + rand.nextInt(max + 1), max);
		if (result == excludedValue && result < max)
			result++;
		else if (result == excludedValue && result > min)
			result--;
		return result;
	}

	public static <T> int getIndexForArrayElement(T[] array, T element) {
		for (int i = 0; i < array.length; i++)
			if (array[i].equals(element))
				return i;
		return -1;
	}
	
	public static Integer[] intToIntObjectArray(int[] array) {
		Integer[] result = new Integer[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i];
		return result;
	}
	
	public static Short[] shortToShortObjectArray(short[] array) {
		Short[] result = new Short[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i];
		return result;
	}
	
	public static Long[] longToLongObjectArray(long[] array) {
		Long[] result = new Long[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i];
		return result;
	}
	
	public static Byte[] byteToByteObjectArray(byte[] array) {
		Byte[] result = new Byte[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i];
		return result;
	}
	
	public static Float[] floatToFloatObjectArray(float[] array) {
		Float[] result = new Float[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i];
		return result;
	}
	
	public static Double[] doubleToDoubleObjectArray(double[] array) {
		Double[] result = new Double[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i];
		return result;
	}
	
	public static Character[] charToCharacterObjectArray(char[] array) {
		Character[] result = new Character[array.length];
		for (int i = 0; i < result.length; i++)
			result[i] = array[i];
		return result;
	}
	
	public static <T> T getAtIndexOrElse(T[] array, int index, T defaultValue) {
		try {
			return array[index];
		} catch (Exception e) {
			return defaultValue;
		}
	}
	
	public static <T> T getOrElse(T obj, T defaultValue) {
		if (obj != null)
			return obj;
		else
			return defaultValue;
	}
	
}
