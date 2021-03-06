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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Wdata_external_lang_links_data {
	private Hash_adp_bry langs_hash = Hash_adp_bry.ci_(); private ListAdp langs_list = ListAdp_.new_();
	public boolean Enabled() {return enabled;} private boolean enabled; public Wdata_external_lang_links_data Enabled_(boolean v) {enabled = v; return this;}
	public boolean Sort() {return sort;} private boolean sort; public Wdata_external_lang_links_data Sort_(boolean v) {sort = v; return this;}
	public int Langs_len() {return langs_list.Count();}
	public Wdata_external_lang_links_data Langs_add(byte[] v) {langs_hash.Add(v, v); langs_list.Add(v); return this;}
	public byte[] Langs_get_at(int i) {return (byte[])langs_list.FetchAt(i);}
	public boolean Langs_hide(byte[] src, int bgn, int end) {
		if (sort) return false;
		return langs_hash.Get_by_mid(src, bgn, end) == null;
	}
	public void Reset() {
		enabled = false;
		sort = false;
		langs_list.Clear();
		langs_hash.Clear();
	}
	public void Parse(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, Pf_func_base pfunc) {
		enabled = true;
		byte[] argx = pfunc.Eval_argx(ctx, src, caller, self);
		if (ByteAry_.Eq(argx, Key_sort)) {sort = true; return;}	// {{noexternallanglinks:*}}; assume it cannot be combined with other langs_hash: EX: {{noexternallanglinks:*|en|fr}}
		int args_len = self.Args_len();
		Langs_add(argx);
		ByteAryBfr tmp_bfr = ctx.App().Utl_bry_bfr_mkr().Get_b128();
		for (int i = 0; i < args_len; i++) {
			Arg_nde_tkn nde = self.Args_get_by_idx(i);
			nde.Val_tkn().Tmpl_evaluate(ctx, src, self, tmp_bfr);
			byte[] lang = tmp_bfr.XtoAryAndClear();
			Langs_add(lang);
		}
		tmp_bfr.Mkr_rls();
	}
	public static final byte[] Key_sort = new byte[] {Byte_ascii.Asterisk};
}
