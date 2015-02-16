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
import gplx.dbs.*;
import gplx.gfui.*; import gplx.xowa.users.*;
import gplx.xowa.hdumps.*; import gplx.xowa.hdumps.core.*;
public class Xoa_app_ {
	public static void Run(String... args) {
		Xoa_app_boot_mgr boot_mgr = new Xoa_app_boot_mgr();
		boot_mgr.Run(args);
	}
	public static final String Name = "xowa";
	public static final String Version = "2.2.2.1";
	public static String Build_date = "2012-12-30 00:00:00";
	public static String Op_sys;
	public static String User_agent = "";
	public static final Gfo_msg_grp Nde = Gfo_msg_grp_.prj_(Name);
	public static Gfo_usr_dlg usr_dlg_console_() {
		Gfo_usr_dlg rv = new Gfo_usr_dlg_base();
		rv.Ui_wkr_(Gfo_usr_dlg_ui_.Console);
		rv.Log_wtr_(new Gfo_log_wtr_base());
		rv.Log_wtr().Queue_enabled_(true);
		return rv;
	}
	public static final byte Mode_console = 0, Mode_gui = 1, Mode_http = 2;
}	
class Xoa_app_boot_mgr {
	private Gfo_usr_dlg usr_dlg; private Gfo_log_wtr log_wtr; private String chkpoint = "null";
	public void Run(String[] args) {
		try {
			if (!Init_env(args)) return;
			App_cmd_mgr args_mgr = Init_args_mgr(args); if (args_mgr == null) return;
			Run_app(args_mgr);
		}
		catch (Exception e) {
			String err_str = Err_.Message_gplx(e);
			log_wtr.Log_err(err_str);
			ConsoleAdp._.WriteLine(err_str);
			if (log_wtr.Log_dir() == null) log_wtr.Log_dir_(Env_.AppUrl().OwnerDir().GenSubFil("xowa.log"));
			log_wtr.Queue_enabled_(false);
		}
	}
	private boolean Init_env(String[] args) {
		Gfo_usr_dlg_._ = usr_dlg = Xoa_app_.usr_dlg_console_();
		log_wtr = usr_dlg.Log_wtr(); log_wtr.Log_msg_to_session_fmt("env.init: version=~{0}", Xoa_app_.Version);
		GfuiEnv_.Init_swt(args, Xoa_app_.class); 
		Io_url jar_url = Env_.AppUrl();
		Xoa_app_.Build_date = Io_mgr._.QueryFil(jar_url).ModifiedTime().XtoUtc().XtoStr_fmt("yyyy-MM-dd HH:mm");
		log_wtr.Log_msg_to_session_fmt("env.init: jar_url=~{0}; build_date=~{1}", jar_url.NameAndExt(), Xoa_app_.Build_date);
		log_wtr.Log_msg_to_session_fmt("env.init: op_sys=~{0}", Op_sys.Cur().Xto_str());
		chkpoint = "init_env";
		return true;
	}
	private App_cmd_mgr Init_args_mgr(String[] args) {
		App_cmd_mgr rv = new App_cmd_mgr();
		try {
			String hdr = String_.Concat_lines_nl_skip_last
			(	Env_.GenHdr(false, "XOWA", "XOWA: the XOWA Offline Wiki Application\n", "")
			,	String_.Repeat("-", 80) 
			,	""
			,	"version: " + Xoa_app_.Version + "; build date: " + Xoa_app_.Build_date
			);
			rv
			.Fmt_hdr_(hdr)
			.Expd_add_many
			(	App_cmd_arg.opt_("root_dir").Example_url_("C:\\xowa").Note_("root directory for xowa; defaults to current directory of xowa.jar")
			,	App_cmd_arg.opt_("user_dir").Example_url_("C:\\xowa\\user\\" + Xou_user.Key_xowa_user).Note_("directory for user_data; defaults to '/xowa/user/" + Xou_user.Key_xowa_user + "'")
			,	App_cmd_arg.opt_("wiki_dir").Example_url_("C:\\xowa\\wiki\\").Note_("directory for wikis; defaults to '/xowa/wiki/'")
			,	App_cmd_arg.opt_("bin_dir_name").Example_("windows").Note_("platform-dependent directory name inside /xowa/bin/; valid values are 'linux', 'macosx', 'windows', 'linux_64', 'macosx_64', 'windows_64'; defaults to detected version")
			,	App_cmd_arg.opt_("app_mode").Example_("gui").Note_("type of app to run; valid values are 'gui', 'cmd', 'server', 'http_server'; defaults to 'gui'")
			,	App_cmd_arg.opt_("cmd_file").Example_url_("C:\\xowa\\xowa.gfs").Note_("file_path of script to execute; defaults to 'xowa.gfs'")
			,	App_cmd_arg.opt_("cmd_text").Example_("\"app.shell.fetch_page('en.wikipedia.org/wiki/Earth', 'html');\"").Note_("script to run; runs after cmd_file; does nothing if empty; default is empty.\nCurrently a useful cmd is to do 'java -jar xowa_your_platform.jar --app_mode cmd --show_license n --show_args n --cmd_text \"app.shell.fetch_page('en.wikipedia.org/wiki/Earth' 'html');\"'. This will output the page's html to the console. You can also change 'html' to 'wiki' to get the wikitext.")
			,	App_cmd_arg.opt_("url").Example_("en.wikipedia.org/wiki/Earth").Note_("url to be shown when xowa first launches; default is home/wiki/Main_Page")
			,	App_cmd_arg.opt_("server_port_recv").Example_("55000").Note_("applies to --app_mode server; port where xowa server will receive messages; clients should send messages to this port")
			,	App_cmd_arg.opt_("server_port_send").Example_("55001").Note_("applies to --app_mode server; port where xowa server will send messages; clients should listen for messages from this port")
			,	App_cmd_arg.opt_("http_server_port").Example_("8080").Note_("applies to --app_mode http_server; port used by http_server; default is 8080")
			,	App_cmd_arg.opt_("http_server_home").Example_("home/wiki/Main_Page").Note_("applies to --app_mode http_server; default home page for root address. EX: navigating to localhost:8080 will navigate to localhost:8080/home/wiki/Main_Page")
			,	App_cmd_arg.sys_header_("show_license").Dflt_(true)
			,	App_cmd_arg.sys_args_("show_args").Dflt_(true)
			,	App_cmd_arg.sys_help_()
			)				
			;	chkpoint = "fmt_hdr";
			boolean pass = rv.Args_process(args); chkpoint = "args_process";
			rv.Print_header(usr_dlg);	// always call print_header
			if (!pass) {
				rv.Print_args(usr_dlg);
				rv.Print_fail(usr_dlg);
				rv.Print_help(usr_dlg, Xoa_app_.Name);
				return null;
			}
			if (rv.Args_has_help()) {
				rv.Print_help(usr_dlg, Xoa_app_.Name);
				return null;
			}
			if (rv.Args_get("show_args").Val_as_bool())
				rv.Print_args(usr_dlg);
		}	catch (Exception e) {usr_dlg.Warn_many("", "", "args failed: ~{0} ~{1}", chkpoint, Err_.Message_gplx(e)); return null;}
		return rv;
	}
	private void Run_app(App_cmd_mgr args_mgr) {
		boolean app_mode_gui = false;
		Xoa_app app = null;
		try {
			// init vars
			Io_url jar_dir = Env_.AppUrl().OwnerDir();
			Io_url root_dir = args_mgr.Args_get("root_dir").Val_as_url_rel_dir_or(jar_dir, jar_dir);
			Io_url user_dir = args_mgr.Args_get("user_dir").Val_as_url_rel_dir_or(root_dir.GenSubDir("user"), root_dir.GenSubDir_nest("user", Xou_user.Key_xowa_user));
			Io_url wiki_dir = args_mgr.Args_get("wiki_dir").Val_as_url_rel_dir_or(root_dir.GenSubDir("wiki"), root_dir.GenSubDir("wiki"));
			Io_url cmd_file = args_mgr.Args_get("cmd_file").Val_as_url_rel_fil_or(jar_dir, root_dir.GenSubFil("xowa.gfs"));
			String app_mode = args_mgr.Args_get("app_mode").Val_as_str_or("gui");
			String launch_url = args_mgr.Args_get("url").Val_as_str_or(null);
			int server_port_recv = args_mgr.Args_get("server_port_recv").Val_as_int_or(55000);
			int server_port_send = args_mgr.Args_get("server_port_send").Val_as_int_or(55001);
			int http_server_port = args_mgr.Args_get("http_server_port").Val_as_int_or(8080);
			String http_server_home = args_mgr.Args_get("http_server_home").Val_as_str_or("home/wiki/Main_Page");
			Xoa_app_.Op_sys = args_mgr.Args_get("bin_dir_name").Val_as_str_or(Bin_dir_name());
			Xoa_app_.User_agent = String_.Format("XOWA/{0} ({1}) [gnosygnu@gmail.com]", Xoa_app_.Version, Xoa_app_.Op_sys);
			String cmd_text = args_mgr.Args_get("cmd_text").Val_as_str_or(null);
			app_mode_gui = String_.Eq(app_mode, "gui");

			// init app
			Db_conn_bldr.I.Reg_default_sqlite();
			app = new Xoa_app(usr_dlg, root_dir, user_dir, Xoa_app_.Op_sys); usr_dlg.Log_wtr().Queue_enabled_(false); log_wtr.Log_msg_to_session_fmt("app.init");
			app.Fsys_mgr().Wiki_dir_(wiki_dir);
			try {
				app.Sys_cfg().Lang_(System_lang());
				if (launch_url != null)
					app.Api_root().App().Startup().Tabs().Manual_(launch_url);
				app.Tcp_server().Rdr_port_(server_port_recv).Wtr_port_(server_port_send);
				app.Http_server().Port_(http_server_port);
				app.Http_server().Home_(http_server_home);
				app.Init(); chkpoint = "init_gfs";
			}
			catch (Exception e) {usr_dlg.Warn_many("", "", "app init failed: ~{0} ~{1}", chkpoint, Err_.Message_gplx(e));}
			app.Gui_wtr().Log_wtr_(app.Log_wtr());	// NOTE: log_wtr must be set for cmd-line (else process will fail);

			// run gfs
			gplx.xowa.users.prefs.Prefs_rename_mgr._.Check(app.User().Fsys_mgr().App_data_cfg_user_fil());
			try {app.Gfs_mgr().Run_url(cmd_file); chkpoint = "run_url";}
			catch (Exception e) {
				usr_dlg.Warn_many("", "", "script file failed: ~{0} ~{1} ~{2}", chkpoint, cmd_file.Raw(), Err_.Message_gplx(e));
				if (app_mode_gui)
					GfuiEnv_.ShowMsg(Err_.Message_gplx(e));
			}
			gplx.xowa.apps.setups.Xoa_setup_mgr.Delete_old_files(app);

			// launch
			app.Launch(); chkpoint = "launch";
			if		(String_.Eq(app_mode, "server"))
				app.Tcp_server().Run();
			else if	(String_.Eq(app_mode, "http_server")) {
				app.Mode_(Xoa_app_.Mode_http);
				app.Http_server().Run();
			}
			else {
				if (cmd_text != null)
					ConsoleAdp._.WriteLine_utf8(Object_.Xto_str_strict_or_empty(app.Gfs_mgr().Run_str(cmd_text)));
				if (app_mode_gui) {
					app.Mode_(Xoa_app_.Mode_gui);
					app.Gui_mgr().Run(); chkpoint = "run";
				}
				else	// teardown app, else lua will keep process running
					if (gplx.xowa.xtns.scribunto.Scrib_core.Core() != null) gplx.xowa.xtns.scribunto.Scrib_core.Core().Term();
			}
		}
		catch (Exception e) {usr_dlg.Warn_many("", "", "app launch failed: ~{0} ~{1}", chkpoint, Err_.Message_gplx(e));}
		finally {
			if (app != null && app_mode_gui)	// only cancel if app_mode_gui is true; (force cmd_line to end process)
				app.Setup_mgr().Cmd_mgr().Canceled_y_();
		}
	}
	private static String Bin_dir_name() {
		String rv = "";
		Op_sys op_sys = Op_sys.Cur();
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
		byte[] lang_code_bry = Bry_.new_ascii_(lang_code);
		Xol_lang_itm lang_itm = Xol_lang_itm_.Get_by_key(lang_code_bry);
		return lang_itm == null ? Xol_lang_.Key_en : lang_itm.Key();
	}
}
