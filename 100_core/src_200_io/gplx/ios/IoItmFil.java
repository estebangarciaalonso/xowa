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
package gplx.ios; import gplx.*;
public class IoItmFil extends IoItm_base {
	@Override public int TypeId() {return IoItmFil.Type_Fil;} @Override public boolean Type_dir() {return false;} @Override public boolean Type_fil() {return true;} public static final int Type_Fil = 2;
	public boolean Exists() {return size != Size_Invalid;}	// NOTE: questionable logic, but preserved for historical reasons; requires that length be set to -1 if !.exists
	public DateAdp ModifiedTime() {return modifiedTime;}
	public IoItmFil ModifiedTime_(DateAdp val) {modifiedTime = val; return this;} DateAdp modifiedTime;
	public IoItmFil ModifiedTime_(String val) {return ModifiedTime_(DateAdp_.parse_gplx(val));}
	@gplx.Virtual public long Size() {return size;} public IoItmFil Size_(long val) {size = val; return this;} long size;
	public IoItmAttrib Attrib() {return attrib;} public IoItmFil Attrib_(IoItmAttrib val) {attrib = val; return this;} IoItmAttrib attrib = IoItmAttrib.normal_();
	public boolean ReadOnly() {return attrib.ReadOnly();} public IoItmFil ReadOnly_(boolean val) {attrib.ReadOnly_(val); return this;} 
	@gplx.New public IoItmFil XtnProps_set(String key, Object val) {return (IoItmFil)super.XtnProps_set(key, val);}
	
	@gplx.Internal protected IoItmFil ctor_IoItmFil(Io_url url, long size, DateAdp modifiedTime) {
		ctor_IoItmBase_url(url); this.size = size; this.modifiedTime = modifiedTime;
		return this;
	}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, IoItmFil_.Prop_Size))	return size;
		else if	(ctx.Match(k, IoItmFil_.Prop_Modified))	return modifiedTime;
		else return super.Invk(ctx, ikey, k, m);
	}
	@gplx.Internal protected IoItmFil() {}
	public static final long Size_Invalid		= -1;
	public static final int  Size_Invalid_int	= -1;
}
