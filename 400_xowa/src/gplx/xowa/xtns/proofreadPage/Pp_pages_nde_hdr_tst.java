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
package gplx.xowa.xtns.proofreadPage; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Pp_pages_nde_hdr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void Init() {
		Io_mgr._.InitEngine_mem();
		fxt.Wiki().Db_mgr().Load_mgr().Clear(); // must clear; otherwise fails b/c files get deleted, but wiki.data_mgr caches the Xowd_regy_mgr (the .reg file) in memory;
		fxt.Wiki().Ns_mgr().Add_new(Xowc_xtn_pages.Ns_page_id_default, "Page").Add_new(Xowc_xtn_pages.Ns_index_id_default, "Index").Ords_sort();
		fxt.ini_page_create("MediaWiki:Proofreadpage_header_template", "value={{{value|nil}}}");
	}
	@Test  public void Basic() {
		fxt.ini_page_create("Index:A", "abc");
		fxt.tst_Parse_page_wiki_str("<pages index=\"A\"/>", String_.Concat_lines_nl
		(	"<p>value=toc"
		,	"</p>"
		,	""
		));
	}
}
