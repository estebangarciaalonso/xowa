/*
XOWA: the extensible offline wiki application
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
package gplx.json; import gplx.*;
public interface Json_itm {
	byte Tid();
	int Src_bgn();
	int Src_end();
	Object Data();
	byte[] Data_bry();
	void Print_as_json(ByteAryBfr bfr, int depth);
	boolean Data_eq(byte[] comp);
}
class Json_itm_null extends Json_itm_base {
	Json_itm_null() {this.Ctor(-1, -1);}
	@Override public byte Tid() {return Json_itm_.Tid_null;}
	@Override public Object Data() {return null;}
	@Override public void Print_as_json(ByteAryBfr bfr, int depth) {bfr.Add(Bry_null);}
	@Override public byte[] Data_bry() {return Bry_null;}
	private static final byte[] Bry_null = ByteAry_.new_ascii_("null");
	public static Json_itm_null Null = new Json_itm_null();
}
class Json_itm_bool extends Json_itm_base {
	public Json_itm_bool(boolean data) {this.data = data; this.Ctor(-1, -1);} private boolean data;
	@Override public byte Tid() {return Json_itm_.Tid_bool;}
	@Override public Object Data() {return data;}
	@Override public byte[] Data_bry() {return data ? Json_itm_.Const_true : Json_itm_.Const_false;}
	@Override public void Print_as_json(ByteAryBfr bfr, int depth) {bfr.Add(data ? Json_itm_.Const_true: Json_itm_.Const_false);}
	public static Json_itm_bool Bool_n = new Json_itm_bool(false), Bool_y = new Json_itm_bool(true);
}
class Json_itm_decimal extends Json_itm_base {
	public Json_itm_decimal(Json_doc doc, int src_bgn, int src_end) {this.Ctor(src_bgn, src_end); this.doc = doc;} Json_doc doc;
	@Override public byte Tid() {return Json_itm_.Tid_decimal;}
	@Override public Object Data() {
		if (data == null)
			data = DecimalAdp_.parse_(String_.new_ascii_(this.Data_bry()));
		return data;
	}	DecimalAdp data;
	@Override public byte[] Data_bry() {
		if (data_bry == null) data_bry = ByteAry_.Mid(doc.Src(), this.Src_bgn(), this.Src_end());
		return data_bry;
	}	byte[] data_bry;
	@Override public void Print_as_json(ByteAryBfr bfr, int depth) {bfr.Add_mid(doc.Src(), this.Src_bgn(), this.Src_end());}
}
class Json_itm_str extends Json_itm_base {
	public Json_itm_str(Json_doc doc, int src_bgn, int src_end, boolean exact) {this.Ctor(src_bgn + 1, src_end - 1); this.doc = doc; this.exact = exact;} private boolean exact; Json_doc doc;
	@Override public byte Tid() {return Json_itm_.Tid_string;}
	@Override public void Print_as_json(ByteAryBfr bfr, int depth) {
		bfr.Add_byte(Byte_ascii.Quote);
		gplx.html.Html_util.Escape_html_to_bfr(bfr, doc.Src(), this.Src_bgn(), this.Src_end(), true, true, true, true);
		bfr.Add_byte(Byte_ascii.Quote);
	}
	@Override public Object Data() {
		if (data_str == null) {
			if (data_bry == null)
				data_bry = Data_make_bry();
			data_str = String_.new_utf8_(data_bry);
		}
		return data_str;
	}	String data_str;
	@Override public byte[] Data_bry() {if (data_bry == null) data_bry = Data_make_bry(); return data_bry;}
	@Override public boolean Data_eq(byte[] comp) {
		if (exact) return ByteAry_.Eq(comp, doc.Src(), this.Src_bgn(), this.Src_end());
		if (data_bry == null) data_bry = Data_make_bry();
		return ByteAry_.Match(data_bry, comp);
	}	byte[] data_bry = null;
	byte[] Data_make_bry() {
		byte[] src = doc.Src(); int bgn = this.Src_bgn(), end = this.Src_end();
		if (exact) return ByteAry_.Mid(src, bgn, end);
		ByteAryBfr bfr = doc.Bfr();
		byte[] utf8_bry = doc.Str_utf8_bry();
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Backslash:
					b = src[++i];
					switch (b) {
						default:
							bfr.Add_byte(b);	break;	// \?		" \ / b f n r t
						case Byte_ascii.Ltr_u:
							int utf8_val = gplx.texts.HexDecUtl.parse_or_(src, i + 1, i + 5, -1);
							int len = gplx.intl.Utf8_.EncodeChar(utf8_val, utf8_bry, 0);
							bfr.Add_mid(utf8_bry, 0, len);
							i += 4;
							break;	// \uFFFF	4 hex-dec
					}
					break;
				default:
					bfr.Add_byte(b);
					break;
			}		
		}
		return bfr.XtoAryAndClear();
	}
}
