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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.files.*;
public class Xob_xfer_temp_cmd extends Xob_itm_basic_base implements Xob_cmd {
	public Xob_xfer_temp_cmd(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.xfer_temp";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		Db_provider provider = Xodb_db_file.init__oimg_lnki(wiki).Provider();
		Xob_xfer_temp_tbl.Create_table(provider);
		Db_stmt trg_stmt = Xob_xfer_temp_tbl.Insert_stmt(provider);

		provider.Txn_mgr().Txn_bgn_if_none();
		DataRdr rdr = provider.Exec_sql_as_rdr(Sql_select);
		Xof_img_size img_size = new Xof_img_size();
		while (rdr.MoveNextPeer()) {
			byte lnki_ext = rdr.ReadByte(Xob_lnki_regy_tbl.Fld_olr_lnki_ext);
			if (Byte_.In(lnki_ext, Xof_ext_.Id_mid, Xof_ext_.Id_oga)) continue;	// audio will never have thumbnails
			int lnki_id = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_id);
			byte orig_repo = rdr.ReadByte(Xob_orig_regy_tbl.Fld_oor_orig_repo);
			int orig_page_id = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_page_id, -1);
			String orig_ttl = rdr.ReadStr(Xob_orig_regy_tbl.Fld_oor_orig_join_ttl);
			byte lnki_type = rdr.ReadByte(Xob_lnki_regy_tbl.Fld_olr_lnki_type);
			int lnki_w = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_w);
			int lnki_h = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_h);
			int lnki_count = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_count);
			double lnki_upright = rdr.ReadDouble(Xob_lnki_regy_tbl.Fld_olr_lnki_upright);
			double lnki_thumbtime = rdr.ReadDouble(Xob_lnki_regy_tbl.Fld_olr_lnki_thumbtime);
			int orig_w = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_w, -1);
			int orig_h = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_h, -1);
			String orig_media_type = rdr.ReadStr(Xob_orig_regy_tbl.Fld_oor_orig_media_type);
			if (orig_media_type == null) orig_media_type = "";
			img_size.Html_size_calc(Xof_exec_tid.Tid_wiki_page, lnki_w, lnki_h, lnki_type, lnki_upright, lnki_ext, orig_w, orig_h, Xof_img_size.Thumb_width_img);
			Xob_xfer_temp_tbl.Insert(trg_stmt, lnki_id, orig_repo, orig_page_id, orig_ttl, lnki_ext, lnki_type, orig_media_type, img_size.File_is_orig(), img_size.File_w(), img_size.Html_w(), img_size.Html_h(), lnki_thumbtime, lnki_count);
		}
		provider.Txn_mgr().Txn_end_all();
	}
	public void Cmd_run() {}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private static final String
		Sql_select = String_.Concat_lines_nl
	(	"SELECT  olr_lnki_id"
	,	",       olr_lnki_ttl"
	,	",       olr_lnki_ext"
	,	",       olr_lnki_type"
	,	",       olr_lnki_w"
	,	",       olr_lnki_h"
	,	",       olr_lnki_upright"
	,	",       olr_lnki_thumbtime"
	,	",       olr_lnki_count"
	,	",       oor_orig_repo"
	,	",       oor_orig_page_id"
	,	",       oor_orig_join_id"
	,	",       oor_orig_join_ttl"
	,	",       oor_orig_size"
	,	",       oor_orig_w"
	,	",       oor_orig_h"
	,	",       oor_orig_bits"
	,	",       oor_orig_media_type"
	,	"FROM    oimg_lnki_regy l"
	,	"        JOIN oimg_orig_regy f ON f.oor_lnki_ttl = l.olr_lnki_ttl"
	,	"ORDER BY f.oor_orig_join_ttl, l.olr_lnki_w DESC"
	);
}
