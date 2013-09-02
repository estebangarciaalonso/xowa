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
package gplx.gfui; import gplx.*;
public interface Gfui_mnu_grp extends Gfui_mnu_itm {
	void Itms_clear();
	Gfui_mnu_itm Itms_add_cmd(String txt, ImageAdp img, GfoInvkAble invk, String invk_cmd);
	Gfui_mnu_itm Itms_add_msg(String txt, ImageAdp img, GfoInvkAble invk, GfoMsg invk_msg, GfoInvkRootWkr root_wkr);
	Gfui_mnu_grp Itms_add_grp(String txt, ImageAdp img);
	Gfui_mnu_itm Itms_add_separator();
}
class Gfui_mnu_grp_null implements Gfui_mnu_grp {
	public Object Under() {return null;}
	public void Itms_clear() {}
	public Gfui_mnu_itm Itms_add_cmd(String txt, ImageAdp img, GfoInvkAble invk, String invk_cmd) {return Gfui_mnu_itm_null.Null;}
	public Gfui_mnu_itm Itms_add_msg(String txt, ImageAdp img, GfoInvkAble invk, GfoMsg invk_msg, GfoInvkRootWkr root_wkr) {return Gfui_mnu_itm_null.Null;}
	public Gfui_mnu_grp Itms_add_grp(String txt, ImageAdp img) {return Gfui_mnu_grp_null.Null;}
	public Gfui_mnu_itm Itms_add_separator() {return Gfui_mnu_itm_null.Null;}
        public static final Gfui_mnu_grp_null Null = new Gfui_mnu_grp_null(); Gfui_mnu_grp_null() {}
}
