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
package gplx.xowa.gui.history; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
import org.junit.*;
public class Xog_history_stack_tst {		
	@Before public void init() {fxt.Clear();} private Xog_history_stack_fxt fxt = new Xog_history_stack_fxt();
	@Test  public void Init()				{fxt.Test_cur(null);}
	@Test  public void Add_1()				{fxt.Exec_add("A").Test_cur("A").Test_len(1).Test_pos(0);}
	@Test  public void Add_same()			{fxt.Exec_add("A", "A").Test_cur("A").Test_len(1).Test_pos(0);}
	@Test  public void Add_3()				{fxt.Exec_add("A", "B", "C").Test_cur("C").Test_len(3).Test_pos(2);}
	@Test  public void Add_3_bwd()			{fxt.Exec_add("A", "B", "C").Exec_go_bwd().Test_cur("B").Test_pos(1);}
	@Test  public void Add_3_bwd_fwd()		{fxt.Exec_add("A", "B", "C").Exec_go_bwd().Exec_go_fwd().Test_cur("C").Test_pos(2);}
	@Test  public void Add_3_bwd_add()		{fxt.Exec_add("A", "B", "C").Exec_go_bwd().Exec_add("D").Test_len(3).Test_cur("D").Test_pos(2);}
	@Test  public void Add_3_bwd_bwd_add()	{fxt.Exec_add("A", "B", "C").Exec_go_bwd().Exec_go_bwd().Exec_add("D").Test_len(2).Test_cur("D").Test_pos(1);}
	@Test  public void Add_dif_ns()			{fxt.Exec_add("A", "Help:A").Test_cur("Help:A");}	// PURPOSE.fix: page_stack was only differtiating by Page_db, not Full; EX: Unicode -> Category:Unicode
}
class Xog_history_stack_fxt {
	public Xog_history_stack_fxt Clear() {
		stack.Clear();
		if (app == null) {
			app = Xoa_app_fxt.app_();
			wiki = Xoa_app_fxt.wiki_tst_(app);
		}
		return this;
	}	private Xoa_app app; private Xow_wiki wiki; private Xog_history_stack stack = new Xog_history_stack();
	public Xog_history_stack_fxt Test_cur(String expd) {
		Xog_history_itm page = stack.Cur_itm();
		String actl = page == null ? null : String_.new_utf8_(page.Page_key());
		Tfds.Eq(expd, actl, "cur");
		return this;
	}
	public Xog_history_stack_fxt Exec_go_bwd() {stack.Go_bwd(); return this;}
	public Xog_history_stack_fxt Exec_go_fwd() {stack.Go_fwd(); return this;}
	public Xog_history_stack_fxt Exec_add(String... ary) {
		for (String s : ary) {
			byte[] bry = ByteAry_.new_utf8_(s);
			Xoa_url url = new Xoa_url();
			url.Wiki_bry_(ByteAry_.new_utf8_("mock")).Page_bry_(bry);
			Xoa_page page = new Xoa_page(wiki, Xoa_ttl.parse_(wiki, bry)).Url_(url);
			stack.Add(page);
		}
		return this;
	}
	public Xog_history_stack_fxt Test_pos(int expd) {Tfds.Eq(expd, stack.Stack_pos(), "pos"); return this;}
	public Xog_history_stack_fxt Test_len(int expd) {Tfds.Eq(expd, stack.Count(), "len"); return this;}
}
