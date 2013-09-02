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
public abstract class Pf_func_base implements Pf_func {
	public byte Defn_tid() {return Xot_defn_.Tid_func;}
	public byte[] Name() {return name;} public Pf_func_base Name_(byte[] v) {name = v; name_len = v.length; return this;} private byte[] name = ByteAry_.Empty; int name_len = 0;
	@gplx.Virtual public boolean Func_require_colon_arg() {return false;}
	public boolean Defn_require_colon_arg() {return this.Func_require_colon_arg();}
	public int CacheSize() {return 1024;}	// arbitrary size
	public abstract int Id();
	public void Rls() {name = null; argx_dat = null;}
	public abstract Pf_func New(int id, byte[] name);
	public Xot_defn Clone(int id, byte[] name) {return New(id, name);}
	public abstract void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr);
	public byte[] Argx_dat() {return argx_dat;} public void Argx_dat_(byte[] v) {argx_dat = v;} private byte[] argx_dat = ByteAry_.Empty;
	public byte[] Eval_argx(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self) {
		if (argx_dat == ByteAry_.Empty) {
			Arg_itm_tkn name_val_tkn = self.Name_tkn().Val_tkn();
			int subs_len = name_val_tkn.Subs_len();
			if (subs_len > 0) {
				ByteAryBfr tmp = ByteAryBfr.new_();
				for (int i = 0; i < subs_len; i++)
					name_val_tkn.Subs_get(i).Tmpl_evaluate(ctx, src, caller, tmp);
				argx_dat = tmp.XtoAryAndClearAndTrim();
			}
		}
		return argx_dat;
	}		
	public byte[] Eval_argx_or_null(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self) {
		if (argx_dat == ByteAry_.Empty) {
			Arg_nde_tkn name_tkn = self.Name_tkn();
			Arg_itm_tkn name_val_tkn = name_tkn.Val_tkn();
			int subs_len = name_val_tkn.Subs_len();
			if (subs_len == 0) {
				int name_bgn = name_tkn.Src_bgn() + name_len;	// start looking after {{ + name_len
				int colon_pos = ByteAry_.FindFwd(src, Byte_ascii.Colon, name_bgn, self.Src_end());	// look for :
				return colon_pos == ByteAry_.NotFound ? null : ByteAry_.Empty;	// if ":" found, return "", else return null (callers should know what to do with this)
			}
			else {
				ByteAryBfr tmp = ByteAryBfr.new_();
				for (int i = 0; i < subs_len; i++)
					name_val_tkn.Subs_get(i).Tmpl_evaluate(ctx, src, caller, tmp);
				argx_dat = tmp.XtoAryAndClearAndTrim();
			}
		}
		return argx_dat;
	}
}
