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
package gplx.xowa.xtns.hiero; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.html.*; import gplx.xowa.html.*;
class Hiero_parser {
	private ByteTrieMgr_slim trie = ByteTrieMgr_slim.cs_();
	private ListAdp blocks = ListAdp_.new_();
	private Hiero_block cur_block;
	private ByteAryBfr cur_tkn = ByteAryBfr.reset_(16);
	public Hiero_block[] Parse(byte[] src, int bgn, int end) {
		blocks.Clear();
		this.cur_block = new Hiero_block();
		cur_tkn.Clear();
		int pos = bgn;
		while (true) {
			if (pos == end) break;
			Object o = trie.MatchAtCur(src, pos, end);
			if (o == null)
				++pos;
			else {
				Hiero_parser_itm itm = (Hiero_parser_itm)o;
				int new_pos = trie.Match_pos();
				switch (itm.Tid()) {
					case Hiero_parser_itm.Tid_comment:
						int end_comm = Byte_ary_finder.Find_fwd(src, Html_consts.Comm_end, new_pos, end);
						if (end_comm == Byte_ary_finder.Not_found)	// --> not found; for now, ignore <!--
							pos = new_pos;
						else
							pos = end_comm + Html_consts.Comm_end_len;
						break;
					case Hiero_parser_itm.Tid_block_spr:
						New_block();
						break;
					case Hiero_parser_itm.Tid_single_char:
						Single_char_block(itm);
						break;
					case Hiero_parser_itm.Tid_dot:
						Dot();
						break;
					case Hiero_parser_itm.Tid_tkn_spr:
						New_token(itm);
						break;
					default:
						New_char(itm);
						break;
				}
			}
		}			
		this.New_block();// flush remaining items
		return (Hiero_block[])blocks.XtoAryAndClear(Hiero_block.class);
	}
	private void New_block() {
		this.New_token(null);
		blocks.Add(cur_block);
		cur_block = new Hiero_block();
	}
	private void New_token(Hiero_parser_itm itm) {
		if (cur_tkn.Len_gt_0())
			cur_block.Add(cur_tkn.XtoAryAndClear());
		if (itm != null)
			cur_block.Add(itm.Key());
	}
	private void Single_char_block(Hiero_parser_itm itm) {
		this.New_block();
		cur_block.Add(itm.Key());
	}
	private void Dot() {
		if (cur_tkn.Eq(Byte_ascii.Dot)) {		// is "."
			cur_tkn.Add_byte(Byte_ascii.Dot);	// make ".."
			this.New_block();
		}
		else {
			this.New_block();
			cur_tkn.Add_byte(Byte_ascii.Dot);	// make "."; note that New_block clears tkn
		}
	}
	private void New_char(Hiero_parser_itm itm) {
		if (cur_tkn.Eq(Byte_ascii.Dot)) {		// is "."
			this.New_block();
			this.cur_tkn.Add(itm.Key());		// $this->token = $char;
		}
		else
			this.cur_tkn.Add(itm.Key());		// $this->token .= $char;
	}
	public void Init() {
		Init_itms(Hiero_parser_itm.Tid_block_spr, " ", "-", "\t", "\n", "\r");
		Init_itms(Hiero_parser_itm.Tid_tkn_spr, "*", ":", "(", ")");
		Init_itms(Hiero_parser_itm.Tid_dot, ".");
		Init_itms(Hiero_parser_itm.Tid_single_char , "!");
		Init_itms(Hiero_parser_itm.Tid_comment, Html_consts.Comm_bgn_str);
	}
	private void Init_itms(byte tid, String... keys) {
		int keys_len = keys.length;
		for (int i = 0; i < keys_len; i++) {
			String key_str = keys[i];
			byte[] key_bry = ByteAry_.new_utf8_(key_str);
			Hiero_parser_itm itm = new Hiero_parser_itm(tid, key_bry);
			trie.Add(key_bry, itm);
		}
	}
}
class Hiero_parser_itm {
	public Hiero_parser_itm(byte tid, byte[] key) {this.tid = tid; this.key = key;}
	public byte Tid() {return tid;} private byte tid;
	public byte[] Key() {return key;} private byte[] key;
	public static final byte Tid_block_spr = 1, Tid_tkn_spr = 2, Tid_single_char = 3, Tid_dot = 4, Tid_comment = 5;
}
class Hiero_block {
	private ListAdp list = ListAdp_.new_();
	public int Len() {return list.Count();}
	public byte[] Get_at(int i) {return (byte[])list.FetchAt(i);}
	public void Add(byte[] v) {list.Add(v);}
}
