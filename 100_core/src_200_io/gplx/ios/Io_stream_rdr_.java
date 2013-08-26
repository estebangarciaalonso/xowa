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
package gplx.ios; import gplx.*;
public class Io_stream_rdr_ {
	public static Io_stream_rdr file_(Io_url url)		{return new Io_stream_rdr_file().Src_url_(url);}
	public static Io_stream_rdr zip_(Io_url url)		{return new Io_stream_rdr_zip().Src_url_(url);}
	public static Io_stream_rdr gzip_(Io_url url)		{return new Io_stream_rdr_gzip().Src_url_(url);}
	public static Io_stream_rdr bzip2_(Io_url url)		{return new Io_stream_rdr_bzip2().Src_url_(url);}
	public static Io_stream_rdr new_by_url_(Io_url url) {
		String ext = url.Ext();
		if		(String_.Eq(ext, Io_stream_.Ext_zip)) 	return gplx.ios.Io_stream_rdr_.zip_(url);
		else if	(String_.Eq(ext, Io_stream_.Ext_gz)) 	return gplx.ios.Io_stream_rdr_.gzip_(url);
		else if	(String_.Eq(ext, Io_stream_.Ext_bz2)) 	return gplx.ios.Io_stream_rdr_.bzip2_(url);
		else 											return gplx.ios.Io_stream_rdr_.file_(url);
	}
	public static Io_stream_rdr new_by_tid_(byte tid) {
		switch (tid) {
			case Io_stream_.Tid_file:					return new Io_stream_rdr_file();
			case Io_stream_.Tid_zip: 					return new Io_stream_rdr_zip();
			case Io_stream_.Tid_gzip: 					return new Io_stream_rdr_gzip();
			case Io_stream_.Tid_bzip2: 					return new Io_stream_rdr_bzip2();
			default:									throw Err_.unhandled(tid);
		}
	}
	public static byte[] Load_all(Io_url url) {
		Io_stream_rdr rdr = new_by_url_(url);
		ByteAryBfr rv = ByteAryBfr.new_();
		try {
			rdr.Open();
			return Load_all(rdr, rv);
		}
		finally {rdr.Rls();}
	}
	public static byte[] Load_all(Io_stream_rdr rdr, ByteAryBfr rv) {
		byte[] bry = new byte[4096];
		while (true) {
			int read = rdr.Read(bry, 0, 4096);
			if (read == gplx.ios.Io_stream_rdr_.Read_done) break; 
			rv.Add_mid(bry, 0, read);
		}
		return rv.XtoAryAndClear();
	}
	public static final int Read_done = -1;
}
abstract class Io_stream_rdr_base implements Io_stream_rdr {
	java.io.InputStream stream;
	public abstract byte Tid();
	public Io_url Src_url() {return src_url;} public Io_stream_rdr Src_url_(Io_url v) {this.src_url = v; return this;} Io_url src_url;
	public void Open_mem(byte[] v) {
		stream = Wrap_stream(new java.io.ByteArrayInputStream(v));
	}
	public Io_stream_rdr Open() {
		try {stream = Wrap_stream(new java.io.FileInputStream(src_url.Xto_api()));}
		catch (Exception exc) {throw Err_.new_fmt_("open failed: url={0}", src_url.Xto_api());}
		return this;
	}
	public int Read(byte[] bry, int bgn, int len) {
		try {return stream.read(bry, bgn, len);}
		catch (Exception exc) {throw Err_.new_fmt_("read failed: bgn={0} len={1}", bgn, len);}
	}
	public long Skip(long len) {		
		try {return stream.skip(len);}
		catch (Exception exc) {throw Err_.new_fmt_("skip failed: len={0}", len);}
	}
	public void Rls() {
		try {stream.close();}
		catch (Exception e) {throw Err_.new_fmt_("close failed: url={0}", src_url.Xto_api());}
	}
	public abstract java.io.InputStream Wrap_stream(java.io.InputStream stream);
}
class Io_stream_rdr_file extends Io_stream_rdr_base {
	@Override public byte Tid() {return Io_stream_.Tid_file;}
	public Io_stream_rdr Open() {
		try {
			if (!Io_mgr._.Exists(src_url))
				stream = Wrap_stream(new java.io.ByteArrayInputStream(ByteAry_.Empty));
			else {
				if (src_url.Info().EngineKey() == gplx.ios.IoEngine_.MemKey)
					stream = Wrap_stream(new java.io.ByteArrayInputStream(Io_mgr._.LoadFilByt(src_url.Xto_api())));
				else
					stream = Wrap_stream(new java.io.FileInputStream(src_url.Xto_api()));
			}
		}
		catch (Exception exc) {throw Err_.new_fmt_("open failed: url={0}", src_url.Xto_api());}
		return this;
	}
	@Override public java.io.InputStream Wrap_stream(java.io.InputStream stream) {return stream;}
}
class Io_stream_rdr_zip implements Io_stream_rdr {
	@Override public byte Tid() {return Io_stream_.Tid_zip;}
	public Io_url Src_url() {return src_url;} public Io_stream_rdr Src_url_(Io_url v) {this.src_url = v; return this;} Io_url src_url;
	java.util.zip.ZipInputStream zip_stream;
	public void Src_bfr_(ByteAryBfr v) {this.src_bfr = v;} ByteAryBfr src_bfr;
	public void Open_mem(byte[] v) {
		Wrap_stream(new java.io.ByteArrayInputStream(v));
	}
	public Io_stream_rdr Open() {
		try {Wrap_stream(new java.io.FileInputStream(src_url.Xto_api()));}
		catch (Exception exc) {throw Err_.new_fmt_("open failed: url={0}", src_url.Xto_api());}
		return this;
	}
	void Wrap_stream(java.io.InputStream input_stream) {zip_stream = new java.util.zip.ZipInputStream(input_stream);}
	public int Read(byte[] bry, int bgn, int len) {
		try {
			while (true){ 
				int read = zip_stream.read(bry, bgn, len);
				if (read == gplx.ios.Io_stream_rdr_.Read_done) {
					if (zip_stream.getNextEntry() == null)
						return gplx.ios.Io_stream_rdr_.Read_done;
				}
				else
					return read;
			}
		}
		catch (Exception exc) {throw Err_.new_fmt_("read failed: bgn={0} len={1}", bgn, len);}
	}
	public long Skip(long len) {		
		try {return zip_stream.skip(len);}
		catch (Exception exc) {throw Err_.new_fmt_("skip failed: len={0}", len);}
	}
	public void Rls() {
		try {zip_stream.close();}
		catch (Exception e) {throw Err_.new_fmt_("close failed: url={0}", src_url.Xto_api());}
	}
}
class Io_stream_rdr_gzip extends Io_stream_rdr_base {
	@Override public byte Tid() {return Io_stream_.Tid_gzip;}
	@Override public int Read(byte[] bry, int bgn, int len) {
		try {
			int total_read = 0;
			while (true) {  // NOTE: the gz stream reads partially; (request 100; only get back 10); keep reading until entire bfr is full or -1
				int read = stream.read(bry, bgn, len);
				if (read == gplx.ios.Io_stream_rdr_.Read_done) break;
				total_read += read;
				if (total_read >= len) break;  // entire bfr full; stop
				bgn += read;  // increase bgn by amount read
				len -= read;  // decrease len by amount read 
			}
			return total_read == 0 ? gplx.ios.Io_stream_rdr_.Read_done : total_read;	// gzip seems to allow 0 bytes read (bz2 and zip return -1 instead); normalize return to -1;
		}
		catch (Exception exc) {
			throw Err_.new_fmt_("read failed: bgn={0} len={1}", bgn, len);
		}
	}
	@Override public java.io.InputStream Wrap_stream(java.io.InputStream stream) {
		try {return new java.util.zip.GZIPInputStream(stream);}
		catch (Exception exc) {throw Err_.new_fmt_("failed to open gz stream");}
	}
}
class Io_stream_rdr_bzip2 extends Io_stream_rdr_base {
	@Override public byte Tid() {return Io_stream_.Tid_bzip2;}
	@Override public java.io.InputStream Wrap_stream(java.io.InputStream stream) {
		try {return new org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream(stream, true);}
		catch (Exception exc) {throw Err_.new_fmt_("failed to open bzip2 stream");}
	}
	@Override public int Read(byte[] bry, int bgn, int len) {
		try {
			int rv = 0;
			int end = bgn + len;
			for (int i = bgn; i < end; i += tmp_len) {
				int bry_len = tmp_len;
				if (i + bry_len > end)
					bry_len = end - i;
				int read = stream.read(bry, i, bry_len);
//				ByteAry_.Copy_pos(tmp, 0, read, bry, i);
				rv += read;
			}
			return rv;
		}
		catch (Exception exc) {
			throw Err_.new_fmt_("read failed: bgn={0} len={1}", bgn, len);
		}
	}
	static final int tmp_len = Io_mgr.Len_kb * 8;
}
