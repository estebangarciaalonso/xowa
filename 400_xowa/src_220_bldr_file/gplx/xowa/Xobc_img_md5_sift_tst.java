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
public class Xobc_img_md5_sift_tst {
	Xobc_img_md5_sift_fxt fxt = new Xobc_img_md5_sift_fxt();
	@Test  public void Basic() {
		fxt.create_
		(	fxt.meta_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv", String_.Concat_lines_nl
		(		"0|0|A.png|!|2?120,220|"
		))
		,	fxt.meta_("mem/xowa/file/#meta/en.wikipedia.org/2/2d.csv", String_.Concat_lines_nl
		(		"0|0|B.png|!|2?110,210|"
		))
		)
		;
		/*
		fxt.new_files
		(	"C.png"
		,	"A.png|140?140"
		)
		fxt.expd_
		(	"70.csv"
		,		A.png|120|120
		,	"71.csv"
		,		B.png|12012
		);
		*/				
	}
}
class Xobc_img_md5_sift_fxt extends Xobc_base_fxt {
	public Xobc_img_md5_sift_fxt create_(Io_fil_chkr... fil_ary) {
		int fil_ary_len = fil_ary.length;
		for (int i = 0; i < fil_ary_len; i++) {
			Io_fil_chkr fil = fil_ary[i];
			Io_mgr._.SaveFilStr(fil.Expd_url(), fil.Expd_data());
		}
		return this;
	}
}
