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
package gplx.dbs;
import gplx.*;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.sql.*;
public class SqliteDbMain {
	public static void main(String[] args) throws Exception {
		SqliteDbMain main = new SqliteDbMain();
		main.Read();
//		main.Mass_upload(Io_url_.new_dir_("J:\\gplx\\xowl\\file\\all#meta\\en.wikipedia.org\\"));
	}
	Connection conn; PreparedStatement stmt;
	void Read() {
		try {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:J:\\gplx\\xowl\\file\\all#meta\\en.wikipedia.org\\meta.db");
		Statement stat = conn.createStatement();
		
//		stat.executeUpdate("DROP TABLE temp;");
//		stat.executeUpdate("CREATE TABLE temp (ttl varchar(1024));");
//		PreparedStatement prep = conn.prepareStatement("INSERT INTO temp VALUES (?);");
//		conn.setAutoCommit(false);
//		prep.setString(1, "Rembrandt auto 1627.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt van Rijn 184.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt laughing.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt van Rijn 199.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt Harmensz. van Rijn 144.jpg"); prep.addBatch();
//		prep.setString(1, "Self-portrait at 34 by Rembrandt.jpg"); prep.addBatch();
//		prep.setString(1, "Selfportrait Rembrandt1641.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt Harmensz. van Rijn 127b.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt Harmensz. van Rijn 132.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt - Self Portrait111.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt self portrait.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrant Self-Portrait, 1660.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt van rijn-self portrait.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt, Auto-portrait, 1660.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt van Rijn 142 version 02.jpg"); prep.addBatch();
//		prep.setString(1, "Rembrandt Harmensz. van Rijn 135.jpg"); prep.addBatch();
//		prep.executeBatch();
//		conn.setAutoCommit(true);
//		ResultSet rs = stat.executeQuery("SELECT TOP 10 files.* FROM files JOIN temp ON files.ttl = temp.ttl;");
//		ResultSet rs = stat.executeQuery("SELECT files.* FROM files LIMIT 100;");
		ResultSet rs = stat.executeQuery("SELECT files.* FROM files WHERE ttl IN ('380CHANGI.jpg', '20120523Palmen_Hockenheim1.jpg') ;");
		while (rs.next()) {
		  System.out.println("ttl = " + rs.getString("ttl") + "; orig_w = " + rs.getString("orig_w") + "; orig_h = " + rs.getString("orig_h"));
		}
		rs.close();		
		}catch(Exception e) {
			Err_.Noop(e);
		}
	}
	void Index() {
		try {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:J:\\gplx\\xowl\\file\\all#meta\\en.wikipedia.org\\meta.db");
		Statement stat = conn.createStatement();
		stat.executeUpdate("PRAGMA synchronous=OFF");
		stat.executeUpdate("PRAGMA count_changes=OFF");
		stat.executeUpdate("PRAGMA journal_mode=MEMORY");
		stat.executeUpdate("PRAGMA temp_store=MEMORY");
		conn.setAutoCommit(false);
		stat.executeUpdate("CREATE INDEX files_ndx ON files (ttl);");
		conn.commit();
		conn.setAutoCommit(true);
		}catch(Exception e) {
			Err_.Noop(e);
		}
	}
	void Mass_upload(Io_url dir) {
		try {
		Class.forName("org.sqlite.JDBC");
		conn = DriverManager.getConnection("jdbc:sqlite:J:\\gplx\\xowl\\file\\all#meta\\en.wikipedia.org\\meta.db");
		Statement stat = conn.createStatement();
		stat.executeUpdate("drop table if exists files;");
		String sql = String_.Concat_lines_nl
		(	"CREATE TABLE files"
		,	"(	ttl				varchar(1024)"
		,	",	redirect		varchar(1024)"
		,	",	ext				int"
		,	",	orig_mode		int"
		,	",	orig_w			int"
		,	",	orig_h			int"
		,	",	thumbs			varchar(2048)"		// assuming 10 bytes per thumb, roughly 200 thumbs
		,	");"
		);
		stat.executeUpdate(sql);
	
		ConsoleAdp._.WriteLine(DateAdp_.Now().XtoStr_fmt_yyyyMMdd_HHmmss_fff());
//		stat.executeUpdate("BEGIN TRANSACTION");
		stat.executeUpdate("PRAGMA synchronous=OFF");
		stat.executeUpdate("PRAGMA count_changes=OFF");
		stat.executeUpdate("PRAGMA journal_mode=MEMORY");
		stat.executeUpdate("PRAGMA temp_store=MEMORY");
		conn.setAutoCommit(false);
		stmt = conn.prepareStatement("insert into files values (?, ?, ?, ?, ?, ?, ?);");
		Iterate_dir(dir);
//		stat.executeUpdate("COMMIT TRANSACTION");
		stmt.executeBatch();
		conn.commit();
		conn.setAutoCommit(true);
		}catch(Exception e) {
			Err_.Noop(e);
		}
	}
	void Iterate_dir(Io_url dir) {
		Io_url[] urls = Io_mgr._.QueryDir_args(dir).DirInclude_().ExecAsUrlAry();
		int urls_len = urls.length;
		ConsoleAdp._.WriteLine(dir.Raw());
		boolean is_root = false;
		for (int i = 0; i < urls_len; i++) {
			Io_url url = urls[i];
			if (url.Type_dir())
				Iterate_dir(url);
			else {
				try {
					is_root = true;
					Insert_file(url);
				}catch(Exception e) {
					Err_.Noop(e);
				}
			}
		}
		try {
			if (is_root) {
				stmt.executeBatch();
				stmt.clearBatch();
			}			
		}catch(Exception e) {
			Err_.Noop(e);
		}
	}
	void Insert_file(Io_url url) {
		if (String_.EqNot(url.Ext(), ".csv")) return;
		String raw = Io_mgr._.LoadFilStr(url);
		String[] lines = String_.SplitLines_nl(raw);
		int lines_len = lines.length;
		for (int i = 0; i < lines_len; i++) {
			String line = lines[i];
			Insert_line(line);
		}
	}
	void Insert_line(String line) {
		try {
		String[] flds = String_.Split(line, '|');
		int flds_len = flds.length;
		if (flds_len == 0) return;
		stmt.setString(1, flds[2]);
		if (flds_len == 4)
			stmt.setString(2, flds[3]);
		if (flds_len > 4) {
			stmt.setInt(3, ByteAry_.new_ascii_(flds[3])[0] - 32);
			byte[] orig = ByteAry_.new_ascii_(flds[4]);
			int orig_mode = orig[0] - Byte_ascii.Num_0;
			int comma_pos = ByteAry_.FindFwd(orig, Byte_ascii.Comma);
			int orig_w = ByteAry_.XtoIntByPos(orig, 2, comma_pos, -1);
			int orig_h = ByteAry_.XtoIntByPos(orig, comma_pos + 1, orig.length, -1);
			stmt.setInt(4, orig_mode);			
			stmt.setInt(5, orig_w);			
			stmt.setInt(6, orig_h);
			if (flds_len > 5)
				stmt.setString(7, flds[4]);
		}
		stmt.addBatch();
		}catch(Exception e) {
			Err_.Noop(e);
		}
	}
}