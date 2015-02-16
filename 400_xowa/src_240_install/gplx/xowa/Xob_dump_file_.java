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
public class Xob_dump_file_ {
	public static boolean Connect_first(Xob_dump_file rv, String[] server_urls) {
		int len = server_urls.length;
		for (int i = 0; i < len; ++i) {
			String server_url = server_urls[i];
			rv.Server_url_(server_url);
			Override_dump_date(rv, server_url);
			if (rv.Connect()) return true;
		}
		return false;
	}
	private static void Override_dump_date(Xob_dump_file rv, String dump_server) {
		String dump_date = rv.Dump_date();
		if (	String_.Eq(dump_date, Xob_dump_file_.Date_latest)
			&&	(	String_.Eq(dump_server, Xob_dump_file_.Server_c3sl)
				||	String_.Eq(dump_server, Xob_dump_file_.Server_masaryk)
				)
			){
			Xoi_mirror_parser mirror_parser = new Xoi_mirror_parser();
			String dump_wiki_url = dump_server + String_.new_ascii_(rv.Wiki_alias()) + "/";
			byte[] dump_url_wiki_html = gplx.ios.IoEngine_xrg_downloadFil.new_("", Io_url_.Null).Exec_as_bry(dump_wiki_url); if (Bry_.Len_eq_0(dump_url_wiki_html)) return;
			String[] dump_available_dates = mirror_parser.Parse(String_.new_utf8_(dump_url_wiki_html));
			String dump_dates_latest = Xoi_mirror_parser.Find_last_lte(dump_available_dates, dump_date);
			if (String_.Eq(dump_dates_latest, "")) return;	// nothing found
			rv.Dump_date_(dump_dates_latest);
		}
	}
	public static byte[] Bld_dump_dir_url(byte[] server_url, byte[] alias, byte[] date) {
		return Bry_.Add
			(	server_url																	// "http://dumps.wikimedia.org/"
			,	Bry_.Replace(alias, Byte_ascii.Dash, Byte_ascii.Underline), Bry_slash	// "simplewiki/"
			,	date, Bry_slash																// "latest/"
			);
	}
	public static byte[] Bld_dump_file_name(byte[] alias, byte[] date, byte[] dump_file_type, byte[] ext) {
		return Bry_.Add
			(	Bry_.Replace(alias, Byte_ascii.Dash, Byte_ascii.Underline), Bry_dash	// "simplewiki-"
			,	date, Bry_dash																// "latest-"
			,	dump_file_type																// "pages-articles"
			,	ext																			// ".xml.bz2"
			);
	}
	private static final byte[] Bry_dash = new byte[] {Byte_ascii.Dash}, Bry_slash = new byte[] {Byte_ascii.Slash};
	public static final byte[] Ext_xml_bz2 = Bry_.new_ascii_(".xml.bz2");
	public static final byte[] Ext_sql_gz  = Bry_.new_ascii_(".sql.gz");
	public static final String 
		  Server_wmf			= "http://dumps.wikimedia.org/"
		, Server_your_org		= "http://dumps.wikimedia.your.org/"
		, Server_c3sl			= "http://wikipedia.c3sl.ufpr.br/"
		, Server_masaryk		= "http://ftp.fi.muni.cz/pub/wikimedia/"
		, Date_latest			= "latest"
		;
}
