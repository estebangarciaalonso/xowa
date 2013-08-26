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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.json.*; import gplx.xowa.xtns.wdatas.*;
class Scrib_lib_wikibase_srl {
	public static KeyVal[] Srl(Wdata_doc_parser parser, Wdata_doc doc, boolean header_enabled) {// REF.MW:/Wikibase/lib/includes/serializers/EntitySerializer.php!getSerialized; http://www.mediawiki.org/wiki/Extension:Wikibase_Client/Lua
		ListAdp rv = ListAdp_.new_();
		if (header_enabled) {
			rv.Add(KeyVal_.new_("id", doc.Qid()));
			rv.Add(KeyVal_.new_("type", Wdata_doc_consts.Val_ent_entity_type_item_str));
		}
		Json_itm_nde root = doc.Doc().Root();
		int len = root.Subs_len();
		for (int i = 0; i < len; i++) {
			Json_itm_kv sub = (Json_itm_kv)root.Subs_get_at(i);
			String key = sub.Key_as_str();
			if			(String_.Eq(key, Wdata_doc_consts.Key_atr_label_str))			rv.Add(KeyVal_.new_("labels", Srl_nde_langs("language", "value", (Json_grp)sub.Val())));
			else if		(String_.Eq(key, Wdata_doc_consts.Key_atr_description_str))		rv.Add(KeyVal_.new_("descriptions", Srl_nde_langs("language", "value", (Json_grp)sub.Val())));
			else if		(String_.Eq(key, Wdata_doc_consts.Key_atr_links_str))			rv.Add(KeyVal_.new_("sitelinks", Srl_nde_langs("site", "title", (Json_grp)sub.Val())));
			else if		(String_.Eq(key, Wdata_doc_consts.Key_atr_aliases_str))			rv.Add(KeyVal_.new_("aliases", Srl_aliases((Json_grp)sub.Val())));
			else if		(String_.Eq(key, Wdata_doc_consts.Key_atr_claims_str))			rv.Add(KeyVal_.new_(Wdata_doc_consts.Key_atr_claims_str, Srl_claims(parser, doc.Doc().Src(), (Json_itm_ary)sub.Val())));
		}
		return (KeyVal[])rv.XtoAry(KeyVal.class);
	}
	private static KeyVal[] Srl_nde_langs(String key_label, String val_label, Json_grp grp) {
		int len = grp.Subs_len();
		KeyVal[] rv = new KeyVal[len];
		for (int i = 0; i < len; i++) {
			Json_itm_kv sub = (Json_itm_kv)grp.Subs_get_at(i);
			String lang = sub.Key_as_str();
			Object description = sub.Val().Data();
			rv[i] = KeyVal_.new_(lang, KeyVal_.Ary(KeyVal_.new_(key_label, lang), KeyVal_.new_(val_label, description)));
		}
		return rv;
	}
	private static KeyVal[] Srl_aliases(Json_grp grp) {
		int len = grp.Subs_len();
		KeyVal[] rv = new KeyVal[len];
		for (int i = 0; i < len; i++) {
			Json_itm_kv sub = (Json_itm_kv)grp.Subs_get_at(i);
			String lang = sub.Key_as_str();
			rv[i] = KeyVal_.new_(lang, Srl_aliases_langs(lang, (Json_grp)sub.Val()));
		}
		return rv;
	}
	private static KeyVal[] Srl_aliases_langs(String lang, Json_grp ary) {
		int len = ary.Subs_len();
		KeyVal[] rv = new KeyVal[len];
		for (int i = 0; i < len; i++) {
			Json_itm sub = ary.Subs_get_at(i);
			rv[i] = KeyVal_.int_(i, KeyVal_.Ary(KeyVal_.new_("language", lang), KeyVal_.new_("value", sub.Data())));	// NOTE: super 0
		}
		return rv;
	}
	private static KeyVal[] Srl_claims(Wdata_doc_parser parser, byte[] src, Json_itm_ary claims_nde) {
		OrderedHash props = parser.Bld_props(src, claims_nde);
		int len = props.Count();
		KeyVal[] rv = new KeyVal[len];
		for (int i = 0; i < len; i++) {
			Wdata_prop_grp grp = (Wdata_prop_grp)props.FetchAt(i);
			String pid_str = "p" + Int_.XtoStr(grp.Id());
			rv[i] = KeyVal_.new_(pid_str, Srl_claims_prop_grp(pid_str, grp));
		}
		return rv;
	}
	private static KeyVal[] Srl_claims_prop_grp(String pid, Wdata_prop_grp grp) {
		int len = grp.Itms_len();
		KeyVal[] rv = new KeyVal[len];
		for (int i = 0; i < len; i++) {
			Wdata_prop_itm_core itm = grp.Itms_get_at(i);
			rv[i] = KeyVal_.int_(i, Srl_claims_prop_itm(pid, itm));	// NOTE: super 0
		}
		return rv;
	}
	private static KeyVal[] Srl_claims_prop_itm(String pid, Wdata_prop_itm_core itm) {
		ListAdp list = ListAdp_.new_();
		list.Add(KeyVal_.new_("id", String_.new_utf8_(itm.Wguid())));
		list.Add(KeyVal_.new_("mainsnak", Srl_claims_prop_itm_core(pid, itm)));
		list.Add(KeyVal_.new_(Wdata_doc_consts.Key_claims_rank_str, itm.Rank_str()));
		list.Add(KeyVal_.new_("type", itm.Prop_type()));
		return (KeyVal[])list.XtoAryAndClear(KeyVal.class);
	}
	private static KeyVal[] Srl_claims_prop_itm_core(String pid, Wdata_prop_itm_core itm) {
		KeyVal[] rv = new KeyVal[3];
		rv[0] = KeyVal_.new_("datavalue", Srl_claims_prop_itm_core_val(itm));
		rv[1] = KeyVal_.new_("property", pid);
		rv[2] = KeyVal_.new_("snaktype", itm.Snak_str());
		return rv;
	}
	private static KeyVal[] Srl_claims_prop_itm_core_val(Wdata_prop_itm_core itm) {
		switch (itm.Val_tid_byte()) {
			case Wdata_prop_itm_base_.Val_tid_string	: return Srl_claims_prop_itm_core_str(itm);
			case Wdata_prop_itm_base_.Val_tid_entity	: return Srl_claims_prop_itm_core_entity(itm);
			case Wdata_prop_itm_base_.Val_tid_time		: return Srl_claims_prop_itm_core_time(itm);
			default: return KeyVal_.Ary_empty;
		}
	}
	private static KeyVal[] Srl_claims_prop_itm_core_str(Wdata_prop_itm_core itm) {
		KeyVal[] rv = new KeyVal[2];
		rv[0] = KeyVal_.new_(Key_type, itm.Val_tid_str());
		rv[1] = KeyVal_.new_(Key_value, String_.new_utf8_(itm.Val()));
		return rv;
	}
	private static KeyVal[] Srl_claims_prop_itm_core_entity(Wdata_prop_itm_core itm) {
		KeyVal[] rv = new KeyVal[2];
		rv[0] = KeyVal_.new_(Key_type, Wdata_prop_itm_base_.Val_str_entity);
		rv[1] = KeyVal_.new_(Key_value, Srl_claims_prop_itm_core_entity_value(itm));
		return rv;
	}
	private static KeyVal[] Srl_claims_prop_itm_core_entity_value(Wdata_prop_itm_core itm) {
		KeyVal[] rv = new KeyVal[2];
		rv[0] = KeyVal_.new_(Wdata_doc_consts.Key_ent_entity_type_str, Wdata_doc_consts.Val_ent_entity_type_item_str);
		rv[1] = KeyVal_.new_(Wdata_doc_consts.Key_ent_numeric_id_str, String_.new_utf8_(itm.Val()));
		return rv;
	}
	private static KeyVal[] Srl_claims_prop_itm_core_time(Wdata_prop_itm_core itm) {
		KeyVal[] rv = new KeyVal[2];
		rv[0] = KeyVal_.new_(Key_type, Wdata_doc_consts.Key_time_time_str);
		rv[1] = KeyVal_.new_(Key_value, Srl_claims_prop_itm_core_time_value(itm));
		return rv;
	}
	private static KeyVal[] Srl_claims_prop_itm_core_time_value(Wdata_prop_itm_core itm) {
		KeyVal[] rv = new KeyVal[6];
		rv[0] = KeyVal_.new_(Wdata_doc_consts.Key_time_time_str				, String_.new_ascii_(itm.Val()));
		rv[1] = KeyVal_.new_(Wdata_doc_consts.Key_time_precision_str		, Wdata_doc_consts.Val_time_precision_str);
		rv[2] = KeyVal_.new_(Wdata_doc_consts.Key_time_before_str			, Wdata_doc_consts.Val_time_before_str);
		rv[3] = KeyVal_.new_(Wdata_doc_consts.Key_time_after_str			, Wdata_doc_consts.Val_time_after_str);
		rv[4] = KeyVal_.new_(Wdata_doc_consts.Key_time_timezone_str			, Wdata_doc_consts.Val_time_timezone_str);
		rv[5] = KeyVal_.new_(Wdata_doc_consts.Key_time_calendarmodel_str	, Wdata_doc_consts.Val_time_calendarmodel_str);
		return rv;
	}
	static final String Key_type = "type", Key_value = "value";
}