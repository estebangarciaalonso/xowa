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
package gplx.xowa.langs.vnts; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
class Xop_vnt_parser {
	private Xop_vnt_flag_ary_bldr flag_ary_bldr = new Xop_vnt_flag_ary_bldr();
	private Xop_vnt_flag_lang_bldr flag_lang_bldr;
	private byte[] src; int src_len, bgn_pos, cur_pos;
	private boolean ws_bgn_chk; private int ws_bgn_idx, ws_end_idx;
	private void Clear() {
		flag_ary_bldr.Clear();
		flag_lang_bldr.Clear();
		ws_bgn_chk = true; ws_bgn_idx = -1; ws_end_idx = -1;
	}
	public void Init(Xop_vnt_flag_lang_bldr flag_lang_bldr) {
		this.flag_lang_bldr = flag_lang_bldr;
	}
	public void Parse(byte[] src, int src_len, int bgn_pos) {
		this.Clear();
		this.src = src; this.src_len = src_len; this.bgn_pos = bgn_pos;
		cur_pos = bgn_pos;
		boolean loop = true;
		while (loop) {
			if (cur_pos >= src_len) break;
			byte b = src[cur_pos];
			Object dlm_obj = dlm_trie.Match(b, src, cur_pos, src_len);
			if (dlm_obj == null) {	// regular char
				if (ws_bgn_chk) ws_bgn_chk = false; else ws_end_idx = -1;		// INLINE: AdjustWsForTxtTkn					
			}
			else {				// dlm obj
				byte dlm_tid = ((ByteVal)dlm_obj).Val();
				switch (dlm_tid) {
					case Dlm_tid_pipe:
						int pipe_pos = dlm_trie.Match_pos();	
						Parse_flag(pipe_pos);
						cur_pos = pipe_pos + 1;	// + 1 to skip pipe
						break;
					case Dlm_tid_colon:
					case Dlm_tid_semic:
					case Dlm_tid_end:
						break;
				}
			}
			cur_pos = dlm_trie.Match_pos();
			break;
		}
	}
	private void Parse_flag(int pipe_pos) {
		Object flag_obj = flag_trie.MatchAtCur(src, ws_bgn_idx, ws_end_idx);
		if (flag_obj != null) {	// known flag; check that next non_ws is |
			int flag_end = flag_trie.Match_pos();
			if (Xop_lxr_.Find_fwd_non_ws(src, flag_end, pipe_pos) == pipe_pos) {	// next non-ws-char pos matches pipe_pos; differentiates between "-{D|" and "-{ D x |"
				flag_ary_bldr.Add((Xop_vnt_flag)flag_obj);
				return;
			}
		}			
		Parse_flag_vnts(pipe_pos, ws_bgn_idx);	// unknown tid sequence; either (a) "lang" cmd ("-{zh-hans;zh-hant|a}-") or (b) invalid cmd ("-{X|a}-")
	}
	private void Parse_flag_vnts(int pipe_pos, int vnt_pos) {
		boolean loop = true;
		ByteTrieMgr_slim trie = flag_lang_bldr.Trie();
		while (loop) {
			boolean last = false;
			boolean valid = true;
			Object vnt_obj = trie.MatchAtCur(src, vnt_pos, ws_end_idx);
			if (vnt_obj == null) break;						// no more vnts found; stop
			vnt_pos = trie.Match_pos();						// update pos to end of vnt
			int semic_pos = Xop_lxr_.Find_fwd_non_ws(src, vnt_pos, pipe_pos);
			if (semic_pos == ByteAry_.NotFound)
				last = true;
			else {											// char found; make sure it is semic
				if (src[semic_pos] != Byte_ascii.Semic) {	// invalid vnt; ignore; EX: -{zh-hansx|}-
					valid = false;
				}
				vnt_pos = semic_pos + 1;					// update pos to after semic
			}
			if (valid) {
				flag_lang_bldr.Add((Xop_vnt_flag_lang_itm)vnt_obj);
			}
			if (last) break;
		}
		flag_ary_bldr.Add(flag_lang_bldr.Bld());
	}
	private static ByteTrieMgr_fast flag_trie = Xop_vnt_flag_.Trie;
	private static final byte Dlm_tid_bgn = 0, Dlm_tid_end = 1, Dlm_tid_pipe = 2, Dlm_tid_colon = 3, Dlm_tid_semic = 4, Dlm_tid_kv = 5;
	private static ByteTrieMgr_fast dlm_trie = ByteTrieMgr_fast.cs_()
	.Add_bry_bval(Xop_variant_lxr.Hook_bgn	, Dlm_tid_bgn)
	.Add_bry_bval(Xop_variant_lxr.Hook_end	, Dlm_tid_end)
	.Add_bry_bval(Byte_ascii.Pipe			, Dlm_tid_pipe)
	.Add_bry_bval(Byte_ascii.Colon			, Dlm_tid_colon)
	.Add_bry_bval(Byte_ascii.Semic			, Dlm_tid_semic)
	.Add_bry_bval(ByteAry_.new_ascii_("=>")	, Dlm_tid_kv)
	;
}
/*
-{flags|lang:text}-				EX: -{D|zh-hant:a}-
-{lang:text;lang:text}			EX: -{zh-hans:a;zh-hant:b}-
-{lang;lang|text}-				EX: -{zh-hans;zh-hant|XXXX}-
-{text}-						EX: -{a}-		"in which case no conversion should take place for text"

-{lang:data_0;data_1;}-			EX: -{zh-hans:<span style='border:solid;color:blue;'>;zh-hant:b}-
. where data_0 and data_1 is actually one itm since ; is not delimiter b/c data_1 must be variant_code
-{zh-hans:a-{zh-hans:b}-c}-	
*/
