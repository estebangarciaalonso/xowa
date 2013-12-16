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
class Xop_comm_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_comment;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast coreTrie) {coreTrie.Add(Bgn_ary, this);}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int srcLen, int bgnPos, int curPos) {
		while (true) {
			if (curPos == srcLen) {ctx.Msg_log().Add_itm_none(Xop_comment_log.Eos, src, bgnPos, curPos); break;}
			Object o = lkp.MatchAtCur(src, curPos, srcLen);
			if (o != null) {
				curPos += End_len;
				break;
			}
			curPos++;
		}

		// trim ws if entire line is comment; REF.MW:Preprocessor_DOM.php|preprocessToXml|handle comments
		int nl_lhs = -1, nl_rhs = -1;
		int subs_len = root.Subs_len();
		for (int i = subs_len - 1; i > -1; i--) {
			Xop_tkn_itm sub = root.Subs_get(i);
			switch (sub.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_space: case Xop_tkn_itm_.Tid_tab:
					break;
				case Xop_tkn_itm_.Tid_newLine:	// new_line found; anything afterwards is a \s or a \t; SEE.WIKT:coincidence
					nl_lhs = i;
					break;
				default:
					i = -1;
					break;
			}
		}
		boolean loop = true;
		int loop_pos = curPos;
		while (loop) {
			if (loop_pos == srcLen) break;
			switch (src[loop_pos++]) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
					break;
				case Byte_ascii.NewLine:
					loop = false;
					nl_rhs = loop_pos;
					break;
				default:
					loop = false;
					break;
			}
		}
		if (nl_lhs != -1 && nl_rhs != -1) {
			for (int i = nl_lhs + 1; i < subs_len; i++) {
				Xop_tkn_itm sub_tkn = root.Subs_get(i);
				sub_tkn.Ignore_y_grp_(ctx, root, i);
			}
			ctx.Para().Pre_disable();
			curPos = nl_rhs;
		}

		ctx.Subs_add(root, tkn_mkr.Ignore(bgnPos, curPos, Xop_ignore_tkn.Ignore_tid_comment));
		return curPos;
	}	private ByteTrieMgr_fast lkp = ByteTrieMgr_fast.cs_();	// NOTE: private trie, b/c comment is leaf
	Xop_comm_lxr() {lkp.Add(End_ary, IntVal.zero_());}
	public static final byte[] Bgn_ary = new byte[] {60, 33, 45, 45}, /*<!--*/ End_ary = new byte[] {45, 45, 62}; /*-->*/
	private static final int End_len = End_ary.length;
	public static final Xop_comm_lxr _ = new Xop_comm_lxr();
}
