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
package gplx.stores.xmls; import gplx.*; import gplx.stores.*;
import org.junit.*;
public class XmlDataRdr_tst {
	@Test  public void Read() {
		DataRdr rdr = fx.rdr_("<title id=\"1\" name=\"first\" profiled=\"false\" />");
		Tfds.Eq(rdr.NameOfNode(), "title");
		Tfds.Eq(rdr.ReadStr("name"), "first");
		Tfds.Eq(rdr.ReadInt("id"), 1);
		Tfds.Eq(rdr.ReadBool("profiled"), false);
	}
	@Test  public void None() {
		DataRdr rdr = fx.rdr_
			(	"<root>"
			,		"<find/>"
			,	"</root>"
			);
		fx.tst_Subs_ByName(rdr, "no_nde", "no_atr");
	}
	@Test  public void One() {
		DataRdr rdr = fx.rdr_
			(	"<root>"
			,		"<find id=\"f0\" />"
			,	"</root>"
			);
		fx.tst_Subs_ByName(rdr, "find", "id", "f0");
	}
	@Test  public void One_IgnoreOthers() {
		DataRdr rdr = fx.rdr_
			(	"<root>"
			,		"<find id=\"f0\" />"
			,		"<skip id=\"s0\" />"
			,	"</root>"
			);
		fx.tst_Subs_ByName(rdr, "find", "id", "f0");
	}
	@Test  public void Many() {
		DataRdr rdr = fx.rdr_
			(	"<root>"
			,		"<find id=\"f0\" />"
			,		"<find id=\"f1\" />"
			,	"</root>"
			);
		fx.tst_Subs_ByName(rdr, "find", "id", "f0", "f1");
	}
	@Test  public void Nested() {
		DataRdr rdr = fx.rdr_
			(	"<root>"
			,		"<sub1>"
			,			"<find id=\"f0\" />"
			,			"<find id=\"f1\" />"
			,		"</sub1>"
			,	"</root>"
			);
		fx.tst_Subs_ByName(rdr, "sub1/find", "id", "f0", "f1");
	}
	@Test  public void Nested_IgnoreOthers() {
		DataRdr rdr = fx.rdr_
			(	"<root>"
			,		"<sub1>"
			,			"<find id=\"f0\" />"
			,			"<skip id=\"s0\" />"
			,		"</sub1>"
			,		"<sub1>"
			,			"<find id=\"f1\" />"	// NOTE: find across ndes
			,			"<skip id=\"s1\" />"
			,		"</sub1>"
			,	"</root>"
			);
		fx.tst_Subs_ByName(rdr, "sub1/find", "id", "f0", "f1");
	}
	XmlDataRdr_fxt fx = XmlDataRdr_fxt.new_();
}
class XmlDataRdr_fxt {
	public DataRdr rdr_(String... ary) {return XmlDataRdr_.text_(String_.Concat(ary));}
	public void tst_Subs_ByName(DataRdr rdr, String xpath, String key, String... expdAry) {
		DataRdr subRdr = rdr.Subs_byName(xpath);
		ListAdp list = ListAdp_.new_();
		while (subRdr.MoveNextPeer())
			list.Add(subRdr.Read(key));

		String[] actlAry = list.XtoStrAry();
		Tfds.Eq_ary(actlAry, expdAry);
	}
	public static XmlDataRdr_fxt new_() {return new XmlDataRdr_fxt();} XmlDataRdr_fxt() {}
}
