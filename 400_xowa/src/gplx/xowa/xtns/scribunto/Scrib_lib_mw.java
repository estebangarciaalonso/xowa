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
import gplx.xowa.langs.*;
class Scrib_lib_mw implements GfoInvkAble, Scrib_lib {
	public Scrib_lib_mw(Scrib_engine engine) {this.engine = engine; this.interpreter = engine.Interpreter(); this.fsys_mgr = engine.Fsys_mgr();} Scrib_engine engine; Scrib_interpreter interpreter; Scrib_fsys_mgr fsys_mgr;
	public Scrib_mod Mod() {return mod;} public void Mod_(Scrib_mod v) {this.mod = v;} private Scrib_mod mod;
	public boolean Allow_env_funcs() {return allow_env_funcs;} private boolean allow_env_funcs = true;
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {			
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.lua")
			, this, String_.Ary(Invk_loadPackage, Invk_frameExists, Invk_parentFrameExists, Invk_getExpandedArgument, Invk_getAllExpandedArguments, Invk_expandTemplate, Invk_preprocess, Invk_callParserFunction, Invk_incrementExpensiveFunctionCount
			, Invk_isSubsting, Invk_newChildFrame
			)
			, KeyVal_.new_("allowEnvFuncs", allow_env_funcs));
		return mod;
	}
	public void Invoke_bgn(Xow_wiki wiki, Xop_ctx ctx, byte[] new_src) {
		if (src != null)	// src exists; indicates that Invoke being called recursively; push existing src onto stack
			src_stack.Add(src);
		this.cur_wiki = wiki; this.ctx = ctx; this.src = new_src;
	}	private Xow_wiki cur_wiki; private byte[] src; private Xop_ctx ctx; private ListAdp src_stack = ListAdp_.new_();
	public void Invoke_end() {
		if (src_stack.Count() > 0)	// src_stack item exists; pop
			src = (byte[])ListAdp_.Pop(src_stack);
		else						// entry point; set to null
			src = null;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_loadPackage))						return LoadPackage((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getExpandedArgument))				return GetExpandedArgument((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_getAllExpandedArguments))			return GetAllExpandedArguments((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_frameExists))						return FrameExists((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_parentFrameExists))					return ParentFrameExists((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_preprocess))							return Preprocess((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_expandTemplate))						return ExpandTemplate((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_callParserFunction))					return CallParserFunction((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_incrementExpensiveFunctionCount))	return IncrementExpensiveFunctionCount((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_isSubsting))							return IsSubsting((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_newChildFrame))						return NewChildFrame((KeyVal[])m.CastObj("v"));
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String Invk_loadPackage = "loadPackage", Invk_frameExists = "frameExists", Invk_parentFrameExists = "parentFrameExists"
	, Invk_getExpandedArgument = "getExpandedArgument", Invk_getAllExpandedArguments = "getAllExpandedArguments"
	, Invk_expandTemplate = "expandTemplate", Invk_preprocess = "preprocess"
	, Invk_callParserFunction = "callParserFunction", Invk_incrementExpensiveFunctionCount = "incrementExpensiveFunctionCount"
	, Invk_isSubsting = "isSubsting"
	, Invk_newChildFrame = "newChildFrame"
	;
	public KeyVal[] LoadPackage(KeyVal[] values) {
		String mod_name = Scrib_kv_utl.Val_to_str(values, 0);
		String mod_code = fsys_mgr.Get_or_null(mod_name);	// check if mod_name is a Scribunto .lua file (in /lualib/)
		if (mod_code != null)
			return Scrib_kv_utl.base1_obj_(interpreter.LoadString(mod_name, mod_code));
		Xoa_ttl ttl = Xoa_ttl.parse_(cur_wiki, ByteAry_.new_utf8_(mod_name));// NOTE: should have Module: prefix
		if (ttl == null) return Scrib_lib_mw.Rslts_none;
		Xoa_page page = cur_wiki.Data_mgr().Get_page(ttl, false);
		if (page.Missing()) return Scrib_lib_mw.Rslts_none;
		Scrib_mod mod = new Scrib_mod(engine, mod_name);
		return Scrib_kv_utl.base1_obj_(mod.LoadString(String_.new_utf8_(page.Data_raw())));
	}
	public KeyVal[] GetExpandedArgument(KeyVal[] values) {
		String frame_id = Scrib_kv_utl.Val_to_str(values, 0);
		Xot_invk frame = null;
		int frame_arg_adj = 1;
		if (String_.Eq(frame_id, "current"))
			frame = engine.Cur_frame_invoke();
		else {
			frame = engine.Cur_frame_owner();
			frame_arg_adj = 0;
		}
		String idx_str = Scrib_kv_utl.Val_to_str(values, 1);
		int idx_int = Int_.parse_or_(idx_str, Int_.MinValue);	// NOTE: should not receive int value < -1; idx >= 0
		ByteAryBfr tmp_bfr = ByteAryBfr.new_();	// NOTE: do not make modular level variable, else random failures; DATE:2013-10-14
		if (idx_int != Int_.MinValue) {	// idx is integer
			Arg_nde_tkn nde = Get_arg(frame, idx_int, frame_arg_adj);
			//frame.Args_eval_by_idx(engine.Ctx().Src(), idx_int); // NOTE: arg[0] is always MW function name; EX: {{#invoke:Mod_0|Func_0|Arg_1}}; arg_x = "Mod_0"; args[0] = "Func_0"; args[1] = "Arg_1"
			if (nde == null) return KeyVal_.Ary_empty;
			nde.Val_tkn().Tmpl_evaluate(ctx, src, engine.Cur_frame_owner(), tmp_bfr);
			return Scrib_kv_utl.base1_obj_(tmp_bfr.XtoStrAndClear());
		}
		else {
			Arg_nde_tkn nde = frame.Args_get_by_key(src, ByteAry_.new_utf8_(idx_str));
			if (nde == null) return KeyVal_.Ary_empty;	// idx_str does not exist;
			nde.Val_tkn().Tmpl_evaluate(ctx, src, engine.Cur_frame_owner(), tmp_bfr);
			return Scrib_kv_utl.base1_obj_(tmp_bfr.XtoStrAndClearAndTrim());	// NOTE: must trim if key_exists; DUPE:TRIM_IF_KEY
		}
	}
	private Arg_nde_tkn Get_arg(Xot_invk invk, int idx, int frame_arg_adj) {	// DUPE:MW_ARG_RETRIEVE
		int cur = ListAdp_.Base1, len = invk.Args_len() - frame_arg_adj; 
		for (int i = 0; i < len; i++) {	// iterate over list to find nth *non-keyd* arg; SEE:NOTE_1
			Arg_nde_tkn nde = (Arg_nde_tkn)invk.Args_get_by_idx(i + frame_arg_adj);
			if (nde.KeyTkn_exists()) {	// BLOCK:ignore_args_with_empty_keys;
				if (Verify_arg_key(src, idx, nde))
					return nde;
				else
					continue;
			}
			if (idx == cur) return nde;
			else ++cur;
		}
		return invk.Args_get_by_key(src, ByteAry_.XtoStrBytesByInt(idx + 1, 1));
	}
	private static boolean Verify_arg_key(byte[] src, int idx, Arg_nde_tkn nde) {
		int key_int = ByteAry_.NotFound;
		byte[] key_dat_ary = nde.Key_tkn().Dat_ary();
		if (Env_.Mode_testing() && src == null)	// some tests will always pass a null src;
			key_int = ByteAry_.X_to_int_or(key_dat_ary, 0, key_dat_ary.length, ByteAry_.NotFound);
		else {
			if (ByteAry_.Len_eq_0(key_dat_ary))	// should be called by current context;
				key_int = ByteAry_.X_to_int_or(src, nde.Key_tkn().Src_bgn(), nde.Key_tkn().Src_end(), ByteAry_.NotFound);
			else								// will be called by parent context; note that this calls Xot_defn_tmpl_.Make_itm which sets a key_dat_ary; DATE:2013-09-23
				key_int = ByteAry_.X_to_int_or(key_dat_ary, 0, key_dat_ary.length, ByteAry_.NotFound);
		}
		if (key_int == ByteAry_.NotFound)	// key is not-numeric
			return false;
		else								// key is numeric
			return idx == key_int;
	}
	public KeyVal[] GetAllExpandedArguments(KeyVal[] values) {
		String frame_id = Scrib_kv_utl.Val_to_str(values, 0);
		Xot_invk frame = null; Xot_invk owner_frame = null;
		int frame_arg_adj = -1;			
		if (String_.Eq(frame_id, "current")) {
			frame = engine.Cur_frame_invoke();
			frame_arg_adj = 1;						// current Module frame has extra arg for proc_name; skip it; EX: {{#invoke:Module|Proc}}
			owner_frame = engine.Cur_frame_owner();
		}
		else {
			frame = engine.Cur_frame_owner();
			frame_arg_adj = 0;
			owner_frame = Xot_invk_mock.Null;
		}
		int args_len = frame.Args_len() - frame_arg_adj;
		if (args_len < 1) return Scrib_kv_utl.base1_obj_(KeyVal_.Ary_empty);	// occurs when "frame:getParent().args" but no parent frame
		ByteAryBfr tmp_bfr = ctx.Wiki().Utl_bry_bfr_mkr().Get_b128();			// NOTE: do not make modular level variable, else random failures; DATE:2013-10-14
		ListAdp rv = ListAdp_.new_();
		int arg_idx = 0;
		for (int i = 0; i < args_len; i++) {
			Arg_nde_tkn nde = frame.Args_get_by_idx(i + frame_arg_adj);
			if (nde.KeyTkn_exists()) {	// BLOCK:ignore_args_with_empty_keys;
				byte[] key_dat_ary = nde.Key_tkn().Dat_ary();
				int key_dat_len = ByteAry_.Len(key_dat_ary);
				if (Env_.Mode_testing() && src == null)	 {
					if (key_dat_len == 0) continue;
				}
				else {
					if (key_dat_len == 0)	// should be called by current context;
						if (nde.Key_tkn().Src_end() - nde.Key_tkn().Src_bgn() == 0) continue;
				}
			}
			nde.Key_tkn().Tmpl_evaluate(ctx, src, owner_frame, tmp_bfr);
			int key_len = tmp_bfr.Len();
			boolean key_missing = key_len == 0;
			Object key = null;
			if (key_missing)	// key missing; EX: {{a|val}}
				key = ++arg_idx;// NOTE: MW requires a key; if none, then default to int index; NOTE: must be int, not String; NOTE: must be indexed to keyless args; EX: in "key1=val1,val2", "val2" must be "1" (1st keyless arg) not "2" (2nd arg); DATE:2013-11-09
			else {				// key exists; EX:{{a|key=val}}
				int key_int = ByteAry_.X_to_int_or(tmp_bfr.Bry(), 0, tmp_bfr.Len(), Int_.MinValue);
				if (key_int == Int_.MinValue)			// key is not int; create str
					key = tmp_bfr.XtoStrAndClear();
				else {									// key is int; must return int for key b/c lua treats table[1] different than table["1"]; DATE:2014-02-13
					tmp_bfr.Clear();					// must clear bfr, else key will be added to val;
					key = key_int;
				}
			}
			nde.Val_tkn().Tmpl_evaluate(ctx, src, owner_frame, tmp_bfr);
			String val = key_missing ? tmp_bfr.XtoStrAndClear() : tmp_bfr.XtoStrAndClearAndTrim(); // NOTE: must trim if key_exists; DUPE:TRIM_IF_KEY
			rv.Add(KeyVal_.obj_(key, val));
		}
		tmp_bfr.Mkr_rls();
		return Scrib_kv_utl.base1_obj_((KeyVal[])rv.XtoAry(KeyVal.class));
	}
	public KeyVal[] FrameExists(KeyVal[] values) {return Scrib_kv_utl.base1_obj_(values_get_frame(values, 0) != null);}
	public KeyVal[] ParentFrameExists(KeyVal[] values) {return Scrib_kv_utl.base1_obj_(!engine.Cur_frame_owner().Root_frame());}
	public KeyVal[] Preprocess(KeyVal[] values) {
		String frame_id = Scrib_kv_utl.Val_to_str(values, 0);
		boolean frame_is_current = String_.Eq(frame_id, "current") ;
		Xot_invk frame = frame_is_current ? engine.Cur_frame_invoke() : engine.Cur_frame_owner();
		Xot_invk owner_frame = frame_is_current ? engine.Cur_frame_owner() : Xot_invk_mock.Null;
		String text_str = Scrib_kv_utl.Val_to_str(values, 1);
		byte[] text_bry = ByteAry_.new_utf8_(text_str);
		Xop_root_tkn tmp_root = ctx.Tkn_mkr().Root(text_bry);
		Xop_ctx tmp_ctx = Xop_ctx.new_sub_(cur_wiki);
		int args_len = frame.Args_len() - 1;
		KeyVal[] kv_args = new KeyVal[args_len];
		ByteAryBfr tmp_bfr = ByteAryBfr.new_();	// NOTE: do not make modular level variable, else random failures; DATE:2013-10-14
		for (int i = 0; i < args_len; i++) {
			Arg_nde_tkn arg = frame.Args_get_by_idx(i + 1);
			arg.Key_tkn().Tmpl_evaluate(ctx, src, frame, tmp_bfr);
			String key = tmp_bfr.XtoStrAndClear();
			if (String_.Eq(key, "")) key = Int_.XtoStr(i);
			arg.Val_tkn().Tmpl_evaluate(ctx, src, owner_frame, tmp_bfr);	// NOTE: must evaluate against owner_frame; evaluating against current frame may cause stack-overflow; DATE:2013-04-04
			String val = tmp_bfr.XtoStrAndClear();
			kv_args[i] = KeyVal_.new_(key, val);
		}
		Xot_invk_mock mock_frame = Xot_invk_mock.new_(kv_args);
		cur_wiki.Parser().Parse_page_tmpl(tmp_root, tmp_ctx, tmp_ctx.Tkn_mkr(), text_bry);
		tmp_root.Tmpl_evaluate(tmp_ctx, text_bry, mock_frame, tmp_bfr);
		return Scrib_kv_utl.base1_obj_(tmp_bfr.XtoStrAndClear());
	}
	public KeyVal[] CallParserFunction(KeyVal[] values) {
		String frame_id = Scrib_kv_utl.Val_to_str(values, 0);
		Xot_invk owner_frame = String_.Eq(frame_id, "current") ? engine.Cur_frame_invoke() : engine.Cur_frame_owner();
		byte[] fnc_name = Scrib_kv_utl.Val_to_bry(values, 1);
		int fnc_name_len = fnc_name.length;
		ByteAryRef argx_ref = ByteAryRef.null_();
		ByteAryRef fnc_name_ref = ByteAryRef.new_(fnc_name);
		KeyVal[] args = CallParserFunction_parse_args(cur_wiki.App().Utl_num_parser(), argx_ref, fnc_name_ref, values);
		Xot_invk_mock frame = Xot_invk_mock.new_(owner_frame.Defn_tid(), 0, args);
		Xol_func_name_itm finder = cur_wiki.Lang().Func_regy().Find_defn(fnc_name, 0, fnc_name_len);
		Xot_defn defn = finder.Func();
		if (defn == Xot_defn_.Null) throw Err_.new_fmt_("callParserFunction: function \"{0}\" was not found", String_.new_utf8_(fnc_name));
		ByteAryBfr bfr = cur_wiki.Utl_bry_bfr_mkr().Get_k004();
		Xop_ctx fnc_ctx = Xop_ctx.new_sub_(cur_wiki);
		Xot_invk_tkn.Eval_func(fnc_ctx, src, owner_frame, frame, bfr, defn, argx_ref.Val());
		bfr.Mkr_rls();
		return Scrib_kv_utl.base1_obj_(bfr.XtoStrAndClear());
	}
	private KeyVal[] CallParserFunction_parse_args(NumberParser num_parser, ByteAryRef argx_ref, ByteAryRef fnc_name_ref, KeyVal[] args) {
		ListAdp rv = ListAdp_.new_();
		// flatten args
		int args_len = args.length;
		for (int i = 2; i < args_len; i++) {
			KeyVal arg = args[i];
			if (Scrib_kv_utl.Val_is_KeyVal_ary(arg)) {
				KeyVal[] arg_kv_ary = (KeyVal[])arg.Val();
				int arg_kv_ary_len = arg_kv_ary.length;
				for (int j = 0; j < arg_kv_ary_len; j++) {
					KeyVal sub_arg = arg_kv_ary[j];
					rv.Add(sub_arg);
				}
			}
			else
				rv.Add(arg);
		}
		rv.SortBy(Scrib_lib_mw_callParserFunction_sorter._);
		// get argx
		byte[] fnc_name = fnc_name_ref.Val();
		int fnc_name_len = fnc_name.length;
		int fnc_name_colon_pos = Byte_ary_finder.Find_fwd(fnc_name, Byte_ascii.Colon, 0, fnc_name_len);
		if (fnc_name_colon_pos == ByteAry_.NotFound) {
			KeyVal arg_argx = (KeyVal)rv.FetchAt(0);
			argx_ref.Val_(arg_argx.Val_to_bry());
			rv.DelAt(0);
		}
		else {
			argx_ref.Val_(ByteAry_.Mid(fnc_name, fnc_name_colon_pos + 1, fnc_name_len));
			fnc_name = ByteAry_.Mid(fnc_name, 0, fnc_name_colon_pos);
			fnc_name_ref.Val_(fnc_name);
		}
		return (KeyVal[])rv.XtoAry(KeyVal.class);
	}
