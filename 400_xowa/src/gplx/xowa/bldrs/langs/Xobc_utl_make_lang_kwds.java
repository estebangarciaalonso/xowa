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
package gplx.xowa.bldrs.langs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
public class Xobc_utl_make_lang_kwds implements GfoInvkAble, Xol_lang_transform {
	public Xobc_utl_make_lang_kwds(Xoa_app app) {this.app = app;} private Xoa_app app;
	
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_set))					Set(m.ReadBry("langs"), m.ReadBry("text"));
		else if	(ctx.Match(k, Invk_keep_trailing_colon))	Parse_keep_trailing_colon(m.ReadBry("langs"), m.ReadBry("text"));
		else if	(ctx.Match(k, Invk_prepend_hash))			Parse_prepend_hash(m.ReadBry("langs"), m.ReadBry("text"));
		else if	(ctx.Match(k, Invk_add_words))				Parse_add_words(m.ReadBry("langs"), m.ReadBry("text"));
		else												return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_set = "set", Invk_keep_trailing_colon = "keep_trailing_colon", Invk_prepend_hash = "prepend_hash", Invk_add_words = "add_words";

	public byte[] Kwd_transform(byte[] lang_key, byte[] kwd_key, byte[] kwd_word) {
		byte[] rv = kwd_word;
		if (!Hash_itm_applies(trailing_colons, lang_key, kwd_key, kwd_word)) {
			int kwd_last = rv.length - 1;
			if (kwd_last > 0 && rv[kwd_last] == Byte_ascii.Colon)
				rv = Bry_.Mid(rv, 0, rv.length - 1);
		}
		if (Hash_itm_applies(prepend_hash, lang_key, kwd_key, kwd_word)) {
			if (rv.length > 0 && rv[0] != Byte_ascii.Hash)
				rv = Bry_.Add(Byte_ascii.Hash, rv);
		}
		return rv;
	}
	public void Add_words() {
		OrderedHash hash = add_words_hash;
		OrderedHash tmp = OrderedHash_.new_bry_();
		int hash_len = hash.Count();
		for (int i = 0; i < hash_len; i++) {
			Xobcl_kwd_lang cfg_lang = (Xobcl_kwd_lang)hash.FetchAt(i); 
			Xol_lang lang = app.Lang_mgr().Get_by_key(cfg_lang.Key_bry()); if (lang == null) continue;
			int cfg_grp_len = cfg_lang.Grps().length;
			for (int j = 0; j < cfg_grp_len; j++) {					
				Xobcl_kwd_row cfg_grp = cfg_lang.Grps()[j];
				int kwd_id = Xol_kwd_grp_.Id_by_bry(cfg_grp.Key());
				if (kwd_id == -1) throw Err_.new_("could not find kwd for key: " + String_.new_utf8_(cfg_grp.Key()));
				Xol_kwd_grp kwd_grp = lang.Kwd_mgr().Get_at(kwd_id);
				tmp.Clear();
				if (kwd_grp == null) {
					kwd_grp = lang.Kwd_mgr().Get_or_new(kwd_id);
					kwd_grp.Srl_load(Bool_.N, Bry_.Ary_empty);	// ASSUME: kwd explicitly added, but does not exist in language; default to !case_match
				}

				for (Xol_kwd_itm itm : kwd_grp.Itms())
					tmp.Add(itm.Val(), itm.Val());
				if (cfg_grp.Itms().length == 0) {
					if (!tmp.Has(cfg_grp.Key())) tmp.Add(cfg_grp.Key(), cfg_grp.Key());
				}
				else {
					for (byte[] itm : cfg_grp.Itms()) {
						if (!tmp.Has(itm)) tmp.Add(itm, itm);
					}
				}
				byte[][] words = (byte[][])tmp.Xto_ary(byte[].class);
				kwd_grp.Srl_load(kwd_grp.Case_match(), words);
			}
		}
	}
	boolean Hash_itm_applies(OrderedHash hash, byte[] lang_key, byte[] kwd_key, byte[] kwd_word) {
		Xobcl_kwd_lang cfg_lang = (Xobcl_kwd_lang)hash.Fetch(lang_key); if (cfg_lang == null) return false;
		Xobcl_kwd_row cfg_grp = cfg_lang.Grps_get_by_key(kwd_key); if (cfg_grp == null) return false;
		return cfg_grp.Itms().length == 0 || cfg_grp.Itms_has(kwd_word);
	}
	public void Set(byte[] langs_bry, byte[] kwds) {
		OrderedHash langs = app.Lang_mgr().Xto_hash(langs_bry);
		int langs_len = langs.Count();
		Xol_kwd_mgr kwd_mgr = new Xol_kwd_mgr(app.Lang_mgr().Lang_en());
		for (int i = 0; i < langs_len; i++) {
//				Xol_lang_itm lang = (Xol_lang_itm)langs.FetchAt(i);
			Xol_lang_srl.Load_keywords(kwd_mgr, kwds);
		}
	}
	public void Parse_add_words(byte[] langs_bry, byte[] kwds) {Parse(langs_bry, kwds, add_words_hash);}	private OrderedHash add_words_hash = OrderedHash_.new_bry_();
	public void Parse_prepend_hash(byte[] langs_bry, byte[] kwds) {Parse(langs_bry, kwds, prepend_hash);}	private OrderedHash prepend_hash = OrderedHash_.new_bry_();
	public void Parse_keep_trailing_colon(byte[] langs_bry, byte[] kwds) {Parse(langs_bry, kwds, trailing_colons);}	private OrderedHash trailing_colons = OrderedHash_.new_bry_();
	private void Parse(byte[] langs_bry, byte[] kwds, OrderedHash hash) {
		Xobcl_kwd_row[] itms = Parse(kwds);
		OrderedHash langs = app.Lang_mgr().Xto_hash(langs_bry);
		int langs_len = langs.Count();
		for (int i = 0; i < langs_len; i++) {
			Xoac_lang_itm itm = (Xoac_lang_itm)langs.FetchAt(i);
			byte[] key_bry = itm.Key_bry();

			Xobcl_kwd_lang grp = (Xobcl_kwd_lang)hash.Fetch(key_bry);
			if (grp == null) {
				grp = new Xobcl_kwd_lang(key_bry, itms); 
				hash.Add(key_bry, grp);
			}
			else
				grp.Merge(itms);
		}
	}
	public static Xobcl_kwd_row[] Parse(byte[] src) {
		int src_len = src.length, pos = 0, fld_bgn = 0;
		byte[] cur_key = Bry_.Empty;
		Xol_csv_parser csv_parser = Xol_csv_parser._;
		ListAdp rv = ListAdp_.new_(); int fld_idx = 0;
		while (true) {
			boolean last = pos == src_len;	// NOTE: logic occurs b/c of \n}~-> dlm which gobbles up last \n
			byte b = last ? Byte_ascii.NewLine : src[pos];
			switch (b) {
				case Byte_ascii.Pipe:
					cur_key = csv_parser.Load(src, fld_bgn, pos);
					fld_bgn = pos + 1;
					++fld_idx;
					break;
				case Byte_ascii.NewLine:
					if (pos - fld_bgn > 0 || fld_idx == 1) {
						byte[] cur_val = csv_parser.Load(src, fld_bgn, pos);
						Xobcl_kwd_row row = new Xobcl_kwd_row(cur_key, Bry_.Split(cur_val, Byte_ascii.Tilde));
						rv.Add(row);
					}
					fld_bgn = pos + 1;
					fld_idx = 0;
					break;
				default:
					break;
			}
			if (last) break;
			++pos;
		}		
		return (Xobcl_kwd_row[])rv.Xto_ary(Xobcl_kwd_row.class);
	}
}
class Xobcl_kwd_lang {
	public Xobcl_kwd_lang(byte[] key_bry, Xobcl_kwd_row[] grps) {
		this.key_bry = key_bry; this.grps = grps;
		for (Xobcl_kwd_row grp : grps)
			grps_hash.Add(grp.Key(), grp);
	}
	public void Merge(Xobcl_kwd_row[] v) {
		grps = (Xobcl_kwd_row[])Array_.Resize_add(grps, v);
		for (Xobcl_kwd_row grp : v) {
			grps_hash.AddReplace(grp.Key(), grp);	// NOTE: AddReplace instead of Add b/c kwds may be expanded; EX: lst is added to all langs but de requires #lst~#section~Abschnitt~; DATE:2013-06-02
		}
	}
	public Xobcl_kwd_row Grps_get_by_key(byte[] key) {return (Xobcl_kwd_row)grps_hash.Fetch(key);} private OrderedHash grps_hash = OrderedHash_.new_bry_();
	public byte[] Key_bry() {return key_bry;} private byte[] key_bry;
	public Xobcl_kwd_row[] Grps() {return grps;} private Xobcl_kwd_row[] grps;
}
