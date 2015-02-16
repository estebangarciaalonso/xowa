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
import gplx.dbs.sqls.*;
public class Db_qry_sql implements Db_qry {
	public int			Tid() {return Db_qry_.Tid_sql;}
	public boolean			Exec_is_rdr() {return isReader;} private boolean isReader;
	public String		Base_table() {throw Err_.not_implemented_();}
	public String		Xto_sql() {return sql;} private String sql;		
	public int Exec_qry(Db_conn conn) {return conn.Exec_qry(this);}
	public static Db_qry_sql dml_(String sql) {return sql_(sql);}
	public static Db_qry_sql ddl_(String sql) {return sql_(sql);}
	public static Db_qry_sql xtn_(String sql) {return sql_(sql);}
	static Db_qry_sql sql_(String sql) {
		Db_qry_sql rv = new Db_qry_sql();
		rv.sql = sql; rv.isReader = false;
		return rv;
	}
	public static Db_qry_sql rdr_(String sql) {
		Db_qry_sql rv = new Db_qry_sql();
		rv.sql = sql; rv.isReader = true;
		return rv;
	}
	public static Db_qry_sql as_(Object obj) {return obj instanceof Db_qry_sql ? (Db_qry_sql)obj : null;}
	public static Db_qry_sql cast_(Object obj) {try {return (Db_qry_sql)obj;} catch(Exception exc) {throw Err_.type_mismatch_exc_(exc, Db_qry_sql.class, obj);}}
	public static String Gen_sql(Db_qry qry, Object... args) {
		byte[] src = Bry_.new_utf8_(Sql_qry_wtr_.Gen_placeholder_parameters(qry));
		int src_len = src.length;
		int args_idx = 0, args_len = args.length, pos = 0;
		Bry_bfr bfr = Bry_bfr.new_(src_len);
		while (pos < src_len) {
			int question_pos = Bry_finder.Find_fwd(src, Byte_ascii.Question, pos);
			if (question_pos == Bry_.NotFound)
				question_pos = src_len;
			bfr.Add_mid(src, pos, question_pos);
			if (args_idx < args_len)
				Gen_sql_arg(bfr, args[args_idx++]);
			pos = question_pos + 1;
		}
		return bfr.Xto_str_and_clear();
	}
	private static void Gen_sql_arg(Bry_bfr bfr, Object val) {
		if (val == null) {bfr.Add(Bry_null); return;}
		Class<?> val_type = val.getClass();
		if		(ClassAdp_.Eq(val_type, Int_.Cls_ref_type))
			bfr.Add_int_variable(Int_.cast_(val));
		else if	(ClassAdp_.Eq(val_type, Bool_.Cls_ref_type))
			bfr.Add_int_fixed(1, Bool_.Xto_int(Bool_.cast_(val)));	// NOTE: save boolean to 0 or 1, b/c (a) db may not support bit datatype (sqllite) and (b) avoid i18n issues with "true"/"false"
		else if (ClassAdp_.Eq(val_type, Double_.Cls_ref_type))
			bfr.Add_double(Double_.cast_(val));
		else if (ClassAdp_.Eq(val_type, Long_.Cls_ref_type))
			bfr.Add_long_variable(Long_.cast_(val));
		else if (ClassAdp_.Eq(val_type, Float_.Cls_ref_type))
			bfr.Add_float(Float_.cast_(val));
		else if (ClassAdp_.Eq(val_type, Byte_.Cls_ref_type))
			bfr.Add_byte(Byte_.cast_(val));
		else if (ClassAdp_.Eq(val_type, DateAdp_.Cls_ref_type))
			bfr.Add_byte_apos().Add_str(DateAdp_.cast_(val).XtoStr_gplx_long()).Add_byte_apos();
		else if (ClassAdp_.Eq(val_type, DecimalAdp_.Cls_ref_type))
			bfr.Add_str(DecimalAdp_.cast_(val).Xto_str());
		else {
			byte[] val_bry = Bry_.new_utf8_(Object_.Xto_str_strict_or_null(val));
			val_bry = Bry_.Replace(val_bry, Byte_ascii.Apos_bry, Bry_escape_apos);
			bfr.Add_byte_apos().Add(val_bry).Add_byte_apos();
		}
	}	private static final byte[] Bry_null = Bry_.new_utf8_("NULL"), Bry_escape_apos = Bry_.new_ascii_("''");
}
