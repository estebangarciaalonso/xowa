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
package gplx.xowa.files; import gplx.*; import gplx.xowa.*;
import gplx.xowa.files.fsdb.*;
public class Xow_file_mgr implements GfoInvkAble {
	public Xow_file_mgr(Xow_wiki wiki) {
		this.wiki = wiki;
		repo_mgr = new Xowf_repo_mgr(wiki);
		meta_mgr = new Xof_meta_mgr(wiki);
	}	private Xow_wiki wiki;
	public Xowf_repo_mgr Repo_mgr() {return repo_mgr;} private Xowf_repo_mgr repo_mgr;
	public Xof_meta_mgr  Meta_mgr() {return meta_mgr;} private Xof_meta_mgr meta_mgr;
	public Xof_cfg_download Cfg_download() {return cfg_download;} private Xof_cfg_download cfg_download = new Xof_cfg_download();
	public Xof_fsdb_mgr Fsdb_mgr() {return fsdb_mgr;} private Xof_fsdb_mgr fsdb_mgr = new Xof_fsdb_mgr();
	public boolean Find_meta(Xof_xfer_itm xfer_itm) {
		xfer_itm.Trg_repo_idx_(Xof_meta_itm.Repo_unknown);
		byte[] xfer_itm_ttl = xfer_itm.Ttl();
		xfer_itm.Atrs_by_ttl(xfer_itm_ttl, ByteAry_.Empty);
		Xof_meta_itm meta_itm = meta_mgr.Get_itm_or_new(xfer_itm_ttl, xfer_itm.Md5());
		xfer_itm.Atrs_by_meta_only(meta_itm);
		if (meta_itm.State_new()) {														// meta_itm is brand new
			xfer_itm.Atrs_by_meta(meta_itm, repo_mgr.Repos_get_at(0).Trg(), wiki.Html_mgr().Img_thumb_width());	// default to 1st repo to prevent null_ref in xfer_mgr; questionable, but all wikis must have at least 1 repo
			xfer_itm.Atrs_calc_for_html();
			return false;
		}
		else {																			// meta_itm exists
			Xof_repo_itm cur_repo = null;
			cur_repo = meta_itm.Repo_itm(wiki);
			xfer_itm.Atrs_by_meta(meta_itm, cur_repo, wiki.Html_mgr().Img_thumb_width());
			return xfer_itm.Atrs_calc_for_html();
		}
	}

	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_repos))					return repo_mgr;
		else if	(ctx.Match(k, Invk_metas))					return meta_mgr;
		else if	(ctx.Match(k, Invk_cfg_download))			return cfg_download;
		else	return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_repos = "repos", Invk_metas = "metas", Invk_cfg_download = "cfg_download";
}