//		private KeyVal[] ExpandTemplate_args(KeyVal[] values) {
//			KeyVal[] rv = Scrib_kv_utl.Val_to_KeyVal_ary(values, 2);
//			int rv_len = rv.length;
//			for (int i = 0; i < rv_len; i++) {	// iterate over args and convert any false boolean to a ""; PHP implicitly does this; fr.wikibooks.org/wiki/Chimie_organique/Sommaire; DATE:2014-01-02
//				KeyVal kv = rv[i];
//				Object kv_val = kv.Val();
//				if (ClassAdp_.Eq_typeSafe(kv_val, Bool_.ClassOf)) {
//					boolean kv_bool = Bool_.cast_(kv_val);
//					if (kv_bool)
//						kv.Val_(1);		// true => 1
//					else
//						kv.Val_(null);
//				}
//			}
//			return rv;
//		}
	public KeyVal[] ExpandTemplate(KeyVal[] values) {
		String ttl_str = Scrib_kv_utl.Val_to_str(values, 1);
		KeyVal[] args_ary = Scrib_kv_utl.Val_to_KeyVal_ary(values, 2);
		byte[] ttl_bry = ByteAry_.new_utf8_(ttl_str);
		Xoa_ttl ttl = Xoa_ttl.parse_(cur_wiki, ttl_bry);	// parse directly; handles titles where template is already part of title; EX: "Template:A"
		if (ttl == null) return Scrib_lib_mw.Rslts_none;	// invalid ttl;
		if (!ttl.ForceLiteralLink() && ttl.Ns().Id_main())	// title is not literal and is not prefixed with Template; parse again as template; EX: ":A" and "Template:A" are fine; "A" is parsed again as "Template:A"
			ttl = Xoa_ttl.parse_(cur_wiki, ByteAry_.Add(cur_wiki.Ns_mgr().Ns_template().Name_db_w_colon(), ttl_bry));	// parse again, but add "Template:"
		// BLOCK.bgn:Xot_invk_tkn.Transclude; cannot reuse b/c Transclude needs invk_tkn, and invk_tkn is manufactured late; DATE:2014-01-02
		byte[] sub_src = null;
		if (ttl.Ns().Id_tmpl()) {					// ttl is template; check tmpl_regy first before going to data_mgr
			Xot_defn_tmpl tmpl = (Xot_defn_tmpl)engine.Wiki().Cache_mgr().Defn_cache().Get_by_key(ttl.Page_db());
			if (tmpl != null) sub_src = tmpl.Data_raw();
		}
		if (sub_src == null)						// ttl is not in template cache, or is a ttl in non-Template ns; load title
			sub_src = engine.Wiki().Cache_mgr().Page_cache().Get_or_load_as_src(ttl);
		if (sub_src !=  null) {
			Xot_invk_mock sub_frame = Xot_invk_mock.new_(engine.Cur_frame_invoke().Defn_tid(), 0, args_ary);
			Xot_defn_tmpl transclude_tmpl = ctx.Wiki().Parser().Parse_tmpl(ctx, ctx.Tkn_mkr(), ttl.Ns(), ttl.Page_db(), sub_src);
			ByteAryBfr sub_bfr = cur_wiki.Utl_bry_bfr_mkr().Get_k004();
			transclude_tmpl.Tmpl_evaluate(ctx, sub_frame, sub_bfr);
			return Scrib_kv_utl.base1_obj_(sub_bfr.Mkr_rls().XtoStrAndClear());
		}
		else {
			String err_msg = "expand_template failed; ttl=" + ttl_str;
			cur_wiki.App().Usr_dlg().Warn_many("", "", err_msg);
			return Scrib_kv_utl.base1_obj_(err_msg);
		}
		// BLOCK.end:Xot_invk_tkn.Transclude
	}

	public KeyVal[] IncrementExpensiveFunctionCount(KeyVal[] values) {return Scrib_kv_utl.base1_obj_(KeyVal_.Ary_empty);}	// NOTE: for now, always return null (XOWA does not care about expensive parser functions)
	public KeyVal[] IsSubsting(KeyVal[] values) {
		boolean is_substing = false;
		Xot_invk current_frame = engine.Cur_frame_invoke();
		if	(current_frame != null && Xot_defn_.Tid_is_subst(current_frame.Defn_tid()))		// check current frame first
			is_substing = true;
		else {																				// check owner frame next
			Xot_invk owner_frame = engine.Cur_frame_owner();
			if (owner_frame != null && Xot_defn_.Tid_is_subst(owner_frame.Defn_tid()))
				is_substing = true;
		}			
		return Scrib_kv_utl.base1_obj_(is_substing);
	}
	public KeyVal[] NewChildFrame(KeyVal[] values) {
		return Scrib_kv_utl.base1_obj_(Scrib_kv_utl.Val_to_str(values, 1));	// HACK: return back name of frame; DATE:2014-01-25
	}
	private Xot_invk values_get_frame(KeyVal[] values, int idx) {
		String frame_id = Scrib_kv_utl.Val_to_str(values, idx);
		return String_.Eq(frame_id, "current") ? engine.Cur_frame_invoke() : engine.Cur_frame_owner();
	}
	private static final KeyVal[] Rslts_none = KeyVal_.Ary_empty;
}
class Scrib_lib_mw_callParserFunction_sorter implements gplx.lists.ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		KeyVal lhs = (KeyVal)lhsObj;
		KeyVal rhs = (KeyVal)rhsObj;
		Object lhs_key = lhs.Key_as_obj();
		Object rhs_key = rhs.Key_as_obj();
		boolean lhs_is_int = Int_.TypeMatch(lhs_key.getClass());
		boolean rhs_is_int = Int_.TypeMatch(rhs_key.getClass());
		if (lhs_is_int != rhs_is_int)									// different types (int vs String or String vs int)
			return lhs_is_int ? CompareAble_.Less : CompareAble_.More;	// sort ints before strings
		if (lhs_is_int)													// both are ints
			return Int_.Compare(Int_.cast_(lhs_key), Int_.cast_(rhs_key));
		else															// both are strings
			return String_.Compare(String_.as_or_fail_(lhs_key), String_.as_or_fail_(rhs_key));
	}
	public static final Scrib_lib_mw_callParserFunction_sorter _ = new Scrib_lib_mw_callParserFunction_sorter(); Scrib_lib_mw_callParserFunction_sorter() {}
}
