package com.mirhoseini.utils.encrypt;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Locale;

public class Encryptor {

	public static String decrypt(String Data, String key) throws Exception {

		Data = Data.toUpperCase(Locale.US).trim();
		key = key.trim();

		long[] formattedKey = formatKey(key);// uint
		int x = 0;
		long[] tempData = new long[2]; // uint
		byte[] dataBytes = new byte[Data.length() / 2];

		for (int i = 0; i < Data.length() / 16; i++) {
			tempData[0] = ConvertHexStringToUint(Data.substring(i * 16,
					i * 16 + 8));
			tempData[1] = ConvertHexStringToUint(Data.substring(i * 16 + 8,
					i * 16 + 8 + 8));
			decode(tempData, formattedKey);

			ByteArrayOutputStream baos;
			DataOutputStream dos;

			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);

			dos.writeLong(tempData[0]);
			dos.close();
			for (int j = 3; j >= 0; j--) {
				dataBytes[x] = baos.toByteArray()[j + 4];
				x++;
			}

			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);

			dos.writeLong(tempData[1]);
			dos.close();
			for (int j = 3; j >= 0; j--) {
				dataBytes[x] = baos.toByteArray()[j + 4];
				x++;
			}
		}

		String decipheredString = new String(dataBytes, "UTF-8");

		return decipheredString;

	}

	public static String encrypt(String data, String key) throws Exception {
		if (data.length() % 8 != 0) {
			for (int i = (8 - data.length() % 8); i > 0; i--)
				data += (char) 0;
		}

		byte[] dataBytes = new byte[data.length()];

		// dataBytes.AddRange(System.Text.ASCIIEncoding.ASCII.GetBytes(Data));
		dataBytes = getBytes(data);

		String cipher = "";
		long[] tempData = new long[2];// uint[] tempData = new uint[2];
		byte[] fourBytes = new byte[4];
		for (int i = 0; i < data.length(); i += 8) {
			for (int k = 0; k < 4; k++)
				fourBytes[k] = dataBytes[i + k];

			tempData[0] = toUInt32(fourBytes);

			for (int k = 0; k < 4; k++)
				fourBytes[k] = dataBytes[i + 4 + k];

			tempData[1] = toUInt32(fourBytes);

			long[] formattedKey = formatKey(key);
			code(tempData, formattedKey);

			cipher += ConvertUintToHexString(tempData[0]);
			cipher += ConvertUintToHexString(tempData[1]);

		}
		return cipher;
	}

	public static long ConvertHexStringToUint(String input) throws Exception { // uint

		long[] byteArray = new long[4];
		if (input.length() != 8)
			throw new Exception("Input hexadecimal number must be 8 in length.");

		for (int i = 0; i < 8; i += 2) {
			// String sinput=input.substring(i, i+2);
			// if()
			// byteArray[i / 2] = input.substring(i, i+2).getBytes(); // TODO:
			byteArray[i / 2] = 0;
			int k = 1;
			for (int j = 0; j < 2; j++) {
				char c = input.substring(i, i + 2).toCharArray()[j];
				if (c >= '0' && c <= '9') {
					byteArray[i / 2] += ((c - '0') * Math.pow(16, k));
				} else {
					switch (c) {
					case 'A':
						byteArray[i / 2] += 10 * Math.pow(16, k);
						break;
					case 'B':
						byteArray[i / 2] += 11 * Math.pow(16, k);
						break;
					case 'C':
						byteArray[i / 2] += 12 * Math.pow(16, k);
						break;
					case 'D':
						byteArray[i / 2] += 13 * Math.pow(16, k);
						break;
					case 'E':
						byteArray[i / 2] += 14 * Math.pow(16, k);
						break;
					case 'F':
						byteArray[i / 2] += 15 * Math.pow(16, k);
						break;
					}
				}
				k--;
			}
		}

		return toUInt32(byteArray);

		// uint result = Convert.ToUInt32(input);
		// return result;

	}

	public static String ConvertUintToHexString(long input) throws IOException {
		String hex = "";
		byte[] byteArray = new byte[4];
		byteArray = getBytes(input);

		for (byte b : byteArray) {
			hex += (String.format("%02X", b));
		}

		return hex;
	}

	private static void decode(long[] v, long[] k) { // uint
		long y = v[0];// uint
		long z = v[1]; // uint
		// uint sum=0xC6EF3720;
		final long delta = Long.parseLong("2654435769");// 0x9E3779B9;// uint
														// //TODO:
		// chech here!
		int n = 32; // uint
		long sum = Long.parseLong("3337565984");// delta * n;// uint //TODO:
		final long a = Long.parseLong("4294967295"); // IMPORTANT: to convert to
														// uint, value of (1111
														// 1111 1111 1111)
		final long three = Long.parseLong("3");
		// check here!
		while (n-- > 0) {
			z -= (((y << 4 ^ y >> 5) + y) & a)
					^ ((sum + k[(int) ((sum >> 11) & three)]) & a);
			z &= a;
			sum -= delta;
			y -= ((((z << 4 ^ z >> 5) & a) + z) & a)
					^ ((sum + k[(int) (sum & three)]) & a);
			y &= a;
		}

		v[0] = y;
		v[1] = z;

	}

	private static void code(long[] v, long[] k) {
		long y = v[0];
		long z = v[1];
		long sum = 0; // Long.parseLong("3337565984");// uint sum = 0;
		final long a = Long.parseLong("4294967295"); // IMPORTANT: to convert to
		// uint, value of (1111
		// 1111 1111 1111)
		final long delta = Long.parseLong("2654435769");// 0x9E3779B9;
		int n = 32;

		final long three = Long.parseLong("3");

		while (n-- > 0) {
			y += (((z << 4 ^ z >> 5) + z) & a)
					^ ((sum + k[(int) (sum & three)]) & a);
			y &= a;

			sum += delta;
			sum &= a;

			z += (((y << 4 ^ y >> 5) + y) & a)
					^ ((sum + k[(int) (sum >> 11 & three)]) & a);
			z &= a;
		}

		v[0] = y;
		v[1] = z;
	}

	private static long[] formatKey(String key) throws Exception { // uint
		if (key.length() <= 0 || key.length() > 16)
			throw new Exception(
					"Key must be between 1 and 16 characters in length.");

		// padding the String to ensure the key is 16 chars in length

		key = padRight(key, 16, (char) 0).substring(0, 16);

		long[] formattedKey = new long[4];// uint[] formattedKey = new uint[4];
		int j = 0;
		for (int i = 0; i < key.length(); i += 4) {
			formattedKey[j] = ConvertStringToUint(key.substring(i, i + 4));
			j++;
		}

		return formattedKey;

	}

	public static long ConvertStringToUint(String input) throws Exception{// uint
		if (input.length() != 4)
			throw new Exception(
					"String length must be 4 in order to be converted!");

		long[] byteArray = new long[4];
		char[] inputArray = new char[4];
		long output;// uint

		inputArray = input.toCharArray();
		// convert to Bytes
		for (int i = 0; i < 4; i++)
			byteArray[i] = inputArray[i];// Convert.ToByte(inputArray[i]);

		// convert to uint32
		output = toUInt32(byteArray); // BitConverter.ToUInt32(byteArray,
		// 0); TODO: check here!

		return output;

		// uint result = Convert.ToUInt32(input);
		// return result;
	}

	private static String padRight(String s, int i, char c) {

		if (s.length() < i)
			for (int j = s.length(); j < i; j++)
				s += c;
		return s;
	}

	private static long toUInt32(long[] b) {
		long result = 0;
		for (int i = 0; i < b.length; i++)
			result += b[i] * Math.pow(256, i);
		return result;
	}

	private static long toUInt32(byte[] b) {
		long result = 0;
		for (int i = 0; i < b.length; i++)
			result += b[i] * Math.pow(256, i);
		return result;
	}

	private static byte[] getBytes(String Data) throws IOException {
		byte[] dataBytes;
		ByteArrayOutputStream baos;
		DataOutputStream dos;

		baos = new ByteArrayOutputStream();
		dos = new DataOutputStream(baos);

		dos.writeBytes(Data);
		dos.close();

		dataBytes = baos.toByteArray();
		return dataBytes;
	}

	private static byte[] getBytes(long input) throws IOException {
		byte[] dataBytes = new byte[4];
		ByteArrayOutputStream baos;
		DataOutputStream dos;

		baos = new ByteArrayOutputStream();
		dos = new DataOutputStream(baos);

		dos.writeLong(input);
		dos.close();

		int x = 0;
		for (int j = 3; j >= 0; j--) {
			dataBytes[x] = baos.toByteArray()[j + 4];
			x++;
		}
		return dataBytes;
	}
}
