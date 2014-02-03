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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
class Scrib_lib_message implements Scrib_lib {
	public Scrib_lib_message(Scrib_engine engine) {this.engine = engine;} Scrib_engine engine;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.message.lua"), this
			, String_.Ary(Invk_toString, Invk_check, Invk_init_message_for_lang)
			// NOTE: "lang" not passed in
			);
		notify_lang_changed_fnc = mod.Fncs_get_by_key("notify_lang_changed");
		return mod;
	}	Scrib_fnc notify_lang_changed_fnc;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_toString))					return ToStr((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_check))						return Check((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_init_message_for_lang))		return Init_message_for_lang((KeyVal[])m.CastObj("v"));
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String Invk_toString = "toString", Invk_check = "check", Invk_init_message_for_lang = "init_message_for_lang";
	public void Notify_lang_changed() {if (notify_lang_changed_fnc != null) engine.Interpreter().CallFunction(notify_lang_changed_fnc.Id(), KeyVal_.Ary_empty);}
	public KeyVal[] ToStr(KeyVal[] values) {
		byte fmt_tid = Scrib_lib_message_data.parse_fmt_(Scrib_kv_utl.Val_to_bry(values, 0));
		KeyVal[] data_kvary = Scrib_kv_utl.Val_to_KeyVal_ary(values, 1);
		Scrib_lib_message_data msg_data = new Scrib_lib_message_data().Parse(data_kvary); 
		return Scrib_kv_utl.base1_obj_(String_.new_utf8_(msg_data.Make_msg(engine.Cur_lang(), engine.Wiki(), engine.Ctx(), true, fmt_tid)));
	}
	public KeyVal[] Check(KeyVal[] values) {
		byte chk_tid = Scrib_lib_message_data.parse_chk_(Scrib_kv_utl.Val_to_bry(values, 0));
		KeyVal[] data_kvary = Scrib_kv_utl.Val_to_KeyVal_ary(values, 1);
		Scrib_lib_message_data msg_data = new Scrib_lib_message_data().Parse(data_kvary);
		return Scrib_kv_utl.base1_obj_(msg_data.Chk_msg(engine.Cur_lang(), engine.Wiki(), engine.Ctx(), false, chk_tid));
	}
	public KeyVal[] Init_message_for_lang(KeyVal[] values) {return Scrib_kv_utl.base1_obj_(KeyVal_.new_("lang", engine.Wiki().Lang().Key_str()));}
}
class Scrib_lib_message_data {
	public boolean Use_db() {return use_db;} private boolean use_db;
	public byte[] Lang_key() {return lang_key;} private byte[] lang_key = ByteAry_.Empty;
	public byte[] Title_bry() {return title_bry;} private byte[] title_bry;
	public byte[] Msg_key() {return msg_key;} private byte[] msg_key;
	public byte[] Raw_msg_key() {return raw_msg_key;} private byte[] raw_msg_key;
	public Object[] Args() {return args;} Object[] args;
	public Xoa_ttl Ttl() {return ttl;} public Scrib_lib_message_data Ttl_(Xoa_ttl v) {ttl = v; return this;}  Xoa_ttl ttl;
	public Scrib_lib_message_data Parse(KeyVal[] ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			KeyVal kv = ary[i];
			byte[] kv_key = ByteAry_.new_ascii_(kv.Key());
			Object key_obj = key_hash.Fetch(kv_key); if (key_obj == null) throw Err_.new_fmt_("msg_key is invalid: {0}", kv_key);
			byte key_tid = ((ByteVal)key_obj).Val();
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
				ttl = ctx.Page().Ttl();
			else
				ttl = Xoa_ttl.parse_(wiki, data_ttl);
		}
		if (raw_msg_key != null) {
			Xol_msg_itm raw_msg_itm = new Xol_msg_itm(-1, ByteAry_.Empty);
			ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
			byte[] raw_msg_val = Pf_msg_mgr.Convert_php_to_gfs(raw_msg_key, tmp_bfr);
			Xol_msg_itm_.update_val_(raw_msg_itm, raw_msg_val);
			byte[] raw_msg_rv = wiki.Msg_mgr().Val_by_itm(tmp_bfr, raw_msg_itm, args);
			tmp_bfr.Mkr_rls();
			return raw_msg_rv;
		}
		if (msg_key == null) return ByteAry_.Empty;
		
