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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*;
public class Xob_dump_mgr_xfer_regy extends Xob_itm_basic_base implements Xob_cmd {
	public Xob_dump_mgr_xfer_regy(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.xfer_regy";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {}
	public void Cmd_run() {
		wiki.Init_assert();
		Xodb_fsys_mgr fsys_mgr = wiki.Db_mgr_as_sql().Fsys_mgr(); 
		Xodb_file db_file = fsys_mgr.Get_or_make("oimg_lnki");
		Db_provider provider = db_file.Provider();
		Xodb_tbl_oimg_xfer_temp xfer_temp = new Xodb_tbl_oimg_xfer_temp().Create_table(provider);
		Db_stmt trg_stmt = xfer_temp.Insert_stmt(provider);

		provider.Txn_mgr().Txn_bgn_if_none();
		DataRdr rdr = provider.Exec_sql_as_rdr(Sql_select);
		int thumb_w_img_const = Xof_img_mgr.Thumb_w_img_const;
		Int_2_ref actl_size = new Int_2_ref();
		while (rdr.MoveNextPeer()) {
			byte ext_id = rdr.ReadByte("olr_ext_id");
			if (Byte_.In(ext_id, Xof_ext_.Id_mid, Xof_ext_.Id_oga)) continue;	// audio will never have thumbnails
			int lnki_id = rdr.ReadInt("olr_id");
			byte wiki_id = rdr.ReadByte("ofr_wiki_id");
			int page_id = rdr.ReadIntOr("ofr_page_id", -1);
			String ttl = rdr.ReadStr("olr_ttl");
			byte lnki_type = rdr.ReadByte("olr_type");
			int lnki_w = rdr.ReadInt("olr_width");
			int lnki_h = rdr.ReadInt("olr_height");
			double upright = rdr.ReadDouble("olr_upright");
			double time = rdr.ReadDouble("olr_time");
			int file_w = rdr.ReadIntOr("ofr_width", -1);
			int file_h = rdr.ReadIntOr("ofr_height", -1);
			boolean lnki_thumb = Xof_xfer_itm.Lnki_thumbable_calc(lnki_type, lnki_w, lnki_h);
			Xof_xfer_itm_.Calc_xfer_size(actl_size, thumb_w_img_const, file_w, file_h, lnki_w, lnki_h, lnki_thumb, upright);
			xfer_temp.Insert(trg_stmt, lnki_id, wiki_id, page_id, ttl, ext_id, actl_size.Val_0(), actl_size.Val_1(), time);
		}
		Xodb_tbl_oimg_xfer_regy xfer_regy = new Xodb_tbl_oimg_xfer_regy().Create_table(provider);
		xfer_regy.Create_data(usr_dlg, provider);
		xfer_regy.Create_indexes(usr_dlg, provider);
		provider.Txn_mgr().Txn_end_all();
	}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private static final String
		Sql_select = String_.Concat_lines_nl
	(	"SELECT  olr_id"
	,	",       olr_ttl"
	,	",       olr_ext_id"
	,	",       olr_type"
	,	",       olr_width"
	,	",       olr_height"
	,	",       olr_upright"
	,	",       olr_time"
	,	",       ofr_wiki_id"
	,	",       ofr_page_id"
	,	",       ofr_join_id"
	,	",       ofr_join_ttl"
	,	",       ofr_size"
	,	",       ofr_width"
	,	",       ofr_height"
	,	",       ofr_bits"
	,	"FROM    oimg_file_regy f"
	,	"        JOIN oimg_lnki_regy l ON f.ofr_join_ttl = l.olr_ttl"
	,	"ORDER BY f.ofr_join_ttl, l.olr_width DESC"
	);
}
