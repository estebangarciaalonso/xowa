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
	@Override public boolean Enabled_default() {return false;}
	@Override public byte[] Xtn_key() {return Xtn_key_static;} public static final byte[] Xtn_key_static = ByteAry_.new_ascii_("listing");
	@Override public Xox_mgr Clone_new() {return new Listing_xtn_mgr();}
	@Override public void Xtn_init_by_wiki(Xow_wiki wiki) {
		if (!Enabled()) return;
		hwtr = new Html_wtr();
		Xop_ctx sub_ctx = Xop_ctx.new_sub_(wiki);
		listings_template		= Load_txt(wiki, sub_ctx, "listings-template");
		phone_symbol			= Load_txt(wiki, sub_ctx, "listings-phone-symbol", "listings-phone");
		fax_symbol				= Load_txt(wiki, sub_ctx, "listings-fax-symbol", "listings-fax");
		email_symbol			= Load_txt(wiki, sub_ctx, "listings-email-symbol", "listings-email");
		tollfree_symbol			= Load_txt(wiki, sub_ctx, "listings-tollfree-symbol", "listings-tollfree");
		checkin_text			= Load_txt(wiki, sub_ctx, "listings-checkin");
		checkout_text			= Load_txt(wiki, sub_ctx, "listings-checkout");
		position_template_1		= Load_txt(wiki, sub_ctx, "listings-position-template");
		position_template_2		= Load_txt(wiki, sub_ctx, "listings-position");
	}
	@gplx.Internal protected Html_wtr Hwtr() {return hwtr;} private Html_wtr hwtr;
	public byte[] Listings_template() {return listings_template;} private byte[] listings_template;
	public byte[] Phone_symbol() {return phone_symbol;} private byte[] phone_symbol;
	public byte[] Fax_symbol() {return fax_symbol;} private byte[] fax_symbol;
	public byte[] Email_symbol() {return email_symbol;} private byte[] email_symbol;
	public byte[] Tollfree_symbol() {return tollfree_symbol;} private byte[] tollfree_symbol;
	public byte[] Checkin_text() {return checkin_text;} private byte[] checkin_text;
	public byte[] Checkout_text() {return checkout_text;} private byte[] checkout_text;
	public byte[] Position_template_1() {return position_template_1;} private byte[] position_template_1;
	public byte[] Position_template_2() {return position_template_2;} private byte[] position_template_2;
	public byte[] Load_txt(Xow_wiki wiki, Xop_ctx sub_ctx, String symbol_ttl, String template_ttl) {
		byte[] symbol_text = Load_txt(wiki, sub_ctx, symbol_ttl);
		byte[] template_text = Load_txt(wiki, sub_ctx, template_ttl);
		byte[] rv = null;
		if (symbol_text != null) {
			hwtr.Nde_full_atrs(Listing_xnde.Tag_abbr, symbol_text, true
				, Listing_xnde.Atr_a_title, Html_util.Escape_html_as_bry(template_text)
				);
			rv = hwtr.X_to_bry_and_clear();
		}
		else {
			rv = template_text;
		}
		return rv;
	}
	public byte[] Load_txt(Xow_wiki wiki, Xop_ctx sub_ctx, String ttl) {
		byte[] rv = wiki.Msg_mgr().Val_by_key_obj(ByteAry_.new_utf8_(ttl)); if (rv == null) return null;	// ttl does not exist
		rv = wiki.Parser().Parse_fragment_to_html(sub_ctx, rv);
		rv = Html_util.Escape_html_as_bry(rv);
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
*/
