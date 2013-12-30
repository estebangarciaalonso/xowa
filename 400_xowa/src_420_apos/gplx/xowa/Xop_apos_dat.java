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
public class Xop_apos_dat {
	public int State() {return state;} public void State_clear() {state = Xop_apos_tkn_.State_nil;} private int state = Xop_apos_tkn_.State_nil;
	public int Typ() {return typ;} private int typ;
	public int Cmd() {return cmd;} private int cmd;
	public int LitApos() {return litApos;} private int litApos;
	public int DualCmd() {return dualCmd;} private int dualCmd;
	public void Ident(Xop_ctx ctx, byte[] src, int aposLen, int cur_pos) {
		typ = cmd = litApos = dualCmd = 0;
		switch (aposLen) {
			case Xop_apos_tkn_.CmdLen_ital:
			case Xop_apos_tkn_.CmdLen_bold:
			case Xop_apos_tkn_.CmdLen_dual:
				Ident_props(aposLen); break;
			case Xop_apos_tkn_.CmdLen_aposBold:
				litApos = 1;
				Ident_props(Xop_apos_tkn_.CmdLen_bold); break;
			default:
				litApos = aposLen - Xop_apos_tkn_.CmdLen_dual;
				Ident_props(Xop_apos_tkn_.CmdLen_dual);
				if (litApos > 1)
					ctx.Msg_log().Add_itm_none(Xop_apos_log.Multiple_apos, src, cur_pos - aposLen, cur_pos);
				break;
		}
	}
	private void Ident_props(int aposLen) {
		typ = aposLen;
		switch (aposLen) {
			case Xop_apos_tkn_.CmdLen_ital:	{
				switch (state) {
					case Xop_apos_tkn_.State_i:		cmd = Xop_apos_tkn_.Cmd_i_end;			state = Xop_apos_tkn_.State_nil;	break;
					case Xop_apos_tkn_.State_bi:	cmd = Xop_apos_tkn_.Cmd_i_end;			state = Xop_apos_tkn_.State_b;		break;
					case Xop_apos_tkn_.State_ib:	cmd = Xop_apos_tkn_.Cmd_bi_end__b_bgn;	state = Xop_apos_tkn_.State_b;		break;
					case Xop_apos_tkn_.State_dual:	cmd = Xop_apos_tkn_.Cmd_i_end;			state = Xop_apos_tkn_.State_b;		dualCmd = Xop_apos_tkn_.Cmd_bi_bgn; break;
					case Xop_apos_tkn_.State_b:		cmd = Xop_apos_tkn_.Cmd_i_bgn;			state = Xop_apos_tkn_.State_bi;		break;
					case Xop_apos_tkn_.State_nil:	cmd = Xop_apos_tkn_.Cmd_i_bgn;			state = Xop_apos_tkn_.State_i;		break;
					default:						throw Err_.unhandled(state);
				}
				break;
			}
			case Xop_apos_tkn_.CmdLen_bold:	{
				switch (state) {
					case Xop_apos_tkn_.State_b:		cmd = Xop_apos_tkn_.Cmd_b_end;			state = Xop_apos_tkn_.State_nil;	break;
					case Xop_apos_tkn_.State_bi:	cmd = Xop_apos_tkn_.Cmd_ib_end__i_bgn;	state = Xop_apos_tkn_.State_i;		break;
					case Xop_apos_tkn_.State_ib:	cmd = Xop_apos_tkn_.Cmd_b_end;			state = Xop_apos_tkn_.State_i;		break;
					case Xop_apos_tkn_.State_dual:	cmd = Xop_apos_tkn_.Cmd_b_end;			state = Xop_apos_tkn_.State_i;		break; // NOTE: dualCmd = Cmd_ib_bgn is implied
					case Xop_apos_tkn_.State_i:		cmd = Xop_apos_tkn_.Cmd_b_bgn;			state = Xop_apos_tkn_.State_ib;		break;
					case Xop_apos_tkn_.State_nil:	cmd = Xop_apos_tkn_.Cmd_b_bgn;			state = Xop_apos_tkn_.State_b;		break;
					default:						throw Err_.unhandled(state);
				}
				break;
			}
			case Xop_apos_tkn_.CmdLen_dual:	{
				switch (state) {
					case Xop_apos_tkn_.State_b:		cmd = Xop_apos_tkn_.Cmd_b_end__i_bgn;	state = Xop_apos_tkn_.State_i;		break;
					case Xop_apos_tkn_.State_i:		cmd = Xop_apos_tkn_.Cmd_i_end__b_bgn;	state = Xop_apos_tkn_.State_b;		break;
					case Xop_apos_tkn_.State_bi:	cmd = Xop_apos_tkn_.Cmd_ib_end;			state = Xop_apos_tkn_.State_nil;	break;
					case Xop_apos_tkn_.State_ib:	cmd = Xop_apos_tkn_.Cmd_bi_end;			state = Xop_apos_tkn_.State_nil;	break;
					case Xop_apos_tkn_.State_dual:	cmd = Xop_apos_tkn_.Cmd_bi_end;			state = Xop_apos_tkn_.State_nil;	break; // NOTE: dualCmd = Cmd_ib_bgn is implied
					case Xop_apos_tkn_.State_nil:	cmd = Xop_apos_tkn_.Cmd_ib_bgn;			state = Xop_apos_tkn_.State_dual;	break;
					default:						throw Err_.unhandled(state);
				}
				break;
			}
			default: throw Err_.unhandled(aposLen);
		}
	}
}
