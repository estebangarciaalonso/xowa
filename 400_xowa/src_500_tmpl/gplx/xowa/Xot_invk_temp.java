/*
XOWA: the extensible offline wiki application
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
public class Xot_invk_temp implements Xot_invk {
	public Xot_invk_temp(boolean root_frame) {this.root_frame = root_frame;}
	public boolean Root_frame() {return root_frame;} private boolean root_frame;
	public Arg_nde_tkn Name_tkn() {return name_tkn;} Arg_nde_tkn name_tkn;
	public int Src_bgn() {return src_bgn;} private int src_bgn;
	public int Src_end() {return src_end;} private int src_end;
	public int Args_len() {return list.Count();}
	public Arg_nde_tkn Args_eval_by_idx(byte[] src, int idx) { // NOTE: idx is base0
		int cur = 0, list_len = list.Count(); 
		for (int i = 0; i < list_len; i++) {	// iterate over list to find nth *non-keyd* arg; SEE:NOTE_1
			Arg_nde_tkn nde = (Arg_nde_tkn)list.FetchAt(i);
			if (nde.KeyTkn_exists()) {
				int key_int = ByteAry_.XtoIntOr(nde.Key_tkn().Dat_ary(), -1);
				if (key_int == -1)
					continue;
				else {	// key is numeric
					if (key_int - 1 == idx) {
						return nde;						
					}
					else {
						continue;
					}
				}
			}
			if (cur++ == idx) return nde;
		}
		return Args_get_by_key(src, ByteAry_.XtoStrBytesByInt(idx + 1, 1));
	}
	public Arg_nde_tkn Args_get_by_idx(int i) {return (Arg_nde_tkn)list.FetchAt(i);}
	public Arg_nde_tkn Args_get_by_key(byte[] src, byte[] key) {
		Object o = hash.Fetch(byteAryRef.Val_(key));
		return o == null ? null : (Arg_nde_tkn)o;
	}	private ByteAryRef byteAryRef = ByteAryRef.null_();
	public void Args_add(Arg_nde_tkn arg) {list.Add(arg);} ListAdp list = ListAdp_.new_();
	public void Args_addByKey(byte[] key, Arg_nde_tkn arg) {
		ByteAryRef key_ref = ByteAryRef.new_(key);
		hash.Del(key_ref);
		hash.Add(key_ref, arg);
	}	HashAdp hash = HashAdp_.new_();
	public byte[] Src() {return src;} private byte[] src;
	public Xot_invk_temp Src_(byte[] src) {this.src = src; return this;}
	public Xot_invk_temp(byte[] src, Arg_nde_tkn name_tkn, int src_bgn, int src_end) {
		this.src = src;
		this.name_tkn = name_tkn; this.src_bgn = src_bgn; this.src_end = src_end;
	}
	public static final Xot_invk_temp PageIsCaller = new Xot_invk_temp(true);	// SEE NOTE_2
	Xot_invk_temp() {}
}
/*
NOTE_1:
The nth arg refers to the nth non-keyd arg;
EX:
defn is {{{1}}}
invk is {{test|key1=a|b}}
{{{1}}} is b b/c b is the 1st non-keyd arg (even though it is the 2nd arg)
*/
/*
NOTE_2:
Consider a Page with the following Wiki text in the Preview box
WIKI: "a {{mwo_concat|{{{1}}}|b}} c"
TEXT: "a {{{1}}}b c"

Note that in order to resolve mwo_concat we need to pass in an Xot_invk
This Xot_invk is the "PageIsCaller" ref
Note this has no parameters and is always empty
*/
