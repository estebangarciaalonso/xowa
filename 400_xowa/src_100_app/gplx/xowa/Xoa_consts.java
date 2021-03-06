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
public class Xoa_consts {
	public static final byte[] Url_wiki_intermediary = ByteAry_.new_ascii_("/wiki/");
	public static final byte[] Url_relative_prefix = new byte[] {Byte_ascii.Slash, Byte_ascii.Slash};	// "//"
	public static final byte[] Pipe_bry = new byte[] {Byte_ascii.Pipe};
	public static final byte[] Nl_bry = new byte[] {Byte_ascii.NewLine};
	public static final byte[] Invk_bgn = new byte[] {Byte_ascii.Curly_bgn, Byte_ascii.Curly_bgn};
	public static final byte[] Invk_end = new byte[] {Byte_ascii.Curly_end, Byte_ascii.Curly_end};
	public static final byte[] Transclude_bgn = new byte[] {Byte_ascii.Curly_bgn, Byte_ascii.Curly_bgn, Byte_ascii.Colon};
	public static final byte[] Slash_bry = new byte[] {Byte_ascii.Slash};
}
