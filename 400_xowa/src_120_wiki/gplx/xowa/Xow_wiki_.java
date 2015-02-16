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
public class Xow_wiki_ {
	public static final String
	  Domain_commons_str = "commons.wikimedia.org"
	, Domain_enwiki_str  = "en.wikipedia.org"
	;
	public static final byte[] 
	  Domain_commons_bry = Bry_.new_ascii_(Domain_commons_str)
	, Domain_en_wiki_bry  = Bry_.new_ascii_(Domain_enwiki_str)
	;
}
