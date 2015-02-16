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
package gplx.gfui; import gplx.*;
public class IptArg_ {
	public static final IptArg[] Ary_empty = new IptArg[0];
	public static final IptArg Null = null;
	public static boolean Is_null_or_none(IptArg arg) {return arg == Null || arg == IptKey_.None;}
	public static IptArg[] Ary(IptArg... v) {return v;}
	public static IptArg[] parse_ary_or_empty(String v) {
		IptArg[] rv = IptArg_.parse_ary_(v);
		int len = rv.length;
		for (int i = 0; i < len; i++)
			if (rv[i] == null) return Ary_empty;// indicates failed parse
		return rv;
	}
	public static IptArg[] parse_ary_(String raw) {
		String[] args = String_.Split(raw, "|");
		int args_len = args.length; if (args_len == 0) return Ary_empty;
		IptArg[] rv = new IptArg[args_len];
		for (int i = 0; i < args_len; i++)
			rv[i] = parse_(String_.Trim(args[i]));
		return rv;
	}
	public static IptArg parse_chain_(String raw) {return IptKeyChain.parse_(raw);}
	public static IptArg parse_or_none_(String raw) {
		try {
			return String_.Eq(raw, String_.Empty)
				? IptKey_.None
				: parse_(raw);
		}
		catch (Exception exc) {	// as an "or" proc, handle errors; note that it may accept raw values from cfg files, so invalid input is possible; DATE:2014-06-04
			Err_.Noop(exc);
			return IptKey_.None;
		}
	}
	public static IptArg parse_(String raw) {
		if		(String_.Has(raw, ","))				return IptKeyChain.parse_(raw);
		String bgn = String_.GetStrBefore(raw, ".");
		if		(String_.Eq(bgn, "wheel"))			return IptMouseWheel_.parse_(raw);
		else if (String_.Eq(bgn, "mouse"))			return IptMouseBtn_.parse_(raw);
		else if (String_.Eq(bgn, "key"))			return IptKey_.parse_(raw);
		else										return IptMacro._.parse_(raw);
	}
	// NOTE: the following two methods should theoretically be interface methods, but since they are only used by two procs, they will be handled with if/else
	@gplx.Internal protected static IptEventType EventType_default(IptArg arg) {
		Class<?> type = arg.getClass();
		if		(	type == IptKey.class
				||	type == IptKeyChain.class)	return IptEventType_.KeyDown;
		else if (type == IptMouseBtn.class)		return IptEventType_.MouseUp;	// changed from MouseDown; confirmed against Firefox, Eclipse; DATE:2014-05-16
		else if (type == IptMouseWheel.class)		return IptEventType_.MouseWheel;
		else if (type == IptMouseMove.class)		return IptEventType_.MouseMove;
		else										throw Err_.unhandled(type);
	}
	@gplx.Internal protected static boolean EventType_match(IptArg arg, IptEventType match) {
		Class<?> type = arg.getClass();
		if		(	type == IptKey.class
				||	type == IptKeyChain.class)	return match == IptEventType_.KeyDown || match == IptEventType_.KeyUp || match == IptEventType_.KeyDown;
		else if (type == IptMouseBtn.class)		return match == IptEventType_.MouseDown || match == IptEventType_.MouseUp || match == IptEventType_.MousePress;
		else if (type == IptMouseWheel.class)		return match == IptEventType_.MouseWheel;
		else if (type == IptMouseMove.class)		return match == IptEventType_.MouseMove;
		else										throw Err_.unhandled(type);
	}
}
class IptMacro {
	public void Reg(String prefix, String alias, IptArg arg) {
		if (regy == null) Init();
		OrderedHash list = (OrderedHash)regy.Fetch(prefix);
		if (list == null) {
			list = OrderedHash_.new_();
			regy.Add(prefix, list);
		}
		list.AddReplace(alias, arg);
	}
	void Init() {
		regy = OrderedHash_.new_();
		Reg("mod", "c", IptKey_.add_(IptKey_.Ctrl));
		Reg("mod", "a", IptKey_.add_(IptKey_.Alt));
		Reg("mod", "s", IptKey_.add_(IptKey_.Shift));
		Reg("mod", "ca", IptKey_.add_(IptKey_.Ctrl, IptKey_.Alt));
		Reg("mod", "cs", IptKey_.add_(IptKey_.Ctrl, IptKey_.Shift));
		Reg("mod", "as", IptKey_.add_(IptKey_.Alt, IptKey_.Shift));
		Reg("mod", "cas", IptKey_.add_(IptKey_.Ctrl, IptKey_.Alt, IptKey_.Shift));
	}
	public IptArg parse_(String raw) {
		if (regy == null) Init();
		String[] plusAry = String_.Split(raw, "+");
		String[] dotAry	= String_.Split(plusAry[0], ".");
		String bgn = dotAry[0], end = dotAry[1];
		OrderedHash list = (OrderedHash)regy.Fetch(bgn);
		if (list == null) throw parse_err(raw, "list not found").Add("list", bgn);
		IptKey rv = (IptKey)list.Fetch(end);
		if (rv == null) throw parse_err(raw, "arg not found").Add("arg", end);
		for (int i = 1; i < plusAry.length; i++) {
			rv = rv.Add((IptKey)IptKey_.parse_(plusAry[i]));
		}
		return rv;
	}
	OrderedHash regy;
	static Err parse_err(String raw, String loc) {return Err_.new_key_("gfui", "could not parse IptArg").Add("raw", raw).Add("loc", loc);}
	public static final IptMacro _ = new IptMacro(); IptMacro() {}
}
