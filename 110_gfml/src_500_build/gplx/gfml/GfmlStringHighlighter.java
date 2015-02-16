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
package gplx.gfml; import gplx.*;
import gplx.core.strings.*;
class GfmlStringHighlighter {
	public String Raw() {return raw;} public GfmlStringHighlighter Raw_(String v) {raw = v; return this;} private String raw;
	public int ExcerptLen() {return excerptLen;} public GfmlStringHighlighter ExcerptLen_(int v) {excerptLen = v; return this;} int excerptLen = 40;
	public GfmlStringHighlighter Mark_(int pos, char c, String msg) {
		marks.Add(new GfmlStringHighlighterMarker().Pos_(pos).Sym_(c).Msg_(msg));
		return this;
	}
	int XtoBgnPos(int pos, int prvEndPos) {
		int rv = pos - excerptLen;
		if (rv < prvEndPos) rv = prvEndPos;	 // ensure bgnPos is not < prev end pos; ex: marks of 5,12; at 12, bgnPos = 2 which is less than 5; make 5
		return rv;
	}
	public String[] Gen() {
		String_bldr posBfr = String_bldr_.new_(), rawBfr = String_bldr_.new_(), symBfr = String_bldr_.new_();
		ListAdp symList = ListAdp_.new_();
		int bgnPos = 0, endPos = 0;
		int rawLen = String_.Len(raw); int rawLenDigits = Int_.DigitCount(rawLen);
		int rawBfrBgn = -1, marksLastIdx = marks.LastIndex();
		for (int i = 0; i < marks.Count(); i++) {
			GfmlStringHighlighterMarker curMark = (GfmlStringHighlighterMarker)marks.FetchAt(i);
			GfmlStringHighlighterMarker nxtMark = i == marksLastIdx ? GfmlStringHighlighterMarker.Null : (GfmlStringHighlighterMarker)marks.FetchAt(i + 1);
			// bgnPos
			bgnPos = XtoBgnPos(curMark.Pos(), endPos);
			if (i == 0) rawBfrBgn = bgnPos;

			// endPos
			int nxtMarkPos = nxtMark == GfmlStringHighlighterMarker.Null ? Int_.MaxValue : nxtMark.Pos();
			endPos = curMark.Pos() + excerptLen;
			if (endPos >= nxtMarkPos) endPos = nxtMarkPos;
			if (endPos > rawLen ) endPos = rawLen + 1;

			// build bfrs
			for (int j = bgnPos; j < endPos; j++) {
				char rawChar = j == rawLen ? ' ' : String_.CharAt(raw, j);
				if		(rawChar == '\t') {posBfr.Add("t"); rawBfr.Add(" ");}
				else if (rawChar == '\n') {posBfr.Add("n"); rawBfr.Add(" ");}
				else				{
					char posChar = j == rawLen ? '>' : ' ';
					posBfr.Add(posChar);
					rawBfr.Add(rawChar);
				}
				char symChar = j == curMark.Pos() ? curMark.Sym() : ' ';
				symBfr.Add(symChar);
			}

			// gap
			int nxtMarkBgn = XtoBgnPos(nxtMark.Pos(), endPos);
			int gap = nxtMarkBgn - endPos;
			if (gap > 0) {
				int gapDigits = Int_.DigitCount(gap);
				posBfr.Add_fmt("[{0}]", Int_.Xto_str_pad_bgn(gap, gapDigits));
				rawBfr.Add_fmt("[{0}]", String_.Repeat(".", gapDigits));
				symBfr.Add_fmt(" {0} ", String_.Repeat(" ", gapDigits));
			}
			if (curMark.Sym() != ' ')
				symList.Add(String_.Format("[{0}] {1} {2}", Int_.Xto_str_pad_bgn(curMark.Pos(), rawLenDigits), curMark.Sym(), curMark.Msg()));
		}
		if (rawBfrBgn == 0) {
			posBfr.Add_at(0, "<");
			rawBfr.Add_at(0, " ");
			symBfr.Add_at(0, " ");
		}
		ListAdp rv = ListAdp_.new_();
		rv.Add(posBfr.XtoStr());
		rv.Add(rawBfr.XtoStr());
		rv.Add(symBfr.XtoStr());
		if (symList.Count() > 0)
			rv.Add("");
		for (int i = 0; i < symList.Count(); i++)
			rv.Add((String)symList.FetchAt(i));
		return rv.XtoStrAry();
	}
	ListAdp marks = ListAdp_.new_();
        public static GfmlStringHighlighter new_() {
		GfmlStringHighlighter rv = new GfmlStringHighlighter();
		return rv;
	}	GfmlStringHighlighter() {}
}
class GfmlStringHighlighterMarker {
	public int Pos() {return pos;} public GfmlStringHighlighterMarker Pos_(int v) {pos = v; return this;} int pos;
	public char Sym() {return sym;} public GfmlStringHighlighterMarker Sym_(char v) {sym = v; return this;} char sym;
	public String Msg() {return msg;} public GfmlStringHighlighterMarker Msg_(String v) {msg = v; return this;} private String msg;
	public static final GfmlStringHighlighterMarker Null = new GfmlStringHighlighterMarker().Pos_(-1);
}
