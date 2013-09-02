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
import gplx.gfui.*;
public interface Gfui_box_text extends Gfui_box {
	String Text(); void Text_(String v);
	FontAdp Font(); void Font_(FontAdp v);
}
class Gfui_box_text_mok implements Gfui_box_text {
	public Gfui_box_text_mok(Gfui_box_grp grp, String id) {this.grp = grp; this.id = id;}
	public String Id() {return id;} private String id;
	public Gfui_box_grp Grp() {return grp;} Gfui_box_grp grp; 
	public String Text() {return text;} public void Text_(String v) {text = v;} private String text = "";
	public FontAdp Font() {return font;} public void Font_(FontAdp v) {font = v;} FontAdp font = FontAdp.new_("Arial", 8, FontStyleAdp_.Plain);
	public void Focus() {grp.Focus_box_(this);}
}
