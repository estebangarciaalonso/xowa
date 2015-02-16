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
package gplx.ios; import gplx.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.*;
import java.nio.channels.*;
import java.util.Date;

import javax.print.FlavorException;
import javax.tools.JavaCompiler;
import gplx.core.criterias.*;
public class IoEngine_system extends IoEngine_base {
	@Override public String Key() {return IoEngine_.SysKey;}
	@Override public void DeleteDirDeep(IoEngine_xrg_deleteDir args) {utl.DeleteDirDeep(this, args.Url(), args);}
	@Override public void XferDir(IoEngine_xrg_xferDir args) {Io_url trg = args.Trg(); utl.XferDir(this, args.Src(), IoEnginePool._.Fetch(trg.Info().EngineKey()), trg, args);}
	@Override public void XferFil(IoEngine_xrg_xferFil args) {utl.XferFil(this, args);}
	@Override public IoItmDir QueryDirDeep(IoEngine_xrg_queryDir args) {return utl.QueryDirDeep(this, args);}
	@Override public void CopyDir(Io_url src, Io_url trg) {IoEngine_xrg_xferDir.copy_(src, trg).Recur_().Exec();}
	@Override public void MoveDirDeep(IoEngine_xrg_xferDir args) {Io_url trg = args.Trg(); utl.XferDir(this, args.Src(), IoEnginePool._.Fetch(trg.Info().EngineKey()), trg, args);}
	@Override public void DeleteFil_api(IoEngine_xrg_deleteFil args) {
		Io_url url = args.Url();
		File fil = Fil_(url);	
		if (!Fil_Exists(fil)) {
			if (args.MissingFails()) throw IoErr.FileNotFound("delete", url);
			else	return;
		}
		MarkFileWritable(fil, url, args.ReadOnlyFails(), "DeleteFile");
		DeleteFil_lang(fil, url);
	}
		@Override public boolean ExistsFil_api(Io_url url) {
		return new File(url.Xto_api()).exists();
	}
	@Override public void SaveFilText_api(IoEngine_xrg_saveFilStr mpo) {
		Io_url url = mpo.Url();

		// encode string
		byte[] textBytes = null;
		textBytes = Bry_.new_utf8_(mpo.Text());

		FileChannel fc = null; FileOutputStream fos = null;
		if (!ExistsDir(url.OwnerDir())) CreateDir(url.OwnerDir());
		try {
			// open file
			try 	{fos = new FileOutputStream(url.Xto_api(), mpo.Append());}
			catch 	(FileNotFoundException e) {throw Err_Fil_NotFound(e, url);}
			fc = fos.getChannel();
		
			// write text
			try 	{fc.write(ByteBuffer.wrap(textBytes));}
			catch	(IOException e) {
				Closeable_Close(fc, url, false);
				Closeable_Close(fos, url, false);
				throw Err_.err_key_(e, IoEngineArgs._.Err_IoException, "write data to file failed").Add("url", url.Xto_api());
			}
			if (!Op_sys.Cur().Tid_is_drd()) {
				File fil = new File(url.Xto_api());
				IoEngine_system_xtn.SetExecutable(fil, true);
			}
		}
		finally {		
			// cleanup
			Closeable_Close(fc, url, false);
			Closeable_Close(fos, url, false);
		}
	}
	@SuppressWarnings("resource")
	@Override public String LoadFilStr(IoEngine_xrg_loadFilStr args) {
		Io_url url = args.Url();
		
		// get reader for file
		InputStream stream = null;
		try 	{stream = new FileInputStream(url.Xto_api());}
		catch 	(FileNotFoundException e) {
			if (args.MissingIgnored()) return "";
			throw Err_Fil_NotFound(e, url);
		}
		InputStreamReader reader = null;
		try 	{reader = new InputStreamReader(stream, IoEngineArgs._.LoadFilStr_Encoding);}
		catch 	(UnsupportedEncodingException e) {
			Closeable_Close(stream, url, false);
			throw Err_Text_UnsupportedEncoding(IoEngineArgs._.LoadFilStr_Encoding, "", url, e);
		}
		
		// make other objects
	    char[] readerBuffer = new char[IoEngineArgs._.LoadFilStr_BufferSize];
	    int pos = 0;
	    StringWriter sw = new StringWriter();
	    
	    // transfer data 
		while (true) {
		    try 	{pos = reader.read(readerBuffer);}
		    catch	(IOException e) {
			    Closeable_Close(stream, url, false);
			    Closeable_Close(reader, url, false);
		    	throw Err_.err_key_(e, IoEngineArgs._.Err_IoException, "read data from file failed").Add("url", url.Xto_api()).Add("pos", pos);
		    }
		    if (pos == -1) break;
		    sw.write(readerBuffer, 0, pos);
		}
		
		// cleanup
	    Closeable_Close(stream, url, false);
	    Closeable_Close(reader, url, false);
		return sw.toString();
	}
	@Override public boolean ExistsDir(Io_url url) {return new File(url.Xto_api()).exists();}
	@Override public void CreateDir(Io_url url) {new File(url.Xto_api()).mkdirs();}
	@Override public void DeleteDir(Io_url url) {
		File dir = new File(url.Xto_api());
		if (!dir.exists()) return;
		boolean rv = dir.delete();
		if (!rv) throw Err_.new_key_(IoEngineArgs._.Err_IoException, "delete dir failed").Add("url", url.Xto_api());
	}
	@Override public IoItmDir QueryDir(Io_url url) {
		IoItmDir rv = IoItmDir_.scan_(url);
		File dirInfo = new File(url.Xto_api());
		if (!dirInfo.exists()) {
			rv.Exists_set(false);
			return rv;
		}
		IoUrlInfo urlInfo = url.Info();
		File[] subItmAry = dirInfo.listFiles();
		if (subItmAry == null) return rv;	// directory has no files
		for (int i = 0; i < subItmAry.length; i++) {
			File subItm = subItmAry[i];
			if (subItm.isFile()) {
				IoItmFil subFil = QueryMkr_fil(urlInfo, subItm);
				rv.SubFils().Add(subFil);
			}
			else {
				IoItmDir subDir = QueryMkr_dir(urlInfo, subItm);				
				rv.SubDirs().Add(subDir);
			}
		}		
		return rv;
	}
	IoItmFil QueryMkr_fil(IoUrlInfo urlInfo, File apiFil) {
		Io_url filUrl = Io_url_.new_inf_(apiFil.getPath(), urlInfo);	// NOTE: may throw PathTooLongException when url is > 248 (exception messages states 260)
		long fil_len = apiFil.exists() ? apiFil.length() : IoItmFil.Size_Invalid;	// NOTE: if file doesn't exist, set len to -1; needed for "boolean Exists() {return size != Size_Invalid;}"; DATE:2014-06-21
		IoItmFil rv = IoItmFil_.new_(filUrl, fil_len, DateAdp_.MinValue, DateAdp_.unixtime_lcl_ms_(apiFil.lastModified()));
		rv.ReadOnly_(!apiFil.canWrite());
		return rv;
	}
	IoItmDir QueryMkr_dir(IoUrlInfo urlInfo, File apiDir) {
		Io_url dirUrl = Io_url_.new_inf_(apiDir.getPath() + urlInfo.DirSpr(), urlInfo);	// NOTE: may throw PathTooLongException when url is > 248 (exception messages states 260)
		return IoItmDir_.scan_(dirUrl);
	}
	@Override public IoItmFil QueryFil(Io_url url) {
		File fil = new File(url.Xto_api());
		return QueryMkr_fil(url.Info(), fil);
	}
	@Override public void UpdateFilAttrib(Io_url url, IoItmAttrib atr) {
		File f = new File(url.Xto_api());
		boolean rv = true;
		if (atr.ReadOnly() != Fil_ReadOnly(f)) {
			if (atr.ReadOnly())
				rv = f.setReadOnly();
			else {
				if (!Op_sys.Cur().Tid_is_drd())
					IoEngine_system_xtn.SetWritable(f, true);
			}
			if (!rv) throw Err_.new_key_(IoEngineArgs._.Err_IoException, "set file attribute failed")
				.Add("attribute", "readOnly").Add("cur", Fil_ReadOnly(f)).Add("new", atr.ReadOnly()).Add("url", url.Xto_api());			
		}
		if (atr.Hidden() != f.isHidden()) {
			//Runtime.getRuntime().exec("attrib +H myHiddenFile.java");			
		}
	}
	@Override public void UpdateFilModifiedTime(Io_url url, DateAdp modified) {
		File f = new File(url.Xto_api());
		long timeInt = modified.UnderDateTime().getTimeInMillis();
//		if (timeInt < 0) {
//			UsrDlg_._.Notify("{0} {1}", url.Xto_api(), timeInt);
//			return;
//		}
		if (!f.setLastModified(timeInt)) {
			if (Fil_ReadOnly(f)) {
				boolean success = false;
				try {
					UpdateFilAttrib(url, IoItmAttrib.normal_());
					success = f.setLastModified(timeInt);
				}
				finally {
					UpdateFilAttrib(url, IoItmAttrib.readOnly_());
				}
				if (!success) throw Err_.new_("could not update file modified time").Add("url", url.Xto_api()).Add("modifiedTime", modified.XtoStr_gplx_long());
			}
		}
	}
	@Override public IoStream OpenStreamRead(Io_url url) {return IoStream_base.new_(url, IoStream_.Mode_rdr);}
	@Override public IoStream OpenStreamWrite(IoEngine_xrg_openWrite args) {
		Io_url url = args.Url();
		if (!ExistsFil_api(url)) SaveFilText_api(IoEngine_xrg_saveFilStr.new_(url, ""));
		return IoStream_base.new_(url, args.Mode());
	}
	@SuppressWarnings("resource")
	@Override public void CopyFil(IoEngine_xrg_xferFil args) {
		// TODO:JAVA6 hidden property ignored; 1.6 does not allow OS-independent way of setting isHidden (wnt only possible through jni)
		boolean overwrite = args.Overwrite();
		Io_url srcUrl = args.Src(), trgUrl = args.Trg();
		File srcFil = new File(srcUrl.Xto_api()), trgFil = new File(trgUrl.Xto_api());
		if (trgFil.isFile()) {		// trgFil exists; check if overwrite set and trgFil is writable
			Chk_TrgFil_Overwrite(overwrite, trgUrl);
			MarkFileWritable(trgFil, trgUrl, args.ReadOnlyFails(), "copy");
		}		
		else {						// trgFil doesn't exist; must create file first else fileNotFound exception thrown
//			if (overwrite) throw Err_
			boolean rv = true; //Exception exc = null;
			if (!ExistsDir(trgUrl.OwnerDir())) CreateDir(trgUrl.OwnerDir());
			try 	{
				trgFil.createNewFile();
				if (!Op_sys.Cur().Tid_is_drd())
					IoEngine_system_xtn.SetExecutable(trgFil, true);
			}
			catch 	(IOException e) {
//				exc = e;
				rv = false;
			}
			if (!rv)
				throw Err_.new_("create file failed").Add("trg", trgUrl.Xto_api());
		}
		FileInputStream srcStream = null; FileOutputStream trgStream = null;
		FileChannel srcChannel = null, trgChannel = null;
		try {
			// make objects
			try 	{srcStream = new FileInputStream(srcFil);}
			catch 	(FileNotFoundException e) {throw IoErr.FileNotFound("copy", srcUrl);}
			try 	{trgStream = new FileOutputStream(trgFil);}
			catch 	(FileNotFoundException e) {
				trgStream = TryToUnHideFile(trgFil, trgUrl);
				if (trgStream == null)
					throw IoErr.FileNotFound("copy", trgUrl);
//				else
//					wasHidden = true;
			}		
			srcChannel = srcStream.getChannel();		
			trgChannel = trgStream.getChannel();
			
			// transfer data
			long pos = 0, count = 0, read = 0;
			try 	{count = srcChannel.size();}
			catch 	(IOException e) {throw Err_.err_key_(e, IoEngineArgs._.Err_IoException, "size failed").Add("src", srcUrl.Xto_api());}
			int totalBufferSize = IoEngineArgs._.LoadFilStr_BufferSize;
			long transferSize = (count > totalBufferSize) ? totalBufferSize : count;	// transfer as much as fileSize, but limit to LoadFilStr_BufferSize 
			while 	(pos < count) {
				try 	{read = trgChannel.transferFrom(srcChannel, pos, transferSize);}
				catch 	(IOException e) {
					Closeable_Close(srcChannel, srcUrl, false);
					Closeable_Close(trgChannel, trgUrl, false);
					Closeable_Close(srcStream, srcUrl, false);
					Closeable_Close(trgStream, srcUrl, false);
					throw Err_.err_key_(e, IoEngineArgs._.Err_IoException, "transfer data failed").Add("src", srcUrl.Xto_api()).Add("trg", trgUrl.Xto_api());
				}
			    if (read == -1) break;
			    pos += read;
			}
//			if (wasHidden)
//				
		}
		finally {
			// cleanup
			Closeable_Close(srcChannel, srcUrl, false);
			Closeable_Close(trgChannel, trgUrl, false);
			Closeable_Close(srcStream, srcUrl, false);
			Closeable_Close(trgStream, srcUrl, false);
		}
		UpdateFilModifiedTime(trgUrl, QueryFil(srcUrl).ModifiedTime());	// must happen after file is closed
	}
	FileOutputStream TryToUnHideFile(File trgFil, Io_url trgUrl) {
		FileOutputStream trgStream = null;
		if (trgFil.exists()) {	// WORKAROUND: java fails when writing to hidden files; unmark hidden and try again
			Process p = null;
			try {
				String d = "attrib -H \"" + trgUrl.Xto_api() + "\"";
				p = Runtime.getRuntime().exec(d);
			} catch (IOException e1) {
				e1.printStackTrace();
			}					
			try {
				p.waitFor();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try 	{trgStream = new FileOutputStream(trgFil);}
			catch 	(FileNotFoundException e) {
				return null;
			}
		}		
		return trgStream;
	} 
	@Override public void MoveFil(IoEngine_xrg_xferFil args) {
		Io_url srcUrl = args.Src(), trgUrl = args.Trg();
		String src_api = srcUrl.Xto_api(), trg_api = trgUrl.Xto_api();
		if (String_.Eq(src_api, trg_api)) return; 	// ignore command if src and trg is same; EX: C:\a.txt -> C:\a.txt should be noop
		File srcFil = new File(src_api), trgFil = new File(trg_api);
		
		// if drive is same, then rename file
		if (String_.Eq(srcUrl.OwnerRoot().Raw(), trgUrl.OwnerRoot().Raw())) {
			boolean overwrite = args.Overwrite();
			if (!srcFil.exists() && args.MissingFails()) throw IoErr.FileNotFound("move", srcUrl);
			if (trgFil.exists()) {
				Chk_TrgFil_Overwrite(overwrite, trgUrl);
				MarkFileWritable(trgFil, trgUrl, args.ReadOnlyFails(), "move");
				DeleteFil_lang(trgFil, args.Trg());	// overwrite is specified and file is writable -> delete
			}
			if (!ExistsDir(trgUrl.OwnerDir())) CreateDir(trgUrl.OwnerDir());
			srcFil.renameTo(trgFil);
		}
		// else copy fil and delete
		else {
			if (!srcFil.exists() && !args.MissingFails()) return;
			CopyFil(args);
			DeleteFil_lang(srcFil, srcUrl);
		}
	}
	void Chk_TrgFil_Overwrite(boolean overwrite, Io_url trg) {
		if (!overwrite)
			throw Err_.invalid_op_("trgFile exists but overwriteFlag not set").Add("trg", trg.Xto_api());		
	}
	@Override public void MoveDir(Io_url src, Io_url trg) {
		String srcStr = src.Xto_api(), trgStr = trg.Xto_api();
		File srcFil = new File(srcStr), trgFil = new File(trgStr);
		if (trgFil.exists()) 	{throw Err_.invalid_op_("cannot move dir if trg exists").Add("src", src).Add("trg", trg);}
		if (String_.Eq(src.OwnerRoot().Raw(), trg.OwnerRoot().Raw())) {			
			srcFil.renameTo(trgFil);
		}
		else {
			XferDir(IoEngine_xrg_xferDir.copy_(src, trg));
		}
	}
	protected static void Closeable_Close(Closeable closeable, Io_url url, boolean throwErr) {
		if 		(closeable == null) return;
		try 	{closeable.close();}
		catch 	(IOException e) {
			if (throwErr)
				throw Err_.err_key_(e, IoEngineArgs._.Err_IoException, "close object failed").Add("class", ClassAdp_.NameOf_obj(closeable)).Add("url", url.Xto_api());
//			else
//				UsrDlg_._.Finally("failed to close FileChannel", "url", url, "apiErr", Err_.Message_err_arg(e));
		}		
	}

	File Fil_(Io_url url) {return new File(url.Xto_api());}
	boolean Fil_Exists(File fil) {return fil.exists();}
	boolean Fil_ReadOnly(File fil) {return !fil.canWrite();}
	boolean Fil_Delete(File fil) {return fil.delete();}
	void Fil_Writable(File fil) {
		if (!Op_sys.Cur().Tid_is_drd())
			IoEngine_system_xtn.SetWritable(fil, true);
	}
	Err Err_Text_UnsupportedEncoding(String encodingName, String text, Io_url url, Exception e) {
		return Err_.err_key_(e, "gplx.texts.UnsupportedEncodingException", "text is in unsupported encoding").CallLevel_1_()
			.Add("encodingName", encodingName)
			.Add("text", text)
			.Add("url", url.Xto_api())
			;
	}
	boolean user_agent_needs_resetting = true;
	@Override public Io_stream_rdr DownloadFil_as_rdr(IoEngine_xrg_downloadFil xrg) {
		Io_stream_rdr_http rdr = new Io_stream_rdr_http(xrg);
		rdr.Open();
		return rdr;
	}
	@Override public boolean DownloadFil(IoEngine_xrg_downloadFil xrg) {
		IoStream trg_stream = null;
		java.io.BufferedInputStream src_stream = null;
		java.net.URL src_url = null;
		HttpURLConnection src_conn = null;
		if (user_agent_needs_resetting) {user_agent_needs_resetting = false; System.setProperty("http.agent", "");}
		boolean exists = Io_mgr._.ExistsDir(xrg.Trg().OwnerDir());
		Gfo_usr_dlg prog_dlg = null;
		String src_str = xrg.Src();
		Io_download_fmt xfer_fmt = xrg.Download_fmt();
		prog_dlg = xfer_fmt.usr_dlg;
		if (!Web_access_enabled) {
			if (prog_dlg != null) {
				if (session_fil == null) session_fil = prog_dlg.Log_wtr().Session_dir().GenSubFil("internet.txt");
				prog_dlg.Log_wtr().Log_msg_to_url_fmt(session_fil, "download disabled: src='~{0}' trg='~{1}'", xrg.Src(), xrg.Trg().Raw());				
			}
			return false;
		}
		try {
			trg_stream = Io_mgr._.OpenStreamWrite(xrg.Trg());
			src_url = new java.net.URL(src_str);
			src_conn = (HttpURLConnection)src_url.openConnection();
//			src_conn.setReadTimeout(5000);	// do not set; if file does not exist, will wait 5 seconds before timing out; want to fail immediately
			String user_agent = xrg.User_agent(); if (user_agent != null) src_conn.setRequestProperty("User-Agent", user_agent);
			long content_length = Long_.parse_or_(src_conn.getHeaderField("Content-Length"), IoItmFil.Size_Invalid_int);
			xrg.Src_content_length_(content_length);
			if (xrg.Src_last_modified_query())	// NOTE: only files will have last modified (api calls will not); if no last_modified, then src_conn will throw get nullRef; avoid nullRef 
				xrg.Src_last_modified_(DateAdp_.unixtime_lcl_ms_(src_conn.getLastModified()));
			if (xrg.Exec_meta_only()) return true;
	        src_stream = new java.io.BufferedInputStream(src_conn.getInputStream());
	        if (!exists) {
	        	Io_mgr._.CreateDir(xrg.Trg().OwnerDir());	// dir must exist for OpenStreamWrite; create dir at last possible moment in case stream does not exist.
	        }
    		byte[] download_bfr = new byte[Download_bfr_len];	// NOTE: download_bfr was originally member variable; DATE:2013-05-03
    		xfer_fmt.Bgn(content_length);
    		int count = 0;
    		while ((count = src_stream.read(download_bfr, 0, Download_bfr_len)) != -1) {
    			if (xrg.Prog_cancel()) {
    				src_stream.close();
    				trg_stream.Rls();
    				Io_mgr._.DeleteFil(xrg.Trg());
    			}
    			xfer_fmt.Prog(count);
    			trg_stream.Write(download_bfr, 0, count);
    		}
    		if (prog_dlg != null) {
    			xfer_fmt.Term();
    			if (session_fil == null) session_fil = prog_dlg.Log_wtr().Session_dir().GenSubFil("internet.txt");
    			prog_dlg.Log_wtr().Log_msg_to_url_fmt(session_fil, "download pass: src='~{0}' trg='~{1}'", src_str, xrg.Trg().Raw());
    		}
    		return true;
		}
		catch (Exception exc) {
			xrg.Rslt_err_(exc);
			if 		(ClassAdp_.Eq_typeSafe(exc, java.net.UnknownHostException.class)) 	xrg.Rslt_(IoEngine_xrg_downloadFil.Rslt_fail_host_not_found);
			else if (ClassAdp_.Eq_typeSafe(exc, java.io.FileNotFoundException.class))	xrg.Rslt_(IoEngine_xrg_downloadFil.Rslt_fail_file_not_found);
			else																		xrg.Rslt_(IoEngine_xrg_downloadFil.Rslt_fail_unknown);
			if (prog_dlg != null && !xrg.Prog_cancel()) {
    			if (session_fil == null) session_fil = prog_dlg.Log_wtr().Session_dir().GenSubFil("internet.txt");
    			prog_dlg.Log_wtr().Log_msg_to_url_fmt(session_fil, "download fail: src='~{0}' trg='~{1}' error='~{2}'", src_str, xrg.Trg().Raw(), Err_.Message_lang(exc));
			}
			if (trg_stream != null) {
				try {
					trg_stream.Rls();
					DeleteFil_api(IoEngine_xrg_deleteFil.new_(xrg.Trg()));
				}
				catch (Exception e2) {Err_.Noop(e2);}
			}
			return false;
		}
    	finally {
    		xrg.Prog_running_(false);
    		try {
    			if (src_stream != null) src_stream.close();
    			if (src_conn != null) src_conn.disconnect();
    			src_conn.getInputStream().close();
    		} 	catch (Exception exc) {
    			Err_.Noop(exc);
    		}
    		if (trg_stream != null) trg_stream.Rls();
    	}
	}	Io_url session_fil; Bry_bfr prog_fmt_bfr;
		byte[] download_bfr; static final int Download_bfr_len = Io_mgr.Len_kb * 128;
	public static Err Err_Fil_NotFound(Io_url url) {
		return Err_.new_key_(IoEngineArgs._.Err_FileNotFound, "file not found").Add("url", url.Xto_api()).CallLevel_1_();
	}
	public static Err Err_Fil_NotFound(Exception e, Io_url url) {
		return Err_.err_(e, "file not found").Key_(IoEngineArgs._.Err_FileNotFound).Add("url", url.Xto_api()).CallLevel_1_();
	}
	void MarkFileWritable(File fil, Io_url url, boolean readOnlyFails, String op) {	
		if (Fil_ReadOnly(fil)) {
			if (readOnlyFails)	// NOTE: java will always allow final files to be deleted; programmer api is responsible for check
				throw Err_.new_key_(IoEngineArgs._.Err_ReadonlyFileNotWritable, "writable operation attempted on readOnly file").Add("op", op).Add("url", url.Xto_api()).CallLevel_1_();
			else
				Fil_Writable(fil);
		}
	}
	void DeleteFil_lang(File fil, Io_url url) {	
		boolean rv = Fil_Delete(fil);
		if (!rv)
			throw Err_.new_key_(IoEngineArgs._.Err_IoException, "file not deleted").Add("url", url.Xto_api());
	}
	IoEngineUtl utl = IoEngineUtl.new_();
	public static IoEngine_system new_() {return new IoEngine_system();} IoEngine_system() {}
	static final String GRP_KEY = "Io_engine";
	public static boolean Web_access_enabled = true;
}
class IoEngineArgs {
	public int		LoadFilStr_BufferSize = 4096 * 256;
	public String	LoadFilStr_Encoding = "UTF-8";
	public String	Err_ReadonlyFileNotWritable = "gplx.ios.ReadonlyFileNotWritable";
	public String	Err_FileNotFound 			= "gplx.ios.FileNotFound";
	public String	Err_IoException				= "gplx.ios.IoException";
	public static final IoEngineArgs _ = new IoEngineArgs();
}
class IoEngine_system_xtn {
	// PATCH.DROID:VerifyError if file.setExecutable is referenced directly in IoEngine_system. However, if placed in separate class
	public static void SetExecutable(java.io.File file, boolean v) 	{file.setExecutable(v);}
	public static void SetWritable(java.io.File file, boolean v) 	{file.setWritable(v);}
}
class Io_download_http {
	public static boolean User_agent_reset_needed = true;
	public static void User_agent_reset() {
		User_agent_reset_needed = false;
		System.setProperty("http.agent", "");	// need to set http.agent to '' in order for "User-agent" to take effect
	}
	public static void Save_to_fsys(IoEngine_xrg_downloadFil xrg) {
		Io_stream_rdr_http rdr = new Io_stream_rdr_http(xrg); 
		IoStream trg_stream = null;
		try {			
			boolean exists = Io_mgr._.ExistsDir(xrg.Trg().OwnerDir());
		    if (!exists)
		    	Io_mgr._.CreateDir(xrg.Trg().OwnerDir());	// dir must exist for OpenStreamWrite; create dir at last possible moment in case stream does not exist.
		    trg_stream = Io_mgr._.OpenStreamWrite(xrg.Trg());
		    byte[] bfr = new byte[Download_bfr_len];
		    rdr.Open();
		    while (rdr.Read(bfr, 0, Download_bfr_len) != Read_done) {		    	
		    }
		}
		finally {
			rdr.Rls();
			if (trg_stream != null) trg_stream.Rls();
		}
		if (xrg.Rslt() != IoEngine_xrg_downloadFil.Rslt_pass)
			Io_mgr._.DeleteFil_args(xrg.Trg()).MissingFails_off().Exec();
	}
	public static final int Read_done = -1;
	public static final int Download_bfr_len = Io_mgr.Len_kb * 128;
}
class Io_stream_rdr_http implements Io_stream_rdr {
	public Io_stream_rdr_http(IoEngine_xrg_downloadFil xrg) {
		this.xrg = xrg;
	}	private IoEngine_xrg_downloadFil xrg;
	public byte Tid() {return Io_stream_.Tid_file;}
	public Io_url Url() {return url;} public Io_stream_rdr Url_(Io_url v) {url = v; return this;} private Io_url url;
	public long Len() {return len;} public Io_stream_rdr Len_(long v) {len = v; return this;} private long len = IoItmFil.Size_Invalid;	// NOTE: must default size to -1; DATE:2014-06-21	
	private String src_str; private HttpURLConnection src_conn; private java.io.BufferedInputStream src_stream;
	private Io_download_fmt xfer_fmt; private Gfo_usr_dlg prog_dlg;
	private boolean read_done = true, read_failed = false;
	public Io_stream_rdr Open() {
		if (Io_download_http.User_agent_reset_needed) Io_download_http.User_agent_reset();
		if (!IoEngine_system.Web_access_enabled) {
			read_done = read_failed = true;
			if (prog_dlg != null)
				prog_dlg.Log_wtr().Log_msg_to_url_fmt(session_fil, "download disabled: src='~{0}' trg='~{1}'", xrg.Src(), xrg.Trg().Raw());
			return this;
		}
		src_str = xrg.Src();
		xfer_fmt = xrg.Download_fmt(); prog_dlg = xfer_fmt.usr_dlg;
		try {
			src_conn = (HttpURLConnection)new java.net.URL(src_str).openConnection();
			String user_agent = xrg.User_agent();
			if (user_agent != null)
				src_conn.setRequestProperty("User-Agent", user_agent);
//			src_conn.setReadTimeout(5000);	// do not set; if file does not exist, will wait 5 seconds before timing out; want to fail immediately
			long content_length = Long_.parse_or_(src_conn.getHeaderField("Content-Length"), IoItmFil.Size_Invalid_int);
			xrg.Src_content_length_(content_length);
			this.len = content_length;
			if (xrg.Src_last_modified_query())	// NOTE: only files will have last modified (api calls will not); if no last_modified, then src_conn will throw get nullRef; avoid nullRef
				xrg.Src_last_modified_(DateAdp_.unixtime_lcl_ms_(src_conn.getLastModified()));
			if (xrg.Exec_meta_only()) {
				read_done = true;
				return this;
			}
	        read_done = false;
	        src_stream = new java.io.BufferedInputStream(src_conn.getInputStream());
    		xfer_fmt.Bgn(content_length);
		}
		catch (Exception e) {Err_handle(e);}
		return this;
	}
	public void Open_mem(byte[] v) {}
	public Object Under() {return src_stream;}
	public int Read(byte[] bry, int bgn, int len) {
		if (read_done) return Io_download_http.Read_done;
		if (xrg.Prog_cancel()) {read_failed = true; return Io_download_http.Read_done;}
		try {
			int read = src_stream.read(bry, bgn, len); 
			xfer_fmt.Prog(read);
			return read;
		}
		catch (Exception e) {
			Err_handle(e);
			return Io_download_http.Read_done;
		}
	}
	private Io_url session_fil = null;
	private boolean rls_done = false;
	public long Skip(long len) {return 0;}
	public void Rls() {
		if (rls_done) return;
		try {
			read_done = true;
			if (prog_dlg != null) {
				xfer_fmt.Term();
			}
			if (session_fil == null && prog_dlg != null) session_fil = prog_dlg.Log_wtr().Session_dir().GenSubFil("internet.txt");
			if (read_failed) {
			}
			else {
				prog_dlg.Log_wtr().Log_msg_to_url_fmt(session_fil, "download pass: src='~{0}' trg='~{1}'", src_str, xrg.Trg().Raw());
				xrg.Rslt_(IoEngine_xrg_downloadFil.Rslt_pass);
			}
			xrg.Prog_running_(false);
		}
		catch (Exception e) {Err_.Noop(e);}	// ignore close errors; also Err_handle calls Rls() so it would be circular
		finally {
			try {if (src_stream != null) src_stream.close();} 
			catch (Exception e) {Err_.Noop(e);}	// ignore failures when cleaning up
			if (src_conn != null) src_conn.disconnect();
			src_stream = null;
			src_conn = null;
			rls_done = true;
		}
	}
	private void Err_handle(Exception exc) {
		read_done = read_failed = true;
		len = -1;
		xrg.Rslt_err_(exc);
		if 		(ClassAdp_.Eq_typeSafe(exc, java.net.UnknownHostException.class)) 	xrg.Rslt_(IoEngine_xrg_downloadFil.Rslt_fail_host_not_found);
		else if (ClassAdp_.Eq_typeSafe(exc, java.io.FileNotFoundException.class))	xrg.Rslt_(IoEngine_xrg_downloadFil.Rslt_fail_file_not_found);
		else																		xrg.Rslt_(IoEngine_xrg_downloadFil.Rslt_fail_unknown);
		if (prog_dlg != null && !xrg.Prog_cancel()) {
			if (session_fil == null) session_fil = prog_dlg.Log_wtr().Session_dir().GenSubFil("internet.txt");
			prog_dlg.Log_wtr().Log_msg_to_url_fmt(session_fil, "download fail: src='~{0}' trg='~{1}' error='~{2}'", src_str, xrg.Trg().Raw(), Err_.Message_lang(exc));
		}
		this.Rls();
	}
}
