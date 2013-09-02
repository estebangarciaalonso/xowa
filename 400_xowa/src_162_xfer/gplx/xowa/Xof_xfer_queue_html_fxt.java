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
import gplx.ios.*; import gplx.xowa.files.*;
public class Xof_xfer_queue_html_fxt extends Xof_xfer_queue_base_fxt {
	@Override public void Clear(boolean src_repo_is_wmf) {
		super.Clear(src_repo_is_wmf);
		this.Api_size().Clear();
	}
	public Xof_xfer_queue_html_fxt Lnki_orig_ (String lnki_ttl)							{return Lnki_(lnki_ttl, Bool_.N, Xof_url_.Null_size_deprecated, Xof_url_.Null_size_deprecated, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null);}
	public Xof_xfer_queue_html_fxt Lnki_thumb_(String lnki_ttl, int lnki_w)				{return Lnki_(lnki_ttl, Bool_.Y, lnki_w, Xof_url_.Null_size_deprecated, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null);}
	public Xof_xfer_queue_html_fxt Lnki_thumb_(String lnki_ttl, int lnki_w, int lnki_h) {return Lnki_(lnki_ttl, Bool_.Y, lnki_w, lnki_h, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null);}
	public Xof_xfer_queue_html_fxt Lnki_(String lnki_ttl, boolean thumb, int lnki_w, int lnki_h, double upright, int seek_time) { // NOTE: only one xfer_itm; supports one Lnki_ per test only
		Xow_wiki wiki = this.En_wiki();
		xfer_itm = wiki.Html_wtr().Lnki_wtr().Lnki_eval(queue, ByteAry_.new_utf8_(lnki_ttl), thumb ? Xop_lnki_type.Id_thumb : Xop_lnki_type.Id_null, lnki_w, lnki_h, upright, seek_time, false, queue_add_ref);
		return this;
	}	Xof_xfer_itm xfer_itm = new Xof_xfer_itm(); BoolRef queue_add_ref = BoolRef.false_();
	Xof_xfer_queue queue = new Xof_xfer_queue();
	public Xof_xfer_queue_html_fxt Src(Io_fil... v) {return (Xof_xfer_queue_html_fxt)Src_base(v);}
	public Xof_xfer_queue_html_fxt Trg(Io_fil... v) {return (Xof_xfer_queue_html_fxt)Trg_base(v);}
	public Xof_xfer_queue_html_fxt Html_src_(String v) {return (Xof_xfer_queue_html_fxt)Html_src_base_(v);}
	public Xof_xfer_queue_html_fxt Html_size_(int w, int h) {this.Html_w_(w); this.Html_h_(h); return this;}
	public Xof_xfer_queue_html_fxt Html_orig_src_(String v) {html_orig_src = v; return this;} private String html_orig_src;
	public Xof_xfer_queue_html_fxt ini_page_api(String wiki_str, String ttl_str, String redirect_str, int orig_w, int orig_h) {return ini_page_api(wiki_str, ttl_str, redirect_str, orig_w, orig_h, true);}
	public Xof_xfer_queue_html_fxt ini_page_api(String wiki_str, String ttl_str, String redirect_str, int orig_w, int orig_h, boolean pass) {
		String wiki_key = String_.Eq(wiki_str, "commons") ? "commons.wikimedia.org" : "en.wikipedia.org";
		this.Api_size().Ini(wiki_key, ttl_str, redirect_str, orig_w, orig_h, pass);
		return this;
	}
	public void tst() {
		Xow_wiki wiki = this.En_wiki();
		ini_src_fils();
		wiki.App().File_mgr().Download_mgr().Enabled_(true);
		wiki.File_mgr().Cfg_download().Enabled_(true);
		queue.Exec(note_wtr, wiki);
		tst_trg_fils();
		if (this.html_orig_src   != null)	Tfds.Eq(this.html_orig_src  , String_.new_utf8_(xfer_itm.Html_orig_src()));
		if (this.Html_view_src() != null)	Tfds.Eq(this.Html_view_src(), String_.new_utf8_(xfer_itm.Html_view_src()));
		if (this.Html_w() != -1)			Tfds.Eq(this.Html_w(), xfer_itm.Html_w());
		if (this.Html_h() != -1)			Tfds.Eq(this.Html_h(), xfer_itm.Html_h());
		queue.Clear();
	}	Xog_win_wtr note_wtr = Xog_win_wtr_null._;
}
