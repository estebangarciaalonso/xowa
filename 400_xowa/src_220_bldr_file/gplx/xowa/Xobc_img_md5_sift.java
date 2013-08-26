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
import gplx.ios.*; import gplx.xowa.bldrs.*; 
class Xobc_img_md5_sift extends Xob_itm_basic_base implements Xob_cmd, GfoInvkAble {
	public Xobc_img_md5_sift(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY;} public static final String KEY = "img_md5_sift";
	public Io_url Src_dir() {return src_dir;} Io_url src_dir;
	public Io_url Temp_dir() {return tmp_dir;} Io_url tmp_dir;
	public Io_url_gen Dump_url_gen() {return dump_url_gen;} Io_url_gen dump_url_gen;
	Gfo_fld_wtr fld_wtr; Gfo_fld_rdr fld_rdr; ByteAryBfr tmp = ByteAryBfr.new_();
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		this.bldr = bldr; fld_wtr = Gfo_fld_wtr.xowa_(); fld_rdr = Gfo_fld_rdr.xowa_();
		tmp_dir = wiki.Fsys_mgr().Tmp_dir().GenSubDir(KEY);
		if (src_dir == null) src_dir = wiki.Fsys_mgr().Tmp_dir().GenSubDir_nest(Xobc_img_prep_xfer.KEY, "dump");
		Io_url errors_dir = tmp_dir.GenSubDir("errors");
//			Io_mgr._.DeleteDirDeep_ary(errors_dir);
		dump_url_gen = Io_url_gen_.dir_(errors_dir);
//		dump_bfr = new ByteAryBfr(dump_fil_len);
//		file_mgr = bldr.App().File_mgr();
		wiki.App().File_mgr().Download_mgr().Enabled_(true);
		wiki.File_mgr().Cfg_download().Enabled_(true);
		dir_wtr.Root_dir_(tmp_dir.GenSubDir("done"));
		dir_wtr.Clear();
	}
