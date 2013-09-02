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
package gplx.xowa; import gplx.*;
public class Xol_lnki_arg_parser {
	static final byte Key_dim_num = 0, Key_dim_x = 1, Key_dim_px = 2, Key_space = 3; ByteTrieMgr_fast key_trie = ByteTrieMgr_fast.cs_(); int key_trie_max; ByteAryBfr int_bfr = ByteAryBfr.reset_(16);
	public void Evt_lang_changed(Xol_lang lang) {
		ByteAryBfr tmp_bfr = int_bfr;
		ByteRef rslt = ByteRef.zero_();
		Xol_kwd_mgr mgr = lang.Kwd_mgr();
		key_trie.Clear();
		Xol_kwd_grp list = null;
		int len = Keys_ids.length;
		for (int i = 0; i < len; i++) {
			int[] val = Keys_ids[i];
			list = mgr.Get_at(val[0]);			// NOTE: val[0] is magic_word id
			if (list == null) {
				 if (Env_.Mode_testing())
					continue;		// TEST: allows partial parsing of $magicWords
				else
					 list = lang.App().Lang_mgr().Lang_en().Kwd_mgr().Get_at(val[0]);
			}
			Xol_kwd_itm[] words = list.Itms();
			int words_len = words.length;
			for (int j = 0; j < words_len; j++) {
				Xol_kwd_itm word = words[j];
				byte[] word_bry = Xol_kwd_parse_data.Strip(tmp_bfr, word.Bry(), rslt);
				Init_key_trie(word_bry, (byte)val[1]);		// NOTE: val[1] is lnki_key tid; ASSUME: case_sensitive for all "img_" words; note that all Messages**.php seem to be case_sensitive ("array(1, ..."); resisting change b/c of complexity/perf (need a cs trie and a ci trie)
			}
		}
		list = mgr.Get_at(Xol_kwd_grp_.Id_img_width);
		if (list == null)
			list = lang.App().Lang_mgr().Lang_en().Kwd_mgr().Get_at(Xol_kwd_grp_.Id_img_width);
		Init_size_trie(tmp_bfr, list);
	}
	public byte Identify_tid(byte[] src, int bgn, int end, IntRef width, IntRef height) {
		int len = end - bgn;
		if (len > key_trie_max) return Tid_caption;				// too long for any key in size_trie; return caption
		ByteVal val = (ByteVal)key_trie.MatchAtCur(src, bgn, end);
		if (val != null && len == key_trie.Match_pos() - bgn)	// check for false matches; EX: alternate= should not match alt=
			return val.Val();									// match; return val;
		Object bwd_obj = bwd_trie.MatchAtCur(src, end - 1, bgn - 1);
		if (bwd_obj != null && ((ByteVal)bwd_obj).Val() == Tid_dim) { // ends with "px"; try to parse size
			int_bfr.Clear();
			int match_len = end -1 - bwd_trie.Match_pos();
			boolean mode_width = true;
			int itm_end = bgn + (len - match_len);				// remove trailing px
			for (int i = bgn; i < itm_end; i++) {
				byte b = src[i];
				Object o = size_trie.Match(b, src, i, itm_end);
				if (o == null) return Tid_caption;				// letter or other invalid character; return caption
				ByteVal v = (ByteVal)o;
				switch (v.Val()) {
					case Key_dim_num:	int_bfr.Add_byte(b); break;
					case Key_space:		break;	// ignore space; EX: "100 px"
					case Key_dim_px:	{
						if (i == itm_end - match_len) i = itm_end; // allow trailing px at end; EX: "20pxpx"; not allowed: "20px20px"; "20pxpxpx"
						else return Tid_caption;
						break;
					}
					case Key_dim_x:		{
						if (mode_width) {
							width.Val_(int_bfr.XtoIntAndClear(-1));
							mode_width = false;
							break;
						}
						else return Tid_caption;
					}
				}
			}
			int dim = int_bfr.XtoIntAndClear(-1);
			if	(mode_width)	width.Val_(dim);
			else				height.Val_(dim);
			return Tid_dim;
		}
		return Tid_caption;
	}	private ByteTrieMgr_bwd_slim bwd_trie = ByteTrieMgr_bwd_slim.cs_();
	ByteTrieMgr_fast size_trie = ByteTrieMgr_fast.cs_();
	void Init_key_trie(byte[] key, byte v) {
		ByteVal val = ByteVal.new_(v);
		key_trie.Add(key, val);
		int key_len = key.length;
		if (key_len > key_trie_max) key_trie_max = key_len;
	}
	void Init_size_trie(ByteAryBfr tmp_bfr, Xol_kwd_grp list) {
		if (list == null && Env_.Mode_testing()) return;	// TEST: allows partial parsing of $magicWords
		size_trie.Clear(); bwd_trie.Clear();
		for (int i = 0; i < 10; i++)
			size_trie.Add((byte)(i + Char_.AsciiZero), ByteVal.new_(Key_dim_num));
		size_trie.Add(Byte_ascii.Space, ByteVal.new_(Key_space));
		size_trie.Add(X_bry, ByteVal.new_(Key_dim_x));
		Xol_kwd_itm[] words = list.Itms();
		int words_len = words.length;
		ByteRef rslt = ByteRef.zero_();
		for (int i = 0; i < words_len; i++) {
//			if (i == 0) {
//				dim_px = word_bry;
//				dim_px_len = dim_px.length;
//			}
			byte[] word_bry = Xol_kwd_parse_data.Strip(tmp_bfr, words[i].Bry(), rslt);			
			if (word_bry.length + 5 > key_trie_max) key_trie_max = word_bry.length + 5; 
			size_trie.Add(word_bry, ByteVal.new_(Key_dim_px));
			bwd_trie.Add(word_bry, ByteVal.new_(Tid_dim));
		}
	}	
	private static final byte[] X_bry = ByteAry_.new_utf8_("x"); //byte[] dim_px; int dim_px_len; 
	public static final byte[] Bry_upright = ByteAry_.new_utf8_("upright"), Bry_thumbtime = ByteAry_.new_utf8_("thumbtime");
	public static final byte
		Tid_unknown = 0, Tid_thumb = 1, Tid_left = 2, Tid_right = 3, Tid_none = 4, Tid_center = 5, Tid_frame = 6, Tid_frameless = 7, Tid_upright = 8, Tid_border = 9
		, Tid_alt = 10, Tid_link = 11, Tid_baseline = 12, Tid_sub = 13, Tid_super = 14, Tid_top = 15, Tid_text_top = 16, Tid_middle = 17, Tid_bottom = 18, Tid_text_bottom = 19
		, Tid_dim = 20
		, Tid_trg = 21, Tid_caption = 22
		, Tid_page = 23
		, Tid_noplayer = 24, Tid_noicon = 25, Tid_thumbtime = 26
		;
	private static final int[][] Keys_ids = new int[][] 
	{	new int[] {Xol_kwd_grp_.Id_img_thumbnail	, Tid_thumb}
	,	new int[] {Xol_kwd_grp_.Id_img_manualthumb	, Tid_thumb}	// RESEARCH: what is manualthumb? 'thumb=$1' vs 'thumb'
	,	new int[] {Xol_kwd_grp_.Id_img_right		, Tid_right}
	,	new int[] {Xol_kwd_grp_.Id_img_left			, Tid_left}
	,	new int[] {Xol_kwd_grp_.Id_img_none			, Tid_none}
	,	new int[] {Xol_kwd_grp_.Id_img_center		, Tid_center}
	,	new int[] {Xol_kwd_grp_.Id_img_framed		, Tid_frame}
	,	new int[] {Xol_kwd_grp_.Id_img_frameless	, Tid_frameless}
	,	new int[] {Xol_kwd_grp_.Id_img_page			, Tid_page}		// RESEARCH: what is page? 'page=$1'
	,	new int[] {Xol_kwd_grp_.Id_img_upright		, Tid_upright}
	,	new int[] {Xol_kwd_grp_.Id_img_border		, Tid_border}
	,	new int[] {Xol_kwd_grp_.Id_img_baseline		, Tid_baseline}
	,	new int[] {Xol_kwd_grp_.Id_img_sub			, Tid_sub}
	,	new int[] {Xol_kwd_grp_.Id_img_super		, Tid_super}
	,	new int[] {Xol_kwd_grp_.Id_img_top			, Tid_top}
	,	new int[] {Xol_kwd_grp_.Id_img_text_top		, Tid_text_top}
	,	new int[] {Xol_kwd_grp_.Id_img_middle		, Tid_middle}
	,	new int[] {Xol_kwd_grp_.Id_img_bottom		, Tid_bottom}
	,	new int[] {Xol_kwd_grp_.Id_img_text_bottom	, Tid_text_bottom}
	,	new int[] {Xol_kwd_grp_.Id_img_link			, Tid_link}
	,	new int[] {Xol_kwd_grp_.Id_img_alt			, Tid_alt}
	,	new int[] {Xol_kwd_grp_.Id_ogg_noplayer		, Tid_noplayer}	// RESEARCH: what does noplayer do?; find example
	,	new int[] {Xol_kwd_grp_.Id_ogg_noicon		, Tid_noicon}
	,	new int[] {Xol_kwd_grp_.Id_ogg_thumbtime	, Tid_thumbtime}
	};
//		,	new int[] {Xol_kwd_grp_.Id_img_width		, Tid_dim}		// NOTE: handle px separately;
}
