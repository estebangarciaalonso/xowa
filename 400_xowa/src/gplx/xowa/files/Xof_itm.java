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
package gplx.xowa.files; import gplx.*; import gplx.xowa.*;
public class Xof_itm {
	public byte[]	Lnki_ttl() {return lnki_ttl;} public Xof_itm Lnki_ttl_(byte[] v) {this.lnki_ttl = v; return this;} private byte[] lnki_ttl;
	public byte		Lnki_type() {return lnki_type;} private byte lnki_type;
	public int		Lnki_w() {return lnki_w;} private int lnki_w;
	public int		Lnki_h() {return lnki_h;} private int lnki_h;
	public double	Lnki_upright() {return lnki_upright;} private double lnki_upright;
	public int		Lnki_thumbtime() {return lnki_thumbtime;} private int lnki_thumbtime;
	public Xof_itm Lnki_atrs_(Xof_itm_key_bldr reg_key_bldr, byte[] lnki_ttl, byte lnki_type, int lnki_w, int lnki_h, double lnki_upright, int lnki_thumbtime) {
		this.reg_key = reg_key_bldr.Bld(lnki_ttl, lnki_type, lnki_w, lnki_h, lnki_upright, lnki_thumbtime);
		this.lnki_ttl = lnki_ttl; this.lnki_type = lnki_type; this.lnki_w = lnki_w; this.lnki_h = lnki_h; this.lnki_upright = lnki_upright; this.lnki_thumbtime = lnki_thumbtime;
		return this;
	}
	public int Lnki_html_id() {return lnki_html_id;} private int lnki_html_id;
	public int Lnki_html_id_len() {return lnki_html_id_len;} private int lnki_html_id_len;
	public ListAdp Lnki_html_id_list() {return lnki_html_id_list;} private ListAdp lnki_html_id_list;
	public void Link_html_id_add(int html_id) {
		switch (lnki_html_id_len) {
			case 0: lnki_html_id = html_id; break;
			case 1: lnki_html_id_list = ListAdp_.new_(); lnki_html_id_list.Add(IntRef.new_(lnki_html_id)); break;
			default: lnki_html_id_list.Add(IntRef.new_(lnki_html_id)); break;
		}
		++lnki_html_id_len;
	}
	public byte Status() {return status;} public void Status_(byte v) {status = v;} private byte status;
	public byte[] Reg_key() {return reg_key;} private byte[] reg_key;
	public byte Reg_status() {return reg_status;} public Xof_itm Reg_status_(byte v) {this.reg_status = v; return this;} private byte reg_status = Reg_status_unknown;
	public String Reg_url() {return reg_url;} public Xof_itm Reg_url_(String v) {this.reg_url = v; return this;} private String reg_url;
	public int Db_dir_id() {return db_dir_id;} public Xof_itm Db_dir_id_(int v) {db_dir_id = v; return this;} private int db_dir_id;
	public int Db_fil_id() {return db_fil_id;} public void Db_fil_id_(int v) {db_fil_id = v;} private int db_fil_id; 
	public boolean Db_img_is_orig() {return db_img_is_orig;} public Xof_itm Db_img_is_orig_(boolean v) {db_img_is_orig = v; return this;} private boolean db_img_is_orig;
	public int Db_img_id() {return db_img_id;} public Xof_itm Db_img_id_(int v) {db_img_id = v; return this;} private int db_img_id;
	public int Db_img_w() {return db_img_w;} public Xof_itm Db_img_w_(int v) {db_img_w = v; return this;} private int db_img_w;
	public int Db_img_h() {return db_img_h;} public Xof_itm Db_img_h_(int v) {db_img_h = v; return this;} private int db_img_h;
	public byte[] Db_data() {return db_data;} public Xof_itm Db_data_(byte[] v) {db_data = v; return this;} private byte[] db_data;
	public static final byte Reg_status_unknown = 0, Reg_status_present = 1, Reg_status_deleted = 2, Reg_status_missing = 3;
	public static final byte Status_vlm_found = 1, Status_dir_found = 2, Status_fil_found = 3, Status_img_found = 4, Status_bin_found = 5; 
}
