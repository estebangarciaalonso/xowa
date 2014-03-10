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
package gplx.xowa.xtns.refs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.html.*;
public class Ref_html_wtr {
	Xoo_ref opt = Xoo_ref.new_();
	public void Xnde_ref(Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde) {
		Ref_nde itm = (Ref_nde)xnde.Xnde_xtn();
		if (itm == null) return;	// FUTURE: See Battle of Midway
		if (itm.Follow_y()) return;	// NOTE: "follow" is always appended to preceding ref; will never generate its own ^ a  
		opt.Itm_html().Bld_bfr_many(bfr
			, Itm_id(itm, true)
			, Grp_id(itm)
			, itm.Group() == ByteAry_.Empty	// grp_key
				? itm.Idx_major() + 1
				: (Object)grp_key_fmtr.Atrs_(opt.Itm_grp_text(), itm.Group(), itm.Idx_major() + 1)
			);
	}	private ByteAryFmtrArg_fmtr_objs grp_key_fmtr = ByteAryFmtrArg_.fmtr_null_();
	ByteAryFmtrArg Itm_id(Ref_nde itm, boolean caller_is_ref) {
		if (itm.Name() == ByteAry_.Empty)
			return itm_id_fmtr.Atrs_(opt.Itm_id_uid(), itm.Uid());
		else {
			if (caller_is_ref)
				return itm_id_fmtr.Atrs_(opt.Itm_id_key_one(), itm.Name(), itm.Idx_major(), itm.Idx_minor());
			else
				return itm_id_fmtr.Atrs_(opt.Itm_id_key_many(), itm.Name(), itm.Idx_major());
		}
	}	private ByteAryFmtrArg_fmtr_objs itm_id_fmtr = ByteAryFmtrArg_.fmtr_null_();
	ByteAryFmtrArg Grp_id(Ref_nde itm) {
		return itm.Name() == ByteAry_.Empty	// name is blank >>> uid 
			? grp_id_fmtr.Atrs_(opt.Grp_id_uid(), itm.Uid())
			: grp_id_fmtr.Atrs_(opt.Grp_id_key(), itm.Name(), itm.Idx_major());
	}	private ByteAryFmtrArg_fmtr_objs grp_id_fmtr = ByteAryFmtrArg_.fmtr_null_();
	int List_len(Ref_nde itm) {
		int len = itm.Related_len();
		int rv = len;
		for (int i = 0; i < len; i++) {
			Ref_nde list_itm = itm.Related_get(i);
			if (list_itm.Nested()) --rv;
		}
		return rv;
	}
	public void Xnde_references(Xoh_html_wtr wtr, Xop_ctx ctx, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {
		References_nde references = (References_nde)xnde.Xnde_xtn();
		Ref_itm_lst lst = ctx.Page().Ref_mgr().Lst_get(references.Group(), references.List_idx());	// get group; EX: <references group="note"/>
		if (lst == null) return;	// NOTE: possible to have a grouped references without references; EX: Infobox planet; <references group=note> in sidebar, but no refs 
		if (lst.Itms_len() == 0) return;
		bfr.Add(opt.Grp_bgn());
		int itms_len = lst.Itms_len();
		for (int j = 0; j < itms_len; j++) {	// iterate over itms in grp
			Ref_nde head_itm = lst.Itms_get_at(j);
//				if (head_itm.Exists_in_lnki_title()) continue;	// EX: if ref is in lnki_title only, ignore; EX:w:UK; DATE:2014-03-05
			ByteAryBfr tmp = ByteAryBfr.new_();
			int list_len = List_len(head_itm);
			grp_list_fmtr.Init(opt, head_itm);
			Ref_nde text_itm = grp_list_fmtr.IdentifyTxt();	// find the item that has the text (there should only be 0 or 1)
			if (text_itm.Body() != null)
				wtr.Write_tkn(ctx, opts, tmp, text_itm.Body().Root_src(), depth + 1, null, Xoh_html_wtr.Sub_idx_null, text_itm.Body());

			// add follows
			int related_len = head_itm.Related_len();
			for (int k = 0; k < related_len; k++) {
				Ref_nde related_itm = head_itm.Related_get(k);
				if (related_itm.Follow_y()) {	// NOTE: both follow and related are in the related list; only add follow
					tmp.Add_byte_space();	// always add space; REF.MW:Cite_body.php;$this->mRefs[$group][$follow]['text'] = $this->mRefs[$group][$follow]['text'] . ' ' . $str;
					wtr.Write_tkn(ctx, opts, tmp, related_itm.Body().Root_src(), depth + 1, null, Xoh_html_wtr.Sub_idx_null, related_itm.Body());
				}
			}

			if (list_len == 0) {		// ref has 0 list_itms or 1 list_itm but nested; EX: "123 ^ text"
				opt.Grp_html_one().Bld_bfr_many(bfr
					, Grp_id(head_itm)	// NOTE: use head_itm for back ref to work (^ must link to same id)
					, Itm_id(head_itm, true)
					, tmp
					);
			}
			else {							// ref has 1+ itms; EX: "123 ^ a b c text"
				opt.Grp_html_many().Bld_bfr_many(bfr
					, Itm_id(text_itm, false)
					, grp_list_fmtr
					, tmp
					);
			}
		}
		bfr.Add(opt.Grp_end());
	}
	private static Xoh_ref_list_fmtr grp_list_fmtr = new Xoh_ref_list_fmtr();
}
class Xoh_ref_list_fmtr implements ByteAryFmtrArg {
	public Ref_nde IdentifyTxt() {
		if (HasTxt(itm)) return itm;
		int itm_related_len = itm.Related_len();
		for (int i = 0; i < itm_related_len; i++) {
			Ref_nde rel = itm.Related_get(i);
			if (HasTxt(rel)) return rel;
		}
		return itm; // no itm has text; TODO:WARN
	}
	boolean HasTxt(Ref_nde v) {return v.Body() != null && v.Body().Root_src().length > 0;}
	public void Init(Xoo_ref opt, Ref_nde itm) {this.opt = opt; this.itm = itm;} Ref_nde itm; Xoo_ref opt;
	public void XferAry(ByteAryBfr trg, int idx) {
		int related_len = itm.Related_len();
		ByteAryFmtr itm_fmtr = opt.Grp_html_list();
		Fmt(itm_fmtr, trg, itm);
		for (int i = 0; i < related_len; i++) {
			Ref_nde link_itm = itm.Related_get(i);
			if (link_itm.Nested()) continue;
			Fmt(itm_fmtr, trg, link_itm);
		}
	}
	private void Fmt(ByteAryFmtr itm_fmtr, ByteAryBfr trg, Ref_nde itm) {
		if (itm.Idx_minor() < 0) return;	// HACK: <ref follow created a negative index; ignore these references for now; de.wikisource.org/wiki/Seite:Die Trunksucht.pdf/63; DATE:2013-06-22
		itm_fmtr.Bld_bfr_many(trg
			, fmtr.Atrs_(opt.Itm_id_key_one(), itm.Name(), itm.Idx_major(), itm.Idx_minor())
			, opt.Backlabels()[itm.Idx_minor()]
			);
	}	private ByteAryFmtrArg_fmtr_objs fmtr = ByteAryFmtrArg_.fmtr_null_();
}
class Xoo_ref {
	public ByteAryFmtr Itm_html() 			{return itm_html;} 			ByteAryFmtr itm_html; 			public Xoo_ref Itm_html_(String v) {itm_html 				= ByteAryFmtr.new_(v, "itm_id", "grp_id", "grp_key"); return this;}
	public ByteAryFmtr Itm_id_uid() 		{return itm_id_uid;} 		ByteAryFmtr itm_id_uid; 		public Xoo_ref Itm_id_uid_(String v) {itm_id_uid 			= ByteAryFmtr.new_(v, "uid"); return this;}
	public ByteAryFmtr Itm_id_key_one() 	{return itm_id_key_one;} 	ByteAryFmtr itm_id_key_one; 	public Xoo_ref Itm_id_key_one_(String v) {itm_id_key_one 	= ByteAryFmtr.new_(v, "itm_key", "uid", "minor"); return this;}
	public ByteAryFmtr Itm_id_key_many() 	{return itm_id_key_many;} 	ByteAryFmtr itm_id_key_many; 	public Xoo_ref Itm_id_key_many_(String v) {itm_id_key_many	= ByteAryFmtr.new_(v, "itm_key", "uid"); return this;}
	public ByteAryFmtr Itm_grp_text() 		{return itm_grp_text;} 		ByteAryFmtr itm_grp_text; 		public Xoo_ref Itm_grp_text_(String v) {itm_grp_text 		= ByteAryFmtr.new_(v, "grp_key", "major"); return this;}
	public ByteAryFmtr Grp_html_one() 		{return grp_html_one;} 		ByteAryFmtr grp_html_one; 		public Xoo_ref Grp_html_one_(String v) {grp_html_one 		= ByteAryFmtr.new_(v, "grp_id", "itm_id", "text"); return this;}
	public ByteAryFmtr Grp_html_many() 		{return grp_html_many;}		ByteAryFmtr grp_html_many; 		public Xoo_ref Grp_html_many_(String v) {grp_html_many 		= ByteAryFmtr.new_(v, "grp_id", "related_ids", "text"); return this;}
	public ByteAryFmtr Grp_html_list()		{return grp_html_list;}		ByteAryFmtr grp_html_list;		public Xoo_ref Grp_html_list_(String v) {grp_html_list		= ByteAryFmtr.new_(v, "itm_id", "backlabel"); return this;}
	public ByteAryFmtr Grp_id_uid() 		{return grp_id_uid;} 		ByteAryFmtr grp_id_uid; 		public Xoo_ref Grp_id_uid_(String v) {grp_id_uid 			= ByteAryFmtr.new_(v, "uid"); return this;}
	public ByteAryFmtr Grp_id_key() 		{return grp_id_key;} 		ByteAryFmtr grp_id_key; 		public Xoo_ref Grp_id_key_(String v) {grp_id_key 			= ByteAryFmtr.new_(v, "itm_key", "major"); return this;}
	public byte[][] Backlabels() {return backlabels;} private byte[][] backlabels;
	public byte[] Grp_bgn() {return grp_bgn;} private byte[] grp_bgn;
	public byte[] Grp_end() {return grp_end;} private byte[] grp_end;
	public static Xoo_ref new_() {
		Xoo_ref rv = new Xoo_ref();
		rv.Itm_html_		("<sup id=\"cite_ref-~{itm_id}\" class=\"reference\"><a href=\"#cite_note-~{grp_id}\">[~{grp_key}]</a></sup>");
		rv.Itm_id_uid_		("~{uid}");
		rv.Itm_id_key_one_	("~{itm_key}_~{uid}-~{minor}");
		rv.Itm_id_key_many_	("~{itm_key}-~{uid}");
		rv.Itm_grp_text_	("~{grp_key} ~{major}");
		rv.Grp_html_one_	("<li id=\"cite_note-~{grp_id}\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-~{itm_id}\">^</a></span> <span class=\"reference-text\">~{text}</span></li>\n");
		rv.Grp_html_many_	("<li id=\"cite_note-~{grp_id}\"><span class=\"mw-cite-backlink\">^~{related_ids}</span> <span class=\"reference-text\">~{text}</span></li>\n");
		rv.Grp_html_list_	(" <sup><a href=\"#cite_ref-~{itm_id}\">~{backlabel}</a></sup>");
		rv.Grp_id_uid_		("~{uid}");
		rv.Grp_id_key_		("~{itm_key}-~{major}");
		rv.grp_bgn = ByteAry_.new_ascii_("<ol class=\"references\">\n");
		rv.grp_end = ByteAry_.new_ascii_("</ol>\n");

		rv.backlabels = Ref_backlabels_xtoAry(String_.Ary	// REF.MW: Cite.i18n.php|cite_references_link_many_format
				(  "a",  "b",  "c",  "d",  "e",  "f",  "g",  "h",  "i",  "j",  "k",  "l",  "m",  "n",  "o",  "p",  "q",  "r",  "s",  "t",  "u",  "v",  "w",  "x",  "y",  "z"
				, "aa", "ab", "ac", "ad", "ae", "af", "ag", "ah", "ai", "aj", "ak", "al", "am", "an", "ao", "ap", "aq", "ar", "as", "at", "au", "av", "aw", "ax", "ay", "az"
				, "ba", "bb", "bc", "bd", "be", "bf", "bg", "bh", "bi", "bj", "bk", "bl", "bm", "bn", "bo", "bp", "bq", "br", "bs", "bt", "bu", "bv", "bw", "bx", "by", "bz"
				, "ca", "cb", "cc", "cd", "ce", "cf", "cg", "ch", "ci", "cj", "ck", "cl", "cm", "cn", "co", "cp", "cq", "cr", "cs", "ct", "cu", "cv", "cw", "cx", "cy", "cz"
				, "da", "db", "dc", "dd", "de", "df", "dg", "dh", "di", "dj", "dk", "dl", "dm", "dn", "do", "dp", "dq", "dr", "ds", "dt", "du", "dv", "dw", "dx", "dy", "dz"
				, "ea", "eb", "ec", "ed", "ee", "ef", "eg", "eh", "ei", "ej", "ek", "el", "em", "en", "eo", "ep", "eq", "er", "es", "et", "eu", "ev", "ew", "ex", "ey", "ez"
				, "fa", "fb", "fc", "fd", "fe", "ff", "fg", "fh", "fi", "fj", "fk", "fl", "fm", "fn", "fo", "fp", "fq", "fr", "fs", "ft", "fu", "fv", "fw", "fx", "fy", "fz"
				, "ga", "gb", "gc", "gd", "ge", "gf", "gg", "gh", "gi", "gj", "gk", "gl", "gm", "gn", "go", "gp", "gq", "gr", "gs", "gt", "gu", "gv", "gw", "gx", "gy", "gz"
				, "ha", "hb", "hc", "hd", "he", "hf", "hg", "hh", "hi", "hj", "hk", "hl", "hm", "hn", "ho", "hp", "hq", "hr", "hs", "ht", "hu", "hv", "hw", "hx", "hy", "hz"
				, "ia", "ib", "ic", "id", "ie", "if", "ig", "ih", "ii", "ij", "ik", "il", "im", "in", "io", "ip", "iq", "ir", "is", "it", "iu", "iv", "iw", "ix", "iy", "iz"
				, "ja", "jb", "jc", "jd", "je", "jf", "jg", "jh", "ji", "jj", "jk", "jl", "jm", "jn", "jo", "jp", "jq", "jr", "js", "jt", "ju", "jv", "jw", "jx", "jy", "jz"
				, "ka", "kb", "kc", "kd", "ke", "kf", "kg", "kh", "ki", "kj", "kk", "kl", "km", "kn", "ko", "kp", "kq", "kr", "ks", "kt", "ku", "kv", "kw", "kx", "ky", "kz"
				, "la", "lb", "lc", "ld", "le", "lf", "lg", "lh", "li", "lj", "lk", "ll", "lm", "ln", "lo", "lp", "lq", "lr", "ls", "lt", "lu", "lv", "lw", "lx", "ly", "lz"
				, "ma", "mb", "mc", "md", "me", "mf", "mg", "mh", "mi", "mj", "mk", "ml", "mm", "mn", "mo", "mp", "mq", "mr", "ms", "mt", "mu", "mv", "mw", "mx", "my", "mz"
				, "na", "nb", "nc", "nd", "ne", "nf", "ng", "nh", "ni", "nj", "nk", "nl", "nm", "nn", "no", "np", "nq", "nr", "ns", "nt", "nu", "nv", "nw", "nx", "ny", "nz"
				, "oa", "ob", "oc", "od", "oe", "of", "og", "oh", "oi", "oj", "ok", "ol", "om", "on", "oo", "op", "oq", "or", "os", "ot", "ou", "ov", "ow", "ox", "oy", "oz"
				, "pa", "pb", "pc", "pd", "pe", "pf", "pg", "ph", "pi", "pj", "pk", "pl", "pm", "pn", "po", "pp", "pq", "pr", "ps", "pt", "pu", "pv", "pw", "px", "py", "pz"
				, "qa", "qb", "qc", "qd", "qe", "qf", "qg", "qh", "qi", "qj", "qk", "ql", "qm", "qn", "qo", "qp", "qq", "qr", "qs", "qt", "qu", "qv", "qw", "qx", "qy", "qz"
				, "ra", "rb", "rc", "rd", "re", "rf", "rg", "rh", "ri", "rj", "rk", "rl", "rm", "rn", "ro", "rp", "rq", "rr", "rs", "rt", "ru", "rv", "rw", "rx", "ry", "rz"
				, "sa", "sb", "sc", "sd", "se", "sf", "sg", "sh", "si", "sj", "sk", "sl", "sm", "sn", "so", "sp", "sq", "sr", "ss", "st", "su", "sv", "sw", "sx", "sy", "sz"
				, "ta", "tb", "tc", "td", "te", "tf", "tg", "th", "ti", "tj", "tk", "tl", "tm", "tn", "to", "tp", "tq", "tr", "ts", "tt", "tu", "tv", "tw", "tx", "ty", "tz"
				, "ua", "ub", "uc", "ud", "ue", "uf", "ug", "uh", "ui", "uj", "uk", "ul", "um", "un", "uo", "up", "uq", "ur", "us", "ut", "uu", "uv", "uw", "ux", "uy", "uz"
				, "va", "vb", "vc", "vd", "ve", "vf", "vg", "vh", "vi", "vj", "vk", "vl", "vm", "vn", "vo", "vp", "vq", "vr", "vs", "vt", "vu", "vv", "vw", "vx", "vy", "vz"
				, "wa", "wb", "wc", "wd", "we", "wf", "wg", "wh", "wi", "wj", "wk", "wl", "wm", "wn", "wo", "wp", "wq", "wr", "ws", "wt", "wu", "wv", "ww", "wx", "wy", "wz"
				, "xa", "xb", "xc", "xd", "xe", "xf", "xg", "xh", "xi", "xj", "xk", "xl", "xm", "xn", "xo", "xp", "xq", "xr", "xs", "xt", "xu", "xv", "xw", "xx", "xy", "xz"
				, "ya", "yb", "yc", "yd", "ye", "yf", "yg", "yh", "yi", "yj", "yk", "yl", "ym", "yn", "yo", "yp", "yq", "yr", "ys", "yt", "yu", "yv", "yw", "yx", "yy", "yz"
				, "za", "zb", "zc", "zd", "ze", "zf", "zg", "zh", "zi", "zj", "zk", "zl", "zm", "zn", "zo", "zp", "zq", "zr", "zs", "zt", "zu", "zv", "zw", "zx", "zy", "zz"
				));
		return rv;
	}
	private static byte[][] Ref_backlabels_xtoAry(String[] ary) {
		int ary_len = ary.length;
		byte[][] rv = new byte[ary_len][];
		for (int i = 0; i < ary_len; i++)
			rv[i] = ByteAry_.new_ascii_(ary[i]);
		return rv;
	}
}
