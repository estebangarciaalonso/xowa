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
package gplx.xowa.files; import gplx.*; import gplx.xowa.*;
import org.junit.*; import gplx.xowa.files.*;
public class Xof_img_size_tst {		
	@Before public void init() {fxt.Reset();} private Xof_img_size_fxt fxt = new Xof_img_size_fxt();
	@Test   public void Box()								{fxt.Lnki_type_(Xop_lnki_type.Id_null).Lnki_( 40,  50).Orig_( 40,  40).Test_html( 40,  40);}	// EX:[[File:Crystal Clear app kedit.svg|50x40px]]
	@Test   public void Long_w()							{fxt.Lnki_type_(Xop_lnki_type.Id_null).Lnki_(128,  80).Orig_(720, 194).Test_html(128,  34);}	// EX:[[File:Firma B.Ohiggins.svg|128x80px|alt=|Bernardo O'Higgins's signature]]
	@Test   public void Long_h()							{fxt.Lnki_(300,  -1).Orig_(149, 408).Test_html(149, 408);}	// EX:[[File:Adhanema Lasva.jpg|thumb|300px|The Firman given to the Bosnian Franciscans]]
	@Test   public void Width_too_long()					{fxt.Lnki_(100,  -1).Orig_( 40,  40).Test_html( 40,  40);}	// limit to height;
	@Test   public void Width_missing()						{fxt.Lnki_( -1,  20).Orig_( 80,  40).Test_html( 40,  20);}	// calc width based on height and file_size
	@Test   public void Prefer_height_over_width()			{fxt.Lnki_( 60,  20).Orig_(120,  60).Test_html( 40,  20);}	// prefer height; if width was preferred, size would be 60,30
	@Test   public void Height_missing()					{fxt.Lnki_( 50,  -1).Orig_(100, 200).Test_html( 50, 100);}
	@Test   public void Explicit_ratio_large()				{fxt.Lnki_(120,  40).Orig_(200, 100).Test_html( 80,  40);}	// see NOTE_2: lnki_ratio > orig_ratio
	@Test   public void Explicit_ratio_small()				{fxt.Lnki_(120,  80).Orig_(200, 100).Test_html(120,  60);}	// see NOTE_2: lnki_ratio > orig_ratio
	@Test  	public void Orig_size_n() 						{fxt.Lnki_(300, 200).Orig_( -1,  -1).Test_html(300, 200);}	// no orig_size; use lnki_w and lnki_h
	@Test  	public void Lnki_size_missing() 				{fxt.Lnki_( -1,  -1).Orig_( -1,  -1).Test_html(220,  -1);}	// no lnki or orig size; default to 220
	@Test  	public void Missing_lnki_w_h() 					{fxt.Lnki_( -1,  -1).Orig_(440, 400).Test_html(220, 200);}	// w=thumbnail default; h=calc from orig
	@Test  	public void Missing_lnki_h() 					{fxt.Lnki_(200,  -1).Orig_(400, 500).Test_html(200, 250);}	// w=lnki; h=calc from orig
	@Test  	public void Missing_lnki_w() 					{fxt.Lnki_( -1, 250).Orig_(400, 500).Test_html(200, 250);}	// w=calc from orig
	@Test  	public void Limit_to_size() 					{fxt.Lnki_(600, 750).Orig_(400, 500).Test_html(400, 500);}	// w/h: truncate to file
	@Test  	public void W_is_wrong() 						{fxt.Lnki_( 20,  20).Orig_( 80, 100).Test_html( 16,  20);}
	@Test  	public void W_is_wrong_2() 						{fxt.Lnki_( 65,  50).Orig_(160, 160).Test_html( 50,  50);}
	@Test  	public void Size_is_wrong() 					{fxt.Lnki_(128,  80).Orig_(720, 194).Test_html(128,  34);}
	@Test  	public void Thumb_upright() 					{fxt.Lnki_upright_(1).Lnki_(300, 200).Orig_( -1,  -1).Test_html(230, 200);}			// w=.(75*300) ROUND 10; is 200 right???
	@Test  	public void Orig_exceeds() 						{fxt.Lnki_type_(Xop_lnki_type.Id_null).Lnki_(600, 400).Orig_(300, 200).Test_html(600, 400, true);}
	@Test  	public void Orig_execeeds_svg() {
		fxt	.Lnki_type_(Xop_lnki_type.Id_null)
			.Lnki_(600, 400)
			.Orig_(300, 200)
			.Lnki_ext_(Xof_ext_.Id_svg)
			.Test_html(600, 400, false);
	}
}
class Xof_img_size_fxt {
	private Xof_img_size img_size = new Xof_img_size();
	public Xof_img_size_fxt Reset() {
		lnki_type = Xop_lnki_type.Id_thumb;
		lnki_ext = Xof_ext_.Id_jpg;
		lnki_upright = Xof_img_size.Upright_null;
		orig_w = orig_h = lnki_w = lnki_h = Xof_img_size.Null;
		return this;
	}
	public Xof_img_size_fxt Lnki_type_(byte v) {lnki_type = v; return this;} private byte lnki_type;
	public Xof_img_size_fxt Lnki_ext_(int v) {lnki_ext = v; return this;} private int lnki_ext;
	public Xof_img_size_fxt Lnki_upright_(double v) {lnki_upright = v; return this;} private double lnki_upright;
	public Xof_img_size_fxt Orig_(int w, int h) {orig_w = w; orig_h = h; return this;} private int orig_w, orig_h;
	public Xof_img_size_fxt Lnki_(int w, int h) {lnki_w = w; lnki_h = h; return this;} private int lnki_w, lnki_h;
	public void Test_html(int expd_w, int expd_h) {Test_html(expd_w, expd_h, false);}
	public void Test_html(int expd_html_w, int expd_html_h, boolean expd_file_is_orig) {
		img_size.Html_size_calc(Xof_exec_tid.Tid_wiki_page, lnki_w, lnki_h, lnki_type, lnki_upright, lnki_ext, orig_w, orig_h, Xof_img_size.Thumb_width_img);
		Tfds.Eq(expd_html_w, img_size.Html_w(), "html_w");
		Tfds.Eq(expd_html_h, img_size.Html_h(), "html_h");
		Tfds.Eq(expd_file_is_orig, img_size.File_is_orig(), "file_is_orig");
	}
}
