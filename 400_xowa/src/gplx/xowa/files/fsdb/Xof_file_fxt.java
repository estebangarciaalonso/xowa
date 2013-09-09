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
package gplx.xowa.files.fsdb; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.fsdb.*; import gplx.dbs.*; import gplx.xowa.files.qrys.*; import gplx.xowa.files.bins.*; import gplx.xowa.files.cnvs.*;
class Xof_file_fxt {		
	private Fsdb_xtn_thm_itm tmp_thm = Fsdb_xtn_thm_itm.new_();
	private Xof_qry_wrk_mock qry_wkr_mock = new Xof_qry_wrk_mock();
	private Xof_bin_wkr_fsdb bin_wkr_fsdb;
	public Xof_fsdb_mgr Fsdb_mgr() {return fsdb_mgr;} private Xof_fsdb_mgr fsdb_mgr;
	public Xof_fsdb_itm_key_bldr_mok Key_bldr() {return key_bldr;} private Xof_fsdb_itm_key_bldr_mok key_bldr = new Xof_fsdb_itm_key_bldr_mok();
	public void Reset() {
		Io_mgr._.DeleteDirDeep(Xo_test.Url_file_enwiki());
		fsdb_mgr = new Xof_fsdb_mgr();
		qry_wkr_mock.Clear();
		Xoa_app app = Xoa_app_fxt.app_();
		Xof_repo_mgr repo_mgr = app.File_mgr().Repos();
		Xof_repo_itm repo_itm = new Xof_repo_itm(repo_mgr, Xow_wiki_.Domain_enwiki_bry).Root_str_("mem/root/enwiki").Wiki_key_(Xow_wiki_.Domain_enwiki_bry).Dir_depth_(2);
		repo_mgr.Add(repo_itm);
		repo_itm = new Xof_repo_itm(repo_mgr, Xow_wiki_.Domain_commons_bry).Root_str_("mem/root/common").Wiki_key_(Xow_wiki_.Domain_commons_bry).Dir_depth_(2);
		repo_mgr.Add(repo_itm);
		fsdb_mgr.Init_by_wiki(Io_url_.mem_dir_("mem/root/"), Xo_test.Url_file_enwiki(), ListAdp_.Null, app.File_mgr().Repos());
		fsdb_mgr.Qry_mgr().Wkrs_(qry_wkr_mock);
		bin_wkr_fsdb = new Xof_bin_wkr_fsdb(fsdb_mgr);
		fsdb_mgr.Bin_mgr().Wkrs_(bin_wkr_fsdb);
		fsdb_mgr.Bin_mgr().Resizer_(Xof_img_wkr_resize_img_mok._);
	}
	public void Init_qry_xowa__bin_fsdb__commons_orig(String ttl, int w, int h) {
		this.Init_bin_fsdb(Xof_fsdb_arg_init_bin.new_().Init_commons(Bool_.N, ttl, w, h));
		this.Init_qry_xowa(Xof_fsdb_arg_init_qry.new_().Init_commons(ttl, w, h));
	}
	public void Init_bin_fsdb(Xof_fsdb_arg_init_bin arg) {
		if (arg.Is_thumb())
			fsdb_mgr.Thm_insert(tmp_thm, arg.Wiki(), arg.Ttl(), arg.Ext_id(), arg.W(), arg.Thumbtime(), arg.H(), arg.Modified(), arg.Hash(), arg.Bin().length, gplx.ios.Io_stream_rdr_.mem_(arg.Bin()));
		else
			fsdb_mgr.Img_insert(tmp_thm, arg.Wiki(), arg.Ttl(), arg.Ext_id(), arg.Modified(), arg.Hash(), arg.Bin().length, gplx.ios.Io_stream_rdr_.mem_(arg.Bin()), arg.W(), arg.H(), Fsdb_xtn_img_itm.Bits_default);
	}
	public void Init_qry_xowa(Xof_fsdb_arg_init_qry arg) {
		qry_wkr_mock.Add_wiki_size(arg.Ttl(), arg.Wiki(), arg.W(), arg.H());
	}
	public void Test_get(Xof_fsdb_arg_exec_get arg) {
		Xof_fsdb_itm itm = itm_(String_.new_utf8_(arg.Ttl()), arg.Lnki_type(), arg.Lnki_w(), arg.Lnki_h(), arg.Lnki_upright(), arg.Lnki_thumbtime());
		OrderedHash hash = itm_hash(itm);
		fsdb_mgr.Reg_select(Xog_win_wtr_.Null, hash);
		if (arg.Rslt_reg() != Xof_reg_wkr_.Tid_null) Tfds.Eq(arg.Rslt_reg(), itm.Rslt_reg(), "rslt_reg");
		if (arg.Rslt_qry() != Xof_qry_wkr_.Tid_null) Tfds.Eq(arg.Rslt_qry(), itm.Rslt_qry(), "rslt_qry");
		if (arg.Rslt_bin() != Xof_bin_wkr_.Tid_null) Tfds.Eq(arg.Rslt_bin(), itm.Rslt_bin(), "rslt_bin");
		if (arg.Rslt_cnv() != Xof_cnv_wkr_.Tid_null) Tfds.Eq(arg.Rslt_cnv(), itm.Rslt_cnv(), "rslt_cnv");
	}
	public void Test_fsys(String url, String expd_bin) {
		Tfds.Eq(expd_bin, Io_mgr._.LoadFilStr(url));
	}
	public void Test_regy(String key, String expd_url) {
		Xof_fsdb_itm itm = Exec_reg_select_itm(key);
		Tfds.Eq(expd_url, itm.Html_url_rel());
	}
	public void Test_regy_pass(String key) {
		Xof_fsdb_itm itm = Exec_reg_select_itm(key);
		Tfds.Eq_false(String_.Len_eq_0(itm.Html_url_rel()));
	}
	public void Test_regy_missing_qry(String key) {
		Xof_fsdb_itm itm = Exec_reg_select_itm(key);
		Tfds.Eq(Xof_reg_wkr_.Tid_missing_qry, itm.Rslt_reg());
	}
	public void Test_regy_missing_bin(String key) {
		Xof_fsdb_itm itm = Exec_reg_select_itm(key);
		Tfds.Eq(Xof_reg_wkr_.Tid_missing_bin, itm.Rslt_reg());
	}
	private Xof_fsdb_itm itm_(String ttl_str, byte type, int w, int h, double upright, int thumbtime) {
		byte[] ttl_bry = ByteAry_.new_ascii_(ttl_str);
		byte[] md5 = Xof_xfer_itm.Md5_(ttl_bry);
		Xof_ext ext = Xof_ext_.new_by_ttl_(ttl_bry);
		return new Xof_fsdb_itm().Init_by_lnki(key_bldr, ttl_bry, type, w, h, upright, thumbtime).Lnki_ext_(ext).Lnki_md5_(md5);
	}
	public void Rls() {fsdb_mgr.Rls();}
	private Xof_fsdb_itm Exec_reg_select_itm(String key) {
		Xof_fsdb_itm itm = new Xof_fsdb_itm().Lnki_key_(ByteAry_.new_utf8_(key));
		OrderedHash hash = itm_hash(itm);
		fsdb_mgr.Reg_select(Xog_win_wtr_.Null, hash);
		return itm;
	}
	private OrderedHash itm_hash(Xof_fsdb_itm itm) {
		OrderedHash rv = OrderedHash_.new_bry_();
		rv.Add(itm.Lnki_key(), itm);
		return rv;
	}
	public static String file_img_(int w, int h) {return String_.Format("{0},{1}", w, h);}
	public static String file_svg_(int w, int h) {return String_.Format("<svg width=\"{0}\" height=\"{1}\" />", w, h);}
}
class Xof_fsdb_itm_key_bldr_mok extends Xof_fsdb_itm_key_bldr {	public String Bld_as_str(String ttl) {return Bld_as_str(ByteAry_.new_utf8_(ttl), Xop_lnki_type.Id_none, Xop_lnki_tkn.Width_null, Xop_lnki_tkn.Height_null, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null);}	
	public String Bld_as_str(String ttl, byte lnki_type, int w) {return Bld_as_str(ByteAry_.new_utf8_(ttl), lnki_type, w, Xop_lnki_tkn.Height_null, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null);}	
}
class Xof_fsdb_arg_init_bin {
	public byte[] Wiki() {return wiki;} public Xof_fsdb_arg_init_bin Wiki_(byte[] v) {wiki = v; return this;} private byte[] wiki;
	public byte[] Ttl() {return ttl;} public Xof_fsdb_arg_init_bin Ttl_(byte[] v) {ttl = v; return this;} private byte[] ttl;
	public int Ext_id() {return ext_id;} public Xof_fsdb_arg_init_bin Ext_id_(int v) {ext_id = v; return this;} private int ext_id;
	public int W() {return w;} public Xof_fsdb_arg_init_bin W_(int v) {w = v; return this;} private int w = W_default;
	public int Thumbtime() {return thumbtime;} public Xof_fsdb_arg_init_bin Thumbtime_(int v) {thumbtime = v; return this;} private int thumbtime = Xop_lnki_tkn.Thumbtime_null;
	public int H() {return h;} public Xof_fsdb_arg_init_bin H_(int v) {h = v; return this;} private int h = H_default;
	public DateAdp Modified() {return modified;} public Xof_fsdb_arg_init_bin Modified_(DateAdp v) {modified = v; return this;} private DateAdp modified = Fsdb_xtn_thm_tbl.Modified_null;
	public String Hash() {return hash;} public Xof_fsdb_arg_init_bin Hash_(String v) {hash = v; return this;} private String hash = Fsdb_xtn_thm_tbl.Hash_null;
	public byte[] Bin() {return bin;} public Xof_fsdb_arg_init_bin Bin_(byte[] v) {bin = v; return this;} private byte[] bin;
	public boolean Is_thumb() {return is_thumb;} public Xof_fsdb_arg_init_bin Is_thumb_(boolean v) {is_thumb = v; return this;} private boolean is_thumb;
//		public boolean Is_orig() {return is_orig;} public Xof_fsdb_arg_init_bin Is_orig_(boolean v) {is_orig = v; return this;} private boolean is_orig;
	public Xof_fsdb_arg_init_bin Ttl_(String v) {ttl = ByteAry_.new_utf8_(v); return this;}
	public Xof_fsdb_arg_init_bin Ext_id_() {ext_id = Xof_ext_.new_by_ttl_(ttl).Id(); return this;}
	public Xof_fsdb_arg_init_bin Bin_(String v) {return Bin_(ByteAry_.new_ascii_(v));} 
	public Xof_fsdb_arg_init_bin Bin_by_size_() {
		String bin_str = ext_id == Xof_ext_.Id_svg ? Xof_file_fxt.file_svg_(w, h) : Xof_file_fxt.file_img_(w, h);
		return Bin_(ByteAry_.new_ascii_(bin_str));
	}
	public Xof_fsdb_arg_init_bin Init_commons_thumb(String ttl)						{return Init(Xow_wiki_.Domain_commons_bry, Bool_.Y, ttl, W_default, H_default, Xop_lnki_tkn.Thumbtime_null);}
	public Xof_fsdb_arg_init_bin Init_commons_thumb(String ttl, int w, int h)		{return Init(Xow_wiki_.Domain_commons_bry, Bool_.Y, ttl, w, h, Xop_lnki_tkn.Thumbtime_null);}
	public Xof_fsdb_arg_init_bin Init_commons_thumb(String ttl, int w, int h, int s){return Init(Xow_wiki_.Domain_commons_bry, Bool_.Y, ttl, w, h, s);}
	public Xof_fsdb_arg_init_bin Init_commons_orig(String ttl, int w, int h)		{return Init(Xow_wiki_.Domain_commons_bry, Bool_.N, ttl, w, h, Xop_lnki_tkn.Thumbtime_null);}
	public Xof_fsdb_arg_init_bin Init_commons(boolean thumb, String ttl, int w, int h)	{return Init(Xow_wiki_.Domain_commons_bry, thumb, ttl, w, h, Xop_lnki_tkn.Thumbtime_null);}
	public Xof_fsdb_arg_init_bin Init(byte[] wiki, boolean thumb, String ttl, int w, int h, int thumbtime) {
		return Wiki_(wiki).Is_thumb_(thumb).Ttl_(ttl).W_(w).H_(h).Thumbtime_(thumbtime).Ext_id_().Bin_by_size_();
	}
	public static final int W_default = 220, H_default = 200;
	public static Xof_fsdb_arg_init_bin new_() {return new Xof_fsdb_arg_init_bin();} Xof_fsdb_arg_init_bin() {}
}
class Xof_fsdb_arg_init_qry {
	public byte[] Wiki() {return wiki;} public Xof_fsdb_arg_init_qry Wiki_(byte[] v) {wiki = v; return this;} private byte[] wiki;
	public byte[] Ttl() {return ttl;} public Xof_fsdb_arg_init_qry Ttl_(byte[] v) {ttl = v; return this;} private byte[] ttl;
	public int W() {return w;} public Xof_fsdb_arg_init_qry W_(int v) {w = v; return this;} private int w = W_default;
	public int H() {return h;} public Xof_fsdb_arg_init_qry H_(int v) {h = v; return this;} private int h = H_default;
	public Xof_fsdb_arg_init_qry Init_commons(String ttl, int w, int h) {this.wiki = Xow_wiki_.Domain_commons_bry; this.ttl = ByteAry_.new_ascii_(ttl); this.w = w; this.h = h; return this;}
	public static Xof_fsdb_arg_init_qry new_() {return new Xof_fsdb_arg_init_qry();} Xof_fsdb_arg_init_qry() {}
	public static final int W_default = 440, H_default = 400;
}
class Xof_fsdb_arg_exec_get {
	public byte[] Ttl() {return ttl;} public Xof_fsdb_arg_exec_get Ttl_(byte[] v) {ttl = v; return this;} private byte[] ttl;
	public byte Lnki_type() {return lnki_type;} public Xof_fsdb_arg_exec_get Lnki_type_(byte v) {lnki_type = v; return this;} private byte lnki_type = Xop_lnki_type.Id_thumb;
	public int Lnki_w() {return lnki_w;} public Xof_fsdb_arg_exec_get Lnki_w_(int v) {lnki_w = v; return this;} private int lnki_w;
	public int Lnki_h() {return lnki_h;} public Xof_fsdb_arg_exec_get Lnki_h_(int v) {lnki_h = v; return this;} private int lnki_h = Xop_lnki_tkn.Height_null;
	public double Lnki_upright() {return lnki_upright;} public Xof_fsdb_arg_exec_get Lnki_upright_(double v) {lnki_upright = v; return this;} private double lnki_upright = Xop_lnki_tkn.Upright_null;
	public int Lnki_thumbtime() {return lnki_thumbtime;} public Xof_fsdb_arg_exec_get Lnki_thumbtime_(int v) {lnki_thumbtime = v; return this;} private int lnki_thumbtime = Xop_lnki_tkn.Thumbtime_null;
	public byte Rslt_reg() {return rslt_reg;} public Xof_fsdb_arg_exec_get Rslt_reg_(byte v) {rslt_reg = v; return this;} private byte rslt_reg = Xof_reg_wkr_.Tid_null;
	public byte Rslt_qry() {return rslt_qry;} public Xof_fsdb_arg_exec_get Rslt_qry_(byte v) {rslt_qry = v; return this;} private byte rslt_qry = Xof_qry_wkr_.Tid_null;
	public byte Rslt_bin() {return rslt_bin;} public Xof_fsdb_arg_exec_get Rslt_bin_(byte v) {rslt_bin = v; return this;} private byte rslt_bin = Xof_bin_wkr_.Tid_null;
	public byte Rslt_cnv() {return rslt_cnv;} public Xof_fsdb_arg_exec_get Rslt_cnv_(byte v) {rslt_cnv = v; return this;} private byte rslt_cnv = Xof_cnv_wkr_.Tid_null;
	public boolean Lnki_type_is_thumb() {return Xop_lnki_type.Id_is_thumb_like(lnki_type);}
	public Xof_fsdb_arg_exec_get Init_thumb(String ttl)			{return Init(ttl, Xop_lnki_type.Id_thumb, 220, Xop_lnki_tkn.Height_null);}
	public Xof_fsdb_arg_exec_get Init_thumb(String ttl, int w)	{return Init(ttl, Xop_lnki_type.Id_thumb, w, Xop_lnki_tkn.Height_null);}
	public Xof_fsdb_arg_exec_get Init_orig(String ttl)			{return Init(ttl, Xop_lnki_type.Id_none, Xop_lnki_tkn.Width_null, Xop_lnki_tkn.Height_null);}
	public Xof_fsdb_arg_exec_get Init(String ttl, byte type, int w, int h) {
		this.ttl = ByteAry_.new_utf8_(ttl);
		this.lnki_type = type;
		this.lnki_w = w;
		this.lnki_h = h;
		return this;
	}
	public Xof_fsdb_arg_exec_get Rslt_reg_noop() {rslt_reg = Xof_reg_wkr_.Tid_noop; return this;}
	public Xof_fsdb_arg_exec_get Rslt_reg_missing_reg() {rslt_reg = Xof_reg_wkr_.Tid_missing_reg; return this;}
	public Xof_fsdb_arg_exec_get Rslt_reg_missing_qry() {rslt_reg = Xof_reg_wkr_.Tid_missing_qry; return this;}
	public Xof_fsdb_arg_exec_get Rslt_reg_missing_bin() {rslt_reg = Xof_reg_wkr_.Tid_missing_bin; return this;}
	public Xof_fsdb_arg_exec_get Rslt_reg_found_url() {rslt_reg = Xof_reg_wkr_.Tid_found_url; return this;}
	public Xof_fsdb_arg_exec_get Rslt_qry_mock_() {rslt_qry = Xof_qry_wkr_.Tid_mock; return this;}
	public Xof_fsdb_arg_exec_get Rslt_qry_noop_() {rslt_qry = Xof_qry_wkr_.Tid_noop; return this;}
	public Xof_fsdb_arg_exec_get Rslt_qry_unavailable_() {rslt_qry = Xof_qry_wkr_.Tid_missing; return this;}
	public Xof_fsdb_arg_exec_get Rslt_bin_fsdb_() {rslt_bin = Xof_bin_wkr_.Tid_fsdb; return this;}
	public Xof_fsdb_arg_exec_get Rslt_bin_not_found_() {rslt_bin = Xof_bin_wkr_.Tid_not_found; return this;}
	public Xof_fsdb_arg_exec_get Rslt_cnv_y_() {rslt_cnv = Xof_cnv_wkr_.Tid_y; return this;}
	public Xof_fsdb_arg_exec_get Rslt_cnv_n_() {rslt_cnv = Xof_cnv_wkr_.Tid_n; return this;}
	public static Xof_fsdb_arg_exec_get new_() {return new Xof_fsdb_arg_exec_get();} Xof_fsdb_arg_exec_get() {}
}
