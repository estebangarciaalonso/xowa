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
public class Xow_ns_mgr implements GfoInvkAble, gplx.lists.ComparerAble {
	OrderedHash id_hash = OrderedHash_.new_(); Hash_adp_bry name_hash = new Hash_adp_bry(false); Hash_adp_bry tmpl_hash = new Hash_adp_bry(false);
	public boolean Type_is_test;
	public int Ords_len() {return ords_len;} private int ords_len;
	public Xow_ns[] Ords() {return ords;} private Xow_ns[] ords = new Xow_ns[Xow_ns_mgr_.Ordinal_max];
	public int Count() {return ns_count;} private int ns_count = 0;
	public int Tmpl_trie_match(byte[] src, int bgn, int end)  {
		int colon_pos = ByteAry_.FindFwd(src, Byte_ascii.Colon, bgn, end); if (colon_pos == ByteAry_.NotFound) return ByteAry_.NotFound;
		Object o = tmpl_hash.Get_by_mid(src, bgn, colon_pos + 1); if (o == null) return ByteAry_.NotFound; // +1 to include colon_pos
		byte[] rv = (byte[])o;
		int tmpl_len = rv.length;
		return bgn + rv.length == bgn + tmpl_len ? tmpl_len : ByteAry_.NotFound;
	}
	public Xow_ns Trie_match_exact(byte[] src, int bgn, int end) {
		Object rv = name_hash.Get_by_mid(src, bgn, end);
		return rv == null ? null : ((Xow_ns_mgr_name_itm)rv).Ns();
	}
	public Xow_ns Get_by_bry_or_main(byte[] name_bry) {
		Xow_ns rv = this.Trie_match_exact(name_bry, 0, name_bry.length);
		return rv == null ? this.Ns_main() : rv;
	}
	public Object Trie_match_colon(byte[] src, int bgn, int end) {return Trie_match_colon(src, bgn, end, colon_pos_tmp.Val_neg1_());}	static IntRef colon_pos_tmp = IntRef.neg1_();
	public Object Trie_match_colon(byte[] src, int bgn, int end, IntRef colon_pos_ref) {
		int colon_pos = ByteAry_.FindFwd(src, Byte_ascii.Colon, bgn, end); if (colon_pos == ByteAry_.NotFound) return null;
		colon_pos_ref.Val_(colon_pos);
		Object rv = name_hash.Get_by_mid(src, bgn, colon_pos);
		if (rv != null) {
			Xow_ns_mgr_name_itm itm = (Xow_ns_mgr_name_itm)rv;
			int src_len = src.length;
			int match_end = bgn + itm.Name_len();
			if (match_end < src_len)
				return src[match_end] == Byte_ascii.Colon ? itm.Ns() : null;
			else if (match_end == src_len)	// handle titles that match ns name; EX: "Book" is in Main namespace, not in "Book" namespace
				return null;
		}
		return rv;
	}
	private void Hash_rebuild() {
		name_hash.Clear(); tmpl_hash.Clear();
		for (int i = 0; i < ords_len; i++) {
			Xow_ns ns = ords[i];
			if (ns == null) continue;	// TEST: allow gaps in ns numbers; see Talk_skip test and related
			if (ns.Id() == Xow_ns_.Id_project_talk) Project_talk_fix(ns);	// NOTE: handle $1 talk as per Language.php!fixVariableInNamespace; placement is important as it must go before key registration but after ord sort
			Hash_rebuild_add_itm(name_hash, ns, ns.Name_bry());
			if (ns.Id_tmpl()) tmpl_hash.Add(ns.Name_db_w_colon(), ns.Name_db_w_colon());
		}
		int aliases_len = aliases.Count();
		for (int i = 0; i < aliases_len; i++) {
			KeyVal kv = (KeyVal)aliases.FetchAt(i);
			int ns_id = ((IntVal)kv.Val()).Val();
			Xow_ns ns = Get_by_id(ns_id); if (ns == null) continue; // happens when alias exists, but not ns; EX: test has Image alias, but not File alias; should not happen "live" but don't want to fail
			byte[] alias_bry = ByteAry_.new_utf8_(kv.Key());
			Hash_rebuild_add_itm(name_hash, ns, alias_bry);
			if (ns.Id_tmpl()) {
				byte[] alias_name = ByteAry_.new_utf8_(kv.Key());
				alias_name = ByteAry_.Add(alias_name, Byte_ascii.Colon);
				tmpl_hash.AddReplace(alias_name, alias_name);
			}
		}
	}
	private void Project_talk_fix(Xow_ns ns) {
		byte[] ns_name = ns.Name_bry();
		if (ByteAry_.FindFwd(ns.Name_bry(), Project_talk_fmt_arg)== ByteAry_.NotFound) return; // no $1 found; exit
		Xow_ns project_ns = ords[ns.Ord_subj_id()];
		if (project_ns == null) return;	// should warn or throw error; for now just exit
		ns.Name_bry_(ByteAry_.Replace(ns_name, Project_talk_fmt_arg, project_ns.Name_bry()));
	}	static final byte[] Project_talk_fmt_arg = ByteAry_.new_ascii_("$1");
	private void Hash_rebuild_add_itm(Hash_adp_bry hash, Xow_ns ns, byte[] key) {
		Xow_ns_mgr_name_itm ns_itm = new Xow_ns_mgr_name_itm(key, ns);
		hash.AddReplace(key, ns_itm);
		if (ByteAry_.FindFwd(key, Byte_ascii.Underline) != ByteAry_.NotFound)	// ns has _; add another entry for space; EX: Help_talk -> Help talk
			hash.AddReplace(ByteAry_.Replace(key, Byte_ascii.Underline, Byte_ascii.Space), ns_itm);
	}
	public Xow_ns_mgr Add_defaults() { // NOTE: needs to happen after File ns is added; i.e.: cannot be put in Xow_ns_mgr() {} ctor
		Add_alias(Xow_ns_.Id_file		, "Image");			// REF.MW: Setup.php; add "Image", "Image talk" for backward compatibility; note that MW hardcodes Image ns as well
		Add_alias(Xow_ns_.Id_file_talk	, "Image_talk");
		Add_alias(Xow_ns_.Id_project	, "Project");		// always add "Project" ns (EX: Wikipedia is name for en.wikipedia.org; not sure if MW hardcodes, but it is in messages
		Add_alias(gplx.xowa.xtns.scribunto.Scrib_pf_invoke.Ns_id_module, "Module");		// always add "Module" ns; de.wikipedia.org has "Modul" defined in siteinfo.xml, but also uses Module
		return this;

	}
	public Xow_ns_mgr Add_alias(int ns_id, String name) {
		KeyVal kv = KeyVal_.new_(name, IntVal.new_(ns_id));
		aliases.AddReplace(name, kv);
		return this;
	}	private OrderedHash aliases = OrderedHash_.new_();
	public void Aliases_clear() {aliases.Clear();}
	
