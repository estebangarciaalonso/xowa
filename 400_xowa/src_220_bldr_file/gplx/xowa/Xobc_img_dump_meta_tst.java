/*
XOWA: the extensible offline wiki application
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
import gplx.ios.*;
import gplx.xowa.bldrs.oimgs.*;
//	public class Xobc_img_dump_meta_tst {
//		Xobc_img_dump_meta_fxt fxt = new Xobc_img_dump_meta_fxt();
//		@Before public void init() {fxt.Clear();}
//		@Test  public void Basic() {
//			fxt.Fxt_dump_ttl().Run
//			(	fxt.page_("File:A.png")
//			,	fxt.page_("File:B_b.png")
//			,	fxt.page_("File:Metropolitan_Museum_of_Art_entrance_NYC.JPG")
//			);
//			fxt.Fxt_dump_sql().Run(String_.Concat
//			(	"INSERT INTO `image` VALUES"
//			,	" ('Metropolitan_Museum_of_Art_entrance_NYC.JPG',1000,100,200,3,16)"
//			,	",('B_b.png',1100,110,210,6,32)"
//			,	",('A.png',1200,120,220,8,48)"
//			,	";"
//			));
//			fxt.Fxt_merge_ttl_sql().Run();
//			fxt.Run();
//			fxt.Tst
//			(	fxt.meta_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv", String_.Concat_lines_nl
//			(		"0|A.png|2?120,220|"
//			,		"0|Metropolitan_Museum_of_Art_entrance_NYC.JPG|2?100,200|"
//			))
//			, 	fxt.meta_("mem/xowa/file/#meta/en.wikipedia.org/2/2d.csv", String_.Concat_lines_nl
//			(		"0|B_b.png|2?110,210|"
//			))
//			);
//		}
//	}
//	class Xobc_img_dump_meta_fxt : Xobc_base_fxt {
//		Tst_mgr tst_mgr = new Tst_mgr();
//		public override void Clear_hook() {
//			this.Init_fxts(this.Bldr(), this.Wiki(), this, fxt_dump_sql, fxt_dump_ttl, fxt_merge_ttl_sql);
//		}	Xobc_img_dump_meta cmd;
//		public Xobc_img_dump_sql_fxt		Fxt_dump_sql()		{return fxt_dump_sql;} private Xobc_img_dump_sql_fxt fxt_dump_sql = new Xobc_img_dump_sql_fxt();
//		public Xobc_img_dump_ttl_fxt		Fxt_dump_ttl()		{return fxt_dump_ttl;} private Xobc_img_dump_ttl_fxt fxt_dump_ttl = new Xobc_img_dump_ttl_fxt();
//		public Xobc_img_merge_ttl_sql_fxt	Fxt_merge_ttl_sql()	{return fxt_merge_ttl_sql;} private Xobc_img_merge_ttl_sql_fxt fxt_merge_ttl_sql = new Xobc_img_merge_ttl_sql_fxt();
//		public Xobc_img_dump_meta Run() {return Run(new Xobc_img_dump_meta(this.Bldr(), this.Wiki()));}
//		public Xobc_img_dump_meta Run(Xobc_img_dump_meta cmd) {
//			this.cmd = cmd;
//			Run_cmd(this.Bldr(), cmd);
//			return cmd;
//		}	
//		public void Tst(params Io_fil_chkr[] expd_ary) {		
//			int expd_ary_len = expd_ary.length;
//			Io_fil[] actl_ary = new Io_fil[expd_ary_len];
//			for (int i = 0; i < expd_ary_len; i++) {
//				Io_fil_chkr expd = expd_ary[i];
//				Io_url expd_url = expd.Expd_url();
//				actl_ary[i] = new Io_fil(expd_url, Io_mgr._.LoadFilStr(expd_url));
//			}
//			tst_mgr.Tst_ary("", expd_ary, actl_ary);
//		}
//	}