		if (ByteAry_.Eq(lang_key, cur_lang) || ByteAry_.Len_eq_0(lang_key))	// if lang_key == engine_lang then use wiki.msg_mgr; also same if lang_key == blank (arg not given)
			return wiki.Msg_mgr().Val_by_key_args(msg_key, args);
		else {
			Xol_lang lang = wiki.App().Lang_mgr().Get_by_key_or_new(lang_key); lang.Init_by_load_assert();
			Xol_msg_itm msg_itm = lang.Msg_mgr().Itm_by_key_or_null(msg_key); if (msg_itm == null) return ByteAry_.Empty;
			return msg_itm.Val();
		}
	}
	public boolean Chk_msg(byte[] cur_lang, Xow_wiki wiki, Xop_ctx ctx, boolean exec_params, byte chk_tid) {
		byte[] msg_val = Fetch_msg(cur_lang, wiki, ctx, false);
		switch (chk_tid) {
			case Check_tid_exists		: return ByteAry_.Len_gt_0(msg_val);
			case Check_tid_isBlank		: return ByteAry_.Len_eq_0(msg_val);	// REF.MW: $message === false || $message === ''
			case Check_tid_isDisabled	: return ByteAry_.Len_eq_0(msg_val) || msg_val.length == 1 && msg_val[0] == Byte_ascii.Dash;	// REF.MW: $message === false || $message === '' || $message === '-'
			default						: throw Err_.unhandled(chk_tid);
		}
	}
	public byte[] Make_msg(byte[] cur_lang, Xow_wiki wiki, Xop_ctx ctx, boolean exec_params, byte fmt_tid) {
		byte[] msg_val = Fetch_msg(cur_lang, wiki, ctx, exec_params);
		if (ByteAry_.Len_eq_0(msg_val)) {
			ByteAryBfr bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
			bfr.Add(gplx.html.Html_entities.Lt).Add(msg_key).Add(gplx.html.Html_entities.Gt);	// NOTE: Message.php has logic that says: if plain, "< >", else "&lt; &gt;"; for now, always use escaped
			return bfr.Mkr_rls().XtoAryAndClear();
		}
		switch (fmt_tid) {
			case Fmt_tid_parse:
			case Fmt_tid_text:			// NOTE: not sure what this does; seems to be a "lighter" parser
				break;
			case Fmt_tid_parseAsBlock:	// NOTE: MW passes msg_val through parser and strips <p> if tid==parse; XOWA does the opposite, so add <p> if parseAsBlock requested
				ByteAryBfr bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
				msg_val = bfr.Add(Xoh_consts.P_bgn).Add(msg_val).Add(Xoh_consts.P_end).Mkr_rls().XtoAryAndClear();
				break;
			case Fmt_tid_escaped:
				msg_val = gplx.html.Html_util.Escape_html_as_bry(msg_val);
				break;
		}
		return msg_val;
	}
	static final byte Key_tid_keys = 1, Key_tid_rawMessage = 2, Key_tid_lang = 3, Key_tid_useDB = 4, Key_tid_title = 5, Key_tid_params = 6;
	private static final Hash_adp_bry key_hash = new Hash_adp_bry(false).Add_str_byteVal("keys", Key_tid_keys).Add_str_byteVal("rawMessage", Key_tid_rawMessage).Add_str_byteVal("lang", Key_tid_lang).Add_str_byteVal("useDB", Key_tid_useDB).Add_str_byteVal("title", Key_tid_title).Add_str_byteVal("params", Key_tid_params);
	public static byte parse_fmt_(byte[] key) {return parse_or_fail(fmt_hash, key, "invalid message format: {0}");}
	public static byte parse_chk_(byte[] key) {return parse_or_fail(check_hash, key, "invalid check arg: {0}");}
	public static byte parse_or_fail(Hash_adp_bry hash, byte[] key, String fmt) {
		Object o = hash.Get_by_bry(key);
		if (o == null) throw Err_.new_fmt_(fmt, String_.new_utf8_(key));
		return ((ByteVal)o).Val();
	}
	public static final byte Fmt_tid_parse = 1, Fmt_tid_text = 2, Fmt_tid_plain = 3, Fmt_tid_escaped = 4, Fmt_tid_parseAsBlock = 5;
	private static final Hash_adp_bry fmt_hash = new Hash_adp_bry(false).Add_str_byteVal("parse", Fmt_tid_parse).Add_str_byteVal("text", Fmt_tid_text).Add_str_byteVal("plain", Fmt_tid_plain).Add_str_byteVal("escaped", Fmt_tid_escaped).Add_str_byteVal("parseAsBlock", Fmt_tid_parseAsBlock);
	public static final byte Check_tid_exists = 1, Check_tid_isBlank = 2, Check_tid_isDisabled = 3;
	private static final Hash_adp_bry check_hash = new Hash_adp_bry(false).Add_str_byteVal("exists", Check_tid_exists).Add_str_byteVal("isBlank", Check_tid_isBlank).Add_str_byteVal("isDisabled", Check_tid_isDisabled);
}
