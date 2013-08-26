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
package gplx;
public class Gfo_url_parser {
	boolean pass = true;
	Gfo_url url;
	ListAdp segs = ListAdp_.new_(), args = ListAdp_.new_();
	Url_encoder encoder = Url_encoder.new_html_href_mw_().Itms_raw_same_many(Byte_ascii.Underline); Hash_adp_bry protocols = new Hash_adp_bry(false);
	public Gfo_url_parser() {
		Bry_val_itm.hash_add_ary_(protocols, Protocol__ary);
	}
	public Gfo_url_parser Protocols_add(String hook, byte tid) {
		byte[] hook_bry = ByteAry_.new_ascii_(hook);
		Object o = protocols.Get_by_bry(hook_bry);
		if (o != null)	// avoid adding dupes; can occur b/c Gfo_url_parser is reused
			protocols.Del(hook_bry);
		Bry_val_itm.hash_add_itm_(protocols, hook_bry, tid);
		return this;
	}
	public byte Relative_url_protocol() {return relative_url_protocol;} public Gfo_url_parser Relative_url_protocol_(byte v) {relative_url_protocol = v; return this;} private byte relative_url_protocol = Protocol_http_tid;
	public byte[] Relative_url_protocol_bry() {return Gfo_url_parser.Protocol__ary[relative_url_protocol];}
	public void Parse_site_fast(Gfo_url_site_data site_data, byte[] src, int bgn, int end) {
		int pos = bgn; boolean rel = false;
		if (pos + 1 < end && src[pos] == Byte_ascii.Slash && src[pos + 1] == Byte_ascii.Slash) {	// starts with "//"
			pos += 2;
			rel = true;
		}
		if (!rel) {	// search for ":"; NOTE: only search if not rel; i.e.: "//"
			int colon_pos = ByteAry_.FindFwd(src, Byte_ascii.Colon, pos, end);	// no colon found; EX: "//a.org/b"; "a.org/b"
			if (colon_pos != ByteAry_.NotFound)									// colon found; EX: "http://" or "https://"
				pos = colon_pos + Int_.Const_dlm_len;
			if (pos < end && src[pos] == Byte_ascii.Slash) {					// skip slash after colon
				pos += 1;
				if (pos < end && src[pos] == Byte_ascii.Slash)					// skip 2nd slash after colon
					pos += 1;
			}
		}
		int slash_pos = ByteAry_.FindFwd(src, Byte_ascii.Slash, pos, end);
		if (slash_pos == ByteAry_.NotFound)									// no terminating slash; EX: http://a.org
			slash_pos = end;
		slash_pos = ByteAry_.Trim_end_pos(src, slash_pos);
		site_data.Atrs_set(rel, pos, slash_pos);
	}
	public boolean Parse(Gfo_url url, byte[] src, int bgn, int end) {
		this.url = url;
		url.Ini_(src);
		pass = true;
		segs.Clear();
		args.Clear();
		int colon_pos = ByteAry_.FindFwd(src, Byte_ascii.Colon, bgn, end);
		Object protocol_obj = colon_pos == ByteAry_.NotFound ? null : protocols.Get_by_mid(src, bgn, colon_pos + 1);	// +1 to include colon
		if (protocol_obj != null) {
			Bry_val_itm protocol_stub = (Bry_val_itm)protocol_obj;
			url.Protocol_bry_(protocol_stub.Bry());
			url.Protocol_tid_(protocol_stub.Tid());
			Parse_site(src, protocol_stub.Bry().length, end);				
		}
		else {
			int pos = bgn;
			boolean loop = true;
			while (loop) {
				if (pos == end) {
					encoder.Decode(src, bgn, end, tmp_bfr, false);
					url.Site_(tmp_bfr.XtoAryAndClear());
					url.Err_(Gfo_url.Err_protocol_missing); pass = false;
					break;
				}
				byte b = src[pos];
				switch (b) {
					case Byte_ascii.Colon:
//							Segs_add(src, bgn, end);
						Parse_segs(src, bgn, end);
						loop = false;
						url.Err_(Gfo_url.Err_protocol_missing); pass = false;
						loop = false;
						break;
					case Byte_ascii.Question:
						Segs_add(src, bgn, pos);
						Parse_args(src, pos + 1, end);
						loop = false;
						url.Err_(Gfo_url.Err_protocol_missing); pass = false;
						break;
					case Byte_ascii.Hash:
						Segs_add(src, bgn, pos);
						Parse_anchor(src, pos + 1, end);
						slash_prv = pos;
						loop = false;
						url.Err_(Gfo_url.Err_protocol_missing); pass = false;
						break;
					case Byte_ascii.Slash:
						if (pos == 0 && pos + 1 < end && src[pos + 1] == Byte_ascii.Slash) {	// starts with "//"
							encoder.Decode(src, bgn, pos, tmp_bfr, false);
							url.Site_(tmp_bfr.XtoAryAndClear());
							url.Protocol_is_relative_(true);
							url.Protocol_tid_(relative_url_protocol);
							byte[] protocol_bry = Protocol__ary[relative_url_protocol];
							url.Protocol_bry_(protocol_bry);
							Parse_site(src, 2, end);	// 2=//
							loop = false;
						}
						else {																	// has "/"
							encoder.Decode(src, bgn, pos, tmp_bfr, false);
							url.Site_(tmp_bfr.XtoAryAndClear());
							Parse_segs(src, pos + 1, end);
							loop = false;
							url.Err_(Gfo_url.Err_protocol_missing); pass = false;
						}
						break;
					default:
						break;
				}			
				++pos;
			}
		}
		return pass;
	}
	void Parse_site(byte[] src, int bgn, int end) {
		int dot_count = 0, dot_pos_0 = -1, dot_pos_1 = -1;
		boolean loop = true;
		byte b = Byte_ascii.Nil;
		while (loop) {
			if (bgn == end) {
				url.Err_(Gfo_url.Err_site_missing);
				break;
			}
			b = src[bgn];
			if (b == Byte_ascii.Slash)
				++bgn;
			else
				break;
		}
		int pos = bgn;
		while (loop) {
			switch (b) {
				case Byte_ascii.Dot:
					switch (dot_count) {
						case 0: dot_pos_0 = pos; break;
						case 1: dot_pos_1 = pos; break;
					}
					++dot_count;
					break;
				case Byte_ascii.Slash:
					Site_set(src, bgn, pos, dot_count, dot_pos_0, dot_pos_1);
					Parse_segs(src, pos + 1, end);
					loop = false;
					break;
				default:
					break;
			}
			if (!loop) break;
			++pos;
			if (pos >= end) {	// NOTE: >= needed b/c sometimes "xowa-cmd:", 5 passed in
				Site_set(src, bgn, end, dot_count, dot_pos_0, dot_pos_1);
				break;
			}
			b = src[pos];
		}		
	}
	int slash_prv;
	void Parse_segs(byte[] src, int bgn, int end) {
		if (bgn == end) return;
		int pos = bgn;
		slash_prv = bgn;
		boolean loop = true;
		while (loop) {
			if (pos == end) {
				if (slash_prv < pos) Segs_add(src, slash_prv, end);
				break;
			}
			byte b = src[pos];
			switch (b) {
				case Byte_ascii.Question:
					Segs_add(src, slash_prv, pos);
					Parse_args(src, pos + 1, end);
					loop = false;
					break;
				case Byte_ascii.Hash:
					Segs_add(src, slash_prv, pos);
					Parse_anchor(src, pos + 1, end);
					slash_prv = pos;
					loop = false;
					break;
				case Byte_ascii.Slash:
					Segs_add(src, slash_prv, pos);
					break;
				default:
					break;
			}			
			++pos;
		}
		url.Segs_((byte[][])segs.XtoAry(byte[].class));
	}
	void Parse_anchor(byte[] src, int bgn, int end) {		
		if (bgn == end) return;
		int pos = bgn;
		boolean loop = true;
		while (loop) {
			if (pos == end) {
				Anchor_set(src, bgn, pos);
				break;
			}
			byte b = src[pos];
			switch (b) {
				case Byte_ascii.Question:
					Anchor_set(src, bgn, pos);
					Parse_args(src, pos + 1, end);
					loop = false;
					break;
				//case Byte_ascii.Slash:	// NOTE: do not handle slash (by trying to parse segs); will cause anchor to fail; EX:A/b#c/d
//					case Byte_ascii.Slash:
//						Anchor_set(src, bgn, pos);
//						Parse_segs(src, pos + 1, end);
//						break;
				default:
					break;
			}			
			++pos;
		}
	}
	void Parse_args(byte[] src, int bgn, int end) {
		if (bgn == end) return;
		int pos = bgn;
		boolean loop = true;
		int key_bgn = pos, key_end = pos, val_bgn = pos;
		while (loop) {
			if (pos == end) {
				Args_add(src, key_bgn, key_end, val_bgn, pos);
				break;
			}
			byte b = src[pos];
			switch (b) {
				case Byte_ascii.Amp:
					Args_add(src, key_bgn, key_end, val_bgn, pos);
					key_bgn = pos + 1;
					break;
				case Byte_ascii.Eq:
					key_end = pos;
					val_bgn = pos + 1;	// +1 to set after eq
					break;
				default:
					break;
			}			
			++pos;
		}
		url.Args_((Gfo_url_arg[])args.XtoAry(Gfo_url_arg.class));
	}
	void Site_set(byte[] src, int bgn, int end, int dot_count, int dot_pos_0, int dot_pos_1) {
		encoder.Decode(src, bgn, end, tmp_bfr, false);
		url.Site_(tmp_bfr.XtoAryAndClear());
		switch (dot_count) {
			default:
			case 2:
				encoder.Decode(src, bgn, dot_pos_0, tmp_bfr, false);
				url.Site_sub_(tmp_bfr.XtoAryAndClear());
				encoder.Decode(src, dot_pos_0 + 1, dot_pos_1, tmp_bfr, false);
				url.Site_name_(tmp_bfr.XtoAryAndClear());
				encoder.Decode(src, dot_pos_1 + 1, end, tmp_bfr, false);
				url.Site_domain_(tmp_bfr.XtoAryAndClear());
				break;
			case 1:
				encoder.Decode(src, bgn, dot_pos_0, tmp_bfr, false);
				url.Site_name_(tmp_bfr.XtoAryAndClear());
				encoder.Decode(src, dot_pos_0 + 1, end, tmp_bfr, false);
				url.Site_domain_(tmp_bfr.XtoAryAndClear());
				break;
		}
	}
	void Segs_add(byte[] src, int bgn, int end) {
		encoder.Decode(src, bgn, end, tmp_bfr, false);
		byte[] seg = tmp_bfr.XtoAryAndClear();
		if (url.Page() != null)
			segs.Add(url.Page());
		url.Page_(seg);
		slash_prv = end + 1;	// +1 to position after / 
	}
	void Anchor_set(byte[] src, int bgn, int end) {
		encoder.Decode(src, bgn, end, tmp_bfr, false);
		url.Anchor_(tmp_bfr.XtoAryAndClear());
	}
	void Args_add(byte[] src, int key_bgn, int key_end, int val_bgn, int val_end) {
		encoder.Decode(src, key_bgn, key_end, tmp_bfr, false);
		byte[] key = tmp_bfr.XtoAryAndClear();
		encoder.Decode(src, val_bgn, val_end, tmp_bfr, false);
		byte[] val = tmp_bfr.XtoAryAndClear();
		Gfo_url_arg arg = new Gfo_url_arg(key, val);
		args.Add(arg);
	}
	private static final ByteAryBfr tmp_bfr = ByteAryBfr.reset_(500);
	public static final byte Protocol_null_tid = 0, Protocol_http_tid = 1, Protocol_https_tid = 2, Protocol_ftp_tid = 3, Protocol_irc_tid = 4, Protocol_gopher_tid = 5, Protocol_telnet_tid = 6, Protocol_nntp_tid = 7, Protocol_worldwind_tid = 8, Protocol_svn_tid = 9, Protocol_git_tid = 10, Protocol_mms_tid = 11, Protocol_mailto_tid = 12, Protocol_news_tid = 13, Protocol_file_tid = 14;
	private static final String Protocol_null_str = "null:", Protocol_http_str = "http:", Protocol_https_str = "https:", Protocol_ftp_str = "ftp:", Protocol_irc_str = "irc:", Protocol_gopher_str = "gopher:", Protocol_telnet_str = "telnet:", Protocol_nntp_str = "nntp:", Protocol_worldwind_str = "worldwind:", Protocol_svn_str = "svn:", Protocol_git_str = "git:", Protocol_mms_str = "mms:", Protocol_mailto_str = "mailto:", Protocol_news_str = "news:", Protocol_file_str = "file:";
	public static final byte[] Protocol_null_bry = ByteAry_.new_ascii_(Protocol_null_str), Protocol_http_bry = ByteAry_.new_ascii_(Protocol_http_str), Protocol_https_bry = ByteAry_.new_ascii_(Protocol_https_str), Protocol_ftp_bry = ByteAry_.new_ascii_(Protocol_ftp_str), Protocol_irc_bry = ByteAry_.new_ascii_(Protocol_irc_str), Protocol_gopher_bry = ByteAry_.new_ascii_(Protocol_gopher_str), Protocol_telnet_bry = ByteAry_.new_ascii_(Protocol_telnet_str), Protocol_nntp_bry = ByteAry_.new_ascii_(Protocol_nntp_str), Protocol_worldwind_bry = ByteAry_.new_ascii_(Protocol_worldwind_str), Protocol_svn_bry = ByteAry_.new_ascii_(Protocol_svn_str), Protocol_git_bry = ByteAry_.new_ascii_(Protocol_git_str), Protocol_mms_bry = ByteAry_.new_ascii_(Protocol_mms_str), Protocol_mailto_bry = ByteAry_.new_ascii_(Protocol_mailto_str), Protocol_news_bry = ByteAry_.new_ascii_(Protocol_news_str), Protocol_file_bry = ByteAry_.new_ascii_(Protocol_file_str);
	private static final byte[][] Protocol__ary = new byte[][] {Protocol_null_bry, Protocol_http_bry, Protocol_https_bry, Protocol_ftp_bry, Protocol_irc_bry, Protocol_gopher_bry, Protocol_telnet_bry, Protocol_nntp_bry, Protocol_worldwind_bry, Protocol_svn_bry, Protocol_git_bry, Protocol_mms_bry, Protocol_mailto_bry, Protocol_news_bry, Protocol_file_bry};
	public static final byte[] Bry_double_slash = new byte[] {Byte_ascii.Slash, Byte_ascii.Slash};
	private static final int Protocol__len = Protocol__ary.length;
	public static byte[] bry_by_id_(byte id) {return Protocol__ary[id];}
	public static byte parse_(byte[] raw) {
		for (byte i = 0; i < Protocol__len; i++) {
			byte[] protocol = Protocol__ary[i];
			if (ByteAry_.Eq(raw, protocol)) return i;
		}
		throw Err_mgr._.parse_("Gfo_url_parser.protocol", "");	
	}
}
class Bry_val_itm {
	public Bry_val_itm(byte tid, byte[] bry) {this.tid = tid; this.bry = bry;}
	public byte Tid() {return tid;} private byte tid;
	public byte[] Bry() {return bry;} private byte[] bry;

	public static void hash_add_ary_(Hash_adp_bry hash, byte[][] ary) {
		byte len = (byte)ary.length;
		for (byte i = 0; i < len; i++)
			hash_add_itm_(hash, ary[i], i);
	}
	public static void hash_add_itm_(Hash_adp_bry hash, byte[] itm, byte tid) {hash.Add(itm, new Bry_val_itm(tid, itm));}
}
