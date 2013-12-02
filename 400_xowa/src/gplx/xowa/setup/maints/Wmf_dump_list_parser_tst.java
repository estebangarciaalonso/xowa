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
package gplx.xowa.setup.maints; import gplx.*; import gplx.xowa.*; import gplx.xowa.setup.*;
import org.junit.*;
public class Wmf_dump_list_parser_tst {
	@Before public void init() {fxt.Clear();} private Wmf_dump_list_parser_fxt fxt = new Wmf_dump_list_parser_fxt();
	@Test  public void Parse() {
		fxt.Test_parse
		(	"<li>2013-07-17 00:32:33 <a href=\"http://dumps.wikimedia.org/enwiki/20130708\">enwiki</a>: <span class=\"done\">Dump complete</span></li>"
		,	fxt.itm("enwiki", "20130708", "y", "Dump complete", "2013-07-17 00:32:33")
		);
		fxt.Test_parse(String_.Concat_lines_nl
		(	"<li>2013-07-24 02:02:13 <a href=\"http://dumps.wikimedia.org/kawiki/20130724\">kawiki</a>: <span class=\"in-progress\">Dump in progress</span></li>"
		,	"<ul><li class=\"in-progress\"><span class=\"updates\">2013-07-24 00:54:55</span> <span class=\"status\">in-progress</span> <span class=\"title\">All pages with complete page edit history (.bz2)</span><div class=\"progress\">2013-07-24 02:02:13: kawiki (ID 18587) 22046 pages (5.5|11140.9/sec all|curr), 869000 revs (215.2|505.3/sec all|curr), 99.9%|99.9% prefetched (all|curr), ETA 2013-07-24 04:09:41 [max 2514872]</div>"
		,	"<ul><li class=\"file\">kawiki-20130724-pages-meta-history.xml.bz2 245.2 MB (written) </li></ul></li></ul>"
		)
		,	fxt.itm("kawiki", "20130724", "n", "Dump in progress", "2013-07-24 02:02:13")
		);
		fxt.Test_parse
		(	"<li>2013-07-17 00:32:33 <a href=\"http://dumps.wikimedia.org/enwiki/20130708\">enwiki</a>: <span class=\"done\">Error</span></li>"
		,	fxt.itm("enwiki", "20130708", "n", "Error", "2013-07-17 00:32:33")
		);
		fxt.Test_parse
		(	"<li>2013-11-28 06:08:56 <a href=\"zh_classicalwiki/20131128\">zh_classicalwiki</a>: <span class='done'>Dump complete</span></li>"
		,	fxt.itm("zh-classicalwiki", "20131128", "y", "Dump complete", "2013-11-28 06:08:56")
		);
	}
}
class Wmf_dump_list_parser_fxt {
	public void Clear() {}
	private Wmf_dump_list_parser parser = new Wmf_dump_list_parser();
	public String itm(String wiki_abrv, String dump_date, String status_done, String status_msg, String status_time) {
		return String_.Concat_with_str("\n", wiki_abrv, dump_date
		, status_done
		, status_msg
		, status_time
		);
	}
	public void Test_parse(String raw, String... expd) {
		Wmf_dump_itm[] actl = parser.Parse(ByteAry_.new_ascii_(raw));
		Tfds.Eq_str_lines(String_.Concat_lines_nl(expd), String_.Concat_lines_nl(Xto_str(actl)));
	}
	public String[] Xto_str(Wmf_dump_itm[] ary) {
		int len = ary.length;
		String[] rv = new String[len];
		for (int i = 0; i < len; i++)
			rv[i] = Xto_str(ary[i]); 
		return rv;
	}
	public static String Xto_str(Wmf_dump_itm itm) {
		DateAdp status_time = itm.Status_time();
		String status_time_str = status_time == null ? "" : status_time.XtoStr_fmt(DateAdp_.Fmt_iso8561_date_time); 
		return String_.Concat_with_str("\n", String_.new_ascii_(itm.Wiki_abrv()), itm.Dump_date().XtoStr_fmt("yyyyMMdd")
			, Yn.XtoStr(itm.Status_completed())
			, String_.new_ascii_(itm.Status_msg())
			, status_time_str
			);
		
	}
}
