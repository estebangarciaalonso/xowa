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
package gplx.xowa; import gplx.*;
public class Xow_wiki_props implements GfoInvkAble {
	public byte[] SiteName() {return siteName;} public Xow_wiki_props SiteName_(byte v) {siteName = ByteAry_.new_ascii_(String_.UpperFirst(String_.new_ascii_(Xow_wiki_type_.Name_by_tid(v)))); return this;} private byte[] siteName = ByteAry_.Empty;
	public byte[] ServerName() {return serverName;} public Xow_wiki_props ServerName_(byte[] v) {serverName = v; server = ByteAry_.Add(bry_http, v); return this;} private byte[] serverName = ByteAry_.new_ascii_("localhost");
	public byte[] Server() {return server;} private byte[] server = ByteAry_.new_ascii_("http://localhost"); static final byte[] bry_http = ByteAry_.new_ascii_("http://");
	public byte[] ArticlePath() {return articlePath;} public Xow_wiki_props ArticlePath_(byte[] v) {articlePath = v; return this;} private byte[] articlePath = Xoh_href_parser.Href_wiki_bry;
	public byte[] ScriptPath() {return scriptPath;} public Xow_wiki_props ScriptPath_(byte[] v) {scriptPath = v; return this;} private byte[] scriptPath = ByteAry_.new_ascii_("/wiki");
	public byte[] StylePath() {return stylePath;} public Xow_wiki_props StylePath_(byte[] v) {stylePath = v; return this;} private byte[] stylePath = ByteAry_.new_ascii_("/wiki/skins");
	public byte[] ContentLanguage() {return contentLanguage;} public Xow_wiki_props ContentLanguage_(byte[] v) {contentLanguage = v; return this;} private byte[] contentLanguage = ByteAry_.Empty;
	public byte[] DirectionMark() {return directionMark;} public Xow_wiki_props DirectionMark_(byte[] v) {directionMark = v; return this;} private byte[] directionMark = ByteAry_.Empty;
	public byte[] CurrentVersion() {return MediaWiki_version;}
	public byte[] Main_page() {return main_page;} public Xow_wiki_props Main_page_(byte[] v) {main_page = v; return this;} private byte[] main_page = Xoa_page.Bry_main_page;
	public byte[] Bldr_version() {return bldr_version;} public Xow_wiki_props Bldr_version_(byte[] v) {bldr_version = v; return this;} private byte[] bldr_version = ByteAry_.Empty;
	public int Css_version() {return css_version;} public Xow_wiki_props Css_version_(int v) {css_version = v; return this;} private int css_version = 1;
	public byte[] Siteinfo_misc() {return siteinfo_misc;}
	public byte[] Siteinfo_mainpage() {return siteinfo_mainpage;} public Xow_wiki_props Siteinfo_mainpage_(byte[] v) {siteinfo_mainpage = v; return this;} private byte[] siteinfo_mainpage = ByteAry_.Empty; // no actual use; retained for troubleshooting purposes
	public DateAdp Modified_latest() {return modified_latest;} DateAdp modified_latest;
	public void Main_page_update(Xow_wiki wiki) {
		siteinfo_mainpage = main_page;
		main_page = gplx.xowa.Xow_mainpage_finder.Find(wiki);
	}
	public Xow_wiki_props Siteinfo_misc_(byte[] v) {
		siteinfo_misc = v;
		int pipe_0 = ByteAry_.FindFwd(v, Byte_ascii.Pipe);
		if (pipe_0 != ByteAry_.NotFound)
			siteName = ByteAry_.Mid(siteinfo_misc, 0, pipe_0);
		return this;
	} byte[] siteinfo_misc = ByteAry_.Empty;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_main_page_))						main_page = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_bldr_version_))					bldr_version = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_siteinfo_misc_))					Siteinfo_misc_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_siteinfo_mainpage_))				siteinfo_mainpage = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_css_version_))					css_version = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_modified_latest_))				modified_latest = m.ReadDate("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_main_page_ = "main_page_"
	, Invk_bldr_version = "bldr_version", Invk_bldr_version_ = "bldr_version_", Invk_siteinfo_misc_ = "siteinfo_misc_", Invk_siteinfo_mainpage_ = "siteinfo_mainpage_"
	, Invk_css_version_ = "css_version_"
	, Invk_modified_latest_ = "modified_latest_"
	;
	public static final byte[] MediaWiki_version = ByteAry_.new_ascii_("1.21wmf11");	// approximate level of compatibility
}
