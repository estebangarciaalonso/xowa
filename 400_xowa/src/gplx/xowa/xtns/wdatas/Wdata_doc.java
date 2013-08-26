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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.json.*;
public class Wdata_doc {
	public Wdata_doc(byte[] qid, Wdata_wiki_mgr mgr, Json_doc doc) {this.qid = qid; this.mgr = mgr; this.doc = doc;} Wdata_wiki_mgr mgr;
	public Json_doc Doc() {return doc;} Json_doc doc;
	public byte[] Qid() {return qid;} private byte[] qid;
	public OrderedHash Links() {if (links == null) links = mgr.Page_doc_parser().Bld_hash(doc, Wdata_doc_consts.Key_atr_links_bry); return links;}	OrderedHash links;
	public byte[] Label_get(byte[] lang_key) {
		if (labels == null) labels = mgr.Page_doc_parser().Bld_hash(doc, Wdata_doc_consts.Key_atr_label_bry);
		Object rv = labels.Fetch(lang_key); if (rv == null) return null;
		Json_itm_kv kv = (Json_itm_kv)rv;
		return kv.Val().Data_bry();
	}	OrderedHash labels;
	public int Props_len() {
		if (props == null) props = mgr.Page_doc_parser().Bld_props(doc);
		return props.Count();
	}
	public Wdata_prop_grp Prop_get(int pid) {
		if (props == null) props = mgr.Page_doc_parser().Bld_props(doc);
		return (Wdata_prop_grp)props.Fetch(mgr.Tmp_prop_ref().Val_(pid));
	}	OrderedHash props;
}
