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
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import org.junit.*;
import gplx.ios.*;
public class Xobc_img_dump_sql_tst {
	@Before public void init() {fxt.Init();} private Xobc_img_dump_sql_fxt fxt = new Xobc_img_dump_sql_fxt();
	@After public void term() {fxt.Term();} 
	@Test     public void Basic() {
		fxt.Run(String_.Concat
		(	"INSERT INTO `image` VALUES"
		,	" ('B.jpg',10,1,2,3,16)"
		,	",('A.jpg',20,2,4,6,32)"
		,	",('C.JPG',30,4,6,8,48)"
		,	";"
		));
		fxt.Tst(String_.Concat_lines_nl
		(	"A.jpg||20|2|4|32|"
		,	"B.jpg||10|1|2|16|"
		,	"C.jpg|JPG|30|4|6|48|"
		));
		;
	}
}
