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
package gplx.xowa.xtns.wdatas.core; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.wdatas.*;
import org.junit.*;
import gplx.json.*; import gplx.xowa.xtns.wdatas.core.*; import gplx.xowa.xtns.wdatas.parsers.*; import gplx.xowa.xtns.wdatas.hwtrs.*;
public class Wdata_date_tst {
	@Before public void init() {fxt.Clear();} private Wdata_date_fxt fxt = new Wdata_date_fxt();
	@Test   public void Parse() {
		fxt.Test_parse("+00000002001-02-03T04:05:06Z",          2001, 2, 3, 4, 5, 6);
		fxt.Test_parse("-98765432109-02-03T04:05:06Z", -98765432109L, 2, 3, 4, 5, 6);
	}
	@Test   public void Julian() {
		fxt.Test_julian(Int_.Ary(1600, 1, 2), Int_.Ary(1600, 1, 18));
	}
	@Test   public void Xto_str() {
		String date = "+00000002001-02-03T04:05:06Z";
		fxt.Test_xto_str(date, Wdata_date.Fmt_ym		, "Feb 2001");
		fxt.Test_xto_str(date, Wdata_date.Fmt_ymd		, "3 Feb 2001");
		fxt.Test_xto_str(date, Wdata_date.Fmt_ymdh		, "4:00 3 Feb 2001");
		fxt.Test_xto_str(date, Wdata_date.Fmt_ymdhn		, "4:05 3 Feb 2001");
		fxt.Test_xto_str(date, Wdata_date.Fmt_ymdhns	, "4:05:06 3 Feb 2001");
	}
	@Test   public void Xto_str_year() {
		fxt.Test_xto_str("-00000001234-01-01T00:00:00Z",  9, "1234 BC");
		fxt.Test_xto_str("+00000001987-01-01T00:00:00Z",  8, "1980s");
		fxt.Test_xto_str("+00000001987-01-01T00:00:00Z",  7, "19. century");
		fxt.Test_xto_str("+00000001987-01-01T00:00:00Z",  6, "1. millenium");
		fxt.Test_xto_str("+00000012345-01-01T00:00:00Z",  5, "10,000 years");
		fxt.Test_xto_str("+00000123456-01-01T00:00:00Z",  4, "in 100,000 years");
	}
	@Test   public void Xto_str_julian() {
		fxt.Init_calendar_is_julian_(Bool_.Y).Test_xto_str("+00000001600-01-02T00:00:00Z", Wdata_date.Fmt_ymd, "18 Jan 1600<sup>jul</sup>");
	}
	@Test   public void Xto_str_before_after() {
		String date = "+00000002001-02-03T04:05:06Z";
		fxt.Clear().Init_before_(1).Test_xto_str(date, Wdata_date.Fmt_ymd, "3 Feb 2001 (-1)");
		fxt.Clear().Init_after_ (1).Test_xto_str(date, Wdata_date.Fmt_ymd, "3 Feb 2001 (+1)");
		fxt.Clear().Init_before_(1).Init_after_(1).Test_xto_str(date, Wdata_date.Fmt_ymd, "3 Feb 2001 (±1)");
		fxt.Clear().Init_before_(1).Init_after_(2).Test_xto_str(date, Wdata_date.Fmt_ymd, "3 Feb 2001 (-1,&#32;+2)");
	}
}
class Wdata_date_fxt {
	private Bry_bfr tmp_bfr = Bry_bfr.new_(16);
	private Wdata_hwtr_msgs msgs;
	private final Bry_fmtr tmp_time_fmtr = Bry_fmtr.new_(); private final Bry_bfr tmp_time_bfr = Bry_bfr.new_(32);		
	public Wdata_date_fxt Clear() {
		init_before = init_after = 0;
		init_calendar_is_julian = false;
		return this;
	}
	public boolean Init_calendar_is_julian() {return init_calendar_is_julian;} public Wdata_date_fxt Init_calendar_is_julian_(boolean v) {init_calendar_is_julian = v; return this;} private boolean init_calendar_is_julian;
	public int Init_before() {return init_before;} public Wdata_date_fxt Init_before_(int v) {init_before = v; return this;} private int init_before;
	public int Init_after() {return init_after;} public Wdata_date_fxt Init_after_(int v) {init_after = v; return this;} private int init_after;
	public void Test_parse(String raw, long expd_y, int expd_m, int expd_d, int expd_h, int expd_n, int expd_s) {
		Wdata_date actl_date = Wdata_date.parse(Bry_.new_ascii_(raw), Wdata_date.Fmt_ymdhns, init_before, init_after, init_calendar_is_julian);
		Tfds.Eq(expd_y, actl_date.Year());
		Tfds.Eq(expd_m, actl_date.Month());
		Tfds.Eq(expd_d, actl_date.Day());
		Tfds.Eq(expd_h, actl_date.Hour());
		Tfds.Eq(expd_n, actl_date.Minute());
		Tfds.Eq(expd_s, actl_date.Second());
	}
	public void Test_julian(int[] orig_ary, int[] expd) {
		Wdata_date orig = new Wdata_date(orig_ary[0], orig_ary[1], orig_ary[2], 0, 0, 0, 0, 0, 0, init_calendar_is_julian);
		Wdata_date actl = Wdata_date.Xto_julian(orig);
		Tfds.Eq(expd[0], (int)actl.Year(), "y");
		Tfds.Eq(expd[1], actl.Month(), "m");
		Tfds.Eq(expd[2], actl.Day(), "d");
	}
	public void Test_xto_str(String raw, int precision, String expd) {
		if (msgs == null) msgs = Wdata_hwtr_msgs.new_en_();
		Wdata_date date = Wdata_date.parse(Bry_.new_ascii_(raw), precision, init_before, init_after, init_calendar_is_julian);
		Wdata_date.Xto_str(tmp_bfr, tmp_time_fmtr, tmp_time_bfr, msgs, date);
		Tfds.Eq(expd, tmp_bfr.Xto_str_and_clear());
	}
}
