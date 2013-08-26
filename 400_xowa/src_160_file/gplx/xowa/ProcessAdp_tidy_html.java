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
public class ProcessAdp_tidy_html extends ProcessAdp { 	@Override public ProcessAdp Tmp_dir_(Io_url v) {
		tidy_source = v.GenSubFil("tidy_source.html");
		tidy_target = v.GenSubFil("tidy_target.html");
		return super.Tmp_dir_(v);
	}	Io_url tidy_source, tidy_target;
	public void Run_tidy_html(ByteAryBfr src_bfr, ByteAryBfr trg_bfr) {
		Io_mgr._.SaveFilBfr(tidy_source, src_bfr);
		this.Run(tidy_source.Raw(), tidy_target.Raw());
		Io_mgr._.LoadFilBfr(tidy_target, trg_bfr);
	}
	public static final String Args_fmt = String_.Concat	// see https://meta.wikimedia.org/wiki/Data_dumps; missing numeric-entities:yes; enclose-text: yes
	(	"-utf8 "						// otherwise defaults to ascii
	,	"--force-output y "				// always generate output; do not abort on error
	,	"--show-body-only y "			// prevent tidy from surrounding input with <html><body>
	,	"--quiet y "					// suppress command-line header
	,	"--wrap 0 "						// do not break html into 80 char chunks
	,	"--enclose-block-text y "		// enclose naked text in element with <p>
	,	"--fix-backslash n "			// do not change \ to / in URLs
	,	"--fix-url n "					// do not escape invalid chars in uris
	,	"--literal-attributes y "		// do not alter whitespace chars in attributes
	,	"--output-xhtml y "				// output as xhtml (p's and li's will have closing tags)
	,	"--quote-nbsp y "				// preserve nbsp as entities; do not convert to Unicode character 160
	,	"--wrap-attributes n "			// do not line-wrap attribute values (assume tidy will try to take a="b\nc" and change to a="b c" which may cause some fidelity issues?)
	,	"--tidy-mark n "				// do not add tidy watermark
	,	"-o \"~{target}\" "				// target file
	,	"\"~{source}\""					// source file
	);
}
