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
public class Gfui_box_html_mok implements Xog_box_html {
	public Gfui_box_html_mok(Gfui_box_grp grp, String id) {this.grp = grp; this.id = id;}
	public String Id() {return id;} private String id;
	public Gfui_box_grp Grp() {return grp;} Gfui_box_grp grp; 
	public String Text() {return text;} public void Text_(String v) {text = v;} private String text = "";
	public String Html_elem_atr_get_str(String elem_id, String atr_key) {return (String)elem_atrs.Fetch(elem_id + "." + atr_key);}
	public void Html_elem_atr_add(String elem_id, String atr_key, Object atr_val) {elem_atrs.AddReplace(elem_id + "." + atr_key, atr_val);} HashAdp elem_atrs = HashAdp_.new_();
	public boolean Html_elem_atr_get_bool(String elem_id, String atr_key) {return Bool_.parse_((String)elem_atrs.Fetch(elem_id + "." + atr_key));}
	public void Focus() {grp.Focus_box_(this);}
	public void Html_doc_body_focus() {}
}
