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
package gplx.fsdb; import gplx.*;
import gplx.dbs.*;
public class Fsdb_db_bin_mgr implements RlsAble {
	private Io_url dir; private String wiki_domain;
	private Fsdb_db_bin_fil[] itms = Fsdb_db_bin_fil.Ary_empty; private int itms_len = 0;
	private Fsdb_db_bin_fil itms_n;
	private Fsdb_db_bin_mgr(Io_url dir, String wiki_domain) {this.dir = dir; this.wiki_domain = wiki_domain;}
	public int Len() {return itms.length;}
	public Fsdb_db_bin_fil Get_at(int i) {return itms[i];}
	public void Commit(Db_provider provider) {		
		Fsdb_db_bin_tbl.Commit_all(provider, itms);
	}
	public void Rls() {
		int len = itms.length;
		for (int i = 0; i < len; i++) {
			Fsdb_db_bin_fil itm = itms[i];
			itm.Rls();
		}
	}
	public int Get_id_for_insert(long bin_len) {
		long new_bin_len = itms_n.Bin_len() + bin_len;
		if (new_bin_len > itms_n.Bin_max())
			Itms_add(bin_len);
		else
			itms_n.Bin_len_(new_bin_len);
		return itms_n.Id();
	}
	public void Insert(int db_id, int bin_id, byte owner_tid, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		Fsdb_db_bin_fil bin_fil = itms[db_id];
		bin_fil.Insert(bin_id, owner_tid, bin_len, bin_rdr);
	}
	public static Fsdb_db_bin_mgr load_(Db_provider p, Io_url dir, String wiki_domain) {
		Fsdb_db_bin_mgr rv = new Fsdb_db_bin_mgr(dir, wiki_domain);
		rv.itms = Fsdb_db_bin_tbl.Select_all(p, dir);
		rv.itms_len = rv.itms.length;
		rv.itms_n = rv.itms[rv.itms_len - 1];
		return rv;
	}
	public static Fsdb_db_bin_mgr make_(Db_provider p, Io_url dir, String wiki_domain) {
		Fsdb_db_bin_tbl.Create_table(p);
		Fsdb_db_bin_mgr rv = new Fsdb_db_bin_mgr(dir, wiki_domain);
		rv.Itms_add(0);
		return rv;
	}
	private void Itms_add(long bin_len) {
		int new_itms_len = itms_len + 1;
		Fsdb_db_bin_fil[] new_itms = new Fsdb_db_bin_fil[new_itms_len];
		for (int i = 0; i < itms_len; i++)
			new_itms[i] = itms[i];
		itms_n = Fsdb_db_bin_fil.make_(itms_len, Fsdb_db_bin_fil.url_(dir, wiki_domain, itms_len), bin_len);
		itms = new_itms;
		itms_len = new_itms_len;
		itms[itms_len - 1] = itms_n;
	}
}
