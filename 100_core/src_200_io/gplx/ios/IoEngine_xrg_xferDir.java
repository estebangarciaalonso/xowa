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
import gplx.core.criterias.*;
public class IoEngine_xrg_xferDir {
	public boolean Type_move() {return move;} public boolean Type_copy() {return !move;} private boolean move = false;
	public Io_url Src() {return src;} public IoEngine_xrg_xferDir Src_(Io_url val) {src = val; return this;} Io_url src;
	public Io_url Trg() {return trg;} public IoEngine_xrg_xferDir Trg_(Io_url val) {trg = val; return this;} Io_url trg;
	public boolean Recur() {return recur;} public IoEngine_xrg_xferDir Recur_() {recur = true; return this;} private boolean recur = false;
	public boolean Overwrite() {return overwrite;} public IoEngine_xrg_xferDir Overwrite_() {return Overwrite_(true);} public IoEngine_xrg_xferDir Overwrite_(boolean v) {overwrite = v; return this;} private boolean overwrite = false;
	public boolean ReadOnlyFails() {return readOnlyFails;} public IoEngine_xrg_xferDir ReadOnlyFails_() {return ReadOnlyFails_(true);} public IoEngine_xrg_xferDir ReadOnlyFails_(boolean v) {readOnlyFails = v; return this;} private boolean readOnlyFails = false;
	public Criteria MatchCrt() {return matchCrt;} public IoEngine_xrg_xferDir MatchCrt_(Criteria v) {matchCrt = v; return this;} Criteria matchCrt = Criteria_.All;
	public Criteria SubDirScanCrt() {return subDirScanCrt;} public IoEngine_xrg_xferDir SubDirScanCrt_(Criteria v) {subDirScanCrt = v; return this;} Criteria subDirScanCrt = Criteria_.All;
	public void Exec() {IoEnginePool._.Fetch(src.Info().EngineKey()).XferDir(this);}
	public static IoEngine_xrg_xferDir move_(Io_url src, Io_url trg) {return new_(src, trg, true);}
	public static IoEngine_xrg_xferDir copy_(Io_url src, Io_url trg) {return new_(src, trg, false);}
	static IoEngine_xrg_xferDir new_(Io_url src, Io_url trg, boolean move) {
		IoEngine_xrg_xferDir rv = new IoEngine_xrg_xferDir();
		rv.src = src; rv.trg = trg; rv.move = move;
		return rv;
	}	IoEngine_xrg_xferDir() {}
}
