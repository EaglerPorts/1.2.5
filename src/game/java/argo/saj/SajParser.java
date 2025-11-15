package argo.saj;

import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;

public final class SajParser {
	public void parse(Reader var1, JsonListener var2) throws InvalidSyntaxException, IOException {
		PositionTrackingPushbackReader var3 = new PositionTrackingPushbackReader(var1);
		char var4 = (char)var3.read();
		switch(var4) {
		case '[':
			var3.unread(var4);
			var2.startDocument();
			this.arrayString(var3, var2);
			break;
		case '{':
			var3.unread(var4);
			var2.startDocument();
			this.objectString(var3, var2);
			break;
		default:
			throw new InvalidSyntaxException("Expected either [ or { but got [" + var4 + "].", var3);
		}

		int var5 = this.readNextNonWhitespaceChar(var3);
		if(var5 != -1) {
			throw new InvalidSyntaxException("Got unexpected trailing character [" + (char)var5 + "].", var3);
		} else {
			var2.endDocument();
		}
	}

	private void arrayString(PositionTrackingPushbackReader var1, JsonListener var2) throws InvalidSyntaxException, IOException {
		char var3 = (char)this.readNextNonWhitespaceChar(var1);
		if(var3 != 91) {
			throw new InvalidSyntaxException("Expected object to start with [ but got [" + var3 + "].", var1);
		} else {
			var2.startArray();
			char var4 = (char)this.readNextNonWhitespaceChar(var1);
			var1.unread(var4);
			if(var4 != 93) {
				this.aJsonValue(var1, var2);
			}

			boolean var5 = false;

			while(!var5) {
				char var6 = (char)this.readNextNonWhitespaceChar(var1);
				switch(var6) {
				case ',':
					this.aJsonValue(var1, var2);
					break;
				case ']':
					var5 = true;
					break;
				default:
					throw new InvalidSyntaxException("Expected either , or ] but got [" + var6 + "].", var1);
				}
			}

			var2.endArray();
		}
	}

	private void objectString(PositionTrackingPushbackReader var1, JsonListener var2) throws InvalidSyntaxException, IOException {
		char var3 = (char)this.readNextNonWhitespaceChar(var1);
		if(var3 != 123) {
			throw new InvalidSyntaxException("Expected object to start with { but got [" + var3 + "].", var1);
		} else {
			var2.startObject();
			char var4 = (char)this.readNextNonWhitespaceChar(var1);
			var1.unread(var4);
			if(var4 != 125) {
				this.aFieldToken(var1, var2);
			}

			boolean var5 = false;

			while(!var5) {
				char var6 = (char)this.readNextNonWhitespaceChar(var1);
				switch(var6) {
				case ',':
					this.aFieldToken(var1, var2);
					break;
				case '}':
					var5 = true;
					break;
				default:
					throw new InvalidSyntaxException("Expected either , or } but got [" + var6 + "].", var1);
				}
			}

			var2.endObject();
		}
	}

	private void aFieldToken(PositionTrackingPushbackReader var1, JsonListener var2) throws InvalidSyntaxException, IOException {
		char var3 = (char)this.readNextNonWhitespaceChar(var1);
		if(34 != var3) {
			throw new InvalidSyntaxException("Expected object identifier to begin with [\"] but got [" + var3 + "].", var1);
		} else {
			var1.unread(var3);
			var2.startField(this.stringToken(var1));
			char var4 = (char)this.readNextNonWhitespaceChar(var1);
			if(var4 != 58) {
				throw new InvalidSyntaxException("Expected object identifier to be followed by : but got [" + var4 + "].", var1);
			} else {
				this.aJsonValue(var1, var2);
				var2.endField();
			}
		}
	}

	private void aJsonValue(PositionTrackingPushbackReader var1, JsonListener var2) throws InvalidSyntaxException, IOException {
		char var3 = (char)this.readNextNonWhitespaceChar(var1);
		switch(var3) {
		case '\"':
			var1.unread(var3);
			var2.stringValue(this.stringToken(var1));
			break;
		case '-':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			var1.unread(var3);
			var2.numberValue(this.numberToken(var1));
			break;
		case '[':
			var1.unread(var3);
			this.arrayString(var1, var2);
			break;
		case 'f':
			char[] var6 = new char[4];
			int var7 = var1.read(var6);
			if(var7 != 4 || var6[0] != 97 || var6[1] != 108 || var6[2] != 115 || var6[3] != 101) {
				var1.uncount(var6);
				throw new InvalidSyntaxException("Expected \'f\' to be followed by [[a, l, s, e]], but got [" + Arrays.toString(var6) + "].", var1);
			}

			var2.falseValue();
			break;
		case 'n':
			char[] var8 = new char[3];
			int var9 = var1.read(var8);
			if(var9 != 3 || var8[0] != 117 || var8[1] != 108 || var8[2] != 108) {
				var1.uncount(var8);
				throw new InvalidSyntaxException("Expected \'n\' to be followed by [[u, l, l]], but got [" + Arrays.toString(var8) + "].", var1);
			}

			var2.nullValue();
			break;
		case 't':
			char[] var4 = new char[3];
			int var5 = var1.read(var4);
			if(var5 != 3 || var4[0] != 114 || var4[1] != 117 || var4[2] != 101) {
				var1.uncount(var4);
				throw new InvalidSyntaxException("Expected \'t\' to be followed by [[r, u, e]], but got [" + Arrays.toString(var4) + "].", var1);
			}

			var2.trueValue();
			break;
		case '{':
			var1.unread(var3);
			this.objectString(var1, var2);
			break;
		default:
			throw new InvalidSyntaxException("Invalid character at start of value [" + var3 + "].", var1);
		}

	}

