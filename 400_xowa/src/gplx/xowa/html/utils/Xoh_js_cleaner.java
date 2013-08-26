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
package gplx.xowa.html.utils; import gplx.*; import gplx.xowa.*; import gplx.xowa.html.*;
public class Xoh_js_cleaner {
	boolean ctor = true;
	public byte[] Clean(Xow_wiki wiki, byte[] src, int bgn, int end) {
		if (ctor) Ctor();
		ByteAryBfr bfr = null;
		boolean dirty = false;
		try {
			bfr = wiki.Utl_bry_bfr_mkr().Get_m001();
			int pos = bgn;
			while (pos < end) {
				byte b = src[pos];
				Object o = trie.Match(b, src, pos, end);
				if (o == null) {
					if (dirty)
						bfr.Add_byte(b);
					++pos;
				}
				else {					
					byte[] frag = (byte[])o;
					int frag_len = frag.length;
					if (frag[0] == Byte_ascii.Lt) {	// jscript node; EX: <script
						if (!dirty) {bfr.Add_mid(src, bgn, pos); dirty = true;}
						bfr.Add(gplx.html.Html_entities.Lt);
						bfr.Add_mid(frag, 1, frag.length);
						pos += frag_len; 
					}
					else {	// jscript attribue; EX: onmouseover
						boolean next_byte_is_equal = false; boolean break_loop = false;
						int atr_pos = pos + frag_len;
						for (; atr_pos < end; atr_pos++) {
							byte atr_b = src[atr_pos];
							switch (atr_b) {
								case Byte_ascii.Tab: case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn: case Byte_ascii.Space: break;
								case Byte_ascii.Eq:
									next_byte_is_equal = true;
									++atr_pos;
									break_loop = true;
									break;
								default:
									break_loop = true;
									break;
							}
							if (break_loop) break;
						}
						if (next_byte_is_equal) {
							if (!dirty) {bfr.Add_mid(src, bgn, pos); dirty = true;}
							bfr.Add(frag);
							bfr.Add(gplx.html.Html_entities.Eq);
							pos = atr_pos;
						}
						else {
							pos += frag_len;
						}
					}
				}
			}
		}	finally {if (bfr != null) bfr.Mkr_rls();}
		return dirty ? bfr.XtoAryAndClear() : null;
	}
	void Ctor() {
		Reg_itm("<script");
		Reg_itm("<iframe");
		Reg_itm("<style");
		Reg_itm("<link");
		Reg_itm("<meta");
		Reg_itm("<Object");
		Reg_itm("<frame");
		Reg_itm("<embed");
		Reg_itm("<body");
		Reg_itm("FSCommand");
		Reg_itm("onAbort");
		Reg_itm("onActivate");
		Reg_itm("onAfterPrint");
		Reg_itm("onAfterUpdate");
		Reg_itm("onBeforeActivate");
		Reg_itm("onBeforeCopy");
		Reg_itm("onBeforeCut");
		Reg_itm("onBeforeDeactivate");
		Reg_itm("onBeforeEditFocus");
		Reg_itm("onBeforePaste");
		Reg_itm("onBeforePrint");
		Reg_itm("onBeforeUnload");
		Reg_itm("onBegin");
		Reg_itm("onBlur");
		Reg_itm("onBounce");
		Reg_itm("onCellChange");
		Reg_itm("onChange");
		Reg_itm("onClick");
		Reg_itm("onContextMenu");
		Reg_itm("onControlSelect");
		Reg_itm("onCopy");
		Reg_itm("onCut");
		Reg_itm("onDataAvailable");
		Reg_itm("onDataSetChanged");
		Reg_itm("onDataSetComplete");
		Reg_itm("onDblClick");
		Reg_itm("onDeactivate");
		Reg_itm("onDrag");
		Reg_itm("onDragEnd");
		Reg_itm("onDragLeave");
		Reg_itm("onDragEnter");
		Reg_itm("onDragOver");
		Reg_itm("onDragDrop");
		Reg_itm("onDrop");
		Reg_itm("onEnd");
		Reg_itm("onError");
		Reg_itm("onErrorUpdate");
		Reg_itm("onFilterChange");
		Reg_itm("onFinish");
		Reg_itm("onFocus");
		Reg_itm("onFocusIn");
		Reg_itm("onFocusOut");
		Reg_itm("onHelp");
		Reg_itm("onKeyDown");
		Reg_itm("onKeyPress");
		Reg_itm("onKeyUp");
		Reg_itm("onLayoutComplete");
		Reg_itm("onLoad");
		Reg_itm("onLoseCapture");
		Reg_itm("onMediaComplete");
		Reg_itm("onMediaError");
		Reg_itm("onMouseDown");
		Reg_itm("onMouseEnter");
		Reg_itm("onMouseLeave");
		Reg_itm("onMouseMove");
		Reg_itm("onMouseOut");
		Reg_itm("onMouseOver");
		Reg_itm("onMouseUp");
		Reg_itm("onMouseWheel");
		Reg_itm("onMove");
		Reg_itm("onMoveEnd");
		Reg_itm("onMoveStart");
		Reg_itm("onOutOfSync");
		Reg_itm("onPaste");
		Reg_itm("onPause");
		Reg_itm("onProgress");
		Reg_itm("onPropertyChange");
		Reg_itm("onReadyStateChange");
		Reg_itm("onRepeat");
		Reg_itm("onReset");
		Reg_itm("onResize");
		Reg_itm("onResizeEnd");
		Reg_itm("onResizeStart");
		Reg_itm("onResume");
		Reg_itm("onReverse");
		Reg_itm("onRowsEnter");
		Reg_itm("onRowExit");
		Reg_itm("onRowDelete");
		Reg_itm("onRowInserted");
		Reg_itm("onScroll");
		Reg_itm("onSeek");
		Reg_itm("onSelect");
		Reg_itm("onSelectionChange");
		Reg_itm("onSelectStart");
		Reg_itm("onStart");
		Reg_itm("onStop");
		Reg_itm("onSyncRestored");
		Reg_itm("onSubmit");
		Reg_itm("onTimeError");
		Reg_itm("onTrackChange");
		Reg_itm("onUnload");
		Reg_itm("onURLFlip");
		Reg_itm("seekSegmentTime");
		ctor = false;
	}
	void Reg_itm(String s) {trie.Add_bry(ByteAry_.new_ascii_(s));} ByteTrieMgr_slim trie = new ByteTrieMgr_slim(false);
}
