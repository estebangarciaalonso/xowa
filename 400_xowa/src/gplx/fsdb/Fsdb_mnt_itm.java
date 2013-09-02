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
package gplx.fsdb; import gplx.*;
import gplx.dbs.*; import gplx.xowa.*; import gplx.xowa.files.fsdb.*;
public class Fsdb_mnt_itm {
	private Fsdb_vlm_db_core core_db = new Fsdb_vlm_db_core();
	private Fsdb_vlm_db_data[] vlms; private int vlms_len;
	private OrderedHash[] retrieve_lists;  private Fsdb_vlm_db_data vlm_temp = new Fsdb_vlm_db_data();
	public String Key() {return key;} public void Key_(String v) {this.key = v;} private String key;
	public String Url() {return url;} public void Url_(String v) {this.url = v;} private String url;
	public Fsdb_mnt_itm Init(String key, String url) {this.key = key; this.url = url; return this;}
	public void Init_vlm_mgr(Io_url root_dir) {
		core_db.Init_core_db(root_dir.GenSubFil("core.sqlite3"));
		vlms = core_db.Vlms();
		this.vlms_len = vlms.length;
		retrieve_lists = retrieve_lists_new(vlms_len);
	}
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int width, int thumbtime, boolean is_orig, int height, DateAdp modified, String sha1, byte[] bin) {
		byte[] path = path_bfr.Add(dir).Add_byte(Dir_spr).Add(fil).XtoAryAndClear();
		Fsdb_vlm_db_data vlm = Vlm_find(path);
		vlm.Img_insert(rv, String_.new_utf8_(dir), String_.new_utf8_(fil), ext_id, width, thumbtime, is_orig, height, modified, sha1, bin);
	}
	public Fsdb_vlm_db_data Vlm_find(byte[] path) {
		vlm_temp.Rng_bgn_(path);
		int idx = Binary_search_.Search((CompareAble[])vlms, vlms.length, vlm_temp);
		return vlms[idx];
	}
	public void Select(Xof_fsdb_itm[] ary) {
		int len = ary.length;
		Select__find_vlm(retrieve_lists, ary, len);
		Select__load_bin(retrieve_lists, vlms_len);
	}
	private void Select__find_vlm(OrderedHash[] retrieve_lists, Xof_fsdb_itm[] ary, int len) {
		Array_.Sort(ary, Xof_fsdb_itm_comparer_name._); // sort ary by name;
		IntRef tmp_pos = IntRef.neg1_(); ByteAryRef tmp_ttl = ByteAryRef.null_();
		byte[] nxt_vlm_bgn = vlms.length == 1 ? null : vlms[1].Rng_bgn();
		int cur_db_id = 0;
		for (int i = 0; i < len; i++) {	// iterate over ttls
			Xof_fsdb_itm itm = ary[i];
			byte[] cur_ttl = itm.Ttl();
			if (nxt_vlm_bgn != null && ByteAry_.Compare(cur_ttl, nxt_vlm_bgn) == CompareAble_.More) {	// cur_ttl > nxt_vlm_bgn
				Select__find_vlm_nxt(tmp_pos, tmp_ttl, vlms, vlms_len, cur_db_id, cur_ttl);
				nxt_vlm_bgn = tmp_ttl.Val();
				cur_db_id = tmp_pos.Val();
			}
			itm.Db_fil_id_(cur_db_id);					// set db_id of itm
			retrieve_lists[i].Add(itm.Reg_key(), itm);	// put itm in db_list
		}		
	}
	private void Select__find_vlm_nxt(IntRef nxt_pos, ByteAryRef nxt_bry, Fsdb_vlm_db_data[] vlms, int vlms_len, int cur_pos, byte[] cur_ttl) {
		for (int i = cur_pos; i < vlms_len; i++) {	// iterate over all remaining vlms; note that for is needed b/c ttl can skip vlms; EX: prv_ttl=B; cur_ttl=D; vlm=B,C,D; vlm C is skipped over
			Fsdb_vlm_db_data vlm = vlms[i];
			byte[] vlm_path_bgn = vlm.Rng_bgn();
			if (ByteAry_.Compare(cur_ttl, vlm_path_bgn) == CompareAble_.Less) {	// cur_ttl < vlm; return;
				nxt_pos.Val_(i);
				nxt_bry.Val_(vlm.Rng_bgn());
				return;
			}
		}
		nxt_pos.Val_(vlms_len - 1);	// last vlm is catch-all
		nxt_bry.Val_(null);
	}
	private void Select__load_bin(OrderedHash[] retrieve_lists, int len) {
		for (int i = 0; i < len; i++) {
			OrderedHash db_list = retrieve_lists[i];
			int db_list_len = db_list.Count(); if (db_list_len == 0) continue;	// nothing in list;
			searcher.Search(vlms[i].Provider(), db_list, db_list_len);
		}
	}	private Xof_vlm_searcher searcher = new Xof_vlm_searcher();
	private ByteAryBfr path_bfr = ByteAryBfr.reset_(512);
	private static final byte Dir_spr = Byte_ascii.Slash;
	private static OrderedHash[] retrieve_lists_new(int len) {
		OrderedHash[] rv = new OrderedHash[len];
		for (int i = 0; i < len; i++)
			rv[i] = OrderedHash_.new_bry_();
		return rv;
	}
	public void Rls() {
		for (int i = 0; i < vlms_len; i++)
			vlms[i].Rls();
	}
}
