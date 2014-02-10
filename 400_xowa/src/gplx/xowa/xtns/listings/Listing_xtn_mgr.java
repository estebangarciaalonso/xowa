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
package gplx.xowa.xtns.listings; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.html.*;
import gplx.xowa.wikis.*;
public class Listing_xtn_mgr extends Xox_mgr_base {
	@Override public byte[] Xtn_key() {return Xtn_key_static;} public static final byte[] Xtn_key_static = ByteAry_.new_ascii_("listing");
	@Override public void Xtn_init_by_wiki(Xow_wiki wiki) {
		if (wiki.Domain_tid() != Xow_wiki_domain_.Tid_wikivoyage) return;
		Xop_ctx sub_ctx = Xop_ctx.new_sub_(wiki);
		phone_symbol = Load_txt(wiki, sub_ctx, "listings-phone-symbol", "listings-phone");
		fax_symbol = Load_txt(wiki, sub_ctx, "listings-phone-symbol", "listings-phone");
		email_symbol = Load_txt(wiki, sub_ctx, "listings-phone-symbol", "listings-phone");
		tollfree_symbol = Load_txt(wiki, sub_ctx, "listings-phone-symbol", "listings-phone");
		checkin_text = Load_txt(wiki, sub_ctx, "listings-phone-symbol", "listings-phone");
		checkout_text = Load_txt(wiki, sub_ctx, "listings-phone-symbol", "listings-phone");
		position_text = Load_txt(wiki, sub_ctx, "listings-phone-symbol", "listings-phone");
		hwtr = new Html_wtr();
	}
	@gplx.Internal protected Html_wtr Hwtr() {return hwtr;} private Html_wtr hwtr;
	public byte[] Phone_symbol() {return phone_symbol;} private byte[] phone_symbol;
	public byte[] Fax_symbol() {return fax_symbol;} private byte[] fax_symbol;
	public byte[] Email_symbol() {return email_symbol;} private byte[] email_symbol;
	public byte[] Tollfree_symbol() {return tollfree_symbol;} private byte[] tollfree_symbol;
	public byte[] Checkin_text() {return checkin_text;} private byte[] checkin_text;
	public byte[] Checkout_text() {return checkout_text;} private byte[] checkout_text;
	public byte[] Position_text() {return position_text;} private byte[] position_text;
	public byte[] Load_txt(Xow_wiki wiki, Xop_ctx sub_ctx, String symbol_ttl, String template_ttl) {
		byte[] symbol_text = wiki.Msg_mgr().Val_by_key_obj(ByteAry_.new_utf8_(symbol_ttl)); if (symbol_text == null) return null;// symbol_ttl does not exist
		symbol_text = wiki.Parser().Parse_fragment_to_html(sub_ctx, symbol_text);
		symbol_text = Html_util.Escape_html_as_bry(symbol_text);
		byte[] template_text = wiki.Msg_mgr().Val_by_key_obj(ByteAry_.new_utf8_(template_ttl));
		byte[] rv = null;
		if (symbol_text != null) {
			hwtr.Nde_full_atrs(Listing_xnde.Tag_abbr, symbol_text
				, Listing_xnde.Atr_a_title, Html_util.Escape_html_as_bry(template_text)
				);
			rv = hwtr.X_to_bry();
		}
		else {
			rv = template_text;
		}
		return rv;
	}
}
/*
//	$position = '';
//	if ( $lat < 361 && $long < 361 ) { // @fixme: incorrect validation
//		if ( !wfMessage( 'listings-position-template' )->inContentLanguage()->isDisabled() ) {
//			$position = wfMessage( 'listings-position-template', $lat, $long )->inContentLanguage()->text();
//			if ( $position != '' ) {
//				$position = $parser->internalParse( '{{' . $position . '}}' );
//				// @todo FIXME: i18n issue (hard coded colon/space)
//				$position = wfMessage( 'listings-position', $position )->inContentLanguage()->text();
//			}
//		}
//	}
$phoneSymbol = $parser->internalParse( wfMessage( 'listings-phone-symbol' )->inContentLanguage()->text() );
if ( $phoneSymbol != '' ) {
	$phoneSymbol = '<abbr title="' . wfMessage( 'listings-phone' )->inContentLanguage()->escaped() . '">' . $phoneSymbol . '</abbr>';
} else {
	$phoneSymbol = wfMessage( 'listings-phone' )->inContentLanguage()->escaped();
}
$faxSymbol = $parser->internalParse( wfMessage( 'listings-fax-symbol' )->inContentLanguage()->text() );
if ( $faxSymbol != '' ) {
	$faxSymbol = '<abbr title="' . wfMessage( 'listings-fax' )->inContentLanguage()->escaped() . '">' . $faxSymbol . '</abbr>';
} else {
	$faxSymbol = wfMessage( 'listings-fax' )->inContentLanguage()->escaped();
}
$emailSymbol = $parser->internalParse( wfMessage( 'listings-email-symbol' )->inContentLanguage()->text() );
if ( $emailSymbol != '' ) {
	$emailSymbol = '<abbr title="' . wfMessage( 'listings-email' )->inContentLanguage()->escaped() . '">' . $emailSymbol . '</abbr>';
} else {
	$emailSymbol = wfMessage( 'listings-email' )->inContentLanguage()->escaped();
}
$tollfreeSymbol = $parser->internalParse( wfMessage( 'listings-tollfree-symbol' )->inContentLanguage()->text() );
if ( $tollfreeSymbol != '' ) {
	$tollfreeSymbol = '<abbr title="' . wfMessage( 'listings-tollfree' )->inContentLanguage()->escaped() . '">' . $tollfreeSymbol . '</abbr>';
} else {
	$tollfreeSymbol = wfMessage( 'listings-tollfree' )->inContentLanguage()->escaped();
}
*/
