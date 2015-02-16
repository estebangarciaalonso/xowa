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
package gplx;
import gplx.core.strings.*;
public class PerfLogMgr_fxt {
	public void Init(Io_url url, String text) {
		this.url = url;
		entries.ResizeBounds(1000);
		entries.Add(new PerfLogItm(0, text + "|" + DateAdp_.Now().XtoStr_gplx()));
		tmr.Bgn();
	}
	public void Write(String text) {
		long milliseconds = tmr.ElapsedMilliseconds();
		entries.Add(new PerfLogItm(milliseconds, text));
		tmr.Bgn();
	}
	public void WriteFormat(String fmt, Object... ary) {
		long milliseconds = tmr.ElapsedMilliseconds();
		String text = String_.Format(fmt, ary);
		entries.Add(new PerfLogItm(milliseconds, text));
		tmr.Bgn();
	}
	public void Flush() {
		String_bldr sb = String_bldr_.new_();
		for (Object itmObj : entries) {
			PerfLogItm itm = (PerfLogItm)itmObj;
			sb.Add(itm.XtoStr()).Add_char_crlf();
		}
		Io_mgr._.AppendFilStr(url, sb.XtoStr());
		entries.Clear();
	}
	ListAdp entries = ListAdp_.new_(); PerfLogTmr tmr = PerfLogTmr.new_(); Io_url url = Io_url_.Null;
	public static final PerfLogMgr_fxt _ = new PerfLogMgr_fxt(); PerfLogMgr_fxt() {}
	class PerfLogItm {
		public String XtoStr() {
			String secondsStr = TimeSpanAdp_.XtoStr(milliseconds, TimeSpanAdp_.Fmt_Default);
			secondsStr = String_.PadBgn(secondsStr, 7, "0"); // 7=000.000; left-aligns all times
			return String_.Concat(secondsStr, "|", text);
		}
		long milliseconds; String text;
		@gplx.Internal protected PerfLogItm(long milliseconds, String text) {
			this.milliseconds = milliseconds; this.text = text;
		}
	}

}
class PerfLogTmr {
	public void Bgn() {bgn = Env_.TickCount();} long bgn;
	public long ElapsedMilliseconds() {return Env_.TickCount() - bgn;	}		
	public static PerfLogTmr new_() {return new PerfLogTmr();} PerfLogTmr() {}
}
