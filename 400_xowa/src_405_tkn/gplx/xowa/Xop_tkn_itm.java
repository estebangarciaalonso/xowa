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
public interface Xop_tkn_itm extends Xop_tkn_grp {
	byte Tkn_tid();
	Xop_tkn_itm Tkn_ini_pos(boolean immutable, int bgn, int end);
	Xop_tkn_itm Tkn_clone(Xop_ctx ctx, int bgn, int end);
	boolean Tkn_immutable();
	Xop_tkn_grp Tkn_grp();
	int Src_bgn();
	int Src_end();
	int Src_bgn_grp(Xop_tkn_grp grp, int sub_idx);
	int Src_end_grp(Xop_tkn_grp grp, int sub_idx);
	int Tkn_sub_idx();
	boolean Ignore();
	Xop_tkn_itm Tkn_grp_(Xop_tkn_grp grp, int sub_idx);
	void Src_end_(int v);
	void Src_end_grp_(Xop_ctx ctx, Xop_tkn_grp grp, int sub_idx, int src_end);
	Xop_tkn_itm Ignore_y_();
	void Ignore_y_grp_(Xop_ctx ctx, Xop_tkn_grp grp, int sub_idx);
	void Clear();
	void Tmpl_fmt(Xop_ctx ctx, byte[] src, Xot_fmtr fmtr);
	void Tmpl_compile(Xop_ctx ctx, byte[] src, Xot_compile_data prep_data); // SEE:NOTE_1:Tmpl_compile
	boolean Tmpl_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, ByteAryBfr bfr);
}
class Xop_tkn_null implements Xop_tkn_itm {
	public byte Tkn_tid() {return Xop_tkn_itm_.Tid_null;}
	public boolean Tkn_immutable() {return true;}
	public Xop_tkn_grp Tkn_grp() {return Xop_tkn_grp_.Null;}
	public Xop_tkn_itm Tkn_ini_pos(boolean immutable, int bgn, int end) {return this;}
	public Xop_tkn_itm Tkn_grp_(Xop_tkn_grp grp, int sub_idx) {return this;}
	public Xop_tkn_itm Tkn_clone(Xop_ctx ctx, int bgn, int end) {return this;}
	public int Tkn_sub_idx() {return -1;}
	public int Src_bgn() {return -1;}
	public int Src_end() {return -1;} 
	public int Src_bgn_grp(Xop_tkn_grp grp, int sub_idx) {return -1;}
	public int Src_end_grp(Xop_tkn_grp grp, int sub_idx) {return -1;}
	public int Subs_src_bgn(int sub_idx) {return -1;}
	public int Subs_src_end(int sub_idx) {return -1;}
	public void Src_end_(int v) {}
	public void Src_end_grp_(Xop_ctx ctx, Xop_tkn_grp grp, int sub_idx, int src_end) {}
	public boolean Ignore() {return false;} public Xop_tkn_itm Ignore_y_() {return this;}
	public int Subs_len() {return 0;}
	public Xop_tkn_itm Subs_get(int i) {return null;}
	public void Subs_add(Xop_tkn_itm sub) {}
	public void Subs_add_grp(Xop_tkn_itm sub, Xop_tkn_grp old_grp, int old_sub_idx) {}
	public void Subs_del_after(int pos_bgn) {}
	public void Subs_clear() {}
	public void Subs_move(Xop_tkn_itm tkn) {}
	public Xop_tkn_itm Immutable_clone(Xop_ctx ctx, Xop_tkn_itm tkn, int sub_idx) {return this;}
	public void Ignore_y_grp_(Xop_ctx ctx, Xop_tkn_grp grp, int sub_idx) {}
	public void Subs_grp_(Xop_ctx ctx, Xop_tkn_itm tkn, Xop_tkn_grp grp, int sub_idx) {}
	public void Subs_src_pos_(int sub_idx, int bgn, int end) {}
	public void Clear() {}
	public void Tmpl_fmt(Xop_ctx ctx, byte[] src, Xot_fmtr fmtr) {}
	public void Tmpl_compile(Xop_ctx ctx, byte[] src, Xot_compile_data prep_data) {}
	public boolean Tmpl_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, ByteAryBfr bfr) {return true;}
	public static final Xop_tkn_null Null_tkn = new Xop_tkn_null();
}
/*
NOTE_1: Tmpl_compile
- called for tmpl_defn
- identifies tkn as static or dynamic; important for evaluate later; if static, evaluate will simply extract src
- if static, parses prm; EX: {{{1|a}}} will produce member variables of idx=1 and dflt=a
- if static, parses tmpl_name; EX: {{concat|a|b}} will generate name of concat
- if <onlyinclude> mark tmpl accordingly
*/
