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
public class Xow_ns_ {
	public static final int	// EX.WP: http://www.mediawiki.org/wiki/Help:Namespaces
		  Id_media				=  -2
		, Id_special			=  -1
		, Id_main				=   0				, Id_talk				=   1
		, Id_user				=   2				, Id_user_talk			=   3
		, Id_project			=   4				, Id_project_talk		=   5
		, Id_file				=   6				, Id_file_talk			=   7
		, Id_mediaWiki			=   8				, Id_mediaWiki_talk		=   9
		, Id_template			=  10				, Id_template_talk		=  11
		, Id_help				=  12				, Id_help_talk			=  13
		, Id_category			=  14				, Id_category_talk		=  15
		, Id_portal				= 100				, Id_portal_talk		= 101
	    , Id_null				= Int_.MinValue
		;
	public static final byte Case_match_all = 0, Case_match_1st = 1;
	public static byte Case_match_parse_(String s) {
		if		(String_.Eq(s, "first-letter"))		return Case_match_1st;
		else if	(String_.Eq(s, "case-sensitive"))	return Case_match_all;
		else										throw Err_mgr._.unhandled_(s);
	}
	public static int Canonical_id(byte[] canonical_name) {
		if (canonical_hash == null) {
			Xow_ns[] ary = Canonical;
			int len = ary.length;
			canonical_hash = OrderedHash_.new_bry_();
			for (int i = 0; i < len; i++) {
				Xow_ns ns = ary[i];
				canonical_hash.Add(ns.Name_bry(), IntVal.new_(ns.Id()));
			}
		}
		Object rv_obj = canonical_hash.Fetch(canonical_name);
		return rv_obj == null ? Xow_ns_.Id_null : ((IntVal)rv_obj).Val();
	}	private static OrderedHash canonical_hash;
	public static int Canonical_idx_media = 0;
	public static final Xow_ns[] Canonical = new Xow_ns[]	// REF.MW: Namespace.php|$wgCanonicalNamespaceNames
	{	Canonical_new_(Id_media,				"Media")
	,	Canonical_new_(Id_special,				"Special")
	,	Canonical_new_(Id_talk,					"Talk")
	,	Canonical_new_(Id_user,					"User")
	,	Canonical_new_(Id_user_talk,			"User talk")
	,	Canonical_new_(Id_project,				"Project")
	,	Canonical_new_(Id_project_talk,			"Project talk")
	,	Canonical_new_(Id_file,					"File")
	,	Canonical_new_(Id_file_talk,			"File talk")
	,	Canonical_new_(Id_mediaWiki,			"MediaWiki")
	,	Canonical_new_(Id_mediaWiki,			"MediaWiki talk")
	,	Canonical_new_(Id_template,				"Template")
	,	Canonical_new_(Id_template_talk,		"Template talk")
	,	Canonical_new_(Id_help,					"Help")
	,	Canonical_new_(Id_help_talk,			"Help talk")
	,	Canonical_new_(Id_category,				"Category")
	,	Canonical_new_(Id_category_talk,		"Category talk")
	};
	public static final String Ns_name_wikipedia = "Wikipedia";
	public static final String Ns_name_main = "Main";
	public static final byte[] Ns_prefix_main = ByteAry_.new_ascii_("Main:");
	private static Xow_ns Canonical_new_(int id, String name) {return new Xow_ns(id, Case_match_1st, ByteAry_.new_ascii_(name), false);}	// NOTE: for id/name reference only; case_match and alias does not matter;
}
