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
public class Xowh_sidebar_mgr implements GfoInvkAble {
	public Xowh_sidebar_mgr(Xow_wiki wiki) {this.wiki = wiki;} private Xow_wiki wiki;		
	public int Grps_len() {return grps.Count();} ListAdp grps = ListAdp_.new_();
	public Xowh_sidebar_itm Grps_get_at(int i) {return (Xowh_sidebar_itm)grps.FetchAt(i);}
	public byte[] Html_bry() {return html_bry;} private byte[] html_bry;
	public void Init() {
		try {
			ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
			Xoa_ttl ttl = Xoa_ttl.parse_(wiki, CONST_sidebar_ttl);
			Xoa_page sidebar_page = wiki.Data_mgr().Get_page(ttl, false);
			if (sidebar_page == null) {html_bry = ByteAry_.Empty; return;}
			Parse(tmp_bfr, sidebar_page.Data_raw());
			Bld_html(tmp_bfr);
			html_bry = tmp_bfr.Mkr_rls().XtoAryAndClear();
		} catch (Exception e) {
			wiki.App().Usr_dlg().Warn_many(GRP_KEY, "sidebar.init", "sidebar init failed: ~{0} ~{1}", wiki.Key_str(), Err_.Message_gplx_brief(e));
			html_bry = ByteAry_.Empty;
		}
	}
	public void Parse(ByteAryBfr tmp_bfr, byte[] src) {
		byte[][] lines = ByteAry_.Split(src, Byte_ascii.NewLine);
		int lines_len = lines.length;
		Xoa_app app = wiki.App(); Url_encoder id_encoder = app.Url_converter_id();
		Xowh_sidebar_itm cur_grp = null;
		Xop_link_parser link_parser = new Xop_link_parser();
		for (int i = 0; i < lines_len; i++) {
			byte[] line = lines[i]; int line_len = line.length;
			if	(line_len == 0) continue;					// skip blank lines
			if	(line[0] != Byte_ascii.Asterisk) continue;	// skip non-list items; must begin with "*"
			byte tid = line[1] == Byte_ascii.Asterisk ? Xowh_sidebar_itm.Tid_itm : Xowh_sidebar_itm.Tid_grp;
			byte[] bry = ByteAry_.Trim(line, tid, line_len);	// trim *, **; note that tid indicates # of asterisks
			if 	(ByteAry_.Match(bry, CONST_itm_search) || ByteAry_.Match(bry, CONST_itm_toolbox) || ByteAry_.Match(bry, CONST_itm_languages)) continue;	// ignore SEARCH, TOOLBOX, LANGUAGES
			int pipe_pos = ByteAry_.FindFwd(bry, Byte_ascii.Pipe);
			byte[] text_key = tid == Xowh_sidebar_itm.Tid_grp ? bry : ByteAry_.Mid(bry, pipe_pos + 1, bry.length);	// get text_key; note that grp is entire bry, while itm is after |
			byte[] text_val = Resolve_key(text_key);
			byte[] id = id_encoder.Encode(tmp_bfr.Add(CONST_id_prefix), text_key).XtoAryAndClear();	// build id; "n-encoded_id"
			Xowh_sidebar_itm cur_itm = new Xowh_sidebar_itm(tid).Id_(id).Text_(text_val);
			wiki.Msg_mgr().Val_html_accesskey_and_title(id, tmp_bfr, cur_itm);
			if (tid == Xowh_sidebar_itm.Tid_grp) {
				cur_grp = cur_itm;
				grps.Add(cur_grp);
			}
			else {
				if (pipe_pos == ByteAry_.NotFound) {	// not of format of "href|main"; (EX: "href_only")
					wiki.App().Usr_dlg().Warn_many(GRP_KEY, "parse.line.missing_text", "pipe missing; only one word is available: ~{0}", String_.new_utf8_(bry));
					continue;
				}
				byte[] href_key = ByteAry_.Mid(bry, 0, pipe_pos);
				byte[] href_val = Resolve_key(href_key);
				href_val = link_parser.Parse(tmp_bfr, tmp_url, wiki, href_val, ByteAry_.Empty);
//					Xoa_ttl href_ttl = Xoa_ttl.parse_(wiki, href_val);
//					if (href_ttl != null && href_ttl.Wik_bgn() != -1) {	// NOTE: some href_vals can be invalid titles; EX: http://a.org/possibly_invalid_chars
//						Xop_link_parser.Link_add(tmp_bfr, Segs_bry, href_ttl.Wik_itm().Domain(), href_ttl.Page_db());
//						href_val = tmp_bfr.XtoAryAndClear();
//					}
//					else {
//						app.Href_parser().Parse(tmp_href, href_val, wiki, Xoa_page.Bry_main_page);
//						switch (tmp_href.Tid()) {
//							case Xoh_href.Tid_http:
//								break;
//							default:
//								href_val = ByteAry_.Add(Xoh_href_parser.Href_wiki_bry, href_val);	// href_key is actual ttl, not msg_key										
//								break;
//						}
//					}
				cur_itm.Href_(href_val);
				if (cur_grp == null)	// handle null_ref; should only occur for tests
					grps.Add(cur_itm);
				else
					cur_grp.Itms_add(cur_itm);
			}
		}
	}	static final byte[][] Segs_bry = new byte[][] {ByteAry_.new_ascii_("wiki")}; Xoh_href tmp_href = new Xoh_href(); Xoa_url tmp_url = new Xoa_url();
	public void Bld_html(ByteAryBfr bfr) {
		int len = grps.Count();
		for (int i = 0; i < len; i++) {
			Xowh_sidebar_itm grp = (Xowh_sidebar_itm)grps.FetchAt(i);
			html_grp_fmtr_arg.Grp_(grp, html_itm_fmtr);
			html_grp_fmtr.Bld_bfr_many(bfr, grp.Id(), grp.Text(), html_grp_fmtr_arg);
		}
	}	Xowh_sidebar_grp_fmtr_arg html_grp_fmtr_arg = new Xowh_sidebar_grp_fmtr_arg();
	ByteAryFmtr html_grp_fmtr = ByteAryFmtr.new_(String_.Concat_lines_nl
	(	"<div class=\"portal\" id='~{grp_id}'>"
	,	"  <h3>~{grp_text}</h3>"
	,	"  <div class=\"body\">"
	,	"    <ul>~{itms}"
	,	"    </ul>"
	,	"  </div>"
	,	"</div>")
	,	"grp_id", "grp_text", "itms");
	ByteAryFmtr html_itm_fmtr = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""
	,	"      <li id=\"~{itm_id}\"><a href=\"~{itm_href}\"~{itm_accesskey_and_title}>~{itm_text}</a></li>"
	),	"itm_id", "itm_href", "itm_accesskey_and_title", "itm_text"); 
	private static final byte[] CONST_itm_search = ByteAry_.new_ascii_("SEARCH"), CONST_itm_toolbox = ByteAry_.new_ascii_("TOOLBOX"), CONST_itm_languages = ByteAry_.new_ascii_("LANGUAGES"), CONST_id_prefix = ByteAry_.new_ascii_("n-");
	private static final String GRP_KEY = "xowa.wiki.gui.skin.mgr";
	byte[] Resolve_key(byte[] key) {
		byte[] val = wiki.Msg_mgr().Val_by_key_obj(key);
		if (ByteAry_.Len_eq_0(val)) val = key;	// if key is not found, default to val
		return Xop_parser_.Parse_fragment(wiki, val);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_html_grp_fmt_))		html_grp_fmtr.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_html_itm_fmt_))		html_itm_fmtr.Fmt_(m.ReadBry("v"));
		else											return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_html_grp_fmt_ = "html_grp_fmt_", Invk_html_itm_fmt_ = "html_itm_fmt_";
	private static final byte[] CONST_sidebar_ttl = ByteAry_.new_ascii_("MediaWiki:Sidebar");
}
