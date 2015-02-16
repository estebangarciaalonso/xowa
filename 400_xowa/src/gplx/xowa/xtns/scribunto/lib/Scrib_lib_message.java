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
package gplx.xowa.xtns.scribunto.lib; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*;
import gplx.core.primitives.*; import gplx.html.*;
public class Scrib_lib_message implements Scrib_lib {
	public Scrib_lib_message(Scrib_core core) {this.core = core;} private Scrib_core core;
	public Scrib_lua_mod Mod() {return mod;} private Scrib_lua_mod mod;
	public Scrib_lib Init() {procs.Init_by_lib(this, Proc_names); return this;}
	public Scrib_lua_mod Register(Scrib_core core, Io_url script_dir) {
		Init();
		mod = core.RegisterInterface(this, script_dir.GenSubFil("mw.message.lua"));// NOTE: "lang" not passed in
		notify_lang_changed_fnc = mod.Fncs_get_by_key("notify_lang_changed");
		return mod;
	}	private Scrib_lua_proc notify_lang_changed_fnc;
	public Scrib_proc_mgr Procs() {return procs;} private Scrib_proc_mgr procs = new Scrib_proc_mgr();
	public boolean Procs_exec(int key, Scrib_proc_args args, Scrib_proc_rslt rslt) {
		switch (key) {
			case Proc_plain:									return Plain(args, rslt);
			case Proc_check:									return Check(args, rslt);
			case Proc_init_message_for_lang:					return Init_message_for_lang(args, rslt);
			default: throw Err_.unhandled(key);
		}
	}
	private static final int Proc_plain = 0, Proc_check = 1, Proc_init_message_for_lang = 2;
	public static final String Invk_plain = "plain", Invk_check = "check", Invk_init_message_for_lang = "init_message_for_lang";
	private static final String[] Proc_names = String_.Ary(Invk_plain, Invk_check, Invk_init_message_for_lang);
	public void Notify_lang_changed() {if (notify_lang_changed_fnc != null) core.Interpreter().CallFunction(notify_lang_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public boolean Plain(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte fmt_tid = Scrib_lib_message_data.Fmt_tid_plain;
		KeyVal[] data_kvary = args.Pull_kv_ary(0);
		Scrib_lib_message_data msg_data = new Scrib_lib_message_data().Parse(data_kvary); 
		return rslt.Init_obj(String_.new_utf8_(msg_data.Make_msg(core.Cur_lang(), core.Wiki(), core.Ctx(), true, fmt_tid)));
	}
	public boolean Check(Scrib_proc_args args, Scrib_proc_rslt rslt) {
		byte chk_tid = Scrib_lib_message_data.parse_chk_(args.Pull_bry(0));
		KeyVal[] data_kvary = args.Pull_kv_ary(1);
		Scrib_lib_message_data msg_data = new Scrib_lib_message_data().Parse(data_kvary);
		return rslt.Init_obj(msg_data.Chk_msg(core.Cur_lang(), core.Wiki(), core.Ctx(), false, chk_tid));
	}
	public boolean Init_message_for_lang(Scrib_proc_args args, Scrib_proc_rslt rslt) {return rslt.Init_obj(KeyVal_.new_("lang", core.Wiki().Lang().Key_str()));}
}
class Scrib_lib_message_data {
	public boolean Use_db() {return use_db;} private boolean use_db;
	public byte[] Lang_key() {return lang_key;} private byte[] lang_key = Bry_.Empty;
	public byte[] Title_bry() {return title_bry;} private byte[] title_bry;
	public byte[] Msg_key() {return msg_key;} private byte[] msg_key;
	public byte[] Raw_msg_key() {return raw_msg_key;} private byte[] raw_msg_key;
	public Object[] Args() {return args;} Object[] args;
	public Xoa_ttl Ttl() {return ttl;} public Scrib_lib_message_data Ttl_(Xoa_ttl v) {ttl = v; return this;}  Xoa_ttl ttl;
	public Scrib_lib_message_data Parse(KeyVal[] ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			KeyVal kv = ary[i];
			byte[] kv_key = Bry_.new_ascii_(kv.Key());
			Object key_obj = key_hash.Fetch(kv_key); if (key_obj == null) throw Err_.new_fmt_("msg_key is invalid: {0}", kv_key);
			byte key_tid = ((Byte_obj_val)key_obj).Val();
			switch (key_tid) {
				case Key_tid_keys:
					KeyVal[] keys_ary = (KeyVal[])kv.Val();
					msg_key = keys_ary[0].Val_to_bry();
					break; 
				case Key_tid_rawMessage:	raw_msg_key = kv.Val_to_bry(); break;
				case Key_tid_lang:			lang_key = kv.Val_to_bry(); break;
				case Key_tid_useDB:			use_db = Bool_.cast_(kv.Val()); break;
				case Key_tid_title:			title_bry = kv.Val_to_bry(); break;
				case Key_tid_params:
					KeyVal[] args_ary = (KeyVal[])kv.Val();
					int args_ary_len = args_ary.length;
					args = new String[args_ary_len];
					for (int j = 0; j < args_ary_len; j++)
						args[j] = args_ary[j].Val_to_str_or_empty();
					break; 
				default:					throw Err_.unhandled(key_tid);
			}
		}
		return this;
	}
	public byte[] Fetch_msg(byte[] cur_lang, Xow_wiki wiki, Xop_ctx ctx, boolean exec_params) {
		if (exec_params) {
			byte[] data_ttl = title_bry;
			if (data_ttl == null)
				ttl = ctx.Cur_page().Ttl();
			else
				ttl = Xoa_ttl.parse_(wiki, data_ttl);
		}
		if (raw_msg_key != null) {
			Xol_msg_itm raw_msg_itm = new Xol_msg_itm(-1, Bry_.Empty);
			Bry_bfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
			byte[] raw_msg_val = gplx.xowa.apps.Xoa_gfs_php_mgr.Xto_gfs(tmp_bfr, raw_msg_key);
			Xol_msg_itm_.update_val_(raw_msg_itm, raw_msg_val);
			byte[] raw_msg_rv = wiki.Msg_mgr().Val_by_itm(tmp_bfr, raw_msg_itm, args);
			tmp_bfr.Mkr_rls();
			return raw_msg_rv;
		}
		if (msg_key == null) return Bry_.Empty;
		
		if (Bry_.Eq(lang_key, cur_lang) || Bry_.Len_eq_0(lang_key))	// if lang_key == core_lang then use wiki.msg_mgr; also same if lang_key == blank (arg not given)
			return wiki.Msg_mgr().Val_by_key_args(msg_key, args);
		else {
			Xol_lang lang = wiki.App().Lang_mgr().Get_by_key_or_new(lang_key); lang.Init_by_load_assert();
			Xol_msg_itm msg_itm = lang.Msg_mgr().Itm_by_key_or_null(msg_key); if (msg_itm == null) return Bry_.Empty;
			return msg_itm.Val();
		}
	}
	public boolean Chk_msg(byte[] cur_lang, Xow_wiki wiki, Xop_ctx ctx, boolean exec_params, byte chk_tid) {
		byte[] msg_val = Fetch_msg(cur_lang, wiki, ctx, false);
		switch (chk_tid) {
			case Check_tid_exists		: return Bry_.Len_gt_0(msg_val);
			case Check_tid_isBlank		: return Bry_.Len_eq_0(msg_val);	// REF.MW: $message === false || $message === ''
			case Check_tid_isDisabled	: return Bry_.Len_eq_0(msg_val) || msg_val.length == 1 && msg_val[0] == Byte_ascii.Dash;	// REF.MW: $message === false || $message === '' || $message === '-'
			default						: throw Err_.unhandled(chk_tid);
		}
	}
	public byte[] Make_msg(byte[] cur_lang, Xow_wiki wiki, Xop_ctx ctx, boolean exec_params, byte fmt_tid) {
		byte[] msg_val = Fetch_msg(cur_lang, wiki, ctx, exec_params);
		if (Bry_.Len_eq_0(msg_val)) {
			Bry_bfr bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
			bfr.Add(gplx.html.Html_entity_.Lt_bry).Add(msg_key).Add(gplx.html.Html_entity_.Gt_bry);	// NOTE: Message.php has logic that says: if plain, "< >", else "&lt; &gt;"; for now, always use escaped
			return bfr.Mkr_rls().Xto_bry_and_clear();
		}
		switch (fmt_tid) {
			case Fmt_tid_parse:
			case Fmt_tid_text:			// NOTE: not sure what this does; seems to be a "lighter" parser
				break;
			case Fmt_tid_parseAsBlock:	// NOTE: MW passes msg_val through parser and strips <p> if tid==parse; XOWA does the opposite, so add <p> if parseAsBlock requested
				Bry_bfr bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
				msg_val = bfr.Add(Html_tag_.P_lhs).Add(msg_val).Add(Html_tag_.P_rhs).Mkr_rls().Xto_bry_and_clear();
				break;
			case Fmt_tid_escaped:
				msg_val = gplx.html.Html_utl.Escape_html_as_bry(msg_val);
				break;
		}
		return msg_val;
	}
	static final byte Key_tid_keys = 1, Key_tid_rawMessage = 2, Key_tid_lang = 3, Key_tid_useDB = 4, Key_tid_title = 5, Key_tid_params = 6;
	private static final Hash_adp_bry key_hash = Hash_adp_bry.ci_ascii_()
	.Add_str_byte("keys", Key_tid_keys)
	.Add_str_byte("rawMessage", Key_tid_rawMessage)
	.Add_str_byte("lang", Key_tid_lang)
	.Add_str_byte("useDB", Key_tid_useDB)
	.Add_str_byte("title", Key_tid_title)
	.Add_str_byte("params", Key_tid_params);
	public static byte parse_fmt_(byte[] key) {return parse_or_fail(fmt_hash, key, "invalid message format: {0}");}
	public static byte parse_chk_(byte[] key) {return parse_or_fail(check_hash, key, "invalid check arg: {0}");}
	public static byte parse_or_fail(Hash_adp_bry hash, byte[] key, String fmt) {
		Object o = hash.Get_by_bry(key);
		if (o == null) throw Err_.new_fmt_(fmt, String_.new_utf8_(key));
		return ((Byte_obj_val)o).Val();
	}
	public static final byte Fmt_tid_parse = 1, Fmt_tid_text = 2, Fmt_tid_plain = 3, Fmt_tid_escaped = 4, Fmt_tid_parseAsBlock = 5;
	private static final Hash_adp_bry fmt_hash = Hash_adp_bry.ci_ascii_()
	.Add_str_byte("parse", Fmt_tid_parse)
	.Add_str_byte("text", Fmt_tid_text)
	.Add_str_byte("plain", Fmt_tid_plain)
	.Add_str_byte("escaped", Fmt_tid_escaped)
	.Add_str_byte("parseAsBlock", Fmt_tid_parseAsBlock);
	public static final byte Check_tid_exists = 1, Check_tid_isBlank = 2, Check_tid_isDisabled = 3;
	private static final Hash_adp_bry check_hash = Hash_adp_bry.ci_ascii_()
	.Add_str_byte("exists", Check_tid_exists)
	.Add_str_byte("isBlank", Check_tid_isBlank)
	.Add_str_byte("isDisabled", Check_tid_isDisabled);
}
