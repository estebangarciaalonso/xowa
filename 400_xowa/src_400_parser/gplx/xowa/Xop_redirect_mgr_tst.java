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
import org.junit.*;
public class Xop_redirect_mgr_tst {		
	@Before public void init() {fxt.Clear();} private Xop_redirect_mgr_fxt fxt = new Xop_redirect_mgr_fxt();
	@Test  public void Basic()					{fxt.Test_redirect("#REDIRECT [[a]]", "A");}
	@Test  public void Basic_colon()			{fxt.Test_redirect("#REDIRECT:[[a]]", "A");}
	@Test  public void Ns_help()				{fxt.Test_redirect("#REDIRECT [[Help:a]]", "Help:A");}
	@Test  public void First()					{fxt.Test_redirect("#REDIRECT [[a]] [[b]]", "A");}
	@Test  public void Exc_false_match()		{fxt.Test_redirect("#REDIRECTA [[a]]", "");}
	@Test  public void Exc_lnki_not_found()		{fxt.Test_redirect("#REDIRECT test", "");}
	@Test  public void Ws()						{fxt.Test_redirect("\n#REDIRECT [[a]]", "A");}	// PAGE:en.w:Germany; {{Template group}} -> \n#REDIRECT [[Template:Navboxes]]
	@Test  public void Utf8() {
		fxt.Init_utf8();
		fxt.Init_kwds(Bool_.N, "#REDIRECT", "#перенаправление");
		fxt.Test_redirect("#REDIRECT [[A]]", "A");
		fxt.Test_redirect("#reDirect [[A]]", "A");
		fxt.Test_redirect("#перенаправление [[A]]", "A");
		fxt.Test_redirect("#ПЕРЕНАПРАВЛЕНИЕ [[A]]", "A");
	}
	@Test  public void Url_decode()				{fxt.Test_redirect("#REDIRECT [[A%28B%29]]"	, "A(B)");}		// PURPOSE: url-decode links; PAGE:en.w:Watcher_(Buffy_the_Vampire_Slayer); DATE:2014-08-18
	@Test  public void Url_decode_plus()		{fxt.Test_redirect("#REDIRECT [[A%28B%29+]]", "A(B)+");}	// PURPOSE: do not url-decode +; PAGE:en.w:Template:Positionskarte+; DATE:2014-08-22
	@Test  public void Amp()					{fxt.Test_redirect("#REDIRECT [[A &amp; B]]", "A & B");}	// PURPOSE: &amp; -> &; PAGE:en.w:Amadou Bagayoko?redirect=n; DATE:2014-09-23
	@Test  public void Frame_ttl() {	// PURPOSE: redirect should set invk frame title to redirect_trg, not original; PAGE:en.w:Statutory_city DATE:2014-08-22
		fxt.Test_frame_ttl("Template:A", "#REDIRECT [[Template:B]]", "Template:B", "Template:B");
	}
}
class Xop_redirect_mgr_fxt {
	private Xop_fxt fxt = new Xop_fxt();
	public void Clear() {
		fxt.Reset();
	}
	public void Init_kwds(boolean case_match, String... kwds) {fxt.Init_lang_kwds(Xol_kwd_grp_.Id_redirect, case_match, kwds);}
	public void Init_utf8() {
		fxt.Wiki().Lang().Case_mgr_utf8_();
	}
	public void Test_frame_ttl(String tmpl_ttl_str, String tmpl_wtxt_str, String redirect_ttl, String expd_frame_ttl) {
		fxt.Init_page_create(tmpl_ttl_str, tmpl_wtxt_str);								// create redirect_src
		fxt.Init_page_create(redirect_ttl, "test");										// create redirect_trg
		fxt.Test_parse_page_tmpl_tkn("{{" + tmpl_ttl_str + "}}");						// parse {{redirect_src}}
		Xoa_ttl tmpl_ttl = Xoa_ttl.parse_(fxt.Wiki(), Bry_.new_utf8_(tmpl_ttl_str));
		Xot_defn_tmpl defn_tmpl = (Xot_defn_tmpl)fxt.Wiki().Cache_mgr().Defn_cache().Get_by_key(tmpl_ttl.Page_db());	// get defn (which parse should have created)
		Tfds.Eq(expd_frame_ttl, String_.new_utf8_(defn_tmpl.Frame_ttl()));				// check frame_ttl
	}
	public void Test_redirect(String raw_str, String expd_str) {
		Xop_redirect_mgr redirect_mgr = fxt.Ctx().Wiki().Redirect_mgr();
		redirect_mgr.Clear();
		byte[] raw_bry = Bry_.new_utf8_(raw_str);
		Xoa_ttl actl_ttl = redirect_mgr.Extract_redirect(raw_bry, raw_bry.length);
		byte[] actl_bry = actl_ttl == null ? Bry_.Empty : actl_ttl.Full_txt();
		Tfds.Eq(expd_str, String_.new_utf8_(actl_bry));
	}
}