	private String numberToken(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		StringBuilder var2 = new StringBuilder();
		char var3 = (char)var1.read();
		if(45 == var3) {
			var2.append('-');
		} else {
			var1.unread(var3);
		}

		var2.append(this.nonNegativeNumberToken(var1));
		return var2.toString();
	}

	private String nonNegativeNumberToken(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		StringBuilder var2 = new StringBuilder();
		char var3 = (char)var1.read();
		if(48 == var3) {
			var2.append('0');
			var2.append(this.possibleFractionalComponent(var1));
			var2.append(this.possibleExponent(var1));
		} else {
			var1.unread(var3);
			var2.append(this.nonZeroDigitToken(var1));
			var2.append(this.digitString(var1));
			var2.append(this.possibleFractionalComponent(var1));
			var2.append(this.possibleExponent(var1));
		}

		return var2.toString();
	}

	private char nonZeroDigitToken(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		char var3 = (char)var1.read();
		switch(var3) {
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return var3;
		default:
			throw new InvalidSyntaxException("Expected a digit 1 - 9 but got [" + var3 + "].", var1);
		}
	}

	private char digitToken(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		char var3 = (char)var1.read();
		switch(var3) {
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9':
			return var3;
		default:
			throw new InvalidSyntaxException("Expected a digit 1 - 9 but got [" + var3 + "].", var1);
		}
	}

	private String digitString(PositionTrackingPushbackReader var1) throws IOException {
		StringBuilder var2 = new StringBuilder();
		boolean var3 = false;

		while(!var3) {
			char var4 = (char)var1.read();
			switch(var4) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				var2.append(var4);
				break;
			default:
				var3 = true;
				var1.unread(var4);
			}
		}

		return var2.toString();
	}

	private String possibleFractionalComponent(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		StringBuilder var2 = new StringBuilder();
		char var3 = (char)var1.read();
		if(var3 == 46) {
			var2.append('.');
			var2.append(this.digitToken(var1));
			var2.append(this.digitString(var1));
		} else {
			var1.unread(var3);
		}

		return var2.toString();
	}

	private String possibleExponent(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		StringBuilder var2 = new StringBuilder();
		char var3 = (char)var1.read();
		if(var3 != 46 && var3 != 69) {
			var1.unread(var3);
		} else {
			var2.append('E');
			var2.append(this.possibleSign(var1));
			var2.append(this.digitToken(var1));
			var2.append(this.digitString(var1));
		}

		return var2.toString();
	}

	private String possibleSign(PositionTrackingPushbackReader var1) throws IOException {
		StringBuilder var2 = new StringBuilder();
		char var3 = (char)var1.read();
		if(var3 != 43 && var3 != 45) {
			var1.unread(var3);
		} else {
			var2.append(var3);
		}

		return var2.toString();
	}

	private String stringToken(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		StringBuilder var2 = new StringBuilder();
		char var3 = (char)var1.read();
		if(34 != var3) {
			throw new InvalidSyntaxException("Expected [\"] but got [" + var3 + "].", var1);
		} else {
			boolean var4 = false;

			while(!var4) {
				char var5 = (char)var1.read();
				switch(var5) {
				case '\"':
					var4 = true;
					break;
				case '\\':
					char var6 = this.escapedStringChar(var1);
					var2.append(var6);
					break;
				default:
					var2.append(var5);
				}
			}

			return var2.toString();
		}
	}

	private char escapedStringChar(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		char var3 = (char)var1.read();
		char var2;
		switch(var3) {
		case '\"':
			var2 = 34;
			break;
		case '/':
			var2 = 47;
			break;
		case '\\':
			var2 = 92;
			break;
		case 'b':
			var2 = 8;
			break;
		case 'f':
			var2 = 12;
			break;
		case 'n':
			var2 = 10;
			break;
		case 'r':
			var2 = 13;
			break;
		case 't':
			var2 = 9;
			break;
		case 'u':
			var2 = (char)this.hexadecimalNumber(var1);
			break;
		default:
			throw new InvalidSyntaxException("Unrecognised escape character [" + var3 + "].", var1);
		}

		return var2;
	}

	private int hexadecimalNumber(PositionTrackingPushbackReader var1) throws InvalidSyntaxException, IOException {
		char[] var2 = new char[4];
		int var3 = var1.read(var2);
		if(var3 != 4) {
			throw new InvalidSyntaxException("Expected a 4 digit hexidecimal number but got only [" + var3 + "], namely [" + String.valueOf(var2, 0, var3) + "].", var1);
		} else {
			try {
				int var4 = Integer.parseInt(String.valueOf(var2), 16);
				return var4;
			} catch (NumberFormatException var6) {
				var1.uncount(var2);
				throw new InvalidSyntaxException("Unable to parse [" + String.valueOf(var2) + "] as a hexidecimal number.", var6, var1);
			}
		}
	}

	private int readNextNonWhitespaceChar(PositionTrackingPushbackReader var1) throws IOException {
		boolean var3 = false;

		int var2;
		do {
			var2 = var1.read();
			switch(var2) {
			case 9:
			case 10:
			case 13:
			case 32:
				break;
			default:
				var3 = true;
			}
		} while(!var3);

		return var2;
	}
}
