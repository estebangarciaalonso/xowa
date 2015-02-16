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
package gplx.xowa; import gplx.*;
public class Xoac_wiki_itm implements Cfg_nde_obj, Xoac_wiki_obj {
	public Xoac_wiki_itm(byte[] key) {this.key_bry = key;}
	public byte[] Key_bry() {return key_bry;} private byte[] key_bry;
	public byte[][] Aliases() {return aliases;} private byte[][] aliases;
	public byte[] Nde_key() {return key_bry;}
	public boolean Nde_typ_is_grp() {return false;}
	public Cfg_nde_obj Nde_subs_make(byte[] itm_type, byte[] itm_key, byte[][] itm_atrs) {throw Err_mgr._.fmt_(GRP_KEY, "invalid_sub_make", "leafs cannot have itms: ~{0} ~{1}", itm_type, itm_key);}
	public Cfg_nde_obj Nde_subs_get(byte[] key) {throw Err_mgr._.fmt_(GRP_KEY, "invalid_sub_get", "leafs cannot have itms: ~{0}", key);}
	public int Nde_subs_len() {return 0;}
	public Cfg_nde_obj Nde_subs_get_at(int i) {throw Err_mgr._.fmt_(GRP_KEY, "invalid_sub_get", "leafs cannot have itms: ~{0}", i);}
	public void Nde_subs_add(byte[] itm_key, Cfg_nde_obj itm_obj) {throw Err_mgr._.fmt_(GRP_KEY, "invalid_sub_add", "leafs cannot have itms: ~{0}", String_.new_utf8_(itm_key));}
	public void Nde_subs_del(byte[] key) {throw Err_mgr._.fmt_(GRP_KEY, "invalid_sub_del", "leafs cannot delete itms: ~{0}", String_.new_utf8_(key));}
	public void Nde_atrs_set(byte[][] ary) {
		int ary_len = ary.length;
		if (ary_len > 0) aliases = Bry_.Split(ary[0], Byte_ascii.Semic);
	}
	static final String GRP_KEY = "xowa.wikis.itms";
}
