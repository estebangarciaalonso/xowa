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
package gplx.xowa.bldrs.wikis.images; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*; import gplx.xowa.bldrs.wikis.*;
import org.junit.*;
import gplx.ios.*;
public class Xob_image_ext_calc_tst {		
	private Xob_image_ext_calc_fxt fxt = new Xob_image_ext_calc_fxt();
	@Test  public void Basic()					{fxt.Init_("A.png"	, Xof_media_type.Name_bitmap	, Xof_ext_.Bry_png, 220, 110)		.Test(Xof_ext_.Id_png);}	// A.png -> png
	@Test  public void Ogg_VIDEO()				{fxt.Init_("A.ogg"	, Xof_media_type.Name_video		, Xof_ext_.Bry_ogg, 220, 110)		.Test(Xof_ext_.Id_ogv);}	// A.ogg and VIDEO -> ogv
	@Test  public void Ogg_VIDEO_null_size()	{fxt.Init_("A.ogg"	, Xof_media_type.Name_video		, Xof_ext_.Bry_ogg,   0,   0)		.Test(Xof_ext_.Id_ogg);}	// A.ogg but 0,0 -> ogg (not ogv)
	@Test  public void Png_is_jpg()				{fxt.Init_("A.png"	, Xof_media_type.Name_bitmap	, Xof_ext_.Bry_jpg, 220, 110)		.Test(Xof_ext_.Id_jpg);}	// A.png and jpg -> jpg
	@Test  public void Jpeg_is_jpeg()			{fxt.Init_("A.jpeg"	, Xof_media_type.Name_bitmap	, Xof_ext_.Bry_jpg, 220, 110)		.Test(Xof_ext_.Id_jpeg);}	// A.jpeg and jpg -> jpeg (unchanged)
}
class Xob_image_ext_calc_fxt {
	private byte[] name, media_type, minor_mime; int w, h;
	public Xob_image_ext_calc_fxt Init_png() {Name_("A.png").Media_type_(Xof_media_type.Name_bitmap).Minor_mime_(Xof_ext_.Bry_png).W_(220).H_(110);
		return this;
	}
	public Xob_image_ext_calc_fxt Init_(String name, String media_type, byte[] minor_mime, int w, int h) {
		Name_(name);
		Media_type_(media_type); 
		Minor_mime_(minor_mime);
		W_(w);
		H_(h);
		return this;
	}
	public Xob_image_ext_calc_fxt Name_(String v) {name = Bry_.new_ascii_(v); return this;}
	public Xob_image_ext_calc_fxt Media_type_(String v) {media_type = Bry_.new_ascii_(v); return this;}
	public Xob_image_ext_calc_fxt Minor_mime_(byte[] v) {minor_mime = v; return this;}
	public Xob_image_ext_calc_fxt Minor_mime_(String v) {return Minor_mime_(Bry_.new_ascii_(v));}
	public Xob_image_ext_calc_fxt W_(int v) {w = v; return this;}
	public Xob_image_ext_calc_fxt H_(int v) {h = v; return this;}
	public Xob_image_ext_calc_fxt Test(int expd) {
		Tfds.Eq(expd, Xob_wiki_image_sql.Calc_ext_id(Gfo_usr_dlg_.Null, name, media_type, minor_mime, w, h));
		return this;
	}
}
