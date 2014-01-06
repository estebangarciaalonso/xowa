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
public class Xop_xnde_tag {
	public Xop_xnde_tag(int id, String name_str) {	// NOTE: should only be used by Xop_xnde_tag_
		this.id = id;
		this.name_bry = ByteAry_.new_ascii_(name_str);
		this.name_str = name_str;
		nameLen = name_bry.length;
		xtnEndTag = ByteAry_.Add(Xop_xnde_tag_.XtnEndTag_bgn, name_bry);	// always force endtag; needed for <noinclude>
		xtnEndTag_tmp = new byte[xtnEndTag.length]; Array_.Copy(xtnEndTag, xtnEndTag_tmp);
	}
	public int Id() {return id;} public Xop_xnde_tag Id_(int v) {id = v; return this;} private int id;
	public byte[] Name_bry() {return name_bry;} private byte[] name_bry;
	public String Name_str() {return name_str;} private String name_str;
	public int NameLen() {return nameLen;} private int nameLen;
	public boolean Xtn() {return xtn;} public Xop_xnde_tag Xtn_() {xtn = true; return this;} private boolean xtn;
	public boolean XtnTmpl() {return xtnTmpl;} public Xop_xnde_tag XtnTmpl_() {xtnTmpl = true; return this;} private boolean xtnTmpl;
	public byte[] XtnEndTag() {return xtnEndTag;} private byte[] xtnEndTag;
	public byte[] XtnEndTag_tmp() {return xtnEndTag_tmp;} private byte[] xtnEndTag_tmp;
	public int BgnNdeMode() {return bgnNdeMode;} private int bgnNdeMode = Xop_xnde_tag_.BgnNdeMode_normal;
	public Xop_xnde_tag BgnNdeMode_inline_() {bgnNdeMode = Xop_xnde_tag_.BgnNdeMode_inline; return this;}
	public int EndNdeMode() {return endNdeMode;} private int endNdeMode = Xop_xnde_tag_.EndNdeMode_normal;
	public Xop_xnde_tag EndNdeMode_inline_() {endNdeMode = Xop_xnde_tag_.EndNdeMode_inline; return this;}
	public Xop_xnde_tag EndNdeMode_escape_() {endNdeMode = Xop_xnde_tag_.EndNdeMode_escape; return this;}
	public boolean SingleOnly() {return singleOnly;} public Xop_xnde_tag SingleOnly_() {singleOnly = true; return this;} private boolean singleOnly;
	public boolean TblSub() {return tblSub;} public Xop_xnde_tag TblSub_() {tblSub = true; return this;} private boolean tblSub;
	public boolean Nest() {return nest;} public Xop_xnde_tag Nest_() {nest = true; return this;} private boolean nest;
	public boolean Restricted() {return restricted;} public Xop_xnde_tag Restricted_() {restricted = true; return this;} private boolean restricted;
	public boolean NoInline() {return noInline;} public Xop_xnde_tag NoInline_() {noInline = true; return this;} private boolean noInline;
	public boolean Inline_by_backslash() {return inline_by_backslash;} public Xop_xnde_tag Inline_by_backslash_() {inline_by_backslash = true; return this;} private boolean inline_by_backslash;
	public boolean Section() {return section;} public Xop_xnde_tag Section_() {section = true; return this;} private boolean section;
	public boolean Repeat_ends() {return repeat_ends;} public Xop_xnde_tag Repeat_ends_() {repeat_ends = true; return this;} private boolean repeat_ends;
	public boolean Repeat_mids() {return repeat_mids;} public Xop_xnde_tag Repeat_mids_() {repeat_mids = true; return this;} private boolean repeat_mids;
	public boolean Empty_ignored() {return empty_ignored;} public Xop_xnde_tag Empty_ignored_() {empty_ignored = true; return this;} private boolean empty_ignored;
	public boolean Raw() {return raw;} public Xop_xnde_tag Raw_() {raw = true; return this;} private boolean raw;
	public static final byte Block_noop = 0, Block_bgn = 1, Block_end = 2;
	public byte Block_open() {return pre_open;} private byte pre_open = Block_noop;
	public byte Block_close() {return pre_close;} private byte pre_close = Block_noop;
	public Xop_xnde_tag Block_open_bgn_() {pre_open = Block_bgn; return this;} public Xop_xnde_tag Block_open_end_() {pre_open = Block_end; return this;}
	public Xop_xnde_tag Block_close_bgn_() {pre_close = Block_bgn; return this;} public Xop_xnde_tag Block_close_end_() {pre_close = Block_end; return this;}
}
