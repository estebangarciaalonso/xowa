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
package gplx.xowa.gui.menus.contexts; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*; import gplx.xowa.gui.menus.*;
import gplx.gfui.*;
public class Xog_mnu_bldr {
	private Xoa_app app; private Gfui_kit kit; private Io_url img_dir;
	private ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255);
	public void Init_by_kit(Xoa_app app, Gfui_kit kit, Io_url img_dir) {
		this.app = app; this.kit = kit; this.img_dir = img_dir; 
	}
	public void Build(Gfui_mnu_grp mgr, Xog_mnu_grp mnu) {
		if (mgr == null) return;	// NOTE: hackish, ignore call from user.gfs b/c it fires before kit is inited; note that Xog_popup_mnu_mgr will call html_box's context menu explicitly
		mgr.Itms_clear();
		Build_owner(mgr, mnu);
	}
	private void Build_owner(Gfui_mnu_grp owner_gui, Xog_mnu_base owner_itm) {
		int len = owner_itm.Len();
		for (int i = 0; i < len; i++) {
			Xog_mnu_itm sub = owner_itm.Get_at(i);
			String sub_text = Get_msg_val(sub.Key(), sub.Msg_prefix_(), Bry_text, sub.Text());
			String sub_shortcut = Get_msg_val(sub.Key(), sub.Msg_prefix_(), Bry_shortcut, sub.Shortcut());
			switch (sub.Tid()) {
				case Xog_mnu_itm.Tid_spr: owner_gui.Itms_add_separator(); break;
				case Xog_mnu_itm.Tid_btn: Add_cmd(owner_gui, sub, sub_text, sub_shortcut); break;
				case Xog_mnu_itm.Tid_grp: {
					Gfui_mnu_grp sub_gui = owner_gui.Itms_add_grp(Insert_shortcut(sub_text, sub_shortcut), Get_img(sub.Img_nest()));
					Build_owner(sub_gui, (Xog_mnu_base)sub);
					break;
				}
				default: throw Err_.unhandled(sub.Tid());
			}
		}
	}
	private String Get_msg_val(String key, String msg_prefix, byte[] msg_type, String or) {
		if (msg_prefix == null) msg_prefix = key;
		byte[] msg_key = tmp_bfr.Add(Bry_menu).Add_str(key).Add_byte(Byte_ascii.Dash).Add(msg_type).XtoAryAndClear();
		byte[] msg_val = app.User().Msg_mgr().Val_by_key_obj(msg_key);
		return ByteAry_.Len_eq_0(msg_val) ? or : String_.new_utf8_(msg_val);
	}
	private ImageAdp Get_img(String[] img_nest) {
		Io_url img_url = img_nest.length == 0 ? Io_url_.Null : img_dir.GenSubFil_nest(img_nest);
		return Io_mgr._.ExistsFil(img_url) ? kit.New_img_load(img_url) : ImageAdp_.Null;	// NOTE: must check if file exists else swt exception;
	}
	private void Add_cmd(Gfui_mnu_grp owner_gui, Xog_mnu_itm sub, String sub_text, String sub_shortcut) {
		String text = Insert_shortcut(sub_text, sub_shortcut);
		GfoMsg msg = app.Gfs_mgr().Parse_root_msg(sub.Cmd());
		ImageAdp img = Get_img(sub.Img_nest());
		owner_gui.Itms_add_msg(text, img, app, msg, app.Gfs_mgr());
	}
	public static String Insert_shortcut(String text, String shortcut) {
		if (String_.Len(shortcut) != 1) return text;
		int pos = String_.FindFwd(String_.Lower(text), String_.Lower(shortcut));
		if (pos == String_.NotFound) return text;
		return String_.MidByPos(text, 0, pos) + "&" + String_.Mid(text, pos); 
	}
	private static final byte[] Bry_menu = ByteAry_.new_ascii_("menu-"), Bry_text = ByteAry_.new_ascii_("text"), Bry_shortcut = ByteAry_.new_ascii_("shortcut");
}
