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
package gplx.xowa; import gplx.*;
public class Xof_ext {
	public Xof_ext(int id, byte[] ext) {this.id = id; this.ext = ext;}
	public int Id() {return id;} private int id;
	public byte[] Ext() {return ext;} private byte[] ext;
	public byte[] Ext_view() {return Xof_ext_.Bry__ary[Id_view()];}
	public byte[] Mime_type() {return Xof_ext_.Mime_type__ary[id];}
	public boolean Id_is_unknown() {return id == Xof_ext_.Id_unknown;}
	public boolean Id_is_thumbable() {
		switch (id) {
			case Xof_ext_.Id_png: case Xof_ext_.Id_jpg: case Xof_ext_.Id_jpeg: case Xof_ext_.Id_gif: case Xof_ext_.Id_tif: case Xof_ext_.Id_tiff: case Xof_ext_.Id_svg: case Xof_ext_.Id_djvu: case Xof_ext_.Id_pdf:
				return true;
			default:
				return false;
		}
	}
	public boolean Id_is_thumbable2()	{
		switch (id) {
		case Xof_ext_.Id_unknown: case Xof_ext_.Id_oga:	// NOTE: removed Xof_ext_.Id_ogg; some vids can be ogg
			return false;
		default:
			return true;
		}
	}
	public boolean Id_is_image() {
		switch (id) {
			case Xof_ext_.Id_png: case Xof_ext_.Id_jpg: case Xof_ext_.Id_jpeg: case Xof_ext_.Id_gif: case Xof_ext_.Id_tif: case Xof_ext_.Id_tiff: case Xof_ext_.Id_svg:
				return true;
			default:
				return false;
		}
	}
	public boolean Id_is_media() {return Id_is_audio() || Id_is_video();}
	public boolean Id_is_audio() {
		switch (id) {
			case Xof_ext_.Id_mid: case Xof_ext_.Id_oga: case Xof_ext_.Id_ogg: return true;
			default: return false;
		}
	}
	public boolean Id_is_video()	{return id == Xof_ext_.Id_ogv || id == Xof_ext_.Id_ogg || id == Xof_ext_.Id_webm;}	// NOTE: ogg can be vid; EX.WP: Comet; Encke_tail_rip_off.ogg
	public boolean Id_is_svg()		{return id == Xof_ext_.Id_svg;}
	public boolean Id_is_ogv()		{return id == Xof_ext_.Id_ogv;}
	public boolean Id_is_ogg()		{return id == Xof_ext_.Id_ogg;}
	public boolean Id_is_djvu()	{return id == Xof_ext_.Id_djvu;}
	public boolean Id_is_pdf()		{return id == Xof_ext_.Id_pdf;}
	public boolean Id_needs_convert()	{
		switch (id) {
			case Xof_ext_.Id_svg: case Xof_ext_.Id_djvu: case Xof_ext_.Id_pdf: return true;
			default: return false;
		}
	}
	public int Id_view() {
		switch (id) {
			case Xof_ext_.Id_svg:																			return Xof_ext_.Id_png;
			case Xof_ext_.Id_tif: case Xof_ext_.Id_tiff: case Xof_ext_.Id_djvu: case Xof_ext_.Id_pdf: 
			case Xof_ext_.Id_ogg: case Xof_ext_.Id_ogv: case Xof_ext_.Id_webm:								return Xof_ext_.Id_jpg;
			default:																						return id;
		}
	}
	public String XtoStr() {return String_.Format("id={0} ext={1}", id, String_.new_utf8_(ext));}
}
