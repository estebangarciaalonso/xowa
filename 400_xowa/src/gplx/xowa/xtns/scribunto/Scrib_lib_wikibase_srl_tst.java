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
import org.junit.*;
import gplx.xowa.xtns.wdatas.*;
import gplx.json.*;
public class Scrib_lib_wikibase_srl_tst {
	@Before public void init() {fxt.Clear();} private Scrib_lib_wikibase_srl_fxt fxt = new Scrib_lib_wikibase_srl_fxt();
	@Test   public void Label() {
		fxt.Init_label("en", "Earth").Init_label("fr", "Terre").Init_label("de", "Erde");
		fxt.Test
		(	"labels:"
		,	"  en:"
		,	"    language:'en'"
		,	"    value:'Earth'"
		,	"  fr:"
		,	"    language:'fr'"
		,	"    value:'Terre'"
		,	"  de:"
		,	"    language:'de'"
		,	"    value:'Erde'"
		,	""
		);
	}
	@Test   public void Description() {
		fxt.Init_description("en", "Earth").Init_description("fr", "Terre").Init_description("de", "Erde");
		fxt.Test
		(	"descriptions:"
		,	"  en:"
		,	"    language:'en'"
		,	"    value:'Earth'"
		,	"  fr:"
		,	"    language:'fr'"
		,	"    value:'Terre'"
		,	"  de:"
		,	"    language:'de'"
		,	"    value:'Erde'"
		,	""
		);
	}
	@Test   public void Sitelinks() {
		fxt.Init_link("enwiki", "Earth").Init_link("frwiki", "Terre").Init_link("dewiki", "Erde");
		fxt.Test
		(	"sitelinks:"
		,	"  enwiki:"
		,	"    site:'enwiki'"
		,	"    title:'Earth'"
		,	"  frwiki:"
		,	"    site:'frwiki'"
		,	"    title:'Terre'"
		,	"  dewiki:"
		,	"    site:'dewiki'"
		,	"    title:'Erde'"
		,	""
		);
	}
	@Test   public void Sitelinks_both_formats() {	// PURPOSE: check that both formats are serializable; DATE:2014-02-06
		Json_doc jdoc = Json_doc.new_apos_concat_nl
		(	"{ 'entity':['item',2]"
		,	", 'links':"
		,	"  {"
		,	"    'enwiki':'Earth'"							// old format
		,	"  , 'frwiki':{'name':'Terre','badges':[]}"		// new format
		,	"  }"
		,	"}"
		);
		Wdata_doc wdoc = new Wdata_doc(ByteAry_.new_ascii_("q2"), fxt.Wdata_fxt().App().Wiki_mgr().Wdata_mgr(), jdoc);
		fxt.Test
		(	wdoc
		,	"sitelinks:"
		,	"  enwiki:"
		,	"    site:'enwiki'"
		,	"    title:'Earth'"
		,	"  frwiki:"
		,	"    site:'frwiki'"
		,	"    title:'Terre'"
		,	""
		);
	}
	@Test   public void Aliases() {
		fxt.Init_alias("en", "en_0", "en_1", "en_2").Init_alias("fr", "fr_0").Init_alias("de", "de_0", "de_1");
		fxt.Test
		(	"aliases:"
		,	"  en:"
		,	"    0:"
		,	"      language:'en'"
		,	"      value:'en_0'"
		,	"    1:"
		,	"      language:'en'"
		,	"      value:'en_1'"
		,	"    2:"
		,	"      language:'en'"
		,	"      value:'en_2'"
		,	"  fr:"
		,	"    0:"
		,	"      language:'fr'"
		,	"      value:'fr_0'"
		,	"  de:"
		,	"    0:"
		,	"      language:'de'"
		,	"      value:'de_0'"
		,	"    1:"
		,	"      language:'de'"
		,	"      value:'de_1'"
		,	""
		);
	}	
	@Test   public void Claims_str() {
		fxt.Init_prop(fxt.Wdata_fxt().prop_str_(2, "Moon"));
		fxt.Test
		(	"claims:"
		,	"  p2:"
		,	"    0:"
		,	"      id:null"
		,	"      mainsnak:"
		,	"        datavalue:"
		,	"          type:'string'"
		,	"          value:'Moon'"
		,	"        property:'p2'"
		,	"        snaktype:'value'"
		,	"      rank:'normal'"
		,	"      type:'statement'"        
		,	""
		);
	}
	@Test   public void Claims_entity() {
		fxt.Init_prop(fxt.Wdata_fxt().prop_entity_(2, 3));
		fxt.Test
		(	"claims:"
		,	"  p2:"
		,	"    0:"
		,	"      id:null"
		,	"      mainsnak:"
		,	"        datavalue:"
		,	"          type:'wikibase-entityid'"
		,	"          value:"
		,	"            entity-type:'item'"
		,	"            numeric-id:'3'"
		,	"        property:'p2'"
		,	"        snaktype:'value'"
		,	"      rank:'normal'"
		,	"      type:'statement'"        
		,	""
		);
	}
	@Test   public void Claims_time() {
		fxt.Init_prop(fxt.Wdata_fxt().prop_time_(2, "2001-02-03 04:05:06"));
		fxt.Test
		(	"claims:"
		,	"  p2:"
		,	"    0:"
		,	"      id:null"
		,	"      mainsnak:"
		,	"        datavalue:"
		,	"          type:'time'"
		,	"          value:"
		,	"            time:'+00000002001-02-03T04:05:06Z'"
		,	"            precision:'11'"
		,	"            before:'0'"
		,	"            after:'0'"
		,	"            timezone:'0'"
		,	"            calendarmodel:'http://www.wikidata.org/entity/Q1985727'"
		,	"        property:'p2'"
		,	"        snaktype:'value'"
		,	"      rank:'normal'"
		,	"      type:'statement'"        
		,	""
		);
	}
}	
class Scrib_lib_wikibase_srl_fxt {
	public void Clear() {
		wdata_fxt = new Wdata_wiki_mgr_fxt();
		wdata_fxt.Init();
		doc_bldr = wdata_fxt.page_bldr_("q2");
		header_enabled = false;
		parser = new Wdata_doc_parser(wdata_fxt.App().Usr_dlg());
	}
	public Wdata_wiki_mgr_fxt Wdata_fxt() {return wdata_fxt;} private Wdata_wiki_mgr_fxt wdata_fxt;
	private Wdata_doc_bldr doc_bldr;
	private boolean header_enabled;
	private Wdata_doc_parser parser;
	public Scrib_lib_wikibase_srl_fxt Init_header_enabled_y_() {header_enabled = true; return this;}
	public Scrib_lib_wikibase_srl_fxt Init_label(String lang, String label) {
		doc_bldr.Label_add(lang, label);
		return this;
	}
	public Scrib_lib_wikibase_srl_fxt Init_description(String lang, String description) {
		doc_bldr.Description_add(lang, description);
		return this;
	}
	public Scrib_lib_wikibase_srl_fxt Init_link(String xwiki, String val) {
		doc_bldr.Link_add(xwiki, val);
		return this;
	}
	public Scrib_lib_wikibase_srl_fxt Init_alias(String lang, String... ary) {
		doc_bldr.Alias_add(lang, ary);
		return this;
	}
	public Scrib_lib_wikibase_srl_fxt Init_prop(Wdata_prop_itm_core prop) {doc_bldr.Props_add(prop); return this;}
	public Scrib_lib_wikibase_srl_fxt Test(String... expd) {
		KeyVal[] actl = Scrib_lib_wikibase_srl.Srl(parser, doc_bldr.Xto_page_doc(), header_enabled);
		Tfds.Eq_ary_str(expd, String_.SplitLines_nl(Xto_str(actl)));
		return this;
	}
	public Scrib_lib_wikibase_srl_fxt Test(Wdata_doc wdoc, String... expd) {
		KeyVal[] actl = Scrib_lib_wikibase_srl.Srl(parser, wdoc, header_enabled);
		Tfds.Eq_ary_str(expd, String_.SplitLines_nl(Xto_str(actl)));
		return this;
	}
	private String Xto_str(KeyVal[] ary) {
		ByteAryBfr bfr = ByteAryBfr.new_();
		Xto_str(bfr, ary, 0);
		return bfr.XtoStrAndClear();
	}
	private void Xto_str(ByteAryBfr bfr, KeyVal[] ary, int depth) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			KeyVal kv = ary[i];
			Xto_str(bfr, kv, depth);
		}
	}
	private void Xto_str(ByteAryBfr bfr, KeyVal kv, int depth) {
		bfr.Add_byte_repeat(Byte_ascii.Space, depth * 2);
		bfr.Add_str(kv.Key()).Add_byte(Byte_ascii.Colon);
		Object kv_val = kv.Val();
		if		(kv_val == null) 							{bfr.Add_str("null").Add_byte_nl(); return;}
		Class<?> kv_val_cls = kv_val.getClass();
		if 	(ClassAdp_.Eq(kv_val_cls, KeyVal[].class)) 	{bfr.Add_byte_nl(); Xto_str(bfr, (KeyVal[])kv_val, depth + 1);}
		else if (ClassAdp_.Eq(kv_val_cls, KeyVal[].class)) 	{bfr.Add_byte_nl(); Xto_str(bfr, (KeyVal)kv_val, depth + 1);}
		else bfr.Add_byte(Byte_ascii.Apos).Add_str(Object_.XtoStr_OrEmpty(kv_val)).Add_byte(Byte_ascii.Apos).Add_byte_nl();
	}
}
	/*
p107:
	1:
		id: q7259$FEE5069C-DF8E-4355-A973-2B0B8BC339A8
		mainsnak:
			datavalue:
			type: wikibase-entityid
			value:
				entity-type: item
				numeric-id: 215627
			property: p107
			snaktype: value
		references:
			0:
			hash: 827686a10b6e2126886dbc6755a3027fb443996
			snaks:
				p143:
					0:
						snaktype: value
						property: p143
						datavalue:
						type: wikibase-entityid
						value:
							numeric-id: 48952
							entity-type: item
		qualifiers:
			p459:
			0:
				snaktype: value
				property: p459
				datavalue:
					type: wikibase-entityid
					value:
						numeric-id: 670933
						entity-type: item
		rank: normal
		type: statement
*/
