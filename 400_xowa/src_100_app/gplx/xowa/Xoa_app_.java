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
import gplx.gfui.*; import gplx.xowa.users.*;
public class Xoa_app_ {
	public static final String App_name = "xowa";
	public static final Gfo_msg_grp Nde = Gfo_msg_grp_.prj_(App_name);
	public static void Run(String... args) {
		Gfo_usr_dlg_xowa usr_dlg = Gfo_usr_dlg_xowa.console_();
		try {Run_args(usr_dlg, args);}
		catch (Exception e) {
			String err_str = Err_.Message_gplx(e);
			Gfo_log_wtr log_wtr = usr_dlg.Log_wtr();
			log_wtr.Log_err(err_str);
			ConsoleAdp._.WriteLine(err_str);
			if (log_wtr.Log_dir() == null) log_wtr.Log_dir_(Env_.AppUrl().OwnerDir().GenSubFil("xowa.log"));
			log_wtr.Queue_enabled_(false);
		}
	}
	private static void Run_args(Gfo_usr_dlg_xowa usr_dlg, String[] args) {usr_dlg.Log_wtr().Log_msg_to_session_fmt("app diags: version=~{0};", Xoa_app_.Version_str);
		GfuiEnv_.Init_swt(args, "xowa.jar", Xoa_app_.class); usr_dlg.Log_wtr().Log_msg_to_session_fmt("sys diags: jar url=~{0}; op_sys=~{1}", Env_.AppUrl(), Op_sys.Cur().Xto_str());
		App_cmd_mgr mgr = new App_cmd_mgr();
		String chkpoint = "boot";
		try {
			DateAdp modified_time = JarAdp_.ModifiedTime_type(Xoa_app_.class).XtoUtc();
			Build_date_str = modified_time.XtoStr_fmt("yyyy-MM-dd HH:mm"); usr_dlg.Log_wtr().Log_msg_to_session_fmt("build_date=~{0};", Build_date_str);
			String hdr = String_.Concat_lines_nl_skipLast
			(	Env_.GenHdr(false, "XOWA", "XOWA: the XOWA Offline Wiki Application\n", "")
			,	String_.Repeat("-", 80) 
			,	""
			,	"version: " + Version_str + "; build date: " + Build_date_str
			);
			mgr.Expd_add_many
			(	App_cmd_arg.opt_("root_dir").Example_url_("C:\\xowa").Note_("root directory for xowa; defaults to current directory of xowa.jar")
			,	App_cmd_arg.opt_("user_dir").Example_url_("C:\\xowa\\user\\" + Xou_user.Key_xowa_user).Note_("directory for user_data; defaults to '/xowa/user/" + Xou_user.Key_xowa_user + "'")
			,	App_cmd_arg.opt_("wiki_dir").Example_url_("C:\\xowa\\wiki\\").Note_("directory for wikis; defaults to '/xowa/wiki/'")
			,	App_cmd_arg.opt_("bin_dir_name").Example_("windows").Note_("platform-dependent directory name inside /xowa/bin/; valid values are 'linux', 'macosx', 'windows', 'linux_64', 'macosx_64', 'windows_64'; defaults to detected version")
			,	App_cmd_arg.opt_("app_mode").Example_("gui").Note_("type of app to run; valid values are 'gui' or 'cmd'; defaults to 'gui'")
			,	App_cmd_arg.opt_("cmd_file").Example_url_("C:\\xowa\\xowa.gfs").Note_("file_path of script to execute; defaults to 'xowa.gfs'")
			,	App_cmd_arg.opt_("cmd_text").Example_("\"app.shell.fetch_page('en.wikipedia.org/wiki/Earth', 'html');\"").Note_("script to run; runs after cmd_file; does nothing if empty; default is empty.\nCurrently a useful cmd is to do 'java -jar xowa_your_platform.jar --app_mode cmd --show_license n --show_args n --cmd_text \"app.shell.fetch_page('en.wikipedia.org/wiki/Earth' 'html');\"'. This will output the page's html to the console. You can also change 'html' to 'wiki' to get the wikitext.")
			,	App_cmd_arg.opt_("url").Example_("en.wikipedia.org/wiki/Earth").Note_("url to be shown when xowa first launches; default is home/wiki/Main_Page")
			,	App_cmd_arg.opt_("server_port_recv").Example_("55000").Note_("applies to --app_mode server; port where xowa server will receive messages; clients should send messages to this port")
			,	App_cmd_arg.opt_("server_port_send").Example_("55001").Note_("applies to --app_mode server; port where xowa server will send messages; clients should listen for messages from this port")
			,	App_cmd_arg.sys_header_("show_license").Dflt_(true)
			,	App_cmd_arg.sys_args_("show_args").Dflt_(true)
			,	App_cmd_arg.sys_help_()
			)
			.Fmt_hdr_(hdr)
			;	chkpoint = "fmt_hdr";
			boolean pass = mgr.Args_process(args); chkpoint = "args_process";
			mgr.Print_header(usr_dlg);	// always call print_header
			if (!pass) {
				mgr.Print_args(usr_dlg);
				mgr.Print_fail(usr_dlg);
				mgr.Print_help(usr_dlg, App_name);
				return;
			}
			if (mgr.Args_has_help()) {
				mgr.Print_help(usr_dlg, App_name);
				return;
			}
			if (mgr.Args_get("show_args").Val_as_bool())
				mgr.Print_args(usr_dlg);
		} catch (Exception e) {usr_dlg.Warn_many(GRP_KEY, "args_failed", "args failed: ~{0} ~{1}", chkpoint, Err_.Message_gplx(e)); return;}

		boolean app_mode_gui = false;
		Xoa_app app = null;
		try {
			Io_url jar_dir = Env_.AppUrl().OwnerDir();
			Io_url root_dir = mgr.Args_get("root_dir").Val_as_url_rel_dir_or(jar_dir, jar_dir);
			Io_url user_dir = mgr.Args_get("user_dir").Val_as_url_rel_dir_or(root_dir.GenSubDir("user"), root_dir.GenSubDir_nest("user", Xou_user.Key_xowa_user));
			Io_url wiki_dir = mgr.Args_get("wiki_dir").Val_as_url_rel_dir_or(root_dir.GenSubDir("wiki"), root_dir.GenSubDir("wiki"));
			Io_url cmd_file = mgr.Args_get("cmd_file").Val_as_url_rel_fil_or(jar_dir, root_dir.GenSubFil("xowa.gfs"));
			String app_mode = mgr.Args_get("app_mode").Val_as_str_or("gui");
			String launch_url = mgr.Args_get("url").Val_as_str_or(Xoa_sys_cfg.Launch_url_dflt);
			int server_port_recv = mgr.Args_get("server_port_recv").Val_as_int_or(55000);
			int server_port_send = mgr.Args_get("server_port_send").Val_as_int_or(55001);
			Op_sys_str = mgr.Args_get("bin_dir_name").Val_as_str_or(Bin_dir_name());
			User_agent = String_.Format("XOWA/{0} ({1}) [gnosygnu@gmail.com]", Xoa_app_.Version_str, Xoa_app_.Op_sys_str);
			String cmd_text = mgr.Args_get("cmd_text").Val_as_str_or(null);
			chkpoint = "args_extract";
			app = new Xoa_app(usr_dlg, root_dir, user_dir, Op_sys_str); usr_dlg.Log_wtr().Queue_enabled_(false);
			app.Fsys_mgr().Wiki_dir_(wiki_dir);
			try {
				app.Sys_cfg().Lang_(System_lang()); chkpoint = "lang";
				app.Sys_cfg().Launch_url_(launch_url); chkpoint = "url";
				app.Server().Rdr_port_(server_port_recv).Wtr_port_(server_port_send);
				app.Init(); chkpoint = "init_gfs";
			}
			catch (Exception e) {
				usr_dlg.Warn_many(GRP_KEY, "app_init_failed", "app init failed: ~{0} ~{1}", chkpoint, Err_.Message_gplx(e));
			}
			app_mode_gui = String_.Eq(app_mode, "gui");
			app.Gui_wtr().Log_wtr_(app.Log_wtr());	// NOTE: log_wtr must be set for cmd-line (else process will fail);
			try {
				app.Gfs_mgr().Run_url(cmd_file); chkpoint = "run_url";
			}
			catch (Exception e) {
				usr_dlg.Warn_many(GRP_KEY, "script_file_failed", "script file failed: ~{0} ~{1} ~{2}", chkpoint, cmd_file.Raw(), Err_.Message_gplx(e));
				if (app_mode_gui)
					GfuiEnv_.ShowMsg(Err_.Message_gplx_brief(e));
			}
			app.Launch(); chkpoint = "launch";
			if (String_.Eq(app_mode, "server")) {
				app.Server().Run();
			}
			else {
				if (cmd_text != null)
					ConsoleAdp._.WriteLine_utf8(Object_.XtoStr_OrEmpty(app.Gfs_mgr().Run_str(cmd_text)));
				if (app_mode_gui) {
					app.Gui_mgr().Run(); chkpoint = "run";
				}
				else {	// teardown app, else lua will keep process running
					if (gplx.xowa.xtns.scribunto.Scrib_engine.Engine() != null) gplx.xowa.xtns.scribunto.Scrib_engine.Engine().Term();
				}
			}
		}
		catch (Exception e) {
			usr_dlg.Warn_many(GRP_KEY, "app_launch_failed", "app launch failed: ~{0} ~{1}", chkpoint, Err_.Message_gplx(e));
		}
		finally {
			if (app != null && app_mode_gui)	// only cancel if app_mode_gui is true; (force cmd_line to end process)
				app.Setup_mgr().Cmd_mgr().Canceled_y_();
		}
	}
	private static String Bin_dir_name() {
		Op_sys op_sys = Op_sys.Cur();
		String rv = "";
		switch (op_sys.Tid()) {
			case Op_sys.Tid_lnx: rv = "linux"; break;
			case Op_sys.Tid_wnt: rv = "windows"; break;
			case Op_sys.Tid_osx: rv = "macosx"; break;
			default: throw Err_mgr._.unhandled_("unknown platform " + Op_sys.Cur());
		}
		if (op_sys.Bitness() == Op_sys.Bitness_64) rv += "_64";
		return rv;
	}
	private static byte[] System_lang() {
		String lang_code = Env_.Env_prop__user_language();
		byte[] lang_code_bry = ByteAry_.new_ascii_(lang_code);
		Xol_lang_itm lang_itm = Xol_lang_itm_.Get_by_key(lang_code_bry);
		return lang_itm == null ? Xol_lang_.Key_en : lang_itm.Key();
	}
	@gplx.Internal protected static void Run_ui(Xoa_app app) {
		String chkpoint = "init";
		try {
			Xoa_gui_mgr ui_mgr = app.Gui_mgr();
			Gfui_kit kit = ui_mgr.Kit();
			ui_mgr.Kit_(kit); chkpoint = "Kit_";
			Xog_win main_win = ui_mgr.Main_win();
			Xog_win_.Init_desktop(main_win); chkpoint = "init_desktop";
			main_win.Launch(); chkpoint = "launch";
			app.User().Prefs_mgr().Launch(); chkpoint = "prefs";
			app.User().Cfg_mgr().Setup_mgr().Setup_run_check(app); chkpoint = "setup_";
			kit.Kit_run(); chkpoint = "run";
		} catch (Exception e) {
			app.Usr_dlg().Warn_many(GRP_KEY, "run_failed", "run_failed: ~{0} ~{1}", chkpoint, Err_.Message_gplx(e));
			if (app.Gui_mgr().Kit() != null) app.Gui_mgr().Kit().Ask_ok("", "", Err_.Message_gplx(e));
		}
	}
	public static final String Version_str = "0.11.1.0";
	public static String Build_date_str = "2012-12-30 00:00:00";
	public static final byte[] Version_bry = ByteAry_.new_ascii_(Version_str);
	public static String User_agent = "";
	public static String Op_sys_str;
	static final String GRP_KEY = "xowa.app_";
	public static final byte[] Pipe_bry = new byte[] {Byte_ascii.Pipe};
}	
