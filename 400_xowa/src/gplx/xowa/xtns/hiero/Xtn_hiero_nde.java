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
package gplx.xowa.xtns.hiero; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.parsers.logs.*;
public class Xtn_hiero_nde implements Xox_xnde, Xop_xnde_atr_parser {
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {}
	public void Xtn_parse(Xow_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		boolean log_wkr_enabled = Log_wkr != Xop_log_basic_wkr.Null; if (log_wkr_enabled) Log_wkr.Log_end_xnde(ctx.Page(), Xop_log_basic_wkr.Tid_hiero, src, xnde);
	}	public static Xop_log_basic_wkr Log_wkr = Xop_log_basic_wkr.Null;
	public void Xtn_write(Xoa_app app, Xoh_html_wtr html_wtr, Xoh_opts opts, Xop_ctx ctx, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, int depth) {}
}
class Hiero_prefab_mgr {
//		private Hash_adp_bry hash = Hash_adp_bry.ci_();
	public void Init_by_load(byte[] bry) {
	}
}
class Hiero_prefab_itm {
	public Hiero_prefab_itm(byte[] key) {this.key = key;}
	public byte[] Key() {return key;} private byte[] key;
}
class Hiero_file_itm {
	public Hiero_file_itm(byte[] key, int file_w, int file_h) {this.key = key; this.file_w = file_w; this.file_h = file_h;}
	public byte[] Key() {return key;} private byte[] key;
	public int File_w() {return file_w;} private int file_w;
	public int File_h() {return file_h;} private int file_h;
}
class Hiero_phenome_itm {
	public Hiero_phenome_itm(byte[] key, byte[] gardiner_code) {this.key = key; this.gardiner_code = gardiner_code;}
	public byte[] Key() {return key;} private byte[] key;
	public byte[] Gardiner_code() {return gardiner_code;} private byte[] gardiner_code;
}
class Hiero_parser {
	private ByteTrieMgr_slim trie = ByteTrieMgr_slim.cs_();
	public void Parse(byte[] src, int bgn, int end) {
		int pos = bgn;
		while (true) {
			if (pos == end) break;
			byte b = src[pos];
			Object o = trie.Match(b, src, pos, end);
			if (o == null)
				++pos;
			else {
				Hiero_parser_itm itm = (Hiero_parser_itm)o;
				int new_pos = trie.Match_pos();
				switch (itm.Tid()) {
					case Hiero_parser_itm.Tid_comment:
						int end_comm = Byte_ary_finder.Find_fwd(src, Xoh_consts.Comm_end, new_pos, end);
						if (end_comm == Byte_ary_finder.Not_found)	// --> not found; for now, ignore
							pos = new_pos;
						else
							pos = end_comm + Xoh_consts.Comm_end.length;
						break;
					case Hiero_parser_itm.Tid_itm_dlm:

						break;
				}
			}
		}
	}
	public void Init() {
		Init_itms(Hiero_parser_itm.Tid_itm_dlm, " ", "-", "\t", "\n", "\r");
		Init_itms(Hiero_parser_itm.Tid_tkn_dlm, "*", ":", "(", ")");
		Init_itms(Hiero_parser_itm.Tid_single , "!");
		Init_itms(Hiero_parser_itm.Tid_comment, Xoh_consts.Comm_bgn_str);
	}
	private void Init_itms(byte tid, String... keys) {
		int keys_len = keys.length;
		for (int i = 0; i < keys_len; i++) {
			String key_str = keys[i];
			byte[] key_bry = ByteAry_.new_utf8_(key_str);
			Hiero_parser_itm itm = new Hiero_parser_itm(tid, key_bry);
			trie.Add(key_bry, itm);
		}
	}
}
class Hiero_parser_itm {
	public Hiero_parser_itm(byte tid, byte[] key) {this.tid = tid; this.key = key;}
	public byte Tid() {return tid;} private byte tid;
	public byte[] Key() {return key;} private byte[] key;
	public static final byte Tid_itm_dlm = 1, Tid_tkn_dlm = 2, Tid_single = 3, Tid_comment = 4;
}
/*
class HieroTokenizer {
private static $delimiters = false;
private static $tokenDelimiters;
private static $singleChars;

private $text;
private $blocks = false;
private $currentBlock;
private $token;
 * Constructor
 *
 * @param $text String:
public function __construct( $text ) {
	$this->text = $text;
	self::initStatic();
}

 * Split text into blocks, then split blocks into items
 *
 * @return array: tokenized text
public function tokenize() {
	if ( $this->blocks !== false ) {
		return $this->blocks;
	}
	$this->blocks = array();
	$this->currentBlock = array();
	$this->token = '';

	$text = preg_replace( '/\\<!--.*?--\\>/s', '', $this->text ); // remove HTML comments

	for ( $i = 0; $i < strlen( $text ); $i++ ) {
		$char = $text[$i];

		if ( isset( self::$delimiters[$char] ) ) {
			$this->newBlock();
		} elseif ( isset( self::$singleChars[$char] ) ) {
			$this->singleCharBlock( $char );
		} elseif ( $char == '.' ) {
			$this->dot();
		} elseif ( isset( self::$tokenDelimiters[$char] ) ) {
			$this->newToken( $char );
		} else {
			$this->char( $char );
		}
	}

	$this->newBlock(); // flush stuff being processed

	return $this->blocks;
}

 * Handles a block delimiter
private function newBlock() {
	$this->newToken();
	if ( $this->currentBlock ) {
		$this->blocks[] = $this->currentBlock;
		$this->currentBlock = array();
	}
}

 * Flushes current token, optionally adds another one
 *
 * @param $token Mixed: token to add or false
private function newToken( $token = false ) {
	if ( $this->token !== '' ) {
		$this->currentBlock[] = $this->token;
		$this->token = '';
	}
	if ( $token !== false ) {
		$this->currentBlock[] = $token;
	}
}

 * Adds a block consisting of one character
 *
 * @param $char String: block character
private function singleCharBlock( $char ) {
	$this->newBlock();
	$this->blocks[] = array( $char );
}

 * Handles void blocks represented by dots
private function dot() {
	if ( $this->token == '.' ) {
		$this->token = '..';
		$this->newBlock();
	} else {
		$this->newBlock();
		$this->token = '.';
	}
}

 * Adds a miscellaneous character to current token
 *
 * @param $char String: character to add
private function char( $char ) {
	if ( $this->token == '.' ) {
		$this->newBlock();
		$this->token = $char;
	} else {
		$this->token .= $char;
	}
}
}
*/
