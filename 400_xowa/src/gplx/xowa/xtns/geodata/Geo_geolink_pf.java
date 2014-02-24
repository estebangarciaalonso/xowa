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
package gplx.xowa.xtns.geodata; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
class Geo_geolink_math {
	private byte dir_id; private int precision, step; double dec;
	public int Error() {return error;} private int error;
	public void Init_coord(Bry_bfr_mkr bfr_mkr, byte[] src, Arg_itm_tkn input, Arg_itm_tkn dir, Arg_itm_tkn precision) {
		this.dir_id = Parse_dir(src, dir);
		this.precision = Parse_precision(src, precision);
		this.dec = 0;
		this.step = 0;
		Parse_input(bfr_mkr, src, input);
		step += 0;
		Appease(this.precision, step);
	}
	private void Appease(int dec, int step) {}
	private static byte Parse_dir(byte[] src, Arg_itm_tkn tkn) {
		Object dir_obj = Dir_trie.MatchAtCur(src, tkn.Src_bgn(), tkn.Src_end());
		return dir_obj == null ? Dir_unknown_id : ((ByteVal)dir_obj).Val();
	}
	private static int Parse_precision(byte[] src, Arg_itm_tkn tkn) {	// REF.MW: MapSourcesMath.php|newCoord
		int rv = ByteAry_.X_to_int_or(src, tkn.Src_bgn(), tkn.Src_end(), Int_.MinValue);
		if		(rv > -1 && rv < 10)		return rv;
		else if	(rv == -1)					return 9;
		else								return 4;
	}
	private void Parse_input(Bry_bfr_mkr bfr_mkr, byte[] src, Arg_itm_tkn tkn) {
		byte[] input = Parse_input_normalize(bfr_mkr, src, tkn);
		if (input == null) return;
		int len = input.length;
		int word_idx = -1, word_bgn = 0, words_len = 0;
		for (int i = 0; i < len; i++) {
			byte b = input[i];
			switch (b) {
				case Byte_ascii.Space:
					Parse_input_word(rv, input, ++word_idx, word_bgn, i);
					++words_len;
					break;
			}
		}
		if (words_len < 1 || words_len > 4) error = -2;
		if (rv[0] >= 0)
			this.dec = (rv[0] + rv[1] / 60 + rv[2] / 3600 ) * rv[3];
		else
			this.dec = (rv[0] - rv[1] / 60 - rv[2] / 3600 ) * rv[3];
		this.step = 1;
	}
	private double[] rv = new double[4];
	private boolean Parse_input_word_is_compass(byte v) {
		switch (v) {
			case Byte_ascii.Ltr_N:
			case Byte_ascii.Ltr_E:
			case Byte_ascii.Ltr_S:
			case Byte_ascii.Ltr_W:
				return true;
			default:
				return false;
		}
	}
	private void Parse_input_word(double[] rv, byte[] input, int word_idx, int word_bgn, int word_end) {
		byte unit_dlm = Input_units[word_idx];
		int pos = Byte_ary_finder.Find_fwd(input, unit_dlm, word_bgn, word_end);
		if (pos != ByteAry_.NotFound)	// remove dlms from end of bry; EX: "123'"  -> "123"
			word_end = pos;
		if (!Parse_input_word_is_compass(input[word_bgn])) {
			double word_val = ByteAry_.XtoDoubleByPosOr(input, word_bgn, word_end, Double_.NaN);
			if (word_val != Double_.NaN) {
				if (word_idx > 2) {error = -4; return;}
				switch (word_idx) {
					case 0:
						if (word_val <= -180 || word_val > 180) {error = -5; return;}
						rv[0] = word_val;
						break;
					case 1:
						if (word_val < 0 || word_val >= 60) {error = -6; return;}
						if (rv[0] != (int)(rv[0])) {error = -7; return;}
						rv[1] = word_val;
						break;
					case 2:
						if (word_val < 0 || word_val >= 60) {error = -8; return;}
						if (rv[1] != (int)(rv[1])) {error = -9; return;}
						rv[2] = word_val;
						break;
				}
			}
			else {error = -3; return;}
		}
		else {
			// 'NSEW'
			// if (word_idx != words_len - 1) {error = -10; return;}
			byte word_byte = input[word_bgn];
			if (rv[0] < 0) {error = -11; return;}
			if (word_end - word_bgn != 1) {error = -3; return;}
			switch (dir_id) {
				case Dir_long_id:
					if (word_byte == Byte_ascii.Ltr_N || word_byte == Byte_ascii.Ltr_S) {error = -12; return;}
					break;
				case Dir_lat_id:
					if (word_byte == Byte_ascii.Ltr_E || word_byte == Byte_ascii.Ltr_W) {error = -12; return;}
					break;
				case Dir_unknown_id:
					if (word_byte == Byte_ascii.Ltr_N || word_byte == Byte_ascii.Ltr_S)	this.dir_id = Dir_lat_id;
					else																this.dir_id = Dir_long_id;
					break;
			}
			if (this.dir_id == Dir_lat_id) {
				double rv_0 = rv[0];
				if (rv_0 < -90 || rv_0 > 90) {error = -13; return;}
			}
			if (word_byte == Byte_ascii.Ltr_S || word_byte == Byte_ascii.Ltr_W)
				rv[3] = -1;
		}
	}
	private byte[] Parse_input_normalize(Bry_bfr_mkr bfr_mkr, byte[] src, Arg_itm_tkn tkn) {
		ByteAryBfr bfr = bfr_mkr.Get_b128();
		int src_bgn = tkn.Src_bgn(), src_end = tkn.Src_end(); if (src_end - src_bgn == 0) {error = -1; return null;}
		int i = src_bgn;
		while (i < src_end) {
			byte b = src[i];
			Object o = Input_trie.Match(b, src, i, src_end);
			if (o == null) {
				bfr.Add_byte(b);
				++i;
			}
			else {
				byte tid = ((ByteVal)o).Val();
				switch (tid) {
					case Input_tid_apos:			bfr.Add_byte(Byte_ascii.Apos).Add_byte_space(); break;		// EX: "'" -> "' "
					case Input_tid_quote:			bfr.Add_byte(Byte_ascii.Quote).Add_byte_space(); break;		// EX: '"' -> '" '
					case Input_tid_dash:			bfr.Add_byte(Byte_ascii.Dash); break;
					case Input_tid_space:			bfr.Add_byte_space(); break;
					case Input_tid_degree:			bfr.Add_byte(Byte_ascii.Slash); bfr.Add_byte_space(); break;		// EX: "°" -> "° "
					case Input_tid_compass:			bfr.Add_byte_space(); bfr.Add_byte(Byte_ascii.Uppercase(b)); break;	// NOTE: always single-char ASCII; EX: N,E,S,W,n,e,s,w
				}
			}
		}
		return bfr.XtoAryAndClearAndTrim();
	}
	private static final byte Dir_unknown_id = 0, Dir_lat_id = 1, Dir_long_id = 2;
	private static final byte[] Dir_lat_bry = ByteAry_.new_ascii_("lat"), Dir_long_bry = ByteAry_.new_ascii_("long");
	private static final ByteTrieMgr_slim Dir_trie = ByteTrieMgr_slim.ci_()
	.Add_bry_bval(Dir_lat_bry			, Dir_lat_id)
	.Add_bry_bval(Dir_long_bry			, Dir_long_id)
	;

