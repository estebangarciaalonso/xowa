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
class Xop_vnt_data {
	public Xop_vnt_data(byte[] lang, byte[] data) {this.lang = lang; this.data = data;}
	public byte[] Lang() {return lang;} private byte[] lang;
	public byte[] Data() {return data;} private byte[] data;
}
class Xop_vnt_cmd {
	public Xop_vnt_cmd(Xop_vnt_flag[] vnt_flag_ary, Xop_vnt_data[] vnt_data_ary) {this.vnt_flag_ary = vnt_flag_ary; this.vnt_data_ary = vnt_data_ary;}
	public Xop_vnt_flag[] Vnt_flag_ary() {return vnt_flag_ary;} private Xop_vnt_flag[] vnt_flag_ary;
	public Xop_vnt_data[] Vnt_data_ary() {return vnt_data_ary;} private Xop_vnt_data[] vnt_data_ary;
}
