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
package gplx.dbs.qrys; import gplx.*; import gplx.dbs.*;
import gplx.dbs.engines.*;
public class Db_stmt_sql implements Db_stmt {// used for formatting SQL statements; not used for actual insert into database
	private static final String Key_na = ""; // key is not_available; only called by procs with signature of Val(<type> v);
	private final ListAdp args = ListAdp_.new_();
	private final Bry_bfr tmp_bfr = Bry_bfr.new_();
	private final Bry_fmtr tmp_fmtr = Bry_fmtr.new_();
	public void Ctor_stmt(Db_engine engine, Db_qry qry) {}
	public Db_conn Conn() {return conn;} public void Conn_(Db_conn v) {this.conn = v;} Db_conn conn;
	public Db_stmt Reset_stmt() {return this;}
	public Db_stmt Crt_bool_as_byte(String k, boolean v)	{return Add_byte_by_bool(Bool_.Y, k, v);}
	public Db_stmt Val_bool_as_byte(String k, boolean v)	{return Add_byte_by_bool(Bool_.N, k, v);}
	public Db_stmt Val_bool_as_byte(boolean v)				{return Add_byte_by_bool(Bool_.N, Key_na, v);}
	private Db_stmt Add_byte_by_bool(boolean where, String k, boolean v) {return Add_byte(where, k, v ? Bool_.Y_byte : Bool_.N_byte);}
	public Db_stmt Crt_byte(String k, byte v)	{return Add_byte(Bool_.Y, k, v);}
	public Db_stmt Val_byte(String k, byte v)	{return Add_byte(Bool_.N, k, v);}
	public Db_stmt Val_byte(byte v)				{return Add_byte(Bool_.N, Key_na, v);}
	private Db_stmt Add_byte(boolean where, String k, byte v) {
		try {Add(k, Byte_.Xto_str(v));} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "byte", v);}
		return this;
	}
	public Db_stmt Crt_int(String k, int v)	{return Add_int(Bool_.Y, k, v);}
	public Db_stmt Val_int(String k, int v)	{return Add_int(Bool_.N, k, v);}
	public Db_stmt Val_int(int v)			{return Add_int(Bool_.N, Key_na, v);}
	private Db_stmt Add_int(boolean where, String k, int v) {
		try {Add(k, Int_.Xto_str(v));} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "int", v);}
		return this;
	}
	public Db_stmt Crt_long(String k, long v)	{return Add_long(Bool_.Y, k, v);}
	public Db_stmt Val_long(String k, long v)	{return Add_long(Bool_.N, k, v);}
	public Db_stmt Val_long(long v)				{return Add_long(Bool_.N, Key_na, v);}
	private Db_stmt Add_long(boolean where, String k, long v) {
		try {Add(k, Long_.Xto_str(v));} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "long", v);} 
		return this;
	}
	public Db_stmt Crt_float(String k, float v)	{return Add_float(Bool_.Y, k, v);}
	public Db_stmt Val_float(String k, float v)	{return Add_float(Bool_.N, k, v);}
	public Db_stmt Val_float(float v)			{return Add_float(Bool_.N, Key_na, v);}
	private Db_stmt Add_float(boolean where, String k, float v) {
		try {Add(k, Float_.Xto_str(v));} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "float", v);}
		return this;
	}
	public Db_stmt Crt_double(String k, double v)	{return Add_double(Bool_.Y, k, v);}
	public Db_stmt Val_double(String k, double v)	{return Add_double(Bool_.N, k, v);}
	public Db_stmt Val_double(double v)				{return Add_double(Bool_.N, Key_na, v);}
	private Db_stmt Add_double(boolean where, String k, double v) {
		try {Add(k, Double_.Xto_str(v));} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "double", v);} 
		return this;
	}
	public Db_stmt Crt_decimal(String k, DecimalAdp v)	{return Add_decimal(Bool_.Y, k, v);}
	public Db_stmt Val_decimal(String k, DecimalAdp v)	{return Add_decimal(Bool_.N, k, v);}
	public Db_stmt Val_decimal(DecimalAdp v)			{return Add_decimal(Bool_.N, Key_na, v);}
	private Db_stmt Add_decimal(boolean where, String k, DecimalAdp v) {
		try {Add(k, v.Xto_str());} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "decimal", v);} 
		return this;
	}
	public Db_stmt Crt_bry(String k, byte[] v)	{return Add_bry(Bool_.Y, k, v);}
	public Db_stmt Val_bry(String k, byte[] v)	{return Add_bry(Bool_.N, k, v);}
	public Db_stmt Val_bry(byte[] v)			{return Add_bry(Bool_.N, Key_na, v);}
	private Db_stmt Add_bry(boolean where, String k, byte[] v) {// HACK: convert to String b/c tdb does not support byte[]
		try {Add(k, Val_str_wrap(String_.new_utf8_(v)));} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "byte[]", v.length);} 
		return this;
	}
	public Db_stmt Crt_bry_as_str(String k, byte[] v)	{return Add_bry_as_str(Bool_.Y, k, v);}
	public Db_stmt Val_bry_as_str(String k, byte[] v)	{return Add_bry_as_str(Bool_.N, k, v);}
	public Db_stmt Val_bry_as_str(byte[] v)				{return Add_bry_as_str(Bool_.N, Key_na, v);}
	private Db_stmt Add_bry_as_str(boolean where, String k, byte[] v) {return Add_str(where, k, String_.new_utf8_(v));}
	public Db_stmt Crt_str(String k, String v)	{return Add_str(Bool_.Y, k, v);}
	public Db_stmt Val_str(String k, String v)	{return Add_str(Bool_.N, k, v);}
	public Db_stmt Val_str(String v)			{return Add_str(Bool_.N, Key_na, v);}
	private Db_stmt Add_str(boolean where, String k, String v) {
		try {Add(k, Val_str_wrap(v));} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "String", v);} 
		return this;
	}
	public Db_stmt Val_rdr_(gplx.ios.Io_stream_rdr v, long rdr_len) {
		try {
			Bry_bfr bfr = Bry_bfr.new_();
			gplx.ios.Io_stream_rdr_.Load_all_to_bfr(bfr, v);
			Add(Key_na, bfr.Xto_str_and_clear());
		} catch (Exception e) {throw Err_.err_(e, "failed to add value: type={0} val={1}", "rdr", v);} 
		return this;
	}
	private String Val_str_wrap(String v) {
		return "'" + String_.Replace(v, "'", "\\'") + "'";
	}
	public boolean Exec_insert() {
		try {boolean rv = conn.Exec_qry(Db_qry_sql.dml_(this.Xto_sql())) != 0; return rv;} catch (Exception e) {throw Err_.err_(e, "failed to exec prepared statement: sql={0}", sql_orig);}
	}
	public int Exec_update() {
		try {int rv = conn.Exec_qry(Db_qry_sql.dml_(this.Xto_sql())); return rv;} catch (Exception e) {throw Err_.err_(e, "failed to exec prepared statement: sql={0}", sql_orig);}
	}
	public int Exec_delete() {
		try {int rv = conn.Exec_qry(Db_qry_sql.dml_(this.Xto_sql())); return rv;} catch (Exception e) {throw Err_.err_(e, "failed to exec prepared statement: sql={0}", sql_orig);}
	}
	public DataRdr Exec_select() {
		try {DataRdr rv = conn.Exec_qry_as_rdr(Db_qry_sql.rdr_(this.Xto_sql())); return rv;} catch (Exception e) {throw Err_.err_(e, "failed to exec prepared statement: sql={0}", sql_orig);}
	}	
	public Db_rdr Exec_select_as_rdr() {throw Err_.not_implemented_();}	
	public Object Exec_select_val() {
		try {Object rv = Db_qry_select.Rdr_to_val(this.Exec_select()); return rv;} catch (Exception e) {throw Err_.err_(e, "failed to exec prepared statement: sql={0}", sql_orig);}
	}
	public Db_stmt Clear() {
		args.Clear();
		return this;
	}
	public void Rls() {this.Clear();}
	public void Add(String k, String v) {
		if (k == Db_meta_fld.Key_null) return;	// key is explicitly null; ignore; allows version_2+ type definitions
		args.Add(v);
	}
	public String Xto_sql() {
		tmp_fmtr.Bld_bfr_many(tmp_bfr, (Object[])args.Xto_ary_and_clear(Object.class));
		return tmp_bfr.Xto_str_and_clear();
	}
	public int Args_len() {return args.Count();}
	public String Args_get_at(int i) {return (String)args.FetchAt(i);}
	private String sql_orig; 
	public Db_qry Qry() {return qry;} private Db_qry qry;
	public Db_stmt Parse(Db_qry qry, String sql_str) {
		this.qry = qry;
		this.sql_orig = sql_str;
		int arg_idx = 0;
		byte[] src = Bry_.new_utf8_(sql_str);
		int pos_prv = 0;
		tmp_bfr.Clear();
		while (true) {
			int pos_cur = Bry_finder.Find_fwd(src, Byte_ascii.Question, pos_prv);
			if (pos_cur == Bry_.NotFound) break;
			tmp_bfr.Add_mid(src, pos_prv, pos_cur);
			tmp_bfr.Add_byte(Byte_ascii.Tilde).Add_byte(Byte_ascii.Curly_bgn);
			tmp_bfr.Add_int_variable(arg_idx++);
			tmp_bfr.Add_byte(Byte_ascii.Curly_end);
			pos_prv = pos_cur + 1;
		}
		tmp_bfr.Add_mid(src, pos_prv, src.length);
		tmp_fmtr.Fmt_(tmp_bfr.Xto_bry_and_clear());
		return this;
	} 
}
