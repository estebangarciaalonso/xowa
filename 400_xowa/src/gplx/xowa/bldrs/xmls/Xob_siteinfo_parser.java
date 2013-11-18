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
package gplx.xowa.bldrs.xmls; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.xmls.*; // NOTE: gplx.xmls does not support Android; DATE:2013-01-17
public class Xob_siteinfo_parser {
	public static byte[] Siteinfo_extract(gplx.ios.Io_stream_rdr src_rdr) {
		Io_buffer_rdr rdr = Io_buffer_rdr.Null;
		try {
			rdr = Io_buffer_rdr.new_(src_rdr, Io_mgr.Len_mb);	// ASSUME: siteInfo is fully contained in the 1st MB of the src_xml
			byte[] src = rdr.Bfr();
			int bgn = ByteAry_.FindFwd(src, Bry_siteinfo_bgn, 0)  ; if (bgn == ByteAry_.NotFound) return null;
			int end = ByteAry_.FindFwd(src, Bry_siteinfo_end, bgn); if (end == ByteAry_.NotFound) return null;
			return ByteAry_.Mid(src, bgn, end + Bry_siteinfo_end.length);
		}
		catch (Exception e) {Err_.Noop(e); return null;}
		finally {rdr.Rls();}
	}
	public static void Siteinfo_parse(Xow_wiki wiki, Gfo_usr_dlg usr_dlg, String siteinfo_str) {
		XmlDoc xdoc = XmlDoc_.parse_(siteinfo_str);
		XmlNde root = xdoc.Root();
		int root_subs_len = root.SubNdes().Count();
		ByteAryBfr siteinfo_misc_bfr = ByteAryBfr.reset_(512);
		for (int i = 0; i < root_subs_len; i++) {
			XmlNde sub_nde = root.SubNdes().FetchAt(i);
			if		(	String_.Eq(sub_nde.Name(), "sitename")
					||	String_.Eq(sub_nde.Name(), "generator")
					||	String_.Eq(sub_nde.Name(), "case"))			siteinfo_misc_bfr.Add_str(sub_nde.Text_inner()).Add_byte_pipe();
			else if	(	String_.Eq(sub_nde.Name(), "base")) {
				String mainpage_url = sub_nde.Text_inner();
				byte[] mainpage_name = Siteinfo_parse_mainpage(ByteAry_.new_utf8_(mainpage_url)); if (mainpage_name == null) throw Err_mgr._.fmt_(GRP_KEY, "siteinfo.mainpage", "could not extract mainpage: ~{0}", mainpage_url);
				wiki.Props().Main_page_(mainpage_name);
			}
			else if (	String_.Eq(sub_nde.Name(), "namespaces")) {
				Siteinfo_parse_ns(wiki, usr_dlg, sub_nde);
			}
			else if (	String_.Eq(sub_nde.Name(), "#text")) {}	// NOTE: JAVA has node names for "#text"
			// else throw Err_mgr._.fmt_(GRP_KEY, "siteinfo.root.unknown_sub", "unknown sub for root nde: ~{0}", sub_nde.Name());	// NOTE: do not fail if MW introduces something odd in future (or if JAVA starts picking up other elements)
		}
		wiki.Props().Siteinfo_misc_(siteinfo_misc_bfr.XtoAryAndClear());
		wiki.Props().Bldr_version_(ByteAry_.new_ascii_(Xoa_app_.Version));
	}
	private static byte[] Siteinfo_parse_mainpage(byte[] url) {
		byte[] wiki_bry = ByteAry_.new_ascii_("/wiki/");
		int bgn_pos	= ByteAry_.FindFwd(url, wiki_bry, 0);
		if (bgn_pos == ByteAry_.NotFound) {							// "/wiki/" not found; EX: http://mywiki/My_main_page
			bgn_pos	= ByteAry_.FindBwd(url, Byte_ascii.Slash);		// ASSUME last segment is page
			if (bgn_pos == ByteAry_.NotFound) throw Err_.new_fmt_("could not parse main page url: {0}", String_.new_utf8_(url));
			++bgn_pos;												// add 1 to position after slash
		}
		else														// "/wiki/" found
			bgn_pos += wiki_bry.length;								// position bgn after "/wiki/"
		return ByteAry_.Mid(url, bgn_pos, url.length);
	}
	private static void Siteinfo_parse_ns(Xow_wiki wiki, Gfo_usr_dlg usr_dlg, XmlNde ns_nde) {
		int subs_len = ns_nde.SubNdes().Count();
		Xow_ns_mgr ns_mgr = wiki.Ns_mgr();
		ns_mgr.Clear();	// NOTE: wipe out any preexisting ns; use siteinfo.xml as definitive list
		for (int i = 0; i < subs_len; i++) {
			XmlNde sub_nde = ns_nde.SubNdes().FetchAt(i);
			if (sub_nde.Atrs().Count() == 0) continue; // NOTE: JAVA again has unexpected nodes
			try {
				int ns_id = Int_.parse_(sub_nde.Atrs().FetchValOr("key", ""));
				byte case_match = Xow_ns_.Case_match_parse_(sub_nde.Atrs().FetchValOr("case", ""));
				String name = sub_nde.Text_inner();
				ns_mgr.Add_new(ns_id, ByteAry_.new_utf8_(name), case_match, false);
			}
			catch (Exception e) {throw Err_mgr._.fmt_(GRP_KEY, "siteinfo.ns", "parse failed: ~{0} ~{1}", sub_nde.Text_inner(), Err_.Message_gplx_brief(e));}
		}
		ns_mgr.Init_done();
	}
	private static final byte[] Bry_siteinfo_bgn = ByteAry_.new_ascii_("<siteinfo>"), Bry_siteinfo_end = ByteAry_.new_ascii_("</siteinfo>");
	static final String GRP_KEY = "xowa.bldr.core.init";
}