//		Io_line_rdr rdr_(Io_url dir) {
//			Io_url[] fils = Io_mgr._.QueryDir_fils(dir);
//			return new Io_line_rdr(bldr.Status_mgr(), fils).Key_gen_(Io_line_rdr_key_gen_.first_pipe);
//		}
	void Write(Xofo_file xfer, ListAdp link_list) {
//		xfer.Links_(link_list);
//		if (dump_bfr.BfrLen() > dump_fil_len) {Io_mgr._.AppendFilByt(dump_url_gen.Nxt_url(), dump_bfr.Bfr(), dump_bfr.BfrLen()); dump_bfr.Clear();}
//		xfer.Write_bldr(fld_parser, dump_bfr);
	}
	public void Cmd_print() {}
	Io_line_rdr rdr_(Io_url dir) {
		Io_url[] fils = Io_mgr._.QueryDir_fils(dir);
		return new Io_line_rdr(bldr.Usr_dlg(), fils).Key_gen_(Io_line_rdr_key_gen_.first_pipe);
	}	Xob_dir_wtr_md5 dir_wtr = new Xob_dir_wtr_md5();
	public void Cmd_run() {
		Io_line_rdr rdr = rdr_(src_dir);
		Gfo_fld_rdr fld_parser = Gfo_fld_rdr.xowa_();
		while (rdr.Read_next()) {
			fld_parser.Ini(rdr.Bfr(), rdr.Itm_pos_bgn());
			byte[] name = fld_parser.Read_bry_escape();
			byte[] md5 = Xof_xfer_itm.Md5_(name);
			dir_wtr.Write(md5, rdr.Bfr(), rdr.Itm_pos_bgn(), rdr.Itm_pos_end());
		}
		dir_wtr.Flush();
	}
	public void Cmd_end() {
		int itms_len = dir_wtr.Itms_len();
		for (int i = 0; i < itms_len; i++) {
			Xob_md5_fil itm = dir_wtr.Itms_get_at(i);
			Reconcile(itm);
		}  
	}
	void Reconcile(Xob_md5_fil itm) {
		// load the file
		// dump into hash; create a new type with Name,Sizes,Founds key by Name
		// read the directory;
		// foreach file in directory, check name
		//	if exists, delete size from itm; if no more sizes, delete itm; (file already built; don't build again)
		//  if not, delete from dir (file built, but removed from wiki; free up space)
		// resave remaining items in hash
	}
}
class Io_url_gen_md5 implements Io_url_gen {
	public Io_url_gen_md5(Io_url root_dir) {this.root_dir = root_dir;} Io_url root_dir;
	public Io_url Cur_url() {return cur_url;} Io_url cur_url;
	public Io_url Nxt_url() {return cur_url;}
	public Io_url[] Prv_urls() {return cur_fil_ary;}
	public void Del_all() {}
	public void Set_md5(byte... ary) {
		int ary_len = ary.length - 1;	// -1: do not create dir for last byte
		cur_url = root_dir;
		for (int i = 0; i < ary_len; i++)
			cur_url = cur_url.GenSubDir(Byte_.XtoStr(ary[i]));
		cur_url.GenSubFil(String_.new_utf8_(ary) + ".csv");
		cur_fil_ary = new Io_url[] {cur_url};
	}	Io_url[] cur_fil_ary;
}
class Io_sort_fil_md5 implements Io_sort_cmd { // 123|bgn|end|1
	public Io_sort_fil_md5(Gfo_usr_dlg usr_dlg, Io_url_gen_md5 url_gen, int flush_len) {this.usr_dlg = usr_dlg; this.url_gen = url_gen; this.flush_len = flush_len;} Io_url_gen_md5 url_gen; ByteAryBfr bfr = ByteAryBfr.new_(); int flush_len; Gfo_usr_dlg usr_dlg;
	public void Sort_bgn() {}
	public void Sort_do(Io_line_rdr rdr) {
		int itm_bgn = rdr.Itm_pos_bgn(), itm_end = rdr.Itm_pos_end();
		int key_bgn = rdr.Key_pos_bgn(), key_end = rdr.Key_pos_end();
		byte[] rdr_bfr = rdr.Bfr();
		byte[] md5_cur = ByteAry_.Mid(rdr_bfr, key_bgn, key_end);
		boolean md5_dif = ByteAry_.Eq(md5_cur, md5_prv);
		if (md5_dif || bfr.Bry_len() + (itm_end - itm_bgn) > flush_len) Flush();
		if (md5_dif) {
			url_gen.Set_md5(md5_cur);
			md5_prv = md5_cur;
		}
		bfr.Add_mid(rdr_bfr, itm_bgn, itm_end);
	}	byte[] md5_prv = ByteAry_.Empty;
	public void Sort_end() {
		Flush();
		bfr.Rls();
	}
	void Flush() {
		Io_url url = url_gen.Nxt_url();
		usr_dlg.Prog_one(GRP_KEY, "make", "making ~{0}", url.NameAndExt());
		Io_mgr._.SaveFilByt(url, bfr.Bry(), bfr.Bry_len());
		bfr.Clear();
	}
	private static final String GRP_KEY = "xowa.bldr.sort_md5";
}
class Xob_dir_wtr_md5 {
	public Io_url Root_dir() {return root_dir;} public void Root_dir_(Io_url v) {this.root_dir = v;}  Io_url root_dir;
	public int Md5_prefix_len() {return md5_prefix_len;} public void Md5_prefix_len_(int v) {md5_prefix_len = v;} private int md5_prefix_len = 3;
	public int Bfr_len_max() {return bfr_len_max;} public void Bfr_len_max_(int v) {bfr_len_max = v;} private int bfr_len_max = Io_mgr.Len_kb * 4;	// assuming 4096 md5s (16^3), max of 16 MB memory
	Hash_adp_bry bfrs_hash = new Hash_adp_bry(true);
	ListAdp bfrs_list = ListAdp_.new_();
	public Xob_md5_fil Itms_get_at(int i) {return (Xob_md5_fil)bfrs_list.FetchAt(i);}
	public int Itms_len() {return bfrs_list.Count();}
	public void Write(byte[] key, byte[] src, int itm_bgn, int itm_end) {
		Object o = bfrs_hash.Get_by_mid(key, 0, md5_prefix_len);
		if (o == null) {
			Io_url url = Root_dir_make_url(key);
			o = new Xob_md5_fil(key, url);
			bfrs_hash.Add_bry_obj(ByteAry_.Mid(key, 0, md5_prefix_len), o);
			bfrs_list.Add(o);
		}
		Xob_md5_fil itm = (Xob_md5_fil)o;
		if (itm.Bfr().Bry_len() + itm_end - itm_bgn > bfr_len_max) Io_mgr._.SaveFilBfr(itm.Url(), itm.Bfr());
		itm.Bfr().Add_mid(src, itm_bgn, itm_end);
	}
	Io_url Root_dir_make_url(byte[] md5) {
		Io_url url = root_dir;
		for (int i = 0; i < md5_prefix_len - 1; i++)
			url = url.GenSubDir(Byte_.XtoStr(md5[i]));
		url = url.GenSubFil(Byte_.XtoStr(md5[md5_prefix_len - 1]) + ".csv");
		return url;
	}
	public void Flush() {
		int bfrs_list_len = bfrs_list.Count();
		for (int i = 0; i < bfrs_list_len; i++) {
			Xob_md5_fil itm = (Xob_md5_fil)bfrs_list.FetchAt(i);
			if (itm.Bfr().Bry_len() > 0)
				Io_mgr._.SaveFilBfr(itm.Url(), itm.Bfr());
		}
	}
	public void Clear() {
		bfrs_list.Clear();
		bfrs_hash.Clear();
	}
}
class Xob_md5_fil {
	public Xob_md5_fil(byte[] md5, Io_url url) {this.md5 = md5; this.url =url;}
	public byte[] Md5() {return md5;} private byte[] md5;
	public Io_url Url() {return url;} Io_url url;
	public ByteAryBfr Bfr() {return bfr;} ByteAryBfr bfr = ByteAryBfr.reset_(4 * Io_mgr.Len_kb);
}
