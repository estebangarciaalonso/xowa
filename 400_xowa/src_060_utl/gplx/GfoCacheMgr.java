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
package gplx;
public class GfoCacheMgr {
	public int MaxSize() {return maxSize;} public GfoCacheMgr MaxSize_(int v) {maxSize = v; return this;} private int maxSize;
	public int ReduceBy() {return reduceBy;} public GfoCacheMgr ReduceBy_(int v) {reduceBy = v; return this;} private int reduceBy;
	public int CurSize() {return curSize;} private int curSize;
	public int Count() {return hash.Count();}
	public void Clear() {hash.Clear(); recent.Clear(); curSize = 0;}
	@gplx.Internal protected Object GetAt(int i) {
		GfoCacheItm rv = (GfoCacheItm)hash.FetchAt(i);;
		return rv.Val();
	}
	public Object GetVal(byte[] key) {
		Object o = hash.Fetch(key); if (o == null) return null;
		GfoCacheItm rv = (GfoCacheItm)o;
		rv.Timestamp_update();
		Object recent_itm = recent.Fetch(key);
		if (recent_itm == null) recent.Add(key, rv);
		return rv.Val();
	}
	OrderedHash recent = OrderedHash_.new_bry_();
	public void Del(byte[] key) {
		Object o = hash.Fetch(key); if (o == null) return;
		GfoCacheItm itm = (GfoCacheItm)o;
		curSize -= itm.Size();
		hash.Del(itm.Key());
		itm.Rls();
	}
	public void AddReplace(byte[] key, RlsAble val, int size) {
//			Del(key);
//			Add(key, val, size);
		Object o = hash.Fetch(key);
		if (o == null)
			Add(key, val, size);
		else {
			GfoCacheItm itm = (GfoCacheItm)o;
			curSize -= itm.Size();
			curSize += size;
			itm.Replace(val, size);
		}
	}
	public void Add(byte[] key, RlsAble val, int size) {
//			if (curSize + size > 600000000) ReduceCache();
		curSize += size;
//			++curSize;
		GfoCacheItm itm = new GfoCacheItm(key, val, size);
		hash.Add(key, itm);
	}
	public void ReduceRecent() {
//			ConsoleAdp._.WriteLine("reducing");
		int len = recent.Count();
		for (int i = 0; i < len; i++) {
			GfoCacheItm itm = (GfoCacheItm)recent.FetchAt(i);
			itm.Rls();	// releases root
		}
		recent.Clear();
	}
	public void ReduceCache() {
		ConsoleAdp._.WriteLine("compacting:");			
//			hash.Sort();
//			int len = hash.Count();
//			ListAdp deleted = ListAdp_.new_();
//			int deleted_size = 0, deleted_count = 0;
//			for (int i = 0; i < len; i++) {
//				GfoCacheItm itm = (GfoCacheItm)hash.FetchAt(i);
//				int new_deleted_size = deleted_size + 1;//itm.Size();
//				if (new_deleted_size > 4000 && deleted_count > 0) break;
//				deleted.Add(itm);
//				deleted_count++;
//				deleted_size = new_deleted_size;
//			}
//			len = deleted.Count();
//			for (int i = 0; i < len; i++) {
//				GfoCacheItm itm = (GfoCacheItm)deleted.FetchAt(i);
//				curSize --;
//				hash.Del(bry_ref.Val_(itm.Key()));
//				itm.Rls();
//			}
//			deleted.Clear();

		int len = hash.Count();
		for (int i = 0; i < len; i++) {
			GfoCacheItm itm = (GfoCacheItm)hash.FetchAt(i);
//				hash.Del(bry_ref.Val_(itm.Key()));
			itm.Rls();
		}
	}
	OrderedHash hash = OrderedHash_.new_bry_();
}
class GfoCacheItm implements gplx.CompareAble, RlsAble {
	public byte[] Key() {return key;} private byte[] key;
	public RlsAble Val() {return val;} RlsAble val;
	public int Size() {return size;} private int size;
	public void Replace(RlsAble val, int size) {this.val = val; this.size = size;}
	public long Timestamp() {return timestamp;} public void Timestamp_update() {timestamp = Env_.TickCount();} long timestamp;
	public int compareTo(Object obj) {
		GfoCacheItm comp = (GfoCacheItm)obj;
		return Long_.Compare(timestamp, comp.timestamp);
	}
	public void Rls() {
		val.Rls();
//			val = null;
//			key = null;
	}
	public GfoCacheItm(byte[] key, RlsAble val, int size) {this.key = key; this.val = val; this.size = size; this.timestamp = Env_.TickCount();}
}
