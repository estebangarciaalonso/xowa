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
public class Xow_bldr_props implements GfoInvkAble {
	public Xow_bldr_props(Xow_wiki wiki) {this.wiki = wiki;} private Xow_wiki wiki; boolean src_fil_is_bz2 = true;
	public byte Category_version() {return category_version;} public Xow_bldr_props Category_version_(byte v) {category_version = v; return this;} private byte category_version = gplx.xowa.ctgs.Xoa_ctg_mgr.Version_1;
	public Io_url Src_dir() {
		if		(src_fil_xml == null && src_fil_bz2 == null)	return wiki.Fsys_mgr().Root_dir();
		else if (src_fil_xml != null)							return src_fil_xml.OwnerDir();
		else if (src_fil_bz2 != null)							return src_fil_bz2.OwnerDir();
		else													throw Err_.new_("unknown src dir");
	}
	public gplx.ios.Io_stream_rdr Src_rdr() {
		if (src_fil_xml == null && src_fil_bz2 == null) {	// will usually be null; non-null when user specifies src through build file
			Io_url url = Xow_fsys_mgr.Find_file_or_fail(wiki.Fsys_mgr().Root_dir(), "*", ".xml", ".bz2");
			if (String_.Eq(url.Ext(), ".xml"))	Src_fil_xml_(url);
			else								Src_fil_bz2_(url);
		}
		return src_fil_is_bz2 ? gplx.ios.Io_stream_rdr_.bzip2_(src_fil_bz2) : gplx.ios.Io_stream_rdr_.file_(src_fil_xml);
	}
	public Io_url Src_fil_xml() {return src_fil_xml;}
	public Xow_bldr_props Src_fil_(boolean xml, Io_url v) {return xml ? Src_fil_xml_(v) : Src_fil_bz2_(v);}
	public Xow_bldr_props Src_fil_xml_(Io_url v) {src_fil_xml = v; src_fil_is_bz2 = Bool_.N; return this;} Io_url src_fil_xml;
	public Xow_bldr_props Src_fil_bz2_(Io_url v) {src_fil_bz2 = v; src_fil_is_bz2 = Bool_.Y; return this;} Io_url src_fil_bz2;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_src_fil_xml_))				Src_fil_xml_(m.ReadIoUrl("v"));
		else if	(ctx.Match(k, Invk_src_fil_bz2_))				Src_fil_bz2_(m.ReadIoUrl("v"));
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_src_fil_xml_ = "src_fil_xml_", Invk_src_fil_bz2_ = "src_fil_bz2_";
	static final String GRP_KEY = "xowa.bldr_props";
}
