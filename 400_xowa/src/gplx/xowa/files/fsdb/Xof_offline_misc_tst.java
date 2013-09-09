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
import org.junit.*;
import gplx.fsdb.*;
public class Xof_offline_misc_tst {
	@Before public void init() {if (Xo_test.Db_skip) return; fxt.Reset();} private Xof_file_fxt fxt = new Xof_file_fxt();
	@After public void term() {if (Xo_test.Db_skip) return; fxt.Rls();}
	@Test   public void Qry_missing() {
		if (Xo_test.Db_skip) return;
		fxt.Test_get(Xof_fsdb_arg_exec_get.new_().Init_thumb("A.png").Rslt_reg_missing_reg().Rslt_qry_unavailable_());
		fxt.Test_regy_missing_qry(fxt.Key_bldr().Bld_as_str("A.png", Xop_lnki_type.Id_thumb, 220));
		fxt.Test_get(Xof_fsdb_arg_exec_get.new_().Init_thumb("A.png").Rslt_reg_missing_qry().Rslt_qry_noop_()); 
	}
	@Test   public void Bin_missing() {
		if (Xo_test.Db_skip) return;
		fxt.Init_qry_xowa(Xof_fsdb_arg_init_qry.new_().Init_commons("A.png", 440, 400));
		fxt.Test_get(Xof_fsdb_arg_exec_get.new_().Init_thumb("A.png").Rslt_reg_missing_reg().Rslt_qry_mock_().Rslt_bin_not_found_());
		fxt.Test_regy_missing_bin(fxt.Key_bldr().Bld_as_str("A.png", Xop_lnki_type.Id_thumb, 220));
		fxt.Test_get(Xof_fsdb_arg_exec_get.new_().Init_thumb("A.png").Rslt_reg_missing_bin().Rslt_qry_noop_());
	}
}
