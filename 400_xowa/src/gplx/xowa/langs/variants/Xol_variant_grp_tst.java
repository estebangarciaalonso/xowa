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
package gplx.xowa.langs.variants; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
import org.junit.*;
import gplx.ios.*;
public class Xol_variant_grp_tst {
	private Xol_variant_grp_fxt fxt = new Xol_variant_grp_fxt();
	@Test   public void csv_() {
		fxt.Test_csv_("zh|zh-hans;zh-hant", "zh", "zh-hans", "zh-hant");
		fxt.Test_csv_("zh|zh-hans;zh-hant;", "zh", "zh-hans", "zh-hant");
	}
}
class Xol_variant_grp_fxt {
	public void Test_csv_(String raw, String grp, String... subs) {
		Xol_variant_grp actl = Xol_variant_grp.csv_(raw);
		Tfds.Eq(grp, String_.new_ascii_(actl.Grp()));
		Tfds.Eq_ary_str(subs, String_.Ary(actl.Subs()));
	}
}
