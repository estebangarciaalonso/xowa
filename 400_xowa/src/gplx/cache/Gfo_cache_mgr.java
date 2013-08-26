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
package gplx.cache; import gplx.*;
public class Gfo_cache_mgr {
	private OrderedHash hash = OrderedHash_.new_();
	public int Compress_max() {return compress_max;} public void Compress_max_(int v) {compress_max = v;} private int compress_max = 16;
	public int Compress_to() {return compress_to;} public void Compress_to_(int v) {compress_to = v;} private int compress_to = 8;
	public Object Get_val_or_null(Object key) {
		Object rv_obj = hash.Fetch(key);
		return rv_obj == null ? null : ((Gfo_cache_itm)rv_obj).Val();
	}
	public void Add(Object key, Object val) {
		if (hash.Count() >= compress_max) Compress(); 
		Gfo_cache_itm itm = new Gfo_cache_itm(key, val); 
		hash.Add(key, itm);
	}
	public void Compress() {
		hash.SortBy(Gfo_cache_itm_comparer.Touched_asc);
		int del_len = hash.Count() - compress_to;
		ListAdp del_list = ListAdp_.new_();
		for (int i = 0; i < del_len; i++) {
			Gfo_cache_itm itm = (Gfo_cache_itm)hash.FetchAt(i);
			del_list.Add(itm);
		}
		for (int i = 0; i < del_len; i++) {
			Gfo_cache_itm itm = (Gfo_cache_itm)del_list.FetchAt(i);
			hash.Del(itm.Key());
		}
	}
}
class Gfo_cache_itm {
	public Gfo_cache_itm(Object key, Object val) {this.key = key; this.val = val; this.Touched_update();}
	public Object Key() {return key;} private Object key;
	public Object Val() {return val;} private Object val;
	public long Touched() {return touched;} private long touched;
	public Gfo_cache_itm Touched_update() {touched = Env_.TickCount(); return this;}
}
class Gfo_cache_itm_comparer implements gplx.lists.ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		Gfo_cache_itm lhs = (Gfo_cache_itm)lhsObj;
		Gfo_cache_itm rhs = (Gfo_cache_itm)rhsObj;
		return Long_.Compare(lhs.Touched(), rhs.Touched());
	}
	public static final Gfo_cache_itm_comparer Touched_asc = new Gfo_cache_itm_comparer(); 
}
