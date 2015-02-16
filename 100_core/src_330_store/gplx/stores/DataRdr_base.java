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
package gplx.stores; import gplx.*;
import gplx.core.strings.*;
public abstract class DataRdr_base implements SrlMgr {
	public boolean Parse() {return parse;} public void Parse_set(boolean v) {parse = v;} private boolean parse;
	public Io_url Uri() {return uri;} public void Uri_set(Io_url s) {uri = s;} Io_url uri = Io_url_.Null;
	public abstract String NameOfNode();
	public boolean Type_rdr() {return true;}
	public HashAdp EnvVars() {return envVars;} HashAdp envVars = HashAdp_.new_();
	public abstract Object Read(String key);
	public abstract int FieldCount();
	public abstract String KeyAt(int i);
	public abstract Object ReadAt(int i);
	@gplx.Virtual public KeyVal KeyValAt(int idx)	{return KeyVal_.new_(this.KeyAt(idx), ReadAt(idx));}
	public String ReadStr(String key) {
		Object val = Read(key);
		try {return (String)val;} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(String.class, key, val, exc);}
	}
	public String ReadStrOr(String key, String or) {
		Object val = Read(key); if (val == null) return or;
		try {return (String)val;} 
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, String.class, key, val, or); return or;}
	}
	public byte[] ReadBryByStr(String key) {return Bry_.new_utf8_(ReadStr(key));}	
	public byte[] ReadBryByStrOr(String key, byte[] or) {
		Object val = Read(key); if (val == null) return or;
		try {return Bry_.new_utf8_((String)val);}
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, byte[].class, key, val, or); return or;}
	}
	@gplx.Virtual public void SrlList(String key, ListAdp list, SrlObj proto, String itmKey) {
		list.Clear();
		DataRdr subRdr = this.Subs_byName_moveFirst(key); // collection node
		subRdr = subRdr.Subs();
		while (subRdr.MoveNextPeer()) {
			SrlObj itm = proto.SrlObj_New(null);
			itm.SrlObj_Srl(subRdr);
			list.Add(itm);
		}
	}
	@gplx.Virtual public Object StoreRoot(SrlObj root, String key) {
		SrlObj clone = root.SrlObj_New(null);
		clone.SrlObj_Srl(this);
		return clone;
	}
	public abstract DataRdr Subs_byName_moveFirst(String name);

	public int ReadInt(String key) {
		Object val = Read(key);
		try {return (parse) ? Int_.parse_(String_.as_(val)) : Int_.cast_(val);} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(int.class, key, val, exc);}
	}
	public int ReadIntOr(String key, int or) {
		Object val = Read(key); if (val == null) return or;
		try {return (parse) ? Int_.parse_(String_.as_(val)) : Int_.cast_(val);} 
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, int.class, key, val, or); return or;}
	}
	public long ReadLongOr(String key, long or) {
		Object val = Read(key); if (val == null) return or;
		try {return (parse) ? Long_.parse_(String_.as_(val)) : Long_.cast_(val);} 
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, long.class, key, val, or); return or;}
	}
	@gplx.Virtual public boolean ReadBool(String key) {
		Object val = Read(key);
		try {return (parse) ? Bool_.cast_(BoolClassXtn._.ParseOrNull(String_.as_(val))) : Bool_.cast_(val);} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(boolean.class, key, val, exc);}
	}
	@gplx.Virtual public boolean ReadBoolOr(String key, boolean or) {
		Object val = Read(key); if (val == null) return or;
		try {return (parse) ? Bool_.parse_(String_.as_(val)) : Bool_.cast_(val);} 
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, boolean.class, key, val, or); return or;}
	}
	public long ReadLong(String key) {
		Object val = Read(key);
		try {return (parse) ? Long_.parse_(String_.as_(val)) : Long_.cast_(val);} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(long.class, key, val, exc);}
	}
	public float ReadFloat(String key) {
		Object val = Read(key);
		try {return (parse) ? Float_.parse_(String_.as_(val)) : Float_.cast_(val);} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(float.class, key, val, exc);}
	}
	public float ReadFloatOr(String key, float or) {
		Object val = Read(key); if (val == null) return or;
		try {return (parse) ? Float_.parse_(String_.as_(val)) : Float_.cast_(val);} 
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, float.class, key, val, or); return or;}
	}
	public double ReadDouble(String key) {
		Object val = Read(key);
		try {return (parse) ? Double_.parse_(String_.as_(val)) : Double_.cast_(val);} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(double.class, key, val, exc);}
	}
	public double ReadDoubleOr(String key, double or) {
		Object val = Read(key); if (val == null) return or;
		try {return (parse) ? Double_.parse_(String_.as_(val)) : Double_.cast_(val);} 
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, double.class, key, val, or); return or;}
	}
	@gplx.Virtual public byte ReadByte(String key) {
		Object val = Read(key);
		try {return (parse) ? Byte_.parse_(String_.as_(val)) : Byte_.cast_(val);} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(byte.class, key, val, exc);}
	}
	@gplx.Virtual public byte ReadByteOr(String key, byte or) {
		Object val = Read(key); if (val == null) return or;
		try {return (parse) ? Byte_.parse_(String_.as_(val)) : Byte_.cast_(val);} 
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, byte.class, key, val, or); return or;}
	}
	@gplx.Virtual public DateAdp ReadDate(String key) {
		Object val = Read(key);
		try {return (parse) ? DateAdp_.parse_gplx(String_.as_(val)) : (DateAdp)val;} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(DateAdp.class, key, val, exc);}
	}
	@gplx.Virtual public DateAdp ReadDateOr(String key, DateAdp or) {
		Object val = Read(key); if (val == null) return or;
		try {return (parse) ? DateAdp_.parse_gplx(String_.as_(val)) : (DateAdp)val;} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(DateAdp.class, key, val, exc);}
	}
	@gplx.Virtual public DecimalAdp ReadDecimal(String key) {
		Object val = Read(key);
		try {
			if (parse) return DecimalAdp_.parse_(String_.as_(val));
			DecimalAdp rv = DecimalAdp_.as_(val);
			return (rv == null) 
				? DecimalAdp_.db_(val)	// HACK: GfoNde_.rdr_ will call ReadAt(int i) on Db_data_rdr; since no Db_data_rdr knows about DecimalAdp, it will always return decimalType
				: rv;
		}
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(DecimalAdp.class, key, val, exc);}
	}
	@gplx.Virtual public DecimalAdp ReadDecimalOr(String key, DecimalAdp or) {
		Object val = Read(key); if (val == null) return or;
		try {
			if (parse) return DecimalAdp_.parse_(String_.as_(val));
			DecimalAdp rv = DecimalAdp_.as_(val);
			return (rv == null) 
				? DecimalAdp_.db_(val)	// HACK: GfoNde_.rdr_ will call ReadAt(int i) on Db_data_rdr; since no Db_data_rdr knows about DecimalAdp, it will always return decimalType
				: rv;
		}
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(DecimalAdp.class, key, val, exc);}
	}
	public char ReadChar(String key) {
		Object val = Read(key);
		try {
			if (parse) return Char_.parse_(String_.as_(val));
			return Char_.cast_(val);
		}
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(char.class, key, val, exc);}
	}
	public char ReadCharOr(String key, char or) {
		Object val = Read(key); if (val == null) return or;
		try {
			if (parse) return Char_.parse_(String_.as_(val));
			return Char_.cast_(val);
		}
		catch (Exception exc) {Err_.Noop(exc); return or;}
	}
	public byte[] ReadBry(String key) {
		Object val = Read(key);
		try {return (byte[])val;} 
		catch (Exception exc) {throw Err_dataRdr_ReadFailed_err(byte[].class, key, val, exc);}
	}
	public byte[] ReadBryOr(String key, byte[] or) {
		Object val = Read(key); if (val == null) return or;
		try {return (byte[])val;} 
		catch (Exception exc) {Err_dataRdr_ReadFailed_useOr(exc, byte[].class, key, val, or); return or;}
	}
	public gplx.ios.Io_stream_rdr ReadRdr(String key) {return gplx.ios.Io_stream_rdr_.Null;}
	public boolean SrlBoolOr(String key, boolean or) {return ReadBoolOr(key, or);}
	public byte SrlByteOr(String key, byte or) {return ReadByteOr(key, or);}
	public int SrlIntOr(String key, int or) {return ReadIntOr(key, or);}
	public long SrlLongOr(String key, long or) {return ReadLongOr(key, or);}
	public String SrlStrOr(String key, String or) {return ReadStrOr(key, or);}
	public DateAdp SrlDateOr(String key, DateAdp or) {return ReadDateOr(key, or);}
	public DecimalAdp SrlDecimalOr(String key, DecimalAdp or) {return ReadDecimalOr(key, or);}
	public double SrlDoubleOr(String key, double or) {return ReadDoubleOr(key, or);}
	public Object SrlObjOr(String key, Object or) {throw Err_.not_implemented_();}
	public void XtoStr_gfml(String_bldr sb) {
		sb.Add(this.NameOfNode()).Add(":");
		for (int i = 0; i < this.FieldCount(); i++) {
			KeyVal kv = this.KeyValAt(i);
			if (i != 0) sb.Add(" ");
			sb.Add_fmt("{0}='{1}'", kv.Key(), String_.Replace(kv.Val_to_str_or_empty(), "'", "\""));
		}
		sb.Add(";");
	}
	public abstract DataRdr Subs();
	public void TypeKey_(String v) {}
	public abstract SrlMgr SrlMgr_new(Object o);
	static Err Err_dataRdr_ReadFailed_err(Class<?> type, String key, Object val, Exception inner) {
		String innerMsg = inner == null ? "" : Err_.Message_lang(inner);
		return Err_.new_key_("DataRdr_ReadFailed", "failed to read data").Add("key", key).Add("val", val).Add("type", type).Add("innerMsg", innerMsg);
	}
	static void Err_dataRdr_ReadFailed_useOr(Class<?> type, String key, Object val, Object or) {
		UsrDlg_._.Warn(UsrMsg.new_("failed to read data; substituting default").Add("key", key).Add("val", val).Add("default", or).Add("type", type));
	}
	static void Err_dataRdr_ReadFailed_useOr(Exception exc, Class<?> type, String key, Object val, Object or) {
		UsrDlg_._.Warn(UsrMsg.new_("failed to read data; substituting default").Add("key", key).Add("val", val).Add("default", or).Add("type", type));
	}
}
