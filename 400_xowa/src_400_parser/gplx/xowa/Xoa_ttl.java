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
package gplx.xowa; import gplx.*;
public class Xoa_ttl {	// EX.WP: http://en.wikipedia.org/wiki/Help:Link; REF.MW: Ttl.php|secureAndSplit;
	public Xow_ns Ns() {return ns;} private Xow_ns ns;
	public boolean ForceLiteralLink() {return forceLiteralLink;} private boolean forceLiteralLink;
	// NOTE: in procs below, all -1 are used to skip previous delimiters; they will only occur for end_pos arguments
	public byte[] Raw() {return raw;} private byte[] raw = ByteAry_.Empty;
	public byte[] Wik_txt()  {return wik_bgn == -1 ? ByteAry_.Empty : ByteAry_.Mid(full_txt, wik_bgn, ns_bgn == -1 ? page_bgn - 1 : ns_bgn - 1);}
	public Xow_xwiki_itm Wik_itm() {return wik_itm;} private Xow_xwiki_itm wik_itm;
	public byte[] Full_txt() {
		int bgn = wik_bgn == -1  ? 0 : ns_bgn == -1 ? page_bgn : ns_bgn;
		int end = full_txt.length;
		if		(anch_bgn != -1) end = anch_bgn - 1;
		return ByteAry_.Mid(full_txt, bgn, end);
	}
	public byte[] Full_txt_raw()	{return full_txt;} private byte[] full_txt = ByteAry_.Empty;
	public byte[] Page_txt()		{return ByteAry_.Mid(full_txt, page_bgn, anch_bgn == -1 ? full_txt.length : anch_bgn - 1);}
	public byte[] Page_db() {
		byte[] rv = this.Page_txt();
		ByteAry_.Replace_reuse(rv, Byte_ascii.Space, Byte_ascii.Underline);
		return rv;
	}
	public String Page_db_as_str() {return String_.new_utf8_(Page_db());}
	public int Leaf_bgn() {return leaf_bgn;}
	public byte[] Base_txt() {return leaf_bgn == -1	? Page_txt() : ByteAry_.Mid(full_txt, page_bgn, leaf_bgn - 1);}
	public byte[] Leaf_txt() {return leaf_bgn == -1	? Page_txt() : ByteAry_.Mid(full_txt, leaf_bgn, anch_bgn == -1 ? full_txt.length : anch_bgn - 1);}
	public int Wik_bgn() {return wik_bgn;}
	public int Anch_bgn() {return anch_bgn;}	// NOTE: anch_bgn is not correct when page has trailing ws; EX: [[A #b]] should have anch_bgn of 3 (1st char after #), but instead it is 2
	public byte[] Anch_txt() {return anch_bgn == -1	? ByteAry_.Empty : ByteAry_.Mid(full_txt, anch_bgn, full_txt.length);}
	public byte[] Talk_txt() {return ns.Id_talk()		? Full_txt() : ByteAry_.Add(tors_txt, Page_txt());} 
	public byte[] Subj_txt() {return ns.Id_subj()		? Full_txt() : ByteAry_.Add(tors_txt, Page_txt());} 
	public byte[] Full_url() {return Xoa_url_encoder._.Encode(full_txt);}
	public byte[] Page_url() {return Xoa_url_encoder._.Encode(this.Page_txt());}
	public byte[] Leaf_url() {return Xoa_url_encoder._.Encode(this.Leaf_txt());}
	public byte[] Base_url() {return Xoa_url_encoder._.Encode(this.Base_txt());}
	public byte[] Root_txt() {return root_bgn == -1	? Page_txt() : ByteAry_.Mid(full_txt, page_bgn, root_bgn - 1);}
	public byte[] Rest_txt() {return root_bgn == -1	? Page_txt() : ByteAry_.Mid(full_txt, root_bgn, anch_bgn == -1 ? full_txt.length : anch_bgn - 1);}
	public byte[] Talk_url() {return Xoa_url_encoder._.Encode(this.Talk_txt());}
	public byte[] Subj_url() {return Xoa_url_encoder._.Encode(this.Subj_txt());}
	public int Qarg_bgn() {return qarg_bgn;} private int qarg_bgn = -1;
	public byte[] Qarg_txt() {return ByteAry_.Mid(full_txt, this.Qarg_bgn(), full_txt.length);}
	public byte[] Base_txt_wo_qarg() {
		int bgn = page_bgn;
		int end = full_txt.length;
		if		(leaf_bgn != -1) end = leaf_bgn - 1;
		else if (qarg_bgn != -1) end = qarg_bgn - 1;
		return ByteAry_.Mid(full_txt, bgn, end);
	}
	public byte[] Leaf_txt_wo_qarg() {
		int bgn = leaf_bgn == -1 ? 0 : leaf_bgn;
		int end = full_txt.length;
		if		(anch_bgn != -1) end = anch_bgn - 1;
		else if (qarg_bgn != -1) end = qarg_bgn - 1;
		return ByteAry_.Mid(full_txt, bgn, end);
	}
	public byte[] Full_txt_wo_qarg() {
		int bgn = wik_bgn == -1  ? 0 : ns_bgn == -1 ? page_bgn : ns_bgn;
		int end = full_txt.length;
		if		(anch_bgn != -1) end = anch_bgn - 1;
		else if (qarg_bgn != -1) end = qarg_bgn - 1;
		return ByteAry_.Mid(full_txt, bgn, end);
	}
	public byte[] Page_txt_wo_qargs() {	// assume that no Special page has non-ascii characters
		int full_txt_len = full_txt.length;
		int ques_pos = ByteAry_.FindBwd(full_txt, Byte_ascii.Question, full_txt_len - 1, page_bgn);
		return ByteAry_.Mid(full_txt, page_bgn, ques_pos == ByteAry_.NotFound ? full_txt_len : ques_pos);
	}
	public static Xoa_ttl parse_(Xow_wiki wiki, int ns_id, byte[] ttl) {
		Xow_ns ns = wiki.Ns_mgr().Get_by_id(ns_id);
		byte[] raw = ByteAry_.Add(ns.Name_db_w_colon(), ttl);
		return new_(wiki, wiki.App().Msg_log(), raw, raw, 0, raw.length);
	}
	public static Xoa_ttl parse_(Xow_wiki wiki, byte[] raw) {return new_(wiki, wiki.App().Msg_log(), raw, raw, 0, raw.length);}
	public static Xoa_ttl new_(Xow_wiki wiki, Gfo_msg_log msg_log, byte[] raw, byte[] src, int bgn, int end) {
		Xoa_ttl rv = new Xoa_ttl();
		boolean pass = rv.Parse(wiki, msg_log, raw, src, bgn, end);
		return pass ? rv : null;
	}	Xoa_ttl() {} static ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255);
	private static final Url_encoder anchor_encoder = Url_encoder.new_html_id_(); static final ByteAryBfr anchor_encoder_bfr = ByteAryBfr.reset_(32);
	boolean Parse(Xow_wiki wiki, Gfo_msg_log msg_log, byte[] raw, byte[] src, int bgn, int end) {
		/* This proc will
		- identify all parts: Wiki, Namespace, Base/Leaf, Anchor; it will also identify Subject/Talk ns 
		- trim whitespace around part delimiters; EX: "Help : Test" --> "Help:Test"; note that it will trim only if the part is real; EX: "Helpx : Test" is unchanged
		- replace multiple whitespaces with 1; EX: "Many     ws" --> "Many ws"
		- capitalize the first letter of the page title 
		note: a byte[] is needed b/c proc does collapsing and casing
		FUTURE:
		- "/", "a/" (should be page); "#" (not a page)
		- Talk:Help:a disallowed; Category talk:Help:a allowed
		- forbid extended ws: '/[ _\xA0\x{1680}\x{180E}\x{2000}-\x{200A}\x{2028}\x{2029}\x{202F}\x{205F}\x{3000}]+/u'
		- remove invalid characters $rxTc
		- forbid ./ /.
		- forbid ~~~
		- handle ip address urls for User and User talk
		*/
		tmp_bfr.Clear();
		if (end - bgn == 0) {msg_log.Add_itm_none(Xop_ttl_log.Len_0, src, bgn, bgn); return false;}
		this.raw = raw;
		Xow_ns_mgr nsMgr = wiki.Ns_mgr(); ns = nsMgr.Ns_main();
		boolean add_ws = false, ltr_bgn_reset = false;
		int ltr_bgn = -1, txt_bb_len = 0, colon_count = 0; tmp_bfr.Clear();
		ByteTrieMgr_slim amp_trie = wiki.App().AmpTrie();
		//ByteTrieMgr_fast ttlTrie = wiki.App().TtlTrie(); 
		byte[] b_ary = null;
		int cur = bgn;
		int match_pos = -1;
		while (cur != end) {
			byte b = src[cur];
			switch (b) {
				case Byte_ascii.Colon:
					if (cur == bgn) {	// initial colon; flag; note that "  :" is not handled; note that colon_count is not incremented
						forceLiteralLink = true;
						++cur;//cur = ttlTrie.Match_pos();
						continue;	// do not add to bfr
					}
					else {
						if (ltr_bgn == -1) {// no ltrs seen; treat as literal; occurs for ::fr:wikt:test and fr::Help:test
							++colon_count;
							break;
						}
						boolean part_found = false;
						if (colon_count == 0) {// 1st colon; 
							Object o = nsMgr.Trie_match_exact(tmp_bfr.Bry(), ltr_bgn, txt_bb_len);
							if (o == null) {	// not ns; try alias
								wik_itm = wiki.Xwiki_mgr().Get_by_mid(tmp_bfr.Bry(), ltr_bgn, txt_bb_len); // check if wiki; note: wiki is not possible for other colons
								if (wik_itm != null) {
									wik_bgn = 0;			// wik_bgn can only start at 0
									part_found = true;
									anch_bgn = -1;			// NOTE: do not allow anchors to begin before wiki_itm; breaks Full_txt for [[:#batch:Main Page]]; DATE:20130102
								}
							}
							else {
								ns = (Xow_ns)o;
								byte[] ns_name = ns.Name_txt();
								int ns_name_len = ns_name.length;
								int tmp_bfr_end = tmp_bfr.Bry_len();
								if (!ByteAry_.Eq(ns_name, tmp_bfr.Bry(), ltr_bgn, tmp_bfr_end) && ns_name_len == tmp_bfr_end - ltr_bgn) {	// if (a) ns_name != bfr_txt (b) both are same length; note that (b) should not happen, but want to safeguard against mismatched arrays
									ByteAry_.Set(tmp_bfr.Bry(), ltr_bgn, tmp_bfr_end, ns_name);
								}
								ns_bgn = ltr_bgn;
								part_found = true;
							}
						}
						if (part_found) {
							page_bgn = txt_bb_len + 1;	// anticipate page_bgn;
							add_ws = false;				// if there was an add_ws, ignore; EX: "Category :" should ignore space
							ltr_bgn_reset = true;		// ltr_bgn_reset
						}
						colon_count++;		// increment colon count
						break;
					}
				case Byte_ascii.Hash: anch_bgn = (txt_bb_len) + 1; break; // flag last anch_bgn
				case Byte_ascii.Slash:
					if (root_bgn == -1)
						root_bgn = (txt_bb_len) + 1;
					leaf_bgn = (txt_bb_len) + 1;
					break;	// flag last leaf_bgn
				case Byte_ascii.NewLine:	// NOTE: for now, treat nl just like space; not sure if it should accept "a\nb" or "\nab"; need to handle trailing \n for "Argentina\n\n" in {{Infobox settlement|pushpin_map=Argentina|pushpin_label_position=|pushpin_map_alt=|pushpin_map_caption=Location of Salta in Argentina}};
				case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.CarriageReturn:	// added \t, \r; DATE:2013-03-27
				case Byte_ascii.Underline:if (ltr_bgn != -1) add_ws = true; ++cur;//cur = ttlTrie.Match_pos(); 
					continue;	// only mark add_ws if ltr_seen; this ignores ws at bgn; also, note "continue"
				case Byte_ascii.Question:
					if (txt_bb_len + 1 < end)	// guard against trailing ? (which shouldn't happen)
						qarg_bgn = txt_bb_len + 1;
					break;
				case Byte_ascii.Amp:
					int cur2 = cur + 1;//cur = ttlTrie.Match_pos();
					if (cur2 == end) {}	// guards against terminating &; EX: [[Bisc &]]; NOTE: needed b/c MatchAtCur does not do bounds checking for cur in src; src[src.length] will be called when & is last character;
					else {
						Object html_ent_obj = amp_trie.MatchAtCur(src, cur2, end);
						if (html_ent_obj != null) {									
							Xop_amp_trie_itm amp_itm = (Xop_amp_trie_itm)html_ent_obj;
							match_pos = amp_trie.Match_pos();
							if (amp_itm.Tid() == Xop_amp_trie_itm.Tid_name) {
								switch (amp_itm.Char_int()) {
									case 160:	// NOTE: &nbsp must convert to space; EX:w:United States [[Image:Dust Bowl&nbsp;- Dallas, South Dakota 1936.jpg|220px|alt=]]
										b_ary = Bry_space;
										break;
									case Byte_ascii.Amp:
									case Byte_ascii.Quote:
									case Byte_ascii.Lt:
									case Byte_ascii.Gt:
										b_ary = amp_itm.Xml_name_bry();
										break;
									case Xop_amp_trie_itm.Char_int_null:	// &#xx;
										int end_pos = ByteAry_.FindFwd(src, Byte_ascii.Semic, match_pos, end);
										if (end_pos == ByteAry_.NotFound) {} // &# but no terminating ";" noop: defaults to current_byte which will be added below;
										else {
											b_ary = amp_itm.Xml_name_bry();									
											match_pos = end_pos + 1;
										}
										break;
									default:
										b_ary = amp_itm.Utf8_bry();
										break;
								}
							}
							else {
								boolean ncr_is_hex = amp_itm.Tid() == Xop_amp_trie_itm.Tid_num_hex;
								fail.Val_false();
								int rv = Xop_amp_wkr.CalcNcr(wiki.Ctx().Msg_log(), ncr_is_hex, src, end, cur2, match_pos, ncr_val, fail);
								if (fail.Val()) {}
								else {
									b_ary = gplx.intl.Utf8_.EncodeCharAsAry(ncr_val.Val());
									match_pos = rv;
								}
							}
						}
					}
					break;
				case Byte_ascii.Lt:
					if (cur + 3 < end) {
						if (	src[cur + 1] == Byte_ascii.Bang
							&&	src[cur + 2] == Byte_ascii.Dash
							&&	src[cur + 3] == Byte_ascii.Dash
							) {
							int cur3 = cur + 3;//cur = ttlTrie.Match_pos();
							int find = ByteAry_.FindFwd(src, Xop_comm_lxr.End_ary, cur3, end);
							if (find != -1) {
								cur = find + Xop_comm_lxr.End_ary.length;
								continue;
							}
							else {
								msg_log.Add_itm_none(Xop_ttl_log.Comment_eos, src, bgn, end);
								return false;
							}
						}
					}
					if (anch_bgn != -1) {
						anchor_encoder.Encode(anchor_encoder_bfr, src, cur, cur + 1);
						b_ary = anchor_encoder_bfr.XtoAryAndClear();
						match_pos = cur + 1;
					}
					break;
//case Byte_ascii.Bang: case Byte_ascii.Quote: case Byte_ascii.Dollar: case Byte_ascii.Percent: case Byte_ascii.Apos: case Byte_ascii.Paren_bgn:
//case Byte_ascii.Paren_end: case Byte_ascii.Asterisk: case Byte_ascii.Comma: case Byte_ascii.Dash: case Byte_ascii.Dot:
//case Byte_ascii.Semic: case Byte_ascii.Eq: case Byte_ascii.Question: case Byte_ascii.At:
//case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
//case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
//case Byte_ascii.Ltr_A: case Byte_ascii.Ltr_B: case Byte_ascii.Ltr_C: case Byte_ascii.Ltr_D: case Byte_ascii.Ltr_E:
//case Byte_ascii.Ltr_F: case Byte_ascii.Ltr_G: case Byte_ascii.Ltr_H: case Byte_ascii.Ltr_I: case Byte_ascii.Ltr_J:
//case Byte_ascii.Ltr_K: case Byte_ascii.Ltr_L: case Byte_ascii.Ltr_M: case Byte_ascii.Ltr_N: case Byte_ascii.Ltr_O:
//case Byte_ascii.Ltr_P: case Byte_ascii.Ltr_Q: case Byte_ascii.Ltr_R: case Byte_ascii.Ltr_S: case Byte_ascii.Ltr_T:
//case Byte_ascii.Ltr_U: case Byte_ascii.Ltr_V: case Byte_ascii.Ltr_W: case Byte_ascii.Ltr_X: case Byte_ascii.Ltr_Y: case Byte_ascii.Ltr_Z:
//case Byte_ascii.Ltr_a: case Byte_ascii.Ltr_b: case Byte_ascii.Ltr_c: case Byte_ascii.Ltr_d: case Byte_ascii.Ltr_e:
//case Byte_ascii.Ltr_f: case Byte_ascii.Ltr_g: case Byte_ascii.Ltr_h: case Byte_ascii.Ltr_i: case Byte_ascii.Ltr_j:
//case Byte_ascii.Ltr_k: case Byte_ascii.Ltr_l: case Byte_ascii.Ltr_m: case Byte_ascii.Ltr_n: case Byte_ascii.Ltr_o:
//case Byte_ascii.Ltr_p: case Byte_ascii.Ltr_q: case Byte_ascii.Ltr_r: case Byte_ascii.Ltr_s: case Byte_ascii.Ltr_t:
//case Byte_ascii.Ltr_u: case Byte_ascii.Ltr_v: case Byte_ascii.Ltr_w: case Byte_ascii.Ltr_x: case Byte_ascii.Ltr_y: case Byte_ascii.Ltr_z:
//case Byte_ascii.Backslash: case Byte_ascii.Pow: case Byte_ascii.Tick: case Byte_ascii.Tilde:
//case Byte_ascii.Plus:	// NOTE: not in wgLegalTitleChars, but appears in MW titles; EX: [[File:ASLSJSW Aas+.PNG]]
// NOTE: DefaultSettings.php defines wgLegalTitleChars as " %!\"$&'()*,\\-.\\/0-9:;=?@A-Z\\\\^_`a-z~\\x80-\\xFF+"; the characters above are okay; those below are not
				case Byte_ascii.Gt: case Byte_ascii.Pipe:
				case Byte_ascii.Brack_bgn: case Byte_ascii.Brack_end: case Byte_ascii.Curly_bgn: case Byte_ascii.Curly_end:
					if (anch_bgn != -1) {
						anchor_encoder.Encode(anchor_encoder_bfr, src, cur, cur + 1);
						b_ary = anchor_encoder_bfr.XtoAryAndClear();
						match_pos = cur + 1;
					}
					else {
						msg_log.Add_itm_none(Xop_ttl_log.Invalid_char, src, bgn, end);
						return false;
					}
					break;
				case Bidi_0_E2:
					if (cur + 2 < end) {
						if (	src[cur + 1] == Bidi_1_80) {
							switch (src[cur + 2]) {
								case Bidi_2_8E: case Bidi_2_8F: case Bidi_2_AA: case Bidi_2_AB: case Bidi_2_AC: case Bidi_2_AD: case Bidi_2_AE:
									cur += 3;
									continue;
								default:
									break;							
							}
						}
					}
					break;
				default:
					break;
			}
			++cur;
//				Object itm_obj = ttlTrie.Match(b, src, cur, end);
//				if (itm_obj == null) ++cur;
//				else {
//					ByteVal itm = (ByteVal)itm_obj;
//					switch (itm.Val()) {
//						case Xoa_ttl_trie.Id_invalid:
//							msg_log.Add_itm_none(src, bgn, end, Xop_ttl_log.Invalid_char);
//							return false;
//						case Xoa_ttl_trie.Id_colon:	
//							if (cur == bgn) {	// initial colon; flag; note that "  :" is not handled; note that colon_count is not incremented
//								forceLiteralLink = true;
//								cur = ttlTrie.Match_pos();
//								continue;	// do not add to bfr
//							}
//							else {
//								if (ltr_bgn == -1) {// no ltrs seen; treat as literal; occurs for ::fr:wikt:test and fr::Help:test
//									++colon_count;
//									break;
//								}
//								boolean part_found = false;
//								if (colon_count == 0) {// 1st colon; 
//									Object o = wiki.Wiki_mgr().GetByMid(tmp_bfr.Bfr(), ltr_bgn, txt_bb_len); // check if wiki; note: wiki is not possible for other colons
//									if (o != null) {
//										wik_bgn = 0;			// wik_bgn can only start at 0
//										part_found = true;
//									}
//									else {	// not wiki; check if ns; note: if wiki, don't try to extract ns; EX: "fr:Aide:test"
//										o = nsMgr.Trie_match_exact(tmp_bfr.Bfr(), ltr_bgn, txt_bb_len);
//										if (o != null) {
//											ns = (Xow_ns)o;
//											ns_bgn = ltr_bgn;
//											part_found = true;
//										}
//									}
//								}
//								if (part_found) {
//									page_bgn = txt_bb_len + 1;	// anticipate page_bgn;
//									add_ws = false;				// if there was an add_ws, ignore; EX: "Category :" should ignore space
//									ltr_bgn_reset = true;		// ltr_bgn_reset
//								}
//								colon_count++;		// increment colon count
//								break;
//							}
//						case Xoa_ttl_trie.Id_hash:	anch_bgn = (txt_bb_len) + 1; break; // flag last anch_bgn
//						case Xoa_ttl_trie.Id_slash:	leaf_bgn = (txt_bb_len) + 1; break;	// flag last leaf_bgn
//						case Xoa_ttl_trie.Id_newLine:	// NOTE: for now, treat nl just like space; not sure if it should accept "a\nb" or "\nab"; need to handle trailing \n for "Argentina\n\n" in {{Infobox settlement|pushpin_map=Argentina|pushpin_label_position=|pushpin_map_alt=|pushpin_map_caption=Location of Salta in Argentina}};
//						case Xoa_ttl_trie.Id_space:
//						case Xoa_ttl_trie.Id_underline:if (ltr_bgn != -1) add_ws = true; cur = ttlTrie.Match_pos(); continue;	// only mark add_ws if ltr_seen; this ignores ws at bgn; also, note "continue"
//						case Xoa_ttl_trie.Id_amp:
//							cur = ttlTrie.Match_pos();
//							if (cur == end) {}	// guards against terminating &; EX: [[Bisc &]]; NOTE: needed b/c MatchAtCur does not do bounds checking for cur in src; src[src.length] will be called when & is last character;
//							else {
//								Object html_ent_obj = wiki.App().AmpTrie().MatchAtCur(src, cur, end);
//								if (html_ent_obj != null) {									
//									Xop_amp_trie_itm amp_itm = (Xop_amp_trie_itm)html_ent_obj;
//									switch (amp_itm.CharInt()) {
//										case Byte_ascii.Amp:
//										case Byte_ascii.Quote:
//										case Byte_ascii.Lt:
//										case Byte_ascii.Gt:
//											b_ary = amp_itm.XmlEntityName();
//											break;
//										default:
//											b_ary = amp_itm.CharIntAsAry();
//											break;
//									}
//								}
//							}
//							break;
//						case Xoa_ttl_trie.Id_comment_bgn:
//							cur = ttlTrie.Match_pos();
//							int find = ByteAry_.FindFwd(src, Xop_comm_lxr.End_ary, cur, end);
//							if (find != -1) {
//								cur = find + Xop_comm_lxr.End_ary.length;
//								continue;
//							}
//							else {
//								msg_log.Add_itm_none(src, bgn, end, Xop_ttl_log.Comment_eos);
//								return false;
//							}
//						default:					break;
//					}
//					cur = ttlTrie.Match_pos();
//				}
			if (add_ws) {	// add ws and toggle flag
				tmp_bfr.Add_byte(Byte_ascii.Space); ++txt_bb_len;
//					tmp_bfr.Add_byte(Byte_ascii.Underline); ++txt_bb_len;
				add_ws = false;
			}
			if (ltr_bgn == -1) ltr_bgn = txt_bb_len; // if 1st letter not seen, mark 1st letter					
			if		(b_ary == null) {tmp_bfr.Add_byte(b); ++txt_bb_len;} // add to bfr
			else					{tmp_bfr.Add(b_ary); txt_bb_len += b_ary.length; b_ary = null; cur = match_pos;}	// NOTE: b_ary != null only for amp_trie
			if (ltr_bgn_reset)  {// colon found; set ws to bgn mode; note that # and / do not reset 
				ltr_bgn_reset = false;
				ltr_bgn = -1;
			}
		}
		if (txt_bb_len == 0) {msg_log.Add_itm_none(Xop_ttl_log.Len_0, src, bgn, end); return false;}
//			if (	((txt_bb_len - ns_len) > 255 && ns.Id() != Xow_ns_.Id_special)		// DELETE: will cause multi-byte langs like thai to fail on long links; DATE:2013-02-02
//				||	((txt_bb_len - ns_len) > 512 && ns.Id() == Xow_ns_.Id_special)) {msg_log.Add_itm_none(Xop_ttl_log.Len_max, src, bgn, end); return false;}
		if (wik_bgn == -1 && page_bgn == txt_bb_len) {	// if no wiki, but page_bgn is at end, then ttl is ns only; EX: "Help:"; NOTE: "fr:", "fr:Help" is allowed 
			msg_log.Add_itm_none(Xop_ttl_log.Ttl_is_ns_only, src, bgn, end);
			return false;
		}
		full_txt = tmp_bfr.XtoAryAndClear();

//			if (wik_bgn == 0 && txt_bb_len == page_bgn) { // 0-len intrawiki; EX: [[fr:]] on Main Page
//				Xoa_ttl cur_page_ttl = wiki.Ctx().Page().Ttl();
//				this.raw = cur_page_ttl == null ? ByteAry_.Empty : ByteAry_.Copy(cur_page_ttl.Page_txt());	// set raw to current page ttl; note that this is needed for lnki_caption to show correctly (otherwise lnki_caption will be blank); this is a quasi-hack as it depends on the ttl of the current page, but passing in another argument feels sloppy
//				tmp_bfr.Add(this.raw);
//			}
		if (wik_bgn == -1 && ns.Case_match() == Xow_ns_.Case_match_1st) {// do not check case if wiki is dif; NOTE: wik_bgn == -1 chk is needed for ttls like "fr:"
			byte page_0 = full_txt[page_bgn];
			int page_0_len = gplx.intl.Utf8_.CharLen(page_0);
			full_txt = wiki.Lang().Case_mgr().Case_reuse_upper(full_txt, page_bgn, page_bgn + page_0_len);
		}
		Xow_ns tors_ns = ns.Id_talk() ? nsMgr.Get_by_ord(ns.Ord_subj_id()) : nsMgr.Get_by_ord(ns.Ord_talk_id());
		tors_txt = tors_ns.Name_txt_w_colon();
		return true;
	}
	private static final BoolRef fail = BoolRef.new_(false); IntRef ncr_val = IntRef.neg1_(); byte[] Bry_space = ByteAry_.new_ascii_(" ");
	public static byte[] Replace_spaces(byte[] raw) {return ByteAry_.Replace(raw, Byte_ascii.Space, Byte_ascii.Underline);}
	public static byte[] Replace_unders(byte[] raw) {return ByteAry_.Replace(raw, Byte_ascii.Underline, Byte_ascii.Space);}
	private int wik_bgn = -1, ns_bgn = -1, page_bgn = 0, leaf_bgn = -1, anch_bgn = -1, root_bgn = -1; byte[] tors_txt;
	public static final int Wik_bgn_int = -1;
	public static final byte Subpage_spr = Byte_ascii.Slash;	// EX: A/B/C
	static final byte 	// NOTE: Bidi characters appear in File titles /\xE2\x80[\x8E\x8F\xAA-\xAE]/S
			Bidi_0_E2 	= (byte)226
		,	Bidi_1_80 	= (byte)128
		,	Bidi_2_8E 	= (byte)142
		,	Bidi_2_8F	= (byte)143
		,	Bidi_2_AA	= (byte)170
		,	Bidi_2_AB	= (byte)171
		,	Bidi_2_AC	= (byte)172
		,	Bidi_2_AD	= (byte)173
		,	Bidi_2_AE	= (byte)174
		;
	public static final int Max_len = 2048;	// ASSUME: max len of 256 * 8 bytes
}
class Xoa_url_encoder {
	public byte[] Encode(byte[] src) {
		int src_len = src.length;
		for (int i = 0; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Space:		bb.Add(Bry_underline); break;
				case Byte_ascii.Amp:		bb.Add(Bry_amp); break;
				case Byte_ascii.Apos:		bb.Add(Bry_apos); break;
				case Byte_ascii.Eq:			bb.Add(Bry_eq); break;
				case Byte_ascii.Plus:		bb.Add(Bry_plus); break;
				default:					bb.Add_byte(b); break;
				// FUTURE: html_entities, etc:
			}
		}
		return bb.XtoAryAndClear();
	}
	private static final byte[] Bry_amp = ByteAry_.new_ascii_("%26"), Bry_eq = ByteAry_.new_ascii_("%3D")
		, Bry_plus = ByteAry_.new_ascii_("%2B"), Bry_apos = ByteAry_.new_ascii_("%27")
		, Bry_underline = new byte[] {Byte_ascii.Underline}
		;
	ByteAryBfr bb = ByteAryBfr.new_();
	public static final Xoa_url_encoder _ = new Xoa_url_encoder(); Xoa_url_encoder() {}
}
class Xoa_ttl_trie {
	public static ByteTrieMgr_fast new_() {
		ByteTrieMgr_fast rv = ByteTrieMgr_fast.cs_();
		rv.Add(Byte_ascii.Colon				, ByteVal.new_(Id_colon));
		rv.Add(Byte_ascii.Hash				, ByteVal.new_(Id_hash));
		rv.Add(Byte_ascii.Slash				, ByteVal.new_(Id_slash));
		rv.Add(Byte_ascii.Space				, ByteVal.new_(Id_space));
		rv.Add(Byte_ascii.Underline			, ByteVal.new_(Id_underline));
		rv.Add(Byte_ascii.Amp				, ByteVal.new_(Id_amp));
		rv.Add(Xop_comm_lxr.Bgn_ary			, ByteVal.new_(Id_comment_bgn));
		rv.Add(Byte_ascii.NewLine			, ByteVal.new_(Id_newLine));
		rv.Add(Byte_ascii.Brack_bgn			, ByteVal.new_(Id_invalid));
		rv.Add(Byte_ascii.Curly_bgn			, ByteVal.new_(Id_invalid));
		return rv;
	}
	public static final byte Id_colon = 0, Id_hash = 1, Id_slash = 2, Id_space = 3, Id_underline = 4, Id_amp = 5, Id_comment_bgn = 6, Id_invalid = 7, Id_newLine = 8;
}
