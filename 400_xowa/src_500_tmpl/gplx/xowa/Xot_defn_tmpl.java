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
public class Xot_defn_tmpl implements Xot_defn {
	public byte Defn_tid() {return Xot_defn_.Tid_tmpl;}
	public boolean Defn_require_colon_arg() {return false;}
	public int CacheSize() {return data_raw.length;}
	public byte[] Name() {return name;} private byte[] name;
	public byte[] Data_raw() {return data_raw;} private byte[] data_raw;
	public Xop_root_tkn Root() {return root;} private Xop_root_tkn root;
	public void Init_by_new(Xow_ns ns, byte[] name, byte[] data_raw, Xop_root_tkn root, boolean onlyInclude) {
		this.ns = ns; this.name = name; this.data_raw = data_raw; this.root = root; this.onlyInclude_exists = onlyInclude;
		ns_id = ns.Id();
	}	private Xow_ns ns; int ns_id;
	public void Init_by_raw(Xop_root_tkn root, boolean onlyInclude_exists) {
		this.root = root; this.onlyInclude_exists = onlyInclude_exists;
	}
	byte[] Extract_onlyinclude(byte[] src, Bry_bfr_mkr bfr_mkr) {
		ByteAryBfr bfr = bfr_mkr.Get_m001();
		int pos = 0;
		int src_len = src.length;
		while (true) {
			int find_bgn = ByteAry_.FindFwd(src, Bry_onlyinclude_bgn, pos, src_len);
			if (find_bgn == ByteAry_.NotFound) {
				break;
			}
			int find_bgn_lhs = find_bgn + Bry_onlyinclude_bgn_len;
			int find_end = ByteAry_.FindFwd(src, Bry_onlyinclude_end, find_bgn_lhs, src_len);
			if (find_end == ByteAry_.NotFound) {
				break;
			}
			bfr.Add_mid(src, find_bgn_lhs, find_end);
			pos = find_end + Bry_onlyinclude_end_len;
		}
		return bfr.Mkr_rls().XtoAryAndClear();
	}
	private static final byte[] Bry_onlyinclude_bgn = ByteAry_.new_ascii_("<onlyinclude>"), Bry_onlyinclude_end = ByteAry_.new_ascii_("</onlyinclude>");
	private static int Bry_onlyinclude_bgn_len = Bry_onlyinclude_bgn.length, Bry_onlyinclude_end_len = Bry_onlyinclude_end.length;
	public void Rls() {
		if (root != null) root.Clear();
		root = null;
	}
	public void Parse_tmpl(Xop_ctx ctx) {ctx.Wiki().Parser().Parse_tmpl(this, ctx, ctx.Tkn_mkr(), ns, name, data_raw);}	boolean onlyinclude_parsed = false;
	public boolean Tmpl_evaluate(Xop_ctx ctx, Xot_invk caller, ByteAryBfr bfr) {
		if (root == null) Parse_tmpl(ctx);
		byte[] full_name = name;
		if (ns_id  != Xow_ns_.Id_template) {	// NOTE: must create unique key for Tmpl_stack_add below; EX:commons:Mona Lisa; {{Institution:Louvre}} -> {{Louvre}}
			byte[] prefix = ns_id == Xow_ns_.Id_main ? Xow_ns_.Ns_prefix_main : ns.Name_db_w_colon();
			full_name = ByteAry_.Add(prefix, name);
		}
		if (!ctx.Wiki().View_data().Tmpl_stack_add(full_name)) {
			bfr.Add_str("<span class=\"error\">Template loop detected:" + String_.new_utf8_(full_name) + "</span>");
			return false;
		}
		boolean rv = true;
		if (onlyInclude_exists) {
			Xow_wiki wiki = ctx.Wiki();
			if (!onlyinclude_parsed) {
				onlyinclude_parsed = true;
				byte[] new_data = Extract_onlyinclude(data_raw, wiki.Utl_bry_bfr_mkr());
				Xop_ctx new_ctx = Xop_ctx.new_sub_(wiki);
//					Xop_root_tkn new_root = new_ctx.Tkn_mkr().Root(new_data);
//					wiki.Parser().Parse(new_root, new_ctx, new_ctx.Tkn_mkr(), new_data, Xop_parser_.Parse_tid_tmpl, wiki.Parser().Tmpl_trie(), Xop_parser_.Doc_bgn_bos);
				Xot_defn_tmpl tmpl = wiki.Parser().Parse_tmpl(new_ctx, new_ctx.Tkn_mkr(), wiki.Ns_mgr().Ns_template(), ByteAry_.Empty, new_data);
				tmpl.Root().Tmpl_compile(new_ctx, new_data, Xot_compile_data.Null);
				data_raw = new_data;
				root = tmpl.Root();
			}
		}
		int subs_len = root.Subs_len();
		for (int i = 0; i < subs_len; i++) {
			boolean result = root.Subs_get(i).Tmpl_evaluate(ctx, data_raw, caller, bfr);
			if (!result) rv = false;
		}
//			else {
//				int subs_len = root.Subs_len();
//				for (int i = 0; i < subs_len; i++) {
//					boolean result = root.Subs_get(i).Tmpl_evaluate(ctx, data_raw, caller, bfr);
//					if (!result) rv = false;
//				}
//			}
		ctx.Wiki().View_data().Tmpl_stack_del();
		return rv;
	}
	public Xot_defn Clone(int id, byte[] name) {throw Err_mgr._.not_implemented_();}
	boolean onlyInclude_exists;
}
