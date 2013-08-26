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
import gplx.intl.*;
public class Xot_tmpl_regy {
	public Xot_tmpl_regy(Xol_lang lang) {this.lang = lang;} private Xol_lang lang;
	public void Add(Xot_defn defn, boolean caseAny) {
		byte[] name = defn.Name();
		int cacheSize = defn.CacheSize(); // * 2 b/c it has src and root; 
		regy.AddReplace(name, defn, cacheSize);
		if (caseAny) {
			name = lang.Case_mgr().Case_build_lower(name, 0, name.length);
			regy.AddReplace(name, defn, 0);
		}
	}
	public Xot_defn GetByKey(byte[] name) {return (Xot_defn)regy.GetVal(name);}
	public void Clear() {regy.Clear();}
	public void ReduceCache() {regy.ReduceRecent();}
	GfoCacheMgr regy = new GfoCacheMgr().MaxSize_(64 * 1024 * 1024).ReduceBy_(32 * 1024 * 1024);
}
