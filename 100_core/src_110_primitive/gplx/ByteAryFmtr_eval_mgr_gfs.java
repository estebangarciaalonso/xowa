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
public class ByteAryFmtr_eval_mgr_gfs implements ByteAryFmtr_eval_mgr {
	public boolean Enabled() {return enabled;} public void Enabled_(boolean v) {enabled = v;} private boolean enabled;
	public byte[] Eval(byte[] cmd) {			
		return enabled ? ByteAry_.new_utf8_(Object_.XtoStr_OrNullStr(GfsCore._.ExecText(String_.new_utf8_(cmd)))) : null;
	}
        public static final ByteAryFmtr_eval_mgr_gfs _ = new ByteAryFmtr_eval_mgr_gfs(); ByteAryFmtr_eval_mgr_gfs() {}
}
