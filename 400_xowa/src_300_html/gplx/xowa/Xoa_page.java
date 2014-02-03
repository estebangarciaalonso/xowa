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
package gplx.xowa; import gplx.*;
import gplx.xowa.gui.*; import gplx.xowa.html.*;
import gplx.xowa.xtns.refs.*; import gplx.xowa.xtns.wdatas.*;
public class Xoa_page {		
	public Xoa_page(Xow_wiki wiki, Xoa_ttl ttl) {
		this.wiki = wiki; this.ttl = ttl;
		Ttl_(ttl);
		lang = wiki.Lang();	// default to wiki.lang; can be override later in wikitext
		hdr_mgr = new Xop_hdr_mgr(wiki, this);
	}	Xoa_page() {}	// called by Null
	public Xow_wiki			Wiki() {return wiki;} private Xow_wiki wiki;
	public Xol_lang			Lang() {return lang;} public Xoa_page Lang_(Xol_lang v) {lang = v; return this;} private Xol_lang lang;
	public int				Id() {return id;} public Xoa_page Id_(int v) {id = v; return this;} private int id;
	public DateAdp			Modified_on() {return modified_on;} public Xoa_page Modified_on_(DateAdp v) {modified_on = v; return this;} DateAdp modified_on = DateAdp_.MinValue;
	public boolean				Missing() {return missing;} public Xoa_page Missing_() {missing = true; return this;} private boolean missing;
	public Xoa_ttl			Ttl() {return ttl;} public Xoa_page Ttl_(Xoa_ttl v) {ttl = v; url.Wiki_bry_(wiki.Domain_bry()).Page_bry_(v.Full_url()); return this;} private Xoa_ttl ttl;
	public Xoa_url			Url() {return url;} public Xoa_page Url_(Xoa_url v) {url = v; return this;} private Xoa_url url = new Xoa_url();
	public boolean				Redirected() {return redirected;} public Xoa_page Redirected_(boolean v) {redirected = v; return this;} private boolean redirected;
	public ListAdp			Redirect_list() {return redirect_list;} private ListAdp redirect_list = ListAdp_.new_();
	public byte				Edit_mode() {return edit_mode;} private byte edit_mode; public void	Edit_mode_update_() {edit_mode = Xoa_page_.Edit_mode_update;}
	public byte[]			Search_text() {return search_text;} public Xoa_page Search_text_(byte[] v) {search_text = v; return this;} private byte[] search_text = ByteAry_.Empty;
	public byte[]			Display_ttl() {return display_ttl;} public Xoa_page Display_ttl_(byte[] v) {display_ttl = v; return this;} private byte[] display_ttl;
	public Xop_root_tkn		Root() {return root;} public Xoa_page Root_(Xop_root_tkn v) {root = v; return this;} private Xop_root_tkn root;
	public byte[]			Data_raw() {return data_raw;} public Xoa_page Data_raw_(byte[] v) {data_raw = v; return this;} private byte[] data_raw = ByteAry_.Empty;
	public byte[]			Data_preview() {return data_preview;} public Xoa_page Data_preview_(byte[] v) {data_preview = v; return this;} private byte[] data_preview = ByteAry_.Empty;
	public String			Html_bmk_pos() {return html_bmk_pos;} public Xoa_page Html_bmk_pos_(String v) {html_bmk_pos = v; return this;} private String html_bmk_pos;
	public boolean				Html_restricted() {return html_restricted;} public void Html_restricted_n_() {html_restricted = false;} private boolean html_restricted = true;
	public Xop_hdr_mgr		Hdr_mgr() {return hdr_mgr;} private Xop_hdr_mgr hdr_mgr;
	public ListAdp			Langs() {return langs;} private ListAdp langs = ListAdp_.new_();
	public Wdata_external_lang_links_data Wdata_external_lang_links() {return wdata_external_lang_links;} private Wdata_external_lang_links_data wdata_external_lang_links = new Wdata_external_lang_links_data();
	public ListAdp			Lnki_list() {return lnki_list;} private ListAdp lnki_list = ListAdp_.new_();
	public Xof_xfer_queue	File_queue() {return file_queue;} private Xof_xfer_queue file_queue = new Xof_xfer_queue();
	public ListAdp			File_math() {return file_math;} private ListAdp file_math = ListAdp_.new_();
	public Xoh_cmd_mgr		Html_cmd_mgr() {return html_cmd_mgr;} private Xoh_cmd_mgr html_cmd_mgr = new Xoh_cmd_mgr();
	public byte[][]			Category_list() {return category_list;} public Xoa_page Category_list_(byte[][] v) {category_list = v; return this;} private byte[][] category_list = new byte[0][];
	public Xtn_ref_mgr		Ref_mgr() {return ref_mgr;} public void Ref_mgr_(Xtn_ref_mgr v) {ref_mgr = v;} private Xtn_ref_mgr ref_mgr = new Xtn_ref_mgr();
	public void Clear() { // NOTE: this is called post-fetch but pre-parse; do not clear items set by post-fetch, such as id, ttl, redirect_list, data_raw
		html_restricted = true;
		search_text = ByteAry_.Empty;
		hdr_mgr.Clear();
		lnki_list.Clear();
		file_math.Clear();
		file_queue.Clear();
		ref_mgr.Grps_clear();
		html_cmd_mgr.Clear();
		langs.Clear();
		wdata_external_lang_links.Reset();
		gplx.xowa.xtns.scribunto.Scrib_engine.Engine_page_changed(this);
	}
	public static Xoa_page blank_page_(Xow_wiki wiki, Xoa_ttl ttl) {
		Xoa_page rv = new Xoa_page(wiki, ttl);
		rv.Data_raw_(ByteAry_.Empty);
		rv.Root_(wiki.Ctx().Tkn_mkr().Root(ByteAry_.Empty));
		rv.edit_mode = Xoa_page_.Edit_mode_create;
		return rv;
	}
	public static final Xoa_page Null = new Xoa_page().Missing_();
}
