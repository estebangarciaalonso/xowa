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
public class Xob_xfer_temp_cmd_thumb extends Xob_itm_basic_base implements Xob_cmd {
	public Xob_xfer_temp_cmd_thumb(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_ctor(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.xfer_temp_thumb";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		Db_provider provider = Xodb_db_file.init__oimg_lnki(wiki).Provider();
		Xob_xfer_temp_tbl.Create_table(provider);
		Db_stmt trg_stmt = Xob_xfer_temp_tbl.Insert_stmt(provider);
		provider.Txn_mgr().Txn_bgn_if_none();
		DataRdr rdr = provider.Exec_sql_as_rdr(Sql_select);
		Xob_xfer_temp_itm temp_itm = new Xob_xfer_temp_itm();
		Xof_img_size img_size = new Xof_img_size();
		while (rdr.MoveNextPeer()) {
			temp_itm.Clear();
			temp_itm.Load(rdr);
			if (temp_itm.Chk(img_size))
				temp_itm.Insert(trg_stmt, img_size);
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
	,	",       olr_lnki_page_id"
	,	",       oor_orig_repo"
	,	",       oor_orig_page_id"
	,	",       oor_orig_join_id"
	,	",       oor_orig_join_ttl"
	,	",       oor_lnki_ttl"
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
