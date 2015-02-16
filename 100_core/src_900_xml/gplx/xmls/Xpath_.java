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
package gplx.xmls; import gplx.*;
import gplx.core.primitives.*;
public class Xpath_ {
	public static XmlNdeList SelectAll(XmlNde owner, String xpath) {return Select(owner, xpath, Xpath_Args.all_());}
	public static XmlNde SelectFirst(XmlNde owner, String xpath) {
		XmlNdeList rv = Select(owner, xpath, Xpath_Args.first_());
		return rv.Count() == 0 ? null : rv.FetchAt(0);	// selects first
	}
	public static XmlNdeList SelectElements(XmlNde owner) {
		XmlNdeList subNdes = owner.SubNdes(); int count = subNdes.Count();
		XmlNdeList_cls_list list = new XmlNdeList_cls_list(count);
		for (int i = 0; i < count; i++) {
			XmlNde sub = subNdes.FetchAt(i);
			if (sub.NdeType_element()) 
				list.Add(sub);
		}
		return list;
	}
	static XmlNdeList Select(XmlNde owner, String xpath, Xpath_Args args) {
		XmlNdeList_cls_list rv = new XmlNdeList_cls_list(ListAdp_.Capacity_initial);
		String[] parts = String_.Split(xpath, "/");
		TraverseSubs(owner, parts, 0, rv, args);
		return rv;
	}
	static void TraverseSubs(XmlNde owner, String[] parts, int depth, XmlNdeList_cls_list results, Xpath_Args args) {
		int partsLen = Array_.Len(parts);
		if (depth == partsLen) return;
		String name = parts[depth];
		XmlNdeList subNdes = owner.SubNdes(); int count = subNdes.Count();
		for (int i = 0; i < count; i++) {
			XmlNde sub = subNdes.FetchAt(i);
			if (args.Cancel) return;
			if (!String_.Eq(name, sub.Name())) continue;
			if (depth == partsLen - 1) {
				results.Add(sub);
				if (args.SelectFirst) args.Cancel = true;
			}
			else
				TraverseSubs(sub, parts, depth + 1, results, args);
		}
	}
	public static final String InnetTextKey = "&innerText";
	public static KeyValHash ExtractKeyVals(String xml, Int_obj_ref posRef, String nodeName) {
		int pos = posRef.Val();
		Err xmlErr = Err_.new_("error parsing xml").Add("xml", xml).Add("pos", pos);
		String headBgnFind = "<" + nodeName + " "; int headBgnFindLen = String_.Len(headBgnFind);
		int headBgn = String_.FindFwd(xml, headBgnFind, pos);						if (headBgn == String_.Find_none) return null;
		int headEnd = String_.FindFwd(xml, ">", headBgn + headBgnFindLen);			if (headEnd == String_.Find_none) throw xmlErr;
		String atrXml = String_.Mid(xml, headBgn, headEnd);
		KeyValHash rv = ExtractNodeVals(atrXml, xmlErr);
		boolean noInnerText = String_.CharAt(xml, headEnd - 1) == '/';				// if />, then no inner text
		if (!noInnerText) {
			int tail = String_.FindFwd(xml, "</" + nodeName + ">", headBgn);		if (tail == String_.Find_none) throw xmlErr.Hdr_("could not find tailPos").Add("headBgn", headBgn);
			String innerText = String_.Mid(xml, headEnd + 1, tail);
			rv.Add(InnetTextKey, innerText);
		}
		posRef.Val_(headEnd);
		return rv;
	}
	static KeyValHash ExtractNodeVals(String xml, Err xmlErr) {
		KeyValHash rv = KeyValHash.new_();
		int pos = 0;
		while (true) {
			int eqPos = String_.FindFwd(xml, "=", pos);		if (eqPos == String_.Find_none) break;
			int q0Pos = String_.FindFwd(xml, "\"", eqPos + 1); if (q0Pos == String_.Find_none) throw xmlErr.Add("eqPos", eqPos);
			int q1Pos = String_.FindFwd(xml, "\"", q0Pos + 1); if (q1Pos == String_.Find_none) throw xmlErr.Add("q1Pos", q1Pos);
			int spPos = eqPos - 1;
			while (spPos > -1) {
				char c = String_.CharAt(xml, spPos);
				if (Char_.IsWhitespace(c)) break;
				spPos--;
			}
			if (spPos == String_.Find_none) throw xmlErr.Hdr_("could not find hdr").Add("eqPos", eqPos);
			String key = String_.Mid(xml, spPos + 1, eqPos);
			String val = String_.Mid(xml, q0Pos + 1, q1Pos);
			rv.Add(key, val);
			pos = q1Pos;
		}
		return rv;
	}
}
class Xpath_Args {
	public boolean SelectFirst; // false=SelectAll
	public boolean Cancel;
	public static Xpath_Args all_() {return new Xpath_Args(false);}
	public static Xpath_Args first_() {return new Xpath_Args(true);}
	Xpath_Args(boolean selectFirst) {this.SelectFirst = selectFirst;}
}
enum Xpath_SelectMode {All, First}
