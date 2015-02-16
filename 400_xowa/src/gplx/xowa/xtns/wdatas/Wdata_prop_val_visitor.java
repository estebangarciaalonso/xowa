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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.xtns.wdatas.core.*;
class Wdata_prop_val_visitor implements Wdata_claim_visitor {
	private Wdata_wiki_mgr wdata_mgr; private Xoa_app app; private Bry_bfr bfr; private byte[] lang_key;
	public Wdata_prop_val_visitor(Xoa_app app, Wdata_wiki_mgr wdata_mgr) {this.app = app; this.wdata_mgr = wdata_mgr;}
	public void Init(Bry_bfr bfr, byte[] lang_key) {this.bfr = bfr; this.lang_key = lang_key;}
	public void Visit_str(Wdata_claim_itm_str itm)							{bfr.Add(itm.Val_str());}
	public void Visit_time(Wdata_claim_itm_time itm)						{bfr.Add(itm.Time());}
	public void Visit_monolingualtext(Wdata_claim_itm_monolingualtext itm)	{bfr.Add(itm.Text());}			// phrase only; PAGE:en.w:Alberta; EX: {{#property:motto}} -> "Fortis et libre"; DATE:2014-08-28
	public void Visit_entity(Wdata_claim_itm_entity itm) {
		Wdata_doc entity_doc = wdata_mgr.Pages_get(Bry_.Add(Byte_ascii.Ltr_q, itm.Entity_id_bry()));
		if (entity_doc == null) return;	// NOTE: wiki may refer to entity that no longer exists; EX: {{#property:p1}} which links to Q1, but p1 links to Q2 and Q2 was deleted; DATE:2014-02-01
		byte[] label = entity_doc.Label_list_get(lang_key);
		if (label == null && !Bry_.Eq(lang_key, Xol_lang_.Key_en))	// NOTE: some properties may not exist in language of wiki; default to english; DATE:2013-12-19
			label = entity_doc.Label_list_get(Xol_lang_.Key_en);
		if (label != null)	// if label is still not found, don't add null reference
			bfr.Add(label);
	}
	public void Visit_quantity(Wdata_claim_itm_quantity itm) {
		byte[] amount_bry = itm.Amount();
		int val = Bry_.Xto_int_or(amount_bry, Ignore_comma, 0, amount_bry.length, 0);
		Xol_lang lang = app.Lang_mgr().Get_by_key(lang_key);
		bfr.Add(lang.Num_mgr().Format_num(val));	// amount; EX: 1,234
		bfr.Add(Bry_quantity_margin_of_error);		// symbol: EX: ±
		bfr.Add(itm.Unit());						// unit;   EX: 1
	}
	public void Visit_globecoordinate(Wdata_claim_itm_globecoordinate itm) {
		bfr.Add(itm.Lat());
		bfr.Add_byte(Byte_ascii.Comma).Add_byte(Byte_ascii.Space);
		bfr.Add(itm.Lng());
	}
	public void Visit_system(Wdata_claim_itm_system itm) {}
	private static final byte[] Ignore_comma = new byte[]{Byte_ascii.Comma};
	private static final byte[] Bry_quantity_margin_of_error = Bry_.new_utf8_("±");
}
