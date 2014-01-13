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
import org.junit.*;
public class Xot_invk_wkr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Reset();}
	@Test  public void Basic() {
		fxt.tst_Parse_page_tmpl("{{a}}", fxt.tkn_tmpl_invk_w_name(0, 5, 2, 3));
	}
	@Test  public void Arg_many() {
		fxt.tst_Parse_page_tmpl("{{a|b|c|d}}", fxt.tkn_tmpl_invk_w_name(0, 11, 2, 3)
			.Args_(fxt.tkn_arg_val_txt_(4, 5), fxt.tkn_arg_val_txt_(6, 7), fxt.tkn_arg_val_txt_(8, 9)));
	}
	@Test  public void Kv() {
		fxt.tst_Parse_page_tmpl("{{a|b=c}}", fxt.tkn_tmpl_invk_w_name(0, 9, 2, 3)
			.Args_
			(	fxt.tkn_arg_nde_()
			.		Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(4, 5)))
			.		Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(6, 7)))
			));
	}
	@Test  public void Kv_arg() {
		fxt.tst_Parse_tmpl("{{a|b={{{1}}}}}", fxt.tkn_tmpl_invk_w_name(0, 15, 2, 3)
			.Args_
			(	fxt.tkn_arg_nde_()
			.		Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(4, 5)))
			.		Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_tmpl_prm_find_(fxt.tkn_txt_(9, 10))))
			));
	}
	@Test  public void Kv_tmpl_compiled() {
		fxt.tst_Parse_tmpl("{{a|b={{c}}}}", fxt.tkn_tmpl_invk_w_name(0, 13, 2, 3)
			.Args_
			(	fxt.tkn_arg_nde_()
			.		Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(4, 5)))
			.		Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_tmpl_invk_w_name(6, 11, 8, 9)))
			));
	}
	@Test  public void Kv_tmpl_dynamic() {
		fxt.tst_Parse_tmpl("{{a|b={{c|{{{1}}}}}}}", fxt.tkn_tmpl_invk_w_name(0, 21, 2, 3)
			.Args_
			(	fxt.tkn_arg_nde_()
			.		Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(4, 5)))
			.		Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_tmpl_invk_w_name(6, 19, 8, 9).Args_(fxt.tkn_arg_nde_().Val_tkn_(fxt.tkn_arg_itm_().Subs_(fxt.tkn_tmpl_prm_find_(fxt.tkn_txt_(13, 14))))))
			)
			));
	}
	@Test  public void Nest() {
		fxt.tst_Parse_page_tmpl("{{a|b{{c|d}}e}}", fxt.tkn_tmpl_invk_w_name(0, 15, 2, 3).Args_
			( fxt.tkn_arg_nde_().Val_tkn_(fxt.tkn_arg_itm_
				( fxt.tkn_txt_(4, 5)
				, fxt.tkn_tmpl_invk_w_name(5, 12, 7, 8).Args_
				( fxt.tkn_arg_val_txt_(9, 10)
				)
				, fxt.tkn_txt_(12, 13)
				))
			));
	}
	@Test  public void Whitespace() {
		fxt.tst_Parse_page_tmpl("{{a| b = c }}", fxt.tkn_tmpl_invk_w_name(0, 13, 2, 3).Args_
			(	fxt.tkn_arg_nde_()
					.Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_space_(4, 5).Ignore_y_(), fxt.tkn_txt_(5,  6), fxt.tkn_space_( 6,  7).Ignore_y_()))
					.Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_space_(8, 9).Ignore_y_(), fxt.tkn_txt_(9, 10), fxt.tkn_space_(10, 11).Ignore_y_()))
			));
	}
	@Test  public void WsNl() {
		fxt.tst_Parse_page_tmpl("{{a|b=c\n}}", fxt.tkn_tmpl_invk_w_name(0, 10, 2, 3).Args_
			(	fxt.tkn_arg_nde_()
					.Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(4,  5)))
					.Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(6,  7), fxt.tkn_nl_char_(7, 8).Ignore_y_()))
			));
	}
	@Test  public void Err_noName() { // WP: [[Russian language]]
		fxt.tst_Parse_page_tmpl("{{|}}", fxt.tkn_curly_bgn_(0), fxt.tkn_pipe_(2), fxt.tkn_txt_(3, 5));
	}
	@Test  public void Err_noName_nl() {
		fxt.tst_Parse_page_tmpl("{{\n|}}", fxt.tkn_curly_bgn_(0), fxt.tkn_nl_char_len1_(2), fxt.tkn_pipe_(3), fxt.tkn_txt_(4, 6));
	}
	@Test  public void Err_noName_tab() {
		fxt.tst_Parse_page_tmpl("{{\t|}}", fxt.tkn_curly_bgn_(0), fxt.tkn_tab_(2), fxt.tkn_pipe_(3), fxt.tkn_txt_(4, 6));
	}
	@Test  public void Err_empty() { // WP: [[Set theory]] 
		fxt.tst_Parse_page_tmpl("{{}}", fxt.tkn_txt_(0, 4));
	}
	@Test  public void DynamicName() {
		fxt.ini_defn_clear();
		fxt.ini_defn_add("proc_1", "proc_1 called");
		fxt.ini_defn_add("proc_2", "proc_2 called");
		fxt.tst_Parse_tmpl_str_test("{{proc_{{{1}}}}}"			, "{{test|1}}"		, "proc_1 called");
		fxt.tst_Parse_tmpl_str_test("{{proc_{{{1}}}}}"			, "{{test|2}}"		, "proc_2 called");
		fxt.tst_Parse_tmpl_str_test("{{proc_{{#expr:1}}}}"		, "{{test}}"		, "proc_1 called");
	}
	@Test  public void Comment() {
		fxt.tst_Parse_tmpl_str_test("b"							, "{{test<!--a-->}}"	, "b");
	}
	@Test  public void Err_equal() { // WP:[[E (mathematical constant)]]
		fxt.tst_Parse_page_tmpl("{{=}}", fxt.tkn_tmpl_invk_(0, 5).Name_tkn_(fxt.tkn_arg_nde_(2, 3).Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_eq_(2)))));
	}
	@Test  public void Err_dangling() {	// WP:[[Antoni Salieri]]; {{icon it}\n
		fxt.tst_Parse_page_tmpl("{{fail} {{pass}}", fxt.tkn_curly_bgn_(0), fxt.tkn_txt_(2, 7), fxt.tkn_space_(7, 8), fxt.tkn_tmpl_invk_w_name(8, 16, 10, 14));
	}
	@Test  public void MultipleColon() {
		fxt.ini_defn_clear();
		fxt.ini_defn_add("H:IPA", "{{{1}}}");
		fxt.tst_Parse_tmpl_str_test("{{H:IPA|{{{1}}}}}"			, "{{test|a}}"		, "a");
	}
	@Test  public void RedundantTemplate() {	// {{Template:a}} == {{a}}
		fxt.ini_defn_clear();
		fxt.ini_defn_add("a", "a");
		fxt.tst_Parse_tmpl_str_test("{{Template:a}}"				, "{{test}}"		, "a");
		fxt.ini_defn_clear();
	}
	@Test  public void Lnki() {	// PURPOSE: pipe inside lnki should not be interpreted for tmpl: WP:[[Template:Quote]]
		fxt.tst_Parse_tmpl_str_test("{{{1}}}{{{2}}}"				, "{{test|[[a|b]]|c}}"	, "[[a|b]]c");
	}
	@Test  public void Lnki2() {// PURPOSE: pipe inside lnki inside tmpl should be interpreted WP:[[H:IPA]]
		fxt.tst_Parse_tmpl_str_test("[[a|{{#switch:{{{1}}}|b=c]]|d=e]]|f]]}}"		, "{{test|b}}"			, "[[a|c]]");
	}
	@Test  public void Nowiki() {	// PURPOSE: nowiki tag should be ignored: en.w:Template:Main
		fxt.tst_Parse_tmpl_str_test("<div <nowiki>class='a'</nowiki> />"				, "{{test}}"			, "<div <nowiki>class='a'</nowiki> />");
	}
	@Test  public void Nowiki_if() {	// PURPOSE: templates and functions inside nowiki should not be evaluated
		fxt.tst_Parse_tmpl_str_test("a <nowiki>{{#if:|y|n}}</nowiki> b"			, "{{test}}"			, "a <nowiki>{{#if:|y|n}}</nowiki> b");
	}
	@Test  public void Nowiki_endtag() {	// PURPOSE: <nowiki/> should not match </nowiki>
		fxt.tst_Parse_page_all_str("<nowiki /> ''b'' <nowiki>c</nowiki>"		, " <i>b</i> c");
	}
	@Test  public void Nowiki_xnde_frag() {	// PURPOSE: nowiki did not handle xnde frag and literalized; nowiki_xnde_frag; DATE:2013-01-27
		fxt.tst_Parse_page_all_str("<nowiki><pre></nowiki>{{#expr:1}}<pre>b</pre>"	, "&lt;pre&gt;1<pre>b</pre>");
	}
	@Test  public void Nowiki_lnki() {	// PURPOSE: nowiki should noop lnkis; DATE:2013-01-27
		fxt.tst_Parse_page_all_str("<nowiki>[[A]]</nowiki>"	, "[[A]]");
	}
	@Test  public void Nowiki_underscore() {	// PURPOSE: nowiki did not handle __DISAMBIG__; DATE:2013-07-28
		fxt.tst_Parse_page_all_str("<nowiki>__DISAMBIG__</nowiki>"	, "__DISAMBIG__");
	}
	@Test  public void Nowiki_asterisk() {	// PURPOSE: nowiki should noop lists; DATE:2013-08-26
		fxt.tst_Parse_page_all_str("<nowiki>*a</nowiki>", "*a");
	}
	@Test  public void Nowiki_space() {	// PURPOSE: nowiki should noop space (else pre); DATE:2013-09-03
		fxt.Ctx().Para().Enabled_y_();
		fxt.tst_Parse_page_all_str("a\n<nowiki> </nowiki>b", "<p>a\n b\n</p>\n");
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void LnkiWithPipe_basic() {	// PURPOSE: pipe in link should not count towards tmpl: WP:{{H:title|dotted=no|pronunciation:|[[File:Loudspeaker.svg|11px|link=|alt=play]]}}
		fxt.tst_Parse_tmpl_str_test("{{{1}}}{{{2}}}"									, "{{test|[[b=c|d]]}}"			, "[[b=c|d]]{{{2}}}");
	}
	@Test  public void LnkiWithPipe_nested() {
		fxt.tst_Parse_tmpl_str_test("{{{1}}}{{{2}}}"									, "{{test|[[b=c|d[[e|f]]g]]}}"	, "[[b=c|d[[e|f]]g]]{{{2}}}");
	}
	@Test  public void LnkiWithPipe_unclosed() {
		fxt.tst_Parse_tmpl_str_test("{{{1}}}{{{2}}}"									, "{{test|[[b=c|d}}"			, "{{test|[[b=c|d}}");
	}
	@Test  public void Err_tmp_empty() {	// PURPOSE: {{{{R from misspelling}} }}
		fxt.ini_Log_(Xop_ttl_log.Invalid_char).tst_Parse_tmpl_str_test("{{{1}}}"										, "{{ {{a}} }}"					, "{{{{:a}}}}");
	}
	@Test  public void AutoAddNewLine() {	// PURPOSE: if {| : ; # *, auto add new_line REF.MW:Parser.php|braceSubstitution
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_inner", "# a");
		fxt.tst_Parse_tmpl_str_test("{{test_inner}}"									, "z {{test}}"					, "z \n# a");
		fxt.ini_defn_clear();
	}
	@Test  public void AutoAddNewLine_pf() {// PURPOSE: if {| : ; # *, auto add new_line; parser_function variant; EX.WP:Soviet Union; Infobox former country
		fxt.tst_Parse_tmpl_str_test(""												, "z {{#if:true|#a|n}}"			, "z \n#a");
	}
	@Test  public void AutoAddNewLine_ignore() {
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_inner", "# a");																							// NOTE: # a -> \n#a; leads to 2 \n below
		fxt.tst_Parse_tmpl_str_test("{{test_inner}}"									, "\n# z\n{{test}}"					, "\n# z\n\n# a");	// NOTE: 2 \n\n
		fxt.ini_defn_clear();
	}
	@Test  public void Mismatch_bgn() {	// PURPOSE: handle {{{ }}; WP:Paris Commune; Infobox Former Country
		fxt.ini_defn_clear();
		fxt.ini_defn_add("!", "|");
		fxt.tst_Parse_tmpl_str_test("{{#if:|{{{!}}{{!}}}|x}}"						, "{{test}}"					, "x");
	}
	@Test  public void Mismatch_tblw() {	// PURPOSE: handle {{{!}}; WP:Soviet Union
		fxt.ini_defn_clear();
		fxt.ini_defn_add("!", "|");
		fxt.tst_Parse_page_all_str("a\n{{{!}}\n|b\n|}", String_.Concat_lines_nl_skipLast
			(	"a"
			,	"<table>"
			,	"  <tr>"
			,	"    <td>b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			) 
			);
		fxt.ini_defn_clear();
	}
	@Test  public void Lnki_space() {
		fxt.ini_defn_clear();
		fxt.ini_defn_add("c", "{{{1}}}");
		fxt.tst_Parse_tmpl_str("{{c|[[a|b ]]}}", "[[a|b ]]");
	}
	@Test  public void WsNl_html() {
		fxt.tst_Parse_tmpl_str_test("{{{1}}}"										, "{{test|\na}}"				, "\na");
	}
	@Test  public void Bug_innerTemplate() {	// PURPOSE: issue with inner templates not using correct key
		fxt.ini_defn_clear();
		fxt.ini_defn_add("temp_1", "{{temp_2|key1=val1}}");
		fxt.ini_defn_add("temp_2", "{{{key1}}}");
		fxt.tst_Parse_tmpl_str("{{temp_1}}", "val1");
	}
	@Test  public void NotFound() {
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_template", "{{[[Template:{{{1}}}|{{{1}}}]]}}");
		fxt.ini_Log_(Xop_ttl_log.Invalid_char).tst_Parse_tmpl_str("{{test_template|a}}", "{{[[Template:a|a]]}}");
		fxt.ini_defn_clear();
	}
	@Test  public void Xnde_xtn_preserved() {	// PURPOSE: tmpl was dropping .Xtn ndes; EX: below was just ab
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_template", "{{{1}}}");
		fxt.tst_Parse_page_all_str("{{test_template|a<source>1</source>b}}", "a<pre>1</pre>b");
		fxt.ini_defn_clear();
	}
	@Test  public void Recurse() {
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_recurse", "bgn:{{test_recurse}}:end");
		fxt.tst_Parse_page_all_str("{{test_recurse}}", "bgn:<span class=\"error\">Template loop detected:test_recurse</span>:end");
		fxt.ini_defn_clear();
	}
	@Test  public void Ws_trimmed_key_0() { // PURPOSE: control test for key_1, key_2
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_1", "{{test_2|{{{1}}}}}");
		fxt.ini_defn_add("test_2", "{{{1}}}");
		fxt.tst_Parse_tmpl_str("{{test_1| a }}", " a ");
		fxt.ini_defn_clear();
	}
	@Test  public void Ws_trimmed_key_1() { // PURPOSE: trim prm when passed as key;
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_1", "{{test_2|key={{{1}}}}}");
		fxt.ini_defn_add("test_2", "{{{key}}}");
		fxt.tst_Parse_tmpl_str("{{test_1| a }}", "a");
		fxt.ini_defn_clear();
	}
	@Test  public void Ws_trimmed_key_2() {	// PURPOSE: trim prm; note that 1 is key not idx; EX.WP:Coord in Chernobyl disaster, Sahara
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_1", "{{test_2|1={{{1}}}}}");
		fxt.ini_defn_add("test_2", "{{{1}}}");
		fxt.tst_Parse_tmpl_str("{{test_1| a }}", "a");
		fxt.ini_defn_clear();
	}
	@Test  public void Ws_trimmed_key_3() { // PURPOSE: trim entire arg only, not individual prm
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_1", "{{test_2|1={{{1}}}{{{2}}}}}");
		fxt.ini_defn_add("test_2", "{{{1}}}");
		fxt.tst_Parse_tmpl_str("{{test_1| a | b }}", "a  b");
		fxt.ini_defn_clear();
	}
	@Test  public void Keyd_arg_is_trimmed() { // PURPOSE: trim entire arg only, not individual prm; EX.WP: William Shakespeare; {{Relatebardtree}}
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_1", "{{test_2|1={{{{{{1}}}}}}}}");
		fxt.ini_defn_add("test_2", "{{{1}}}");
		fxt.tst_Parse_tmpl_str("{{test_1| b | b = x }}", "x");
		fxt.ini_defn_clear();
	}
	@Test  public void Ws_arg() { // PURPOSE: whitespace arg should not throw array index out of bounds; EX.WIKT: wear one's heart on one's sleeve
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_1", "{{{{{1}}}}}");
		fxt.tst_Parse_tmpl_str("{{test_1| }}", "(? [[dynamic is blank]] ?)");
		fxt.ini_defn_clear();
	}
	@Test  public void Asterisk_always_adds_nl() {	// PURPOSE: function should expand "*a" to "\n*a" even if "*a" is bos; SEE:NOTE_1 EX.WP: Rome and Panoramas
		fxt.tst_Parse_page_tmpl_str("{{#if:x|*a}}", "\n*a");
	}
	@Test  public void Xnde_xtn_ref_not_arg() {	// PURPOSE: <ref name= should not be interpreted as arg; EX: {{tmp|a<ref name="b"/>}}; arg1 is a<ref name="b"/> not "b"; EX.WP: WWI
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_1", "{{{1}}}");
		fxt.tst_Parse_page_tmpl_str("{{test_1|a<ref name=b />}}", "a<ref name=b />");
		fxt.ini_defn_clear();
	}
	@Test  public void Multi_bgn_5_end_3_2() {
		fxt.tst_Parse_tmpl("{{{{{1}}}|a}}", fxt.tkn_tmpl_invk_(0, 13)
			.Name_tkn_(fxt.tkn_arg_nde_(2, 9).Key_tkn_(fxt.tkn_arg_itm_(fxt.tkn_tmpl_prm_find_(fxt.tkn_txt_(5, 6)))))
			.Args_
			(	fxt.tkn_arg_val_txt_(10, 11)
			)
			);
	}
	@Test  public void Transclude_template() {	// PURPOSE: {{:Template:Test}} is same as {{Template:Test}}; EX.WIKT:android; japanese and {{:Template:ja/script}}
		fxt.ini_defn_clear();
		fxt.ini_defn_add("Test_1", "{{#if:|y|n}}");	// NOTE: must be of form "Test 1"; test_1 will fail
		fxt.tst_Parse_tmpl_str("{{:Template:Test 1}}", "n");
		fxt.ini_defn_clear();
	}
	@Test  public void Raw() { // PURPOSE: {{raw:A}} is same as {{A}}; EX.WIKT:android; {{raw:ja/script}}
		fxt.ini_defn_clear();
		fxt.ini_defn_add("Test 1", "{{#if:|y|{{{1}}}}}");
		fxt.tst_Parse_tmpl_str("{{raw:Test 1|a}}", "a");
		fxt.ini_defn_clear();
	}
	@Test  public void Special() { // PURPOSE: {{Special:Whatlinkshere}} is same as [[:Special:Whatlinkshere]]; EX.WIKT:android; isValidPageName
		fxt.tst_Parse_page_tmpl_str("{{Special:Whatlinkshere}}", "[[:Special:Whatlinkshere]]");
	}
	@Test  public void Special_arg() { // PURPOSE: make sure Special still works with {{{1}}}
		fxt.ini_defn_clear();
		fxt.ini_defn_add("Test1", "{{Special:Whatlinkshere/{{{1}}}}}");
		fxt.tst_Parse_tmpl_str("{{Test1|-1}}", "[[:Special:Whatlinkshere/-1]]");
		fxt.ini_defn_clear();
	}
	@Test  public void Raw_special() { // PURPOSE: {{raw:A}} is same as {{A}}; EX.WIKT:android; {{raw:ja/script}}
		fxt.tst_Parse_tmpl_str("{{raw:Special:Whatlinkshere}}", "[[:Special:Whatlinkshere]]");
		fxt.ini_defn_clear();
	}
	@Test  public void Lnki_has_invk_end() {// PURPOSE: [[A|bcd}}]] should not break enclosing templates; EX.CM:Template:Protect
		fxt.tst_Parse_page_tmpl_str(String_.Concat_lines_nl_skipLast
			(	"{{#switch:y"
			,	"  |y=[[A|b}}]]"
			,	"  |#default=n"
			,	"}}"
			),	"[[A|b}}]]");		
	}
	@Test  public void Lnki_has_prm_end() {// PURPOSE: [[A|bcd}}}]] should not break enclosing templates; EX.CM:Template:Protect
		fxt.tst_Parse_page_tmpl_str(String_.Concat_lines_nl_skipLast
			(	"{{#switch:y"
			,	"  |y=[[A|b}}}]]"
			,	"  |#default=n"
			,	"}}"
			),	"[[A|b}}}]]");		
	}
	@Test  public void Tmpl_overrides_pfunc() { // PURPOSE: {{plural|}} overrides {{plural:}}
		fxt.ini_defn_clear();
		fxt.ini_defn_add("plural", "{{{1}}}");
		fxt.tst_Parse_tmpl_str("{{plural|a}}"		, "a");
		fxt.tst_Parse_tmpl_str("{{plural:2|a|b}}"	, "b");	// make sure pfunc still works
		fxt.ini_defn_clear();
	}
	@Test  public void Tmpl_aliases() { // PURPOSE: handled aliases for Template ns
		fxt.Wiki().Ns_mgr().Add_alias(Xow_ns_.Id_template, "TemplateAlias");
		fxt.Wiki().Ns_mgr().Ords_sort();
		fxt.ini_defn_clear();
		fxt.ini_defn_add("tmpl_key", "tmpl_val");
		fxt.tst_Parse_tmpl_str("{{TemplateAlias:tmpl_key}}"		, "tmpl_val");
		fxt.ini_defn_clear();
	}
	@Test  public void Tmpl_aliases_2() { // PURPOSE: handled aliases for other ns; DATE:2013-02-08
		fxt.Wiki().Ns_mgr().Add_alias(Xow_ns_.Id_project, "WP");
		fxt.Wiki().Ns_mgr().Ords_sort();
		fxt.ini_defn_clear();
		fxt.ini_page_create("Project:tmpl_key", "tmpl_val");
		fxt.tst_Parse_tmpl_str("{{WP:tmpl_key}}"		, "tmpl_val");
		fxt.ini_defn_clear();
	}
	@Test  public void Transclude() { // PURPOSE: transclusion test with arguments
		fxt.ini_defn_clear();
		fxt.ini_page_create("PageToTransclude", "a{{{key}}}c");
		fxt.tst_Parse_tmpl_str("some text to make this page longer than transclusion {{:PageToTransclude|key=b}}"	, "some text to make this page longer than transclusion abc");
		fxt.ini_defn_clear();
	}
	@Test  public void Template_loop_across_namespaces() {// PURPOSE: {{Institution:Louvre}} results in template loop b/c it calls {{Louvre}}; EX: c:Mona Lisa
		fxt.ini_page_create("Template:Test", "test");
		fxt.ini_page_create("Category:Test", "{{Test}}");
		fxt.tst_Parse_page_all_str("{{Category:Test}}", "test");
	}
	@Test  public void Closing_braces_should_not_extend_beyond_lnki() {	// PURPOSE: extra }} should not close any frames beyond lnki; EX:w:Template:Cite wikisource; w:John Fletcher (playwright)
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_b", "{{{test_b_0|}}}");
		fxt.ini_defn_add("test_a", "{{test_b|test_b_0=[[{{{test_a_0}}}}}]]}}");	// NOTE: extra 2 }}; should render literally and not close "{{test_b"
		fxt.tst_Parse_tmpl_str("{{test_a|test_a_0=1}}"	, "[[1}}]]");
		fxt.ini_defn_clear();
	}
	@Test  public void Trim_ws_on_sub_tmpls() {	// PURPOSE: ws should be trimmed on eval tkns; EX:w:Lackawanna Cut-Off; {{Lackawanna Cut-Off}}
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_b", "\n\nb\n\n");
		fxt.ini_defn_add("test_a", "a{{test_b}}c");
		fxt.tst_Parse_tmpl_str("{{test_a}}"	, "a\n\nbc");
		fxt.ini_defn_clear();
	}
	@Test   public void Nowiki_tblw() {	// PURPOSE: nowiki does not exclude sections with pipe; will cause tables to fail; EX: de.wikipedia.org/wiki/Hilfe:Vorlagenprogrammierung
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
		(	"{|"
		,	"|-"
		,	"|<nowiki>{{ #time:M|Jan}}</nowiki>"
		,	"|}"
		), String_.Concat_lines_nl_skipLast
		(	"<table>"
		,	"  <tr>"	
		,	"    <td>{{ #time:M|Jan}}"	
		,	"    </td>"	
		,	"  </tr>"	
		,	"</table>"
		,	""
		));
	}
	@Test   public void Template_plus_other_ns() {  // PURPOSE.fix: Template:Wikipedia:A was transcluding "Wikipedia:A" instead of "Template:Wikipedia:A"; DATE:2013-04-03  
		fxt.ini_defn_clear();
		fxt.ini_page_create("Template:Wikipedia:A"				, "B");
		fxt.tst_Parse_tmpl_str("{{Template:Wikipedia:A}}"		, "B");
		fxt.ini_defn_clear();
	}
	@Test  public void Transcluded_redirect() {		// PURPOSE: StackOverflowError when transcluded sub-page redirects back to root_page; DATE:2014-01-07
		fxt.ini_page_create("Root/Leaf", "#REDIRECT [[Root]]");
		fxt.ini_page_create("Root", "<gallery>A.png|a{{/Leaf}}b</gallery>");		// NOTE: gallery neeeded for XOWA to fail; MW fails if just {{/Leaf}}
		fxt.Test_parse_page("Root", "<gallery>A.png|a{{/Leaf}}b</gallery>");
	}
}
/*
NOTE_1: function should expand "*a" to "\n*a" even if "*a" is bos
consider following
Template:Test with text of "#a"
a) "a{{test}}" would return "a\n#a" b/c of rule for auto-adding \n
b) bug was that "{{test}}" would return "#a" b/c "#a" was at bos which would expand to list later
   however, needs to be "\n#a" b/c appended to other strings wherein bos would be irrelevant.
Actual situation was very complicated. EX.WP:Rome
*/
