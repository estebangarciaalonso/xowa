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
package gplx.xowa.setup.maints; import gplx.*; import gplx.xowa.*; import gplx.xowa.setup.*;
class Wmf_dump_list_parser {
	public Wmf_dump_itm[] Parse(byte[] src) {
		ListAdp itms = ListAdp_.new_();
		int pos = 0;
		while (true) {
			int a_pos = ByteAry_.FindFwd(src, Find_anchor, pos); if (a_pos == ByteAry_.NotFound) break;	// no more anchors found
			pos = a_pos + Find_anchor.length;
			try {
			Wmf_dump_itm itm = new Wmf_dump_itm();
			if (!Parse_href(itm, src, a_pos)) continue; 
			itms.Add(itm);
			itm.Status_time_(Parse_status_time(src, a_pos));
			itm.Status_msg_(Parse_status_msg(src, a_pos));
			} catch (Exception e) {Err_.Noop(e);}
		}
		return (Wmf_dump_itm[])itms.XtoAry(Wmf_dump_itm.class);
	}
	private boolean Parse_href(Wmf_dump_itm itm, byte[] src, int a_pos) {	// EX: http://dumps.wikimedia.org/enwiki/20130807
		int href_pos = ByteAry_.FindFwd(src, Find_href, a_pos); if (href_pos == ByteAry_.NotFound) return false;	// no <li>; something bad happened
		int href_bgn_pos = ByteAry_.FindFwd(src, Byte_ascii.Quote, href_pos + Find_href.length);
		int href_end_pos = ByteAry_.FindFwd(src, Byte_ascii.Quote, href_bgn_pos + 1); if (href_end_pos == ByteAry_.NotFound) return false;
		byte[] href_bry = ByteAry_.Mid(src, href_bgn_pos + 1, href_end_pos);
		int date_end = href_bry.length;
		int date_bgn = ByteAry_.FindBwd(href_bry, Byte_ascii.Slash); if (date_bgn == ByteAry_.NotFound) return false;
		byte[] date_bry = ByteAry_.Mid(href_bry, date_bgn + 1, date_end);
		DateAdp date = DateAdp_.parse_fmt(String_.new_ascii_(date_bry), "yyyyMMdd");
		itm.Dump_date_(date);
		int abrv_end = date_bgn;
		int abrv_bgn = ByteAry_.FindBwd(href_bry, Byte_ascii.Slash, abrv_end - 1); if (abrv_bgn == ByteAry_.NotFound) abrv_bgn = -1;	// "enwiki/20130708"
		byte[] abrv_bry = ByteAry_.Mid(href_bry, abrv_bgn + 1, abrv_end);
		itm.Wiki_abrv_(abrv_bry);
		return true;
	}
	private DateAdp Parse_status_time(byte[] src, int a_pos) {
		int li_pos = ByteAry_.FindBwd(src, Find_li, a_pos); if (li_pos == ByteAry_.NotFound) return null;
		int bgn = ByteAry_.FindFwd(src, Byte_ascii.Gt, li_pos + Find_li.length); if (bgn == ByteAry_.NotFound) return null;
		byte[] rv_bry = ByteAry_.Mid(src, bgn + 1, a_pos);
		return DateAdp_.parse_fmt(String_.Trim(String_.new_ascii_(rv_bry)), "yyyy-MM-dd hh:mm:ss");
	}
	private byte[] Parse_status_msg(byte[] src, int a_pos) {
		int span_pos = ByteAry_.FindFwd(src, Find_span_bgn, a_pos); if (span_pos == ByteAry_.NotFound) return null;
		int bgn = ByteAry_.FindFwd(src, Byte_ascii.Gt, span_pos + Find_span_bgn.length); if (bgn == ByteAry_.NotFound) return null;
		int end = ByteAry_.FindFwd(src, Find_span_end, bgn); if (end == ByteAry_.NotFound) return null;
		return ByteAry_.Mid(src, bgn + 1, end);
	}
	private static byte[]
		Find_anchor = ByteAry_.new_ascii_("<a")
	,	Find_href = ByteAry_.new_ascii_(" href=")
	, 	Find_li = ByteAry_.new_ascii_("<li")
	, 	Find_span_bgn = ByteAry_.new_ascii_("<span")
	, 	Find_span_end = ByteAry_.new_ascii_("</span>")
	;
}
class Wmf_dump_itm {
	public byte[] Wiki_abrv() {return wiki_abrv;} public void Wiki_abrv_(byte[] v) {this.wiki_abrv = v;} private byte[] wiki_abrv;
	public DateAdp Dump_date() {return dump_date;} public void Dump_date_(DateAdp v) {this.dump_date = v;} private DateAdp dump_date;
	public byte[] Status_msg() {return status_msg;}
	public void Status_msg_(byte[] v) {
		this.status_msg = v;
		if 		(ByteAry_.Eq(status_msg, Status_msg_dump_complete))
			status_completed = true;
		else if (ByteAry_.Eq(status_msg, Status_msg_dump_in_progress))
			status_completed = false;
		else
			status_completed = false;	// assume anything else is false; EX: Error
	} 	private byte[] status_msg;
	public DateAdp Status_time() {return status_time;} public void Status_time_(DateAdp v) {this.status_time = v;} private DateAdp status_time;
	public boolean Status_completed() {return status_completed;} private boolean status_completed;
	private static byte[] Status_msg_dump_complete = ByteAry_.new_ascii_("Dump complete"), Status_msg_dump_in_progress = ByteAry_.new_ascii_("Dump in progress");
}