	private static final byte Input_tid_apos = 1, Input_tid_quote = 2, Input_tid_dash = 3, Input_tid_space = 4, Input_tid_degree = 5, Input_tid_compass = 6;
	private static final byte Input_byte_degree = Byte_ascii.Slash;		// NOTE: ugly cheat to avoid using multi-byte char; note that all "/" are swapped out to " ", so any remaining "/" was added by the normalizer; EX:  "123° 4/5" -> "123/ 4 5"
	private static final byte[] Input_units = new byte[] {Input_byte_degree, Byte_ascii.Apos, Byte_ascii.Quote, Byte_ascii.Space};
	private static final byte[] Input_bry_degree = ByteAry_.new_utf8_("°");
	private static final ByteTrieMgr_slim Input_trie = ByteTrieMgr_slim.cs_()
	.Add_str_byte("'"					, Input_tid_apos)		// NOTE: must add ' so that "'" -> "' "
	.Add_str_byte("‘"					, Input_tid_apos)
	.Add_str_byte("’"					, Input_tid_apos)
	.Add_str_byte("′"					, Input_tid_apos)
	.Add_str_byte("\""					, Input_tid_quote)		// NOTE: must add " so that '"' -> '" '
	.Add_str_byte("''"					, Input_tid_quote)
	.Add_str_byte("“"					, Input_tid_quote)
	.Add_str_byte("”"					, Input_tid_quote)
	.Add_str_byte("″"					, Input_tid_quote)
	.Add_str_byte("-"					, Input_tid_dash)
	.Add_str_byte("−"					, Input_tid_dash)
	.Add_str_byte(" "					, Input_tid_space)
	.Add_str_byte("_"					, Input_tid_space)
	.Add_str_byte("/"					, Input_tid_space)
	.Add_str_byte("\t"					, Input_tid_space)
	.Add_str_byte("\n"					, Input_tid_space)
	.Add_str_byte("\r"					, Input_tid_space)
	.Add_bry_bval(Input_bry_degree			, Input_tid_degree)
	.Add_str_byte("N"					, Input_tid_compass)
	.Add_str_byte("S"					, Input_tid_compass)
	.Add_str_byte("E"					, Input_tid_compass)
	.Add_str_byte("W"					, Input_tid_compass)
	.Add_str_byte("n"					, Input_tid_compass)
	.Add_str_byte("s"					, Input_tid_compass)
	.Add_str_byte("e"					, Input_tid_compass)
	.Add_str_byte("w"					, Input_tid_compass)
	;
}
//	public class Geo_geolink_pf_tst {
//		@Before public void init()				{fxt.Reset();} private Xop_fxt fxt = new Xop_fxt();
//		@Test  public void Basic()				{fxt.Test_parse_tmpl_str_test("{{#coordinates:1|2}}"									, "{{test}}"	, "");}
//	}
