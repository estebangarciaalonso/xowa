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
public interface Gfui_box_win extends Gfui_box_grp, Gfui_box {	
	String Text(); void Text_(String v);
}
class Gfui_box_win_mok implements Gfui_box_win {
	public Gfui_box_win_mok(String id) {this.id = id; focus_box = this;}
	public String Id() {return id;} private String id;
	public Gfui_box_grp Grp() {return null;} 
	public Gfui_box Focus_box() {return focus_box;} public void Focus_box_(Gfui_box box) {focus_box = box;} Gfui_box focus_box;
	public String Text() {return text;} public void Text_(String v) {text = v;} private String text = "";
	public void Focus() {focus_box = this;}
	public int Subs_len() {return hash.Count();} OrderedHash hash = OrderedHash_.new_();
	public Gfui_box Subs_get_at(int i) {return (Gfui_box)hash.FetchAt(i);}
	public Gfui_box Subs_get_by(String key) {return (Gfui_box)hash.Fetch(key);}
	public void Subs_add(Gfui_box sub) {hash.Add(sub.Id(), sub);}
}