	public Xow_ns Ns_main()					{return ns_main;}		Xow_ns ns_main;
	public Xow_ns Ns_template()				{return ns_template;}	private Xow_ns ns_template;
	public Xow_ns Ns_file()					{return ns_file;}		Xow_ns ns_file;
	public Xow_ns Ns_category()				{return ns_category;}	private Xow_ns ns_category;
	public Xow_ns Ns_portal()				{return ns_portal;}	private Xow_ns ns_portal;
	public Xow_ns Ns_project()				{return ns_project;}	private Xow_ns ns_project;
	public Xow_ns Ns_mediawiki()			{return ns_mediawiki;}	private Xow_ns ns_mediawiki;
	public Xow_ns Get_by_id(int id)			{return (Xow_ns)id_hash.Fetch(ns_hash_lkp.Val_(id));} IntRef ns_hash_lkp = IntRef.zero_();
	public Xow_ns Get_by_ord(int ord)		{return ords[ord];}
	public int Id_count()					{return id_hash.Count();}
	public Xow_ns Id_get_at(int idx)		{return (Xow_ns)id_hash.FetchAt(idx);}
	public Xow_ns_mgr Add_new(int nsId, String name) {return Add_new(nsId, ByteAry_.new_utf8_(name), Xow_ns_.Case_match_1st, false);}	// for tst_ constructor
	public Xow_ns_mgr Add_new(int ns_id, byte[] name, byte caseMatchId, boolean alias) {
		ByteAry_.Replace_all_direct(name, Byte_ascii.Space, Byte_ascii.Underline);	// standardize on _; EX: User talk -> User_talk; DATE:2013-04-21
		Xow_ns ns = new Xow_ns(ns_id, caseMatchId, name, alias);
		switch (ns_id) {
			case Xow_ns_.Id_main:					ns_main = ns; break;
			case Xow_ns_.Id_template:				ns_template = ns; break;
			case Xow_ns_.Id_category:				ns_category = ns; break;
			case Xow_ns_.Id_portal:					ns_portal = ns; break;
			case Xow_ns_.Id_project:				ns_project = ns; break;
			case Xow_ns_.Id_mediaWiki:				ns_mediawiki = ns; break;
			case Xow_ns_.Id_file:					if (ns_file == null) ns_file = ns; break;	// NOTE: if needed, else Image will become the official ns_file
		}
		++ns_count;
		if (!id_hash.Has(ns_hash_lkp.Val_(ns_id)))		// NOTE: do not add if already exists; avoids alias
			id_hash.Add(IntRef.new_(ns.Id()), ns);
		name_hash.AddReplace(ns.Name_bry(), new Xow_ns_mgr_name_itm(ns.Name_bry(), ns));
		return this;
	}
	public int compare(Object lhsObj, Object rhsObj) {
		Xow_ns lhs = (Xow_ns)lhsObj;
		Xow_ns rhs = (Xow_ns)rhsObj;
		return Int_.Compare(lhs.Id(), rhs.Id());
	}
	public void Clear() {
		name_hash.Clear();
		id_hash.Clear();
		tmpl_hash.Clear();
		for (int i = 0; i < ords_len; i++)
			ords[i] = null;
		ords_len = 0;
		ns_count = 0;
		ns_file = null;
	}
	public void Init_done() {
		this.Add_defaults();
		this.Ords_sort();
	}
	public Xow_ns_mgr Ords_sort() {
		int ords_cur = 0;
		int ns_len = id_hash.Count();
		id_hash.SortBy(this);
		// assert that all items are grouped in pairs of subj, talk; note that subj is even and talk is odd
		int nxt_ns_id = Int_.MinValue;
		int prv_ns_id = Int_.MinValue;
		for (int i = 0; i < ns_len; i++) {
			Xow_ns ns = (Xow_ns)id_hash.FetchAt(i);
			int ns_id = ns.Id();
			if (ns_id < 0						// ignore negative ns (which don't have subj/talk pairing) 			
				|| ns.Alias()					// ignore alias
				) continue;			
			if (nxt_ns_id != Int_.MinValue) {	// nxt_ns_id is set
				if (nxt_ns_id != ns_id)		// prv was subj, but cur does not match expected talk_id; create talk for prv subj
					Ords_sort_add(nxt_ns_id);
				nxt_ns_id = Int_.MinValue;		// always reset value
			}
			if (ns_id % 2 == 0)				// subj
				nxt_ns_id = ns_id + 1;		// anticipate nxt_ns_id
			else {								// talk
				if (prv_ns_id != ns_id - 1)	// prv was not subj for cur; create subj for current talk
					Ords_sort_add(ns_id - 1);
			}
			prv_ns_id = ns_id;
		}
		if (nxt_ns_id != Int_.MinValue)		// handle trailing ns_id; EX: 0, 1, 2; need to make 3
			Ords_sort_add(nxt_ns_id);
		
		// sort again b/c new ns may have been added
		id_hash.SortBy(this);
		ns_len = id_hash.Count();
		// assign ords; assert that subj has even ordinal index
		ords_len = 0;
		for (int i = 0; i < ns_len; i++) {
			Xow_ns ns = (Xow_ns)id_hash.FetchAt(i);
			int ns_id = ns.Id();
			if (ns.Alias()) continue;			// ignore alias
			if (ns_id < 0) {}
			else {
				if (ns_id % 2 == 0) {			// subj
					if (ords_cur % 2 != 0) {	// current ordinal is not even; skip 
						++ords_len;
						++ords_cur;
					}
				}
			}
			ns.Ord_(ords_cur);
			ords[ords_cur++] = ns;
			++ords_len;
		}
		Hash_rebuild();
		return this;
	}
	private void Ords_sort_add(int ns_id) {
		this.Add_new(ns_id, ByteAry_.XbyInt(ns_id), Xow_ns_.Case_match_1st, false);	// NOTE: name and case_match are mostly useless defaults; note that in theory this proc should not be called (all siteInfos should be well-formed) but just in case, create items now so that Get_by_ord() does not fail
	}
	class Xow_ns_mgr_name_itm {
		public Xow_ns_mgr_name_itm(byte[] name, Xow_ns ns) {this.name = name; this.name_len = name.length; this.ns = ns;}
		public byte[] Name() {return name;} private byte[] name;
		public int Name_len() {return name_len;} private int name_len;
		public Xow_ns Ns() {return ns;} private Xow_ns ns;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_load))				Xow_cfg_wiki_core.Load_ns_(this, m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_clear))				this.Clear();
		else if	(ctx.Match(k, Invk_add_alias_bulk))		Exec_add_alias_bulk(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_get_by_id_or_new))	return this.Get_by_id_or_null(m.ReadInt("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_add_alias_bulk = "add_alias_bulk", Invk_get_by_id_or_new = "get_by_id_or_new";
	public Xow_ns Get_by_id_or_null(int ns_id) {
		Xow_ns rv = Get_by_id(ns_id);
		if (rv == null) return Null_ns;
		return rv;
	}	private static final Xow_ns Null_ns = new Xow_ns(Int_.MaxValue, Byte_.Zero, ByteAry_.Empty, false);
	public static final String Invk_load = "load", Invk_clear = "clear";
	private void Exec_add_alias_bulk(byte[] raw) {
		byte[][] lines = ByteAry_.Split(raw, Byte_ascii.NewLine);
		int lines_len = lines.length;
		for (int i = 0; i < lines_len; i++) {
			byte[] line = lines[i];
			if (line.length == 0) continue;
			byte[][] flds = ByteAry_.Split(line, Byte_ascii.Pipe);
			int cur_id = ByteAry_.XtoIntOr(flds[0], Int_.MinValue);
			this.Add_alias(cur_id, String_.new_utf8_(flds[1]));
		}
		Ords_sort();
	}
}
