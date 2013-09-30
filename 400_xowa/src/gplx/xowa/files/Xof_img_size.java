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
import gplx.gfui.*;
public class Xof_img_size {
	public int Html_w() {return html_w;} private int html_w;
	public int Html_h() {return html_h;} private int html_h;
	public int File_w() {return file_w;} private int file_w;
	public boolean File_is_orig() {return file_is_orig;} private boolean file_is_orig;

	public void Html_size_calc(byte exec_tid, int lnki_w, int lnki_h, byte lnki_type, double lnki_upright, int lnki_ext, int orig_w, int orig_h, int thumb_default_w) {
		html_w = lnki_w; html_h = lnki_h;						// set html vals to lnki vals
		file_is_orig = false;
		if (html_w == Null && html_h == Null) {					// no size set; NOTE: do not default to thumb if only height is set; EX: x900px should have w=0 h=900
			if (lnki_type == Xop_lnki_type.Id_thumb)
				html_w = thumb_default_w;
			else
				html_w = orig_w;
		}
		if (lnki_upright > 0)									// upright set; recalc html_w
			html_w = Upright_calc(lnki_upright, html_w);
		if (orig_w == Null) return;								// no orig_w; just use html_w and html_h (html_h will likely be -1 and wrong)

		if (html_w == Xof_img_size.Null) html_w = orig_w;		// html_w missing >>> use orig_w; REF.MW:Linker.php|makeImageLink2|$hp['width'] = $file->getWidth( $page );				
		if (html_h != Xof_img_size.Null) {						// html_h exists; REF.MW:ImageHandler.php|normaliseParams|if ( isset( $params['height'] ) && $params['height'] != -1 ) {
			if (html_w * orig_h > html_h * orig_w)				// html ratio > orig ratio; recalc html_w; SEE:NOTE_2
				html_w = Calc_w(orig_w, orig_h, html_h);
		}
		html_h = Scale_h(orig_w, orig_h, html_w);				// calc html_h
		boolean limit_size = Calc_limit_size(exec_tid, lnki_type, lnki_ext); 
		if (html_w > orig_w && limit_size) {					// do not allow html_w > orig_w; REF.MW:Generic.php|normaliseParams
			html_w = orig_w;
			html_h = orig_h;
		}
		file_w = html_w;										// set file_w to html_w
		if (	lnki_type != Xop_lnki_type.Id_thumb
			&&	Xof_ext_.Orig_file_is_img(lnki_ext)
			&&	html_w > orig_w) {
			file_is_orig = true;
//				file_w = File_w__is_orig;
		}
	}
	private static boolean Calc_limit_size(byte exec_tid, int lnki_type, int lnki_ext) {
		if (lnki_type != Xop_lnki_type.Id_thumb) return false;  // only limit to size for thumb; EX:[[File:A.png|thumb|999x999px]] does not get limited but [[File:A.png|999x999px]] does  
		if (lnki_ext == Xof_ext_.Id_svg)						// if svg...
			return exec_tid == Xof_exec_tid.Tid_wiki_file;		// ... only limit to size if [[File]] page
		else													// not svg and thumb; always limit to size
			return true;
	}
	public static int Calc_w(int file_w, int file_h, int lnki_h) {		// REF.MW:media/MediaHandler.php|fitBoxWidth
		double ideal_w = (double)file_w * (double)lnki_h / (double)file_h;
		double ideal_w_ceil = Math_.Ceil(ideal_w);
		return Math_.Round(ideal_w_ceil * file_h / file_w, 0) > lnki_h
			? (int)Math_.Floor(ideal_w)
			: (int)ideal_w_ceil;
	}
	public static int Scale_h(int file_w, int file_h, int lnki_w) {
		return file_w == 0												// REF.MW:File.php|scaleHeight
			? 0
			: (int)Math_.Round(((double)lnki_w * file_h) / file_w, 0);	// NOTE: (double) needed else result will be int and fraction will be truncated
	}
	public static int Upright_calc(double upright, int width) {		
		if 		(upright == Upright_null)		return width;	// upright is null; return width
		else if (upright == Upright_default)	upright = .75f;	// upright is default; set val to .75; EX: [[File:A.png|upright]]
		int rv = (int)(width * upright);  
		return Round_10p2(rv);
	}
	private static int Round_10p2(int v) {
		int mod = v % 10;
		if (mod > 4) 	v += 10 - mod;
		else			v -= mod;
		return v;
	}
	public static final int Null = -1;
	public static final int Thumb_width_img = 220, Thumb_width_ogv = 220;
	public static final double Upright_null = -1, Upright_default = 1;
	public static final int Size_null_deprecated = -1, Size_null = 0;	// Size_null = 0, b/c either imageMagick / inkscape fails when -1 is passed
	public static final int File_w__same_as_html = -1, File_w__is_orig = -2;
}
/*
NOTE_1:proc source/layout
MW calls the falling procs
. Linker.php|makeImageLink2
. Linker.php|makeThumbLink2
. File.php|transform
. Bitmap.php|normaliseParams
. media/MediaHandler.php|fitBoxWidth
. File.php|scaleHeight
Note that this proc is a selective culling of the w,h setting code in the above (the procs do a lot of other checks/building)
also, MW's if branching can be combined. for now, emulating MW and not enforcing matching if/else 

NOTE_2: lnki_ratio > orig_ratio
REF.MW:media/MediaHandler.php|fitBoxWidth
COMMENT:"Height is the relative smaller dimension, so scale width accordingly"

consider file of 200,100 (2:1)
EX_1: view is 120,40 (3:1)
- dimensions are either (a) 120,80 or (b) 80,40
- use (b) 80,40

EX_2: view is 120,80 (1.5:1)
- dimensions are either (a) 120,60 or (b) 160,80
- use (a) 120,60
*/