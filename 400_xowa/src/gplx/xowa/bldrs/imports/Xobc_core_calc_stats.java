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
package gplx.xowa.bldrs.imports; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.xowa.bldrs.*;
public class Xobc_core_calc_stats extends Xob_itm_basic_base implements Xob_cmd {
	public Xobc_core_calc_stats(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki);}
	public String Cmd_key() {return KEY;} public static final String KEY = "core.calc_stats";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {}
	public void Cmd_run() {Exec();}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private void Exec() {
		int ns_len = wiki.Ns_mgr().Ords_len();
		int total = 0;
		for (int i = 0; i < ns_len; i++) {
			Xow_ns ns = wiki.Ns_mgr().Ords_ary()[i];
			int ns_count = Calc_counts(ns);
			ns.Count_(ns_count);
			total += ns_count;
		}
		int count_main = Calc_count_articles(wiki.Ns_mgr().Ns_main());
		int count_file = Calc_count_articles(wiki.Ns_mgr().Ns_file());
		Bry_bfr bfr = Bry_bfr.new_();
		Gen_call(Bool_.Y, bfr, Xow_wiki.Invk_stats);
		Gen_call(Bool_.N, bfr, Xow_wiki_stats.Invk_number_of_articles_, count_main);
		Gen_call(Bool_.N, bfr, Xow_wiki_stats.Invk_number_of_files_, count_file);
		Gen_call(Bool_.N, bfr, Xow_wiki_stats.Invk_number_of_pages_, total);
		for (int i = 0; i < ns_len; i++) {
			Xow_ns ns = wiki.Ns_mgr().Ords_ary()[i];
			if (ns.Id() < 0) continue;
			bfr.Add_byte_nl();
			Gen_call(Bool_.N, bfr, Xow_wiki_stats.Invk_number_of_articles_in_ns_, ns.Num_str(), Int_.Xto_str_pad_bgn(ns.Count(), 10));
		}
		bfr.Add_byte_nl().Add_byte(Byte_ascii.Semic).Add_byte_nl();
		Io_url wiki_gfs = Wiki_gfs_url(wiki);
		Io_mgr._.SaveFilBfr(wiki_gfs, bfr);
	}
	private void Gen_call(boolean first, Bry_bfr bfr, String key, Object... vals) {
		if (!first) bfr.Add_byte(Byte_ascii.Dot);
		bfr.Add_str(key);
		int len = vals.length;
		if (len > 0) {
			bfr.Add_byte(Byte_ascii.Paren_bgn);
			for (int i = 0; i < len; i++) {
				if (i != 0) bfr.Add_byte(Byte_ascii.Comma).Add_byte(Byte_ascii.Space);
				Object val = vals[i];
				bfr.Add_str(Object_.Xto_str_strict_or_null_mark(val));
			}
			bfr.Add_byte(Byte_ascii.Paren_end);
		}
	}
	int Calc_counts(Xow_ns ns) {
		Io_url reg_url = wiki.Fsys_mgr().Url_ns_reg(ns.Num_str(), Xow_dir_info_.Tid_ttl);
		Xowd_regy_mgr reg_mgr = new Xowd_regy_mgr(reg_url);
		int files_ary_len = reg_mgr.Files_ary().length;
		int count = 0;
		for (int i = 0; i < files_ary_len; i++) {
			count += reg_mgr.Files_ary()[i].Count();
		}
		return count;
	}
	int Calc_count_articles(Xow_ns ns) {
		Io_url hive_dir = wiki.Fsys_mgr().Root_dir().GenSubDir_nest(Xow_dir_info_.Name_ns, ns.Num_str(), Xow_dir_info_.Name_title);
		return Calc_count_articles_dir(ns, hive_dir);
	}
	int Calc_count_articles_dir(Xow_ns ns, Io_url dir) {
		Io_url[] subs = Io_mgr._.QueryDir_args(dir).DirInclude_().ExecAsUrlAry();
		int count = 0;
		int subs_len = subs.length;
		bldr.Usr_dlg().Prog_one(GRP_KEY, "count", "calculating: ~{0}", dir.Raw());
		for (int i = 0; i < subs_len; i++) {
			Io_url sub = subs[i];
			if (sub.Type_dir())
				count += Calc_count_articles_dir(ns, sub);
			else
				count += Calc_count_articles_fil(ns, sub);
		}
		return count;
	}
	int Calc_count_articles_fil(Xow_ns ns, Io_url fil) {
		if (String_.Eq(fil.NameAndExt(), Xow_dir_info_.Name_reg_fil)) return 0;
		int rv = 0;
		byte[] bry = Io_mgr._.LoadFilBry(fil);
		Xob_xdat_file xdat_file = new Xob_xdat_file().Parse(bry, bry.length, fil);
		Xodb_page page = Xodb_page.tmp_();
		int count = xdat_file.Count();
		for (int i = 0; i < count; i++) {
			byte[] ttl_bry = xdat_file.Get_bry(i);
			Xodb_page_.Txt_ttl_load(page, ttl_bry);
			rv += page.Type_redirect() ? 0 : 1;
		}
		return rv;
	}
	static final String GRP_KEY = "xowa.bldr.calc_stats";
	public static Io_url Wiki_gfs_url(Xow_wiki wiki) {return wiki.Fsys_mgr().Root_dir().GenSubFil_nest("cfg", "wiki_stats.gfs");}
}
