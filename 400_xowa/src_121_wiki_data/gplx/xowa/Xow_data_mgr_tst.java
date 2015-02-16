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
import org.junit.*;
public class Xow_data_mgr_tst {
	Xow_data_mgr_fxt fxt = new Xow_data_mgr_fxt();
	@Before public void init() {fxt.Clear(); Tfds.Now_enabled_y_();}
	@After public void term() {Tfds.Now_enabled_n_();}
	@Test  public void Create() {
		fxt	.Create("A1", "A1 data")
			.Create("B12", "B12 data")
			.Create("C123", "C123 data")
			.Tst_regy_title("0|A1|C123|3\n")
			.Tst_data_title(String_.Concat_lines_nl
			(	"!!!!>|!!!!?|!!!!@|"
			,	"!!!!!|!!!!!|!!!!!|0|!!!!(|A1"
			,	"!!!!\"|!!!!!|!!!!\"|0|!!!!)|B12"
			,	"!!!!#|!!!!!|!!!!#|0|!!!!*|C123"
			))
			.Tst_data_page(String_.Concat_lines_nl
			(	"!!!!9|!!!!;|!!!!=|"
			,	"!!!!!\t##PX+\tA1\tA1 data\t"
			,	"!!!!\"\t##PX/\tB12\tB12 data\t"
			,	"!!!!#\t##PX0\tC123\tC123 data\t"
			))
			;
	}
	@Test  public void Update() {
		fxt	.Create("A1", "A1 data")
			.Create("B12", "B12 data")
			.Create("C123", "C123 data")
			.Update("B12", "B12 changed")
			.Tst_regy_title("0|A1|C123|3\n")
			.Tst_data_title(String_.Concat_lines_nl
			(	"!!!!>|!!!!?|!!!!@|"
			,	"!!!!!|!!!!!|!!!!!|0|!!!!(|A1"
			,	"!!!!\"|!!!!!|!!!!\"|0|!!!!,|B12"
			,	"!!!!#|!!!!!|!!!!#|0|!!!!*|C123"
			))
			.Tst_data_page(String_.Concat_lines_nl
			(	"!!!!9|!!!!>|!!!!=|"
			,	"!!!!!\t##PX+\tA1\tA1 data\t"
			,	"!!!!\"\t##PX/\tB12\tB12 changed\t"
			,	"!!!!#\t##PX0\tC123\tC123 data\t"
			))
			;
	}
	@Test  public void Update_zip() {
//			fxt.Wiki().Fsys_mgr().Dir_regy()[Xow_ns_.Id_main].Ext_tid_(gplx.ios.Io_stream_.Tid_zip);
//			fxt.Wiki().Data_mgr().Zip_mgr_(new Io_zip_mgr_mok());
//			fxt	.Create("A1", "A1 data")
//				.Create("B12", "B12 data")
//				.Create("C123", "C123 data")
//				.Update("B12", "B12 changed")
//				.Tst_regy_title("0|A1|C123|3\n")
//				.Tst_data_title(String_.Concat_lines_nl
//				(	"!!!!>|!!!!?|!!!!@|"
//				,	"!!!!!|!!!!!|!!!!!|0|!!!!(|A1"
//				,	"!!!!\"|!!!!!|!!!!\"|0|!!!!,|B12"
//				,	"!!!!#|!!!!!|!!!!#|0|!!!!*|C123"
//				))
//				.Tst_data_page(String_.Concat_lines_nl
//				(	"zipped:!!!!9|!!!!>|!!!!=|"
//				,	"!!!!!\t##PX+\tA1\tA1 data\t"
//				,	"!!!!\"\t##PX/\tB12\tB12 changed\t"
//				,	"!!!!#\t##PX0\tC123\tC123 data\t"
//				))
//				;
	}
	@Test  public void Create_out_of_order() {
		fxt	.Create("C123", "C123 data")
			.Create("B12", "B12 data")
			.Create("A1", "A1 data")
			.Tst_regy_title("0|A1|C123|3\n")
			.Tst_data_title(String_.Concat_lines_nl
			(	"!!!!>|!!!!?|!!!!@|"
			,	"!!!!#|!!!!!|!!!!#|0|!!!!(|A1"
			,	"!!!!\"|!!!!!|!!!!\"|0|!!!!)|B12"
			,	"!!!!!|!!!!!|!!!!!|0|!!!!*|C123"
			))
			.Tst_data_page(String_.Concat_lines_nl
			(	"!!!!=|!!!!;|!!!!9|"
			,	"!!!!!\t##PX+\tC123\tC123 data\t"
			,	"!!!!\"\t##PX/\tB12\tB12 data\t"
			,	"!!!!#\t##PX0\tA1\tA1 data\t"
			))
			;
	}
	@Test  public void Rename() {
		fxt	.Create("A1", "A1 data")
			.Create("B12", "B12 data")
			.Create("C123", "C123 data")
			.Rename("C123", "C1234")
			.Tst_regy_title("0|A1|C123|3\n")
			.Tst_data_title(String_.Concat_lines_nl
			(	"!!!!>|!!!!?|!!!!@|"
			,	"!!!!!|!!!!!|!!!!!|0|!!!!(|A1"
			,	"!!!!\"|!!!!!|!!!!\"|0|!!!!)|B12"
			,	"!!!!#|!!!!!|!!!!#|0|!!!!*|C123"
			))
			.Tst_data_page(String_.Concat_lines_nl
			(	"!!!!9|!!!!;|!!!!=|"
			,	"!!!!!\t##PX+\tA1\tA1 data\t"
			,	"!!!!\"\t##PX/\tB12\tB12 data\t"
			,	"!!!!#\t##PX0\tC123\tC123 data\t"
			))
			;
	}
}
class Xow_data_mgr_fxt {
	Xoa_app app;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public void Clear() {
		app = Xoa_app_fxt.app_();
		wiki = Xoa_app_fxt.wiki_tst_(app);
		wiki.Db_mgr().Save_mgr().Page_id_next_(0);
	}
	public Xow_data_mgr_fxt Create(String ttl_str, String data) {
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, Bry_.new_utf8_(ttl_str));
		wiki.Db_mgr().Save_mgr().Data_create(ttl, Bry_.new_utf8_(data));
		return this;
	}
	public Xow_data_mgr_fxt Update(String ttl_str, String data) {
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, Bry_.new_utf8_(ttl_str));
		Xoa_page page = Xoa_page.test_(wiki, ttl);
		wiki.Db_mgr().Save_mgr().Data_update(page, Bry_.new_utf8_(data));
		return this;
	}
	public Xow_data_mgr_fxt Rename(String old_ttl, String new_ttl) {
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, Bry_.new_utf8_(old_ttl));
		Xoa_page page = Xoa_page.test_(wiki, ttl);
		wiki.Db_mgr().Save_mgr().Data_rename(page, ttl.Ns().Id(), Bry_.new_utf8_(new_ttl));
		return this;
	}
	public Xow_data_mgr_fxt Tst_regy_title(String expd) {return Tst_regy(Xow_dir_info_.Name_title, expd);}
	Xow_data_mgr_fxt Tst_regy(String name, String expd) {
		Io_url file_orig = Io_url_.mem_fil_("mem/xowa/wiki/en.wikipedia.org/ns/000/" + name + "/reg.csv");
		Tfds.Eq_str_lines(expd, Io_mgr._.LoadFilStr(file_orig));
		return this;
	}
	public Xow_data_mgr_fxt Tst_data_page(String expd) {return Tst_data(Xow_dir_info_.Tid_page , Xow_ns_.Id_main, 0, expd);}
	public Xow_data_mgr_fxt Tst_data_title(String expd) {return Tst_data(Xow_dir_info_.Tid_ttl, Xow_ns_.Id_main, 0, expd);}
	public Xow_data_mgr_fxt Tst_data(byte dir_tid, int ns_id, int fil, String expd) {
		Io_url url = wiki.Fsys_mgr().Url_ns_fil(dir_tid, ns_id, fil);
		Tfds.Eq_str_lines(expd, Io_mgr._.LoadFilStr(url));
		return this;
	}
}
