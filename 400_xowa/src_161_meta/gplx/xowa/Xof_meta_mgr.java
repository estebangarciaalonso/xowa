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
import gplx.core.flds.*;
public class Xof_meta_mgr implements GfoInvkAble {
	Object[] root = new Object[16]; OrderedHash dirty_fils = OrderedHash_.new_bry_();
	public Xof_meta_mgr(Xow_wiki wiki) {this.wiki = wiki; this.root_dir = wiki.App().Fsys_mgr().File_dir().GenSubDir_nest("#meta", wiki.Domain_str());}
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public Io_url Root_dir() {return root_dir;} Io_url root_dir;
	public int Depth() {return depth;} public Xof_meta_mgr Depth_(int v) {depth = v; return this;} private int depth = 3; // allows a full english wikipedia to have meta files of approximately 32 kb; otherwise would be 480 kb; most wikis will not get to this size, but worst case is wasting 16 kb in (16 * 16) files which is less than 4 mb
	public boolean Append_only() {return append_only;} public Xof_meta_mgr Append_only_(boolean v) {append_only = v; return this;} private boolean append_only;
	public Xof_meta_fil Get_fil_or_new(byte[] md5) {
		Xof_meta_fil rv = Get_fil_or_null(md5);
		if (rv == null) {
			if (!append_only)
				rv = Load(md5);
			if (rv == null) {
				rv = Bld_new(root, depth - 1, this, md5, 0);
				dirty_fils.Add(md5, rv);
			}
		}
		return rv;
	}
	public Xof_meta_itm Get_itm_or_new(byte[] ttl) {return Get_itm_or_new(ttl, gplx.xowa.files.Xof_xfer_itm_.Md5_(ttl));}
	public Xof_meta_itm Get_itm_or_new(byte[] ttl, byte[] md5) {
		Xof_meta_fil fil = this.Get_fil_or_new(md5);
		return fil.Get_or_new(ttl);
	}
	Xof_meta_fil Get_fil_or_null(byte[] md5) {return Get_fil_or_null_recur(root, depth - 1, md5, 0);}
	public void Dirty_(Xof_meta_fil fil) {
		byte[] md5 = fil.Md5();
		if (!dirty_fils.Has(md5)) dirty_fils.Add(md5, fil);
	}
	public void Clear() {root = new Object[16]; dirty_fils.Clear();}
	public void Save() {Save(false);}
	public void Save(boolean clear) {
		int dirty_len = dirty_fils.Count();
		Bry_bfr tmp_bfr = Bry_bfr.new_();
		wtr.Bfr_(tmp_bfr);
		for (int i = 0; i < dirty_len; i++) {
			Xof_meta_fil fil = (Xof_meta_fil)dirty_fils.FetchAt(i);
			Io_url fil_url = Xof_meta_fil.Bld_url(root_dir, fil.Md5(), depth);
			fil.Save(wtr);
			if (append_only)
				Io_mgr._.AppendFilBfr(fil_url, tmp_bfr);
			else
				Io_mgr._.SaveFilBfr(fil_url, tmp_bfr);
		}
		dirty_fils.Clear();
		if (clear) this.Clear();
	}	Gfo_fld_wtr wtr = Gfo_fld_wtr.xowa_();
	Xof_meta_fil Load(byte[] md5) {
		Io_url fil_url = Xof_meta_fil.Bld_url(root_dir, md5, depth);
		byte[] bry = Io_mgr._.LoadFilBry(fil_url); if (bry == Bry_.Empty) return null;
		rdr.Ini(bry, 0);
		Xof_meta_fil rv = Bld_new(root, depth - 1, this, md5, 0);	// NOTE: need to register file before loading it; defect wherein 2 files with same hash prefix would skip one b/c Loaded file was not registered; EX.WS: en.wikiquote.org/The Hitchhiker's Guide to the Galaxy; NMMP_dolphin_with_locator.jpeg, da6f95736ed249f371f30bf5f1205fbd; Hoags_object.jpg, daed4a54e48e4266bd2f2763b7c4018c
		rv.Load(rdr, parser);
		return rv;
	}	Gfo_fld_rdr rdr = Gfo_fld_rdr.xowa_(); Xof_meta_thumb_parser parser = new Xof_meta_thumb_parser();
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_regy_depth_))		depth = m.ReadInt("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_regy_depth_ = "depth_";
	static final String GRP_KEY = "xowa.file_regy.";
	private static Xof_meta_fil Get_fil_or_null_recur(Object[] ary, int depth_max, byte[] md5, int md5_idx) {
		int ary_idx = Int_.Xto_int_hex(md5[md5_idx]);
		if (ary_idx < 0 || ary_idx > 15) throw Err_mgr._.fmt_auto_(GRP_KEY, "md5_not_valid", String_.new_utf8_(md5), md5_idx);
		Object o = ary[ary_idx];
		if (o == null) return null;
		if (md5_idx == depth_max) {	// leaf; return fil
			try {return (Xof_meta_fil)o;} catch (Exception exc) {throw Err_mgr._.cast_(exc, Xof_meta_fil.class, o);}
		}
		Object[] nxt = null; try {nxt = (Object[])o;} catch(Exception exc) {throw Err_mgr._.cast_(exc, Object[].class, o);}
		return Get_fil_or_null_recur(nxt, depth_max, md5, md5_idx + 1);
	}
	private static Xof_meta_fil Bld_new(Object[] ary, int depth_max, Xof_meta_mgr regy_mgr, byte[] md5, int md5_idx) {
		int ary_idx = Int_.Xto_int_hex(md5[md5_idx]);
		if (ary_idx < 0 || ary_idx > 15) throw Err_mgr._.fmt_auto_(GRP_KEY, "md5_not_valid", String_.new_utf8_(md5), md5_idx);
		Object o = ary[ary_idx];
		if (md5_idx == depth_max) {	// leaf; create itm
			Xof_meta_fil rv = null;
			if (o == null) {
				rv = new Xof_meta_fil(regy_mgr, md5);
				ary[ary_idx] = rv; 
			}
			else
				rv = (Xof_meta_fil)o;
			return rv;
		}
		else {
			Object[] nxt = null;
			if (o == null) {
				nxt = new Object[16];
				ary[ary_idx] = nxt;
			}
			else
				nxt = (Object[])o;
			return Bld_new(nxt, depth_max, regy_mgr, md5, md5_idx + 1);
		}
	}
}
