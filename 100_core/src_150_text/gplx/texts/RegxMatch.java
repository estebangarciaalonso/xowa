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
package gplx.texts; import gplx.*;
public class RegxMatch {
	public RegxMatch(boolean rslt, int find_bgn, int find_end, RegxGroup[] groups) {this.rslt = rslt; this.find_bgn = find_bgn; this.find_end = find_end; this.groups = groups;}
	public boolean Rslt() {return rslt;} private boolean rslt;
	public boolean Rslt_none() {return !rslt;}	// NOTE: was "|| find_end - find_bgn == 0"; DATE:2013-04-11; DATE:2014-09-02
	public int Find_bgn() {return find_bgn;} int find_bgn;
	public int Find_end() {return find_end;} int find_end;
	public int Find_len() {return find_end - find_bgn;}
	public RegxGroup[] Groups() {return groups;} RegxGroup[] groups = RegxGroup.Ary_empty;
	public static final RegxMatch[] Ary_empty = new RegxMatch[0];
}
