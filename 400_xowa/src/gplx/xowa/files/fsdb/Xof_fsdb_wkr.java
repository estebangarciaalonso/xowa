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
package gplx.xowa.files.fsdb; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.xowa.files.qrys.*;
import gplx.xowa.files.bins.*;
import gplx.xowa.files.qrys.fs_roots.*;
public interface Xof_fsdb_wkr extends GfoInvkAble {
	Xof_fsdb_wkr Clone_new(Xow_wiki wiki);
	Xof_qry_wkr Qry_wkr();
	Xof_bin_wkr Bin_wkr();
}
class Xof_fsdb_wkr__root_dir implements Xof_fsdb_wkr {
	public Xof_fsdb_wkr Clone_new(Xow_wiki wiki) {
		Xof_fsdb_wkr__root_dir rv = new Xof_fsdb_wkr__root_dir();
		rv.Ctor_by_wiki(wiki);
		return rv;
	}
	private void Ctor_by_wiki(Xow_wiki wiki) {
		qry_wkr = new Fs_root_wkr(wiki);
	}
	public Xof_qry_wkr Qry_wkr() {return qry_wkr;} private Fs_root_wkr qry_wkr;
	public Xof_bin_wkr Bin_wkr() {return null;}

	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_get)) {}
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_get = "get";
}
/*
wiki.files.fsdb.get('root_dir').get('main') {
	url = '~{xowa_root}/a/b';
}
*/
