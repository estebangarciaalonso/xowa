/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package gplx.intl; import gplx.*;
public class Utf8_ {
	public static byte[] Mid(byte[] src, int bgn) {
		int len = CharLen(src[bgn]);
		return ByteAry_.Mid(src, bgn, bgn + len);
	}
	public static int CharLen(byte b) {	// NOTE: changed charLen to match w:UTF-8; DATE:2013-11-27
		int i = b & 0xff;
		switch (i) {
			case   0: case   1: case   2: case   3: case   4: case   5: case   6: case   7: case   8: case   9: case  10: case  11: case  12: case  13: case  14: case  15: 
			case  16: case  17: case  18: case  19: case  20: case  21: case  22: case  23: case  24: case  25: case  26: case  27: case  28: case  29: case  30: case  31: 
			case  32: case  33: case  34: case  35: case  36: case  37: case  38: case  39: case  40: case  41: case  42: case  43: case  44: case  45: case  46: case  47: 
			case  48: case  49: case  50: case  51: case  52: case  53: case  54: case  55: case  56: case  57: case  58: case  59: case  60: case  61: case  62: case  63: 
			case  64: case  65: case  66: case  67: case  68: case  69: case  70: case  71: case  72: case  73: case  74: case  75: case  76: case  77: case  78: case  79: 
			case  80: case  81: case  82: case  83: case  84: case  85: case  86: case  87: case  88: case  89: case  90: case  91: case  92: case  93: case  94: case  95: 
			case  96: case  97: case  98: case  99: case 100: case 101: case 102: case 103: case 104: case 105: case 106: case 107: case 108: case 109: case 110: case 111: 
			case 112: case 113: case 114: case 115: case 116: case 117: case 118: case 119: case 120: case 121: case 122: case 123: case 124: case 125: case 126: case 127:
			case 128: case 129: case 130: case 131: case 132: case 133: case 134: case 135: case 136: case 137: case 138: case 139: case 140: case 141: case 142: case 143: 
			case 144: case 145: case 146: case 147: case 148: case 149: case 150: case 151: case 152: case 153: case 154: case 155: case 156: case 157: case 158: case 159: 
			case 160: case 161: case 162: case 163: case 164: case 165: case 166: case 167: case 168: case 169: case 170: case 171: case 172: case 173: case 174: case 175: 
			case 176: case 177: case 178: case 179: case 180: case 181: case 182: case 183: case 184: case 185: case 186: case 187: case 188: case 189: case 190: case 191: 
				return 1;
			case 192: case 193: case 194: case 195: case 196: case 197: case 198: case 199: case 200: case 201: case 202: case 203: case 204: case 205: case 206: case 207: 
			case 208: case 209: case 210: case 211: case 212: case 213: case 214: case 215: case 216: case 217: case 218: case 219: case 220: case 221: case 222: case 223: 
				return 2;
			case 224: case 225: case 226: case 227: case 228: case 229: case 230: case 231: case 232: case 233: case 234: case 235: case 236: case 237: case 238: case 239: 
				return 3;
			case 240: case 241: case 242: case 243: case 244: case 245: case 246: case 247:
				return 4;
			case 248: case 249: case 250: case 251:
				return 5;
			case 252: case 253:
				return 6;
			case 254: case 255:
			default:
				return 6;
		}
	}
	public static byte[] Increment_char_last(byte[] bry) {
		int bry_len = bry.length; if (bry_len == 0) return bry;
		int cur_pos = bry_len - 1;
		while (true) {
			int new_pos = Char_bgn(bry, cur_pos);
			int b_len = (cur_pos - new_pos) + 1;
			int nxt_codepoint = Codepoint_max;
			if (b_len == 1) {	// ASCII char; just increment by 1
				nxt_codepoint = Codepoint_next(bry[new_pos]);
				if (nxt_codepoint < 128) {
					bry = ByteAry_.Copy(bry);
					bry[new_pos] = (byte)nxt_codepoint;
					return bry;
				}
			}
			int cur_codepoint = DecodeChar(bry, new_pos);
			nxt_codepoint = Codepoint_next(cur_codepoint);
			if (nxt_codepoint != Codepoint_max) {
				byte[] nxt_codepoint_bry = EncodeCharAsAry(nxt_codepoint);
				bry = ByteAry_.Add(ByteAry_.Mid(bry, 0, new_pos), nxt_codepoint_bry);
				return bry;
			}
			cur_pos = new_pos - 1;
			if (cur_pos < 0) return null;
		}
	}
	public static int Char_bgn(byte[] bry, int pos) {
		int end = pos - 4; if (end < 0) end = 0;
		for (int i = pos; i >= end; i--) {
			byte b = bry[i];
			int b_len = CharLen(b);
			if (b_len > 1) return i;	// multi-byte char; return pos of first char
		}
		return pos;	// no mult-byte char found; return pos
	}
	public static int Codepoint_next(int cur) {
		while (cur++ < Codepoint_max) {
			if (cur == Codepoint_surrogate_bgn) cur = Codepoint_surrogate_end + 1;
			if (!Codepoint_valid(cur)) continue;
			return cur;
		}
		return Codepoint_max;
	}
	private static boolean Codepoint_valid(int v) {
				return Character.isDefined(v);
			}
	public static final int 
	  Codepoint_max = 0x10FFFF //see http://unicode.org/glossary/
	, Codepoint_surrogate_bgn = 0xD800
	, Codepoint_surrogate_end = 0xDFFF
	;
	public static int DecodeChar(byte[] ary, int pos) {
		byte b0 = ary[pos];
		if 		((b0 & 0x80) == 0) {
			return  b0;			
		}
		else if ((b0 & 0xE0) == 0xC0) {
			return  ( b0           & 0x1f) <<  6
				| 	( ary[pos + 1] & 0x3f)
				;			
		}
		else if ((b0 & 0xF0) == 0xE0) {
			return  ( b0           & 0x0f) << 12
				| 	((ary[pos + 1] & 0x3f) <<  6)
				| 	( ary[pos + 2] & 0x3f)
				;			
		}
		else if ((b0 & 0xF8) == 0xF0) {
			return  ( b0           & 0x07) << 18
				| 	((ary[pos + 1] & 0x3f) << 12)
				| 	((ary[pos + 2] & 0x3f) <<  6)
				| 	( ary[pos + 3] & 0x3f)
				;			
		}
		else if ((b0 & 0xFC) == 0xF8) {
			return  ( b0           & 0x03) << 24
				| 	((ary[pos + 1] & 0x3f) << 18)
				| 	((ary[pos + 2] & 0x3f) << 12)
				| 	((ary[pos + 3] & 0x3f) <<  6)
				| 	( ary[pos + 4] & 0x3f)
				;			
		}
		else if ((b0 & 0xFC) == 0xFC) {
			return  ( b0           & 0x03) << 30
				| 	((ary[pos + 1] & 0x3f) << 24)
				| 	((ary[pos + 2] & 0x3f) << 18)
				| 	((ary[pos + 3] & 0x3f) << 12)
				| 	((ary[pos + 4] & 0x3f) <<  6)
				| 	( ary[pos + 5] & 0x3f)
			;			
		}
		else {
			return b0 & 0xFF;
		}
	}
	public static byte[] Encode_as_bry_by_hex(String raw) {return Encode_as_bry_by_hex(ByteAry_.new_ascii_(raw));}
	public static byte[] Encode_as_bry_by_hex(byte[] raw) {
		if (raw == null) return null;
		int int_val = gplx.texts.HexDecUtl.parse_or_(raw, Int_.MinValue);
		return int_val == Int_.MinValue ? null : EncodeCharAsAry(int_val);
	}
	public static byte[] EncodeCharAsAry(int charAsInt) {
		int rv_len = Len(charAsInt);
		byte[] rv = new byte[rv_len];
		EncodeChar(charAsInt, rv, 0);
		return rv;
	}
	public static int EncodeChar(int charAsInt, byte[] src, int pos) {
//			Tfds.Write(charAsInt, 1 << 11, 1 << 16);
		if (charAsInt < 0x80) {
			src[pos] 	= (byte)charAsInt;
			return 1;
		}
		else if (charAsInt < (1 << 11)) {
			src[pos] 	= (byte)(0xC0 | (charAsInt >> 6));
			src[++pos] 	= (byte)(0x80 | (charAsInt & 0x3F));
			return 2;
		}	
		else if (charAsInt < (1 << 16)) {
			src[pos] 	= (byte)(0xE0 | (charAsInt >> 12));
			src[++pos] 	= (byte)(0x80 | (charAsInt >>  6) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt        & 0x3F));
			return 3;
		}	
		else if (charAsInt < (1 << 21)) {
			src[pos] 	= (byte)(0xF0 | (charAsInt >> 18));
			src[++pos] 	= (byte)(0x80 | (charAsInt >> 12) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt >>  6) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt        & 0x3F));
			return 4;
		}
		else if (charAsInt < (1 << 26)) {
			src[pos] 	= (byte)(0xF8 | (charAsInt >> 24));
			src[++pos] 	= (byte)(0x80 | (charAsInt >> 18) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt >> 12) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt >>  6) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt        & 0x3F));
			return 5;
		}	
		else if (charAsInt < (1 << 31)) {
			src[pos] 	= (byte)(0xFC | (charAsInt >> 30));
			src[++pos] 	= (byte)(0x80 | (charAsInt >> 24) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt >> 18) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt >> 12) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt >>  6) & 0x3F);
			src[++pos] 	= (byte)(0x80 | (charAsInt        & 0x3F));
			return 6;
		}	
		else if (charAsInt == 0) {
			src[pos] 	= (byte)0xC0;
			src[++pos] 	= (byte)0x80;
			return -1;
		}
		return -1;
	}
	static int Len(int charAsInt) {
		if		(charAsInt < 0x80)				return 1;
		else if (charAsInt < (1 << 11))			return 2;
		else if (charAsInt < (1 << 16))			return 3;
		else if (charAsInt < (1 << 21))			return 4;
		else if (charAsInt < (1 << 26))			return 5;
		else if (charAsInt < (Int_.MaxValue))	return 6;
		else if (charAsInt == 0)				return -1;
		return -1;
	}
}
