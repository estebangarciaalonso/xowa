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
package gplx.xowa.langs.vnts; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
import org.junit.*;
public class Xop_vnt_parser_tst {
	private Xop_vnt_parser_fxt fxt = new Xop_vnt_parser_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Unknown()					{fxt.Exec_parse("-{ X |a}-")					.Test_cmd_unknown();}
//		@Test  public void Raw_basic()					{fxt.Exec_parse("-{R|a}-")						.Test_cmd_raw();}
//		@Test  public void Raw_ws()						{fxt.Exec_parse("-{ R |a}-")					.Test_cmd_raw();}
//		@Test  public void Raw_unknown()				{fxt.Exec_parse("-{ R x |a}-")					.Test_cmd_unknown();}
//		@Test  public void Vnts_basic()					{fxt.Exec_parse("-{zh-hans;zh-hant|a}-")		.Test_cmd_vnts("zh-hans", "zh-hant");}
//		@Test  public void Vnts_semic()					{fxt.Exec_parse("-{zh-hans;zh-hant;|a}-")		.Test_cmd_vnts("zh-hans", "zh-hant");}
//		@Test  public void Vnts_ws()					{fxt.Exec_parse("-{ zh-hans ; zh-hant ; |a}-")	.Test_cmd_vnts("zh-hans", "zh-hant");}
//		@Test  public void Vnts_unknown_1st()			{fxt.Exec_parse("-{ zh-hans x ; zh-hant ; |a}-").Test_cmd_vnts("zh-hans");}
//		@Test  public void Vnts_unknown_nth()			{fxt.Exec_parse("-{ zh-hans ; zh-hant x; |a}-")	.Test_cmd_vnts("zh-hans");}
//		@Test  public void Vnts_unknown_all()			{fxt.Exec_parse("-{ zh-hans x ; zh-hant x;|a}-").Test_cmd_unknown();}
}
class Xop_vnt_parser_fxt {
	private Xop_vnt_parser parser = new Xop_vnt_parser();
	public void Clear() {
		Xop_vnt_flag_lang_bldr flag_lang_bldr = new Xop_vnt_flag_lang_bldr();
		flag_lang_bldr.Init(ByteAry_.Ary("zh-hans", "zh-hant"));
		parser.Init(flag_lang_bldr);
	}
	public Xop_vnt_parser_fxt Exec_parse(String src_str) {
		byte[] src_bry = ByteAry_.new_utf8_(src_str);
		parser.Parse(src_bry, src_bry.length, 0);
		return this;
	}
	public Xop_vnt_parser_fxt Test_cmd_raw() {
		return this;
	}
	public Xop_vnt_parser_fxt Test_cmd_unknown() {
		return this;
	}
	public Xop_vnt_parser_fxt Test_cmd_vnts(String... vnts) {
		return this;
	}
}
