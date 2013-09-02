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
import org.junit.*;
public class Xof_xfer_itm_tst {
	Xof_xfer_queue_html_fxt fxt = new Xof_xfer_queue_html_fxt();
	@Before public void init() {fxt.Clear(true);}
	@Test  public void Height_should_precede_width() {// PURPOSE: height should precede width; EX: <gallery>David_Self_Portrait.jpg</gallery>; c:Jacques-Louis David
		fxt.save_(fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv", "A.png|z||2?0,0|1?86,121;1?120,168"));
		fxt	.Lnki_thumb_("A.png", 120, 120)
		.Html_src_("file:///mem/trg/en.wikipedia.org/fit/7/0/A.png/86px.png")	
		.Html_size_(85, 120)
		.tst();
	}
}
