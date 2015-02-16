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
package gplx.xowa.apps.fsys; import gplx.*; import gplx.xowa.*; import gplx.xowa.apps.*;
public class Launcher_app_mgr implements GfoInvkAble {
	public Launcher_app_mgr(Xoa_app app) {this.app = app;} private Xoa_app app;
	public void Init() {
		Xoa_fsys_eval cmd_eval = app.Url_cmd_eval();
		ProcessAdp.ini_(this, app.Gui_wtr(), app_query_img_size			, cmd_eval, ProcessAdp.Run_mode_sync_timeout	, 10 * 60, "~{<>bin_plat_dir<>}imagemagick\\identify", "-ping -format \"<{%w,%h}>\" \"~{file}\"", "file");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_resize_img				, cmd_eval, ProcessAdp.Run_mode_sync_timeout	, 10 * 60, "~{<>bin_plat_dir<>}imagemagick\\convert", "\"~{source}\" -coalesce -resize ~{width}x~{height} \"~{target}\"", "source", "target", "width", "height");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_convert_svg_to_png		, cmd_eval, ProcessAdp.Run_mode_sync_timeout	, 10 * 60, "~{<>bin_plat_dir<>}inkscape\\inkscape", "-z -w ~{width} -f \"~{source}\" -e \"~{target}\"", "source", "target", "width").Thread_kill_name_("inkscape.exe");	// // -z=without-gui; -w=width; -f=file -e=export-png
		ProcessAdp.ini_(this, app.Gui_wtr(), app_convert_tex_to_dvi		, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  2 * 60, "~{<>bin_plat_dir<>}miktex\\miktex\\bin\\latex", "-quiet -output-directory=~{temp_dir} -job-name=xowa_temp ~{tex_file}", "tex_file", "temp_dir");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_convert_dvi_to_png		, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  2 * 60, "~{<>bin_plat_dir<>}miktex\\miktex\\bin\\dvipng", "~{dvi_file} -o ~{png_file} -q* -T tight -bg Transparent", "dvi_file", "png_file", "temp_dir");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_convert_djvu_to_tiff	, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  1 * 60, "~{<>bin_plat_dir<>}djvulibre\\ddjvu", "-format=tiff -page=1 \"~{source}\" \"~{target}\"", "source", "target");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_decompress_bz2			, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  0	 , "~{<>bin_plat_dir<>}7-zip\\7za", "x -y \"~{src}\" -o\"~{trg_dir}\"", "src", "trg", "trg_dir");	// x=extract; -y=yes on all queries; -o=output_dir
		ProcessAdp.ini_(this, app.Gui_wtr(), app_decompress_bz2_by_stdout, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  0	 , "~{<>bin_plat_dir<>}7-zip\\7za", "x -so \"~{src}\"", "src");	// x=extract; -so=stdout
		ProcessAdp.ini_(this, app.Gui_wtr(), app_decompress_zip			, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  0	 , "~{<>bin_plat_dir<>}7-zip\\7za", "x -y \"~{src}\" -o\"~{trg_dir}\"", "src", "trg", "trg_dir");	// x=extract; -y=yes on all queries; -o=output_dir
		ProcessAdp.ini_(this, app.Gui_wtr(), app_decompress_gz			, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  0	 , "~{<>bin_plat_dir<>}7-zip\\7za", "x -y \"~{src}\" -o\"~{trg_dir}\"", "src", "trg", "trg_dir");	// x=extract; -y=yes on all queries; -o=output_dir
		ProcessAdp.ini_(this, app.Gui_wtr(), app_lua					, cmd_eval, ProcessAdp.Run_mode_async			,  0	 , "~{<>bin_plat_dir<>}lua\\lua", "", "");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_lilypond				, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  1 * 60, "~{<>bin_plat_dir<>}lilypond\\usr\\bin\\lilypond.exe", "\"-dsafe=#t\" -dbackend=ps --png --header=texidoc -dmidi-extension=midi \"~{file}\"", "file");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_abc2ly					, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  1 * 60, "~{<>bin_plat_dir<>}lilypond\\usr\\bin\\python.exe", "abc2ly.py -s \"--output=~{target}\" \"~{source}\"", "source", "target");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_trim_img				, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  1 * 60, "~{<>bin_plat_dir<>}imagemagick\\convert", "-trim \"~{source}\"  \"~{target}\"", "source", "target");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_convert_midi_to_ogg	, cmd_eval, ProcessAdp.Run_mode_sync_timeout	,  1 * 60, "~{<>bin_plat_dir<>}timidity\\timidity", "-Ov \"--output-file=~{target}\" \"~{source}\"", "source", "target");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_view_web				, cmd_eval, ProcessAdp.Run_mode_async			,  0	 , "cmd", "/c start \"~{url}\"", "url");
		ProcessAdp.ini_(this, app.Gui_wtr(), app_view_text				, cmd_eval, ProcessAdp.Run_mode_async			,  0	 , "cmd", "/c start \"~{url}\"", "url");
		int cmds_view_file_by_ext_len = cmds_view_file_by_ext.length;
		for (int i= 0; i < cmds_view_file_by_ext_len; i++) {
			ProcessAdp cmd = new ProcessAdp();
			cmds_view_file_by_ext [i] = cmd;
			ProcessAdp.ini_(this, app.Gui_wtr(), cmd					, cmd_eval, ProcessAdp.Run_mode_async		,  0	, "cmd", "/c start \"~{file}\"", "file");
		}
	}
	public ProcessAdp App_query_img_size()				{return app_query_img_size;}			private ProcessAdp app_query_img_size = new ProcessAdp();
	public ProcessAdp App_resize_img()					{return app_resize_img;}				private ProcessAdp app_resize_img = new ProcessAdp();
	public ProcessAdp App_convert_svg_to_png()			{return app_convert_svg_to_png;}		private ProcessAdp app_convert_svg_to_png = new ProcessAdp();
	public ProcessAdp App_convert_tex_to_dvi()			{return app_convert_tex_to_dvi;}		private ProcessAdp app_convert_tex_to_dvi = new ProcessAdp();
	public ProcessAdp App_convert_dvi_to_png()			{return app_convert_dvi_to_png;}		private ProcessAdp app_convert_dvi_to_png = new ProcessAdp();
	public ProcessAdp App_convert_djvu_to_tiff()		{return app_convert_djvu_to_tiff;}		private ProcessAdp app_convert_djvu_to_tiff = new ProcessAdp();
	public ProcessAdp App_view_web()					{return app_view_web;}					private ProcessAdp app_view_web = new ProcessAdp();
	public ProcessAdp App_view_text()					{return app_view_text;}					private ProcessAdp app_view_text = new ProcessAdp();
	public ProcessAdp App_decompress_bz2()				{return app_decompress_bz2;}			private ProcessAdp app_decompress_bz2 = new ProcessAdp();
	public ProcessAdp App_decompress_zip()				{return app_decompress_zip;}			private ProcessAdp app_decompress_zip = new ProcessAdp();
	public ProcessAdp App_decompress_gz()				{return app_decompress_gz;}				private ProcessAdp app_decompress_gz  = new ProcessAdp();
	public ProcessAdp App_decompress_bz2_by_stdout()	{return app_decompress_bz2_by_stdout;}	private ProcessAdp app_decompress_bz2_by_stdout = new ProcessAdp();
	public ProcessAdp App_lua()							{return app_lua;}						private ProcessAdp app_lua = new ProcessAdp();
	public ProcessAdp App_lilypond()					{return app_lilypond;}					private ProcessAdp app_lilypond = new ProcessAdp();
	public ProcessAdp App_abc2ly()						{return app_abc2ly;}					private ProcessAdp app_abc2ly = new ProcessAdp();
	public ProcessAdp App_trim_img()					{return app_trim_img;}					private ProcessAdp app_trim_img = new ProcessAdp();
	public ProcessAdp App_convert_midi_to_ogg()			{return app_convert_midi_to_ogg;}		private ProcessAdp app_convert_midi_to_ogg = new ProcessAdp();
	public ProcessAdp App_by_ext(String ext)			{return App_by_ext_key(String_.Mid(ext, 1));}	// ignore 1st . in ext; EX: ".png" -> "png"
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_query_img_size))				return app_query_img_size;
		else if	(ctx.Match(k, Invk_resize_img))					return app_resize_img;
		else if	(ctx.Match(k, Invk_convert_svg_to_png))			return app_convert_svg_to_png;
		else if	(ctx.Match(k, Invk_convert_tex_to_dvi))			return app_convert_tex_to_dvi;
		else if	(ctx.Match(k, Invk_convert_dvi_to_png))			return app_convert_dvi_to_png;
		else if	(ctx.Match(k, Invk_convert_djvu_to_tiff))		return app_convert_djvu_to_tiff;
		else if	(ctx.Match(k, Invk_view_web))					return app_view_web;
		else if	(ctx.Match(k, Invk_decompress_bz2))				return app_decompress_bz2;
		else if	(ctx.Match(k, Invk_decompress_zip))				return app_decompress_zip;
		else if	(ctx.Match(k, Invk_decompress_gz))				return app_decompress_gz;
		else if	(ctx.Match(k, Invk_decompress_bz2_by_stdout))	return app_decompress_bz2_by_stdout;
		else if	(ctx.Match(k, Invk_lua))						return app_lua;
		else if	(ctx.Match(k, Invk_lilypond))					return app_lilypond;
		else if	(ctx.Match(k, Invk_abc2ly))						return app_abc2ly;
		else if	(ctx.Match(k, Invk_convert_midi_to_ogg))		return app_convert_midi_to_ogg;
		else if	(ctx.Match(k, Invk_trim_img))					return app_trim_img;
		else if	(ctx.Match(k, Invk_web))						return app_view_web;
		else if	(ctx.Match(k, Invk_text))						return app_view_text;
		else if	(ctx.Match(k, Invk_image))						return Init_by_exts("png", "jpg", "jpeg", "gif", "tif", "tiff", "svg");
		else if	(ctx.Match(k, Invk_media))						return Init_by_exts("mid", "ogg", "oga", "ogv", "webm");
		else if	(ctx.Match(k, Invk_svg))						return Init_by_exts("svg");
		else if	(ctx.Match(k, Invk_pdf))						return Init_by_exts("pdf");
		else if	(ctx.Match(k, Invk_djvu))						return Init_by_exts("djvu");
		else if	(ctx.Match(k, Invk_view_by_ext))				Exec_view_by_ext(m.ReadStr("exts"), m.ReadStr("cmd"), m.ReadStr("args"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public void Exec_view_web(byte[] url) {
		url = Bry_.Replace(url, Quote_normal, Quote_escape); // escape quotes; DATE:2013-03-31
		String url_str = String_.new_utf8_(url);
		url_str = ProcessAdp.Escape_ampersands_if_process_is_cmd(Op_sys.Cur().Tid_is_wnt(), app_view_web.Exe_url().Raw(), url_str);	// escape ampersands; DATE:2014-05-20
		app_view_web.Run(url_str);
	}	private static final byte[] Quote_normal = new byte[] {Byte_ascii.Quote}, Quote_escape = new byte[] {Byte_ascii.Quote, Byte_ascii.Quote};
	private ProcessAdp App_by_ext_key(String ext) {return cmds_view_file_by_ext[Xof_ext_.Get_id_by_ext_(Bry_.new_ascii_(ext))];}
	public void Exec_view_by_ext(String exts_raw, String cmd, String args) {
		String[] exts_ary = String_.Split(exts_raw, '|');
		int exts_ary_len = exts_ary.length;
		for (int i = 0; i < exts_ary_len; i++) 
			App_by_ext_key(exts_ary[i]).Cmd_args(cmd, args);
	}	ProcessAdp[] cmds_view_file_by_ext = new ProcessAdp[Xof_ext_.Id__max];
	private ProcessAdp Init_by_exts(String... exts) {
		ProcessAdp rv = App_by_ext_key(exts[0]);
		int len = exts.length;
		for (int i = 0; i < len; i++) {
			cmds_view_file_by_ext[Xof_ext_.Get_id_by_ext_(Bry_.new_ascii_(exts[i]))] = rv;
		}
		return rv;
	}
	private static final String Invk_query_img_size = "query_img_size", Invk_resize_img = "resize_img", Invk_convert_svg_to_png = "convert_svg_to_png", Invk_convert_tex_to_dvi = "convert_tex_to_dvi", Invk_convert_dvi_to_png = "convert_dvi_to_png"
		, Invk_convert_djvu_to_tiff = "convert_djvu_to_tiff", Invk_view_web = "view_web"
		, Invk_decompress_bz2 = "decompress_bz2", Invk_decompress_zip = "decompress_zip", Invk_decompress_gz = "decompress_gz", Invk_decompress_bz2_by_stdout = "decompress_bz2_by_stdout"
		, Invk_view_by_ext = "view_by_ext"
		, Invk_lua = "lua", Invk_lilypond = "lilypond", Invk_abc2ly = "abc2ly", Invk_trim_img = "trim_img", Invk_convert_midi_to_ogg = "convert_midi_to_ogg"
		, Invk_web = "web"
		, Invk_media = "media"
		, Invk_image = "image"
		, Invk_svg = "svg"
		, Invk_pdf = "pdf"
		, Invk_djvu = "djvu"
		, Invk_text = "text"
		;
	public static final int Len_dlm_fld = 1, Adj_next_char = 1;
}
