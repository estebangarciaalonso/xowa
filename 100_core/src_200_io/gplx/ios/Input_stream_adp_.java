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
public class Input_stream_adp_ {
	public static Input_stream_adp new_stream_(Object stream) {return Input_stream_adp_base.new_(Io_url_.Null, Long_.Neg1, stream);}
	public static final Input_stream_adp Null = Input_stream_adp_null._;
}
class Input_stream_adp_base implements Input_stream_adp {
	protected Input_stream_adp_base() {}
	@gplx.Virtual public Io_url Url() {return url;} Io_url url = Io_url_.Null;
	public int Read(byte[] ary) {return this.Read(ary, 0, ary.length);}
		@gplx.Virtual public Object Under_input_stream() {return stream;} InputStream stream;
	@gplx.Virtual public long Pos() {return pos;} private long pos = 0;
	@gplx.Virtual public long Len() {return len;} private long len = 0;
	@gplx.Virtual public int Read(byte[] array, int offset, int count) {
		try {
			int read = stream.read(array, offset, count);
			pos += read;
			return read;
		}
		catch (IOException e) {throw Err_.err_key_(e, IoEngineArgs._.Err_IoException, "read failed");}
	}
	@gplx.Virtual public void Rls() {
		try {stream.close();}
		catch (IOException e) {throw Err_.err_key_(e, IoEngineArgs._.Err_IoException, "rls failed");}
	}
	@gplx.Virtual public long Seek(long v) {
		try {
			long new_pos = stream.skip(pos);
			this.pos = new_pos;
			return new_pos;
		}
		catch (IOException e) {throw Err_.err_key_(e, IoEngineArgs._.Err_IoException, "skip failed");}		
	}
	public static Input_stream_adp_base new_(Io_url url, long len, Object stream) {
		Input_stream_adp_base rv = new Input_stream_adp_base();
		rv.url = Io_url_.Null;
		rv.len = len;
		rv.stream = (java.io.InputStream)stream;
		return rv;
	}
	}
class Input_stream_adp_null implements Input_stream_adp {
	public Object Under_input_stream() {return null;}
	public Io_url Url() {return Io_url_.Null;}
	public long Pos() {return -1;}
	public long Len() {return -1;}
	public int Read(byte[] bry) {return 0;}
	public int Read(byte[] bry, int bgn, int len) {return -1;}
	public long Seek(long pos) {return 0;}
	public void Rls() {}
        public static final Input_stream_adp_null _ = new Input_stream_adp_null(); Input_stream_adp_null() {}
}