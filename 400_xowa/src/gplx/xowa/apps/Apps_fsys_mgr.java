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
package gplx.xowa.apps; import gplx.*; import gplx.xowa.*;
public class Apps_fsys_mgr implements GfoInvkAble {
	public Apps_fsys_mgr(Xoa_app app, Io_url root_dir, String bin_dir_name) {
		this.root_dir = root_dir;
		bin_any_dir		= root_dir.GenSubDir("bin").GenSubDir("any");
		bin_plat_dir	= root_dir.GenSubDir("bin").GenSubDir(bin_dir_name);
		file_dir		= root_dir.GenSubDir("file");
		wiki_dir		= root_dir.GenSubDir("wiki");
		temp_dir		= root_dir.GenSubDir("tmp");
		app_mgr			= new Apps_app_mgr(app);
	}
	public Io_url Root_dir()		{return root_dir;} Io_url root_dir;
	public Io_url File_dir()		{return file_dir;} Io_url file_dir;
	public Io_url Wiki_dir()		{return wiki_dir;} public Apps_fsys_mgr Wiki_dir_(Io_url v) {wiki_dir = v; return this;} Io_url wiki_dir;
	public Io_url Temp_dir()		{return temp_dir;} public Apps_fsys_mgr Temp_dir_(Io_url v) {temp_dir = v; return this;} Io_url temp_dir;	// set to /xowa/user/<name>/temp
	public Io_url Bin_any_dir()		{return bin_any_dir;}	Io_url bin_any_dir;
	public Io_url Bin_plat_dir()	{return bin_plat_dir;}	Io_url bin_plat_dir;
	public Io_url Bin_db_dir()		{return bin_any_dir.GenSubDir_nest("sql", "xowa");}
	public Apps_app_mgr App_mgr()	{return app_mgr;} Apps_app_mgr app_mgr;
	public void Init() {
		app_mgr.Init();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_apps))					return app_mgr;
		else if	(ctx.Match(k, Invk_root_dir))				return root_dir;
		else	return GfoInvkAble_.Rv_unhandled;
	}
	private static final String Invk_apps = "apps", Invk_root_dir = "root_dir";
	public static final String DirName_wiki = "wiki", DirName_user = "user";
}
