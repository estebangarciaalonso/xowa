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
interface ExprTkn {
	int TypeId();
	byte[] Val_ary();
	String Val_str();
}
class ExprTkn_ {
	public static final int TypeId_operator = 1, TypeId_paren_lhs = 5, TypeId_paren_rhs = 6, TypeId_space = 7, TypeId_number = 8;
}
interface PrcTkn extends ExprTkn {
	int ArgCount();
	int Precedence();
	PrcTkn GetAlt();
	boolean Calc(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack);
}
class WsTkn implements ExprTkn {
	public int TypeId() {return ExprTkn_.TypeId_space;}
	public byte[] Val_ary() {return val_ary;} private byte[] val_ary;
	public String Val_str() {return val_str;} private String val_str;
	public WsTkn(byte b) {this.val_ary = new byte[] {b}; this.val_str = Char_.XtoStr(Char_.XbyInt(b));}
}
class ParenBgnTkn implements ExprTkn, PrcTkn {
	public int TypeId() {return ExprTkn_.TypeId_paren_lhs;}
	public byte[] Val_ary() {return val_ary;} private byte[] val_ary = ByteAry_.new_utf8_(val_str);
	public String Val_str() {return val_str;} static final String val_str = "(";
	public int ArgCount() {return 0;}
	public int Precedence() {return -1;}
	public PrcTkn GetAlt() {return this;}
	public boolean Calc(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {throw Err_.not_implemented_();}
	public static ParenBgnTkn _ = new ParenBgnTkn(); ParenBgnTkn() {}
}
class ParenEndTkn implements ExprTkn {
	public int TypeId() {return ExprTkn_.TypeId_paren_rhs;}
	public byte[] Val_ary() {return val_ary;} private byte[] val_ary = ByteAry_.new_utf8_(val_str);
	public String Val_str() {return val_str;} static final String val_str = ")";
	public static ParenEndTkn _ = new ParenEndTkn(); ParenEndTkn() {}
}
class NumTkn implements ExprTkn {
	public int TypeId() {return ExprTkn_.TypeId_number;}		
	public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	public String Val_str()	{return String_.new_utf8_(val_ary);}
	public NumTkn(int val_int) {
		this.val_int = val_int;
		this.val_ary = new byte[] {Byte_.int_(val_int + Byte_ascii.Num_0)};
	}	int val_int;
}
class DotTkn implements ExprTkn {
	public int TypeId() {return ExprTkn_.TypeId_number;}		
	public byte[] Val_ary()	{return Val_Ary;} static final byte[] Val_Ary = new byte[] {Byte_ascii.Dot};
	public String Val_str()	{return String_.new_utf8_(Val_Ary);}
	public DotTkn() {}
}
abstract class PrcTkn_base implements PrcTkn {
	public int TypeId() {return ExprTkn_.TypeId_operator;}
	public abstract int Precedence();
	public abstract int ArgCount();
	public abstract byte[] Val_ary();
	public String Val_str()	{return String_.new_utf8_(Val_ary());}
	@gplx.Virtual public PrcTkn GetAlt() {return this;}
//		public virtual boolean Type_expr() {return false;}
	public boolean Calc(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		if (val_stack.Len() < this.ArgCount()) {shunter.Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_missing_operand, Val_ary()); return false;}
		return File_hook(ctx, shunter, val_stack);
	}
	public abstract boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack);
}
class ValStack {
	public void Clear() {stack_len = 0;}
	public int Len() {return stack_len;}
	public DecimalAdp Pop() {
		int stack_len_new = stack_len - 1;
		DecimalAdp rv = stack[stack_len_new];
		stack_len = stack_len_new;
		return rv;
	}
	public void Push(DecimalAdp v) {
		int stack_len_new = stack_len + 1;
		if (stack_len_new > stack_max) {
			stack_max = stack_len_new * 2;
			stack = (DecimalAdp[])Array_.Resize(stack, stack_max);
		}
		stack[stack_len] = v;
		stack_len = stack_len_new;
	}
	DecimalAdp[] stack = new DecimalAdp[0]; int stack_len = 0, stack_max = 0;
}
class PrcStack {
	public void Clear() {stack_len = 0;}
	public int Len() {return stack_len;}
	public PrcTkn GetLast() {return stack_len == 0 ? null : stack[stack_len - 1];}
	public PrcTkn Pop() {
		int stack_len_new = stack_len - 1;
		PrcTkn rv = stack[stack_len_new];
		stack_len = stack_len_new;
		return rv;
	}
	public void Push(PrcTkn v) {
		int stack_len_new = stack_len + 1;
		if (stack_len_new > stack_max) {
			stack_max = stack_len_new * 2;
			stack = (PrcTkn[])Array_.Resize(stack, stack_max);
		}
		stack[stack_len] = v;
		stack_len = stack_len_new;
	}
	PrcTkn[] stack = new PrcTkn[0]; int stack_len = 0, stack_max = 0;
}
class PrcTkn_plus extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 6;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public PrcTkn GetAlt() {return PrcTkn_plus_positive._;}
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Op_add(rhs));
		return true;
	}
	public PrcTkn_plus(String v) {val_ary = ByteAry_.new_utf8_(v);}
	public static final PrcTkn_plus _ = new PrcTkn_plus(); PrcTkn_plus() {}
}
class PrcTkn_plus_positive extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 10;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {return true;}// effectively a noop
	PrcTkn_plus_positive(String v) {val_ary = ByteAry_.new_utf8_(v);}
	public static final PrcTkn_plus_positive _ = new PrcTkn_plus_positive("+");
}
class PrcTkn_minus extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 6;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public PrcTkn GetAlt() {return PrcTkn_minus_negative._;}
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Op_subtract(rhs));
		return true;
	}
	public PrcTkn_minus(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_minus_negative extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 10;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(val.Op_mult(DecimalAdp_.Neg1));
		return true;
	}
	public PrcTkn_minus_negative(String v) {val_ary = ByteAry_.new_utf8_(v);}
	public static final PrcTkn_minus_negative _ = new PrcTkn_minus_negative("-");
}
class PrcTkn_divide extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 7;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		if (rhs.Eq(0)) {
			shunter.Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_division_by_zero);
			return false;
		}
		val_stack.Push(lhs.Op_divide(rhs));
		return true;
	}
	public PrcTkn_divide(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_times extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 7;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Op_mult(rhs));
		return true;
	}
	public PrcTkn_times(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_pow extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 8;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		int rhs_int = rhs.XtoInt();	
		if ((double)rhs_int == rhs.XtoDouble())	// exponent is integer; use decimal pow which does less casts to double
			val_stack.Push(lhs.Op_pow(rhs_int));
		else {
			double rslt = Math_.Pow(lhs.XtoDouble(), rhs.XtoDouble());
			if (Double_.IsNaN(rslt)) {
				shunter.Rslt_set(Double_.NaN_bry);
				return false;
			}
			else
				val_stack.Push(DecimalAdp_.double_thru_str_(rslt));
		}
		return true;
	}
	public PrcTkn_pow(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_e_op extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 9;}	// NOTE: needs to be < than - sign
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public PrcTkn GetAlt() {return PrcTkn_e_const._;}
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		int rhs_int = rhs.XtoInt();
		double rhs_double = rhs.XtoDouble();
		if ((double)rhs_int == rhs_double)	// exponent is integer; use pow_10 which does less casts to double
			val_stack.Push(lhs.Op_mult(DecimalAdp_.pow_10_(rhs_int)));
		else
			val_stack.Push(lhs.Op_mult(DecimalAdp_.double_thru_str_(Math_.Pow(10d, rhs_double))));
		return true;
	}
	public PrcTkn_e_op(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_mod extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 7;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		// must convert to int else issues with {{#expr:0.00999999mod10}} and {{USCensusPop|1960=763956|1970=756510}}; REF: http://php.net/manual/en/language.operators.arithmetic.php: "Operands of modulus are converted to integers (by stripping the decimal part) before processing"
		// must convert to long else issues with (39052000900/1) mod 100 which should be 0, not 47; JAVA does not fail int conversion, and instead converts to Int_.MaxValue; EX: de.w:Quijano_(Pi�lagos)
		long rhs = ((DecimalAdp)val_stack.Pop()).XtoLong();
		long lhs = ((DecimalAdp)val_stack.Pop()).XtoLong();
		if (rhs == 0) {
			shunter.Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_division_by_zero);
			return false;
		}
		val_stack.Push(DecimalAdp_.long_(lhs % rhs));
		return true;
	}
	public PrcTkn_mod(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_eq extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 4;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Eq(rhs) ? DecimalAdp_.One : DecimalAdp_.Zero);
		return true;
	}
	public PrcTkn_eq(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_neq extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 4;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Eq(rhs) ? DecimalAdp_.Zero : DecimalAdp_.One);
		return true;
	}
	public PrcTkn_neq(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_gt extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 4;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Comp_gt(rhs) ? DecimalAdp_.One : DecimalAdp_.Zero);
		return true;
	}
	public PrcTkn_gt(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_lt extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 4;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Comp_lt(rhs) ? DecimalAdp_.One : DecimalAdp_.Zero);
		return true;
	}
	public PrcTkn_lt(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_gte extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 4;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Comp_gte(rhs) ? DecimalAdp_.One : DecimalAdp_.Zero);
		return true;
	}
	public PrcTkn_gte(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_lte extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 4;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(lhs.Comp_lte(rhs) ? DecimalAdp_.One : DecimalAdp_.Zero);
		return true;
	}
	public PrcTkn_lte(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_and extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 3;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(!lhs.Eq(0) && !rhs.Eq(0) ? DecimalAdp_.One : DecimalAdp_.Zero);
		return true;
	}
	public PrcTkn_and(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_or extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 2;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		val_stack.Push(!lhs.Eq(0) || !rhs.Eq(0) ? DecimalAdp_.One : DecimalAdp_.Zero);
		return true;
	}
	public PrcTkn_or(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_not extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(val.Eq(0) ? DecimalAdp_.One : DecimalAdp_.Zero);
		return true;
	}
	public PrcTkn_not(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_e_const extends PrcTkn_base {
	@Override public int ArgCount()		{return 0;}
	@Override public int Precedence()	{return 0;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		val_stack.Push(DecimalAdp_.Const_e);
		return true;
	}
	public PrcTkn_e_const(String v) {val_ary = ByteAry_.new_utf8_(v);}
	public static final PrcTkn_e_const _ = new PrcTkn_e_const("e");
}
class PrcTkn_pi extends PrcTkn_base {
	@Override public int ArgCount()		{return 0;}
	@Override public int Precedence()	{return 0;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		val_stack.Push(DecimalAdp_.Const_pi);
		return true;
	}
	public PrcTkn_pi(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_ceil extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(DecimalAdp_.double_(Math_.Ceil(val.XtoDouble())));
		return true;
	}
	public PrcTkn_ceil(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_trunc extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(DecimalAdp_.double_(Math_.Trunc(val.XtoDouble())));
		return true;
	}
	public PrcTkn_trunc(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_floor extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(DecimalAdp_.double_(Math_.Floor(val.XtoDouble())));
		return true;
	}
	public PrcTkn_floor(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_abs extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(val.Op_abs());
		return true;
	}
	public PrcTkn_abs(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_exp extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(DecimalAdp_.double_(Math_.Exp(val.XtoDouble())));
		return true;
	}
	public PrcTkn_exp(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_ln extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		if (val.Comp_lte(0)) {shunter.Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_invalid_argument_ln); return false;}		
		val_stack.Push(DecimalAdp_.double_(Math_.Log(val.XtoDouble())));
		return true;
	}
	public PrcTkn_ln(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_sin extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(DecimalAdp_.double_(Math_.Sin(val.XtoDouble())));
		return true;
	}
	public PrcTkn_sin(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_cos extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(DecimalAdp_.double_(Math_.Cos(val.XtoDouble())));
		return true;
	}
	public PrcTkn_cos(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_tan extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(DecimalAdp_.double_(Math_.Tan(val.XtoDouble())));
		return true;
	}
	public PrcTkn_tan(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_asin extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		if (val.Comp_lt(-1) || val.Comp_gt(1)) {shunter.Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_invalid_argument, this.Val_ary()); return false;}
		val_stack.Push(DecimalAdp_.double_(Math_.Asin(val.XtoDouble())));
		return true;
	}
	public PrcTkn_asin(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_acos extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		if (val.Comp_lt(-1) || val.Comp_gt(1)) {shunter.Err_set(ctx, Xol_msg_itm_.Id_pfunc_expr_invalid_argument, this.Val_ary()); return false;}
		val_stack.Push(DecimalAdp_.double_(Math_.Acos(val.XtoDouble())));
		return true;
	}
	public PrcTkn_acos(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_atan extends PrcTkn_base {
	@Override public int ArgCount()		{return 1;}
	@Override public int Precedence()	{return 9;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp val = val_stack.Pop();
		val_stack.Push(DecimalAdp_.double_(Math_.Atan(val.XtoDouble())));
		return true;
	}
	public PrcTkn_atan(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
class PrcTkn_round extends PrcTkn_base {
	@Override public int ArgCount()		{return 2;}
	@Override public int Precedence()	{return 5;}
	@Override public byte[] Val_ary()	{return val_ary;} private byte[] val_ary;
	@Override public boolean File_hook(Xop_ctx ctx, Pf_xtn_expr_shunter shunter, ValStack val_stack) {
		DecimalAdp rhs = val_stack.Pop();
		DecimalAdp lhs = val_stack.Pop();
		if (rhs.Comp_gt(16)) {
//				ConsoleAdp._.WriteLine(rhs +  " round " + Double_.XtoStr(rhs));
			rhs = DecimalAdp_.int_(16);
		}
		else if (rhs.Comp_lt(-16)) {
//				ConsoleAdp._.WriteLine(rhs +  " round" + Double_.XtoStr(rhs));
			rhs = DecimalAdp_.int_(-16);
		}
		val_stack.Push(lhs.Op_round(rhs.XtoInt()));
		return true;
	}
	public PrcTkn_round(String v) {val_ary = ByteAry_.new_utf8_(v);}
}
