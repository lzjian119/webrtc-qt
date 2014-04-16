package com.video.utils;

public class Tools {
	
	/**
	 * Byte[]���ݿ���
	 */
	public static void CopyByteArray(byte[] dst, byte[] src, int len) {
		System.arraycopy(src, 0, dst, 0, len);
	}
	
	/**
	 * Byte[]���ݿ���
	 */
	public static void CopyByteArray(byte[] dst, int dstPos, byte[] src, int srcPos, int len) {
		System.arraycopy(src, srcPos, dst, dstPos, len);
	}
	
	/**
	 * ����short��ֵ
	 */
	public static void setWordValue(byte[] buffer, int offset, short value) {
		buffer[offset] = (byte) (value & 0x00FF);
		buffer[offset + 1] = (byte) ((value & 0xFF00) >> 8);
	}

	/**
	 * ���short��ֵ
	 */
	public static short getWordValue(byte[] buffer, int offset) {
		short result = 0;
		result = (byte) (0xFF & buffer[offset + 1]);
		result = (short) ((result << 8) & 0xFF00);
		result += (short) (buffer[offset] & 0x00FF);
		return result;
	}
	
	/**
	 * ����int��ֵ
	 */
	public static void setIntValue(byte[] buffer, int offset, int value) {
		buffer[offset] = (byte) ((value & 0x000000FF));
		buffer[offset + 1] = (byte) ((value & 0x0000FF00) >> 8);
		buffer[offset + 2] = (byte) ((value & 0x00FF0000) >> 16);
		buffer[offset + 3] = (byte) ((value & 0xFF000000) >> 24);
	}
	
	/**
	 * ���int��ֵ
	 */
	public static int getIntValue(byte[] buffer, int offset) {
		int result = 0;
		result = (0xFF & buffer[offset]) + 
				 ((0xFF & buffer[offset + 1]) << 8) + 
				 ((0xFF & buffer[offset + 2]) << 16) + 
				 ((0xFF & buffer[offset + 3]) << 24);
		return result;
	}
}