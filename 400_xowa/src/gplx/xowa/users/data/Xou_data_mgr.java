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
package gplx.xowa.users.data; import gplx.*; import gplx.xowa.*; import gplx.xowa.users.*;
import gplx.dbs.*;
public class Xou_data_mgr {
	private Xoa_app app;		
	private Io_url data_url;
	private Xou_data_xtn_mgr xtn_mgr = new Xou_data_xtn_mgr();
	private ListAdp wkr_list = ListAdp_.new_();
	public Xou_data_mgr(Xoa_app app) {
		this.app = app;
	}
	public Db_provider Provider() {return provider;} private Db_provider provider;
	public void Wkr_reg(Xou_data_wkr wkr) {
		wkr_list.Add(wkr);
	}
	public void App_init() {
		data_url = app.User().Fsys_mgr().Root_dir().GenSubFil("data.sqlite3");
		BoolRef created_ref = BoolRef.false_();
		provider = Sqlite_engine_.Provider_load_or_make_(data_url, created_ref);
		xtn_mgr.Db_init(provider);
		if (created_ref.Val()) xtn_mgr.Db_when_created();
	}
	public void App_term() {
		provider.Rls();
	}
}
class Xou_data_xtn_mgr {
//		private Db_provider provider;
	public void Db_init(Db_provider provider) {}//this.provider = provider;}
	public void Db_when_created() {
		// create table
	}
}
class Xou_data_xtn_itm {
	public int Id() {return id;} private int id;
	public String Key() {return key;} private String key;
	public String Created_in() {return created_in;} private String created_in;
	public byte Cmd_mode() {return cmd_mode;} public Xou_data_xtn_itm Cmd_mode_(byte v) {cmd_mode = v; return this;} private byte cmd_mode;
	public Xou_data_xtn_itm Init_by_load(DataRdr rdr) {
		id = rdr.ReadInt(Xou_data_xtn_tbl.Fld_xtn_id);
		key = rdr.ReadStr(Xou_data_xtn_tbl.Fld_xtn_key);
		created_in = rdr.ReadStr(Xou_data_xtn_tbl.Fld_xtn_created_in);
		cmd_mode = Db_cmd_mode.Ignore;
		return this;
	}
	public Xou_data_xtn_itm Init_by_make(int id, String key, String created_in) {
		this.id = id;
		this.key = key;
		this.created_in = created_in;
		cmd_mode = Db_cmd_mode.Create;
		return this;
	}
        public static final Xou_data_xtn_itm Null = new Xou_data_xtn_itm(); public Xou_data_xtn_itm() {}
}
class Xou_data_xtn_tbl {
	private Db_provider provider;
//		private Db_stmt select_stmt;
	private Db_stmt_bldr stmt_bldr = new Db_stmt_bldr(Tbl_name, String_.Ary(Fld_xtn_id), Fld_xtn_key, Fld_xtn_created_in);
	public void Db_init(Db_provider provider) {this.provider = provider;}
	public void Db_save(Xou_data_xtn_itm itm) {
		if (stmt_bldr == null) stmt_bldr.Init(provider);
		Db_stmt stmt = stmt_bldr.Get(itm.Cmd_mode());
		switch (itm.Cmd_mode()) {
			case Db_cmd_mode.Create:	stmt.Clear().Val_int_(itm.Id())	.Val_str_(itm.Key()).Val_str_(itm.Created_in()).Exec_insert(); break;
			case Db_cmd_mode.Update:	stmt.Clear()					.Val_str_(itm.Key()).Val_str_(itm.Created_in()).Exec_update(); break;
			case Db_cmd_mode.Delete:	stmt.Clear().Val_int_(itm.Id()).Exec_delete();	break;
			case Db_cmd_mode.Ignore:	break;
			default:					throw Err_.unhandled(itm.Cmd_mode());
		}
		itm.Cmd_mode_(Db_cmd_mode.Ignore);
	}
	public void Db_term() {
//			if (select_stmt != null) select_stmt.Rls();
		if (stmt_bldr != null) stmt_bldr.Rls();
	}
	public void Db_when_new() {
		Sqlite_engine_.Tbl_create(provider, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(provider, Idx_key);
	}
//		public Xou_data Select(String name) {
//			if (select_stmt == null) select_stmt = Db_stmt_.new_select_(provider, Tbl_name, String_.Ary(Fld_dir_name));
//			DataRdr rdr = DataRdr_.Null;
//			try {
//				rdr = select_stmt.Clear()
//				.Val_str_(name)
//				.Exec_select();
//				if (rdr.MoveNextPeer()) {
//					return new Cache_dir_itm().Init_by_load(rdr);
//				}
//				else
//					return Cache_dir_itm.Null;
//			}
//			catch (Exception e) {select_stmt = null; throw Err_.err_(e, "stmt failed");}
//			finally {rdr.Rls();}
//		}		
	private static final String Tbl_sql = String_.Concat_lines_nl
	( "CREATE TABLE xowa_xtn"
	, "( xtn_id            integer       NOT NULL        PRIMARY KEY"
	, ", xtn_key           varchar(255)"
	, ", xtn_created_in    varchar(255)"
	, ");"
	);
	public static final String Tbl_name = "xowa_xtn"
	, Fld_xtn_id = "xtn_id", Fld_xtn_key = "xtn_key", Fld_xtn_created_in = "xtn_created_in"
	;
	private static final Db_idx_itm
		Idx_key     		= Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS xowa_xtn__key ON xowa_xtn (xtn_key);")
	;
}
